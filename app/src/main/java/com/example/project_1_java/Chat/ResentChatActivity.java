package com.example.project_1_java.Chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project_1_java.Chat.Adapter.ResentChatAdapter;
import com.example.project_1_java.Model.ChatModel;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.example.project_1_java.databinding.ActivityResentChatBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ResentChatActivity extends AppCompatActivity {
    private ActivityResentChatBinding binding;
    private ResentChatAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResentChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setuplistener();
        setupSearchRecycleView();
    }

    private void setuplistener() {
        binding.imgBack.setOnClickListener(v->finish());
    }

    void setupSearchRecycleView(){
        Query query = FirebaseUtil.allChatRoomCollectionReference()
                .whereArrayContains("userId",FirebaseUtil.currentUserId())
                .orderBy("timeStamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatModel> options = new FirestoreRecyclerOptions.Builder<ChatModel>()
                .setQuery(query,ChatModel.class).build();
        adapter = new ResentChatAdapter(options,this);
        binding.rcChatResent.setLayoutManager(new LinearLayoutManager(this));
        binding.rcChatResent.setAdapter(adapter);
        adapter.startListening();

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
            adapter.notifyDataSetChanged();
        }
    }
}