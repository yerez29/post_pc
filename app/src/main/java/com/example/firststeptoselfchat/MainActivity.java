package com.example.firststeptoselfchat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity{
    public static final String EMPTY_STRING = "";
    public static final String ERROR_MSG = "you can't send an empty message, oh silly!";
    public MyRecyclerViewAdapter adapter;
    private ArrayList<String> messages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendBtn = findViewById(R.id.button);
        Context context = getApplicationContext();
        CharSequence text = ERROR_MSG;
        int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, text, duration);
//        final ArrayList<String> messages = new ArrayList<>();
        final RecyclerView recyclerView = findViewById(R.id.my_recycle_view);
        if (messages.size() > 0)
        {
            recyclerView.scrollToPosition(messages.size() - 1);
        }
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new MyRecyclerViewAdapter(this, messages);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputText = findViewById(R.id.editText);
                String outputStr = inputText.getText().toString();
                inputText.setText(EMPTY_STRING);
                if (outputStr.equals(EMPTY_STRING)){
                    toast.show();
                }
                else {
                    messages.add(outputStr);
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size() - 1);
                }
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList("key", messages);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getStringArrayList("key") != null)
        {
            messages.addAll(savedInstanceState.getStringArrayList("key"));
        }
    }
}
