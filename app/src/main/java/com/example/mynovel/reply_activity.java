package com.example.mynovel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class reply_activity extends AppCompatActivity {
    private static final int COMPLETED=0;
    private String user;
    private String user_content;
    private String current_user;
    private TextView replieduser;
    private TextView repliedcontent;
    private EditText message;
    private ListView allReplyMessages;
    private myAdapter myadapter;

    //数据数组
    //所有回复
    private ArrayList<userMessage> allUserMessages;
    //筛选出当前回复
    private ArrayList<userMessage> current_messages;
    //作为插入数据使用
    private userMessage umg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_activity);
        Intent getUsername = getIntent();
        user = getUsername.getStringExtra("relpy_username");
        user_content = getUsername.getStringExtra("reply_content");
        replieduser = (TextView)findViewById(R.id.replied_user);
        repliedcontent = (TextView)findViewById(R.id.replied_content);
        allReplyMessages = (ListView)findViewById(R.id.replyMessages_listview);
        message = (EditText)findViewById(R.id.mymessage);
        allUserMessages = new ArrayList<userMessage>();
        current_messages = new ArrayList<userMessage>();
        replieduser.setTextColor(Color.rgb(0,0,255));
        replieduser.setText(user);
        repliedcontent.setText(user_content);
        //获取当前用户名
        getCurrentuser();
        //获取所有数据并进行界面初始化
        getAllMessages();
    }
    public void getCurrentuser(){
        SharedPreferences sharedPreferences = getSharedPreferences("current",MODE_PRIVATE);
        this.current_user = sharedPreferences.getString("current_user",null);
    }
    public void replyClick(View view){
        //提交回复事件
        String mymessage = message.getText().toString();
        umg = new userMessage(current_user,mymessage,user,user_content);
        current_messages.add(umg);
        if (myadapter==null){
            myadapter = new myAdapter(this);
            allReplyMessages.setAdapter(myadapter);
            Toast.makeText(this,"评论成功",Toast.LENGTH_SHORT).show();
        }else {
            myadapter.notifyDataSetChanged();
            Toast.makeText(this,"评论成功",Toast.LENGTH_SHORT).show();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                messageImple.getMessageImple().insertMessage(umg);
            }
        }).start();

    }
    public void getAllMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                allUserMessages = messageImple.getMessageImple().getUserMessagedata();
                Message msg = new Message();
                msg.what = COMPLETED;
                init_handler.sendMessage(msg);
            }
        }).start();
    }
    private Handler init_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==COMPLETED){
                //初始化界面UI
                //获取当前回复列表
                for (userMessage um:allUserMessages){
                    if (um.getTouser().equals(user) && um.getTomsg().equals(user_content)){
                        current_messages.add(um);
                    }
                }
                if (current_messages.size()!=0){
                    myadapter = new myAdapter(reply_activity.this);
                    allReplyMessages.setAdapter(myadapter);
                }
            }
        }
    };
    public class myAdapter extends BaseAdapter
    {

        private LayoutInflater mInflater;
        public myAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            if(current_messages.size()==0){
                return 0;
            }else {
                return current_messages.size();
            }
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            viewholder holder;
            if (convertView == null) {
                holder=new viewholder();
                convertView = mInflater.inflate(R.layout.message_listview,null);
                holder.replyusername = (TextView)convertView.findViewById(R.id.message_username);
                holder.replycontent = (TextView)convertView.findViewById(R.id.message_usercontent);
                convertView.setTag(holder);
            }else {
                holder = (reply_activity.viewholder)convertView.getTag();
            }
            for (int i=0;i<current_messages.size();i++){
                Log.d("error333", "getView: "+current_messages.get(i).getUsername());
            }
            holder.replyusername.setText(current_messages.get(position).getUsername());
            holder.replycontent.setText(current_messages.get(position).getMessage());

            return convertView;
        }
    }
    public final class viewholder{
        private TextView replyusername;
        private TextView replycontent;
    }
}
