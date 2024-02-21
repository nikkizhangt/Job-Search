package com.example.jobsearch.servlet;

import com.example.jobsearch.db.MySQLConnection;
import com.example.jobsearch.entity.Item;
import com.example.jobsearch.entity.ResultResponse;
import com.example.jobsearch.external.SerpClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        HttpSession session = request.getSession(false);
//        if (session == null) {
//            response.setStatus(403);
//            mapper.writeValue(response.getWriter(), new ResultResponse("Session invalid"));
//            return;
//        }
        String userId = request.getParameter("user_id");

        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));

        MySQLConnection connection = new MySQLConnection();
        Set<String> favoriteItemsIds = connection.getFavoriteItemIds(userId);
        connection.close();

        SerpClient client = new SerpClient();

        List<Item> items = client.search(lat, lon, null);
        for (Item item : items) {
            item.setFavorite(favoriteItemsIds.contains(item.getId()));
        }

        mapper.writeValue(response.getWriter(), items);


    }
}
