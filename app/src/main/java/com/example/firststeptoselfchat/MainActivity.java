package com.example.firststeptoselfchat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    public static final String EMPTY_STRING = "";
    public static final String ERROR_MSG = "you can't send an empty message, oh silly!";
    private static final String LENGTH_OF_LIST = "length_of_list";
    private static final String LIST_OF_MESSAGES = "list_of_messages";
    private static final String COUNTER_VALUE = "counter_value";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionRef = db.collection("messages");
    private DocumentReference docRef = db.collection("key").document("current_msg_key");
    public MyRecyclerViewAdapter adapter;
    private ArrayList<String> messages = new ArrayList<>();
    private int lstLength;
    private int msgCounter;
    private String timeStamp;
    private Map<String, Object> keyMap = new HashMap<>();
    private ArrayList<Message> arrayList = new ArrayList<>();
    final Gson gson = new Gson();
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private class fireStoreBGthread extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
                collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        arrayList.clear();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            Message message = documentSnapshot.toObject(Message.class);
                            arrayList.add(message);
                        }
                        Collections.sort(arrayList, new Comparator<Message>() {
                            @Override
                            public int compare(Message m1, Message m2) {
                                return Integer.valueOf(m1.getMsgKey()).compareTo(Integer.valueOf(m2.getMsgKey()));
                            }
                        });
                        messages.clear();
                        for (Message message:arrayList)
                        {
                            messages.add(message.getMsgContent());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendBtn = findViewById(R.id.button);
        Context context = getApplicationContext();
        CharSequence text = ERROR_MSG;
        int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, text, duration);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        lstLength = sp.getInt(LENGTH_OF_LIST, 0);
        editor = sp.edit();
        Log.v(LENGTH_OF_LIST, String.valueOf(lstLength));
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    try
                    {
                        msgCounter = Integer.parseInt(documentSnapshot.getString(COUNTER_VALUE));
                    }
                    catch (NumberFormatException e)
                    {
                        msgCounter = 0;
                    }
                }
                else
                {
                    msgCounter = 0;
                }
            }
        });
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
        new fireStoreBGthread().execute(); // can add params for a constructor if needed
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
                    msgCounter += 1;
                    keyMap.put(COUNTER_VALUE, String.valueOf(msgCounter));
                    docRef.set(keyMap);
                    timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
                    Message newMessage = new Message(outputStr, timeStamp, String.valueOf(msgCounter));
                    collectionRef.document(String.valueOf(msgCounter)).set(newMessage);
                    arrayList.add(newMessage);
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
                        collectionRef.document(arrayList.get(position).getMsgKey()).delete();
                        arrayList.remove(position);
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
