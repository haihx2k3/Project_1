package com.example.project_1_java.Chat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_1_java.Model.ChatMessageModel;
import com.example.project_1_java.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChatAdapter extends FirestoreRecyclerAdapter<ChatMessageModel,ChatAdapter.ChatViewHolder> {
    private final Context context;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    public ChatAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatMessageModel model) {
        if (model.getSenderId().equals(currentUser != null ? currentUser.getUid() : null)) {
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextView.setText(model.getMessage());
        } else {
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextView.setText(model.getMessage());
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_layout,parent,false);
        return new ChatViewHolder(view);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{
        View leftChatLayout,rightChatLayout;
        TextView leftChatTextView,rightChatTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.lnChatLeft);
            rightChatLayout = itemView.findViewById(R.id.lnChatRight);
            leftChatTextView = itemView.findViewById(R.id.txtChatLeft);
            rightChatTextView = itemView.findViewById(R.id.txtChatRight);

        }
    }
}
