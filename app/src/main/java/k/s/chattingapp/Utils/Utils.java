package k.s.chattingapp.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static FirebaseAuth mAuth;
    public static FirebaseUser mUser;
    public static FirebaseDatabase mDatabase;
    public static DatabaseReference mDatabaseRef;
    public static String timeStampToTime(String timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(date);
    }
    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));
    }


}
