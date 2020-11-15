package com.example.mynovel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class reader_modifypwd extends AppCompatActivity {
    private static final int COMPLETED=0;
    private EditText editText_old;
    private EditText editText_new;
    private EditText editText_newAgain;
    private Button commit;
    private ArrayList<User>users;
    private User current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_modifypwd);
        editText_old = (EditText)findViewById(R.id.reader_oldpwd);
        editText_new = (EditText)findViewById(R.id.reader_newpwd);
        editText_newAgain = (EditText)findViewById(R.id.reader_newpwdAgain);
        commit = (Button)findViewById(R.id.commit_pwd);
        users = new ArrayList<User>();
        current_user = new User();
        getData();
    }
    public String getCurrent_user(){
        SharedPreferences sharedPreferences = getSharedPreferences("current",MODE_PRIVATE);
        String current = sharedPreferences.getString("current_user",null);
        return current;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==COMPLETED) {
                Intent backIntent = new Intent(reader_modifypwd.this, MainActivity.class);
                backIntent.putExtra("data", 2);
                startActivity(backIntent);
            }
        }
    };
    public void commitPassword(View v){
        //用sharedPreference取出当前用户
        current_user.setName(getCurrent_user());
        for (User u:users){
            if (u.getName().equals(current_user.getName())){
                current_user.setPwd(u.getPwd());
                current_user.setTel(u.getTel());
                current_user.setId(u.getId());
            }
        }
        String oldpwd = editText_old.getText().toString();
        String newpwd = editText_new.getText().toString();
        final String newAgain = editText_newAgain.getText().toString();
        //判断密码是否符合当前用户的密码
        if (oldpwd.equals(current_user.getPwd())){
            if (!newpwd.equals("")){
                if (newpwd.equals(newAgain)){
                    //验证成功，更新密码
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int result = readerImple.getDBserver().updateUserData(current_user.getId(),current_user.getName(),
                                    newAgain,current_user.getTel(),current_user.getPwd());
                            Message msg = new Message();
                            msg.what = COMPLETED;
                            handler.sendMessage(msg);
                        }
                    }).start();

                }else {
                    Toast.makeText(reader_modifypwd.this,"两次密码不一致",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(reader_modifypwd.this,"新密码不能为空",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(reader_modifypwd.this,"原密码输入错误",Toast.LENGTH_LONG).show();
        }

    }
    public void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                users = readerImple.getDBserver().getUserdata();
            }
        }).start();
    }
}
