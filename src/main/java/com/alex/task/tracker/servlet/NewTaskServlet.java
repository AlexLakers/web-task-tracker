package com.alex.task.tracker.servlet;


import com.alex.task.tracker.dto.AccountResponseDto;
import com.alex.task.tracker.dto.TaskCreationDto;
import com.alex.task.tracker.entity.enums.TaskPriority;
import com.alex.task.tracker.exception.AccountNotExistException;
import com.alex.task.tracker.exception.access.UnsupportedCreateOperationException;
import com.alex.task.tracker.exception.task.TaskCreationException;
import com.alex.task.tracker.exception.task.TaskNotExistException;
import com.alex.task.tracker.service.AccountService;
import com.alex.task.tracker.service.TaskService;
import com.alex.task.tracker.util.JspConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;

/**
 * This class is a servlet that handle the request from user with URL '/tasks/create'.
 * It starts the creation a new task for logged account
 * by {@link TaskCreationDto dto} transmitted into the body param.
 * We have two situation:
 * firstly If the task  has been created successfully the occurs
 * setting the corresponding attributes in request
 * for redirect to the 'result' page with all the necessary information.
 * Else occurs setting status of response as 400.
 */
@Slf4j
@WebServlet("/tasks/create")
public class NewTaskServlet extends HttpServlet {
    private final TaskService taskService = TaskService.getInstance();
    private final AccountService accountService = AccountService.getInstance();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var loggedAccountDto = (AccountResponseDto) req.getSession().getAttribute("account");

        var createTaskDto = TaskCreationDto.builder()
                .name(req.getParameter("name"))
                .loggedAccountId(loggedAccountDto.getId())
                .performerId(Long.valueOf(req.getParameter("performer")))
                .deadLineDate(req.getParameter("endDate"))
                .description(req.getParameter("description"))
                .priority(req.getParameter("priority"))
                .build();

        Long taskId = null;
        try {
            taskId = taskService.create(createTaskDto);
            req.setAttribute("taskId", taskId);
            req.setAttribute("action", "create");
            if (taskId != null) {

                log.info("The task with id:{} has been created successful", taskId);
                req.setAttribute("result", "successfull");
            } else {
                log.info("The creating process of a new task is failed");
                req.setAttribute("result", "failed");
            }
            req.getRequestDispatcher(JspConst.RESULT.getJspFullName()).forward(req, resp);
        } catch (ConstraintViolationException e) {
            req.setAttribute("validErrors", e.getMessage());
            log.warn("The validation errors:{}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            doGet(req, resp);
        } catch (TaskCreationException e) {
            log.error("The creating error", e);
            sendErrorStatusByCausedException(e, resp);
        }

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<AccountResponseDto> accounts = accountService.findAll();
        req.getServletContext().setAttribute("accounts", accounts);
        req.getServletContext().setAttribute("priorities", TaskPriority.values());
        req.getRequestDispatcher(JspConst.NEW_TASK_FORM.getJspFullName()).forward(req, resp);
    }

    @SneakyThrows
    private void sendErrorStatusByCausedException(TaskCreationException e, HttpServletResponse resp) {
        var causedExc = e.getCause();
        if (causedExc instanceof TaskNotExistException
                || causedExc instanceof AccountNotExistException) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else if (causedExc instanceof UnsupportedCreateOperationException) {
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
