package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable(){
        try (Statement statement = Util.getConnection().createStatement()){
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Users(ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(30), SECONDNAME VARCHAR(45), AGE INT(2))");
        }catch(SQLException ex){
            System.out.println("Ошибка обращения к БД");
        }
    }

    public void dropUsersTable() {
        try(Statement statement = Util.getConnection().createStatement()){
            statement.executeUpdate("DROP TABLE IF EXISTS Users");
            System.out.println("Таблица удалена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = Util.getConnection().prepareStatement("INSERT INTO users(name, secondname, age) VALUES (?,?,?)")) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        }catch(SQLException ex){
            System.out.println("Ошибка записи в БД");
        }
    }

    public void removeUserById(long id) {
        try(PreparedStatement statement = Util.getConnection().prepareStatement("DELETE FROM users WHERE ID = ?")) {
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка удаления по ID");
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try(Statement statement = Util.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка получения данных всех юзеров");
        }
        return users;
    }

    public void cleanUsersTable() {
        try(Statement statement = Util.getConnection().createStatement()){
            statement.executeUpdate("DELETE FROM users");
        } catch (SQLException e) {
            System.out.println("Ошибка очистки таблицы");
        }
    }
}
