package com.mimcrea.ping;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mimcrea.ping.Adapter.RoomAdapter;
import com.mimcrea.ping.Interface.RoomAdapterVievEvent;
import com.mimcrea.ping.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    private TextView userName,title,userEmail;
    private ImageView userImage;
    private ImageButton ımageButton;
    private RecyclerView recyclerView;
    private AlertDialog dialog;
    private Button userAdd,userPing;
    private boolean userexist=false;
    private  String selectedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        //textView=findViewById(R.id.activity_room_text);

        roomInit();
        roomAdapterChange();

        //textView.setText(getIntent().getStringExtra("roomId"));


    }
    private void roomInit(){
        title=findViewById(R.id.activity_room_toolbar_title);
        recyclerView=findViewById(R.id.activity_room_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(RoomActivity.this,LinearLayoutManager.VERTICAL,false));
        title.setText(getIntent().getStringExtra("roomName"));

    }


    private  void roomAdapterChange(){
        String roomId=getIntent().getStringExtra("roomId");
        final String userId=getIntent().getStringExtra("userUid");

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Room").child(roomId);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserModel> userlist=new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    UserModel user=new UserModel(ds.getValue(String.class),ds.getKey());
                    userlist.add(user);
                    //textView.setText(textView.getText()+" "+ds.getValue());
                    String userUid=ds.getKey();
                    System.out.println("hawww ekleme ekranı="+userUid+" "+userId);
                    if (userUid.equals(userId)) {
                        System.out.println("hawww ekleme ekranı eşit ");
                        userexist=true;
                    }
                }
                if(userexist==false){
                    roomAddUserDialog();
                }
                recyclerView.setAdapter(new RoomAdapter(userlist, new RoomAdapterVievEvent() {
                    @Override
                    public void onClick(String userUid) {
                        selectedUser=userUid;
                        System.out.println("hawwww user="+userUid);

                        openUserDialog();
                    }
                }));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "hataaaa", Toast.LENGTH_LONG).show();

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    private void openUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RoomActivity.this);
        builder.setTitle("Kullanıcı");
        View view=getLayoutInflater().inflate(R.layout.activity_room_user_dialog,null);
        userName=view.findViewById(R.id.activity_room_user_dialog_name);
        userEmail=view.findViewById(R.id.activity_room_user_dialog_email);
        userImage=view.findViewById(R.id.activity_room_user_dialog_image);
        userPing=view.findViewById(R.id.activity_room_user_dialog_ping);
        builder.setView(view);
        //builder.setCancelable(false);//dialog dışına erişim kapandı;
        dialog=builder.create();
        userLoad();
        //dialogu gösterme
        dialog.show();

    }

    private void roomAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RoomActivity.this);
        builder.setTitle("Odaya Kayıt Ekranı");
        View view=getLayoutInflater().inflate(R.layout.room_user_add,null);
        userAdd=view.findViewById(R.id.room_user_add_button);
        builder.setView(view);
        builder.setCancelable(false);//dialog dışına erişim kapandı;
        builder.setNegativeButton("Kayıt olmak İstemiyorum", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent ıntent=new Intent(RoomActivity.this,MainActivity.class);
                startActivity(ıntent);
            }
        });
        dialog=builder.create();

        roomAddUser();
        //dialogu gösterme
        dialog.show();
    }

    private void userLoad() {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("Users").child(selectedUser);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user=dataSnapshot.getValue(UserModel.class);

                if(user!=null) {
                    System.out.println("hawwww"+user.getUserName());
                    userName.setText(user.getUserName().trim());
                    userEmail.setText(user.getUserEmail());
                    Glide.with(RoomActivity.this)
                            .load(user.getUserPictureUrl())
                            .into(userImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    private void roomAddUser(){
        userAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference=database.getReference();
                reference.child("Room").child(getIntent().getStringExtra("roomId")).child(getIntent().getStringExtra("userUid")).setValue("engin");
                dialog.dismiss();
            }
        });
    }
}
