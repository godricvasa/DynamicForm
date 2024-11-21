package main.webapp.Functionality;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

@WebServlet("/DropDownServlet")
public class DropDownServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String filepath = getServletContext().getRealPath("/WEB-INF/form_metadata.json");
        try (FileReader reader = new FileReader(filepath)) {
            JsonObject obj = new Gson().fromJson(reader,JsonObject.class);
            Set<String> set = obj.keySet();
            Gson gson = new Gson();
            System.out.println(set);
            JsonArray responseArray = new JsonArray();

            for(String s:set){
                JsonObject responseObject = new JsonObject();
                responseObject.addProperty("tableName",s);
                responseArray.add(responseObject);
            }

            String name_to_send = gson.toJson(responseArray);
            System.out.println(name_to_send);
            resp.getWriter().write(name_to_send);


//            resp.getWriter().write();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
