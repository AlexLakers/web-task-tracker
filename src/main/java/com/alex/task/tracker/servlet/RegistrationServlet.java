package com.alex.task.tracker.servlet;


import com.alex.task.tracker.dto.AccountCreationDto;
import com.alex.task.tracker.entity.Task;
import com.alex.task.tracker.exception.auth.AccountRegistrationException;
import com.alex.task.tracker.service.AccountService;
import com.alex.task.tracker.util.JspConst;
import com.alex.task.tracker.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

/**
 * This class is a servlet that handle the request from user with URL '/registration'.
 * It starts the registration process for the new  account by {@link AccountCreationDto dto}
 * transmitted into the body and entered using html-form.
 * If the account has been created successfully then occurs redirect to 'login' page.
 * if something went wrong ,for example you try to use invalid dto or another error was detected
 * then occurs setting corresponding http status of response:400,500.
 * by given data then current account added to session as attribute
 * and occurs redirect to 'tasks' page.
 * Else occurs setting status of response as 401.
 *
 * @see Task task.
 * @see UrlConst url.
 */

@Slf4j
@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private final AccountService accountService = AccountService.getInstance();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var createAccountDto = AccountCreationDto.builder()
                .firstName(req.getParameter("firstName"))
                .lastName(req.getParameter("lastName"))
                .login(req.getParameter("email"))
                .birthday(req.getParameter("birthday"))
                .password(req.getParameter("rawPassword"))
                .build();
        try {
            Long id=null;
            if ((id=accountService.createAccount(createAccountDto)) != null) {
                log.debug("A new user with id: {} has been created.",id);
                resp.sendRedirect(UrlConst.LOGIN);
            }
        } catch (ConstraintViolationException e) {
            req.setAttribute("validErrors", e.getMessage());
            log.warn("The validation errors:{}",e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            doGet(req, resp);

        } catch (AccountRegistrationException e) {
            log.error("The registration error:", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher(JspConst.NEW_ACCOUNT_FORM.getJspFullName()).forward(request, response);
    }

}
