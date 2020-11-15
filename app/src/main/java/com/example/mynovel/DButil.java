package com.example.mynovel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DButil {
    private static final String url = "jdbc:mysql://10.9.209.12:3306/mynovel";
    private static final String user = "root";//用户名
    private static final String password = "15279909969";

    private static DButil instance;
    public static DButil getInstance(){
        if (instance==null){
            instance = new DButil();
        }
        return instance;
    }
    //连接数据库
    public static Connection getSqlConnection(String url,String user,String password){
        Connection con = null;
        try {
            final String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);
            con = (Connection) DriverManager.getConnection(url,user,password);

        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return con;
    }
    //关闭数据库
    public  static void closeAll(Connection conn,PreparedStatement ps){
        if(conn!=null){
            try{
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        if (ps!=null){
            try{
                ps.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    //关闭数据库
    public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


