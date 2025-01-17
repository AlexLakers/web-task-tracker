package com.alex.task.tracker.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;


/**
 * This class contains all the url-path in the app.
 */

@UtilityClass
public class UrlConst {
    public static final String MAIN="/";
    public static final String TASKS="/tasks";
    public static final String TASK_UPDATE="/tasks/update";
    public static final String TASK_DELETE="/tasks/delete";
    public static final String TASK_CREATE="/tasks/create";
    public static final String LOGIN="/login";
    public static final String LOGOUT="/logout";
    public static final String REGISTER="/registration";
}
