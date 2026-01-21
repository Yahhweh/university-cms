package placeholder.organisation.unicms.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "servlet", urlPatterns = "/exa,ple")
public class Servlet extends HttpServlet {

    @Override
    public void init() throws ServletException{
        super.init();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException{
        super.doGet(req, response);
    }

    @Override
    public void doPost(HttpServletRequest req , HttpServletResponse response) throws ServletException, IOException{
        super.doPost(req, response);
    }

}
