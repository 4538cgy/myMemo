package com.example.mymemo.Activity.adapter;

import static android.widget.Toast.makeText;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymemo.Activity.Interface.MiniListDeleteCallback;
import com.example.mymemo.R;

import java.util.List;

public class MiniListAdapter extends RecyclerView.Adapter<ListHolder> {
    Context mActivity;
    List<String> miniList;

    private MiniListDeleteCallback miniListDeleteCallback;

    public void setMiniListDeleteCallback(MiniListDeleteCallback miniListDeleteCallback) {
        this.miniListDeleteCallback = miniListDeleteCallback;
    }

    public MiniListAdapter(Context mActivity , List<String> miniList){
        this.mActivity = mActivity;
        this.miniList = miniList;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View 객체에 아이템방식을 담아서 Holder에 넘겨준다
        View view = inflater.inflate(R.layout.adapter_mini_list_item, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        Glide.with(holder.itemView).
                load(miniList.get(position)).
                override(200, 200).
                centerCrop().
                into(holder.mini_Img);

        holder.click_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniListDeleteCallback.deleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return miniList.size();
    }
}

class ListHolder extends RecyclerView.ViewHolder {
    ImageView mini_Img;
    ConstraintLayout click_Delete;

    public ListHolder(@NonNull View itemView) {
        super(itemView);
        mini_Img = itemView.findViewById(R.id.mini_Img);
        click_Delete = itemView.findViewById(R.id.click_Delete);


    }
}
