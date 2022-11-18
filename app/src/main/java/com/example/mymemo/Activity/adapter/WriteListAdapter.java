package com.example.mymemo.Activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymemo.Activity.Interface.CallbackClickListnener;
import com.example.mymemo.Activity.Interface.MemoEventBus;
import com.example.mymemo.Activity.MainActivity;
import com.example.mymemo.Activity.ViewActivity;
import com.example.mymemo.Activity.model.WriteModel;
import com.example.mymemo.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class WriteListAdapter extends RecyclerView.Adapter<Holder> {
    Context mActivity;
    List<WriteModel> dataList;
    ArrayList<String> uriList = new ArrayList<>();

    private CallbackClickListnener callbackClickListnener;

    public void setCallbackClickListnener(CallbackClickListnener callbackClickListnener) {
        this.callbackClickListnener = callbackClickListnener;
    }

    public WriteListAdapter(Context mActivity, List<WriteModel> dataList){
        this.mActivity = mActivity;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View 객체에 아이템방식을 담아서 Holder에 넘겨준다
        View view = inflater.inflate(R.layout.adapter_list_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        final WriteModel dataitem = dataList.get(position);
            holder.context_Item_Title.setText(dataitem.getTitle());
            holder.context_Text.setText(dataitem.getContext());
            holder.context_date.setText(dataitem.getDate());
            if(dataList.get(position).getUriList() != null){
                holder.picture_List.setVisibility(View.VISIBLE);
                Glide.with(holder.itemView).
                        load(dataList.get(position).getImgThum()).
                        into(holder.picture_List);
            }else {
                holder.picture_List.setVisibility(View.GONE);
            }
            //리스트 item 클릭
            holder.context_List_Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int sId = dataitem.getId();
                    String context_Title = dataitem.getTitle();
                    String context_Text = dataitem.getContext();
                    uriList = dataitem.getUriList();
                    callbackClickListnener.onClick(v ,holder.getAdapterPosition(),sId,context_Title,context_Text, uriList);
                }
            });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
class Holder extends RecyclerView.ViewHolder {
    ConstraintLayout context_List_Layout;
    TextView context_Item_Title;
    TextView context_Text;
    TextView context_date;
    ImageView picture_List;

    public Holder(@NonNull View itemView) {
        super(itemView);
        context_List_Layout = itemView.findViewById(R.id.context_List_Layout);
        context_Item_Title = itemView.findViewById(R.id.context_Item_Title);
        context_Text = itemView.findViewById(R.id.context_Text);
        context_date = itemView.findViewById(R.id.context_date);
        picture_List = itemView.findViewById(R.id.picture_List);
    }
}

