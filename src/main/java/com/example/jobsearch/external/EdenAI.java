package com.example.jobsearch.external;

import com.example.jobsearch.entity.ExtractRequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.io.IOException;

public class EdenAI {
    private static final String EDENAI_TOEKN = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiNjM5NTBjYWEtNDQ1Ni00MzgyLTlkOGYtMDczYjg0YzY0MWRjIiwidHlwZSI6ImFwaV90b2tlbiJ9.Xb7UOmroFRpzrOGDzxHQsvjRHr2_tN4ZcZ6QxHTW9ZI";

    private static final String EXTRACT_URL = "https://api.edenai.run/v2/text/keyword_extraction";

    public static void main(String[] args) {
        String s = "Software Engineer(s)\\n\\nSocial Media App (React JS and Node JS only...\\n\\nThis is a part-time, equity-only position at this stage. IF you want to be part of a new start-up establishing a new social media paradigm and use case read on!\\n\\nThis is not position for on the job learning. We want someone who has all the technical answers.\\n\\nThe Venue platform is a digital social platform creating a new paradigm for all commercial communities which involves physical interaction. Examples of such communities include malls, stadiums, resorts, amusement parks, exhibition events, festivals, compounds, etc. We aim to provide owners, operators, and managers with the tools to engage in real-time interaction with visitors, who are provided with a better-engaged experience with targeted real-time information and offerings. The goal is to increase visitor engagement, satisfaction, and transaction outcomes.\\n\\nWe are seeking highly skilled and motivated Software Engineer(s) to join our dynamic team in the development of a cutting-edge social media application. As a Software Engineer, you will play a key role in designing, implementing, and maintaining features for the app, with a primary focus on React JS for the front end and Node JS for the back end. Our initial platform design is available in Figma.\\n\\nResponsibilities:\\n\\nFront-End Development:\\n• Utilize React JS to design and implement interactive user interfaces with a focus on performance and responsiveness.\\n• Collaborate with UX/UI designers to create a visually appealing and user-friendly experience.\\n• Implement state management solutions and integrate with back-end services.\\n\\nBack-End Development:\\n• Develop server-side logic using Node JS to handle user authentication, data storage, and API integrations.\\n• Design and implement RESTful APIs to facilitate communication between the front end and back end.\\n• Collaborate with database administrators to optimize database queries and ensure efficient data storage.\\n\\nFull-Stack Development:\\n• Work on both the front-end and back-end aspects of the application to ensure seamless integration.\\n• Implement security best practices to protect user data and privacy.\\n• Collaborate with cross-functional teams to define and implement new features.\\n\\nCode Quality and Testing:\\n• Write clean, maintainable, and well-documented code.\\n• Conduct thorough testing of both front-end and back-end components to ensure reliability and robustness.\\n• Participate in code reviews to provide constructive feedback and ensure code quality standards are met.\\n\\nPerformance Optimization:\\n• Identify and address performance bottlenecks in both front-end and back-end components.\\n• Implement optimization techniques to enhance the overall speed and responsiveness of the application.\\n\\nCollaboration:\\n• Collaborate with product managers, designers, and other stakeholders to understand requirements and translate them into technical specifications.\\n• Work closely with other team members to ensure a cohesive and integrated development process.\\n\\nRequirements:\\n• Bachelor's degree in Computer Science, Software Engineering, or a related field.\\n• Proven experience as a Software Engineer with a focus on React JS and Node JS.\\n• Strong proficiency in JavaScript, HTML, and CSS.\\n• Experience with front-end frameworks/libraries (e.g., Redux, TypeScript) and back-end frameworks (e.g., Express).\\n• Knowledge of RESTful API design and integration.\\n• Familiarity with database systems such as MongoDB or SQL databases.\\n• Understanding of version control systems, preferably Git.\\n• Excellent problem-solving and communication skills.\\n• Ability to work both independently and collaboratively in a team environment.\\n• Up-to-date knowledge of industry trends and best practices.\\n\\nIf you are passionate about creating innovative and engaging social media experiences and possess the technical skills required for this role, we encourage you to apply and be a part of our exciting journey in building the next generation of social media platforms.\\n\\n2.5% of Newco. Subsequent to funding a full-time role with cash compensation would be possible.\\n\\nIf this opportunity resonates, the next step would be to reach out and we can arrange a call to discuss further";

        EdenAI client = new EdenAI();

        Set<String> keywordSet = client.extract(s, 3);
        System.out.println(keywordSet);
    }

    public Set<String> extract(String article, int keywords_num) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();

        HttpPost request = new HttpPost(EXTRACT_URL); //create POST request
        request.setHeader("Content-type", "application/json");
        request.setHeader("Authorization", EDENAI_TOEKN);
        request.setHeader("accept", "application/json");
        ExtractRequestBody body = new ExtractRequestBody(article); //pass article string to an object

//        System.out.println("POST Created");

        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(body); //write into request body into json
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }

//        System.out.println("Reqeust Body Created");

        try {
            request.setEntity(new StringEntity(jsonBody)); //write json to request body
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }

//        System.out.println("Request Body Written");

        ResponseHandler<Set<String>> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() != 200) { //If not 200OK return empty set
                return Collections.emptySet();
            }
            HttpEntity entity = response.getEntity(); //initiate entity, if entity failed return empty set
            if (entity == null) {
                return Collections.emptySet();
            }
            JsonNode root = mapper.readTree(entity.getContent()); //get content till nlpcloud's items
            JsonNode ibm = root.get("ibm");

            System.out.println(ibm.asText());

            JsonNode ibmitems = ibm.get("items");

            TreeMap<Double, ArrayList<String>> keywords = new TreeMap<>();
            Iterator<JsonNode> itemsIterator = ibmitems.elements();
            //Read Value and store in keywords set
            while (itemsIterator.hasNext()) {
                JsonNode itemNode = itemsIterator.next();
                String keyword = itemNode.get("keyword").asText();
                double importance = itemNode.get("importance").asDouble();
                ArrayList<String> words_list = keywords.getOrDefault(importance, new ArrayList<String>());
                words_list.add(keyword);
                keywords.put(importance, words_list);
            }

            Set<String> refined_set = new HashSet<>();

            while (refined_set.size() < keywords_num && !keywords.isEmpty()) {
                ArrayList<String> words_list = keywords.pollLastEntry().getValue();
                while (!words_list.isEmpty() && refined_set.size() < keywords_num) {
                    refined_set.add(words_list.remove(0));
                }
            }

            return refined_set;
        };

//        System.out.println("Response Handler Created");

        try {
            return httpClient.execute(request, responseHandler); //handle request sent
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("HTTP Failed");

        return Collections.emptySet();
    }
}