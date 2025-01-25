package com.alex.task.tracker.servlet;


import com.alex.task.tracker.dto.AccountResponseDto;
import com.alex.task.tracker.dto.TaskUpdatingDto;
import com.alex.task.tracker.entity.PermissionType;
import com.alex.task.tracker.entity.Task;
import com.alex.task.tracker.exception.ServiceException;
import com.alex.task.tracker.exception.access.UnsupportedUpdateOperationException;
import com.alex.task.tracker.service.TaskService;
import com.alex.task.tracker.util.JspConst;
import com.alex.task.tracker.util.UrlConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * This class is a servlet that handle the request from user with URL '/tasks/update'.
 * It starts the updating process for specific task
 * by {@link TaskUpdatingDto dto} transmitted as a body of http-request.
 * If the account has been updated by given input params then occurs setting corresponding attributes in request
 * for redirect to the 'result' page with all the necessary information.
 * Else - occurs send error with status 406 or 500.
 *
 * @see Task task.
 * @see TaskService service.
 * @see UrlConst url.
 * @see JspConst jsp.
 */

@Slf4j
@WebServlet("/tasks/update")
public class UpdateTaskServlet extends HttpServlet {
    private final TaskService taskService = TaskService.getInstance();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var loggedAccountDto = (AccountResponseDto) req.getSession().getAttribute("account");

        var updateTaskDto = TaskUpdatingDto.builder()
                .id(Long.valueOf(req.getParameter("id")))
                .loggedAccountId(loggedAccountDto.getId())
                .performerId(req.getParameter("performer")==null
                        ? null
                        : Long.parseLong(req.getParameter("performer")))
                .deadLineDate(req.getParameter("endDate"))
                .description(req.getParameter("description"))
                .priority(req.getParameter("priority"))
                .status(req.getParameter("status"))
                .build();


        req.setAttribute("taskId", updateTaskDto.id());
        req.setAttribute("action", PermissionType.UPDATE.name());

        try {
            if (taskService.update(updateTaskDto)) {
                log.info("The task with id: {} has been updated successful", updateTaskDto.loggedAccountId());
                req.setAttribute("result", "successfull");
            } else {
                log.info("The updating process for the task with id: {} is failed", updateTaskDto.id());
                req.setAttribute("result", "failed");
            }
            req.getRequestDispatcher(JspConst.RESULT.getJspFullName()).forward(req, resp);
        } catch (ServiceException e) {
            log.error("The updating error", e);
            sendErrorStatusByCausedException(e, resp);
        }

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
   /*     Long.valueOf(req.getParameter("id"))*/
        req.getRequestDispatcher(JspConst.UPDATE_TASK_FORM.getJspFullName()).forward(req, resp);
    }

    @SneakyThrows
    private void sendErrorStatusByCausedException(ServiceException e, HttpServletResponse resp) {
        if (e.getCause() instanceof UnsupportedUpdateOperationException) {
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
