package org.snippet;

import java.util.Locale;
import java.util.ResourceBundle;

import static java.nio.charset.StandardCharsets.UTF_8;

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

