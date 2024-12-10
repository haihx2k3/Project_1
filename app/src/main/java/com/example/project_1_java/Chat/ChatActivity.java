package com.example.project_1_java.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.project_1_java.Chat.Adapter.ChatAdapter;
import com.example.project_1_java.Chat.Presenter.ChatContract;
import com.example.project_1_java.Chat.Presenter.ChatPresenter;
import com.example.project_1_java.Model.ChatMessageModel;
import com.example.project_1_java.R;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.example.project_1_java.databinding.ActivityChatBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ChatActivity extends AppCompatActivity implements ChatContract.View {
    private ActivityChatBinding binding;
    private ChatPresenter presenter;
    private ChatAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String sellerId = getIntent().getStringExtra("sellerId");
        String userName = getIntent().getStringExtra("userName");
        String avatar = getIntent().getStringExtra("avatar");
        presenter = new ChatPresenter(this,sellerId,userName,avatar);
        presenter.setupChat(userName,avatar);
        setupListener();
        setUpChat();
    }

    private void setUpChat() {
        Query query = new FirebaseUtil().getChatRoomMessageReferences(presenter.getChatRoomId())
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class)
                .build();

        adapter = new ChatAdapter(options, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        binding.rcChat.setLayoutManager(manager);
        binding.rcChat.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                binding.rcChat.smoothScrollToPosition(0);
            }
        });
    }

    private void setupListener() {
        binding.imgBack.setOnClickListener(v->finish());
        binding.imgSent.setOnClickListener(view -> {
            String message = binding.messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                presenter.sendMessage(message);
                binding.messageInput.setText("");
            }
        });
    }

    @Override
    public void displayUserName(String userName,String avatar) {
        binding.txtUser.setText(userName);
        Glide.with(this).load(avatar).placeholder(R.drawable.profile).into(binding.cirAvtChat);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }

}