package main.webapp.Auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/endSession")
public class endSessionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("yup");
        resp.setContentType("text/html");
        HttpSession session = req.getSession();
//        Cookie cookie = new Cookie("owner","");
//        cookie.setMaxAge(0);
//        resp.addCookie(cookie);
        session.invalidate();
        resp.getWriter().write(req.getContextPath() + "/login");
    }
}
