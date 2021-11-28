package ru.vlsv.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * NotificationService.
 *
 * @author Anatoly Lebedev
 * @version 1.0.0 28.11.2021
 * @link https://github.com/Centnerman
 */

public class PostSender {

    public PostSender() {
    }

    public String send(String address, String message) {
        try {

//            String params = URLEncoder.encode("param1", "UTF-8")
//                    + "=" + URLEncoder.encode("value1", "UTF-8");
//            params += "&" + URLEncoder.encode("param2", "UTF-8")
//                    + "=" + URLEncoder.encode("value2", "UTF-8");

            InetAddress addr = InetAddress.getByName(address);
            int port = 80;
            Socket socket = new Socket(addr, port);
            String path = "/";

            // Send headers
            BufferedWriter wr =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            wr.write("POST " + path + " HTTP/1.0rn");
            wr.write("Content-Length: " + message.length() + "rn");
            wr.write("Content-Type: application/x-www-form-urlencodedrn");
            wr.write("rn");

            // Send parameters
            wr.write(message);
            wr.flush();

            // Get response
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }

            wr.close();
            rd.close();
            return "Message sent by HTML";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Message will not sent by HTML";
    }

}