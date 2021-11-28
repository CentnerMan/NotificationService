package ru.vlsv.server;

import ru.vlsv.common.Command;
import ru.vlsv.common.DBHandler;
import ru.vlsv.common.Tools;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

/**
 * NotificationService.
 *
 * @author Anatoly Lebedev
 * @version 1.0.0 27.11.2021
 * @link https://github.com/Centnerman
 */

public class ReceiveCommandServer extends Thread {

    int port = Tools.RECEIVER_PORT;

    private final ServerSocket serverSocket;

    public ReceiveCommandServer() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void run() {

        while (true) {
            try {
                System.out.println("Ожидание клиента на порт " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());

                Command command = Command.msgToCommand(in.readUTF());
                out.writeUTF("Задание на " + command.getTime() + " с " + command.getNotification_type().toString()
                        + " отправкой установлено");
                out.flush();

                DBHandler dbHandler = DBHandler.getInstance();
                dbHandler.addCommand(command);
                System.out.println(command.toMsg());

                in.close();
                out.close();
                server.close();

            } catch (SocketTimeoutException s) {
                System.out.println("Время сокета истекло!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
