package org.snippet;

import java.util.ResourceBundle;


/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since
 */
public class ResourceUtil {
    private static final ResourceBundle resourceBundle;

    static {
        resourceBundle = ResourceBundle.getBundle("config");
    }

    public static String getKey(String key) {
        return resourceBundle.getString(key);
    }
}

