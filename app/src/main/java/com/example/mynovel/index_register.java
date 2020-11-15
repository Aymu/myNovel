package com.example.mynovel;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;
import java.util.ArrayList;

public class index_register extends AppCompatActivity {

    private static final String url = "jdbc:mysql://10.9.209.12:3306/mynovel";
    private static final String users = "root";//用户名
    private static final String password = "15279909969";
    private boolean isRepeat=false;
    private ImageView iv;
    private EditText user;
    private EditText pwd;
    private EditText repwd;
    private EditText tel;
    private EditText code;
    private String realcode;
    private Connection connection;
    private ArrayList<User> us;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_register);
        iv = (ImageView) findViewById(R.id.show_code);
        user =  (EditText)findViewById(R.id.res_user);
        pwd =  (EditText)findViewById(R.id.res_pwd);
        repwd =  (EditText)findViewById(R.id.res_repwd);
        tel =  (EditText)findViewById(R.id.res_tel);
        code =  (EditText)findViewById(R.id.res_code);
        iv.setImageBitmap(Code.getInstance().createBitmap());
        realcode = Code.getInstance().getCode().toLowerCase();
        us = new ArrayList<User>();
        getusers();
    }
    public void getusers(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                us = readerImple.getDBserver().getUserdata();
            }
        }).start();
    }
    public void successRes(View view) {
        String phoneCode = code.getText().toString().toLowerCase();
        String password = pwd.getText().toString();
        String repassword = repwd.getText().toString();
        String name = user.getText().toString();
        String tell = tel.getText().toString();
        for (User u : us) {
            if (name.equals(u.getName())) {
                isRepeat = true;
            }
        }
        if (!isRepeat) {
            if (password.equals(repassword) && !password.equals("")) {
                if (phoneCode.equals(realcode)) {
                    //将数据写入数据库
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User user1 = new User();
                            user1.setName(user.getText().toString());
                            user1.setPwd(pwd.getText().toString());
                            user1.setTel(tel.getText().toString());
                            readerImple.getDBserver().insert(user1);
                        }
                    }).start();


                    Intent intent = new Intent(index_register.this, MainActivity.class);
                    intent.putExtra("data", 1);
                    startActivity(intent);
                } else {
                    Toast.makeText(index_register.this, phoneCode + "验证码错误", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(index_register.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(index_register.this,"该用户名已注册",Toast.LENGTH_SHORT).show();
        }
    }


    public void refresh(View view){
        iv.setImageBitmap(Code.getInstance().createBitmap());
        realcode = Code.getInstance().getCode().toLowerCase();
    }
}
