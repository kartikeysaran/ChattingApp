package k.s.chattingapp.Utils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import k.s.chattingapp.Model.User;
import k.s.chattingapp.R;
import k.s.chattingapp.UI.ChatActivity.ChatActivity;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    ArrayList<User> arrayList;
    Context context;

    public UserListAdapter(ArrayList<User> arrayList) {
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.itemLayout.setOnClickListener(v->{
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("uid", arrayList.get(position).getUid());
            intent.putExtra("name", arrayList.get(position).getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, lastMsg, time;
        CardView itemLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_user_tv_name);
            lastMsg = itemView.findViewById(R.id.item_user_tv_last_msg);
            time = itemView.findViewById(R.id.item_user_tv_time);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}
