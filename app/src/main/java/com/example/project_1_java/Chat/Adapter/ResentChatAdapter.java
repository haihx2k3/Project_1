package com.example.project_1_java.Chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_1_java.Chat.ChatActivity;
import com.example.project_1_java.Funcion.AndroidUtils;
import com.example.project_1_java.Funcion.FormatDate;
import com.example.project_1_java.Model.ChatModel;
import com.example.project_1_java.Model.UserModel;
import com.example.project_1_java.R;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ResentChatAdapter extends FirestoreRecyclerAdapter<ChatModel, ResentChatAdapter.ResentChatViewHolder> {
    private final Context context;
    public ResentChatAdapter(@NonNull FirestoreRecyclerOptions<ChatModel> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ResentChatViewHolder holder, int position, @NonNull ChatModel model) {
        FirebaseUtil.getOtherUserFromChatroom(model.getUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                boolean lastMessageByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUserId());
                UserModel otherUserModel = task.getResult().toObject(UserModel.class);
                holder.userName.setText(otherUserModel.getUserName());
                if (lastMessageByMe){
                    holder.lastMessage.setText("Bạn: "+ model.getLastMessage());
                }else {
                    holder.lastMessage.setText(model.getLastMessage());
                }
                holder.lastTime.setText(FormatDate.timeStampToString(model.getTimeStamp()));
                holder.itemView.setOnClickListener(v->{
                    Intent intent = new Intent(context, ChatActivity.class);
                    AndroidUtils.passUserModelAsIntent(intent,otherUserModel);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
                Glide.with(holder.itemView).load(otherUserModel.getImgProfile()).placeholder(R.drawable.profile).into(holder.profilePic);
            }
        });
    }

    @NonNull
    @Override
    public ResentChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resent_chat,parent,false);
        return new ResentChatViewHolder(view);
    }

    public static class ResentChatViewHolder extends RecyclerView.ViewHolder{
        TextView userName,lastMessage,lastTime;
        ImageView profilePic;

        public ResentChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.txtUserNameRs);
            lastMessage = itemView.findViewById(R.id.txtLastMes);
            lastTime = itemView.findViewById(R.id.txtLastTime);
            profilePic = itemView.findViewById(R.id.cirResentChat);

        }
    }
}
