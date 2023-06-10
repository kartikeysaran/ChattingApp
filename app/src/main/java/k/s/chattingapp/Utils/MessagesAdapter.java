package k.s.chattingapp.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import k.s.chattingapp.Model.Message;
import k.s.chattingapp.R;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    ArrayList<Message> arrayList;
    int RECEIVE = 1, SEND = 0;

    public MessagesAdapter(ArrayList<Message> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sender_msg, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receiver_msg, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder holder, int position) {
        holder.msg.setText(arrayList.get(position).getMessage());
        holder.time.setText(arrayList.get(position).getTimestamp());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView msg;
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.item_msg_title);
            time = itemView.findViewById(R.id.item_msg_time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message currMsg = arrayList.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(currMsg.getSenderId())) {
            return SEND;
        } else
            return RECEIVE;
        }
    }
