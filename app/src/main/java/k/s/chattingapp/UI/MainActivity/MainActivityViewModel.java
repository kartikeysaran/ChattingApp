package k.s.chattingapp.UI.MainActivity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import k.s.chattingapp.Model.User;
import k.s.chattingapp.Utils.Utils;

public class MainActivityViewModel extends ViewModel {
    MutableLiveData<ArrayList<User>> userArrayListLive;
    ArrayList<User> userArrayList;

    public MutableLiveData<ArrayList<User>> getUserArrayListLive() {
        if(userArrayList == null || userArrayList.isEmpty()) {
            userArrayListLive = new MutableLiveData<>();
            populateData();
        }
        return userArrayListLive;
    }

    public void setUserArrayListLive(MutableLiveData<ArrayList<User>> userArrayListLive) {
        this.userArrayListLive = userArrayListLive;
    }

    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }

    public void setUserArrayList(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    // TODO: Implement the ViewModel
    private void populateData() {
        Utils.mAuth = FirebaseAuth.getInstance();
        Utils.mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        this.userArrayList = new ArrayList<>();
        this.userArrayListLive = new MutableLiveData<>();
        Utils.mDatabaseRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    if(!user.getUid().equals(FirebaseAuth.getInstance().getUid()) ) {
                        Log.d("UPDATE", "CurrentUser: "+ FirebaseAuth.getInstance().getUid()+" UID: "+user.getUid());
                        userArrayList.add(user);
                    }
                }
                setUserArrayList(userArrayList);
                userArrayListLive.setValue(getUserArrayList());
                setUserArrayListLive(userArrayListLive);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}