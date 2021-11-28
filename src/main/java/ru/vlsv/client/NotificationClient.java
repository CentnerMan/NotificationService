package ru.vlsv.client;

import ru.vlsv.common.NotificationType;
import ru.vlsv.common.Tools;

import java.io.*;
import java.net.Socket;

/**
 * NotificationService.
 *
 * @author Anatoly Lebedev
 * @version 1.0.0 27.11.2021
 * @link https://github.com/Centnerman
 */

public class NotificationClient {

    public static void main(String[] args) {

        String serverName = Tools.SERVER;
        int port = Tools.RECEIVER_PORT;

        String httpCommand = "id" + "\n" + "messageHTTP" + "\n" + "15:36" + "\n" + NotificationType.HTTP + "\n" + "url=http://vlsv.ru/";
        String emailCommand = "id2" + "\n" + "messageMail" + "\n" + "22:39" + "\n" + NotificationType.MAIL + "\n" + "email=lebedev.anatoly@mail.ru";

        try {
            System.out.println("Подключение к " + serverName + " на порт " + port);
            Socket client = new Socket(serverName, port);

            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

//            out.writeUTF(httpCommand);
            out.writeUTF(emailCommand);
            System.out.println(in.readUTF());

            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
