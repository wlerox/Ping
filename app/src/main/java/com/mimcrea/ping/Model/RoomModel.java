package com.mimcrea.ping.Model;

public class RoomModel {

    private String roomName;

    public RoomModel(String roomName,String roomId){
        this.roomName=roomName;
        this.roomId=roomId;

    }
    public RoomModel(){

    }

    public String getRoomName() {
        return roomName;
    }


    //sdsdsdsdssdddddddddddddddddddddddddddddddd
    public String getRoomId() {
        return roomId;
    }
    private String roomId;

}
