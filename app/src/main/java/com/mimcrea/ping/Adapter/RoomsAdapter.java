package com.mimcrea.ping.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mimcrea.ping.Interface.RoomAdapterVievEvent;
import com.mimcrea.ping.Interface.RoomsAdapterViewEvent;
import com.mimcrea.ping.Model.RoomModel;
import com.mimcrea.ping.R;

import java.util.ArrayList;
import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomsAdapterVievHolder>{
    private RoomsAdapterViewEvent roomsAdapterViewEvent;
    private List<RoomModel> roomList=new ArrayList<>();

    public RoomsAdapter(List<RoomModel> roomList,RoomsAdapterViewEvent roomsAdapterViewEvent){
        this.roomsAdapterViewEvent=roomsAdapterViewEvent;
        this.roomList=roomList;
    }

    @NonNull
    @Override
    public RoomsAdapterVievHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RoomsAdapter.RoomsAdapterVievHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_rec_source,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapterVievHolder roomsAdapterVievHolder, final int i) {
        roomsAdapterVievHolder.textView.setText(roomList.get(i).getRoomName().trim());
        roomsAdapterVievHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomsAdapterViewEvent.onClick(roomList.get(i).getRoomId().trim(),roomList.get(i).getRoomName().trim());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(roomList==null)
            return 0;
        else
            return  roomList.size();
    }

    public  class RoomsAdapterVievHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RoomsAdapterVievHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.activity_main_rec_source_text);
        }
    }
}
