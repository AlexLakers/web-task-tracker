package com.alex.task.tracker.servlet;

import com.alex.task.tracker.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * This is a servlet class.
 * It handles the request from user by URL '/logout' for logout current user.
 *
 * @see UrlConst UrlConst
 */

@Slf4j
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var loggedAccount = req.getSession().getAttribute("account");
        if (loggedAccount != null) {
            req.getSession().invalidate();
            log.info("The account:{} is logged out",loggedAccount);
            resp.sendRedirect(UrlConst.LOGIN);
        }

    }
}



