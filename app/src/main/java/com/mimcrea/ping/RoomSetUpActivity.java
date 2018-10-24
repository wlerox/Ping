package com.mimcrea.ping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import android.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoomSetUpActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ImageButton backButton;
    private TextView roomName;
    private Button roomSetUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_set_up);

        yukle();
        roomName=findViewById(R.id.activity_room_set_up_roomname);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RoomSetUpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        roomSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Rooms").push().setValue(roomName.getText().toString().trim());
            }
        });




    }
    private void yukle(){
        toolbar=findViewById(R.id.activity_room_set_up_toolbar);
        backButton=findViewById(R.id.activity_room_set_up_back);
        roomName=findViewById(R.id.activity_room_set_up_roomname);
        roomSetUp=findViewById(R.id.activity_room_set_up_roomsetup);
    }

}
