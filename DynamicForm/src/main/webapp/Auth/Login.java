package main.webapp.Auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.cj.jdbc.Driver;
import main.webapp.Filters.FilterServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("Login.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            DriverManager.registerDriver(new Driver());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "vasa");

            ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();

            JsonObject responseJson = new JsonObject();

            FilterServlet fs = new FilterServlet();



            if (rs.next()) {
                if (rs.getString("password").equals(password)) {
                    HttpSession session = req.getSession(true);
                    System.out.println("password match");

                    session.setAttribute("username", username);
//                    Cookie cookie = new Cookie("owner",session.getId());
//                    cookie.setMaxAge(7*24*60*60 );
//                    resp.addCookie(cookie);
                    Cookie sessionCookie = new Cookie("JSESSIONID",session.getId());
                    sessionCookie.setMaxAge(7*24*60*60  );
                    sessionCookie.setHttpOnly(true);
                    sessionCookie.setSecure(true);
                    resp.addCookie(sessionCookie);
                    responseJson.addProperty("status", "success");
                    responseJson.addProperty("redirect", req.getContextPath() + "/main");
                    System.out.println("gave good");
                } else {
                    System.out.println("not good");
                    responseJson.addProperty("status", "error");
                    responseJson.addProperty("message", "Invalid password");
                }
            } else {
                responseJson.addProperty("status", "error");
                responseJson.addProperty("message", "User not found");
            }

            resp.getWriter().write(new Gson().toJson(responseJson));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
