package main.webapp.Functionality;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/SubmitFormServlet")
public class SubmitFormServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder jsonData = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonData.append(line);
        }
        System.out.println(jsonData);

        // Parse JSON data
        String jsonString = jsonData.toString();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        String tableName = jsonObject.get("tableName").getAsString();
        JsonArray fields = jsonObject.getAsJsonArray("fields");

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "vasa");


            String query = "INSERT INTO form_data3 (username,tableName, label, value) VALUES (?,?, ?, ?) ON DUPLICATE KEY UPDATE value=values(value)";

            ps = conn.prepareStatement(query);

            for (JsonElement fieldElement : fields) {
                JsonObject field = fieldElement.getAsJsonObject();
                ps.setString(1, (String) request.getSession().getAttribute("username"));
                ps.setString(2, tableName);
                ps.setString(3, field.get("label").getAsString());
                ps.setString(4, field.get("value").getAsString());

                ps.executeUpdate();
            }


            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Data saved successfully\"}");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}

