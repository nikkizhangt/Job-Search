package com.example.jobsearch.servlet;

import com.example.jobsearch.db.MySQLConnection;
import com.example.jobsearch.entity.LoginRequestBody;
import com.example.jobsearch.entity.LoginResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        LoginRequestBody body = mapper.readValue(request.getReader(), LoginRequestBody.class);
        MySQLConnection connection = new MySQLConnection();
        LoginResponseBody loginResponseBody;
        if (connection.verifyLogin(body.userId, body.password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user_id", body.userId);
            loginResponseBody = new LoginResponseBody("OK", body.userId, connection.getFullname(body.userId));
        } else {
            loginResponseBody = new LoginResponseBody("Login failed, user id and passcode do not exist.", null, null);
            response.setStatus(401);
        }
        connection.close();
        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), loginResponseBody);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
