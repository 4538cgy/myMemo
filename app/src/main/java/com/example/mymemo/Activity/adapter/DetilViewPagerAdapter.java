package com.example.mymemo.Activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymemo.R;

import java.util.ArrayList;

public class DetilViewPagerAdapter extends RecyclerView.Adapter<DetilViewPagerAdapter.ViewHolderPage> {

    Context mContext = null;
    ArrayList<String> uriList;

    public DetilViewPagerAdapter(Context mContext, ArrayList<String> uriList){
        this.mContext = mContext;
        this.uriList = uriList;

    }

    @NonNull
    @Override
    public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_detil_view_pager, parent, false);
        return new ViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPage holder, int position) {
        Glide.with(holder.itemView)
                .load(uriList.get(position))
                .into(holder.detil_Img);
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class ViewHolderPage extends RecyclerView.ViewHolder {

        ImageView detil_Img;

        public ViewHolderPage(@NonNull View itemView) {
            super(itemView);
            detil_Img = itemView.findViewById(R.id.detil_Img);
        }
    }
}
