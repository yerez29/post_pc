package com.example.firststeptoselfchat;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> recycleData;
    private LayoutInflater recycleInflater;

    MyRecyclerViewAdapter(Context context, List<String> data) {
        this.recycleInflater = LayoutInflater.from(context);
        this.recycleData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = recycleInflater.inflate(R.layout.item_one_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String str = recycleData.get(position);
        holder.myTextView.setText(str);
    }

    @Override
    public int getItemCount() {
        return recycleData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView myTextView;
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textView);
        }
    }
}