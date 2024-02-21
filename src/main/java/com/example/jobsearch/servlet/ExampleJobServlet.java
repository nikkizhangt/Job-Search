package com.example.jobsearch.servlet;
import com.example.jobsearch.entity.ExampleCoordinates;
import com.example.jobsearch.entity.ExampleJob;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet(name = "ExampleJobServlet", urlPatterns =  "/example_job")

public class ExampleJobServlet extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        ExampleCoordinates coordinates = new ExampleCoordinates(37.485130, -122.148316);
        ExampleJob job = new ExampleJob("Software Engineer", 123456, "Aug 1, 2020", false, coordinates);
        response.getWriter().print(mapper.writeValueAsString(job));
//        mapper.writeValue(response.getWriter(),job); another way
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
