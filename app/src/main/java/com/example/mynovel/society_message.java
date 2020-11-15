package com.example.mynovel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class society_message extends AppCompatActivity {
    private ListView society_messages;
    private ArrayList<userMessage> allUserMessages;
    private myAdapter myadapter;
    private static final int COMPLETED=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_message);
        society_messages = (ListView)findViewById(R.id.societyMessage_listview);
        allUserMessages = new ArrayList<userMessage>();
        getAllUserMessages();
    }
    private Handler displayHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==COMPLETED){
                myadapter = new myAdapter(society_message.this);
                society_messages.setAdapter(myadapter);
                society_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //跳转到回复页面并传送当前的username过去
                        Intent reply_intent = new Intent(society_message.this,reply_activity.class);
                        reply_intent.putExtra("relpy_username",allUserMessages.get(position).getUsername());
                        reply_intent.putExtra("reply_content",allUserMessages.get(position).getMessage());
                        startActivity(reply_intent);
                    }
                });
            }
        }
    };
    public void getAllUserMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
               allUserMessages = messageImple.getMessageImple().getUserMessagedata();
               Message msg = new Message();
               msg.what=COMPLETED;
               displayHandler.sendMessage(msg);
            }
        }).start();
    }
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
            if(allUserMessages.size()==0){
                return 0;
            }else {
                return allUserMessages.size();
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
                holder.username = (TextView)convertView.findViewById(R.id.message_username);
                holder.content = (TextView)convertView.findViewById(R.id.message_usercontent);
                convertView.setTag(holder);
            }else {
                holder = (society_message.viewholder)convertView.getTag();
            }
            if (allUserMessages.get(position).getTouser().equals("")) {
                holder.username.setText(allUserMessages.get(position).getUsername());
            }else {
                holder.username.setText(allUserMessages.get(position).getUsername()+"       回复"
                +allUserMessages.get(position).getTouser());
            }
            holder.content.setText(allUserMessages.get(position).getMessage());

            return convertView;
        }
    }
    public final class viewholder{
        private TextView username;
        private TextView content;
    }

}
