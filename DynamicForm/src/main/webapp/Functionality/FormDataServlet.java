package main.webapp.Functionality;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.jdbc.Driver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/FormDataServlet")
public class FormDataServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String tableName = request.getParameter("tableName");
        List<JsonObject> formFields = new ArrayList<>();
        System.out.println(tableName);


        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            DriverManager.registerDriver(new Driver ());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "vasa");

            String query = "SELECT label, value FROM form_data3 WHERE username=? and tableName = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, (String) request.getSession().getAttribute("username"));
            ps.setString(2, tableName);
            rs = ps.executeQuery();

            while (rs.next()) {
                JsonObject field = new JsonObject();
                field.addProperty("label", rs.getString("label"));
                field.addProperty("data_type", "text");
                field.addProperty("value", rs.getString("value"));
                formFields.add(field);
            }

            Gson gson = new Gson();
            String jsonResponse = gson.toJson(formFields);
            response.getWriter().write(jsonResponse);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
    }

}

