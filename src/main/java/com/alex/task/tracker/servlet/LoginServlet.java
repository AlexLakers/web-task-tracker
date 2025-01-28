package com.alex.task.tracker.servlet;

import com.alex.task.tracker.entity.Task;
import com.alex.task.tracker.exception.auth.AccountLoginException;
import com.alex.task.tracker.service.AccountService;
import com.alex.task.tracker.util.JspConst;
import com.alex.task.tracker.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * This class is a servlet that handle the request from user with URL '/login'.
 * It starts the logg in process for specific account by login and pass
 * transmitted into the body and entered using html-form.
 * If the account exist by given data then current account added to session as attribute
 * and occurs redirect to 'tasks' page.
 * Else occurs setting status of response as 401.
 *
 * @see Task task.
 * @see UrlConst url.
 */

@Slf4j
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AccountService accountService = AccountService.getInstance();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var login = req.getParameter("email");
        var pass = req.getParameter("password");
        try {
            var maybeLoggedAccountDto = accountService.loginAccount(login, pass);

            if (maybeLoggedAccountDto.isPresent()) {
                req.getSession().setAttribute("account", maybeLoggedAccountDto.get());
                log.info("The user: {} has been logged in.", maybeLoggedAccountDto.get());
                resp.sendRedirect(UrlConst.TASKS);
            } else {
                req.setAttribute("enteredLogin", login);
                log.warn("Authentication with login [%s]failed".formatted(login));
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                doGet(req, resp);
            }
        } catch (AccountLoginException e) {
            log.warn("Authentication error", e);
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            doGet(req, resp);

        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspConst.LOGIN.getJspFullName()).forward(req, resp);
    }
}
