package com.example.firststeptoselfchat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    public static final String EMPTY_STRING = "";
    public static final String ERROR_MSG = "you can't send an empty message, oh silly!";
    private static final String LENGTH_OF_LIST = "length_of_list";
    private static final String LIST_OF_MESSAGES = "list_of_messages";
    public MyRecyclerViewAdapter adapter;
    private ArrayList<String> messages = new ArrayList<>();
    private int lstLength;
    final Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendBtn = findViewById(R.id.button);
        Context context = getApplicationContext();
        CharSequence text = ERROR_MSG;
        int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, text, duration);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        lstLength = sp.getInt(LENGTH_OF_LIST, 0);
        final SharedPreferences.Editor editor = sp.edit();
        if (lstLength != 0)
        {
            String rjson = sp.getString(LIST_OF_MESSAGES, EMPTY_STRING);
            Type type = new TypeToken<List<String>>() {}.getType();
            messages = gson.fromJson(rjson, type);
        }
        Log.v(LENGTH_OF_LIST, String.valueOf(lstLength));
        final RecyclerView recyclerView = findViewById(R.id.my_recycle_view);
        if (messages.size() > 0)
        {
            recyclerView.scrollToPosition(messages.size() - 1);
        }
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new MyRecyclerViewAdapter(this, messages);
        adapter.setClickListener(this);
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
                    lstLength += 1;
                    editor.putInt(LENGTH_OF_LIST, lstLength);
                    String wjson = gson.toJson(messages);
                    editor.putString(LIST_OF_MESSAGES, wjson);
                    editor.apply();
                }
            }
        });
    }
    @Override
    public void onItemClick(final View view, final int position, boolean isLongClick) {
        if (!isLongClick)
        {
            return;
        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        messages.remove(position);
                        adapter.notifyItemRemoved(position);
                        lstLength -= 1;
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        final SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(LENGTH_OF_LIST, lstLength);
                        String wjson = gson.toJson(messages);
                        editor.putString(LIST_OF_MESSAGES, wjson);
                        editor.apply();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }
}
