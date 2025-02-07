package com.example.project_1_java.Chat.Presenter;

import com.example.project_1_java.Model.ChatModel;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;

import java.util.Arrays;
import java.util.HashMap;

public class ChatPresenter implements ChatContract.Presenter{
    private final ChatContract.View view;
    private final String userId,avatar,userName;
    private final FirebaseUser currentUser;
    private FieldValue fieldValue;
    private String chatRoomId;
    private ChatModel chat;
    public ChatPresenter(ChatContract.View view, String userId, String userName ,String avatar){
        this.view = view;
        this.userId = userId;
        this.userName = userName;
        this.avatar = avatar;
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.chatRoomId = new FirebaseUtil().getChatRoomId(currentUser.getUid(),userId);
        this.fieldValue = FieldValue.serverTimestamp();
    }
    @Override
    public void setupChat(String userName,String avatar) {
        view.displayUserName(userName,avatar);
        getOrCreateChatRoomModel();
    }
    public String getChatRoomId(){
        return chatRoomId;
    }
    private void getOrCreateChatRoomModel() {
        new FirebaseUtil().getChatRoomReference(chatRoomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chat = task.getResult().toObject(ChatModel.class);
                if (chat == null) {
                    chat = new ChatModel(
                            chatRoomId,
                            Arrays.asList(currentUser.getUid(), userId),
                            fieldValue,
                            ""
                    );
                    new FirebaseUtil().getChatRoomReference(chatRoomId).set(chat);
                }
            }
        });
    }

    @Override
    public void sendMessage(String message) {
        chat.setTimeStamp(fieldValue);
        chat.setLastMessageSenderId(currentUser.getUid());
        chat.setLastMessage(message);
        new FirebaseUtil().getChatRoomReference(chatRoomId).set(chat);
        HashMap<String, Object> chatMessage = new HashMap<>();
        chatMessage.put("message", message);
        chatMessage.put("senderId", currentUser.getUid());
        chatMessage.put("timestamp", FieldValue.serverTimestamp());
        new FirebaseUtil().getChatRoomMessageReferences(chatRoomId).add(chatMessage);
    }
}
