package com.example.mynovel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.math.MathContext;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //固定管理员登录账号和密码
    private static final String admin_user = "admin";
    private static final String admin_pwd="123456";

    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://10.9.209.12:3306/mynovel";//MYSQL数据库连接Url
    private static String user = "root";//用户名
    private static String password = "15279909969";

    private Intent intent;
    private ArrayList<User> users;
    public String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("error3", "onCreate: 11111");
        intent = getIntent();
        if (intent.getIntExtra("data",0)==1){
            Toast.makeText(this,"注册成功",Toast.LENGTH_LONG).show();
        }else if (intent.getIntExtra("data",0)==2){
            Toast.makeText(this,"密码已修改，请重新登录",Toast.LENGTH_LONG).show();
        }
        //获取所有读者信息
        users = new ArrayList<User>();
        if (isConnectInternet()) {
            Log.d("error1", "onCreate: " + String.valueOf(1));
        }else {
            Log.d("error2", "onCreate: ");
        }
    }
    public void getUsers(){//通过子线程从数据库获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                users = readerImple.getDBserver().getUserdata();
            }
        }).start();
    }
    public boolean isConnectInternet() {

        ConnectivityManager conManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE );

        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

        if (networkInfo != null ){ // 注意，这个判断一定要的哦，要不然会出错

            return networkInfo.isAvailable();

        }
        return false ;
    }
    public void tryLogin(View view){
        getUsers();
        EditText user = (EditText)findViewById(R.id.user);
        EditText pwd = (EditText) findViewById(R.id.pwd);

        String u = user.getText().toString();
        String p = pwd.getText().toString();
        //判断用户登录和管理员登录
        for(User user1:users){
            if(user1.getName().equals(u)){
                if (user1.getPwd().equals(p)){
                    current_user = user1.getName();
                    saveUser(current_user);
                    Intent intent2 = new Intent(MainActivity.this,index_reader.class);
                    startActivity(intent2);
                }else{
                    Toast.makeText(MainActivity.this,"密码错误，请重新输入",Toast.LENGTH_LONG).show();
                }
                    }
                }
        }

    public void tryRes(View view){
        Intent register = new Intent(MainActivity.this,index_register.class);
        startActivity(register);
    }
    public void saveUser(String cur_user){
        SharedPreferences sharedPreferences =getSharedPreferences("current",MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("current_user",cur_user);
        editor.commit();
    }
}
