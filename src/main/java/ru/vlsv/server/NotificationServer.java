package ru.vlsv.server;

import java.io.IOException;

/**
 * NotificationService.
 *
 * @author Anatoly Lebedev
 * @version 1.0.0 27.11.2021
 * @link https://github.com/Centnerman
 */

/*
Нужно написать Notification service.

На сервере крутится программа, слушает TCP порт, принимает команды - напомни мне о чём-то. Умеет напоминать двумя каналами - email и http.

Команда состоит из: external_id (string), message (string), time, norification_type (enum: mail/http) extra_params.

Extra_params для http: url=http://....

Extra_params для mail: email=a@a.ru

Сервер принимает команду, кладёт в очередь. Когда наступает время указанное в напоминании, отправляет его указанным каналом. Типа будильника
 */

public class NotificationServer {

    public static void main(String[] args) {
        try {
            Thread receiver = new ReceiveCommandServer();
            receiver.start();
            Thread sender = new MessageSendServer();
            sender.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
