package com.example.jobsearch.servlet;

import com.example.jobsearch.db.MySQLConnection;
import com.example.jobsearch.entity.HistoryRequestBody;
import com.example.jobsearch.entity.Item;
import com.example.jobsearch.entity.ResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@WebServlet(name="HistoryServlet", urlPatterns = {"/history"} )
public class HistoryServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            mapper.writeValue(response.getWriter(), new ResultResponse("Session invalid"));
            return;
        }

        HistoryRequestBody body = mapper.readValue(request.getReader(), HistoryRequestBody.class);
        MySQLConnection connection = new MySQLConnection();
        System.out.println("reach here");
        connection.setFavoriteItems(body.userId, body.favorite);
        System.out.println("if not reach here connection problem");
        connection.close();

        ResultResponse resultResponse = new ResultResponse("SUCCESS");
        mapper.writeValue(response.getWriter(), resultResponse);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession(false);
//        if (session == null) {
//            response.setStatus(403);
//            mapper.writeValue(response.getWriter(), new ResultResponse("Session invalid"));
//            return;
//        }
        String userId = request.getParameter("user_id");
        System.out.println("userId is " + userId);
        MySQLConnection connection = new MySQLConnection();
        Set<Item> items = connection.getFavoriteItems(userId);
        connection.close();
        mapper.writeValue(response.getWriter(), items);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            mapper.writeValue(response.getWriter(), new ResultResponse("Session invalid"));
            return;
        }
        HistoryRequestBody body = mapper.readValue(request.getReader(), HistoryRequestBody.class);

        MySQLConnection connection = new MySQLConnection();
        connection.unsetFavoriteItems(body.userId, body.favorite.getId());
        connection.close();

        ResultResponse resultResponse = new ResultResponse("SUCCESS");
        mapper.writeValue(response.getWriter(), resultResponse);

    }

}
