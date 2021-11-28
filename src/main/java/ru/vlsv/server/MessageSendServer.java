package ru.vlsv.server;

import ru.vlsv.common.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * NotificationService.
 *
 * @author Anatoly Lebedev
 * @version 1.0.0 27.11.2021
 * @link https://github.com/Centnerman
 */

public class MessageSendServer extends Thread {

    int port = Tools.SENDER_PORT;
    final long timeInterval = 30000L; // 30 секунд

    private final ServerSocket serverSocket;

    EmailSender emailSender;
    PostSender postSender;

    public MessageSendServer() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void run() {

        while (true) {
            LocalTime nowTime = LocalTime.now();
            System.out.println(nowTime);

            try {
                DBHandler dbHandler = DBHandler.getInstance();
                int i = 0;
                ArrayList<Command> list = (ArrayList<Command>) dbHandler.getAllCommands();
                while (i < list.size()) {
                    if (list.get(i).getTime().getHour() <= nowTime.getHour() &&
                            list.get(i).getTime().getMinute() <= nowTime.getMinute()) {
                        System.out.println(sendMessage(list.get(i)));

                        dbHandler.deleteCommand(list.get(i).getTime().getHour() + ":" +
                                list.get(i).getTime().getMinute());
                    }
                    i++;
                }
                MessageSendServer.sleep(timeInterval);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String sendMessage(Command command) {
        String result = "Transfer error";
        if (command.getNotification_type().equals(NotificationType.HTTP)) {
            String address = command.getExtra_params().substring(4);
            postSender = new PostSender();
            result = postSender.send(address, command.getMessage());
        }
        if (command.getNotification_type().equals(NotificationType.MAIL)) {
            String address = command.getExtra_params().substring(6);
            emailSender = new EmailSender(Tools.EMAIL_LOGIN, Tools.EMAIL_PASSWORD);
            result = emailSender.send(Tools.EMAIL_LOGIN,
                    address, "Напоминалка", command.getMessage());
        }
        return result;
    }
}
