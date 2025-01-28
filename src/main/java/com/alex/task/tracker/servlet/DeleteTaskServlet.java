package com.alex.task.tracker.servlet;


import com.alex.task.tracker.dto.AccountResponseDto;
import com.alex.task.tracker.entity.PermissionType;
import com.alex.task.tracker.entity.Task;
import com.alex.task.tracker.exception.ServiceException;
import com.alex.task.tracker.exception.access.UnsupportedDeleteOperationException;
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
import org.glassfish.jaxb.core.v2.TODO;

import java.io.IOException;

//@Log4j

/**
 * This class is a servlet that handle the request from user with URL '/tasks/delete'.
 * It starts the deleting process for specific task by id transmitted as a request param.
 * If the account has been deleted by given id then occurs setting corresponding attributes in request
 * for redirect to the 'result' page with all the necessary information.
 * Else - occurs send error with status 401, 500.
 *
 * @see Task task.
 * @see TaskService service.
 * @see UrlConst url.
 * @see JspConst jsp.
 */

@Slf4j
@WebServlet("/tasks/delete")
public class DeleteTaskServlet extends HttpServlet {
    private final TaskService taskService = TaskService.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var taskId = Long.valueOf(req.getParameter("id"));
        var currentAccount = (AccountResponseDto) req.getSession().getAttribute("account");
        try {
            req.setAttribute("taskId", taskId);
            req.setAttribute("action", PermissionType.DELETE.name());

            if (taskService.delete(taskId, currentAccount.getId())) {
                req.setAttribute("result", "successfull");
                log.info("The task with id: {} has been deleted successful", taskId);

            } else {
                log.info("The removing process for the task with id: {} is failed", taskId);
                req.setAttribute("result", "failed");
            }
            req.getRequestDispatcher(JspConst.RESULT.getJspFullName()).forward(req, resp);

        } catch (ServiceException e) {
            log.error("The deleting error", e);
            sendErrorStatusByCausedException(e, resp);
        }
    }


    @SneakyThrows
    private void sendErrorStatusByCausedException(ServiceException e, HttpServletResponse resp) {
        if (e.getCause() instanceof UnsupportedDeleteOperationException) {
            resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
