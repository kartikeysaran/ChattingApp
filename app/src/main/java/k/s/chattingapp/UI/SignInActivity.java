package k.s.chattingapp.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import k.s.chattingapp.Model.User;
import k.s.chattingapp.R;
import k.s.chattingapp.UI.MainActivity.MainActivity;
import k.s.chattingapp.Utils.Utils;

public class SignInActivity extends AppCompatActivity {

    private EditText name, email, password;
    private TextView title;
    private Button signIn, switchBtn;
    private boolean signInCheck = false;
    private User user;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setUpViews();
        Utils.mAuth = FirebaseAuth.getInstance();
        Utils.mUser = Utils.mAuth.getCurrentUser();
        if(Utils.mUser != null) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }

    }
    private void setUpViews() {
        name = findViewById(R.id.edit_text_name);
        email = findViewById(R.id.edit_text_email);
        password = findViewById(R.id.edit_text_password);
        title = findViewById(R.id.text_view_title_signinpage);
        signIn = findViewById(R.id.btn_login);
        switchBtn = findViewById(R.id.btn_switch);
        pb = findViewById(R.id.progress_bar);
        switchBtn.setOnClickListener(v->{
            if(signInCheck) {
                switchSignUp();
                signInCheck = false;
            } else {
                switchLogin();
                signInCheck = true;
            }
        });
        signIn.setOnClickListener(v->{
            pb.setVisibility(View.VISIBLE);
            if(signInCheck) {
                if(!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    Utils.mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            pb.setVisibility(View.GONE);
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(SignInActivity.this, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    pb.setVisibility(View.GONE);
                    email.setError("Required");
                    password.setError("Required");
                }
            } else {
                if(!email.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !password.getText().toString().isEmpty() ) {

                    switchBtn.setEnabled(false);
                    signIn.setEnabled(false);
                    Utils.mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            user = new User(name.getText().toString(), email.getText().toString(), authResult.getUser().getUid());
                            addUserToDatabase(user);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignInActivity.this, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);
                        }
                    });
                } else {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                    email.setError("Mandatory");
                    name.setError("Mandatory");
                    password.setError("Mandatory");
                }
            }
        });
    }
    private void switchLogin() {
        name.setVisibility(View.GONE);
        title.setText("Sign In");
        signIn.setText("Sign In");
        switchBtn.setText("Sign up instead");
    }
    private void switchSignUp() {
        name.setVisibility(View.VISIBLE);
        title.setText("Sign Up");
        signIn.setText("Sign Up");
        switchBtn.setText("Sign in instead");
    }
    private void addUserToDatabase(User user) {
            Utils.mDatabase = FirebaseDatabase.getInstance();
            Utils.mDatabaseRef = Utils.mDatabase.getReference();
            Utils.mDatabaseRef.child("users").child(user.getUid()).setValue(user);
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
    }
}