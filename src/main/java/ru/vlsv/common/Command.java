package ru.vlsv.common;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationService.
 *
 * @author Anatoly Lebedev
 * @version 1.0.0 27.11.2021
 * @link https://github.com/Centnerman
 */

public class Command implements Serializable {
    private String external_id;
    private String message;
    private LocalTime time;
    private NotificationType notification_type;
    private String extra_params;

    public Command(String external_id, String message, LocalTime time,
                   NotificationType notification_type, String extra_params) {
        this.external_id = external_id;
        this.message = message;
        this.time = time;
        this.notification_type = notification_type;
        this.extra_params = extra_params;
    }

    public Command() {
    }

    public static String commandToMsg(Command command) {
        return command.getExternal_id() + "\n" + command.getMessage() + "\n" +
                command.getTime().getHour() + ":" + command.getTime().getMinute() + "\n" +
                command.getNotification_type() + "\n" + command.getExtra_params();
    }

    public String toMsg() {
        return this.getExternal_id() + "\n" + this.getMessage() + "\n" +
                this.getTime().getHour() + ":" + this.getTime().getMinute() + "\n" +
                this.getNotification_type().toString() + "\n" + this.getExtra_params();
    }


    public String[] toDB() {
        String[] strCommand = new String[5];
        strCommand[0] = this.getExternal_id();
        strCommand[1] = this.getMessage();
        strCommand[2] = this.getTime().getHour() + ":" + this.getTime().getMinute();
        strCommand[3] = this.getNotification_type().toString();
        strCommand[4] = this.getExtra_params();
        return strCommand;
    }

    public static Command msgToCommand(String msg) {
        Command resultCommand = new Command();
        String[] inMsg = msg.split("\n");
        resultCommand.setExternal_id(inMsg[0]);
        resultCommand.setMessage(inMsg[1]);
        resultCommand.setTime(LocalTime.parse(inMsg[2]));
        resultCommand.setNotification_type(NotificationType.valueOf(inMsg[3]));
        resultCommand.setExtra_params(inMsg[4]);
        return resultCommand;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public NotificationType getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(NotificationType notification_type) {
        this.notification_type = notification_type;
    }

    public String getExtra_params() {
        return extra_params;
    }

    public void setExtra_params(String extra_params) {
        this.extra_params = extra_params;
    }
}
