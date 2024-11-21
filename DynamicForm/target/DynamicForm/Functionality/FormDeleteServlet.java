package main.webapp.Functionality;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/FormDeleteServlet")
public class FormDeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        String tableName= req.getParameter("tableName");
        System.out.println(tableName);
        Connection conn = null;
        PreparedStatement ps = null;
        System.out.println(tableName);

        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "vasa");

            System.out.println(tableName);
            String username = (String) req.getSession().getAttribute("username");
            String query = "delete from form_data3 where username ='"+username+"' and"+ " tableName = '"+tableName+"'";

            ps = conn.prepareStatement(query);

            ps.executeUpdate();




            resp.getWriter().write("successful deletion of record");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }

    }
}
