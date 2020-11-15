package com.example.mynovel;

import android.util.Log;

import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class readerImple {
    private static final String url ="jdbc:mysql://cdb-h1ubzfre.bj.tencentcdb.com:10129/mynovel?useUnicode=true&characterEncoding=UTF-8";
    private static final String user = "root";//用户名
    private static final String password = "15279909969aqw";

    private Connection conn =null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private static readerImple dbserver ;


    public readerImple(){

    }
    public static readerImple getDBserver(){
        if(dbserver==null){
            dbserver = new readerImple();
        }
        return dbserver;
    }
    //查询所有用户信息
    public ArrayList<User> getUserdata(){
        ArrayList<User> users = new ArrayList<User>();
        String sql = "select * from reader";
        conn =  DButil.getSqlConnection(url,user,password);
        if (conn==null){
            Log.d("error1", "getUserdata: 111");
        }
        try{
            if (conn!=null && !(conn.isClosed())){
                ps = (PreparedStatement)conn.prepareStatement(sql);
                if (ps!=null){
                    rs = ps.executeQuery();
                    if (rs!=null){
                        while (rs.next()){
                            User u = new User();
                            u.setId(rs.getInt("reader_id"));
                            u.setName(rs.getString("reader_name"));
                            u.setPwd(rs.getString("reader_pwd"));
                            u.setTel(rs.getString("reader_tel"));
                            users.add(u);
                        }
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        DButil.closeAll(conn,ps,rs);
        return  users;
    }
    //修改用户所有信息,以password作为关键点
    public int updateUserData(int id,String name,String pass,String telphone,String modifyword){
        int result=-1;
        if(!StringUtils.isEmptyOrWhitespaceOnly(modifyword)){
            //获取链接数据库对象
            conn= DButil.getSqlConnection(url,user,password);

            //MySQL 语句
            String sql="update reader set reader_id=?,reader_name=?,reader_tel=?,reader_pwd=? where reader_id=?";
            try {
                boolean closed=conn.isClosed();
                if(conn!=null&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setInt(1,id);
                    ps.setString(2,name);
                    ps.setString(3,telphone);
                    ps.setString(4,pass);
                    ps.setInt(5,id);
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DButil.closeAll(conn,ps);//关闭相关操作
        return result;
    }
    //插入用户数据
    public int insertReaderData(ArrayList<User> users){
        int result = -1;
        if(users!=null && users.size()>0){
            conn = DButil.getSqlConnection(url,user,password);

            String sql = "insert into reader(reader_name,reader_pwd,reader_tel)values(?,?,?)";
            try {
                boolean closed = conn.isClosed();
                if ((conn!=null)&& (!closed)){
                    for (User user: users){
                        ps = (PreparedStatement)conn.prepareStatement(sql);
                        String name = user.getName();
                        String pwd = user.getPwd();
                        String tel = user.getTel();
                        ps.setString(1,name);
                        ps.setString(2,pwd);
                        ps.setInt(3,Integer.valueOf(tel));
                        result = ps.executeUpdate();
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        DButil.closeAll(conn,ps);
        return result;
    }
    public void insert(User users){
        int result = -1;
        String sql = "insert into reader(reader_name,reader_pwd,reader_tel)values(?,?,?)";
        conn = DButil.getInstance().getSqlConnection(url,user,password);

        try {

            boolean closed = conn.isClosed();
            if ((conn!=null)&& (!closed)){
                ps = (PreparedStatement)conn.prepareStatement(sql);
                String name = users.getName();
                String pwd = users.getPwd();
                String tel = users.getTel();
                ps.setString(1,name);
                ps.setString(2,pwd);
                ps.setString(3,tel);
                result = ps.executeUpdate();
            }
            }catch (SQLException e){
                e.printStackTrace();
            }
    }
    //删除用户信息
    public int deleteReaderData(String modifyData){
        int result = -1;
        if (!StringUtils.isEmptyOrWhitespaceOnly(modifyData)){
            conn = DButil.getSqlConnection(url,user,password);
            String sql = "delete from reader where reader_tel = ?";
            try{
                boolean closed = conn.isClosed();
                if((conn!=null)&& (!closed)){
                    ps = (PreparedStatement)conn.prepareStatement(sql);
                    ps.setString(1,modifyData);
                    result = ps.executeUpdate();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        DButil.closeAll(conn,ps);
        return result;
    }

}
