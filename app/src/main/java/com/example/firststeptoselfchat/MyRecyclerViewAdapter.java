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
    private ItemClickListener mClickListener;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView myTextView;
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(), false);
        }

        public boolean onLongClick(View view){
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(), true);
            return true;
        }
    }

    String getItem(int id) {
        return recycleData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, boolean isLongClick);
    }
}