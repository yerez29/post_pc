package com.example.firststeptoselfchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity {

    private static final String CURRENT_USER = "The current user is: ";
    private static final String MSG_CONTENT = "The message content is: ";
    private static final String TIME_STAMP = "The message sent at: ";
    private static final String DEVICE_MODEL = "Phone model is: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        final String current_user_name = getIntent().getStringExtra("CURRENT_USER_NAME");
        final String position = getIntent().getStringExtra("POSITION");
        String content = getIntent().getStringExtra("CONTENT");
        String time_stamp = getIntent().getStringExtra("TIME_STAMP");
        String device_model = getIntent().getStringExtra("DEVICE_MODEL");
        TextView user_name = findViewById(R.id.current_user_name);
        TextView msg_content = findViewById(R.id.msg_content);
        TextView msg_time_stamp = findViewById(R.id.time_stamp);
        TextView msg_device_model = findViewById(R.id.device_model);
        Button delete_msg = findViewById(R.id.permanent_delete);
        user_name.setText(CURRENT_USER + current_user_name);
        msg_content.setText(MSG_CONTENT + content);
        msg_time_stamp.setText(TIME_STAMP + time_stamp);
        msg_device_model.setText(DEVICE_MODEL + device_model);
        delete_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("POSITION", position);
                intent.putExtra("USER_NAME", current_user_name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
