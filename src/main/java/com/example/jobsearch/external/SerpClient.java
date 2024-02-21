package com.example.jobsearch.external;

import com.example.jobsearch.entity.Item;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class SerpClient {
    private static final String URL_TEMPLATE = "https://serpapi.com/search.json?engine=google_jobs&q=%s&uule=%s&api_key=%s";
    private static final String API_KEY = "46f9722aec4504760ba4dd9aff3ab6b3f9f397e8d427e844f3705ca91dbc6fbd";

    private static final String DEFAULT_KEYWORD = "developer";

    public List<Item> search(Double lat, Double lon, String keyword) {
        if (keyword == null) keyword = DEFAULT_KEYWORD;
        try {
            keyword = URLEncoder.encode(keyword,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        GeoConverterClient converterClient = new GeoConverterClient();
        String uuleCode = converterClient.convert(lon, lat);
        String url = String.format(URL_TEMPLATE, keyword, uuleCode, API_KEY);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        ResponseHandler<List<Item>> responseHandler = response -> {
            if (response.getStatusLine().getStatusCode() != 200) {
                return Collections.emptyList();
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return Collections.emptyList();
            }
            ObjectMapper mapper = new ObjectMapper();
            // Different start
            JsonNode root = mapper.readTree(entity.getContent());
            JsonNode results = root.get("jobs_results");
            Iterator<JsonNode> result = results.elements();
            List<Item> items = new ArrayList<>();
            while(result.hasNext()) {
                JsonNode itemNode = result.next();
                Item item = extract(itemNode);
                System.out.println(item.toString());
                items.add(item);
            }
            extractKeywords(items);
            return items;
//            return Arrays.asList(mapper.readValue(entity.getContent(), Item[].class));
        };
        try {
            return httpclient.execute(new HttpGet(url), responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    private static Item extract(JsonNode itemNode) {
        String job_id = itemNode.get("job_id").asText();
        String title = itemNode.get("title").asText();
        String companyName = itemNode.get("company_name").asText();
        String location = itemNode.get("location").asText();
        String via = itemNode.get("via").asText();
        String description = itemNode.get("description").asText();
        List<String> highlights = new ArrayList<String>();
        String url = "";
        Set<String> keywords = new HashSet<>();
        JsonNode highlights_node = itemNode.get("job_highlights");
        Iterator<JsonNode> highlight = highlights_node.elements();
        while (highlight.hasNext()) {
            Iterator<JsonNode> item = highlight.next().get("items").elements();
            while (item.hasNext()) {
                highlights.add(item.next().asText());
            }
        }
        //Get a link for application
        Iterator<JsonNode> url_it = itemNode.get("related_links").elements();
        if (url_it.hasNext()) {
            url = url_it.next().get("link").asText();
        }

        //Store extension(keywords) to keywords
        Iterator<JsonNode> extension_it = itemNode.get("extensions").elements();
        while (extension_it.hasNext()) {
            keywords.add(extension_it.next().asText());
        }
        return new Item(job_id, title, companyName, location, via, description, highlights, url, keywords, false);
    }
    private static void extractKeywords(List<Item> items) {
        EdenAI client = new EdenAI();
        for (Item item: items) {
            String article = item.getDescription() + ". " + String.join(". ", item.getJobHighlights());
            Set<String> keywords = new HashSet<>();
            keywords.addAll(client.extract(article, 3));
            keywords.addAll(item.getKeywords());
            item.setKeywords(keywords);
        }
    }
}
