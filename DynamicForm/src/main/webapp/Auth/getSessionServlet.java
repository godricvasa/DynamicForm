package main.webapp.Auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/getSession")
public class getSessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        String name = (String) session.getAttribute("username");
//        Cookie[] cookies = req.getCookies();
//        String username = "";
//        for(Cookie cookie:cookies){
//            if (cookie.getName().equals("owner")){
//                username=cookie.getValue();
//            }
//
//        }


        resp.getWriter().write(name);
    }
}
