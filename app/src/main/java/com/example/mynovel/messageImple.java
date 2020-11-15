package com.example.mynovel;

import android.util.Log;

import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class messageImple {
    private static final String url = "jdbc:mysql://cdb-h1ubzfre.bj.tencentcdb.com:10129/mynovel?useUnicode=true&characterEncoding=UTF-8";
    private static final String user = "root";//用户名
    private static final String password = "15279909969aqw";

    private Connection conn =null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private static messageImple messageImp;
    public messageImple(){

    }
    public static messageImple getMessageImple(){
        if (messageImp==null){
            messageImp = new messageImple();
        }
        return  messageImp;
    }
    public void insertMessage(userMessage um){
        int result = -1;
        String sql = "insert into reader_message(username,message,toname,tomsg)values(?,?,?,?)";
        conn = DButil.getInstance().getSqlConnection(url,user,password);

        try {
            boolean closed = conn.isClosed();
            if ((conn!=null)&& (!closed)){
                ps = (PreparedStatement)conn.prepareStatement(sql);
                String name = um.getUsername();
                String message = um.getMessage();
                String toname = um.getTouser();
                String tomsg = um.getTomsg();
                ps.setString(1,name);
                ps.setString(2,message);
                ps.setString(3,toname);
                ps.setString(4,tomsg);
                result = ps.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        DButil.closeAll(conn,ps);
    }
    //查询所有留言信息
    public ArrayList<userMessage> getUserMessagedata(){
        ArrayList<userMessage> users = new ArrayList<userMessage>();
        String sql = "select * from reader_message";
        conn =  DButil.getSqlConnection(url,user,password);
        try{
            if (conn!=null && !(conn.isClosed())){
                ps = (PreparedStatement)conn.prepareStatement(sql);
                if (ps!=null){
                    rs = ps.executeQuery();
                    if (rs!=null){
                        while (rs.next()){
                            userMessage u = new userMessage();
                            u.setUsername(rs.getString(1));
                            u.setMessage(rs.getString(2));
                            u.setTouser(rs.getString(3));
                            u.setTomsg(rs.getString(4));
                            users.add(u);
                        }
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        DButil.closeAll(conn,ps,rs);
        return users;
    }
    //删除留言信息
    public int deleteMessage(String username,String msgData){
        int result = -1;
        if (!StringUtils.isEmptyOrWhitespaceOnly(msgData)){
            conn = DButil.getSqlConnection(url,user,password);
            String sql = "delete from reader_message where username=? and message=?";
            try{
                boolean closed = conn.isClosed();
                if((conn!=null)&& (!closed)){
                    ps = (PreparedStatement)conn.prepareStatement(sql);
                    ps.setString(1,username);
                    ps.setString(2,msgData);
                    result = ps.executeUpdate();
                    Log.d("111", "deleteMessage: success");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        DButil.closeAll(conn,ps);
        return result;
    }
}
