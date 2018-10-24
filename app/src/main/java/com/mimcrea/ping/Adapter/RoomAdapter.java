package com.mimcrea.ping.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mimcrea.ping.Interface.RoomAdapterVievEvent;
import com.mimcrea.ping.Model.RoomModel;
import com.mimcrea.ping.Model.UserModel;
import com.mimcrea.ping.R;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomAdapterViewHolder>{
    private RoomAdapterVievEvent roomAdapterVievEvent;
    private List<UserModel> userList=new ArrayList<>();

    public RoomAdapter(List<UserModel> userList,RoomAdapterVievEvent roomAdapterVievEvent){
        this.roomAdapterVievEvent=roomAdapterVievEvent;
        this.userList=userList;
    }
    @NonNull
    @Override
    public RoomAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RoomAdapterViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_rec_source,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapterViewHolder roomAdapterViewHolder, final int i) {
        roomAdapterViewHolder.textView.setText(userList.get(i).getUserName());
        roomAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomAdapterVievEvent.onClick(userList.get(i).getUserUid());
            }
        });


    }

    @Override
    public int getItemCount() {
        if(userList==null)
            return 0;
        else
            return  userList.size();
    }

    public class RoomAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RoomAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.activity_main_rec_source_text);
        }
    }
}
