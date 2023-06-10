package k.s.chattingapp.UI.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import k.s.chattingapp.UI.SignInActivity;
import k.s.chattingapp.Model.User;
import k.s.chattingapp.R;
import k.s.chattingapp.Utils.UserListAdapter;
import k.s.chattingapp.Utils.Utils;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private MainActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.getUserArrayListLive().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                userListAdapter = new UserListAdapter(users);
                recyclerView.setAdapter(userListAdapter);
                Log.d("LISTSIZE", "Size: "+users.size());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_log_out) {
            Utils.mAuth.signOut();
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}