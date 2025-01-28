package com.alex.task.tracker.servlet;


import com.alex.task.tracker.dto.AccountResponseDto;
import com.alex.task.tracker.dto.TaskFilterDto;
import com.alex.task.tracker.dto.TaskResponseDto;
import com.alex.task.tracker.entity.enums.TaskPriority;
import com.alex.task.tracker.entity.enums.TaskStatus;
import com.alex.task.tracker.exception.task.TasksFilteringException;
import com.alex.task.tracker.service.AccountService;
import com.alex.task.tracker.service.TaskService;
import com.alex.task.tracker.util.JspConst;
import com.alex.task.tracker.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * This class is a servlet that handle the request from user with URL '/tasks'.
 * It allows you to get all the available tasks by {@link TaskFilterDto filter}.
 * These filter params entered using 'tasks' page by specific account.
 * Then the entered params transmitted as a body part of a request.
 * If this process was going on successfully then the account to sow all tasks by filter.
 * Else - we will see status of response '400'.
 *
 * @see TaskResponseDto task dto.
 * @see TaskService service.
 * @see UrlConst url.
 * @see JspConst jsp.
 */

@Slf4j
@WebServlet("/tasks")
public class TasksServlet extends HttpServlet {
    private final TaskService taskService = TaskService.getInstance();
    private final AccountService accountService = AccountService.getInstance();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //mayme copy finding all the account and availible status,proiporites into other servlet fore actual
        List<AccountResponseDto> accounts = accountService.findAll();
        req.getServletContext().setAttribute("accounts", accounts);
        req.getServletContext().setAttribute("priorities", TaskPriority.values());
        req.getServletContext().setAttribute("status", TaskStatus.values());
        req.getRequestDispatcher(JspConst.TASKS.getJspFullName()).forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var currentAccount = (AccountResponseDto) req.getSession().getAttribute("account");
        var perfId = (req.getParameter("performer") != null)
                ? Long.valueOf(req.getParameter("performer"))
                : currentAccount.getId();

        var filterDto = new TaskFilterDto(
                currentAccount.getId(),
                perfId,
                req.getParameter("creationDate"),
                req.getParameter("deadLineDate"),
                req.getParameter("priority"),
                req.getParameter("status")
        );
        try {
            List<TaskResponseDto> tasks = taskService.findAllBy(filterDto);
            req.setAttribute("tasks", tasks);
            doGet(req, resp);

        } catch (TasksFilteringException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            log.error("The error: {} of search accounts by filter: {}", e, filterDto);

        }

    }
}
