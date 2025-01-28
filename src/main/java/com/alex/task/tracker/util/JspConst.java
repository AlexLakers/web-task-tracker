package com.alex.task.tracker.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.UtilityClass;

/**
 * This class contains all the paths to jsp-pages in the app.
 */

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum JspConst {
    TASKS("tasks"),
    LOGIN("login"),
    NEW_ACCOUNT_FORM("newAccountForm"),
    NEW_TASK_FORM("newTaskForm"),
    UPDATE_TASK_FORM("updateTaskForm"),
    RESULT("result");
    private static final String JSP_BASE_NAME="/WEB-INF/jsp/%1$s.jsp";
    private final String jspName;

    public String getJspFullName() {
        return JSP_BASE_NAME.formatted(jspName);
    }

}
