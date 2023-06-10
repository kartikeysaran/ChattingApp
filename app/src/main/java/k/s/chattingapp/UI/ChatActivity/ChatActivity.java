package k.s.chattingapp.UI.ChatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import k.s.chattingapp.Model.Message;
import k.s.chattingapp.Model.User;
import k.s.chattingapp.R;
import k.s.chattingapp.Utils.MessagesAdapter;
import k.s.chattingapp.Utils.Utils;

public class ChatActivity extends AppCompatActivity {

    private User user;
    private RecyclerView recyclerView;
    private MessagesAdapter messagesAdapter;
    private ImageButton send;
    private EditText eT_text;
    private String senderRoom, receiverRoom;
    private ArrayList<Message> messageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recycler_view);
        messageArrayList = new ArrayList<>();
        send = findViewById(R.id.img_btn_send);
        send.setEnabled(false);
        eT_text = findViewById(R.id.edit_text_message);
        eT_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() > 0) {
                    send.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        messagesAdapter = new MessagesAdapter(messageArrayList);
        recyclerView.setAdapter(messagesAdapter);

        Intent intent = getIntent();
        user = new User();
        String uid = (String) intent.getExtras().get("uid");
        if(uid!=null && !uid.isEmpty()) {
            senderRoom = Utils.mAuth.getUid()+uid;
            receiverRoom = uid+Utils.mAuth.getUid();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getUser(uid);
            getMessages();
            send.setOnClickListener(v->{
                addMessage(eT_text.getText().toString());
            });
        }

    }

    private void getUser(String uid) {
        Utils.mAuth = FirebaseAuth.getInstance();
        Utils.mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        Utils.mDatabaseRef.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChildren()) {
                    Toast.makeText(ChatActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
                User user1 = snapshot.getValue(User.class);
                getSupportActionBar().setTitle(user1.getName());
                getSupportActionBar().setSubtitle("online");

                setUser(user1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMessages() {
        Utils.mAuth = FirebaseAuth.getInstance();
        Utils.mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        Utils.mDatabaseRef.child("chats").child(senderRoom).child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageArrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            messageArrayList.add(message);
                        }
                        messagesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void addMessage(String message) {
        Utils.mAuth = FirebaseAuth.getInstance();
        Utils.mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Message msg = new Message(message, Utils.getCurrentTimeStamp() , FirebaseAuth.getInstance().getUid());
        Utils.mDatabaseRef.child("chats").child(senderRoom).child("messages").push()
                .setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Utils.mDatabaseRef.child("chats").child(receiverRoom).child("messages").push()
                                        .setValue(msg);
                        eT_text.setText("");
                        eT_text.clearFocus();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}