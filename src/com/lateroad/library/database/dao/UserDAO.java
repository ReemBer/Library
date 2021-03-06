package com.lateroad.library.database.dao;

import com.lateroad.library.database.DBPool;
import com.lateroad.library.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class UserDAO extends AbstractDAO<User> {
    public static final String SQL_SELECT_ALL_USERS = "SELECT * FROM library.user";
    public static final String SQL_FIND_BY_LOGIN = "SELECT * FROM library.user WHERE login=? AND password =?";

    private static UserDAO instance = null;
    private static ReentrantLock lock = new ReentrantLock();
    private static AtomicBoolean instanceCreated = new AtomicBoolean(false);

    public static UserDAO getInstance() {
        if (!instanceCreated.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new UserDAO();
                    instanceCreated.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

//    public User findAbonentByLastName(String name) {
//        User user = new User();
//        Connection cn = null;
//        PreparedStatement st = null;
//        try {
//            cn = DBPool.getConnection();
//            st = cn.prepareStatement(SQL_SELECT_ALL_USERS);
//            ResultSet resultSet = st.executeQuery();
//            resultSet.next();
//            user.setLogin(resultSet.getString("login"));
//            user.setPassword(resultSet.getString("password"));
//            user.setRole(resultSet.getString("role"));
//            user.setMuted(resultSet.getBoolean("is_muted"));
//        } catch (SQLException e) {
//            System.err.println("SQL exception (request or table failed): " + e);
//        } finally {
//            close(st);
//// код возвращения экземпляра Connection в пул
//        }
//        return user;
//    }


    public boolean isExist(String login) {
        return false;
    }

    @Override
    public User find(User wantedUser) {
        DBPool dbPool = DBPool.getInstance();
        User user = null;
        Connection connection = dbPool.getConnection();

        try(PreparedStatement st = connection.prepareStatement(SQL_FIND_BY_LOGIN)){
            st.setString(1, wantedUser.getLogin());
            st.setString(2, wantedUser.getPassword());
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setRole(resultSet.getString("role"));
            }
        } catch (SQLException e) {
            System.err.println("SQL exception (request or table failed): " + e);
        } finally {
            dbPool.putConnection(connection);
        }
       return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        DBPool dbPool = DBPool.getInstance();
        Connection cn = dbPool.getConnection();
        try(Statement st = cn.createStatement();
            ResultSet resultSet = st.executeQuery(SQL_SELECT_ALL_USERS);) {
            while (resultSet.next()) {
                User user = new User();
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("SQL exception (request or table failed): " + e);
        } finally {
            dbPool.putConnection(cn);
        }
        return users;
    }

    @Override
    public void insert(User entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User update(User entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User delete(User entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User delete(int id) {
        throw new UnsupportedOperationException();
    }

}