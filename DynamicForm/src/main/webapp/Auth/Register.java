    package main.webapp.Auth;




    import com.google.gson.Gson;
    import com.google.gson.JsonArray;
    import com.google.gson.JsonElement;
    import com.google.gson.JsonObject;
    import com.google.gson.stream.JsonWriter;
    import com.mysql.cj.jdbc.Driver;
    import org.json.JSONArray;
    import org.json.JSONObject;

    import javax.crypto.SecretKeyFactory;
    import javax.crypto.spec.PBEKeySpec;
    import javax.servlet.RequestDispatcher;
    import javax.servlet.ServletException;
    import javax.servlet.annotation.WebServlet;
    import javax.servlet.http.HttpServlet;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.*;
    import java.lang.reflect.Array;
    import java.security.NoSuchAlgorithmException;
    import java.security.SecureRandom;
    import java.security.spec.InvalidKeySpecException;
    import java.security.spec.KeySpec;
    import java.sql.*;
    import java.util.Arrays;
    import java.util.Base64;

    @WebServlet("/register")
    public class Register extends HttpServlet {



        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            RequestDispatcher dispatcher = req.getRequestDispatcher("Register.jsp");
            dispatcher.forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            boolean usernameExists = false;
            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
            String username = req.getParameter("username");
            String password = req.getParameter("password");
           if (password.length()==0){
               resp.getWriter().write("password length must be greater than 0");
           }
           else{
               System.out.println(username);
               System.out.println(password);
               Connection conn = null;
               PreparedStatement ps = null;
               ResultSet rs = null;

               try {
                   DriverManager.registerDriver(new Driver());
                   conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db", "root", "vasa");

                   PreparedStatement st = conn.prepareStatement("select username from users");
                   ResultSet r1=st.executeQuery();
                   String usernameCounter;
    //            r1.next();
                   while(r1.next())
                   {

                       usernameCounter =  r1.getString("username");
                       System.out.println(usernameCounter);
                       if(usernameCounter.equals(username)) //this part does not happen even if it should
                       {
                           System.out.println("It already exists");
                           resp.getWriter().write("username already exists try out another username");
                           usernameExists = true;
                       }


                   }
                   if (!usernameExists){

                       PreparedStatement st2 = conn.prepareStatement("INSERT INTO users(username,password) VALUES('"+username+"',"+"'"+password+"')");
                       st2.executeUpdate();

                       System.out.println("success");
                       resp.getWriter().write("user created successfully");
    //                resp.sendRedirect("DynamicForm/login");
                   }




               } catch (SQLException e) {
                   e.printStackTrace();
               } finally {
                   if (rs != null) try {
                       rs.close();
                   } catch (SQLException ignored) {
                   }
                   if (ps != null) try {
                       ps.close();
                   } catch (SQLException ignored) {
                   }
                   if (conn != null) try {
                       conn.close();
                   } catch (SQLException ignored) {
                   }
               }
           }

        }
    }
