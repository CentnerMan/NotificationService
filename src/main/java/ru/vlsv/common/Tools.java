package ru.vlsv.common;

import java.io.File;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * NotificationService.
 *
 * @author Anatoly Lebedev
 * @version 1.0.0 27.11.2021
 * @link https://github.com/Centnerman
 */

public class Tools {
    public static final String SERVER = "localhost";
    public static final int RECEIVER_PORT = 6066;
    public static final int SENDER_PORT = 6077;
    public static final String EMAIL_LOGIN = "mymail@gmail.com";
    public static final String EMAIL_PASSWORD = "password";

    Preferences preferences = Preferences.userRoot();

    Properties properties = System.getProperties();

    public static boolean createDirIfNotExist(String currentPath) {
        boolean result = false;
        File userDir = new File(currentPath);
        if (!userDir.exists()) {
            if (userDir.mkdir()) {
                System.out.println("Папка " + currentPath + " создана");
                result = true;
            } else {
                System.out.println("Ошибка создания папки " + currentPath);
            }
        }
        return result;
    }
}
