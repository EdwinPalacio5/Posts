package com.bim.posts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.bim.posts.API.APIMgr;
import com.bim.posts.databinding.ActivityMainBinding;
import com.bim.posts.fragment.PostFragment;
import com.bim.posts.utils.AppMgr;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

        AppMgr.ApiMgr = APIMgr.getInstance(this);

        startPostFragment();
    }

    private void startPostFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(binding.baseContainer.getId(), new PostFragment(), "POSTS");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
    }
}