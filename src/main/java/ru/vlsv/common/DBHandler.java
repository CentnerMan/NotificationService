package ru.vlsv.common;

import org.sqlite.JDBC;

import java.sql.*;
import java.time.LocalTime;
import java.util.*;

/**
 * NotificationService.
 *
 * @author Anatoly Lebedev
 * @version 1.0.0 28.11.2021
 * @link https://github.com/Centnerman
 */

public class DBHandler {

    private static final String CON_STR = "jdbc:sqlite:commands.db";

    private static DBHandler instance = null;

    public static synchronized DBHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DBHandler();
        return instance;
    }

    private Connection connection;

    private DBHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        // Выполняем подключение к базе данных
        this.connection = DriverManager.getConnection(CON_STR);
        createTableIfNotExist(); // Если нет таблицы - создаем.
    }

    private void createTableIfNotExist() {
        String sql = String.format("CREATE TABLE IF NOT EXISTS commands\n"
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,\n"
                + " external_id TEXT NOT NULL,\n"
                + " message TEXT NOT NULL,\n"
                + " text_time TEXT NOT NULL,\n"
                + " notification_type TEXT NOT NULL,\n"
                + " extra_param TEXT NOT NULL"
                + ")");
        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Command> getAllCommands() {

        try (Statement statement = this.connection.createStatement()) {

            List<Command> commands = new ArrayList<Command>();
            ResultSet resultSet = statement.executeQuery("SELECT external_id, message, " +
                    "text_time, notification_type, extra_param FROM commands");
            // Проходимся по нашему resultSet и заносим данные в commands
            while (resultSet.next()) {
                Command command = new Command();
                command.setExternal_id(resultSet.getString("external_id"));
                command.setMessage(resultSet.getString("message"));
                command.setTime(LocalTime.parse(resultSet.getString("text_time")));
                command.setNotification_type(NotificationType.valueOf(resultSet.getString("notification_type")));
                command.setExtra_params(resultSet.getString("extra_param"));
                commands.add(command);
            }
            // Возвращаем наш список
            return commands;

        } catch (SQLException e) {
            e.printStackTrace();
            // Если произошла ошибка - возвращаем пустую коллекцию
            return Collections.emptyList();
        }
    }

    // Добавление команды в БД
    public void addCommand(Command command) {
        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO commands(`external_id`, `message`, `text_time`, `notification_type`, `extra_param`) " +
                        "VALUES(?, ?, ?, ?, ?)")) {
            statement.setObject(1, command.getExternal_id());
            statement.setObject(2, command.getMessage());
            statement.setObject(3, command.getTime().getHour() + ":" + command.getTime().getMinute());
            statement.setObject(4, command.getNotification_type().toString());
            statement.setObject(5, command.getExtra_params());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Удаление команды по времени
    public void deleteCommand(String time) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM commands WHERE text_time = ?")) {
            statement.setObject(1, time);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}