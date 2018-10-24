package com.mimcrea.ping;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mimcrea.ping.Adapter.RoomAdapter;
import com.mimcrea.ping.Adapter.RoomsAdapter;
import com.mimcrea.ping.Interface.RoomAdapterVievEvent;
import com.mimcrea.ping.Interface.RoomsAdapterViewEvent;
import com.mimcrea.ping.Model.RoomModel;
import com.mimcrea.ping.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //firebase google girişi için tanımlamalar
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 0 ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private com.google.android.gms.common.SignInButton signIn;
    private GoogleApiClient mGoogleApiClient;
    public FirebaseUser user;
    private AlertDialog dialog;

    //burada tanımlamalar bitti
    private android.support.v7.widget.Toolbar toolbar;
    private ImageButton roomSetUp;
    private RecyclerView recyclerView;
    //private RoomsAdapterViewEvent roomsAdapterVievEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userLogin();
        yukle();

        roomSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RoomSetUpActivity.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Rooms");
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               List<RoomModel> roomList=new ArrayList<>();
               for(DataSnapshot ds:dataSnapshot.getChildren()){
                   //String value=ds.getValue(String.class);
                   RoomModel room=new RoomModel(ds.getValue(String.class),ds.getKey());

                   roomList.add(room);
               }

               for (int i=0;i<roomList.size();i++)
                   System.out.println("hawwww "+roomList.get(i).getRoomName());
               recyclerView.setAdapter(new RoomsAdapter(roomList, new RoomsAdapterViewEvent() {
                   @Override
                   public void onClick(String roomid, String roomname) {
                       Intent ıntent=new Intent(MainActivity.this,RoomActivity.class);
                       System.out.println("hawwwww "+roomid +" "+ roomname);
                       ıntent.putExtra("roomId",roomid);
                       ıntent.putExtra("roomName",roomname);
                       ıntent.putExtra("userUid",user.getUid());
                       startActivity(ıntent);
                   }
               }));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);



    }

    //ön yükleme
    public void yukle(){
        recyclerView=findViewById(R.id.activity_main_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
        toolbar=findViewById(R.id.activity_main_toolbar_tool);
        roomSetUp=findViewById(R.id.activity_main_toolbar_setuproom);
    }

    //Giriş ekranı alertdialog

    public void userLogin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Giriş Ekranı");
        View view=getLayoutInflater().inflate(R.layout.google_auth,null);
        signIn=view.findViewById(R.id.google_auth_sign_in);
        builder.setView(view);
        builder.setCancelable(false);//dialog dışına erişim kapandı;
        dialog=builder.create();
        //dialogu gösterme
        googleAuth();
        dialog.show();



    }
    public void googleAuth(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } )// Bağlantı fail olup olmadığını dinliyoruz
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user=firebaseAuth.getCurrentUser();
                //System.out.println("haww"+user.getDisplayName()+" "+user.getEmail()+" "+user.getPhotoUrl());
                //signIn.setVisibility(View.GONE);
                if(user !=null){
                    System.out.println("haww  user dolu"+user.getUid());
                    //kullanıcı giriş yapmış
                    userSave();//kullanıcının bilgilerini alıyorum
                    dialog.dismiss();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else {
                    // kullanıcı çıkış yaptı
                    FirebaseAuth.getInstance().signOut();
                    System.out.println("haww  user boş");
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void userSave() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference();
        UserModel userModel=new UserModel(user.getUid(),user.getDisplayName(),user.getEmail(),String.valueOf(user.getPhotoUrl()));
        reference.child("Users").child(userModel.getUserUid()).setValue(userModel);




    }

    private void signIn(){
        Intent singnInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(singnInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                //Google girişi başarılı oldu, Firebase ile haberleşiyoruz
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else{
                Toast.makeText(MainActivity.this, "Google girişi  failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        System.out.println("haww on start"+user);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener !=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential=GoogleAuthProvider.getCredential((account.getIdToken()),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // giriş başarısız olduğunda toast mesajı gösterme

                        if (!task.isSuccessful()) {

                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
