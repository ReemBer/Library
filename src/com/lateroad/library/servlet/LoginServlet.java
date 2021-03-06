package com.lateroad.library.servlet;

import com.lateroad.library.entity.User;
import com.lateroad.library.exception.UserNotFoundException;
import com.lateroad.library.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private UserService userService;

    public LoginServlet() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/signin.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("signin".equals(req.getParameter("action"))) {
            HttpSession session = req.getSession();
            String login = req.getParameter("login");
            String password = req.getParameter("password");

            if (login != null && password != null) {
                User user = null;
                try {
                    user = userService.userGetByEmailAndPassword(login, password);
                } catch (UserNotFoundException e) {
                    e.printStackTrace();
                    //
                }
                if (user != null) {
                    session.setAttribute("PRINCIPAL", user);
                    RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/home.jsp");
                    session.setAttribute("username", user.getLogin());
                    session.setAttribute("inOrOutLabel", "Выйти");
                    session.setAttribute("inOrOut", "out");
                    session.setAttribute("inOrOutAdress", "/logout.html");
                    requestDispatcher.forward(req, resp);
                    return;
                }
            }
            resp.sendRedirect("login.html?loginorpassword=invalid");
        }

    }
}
