package com.bim.posts.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.Request;
import com.bim.posts.adapter.PostAdapter;
import com.bim.posts.databinding.FragmentPostBinding;
import com.bim.posts.model.Post;
import com.bim.posts.utils.AppMgr;
import com.bim.posts.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostFragment extends Fragment {

    FragmentPostBinding binding;
    ArrayList<Post> posts = new ArrayList<>();
    PostAdapter postAdapter;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        binding.svSearch.setOnQueryTextListener(setupSearch());
        getAllPost();
        return binding.getRoot();
    }

    private SearchView.OnQueryTextListener setupSearch() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(postAdapter != null){
                    postAdapter.getFilter().filter(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(postAdapter != null){
                    postAdapter.getFilter().filter(s);
                }
                return false;
            }
        };
    }

    private void getAllPost() {
        AppMgr.requestGetWithCallback((response, statusCode, errorMessage) -> {
            System.out.println(response);

            if(statusCode == Common._success_request){
                try {
                    JSONArray postsResponse = new JSONArray(response);
                    int countPosts = postsResponse.length();
                    if(countPosts > 0){
                        int i;
                        posts = new ArrayList<>();
                        JSONObject postResponse;
                        for(i = 0; i < countPosts; i++){
                            postResponse = postsResponse.getJSONObject(i);

                            Post post = new Post();
                            post.setUserId(postResponse.getInt("userId"));
                            post.setId(postResponse.getInt("id"));
                            post.setTitle(postResponse.getString("title"));
                            post.setBody(postResponse.getString("body"));

                            posts.add(post);
                        }
                        renderPosts(posts);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }else{
                //
            }
        }, Common.URL_POSTS, Request.Method.GET);
    }

    private void renderPosts(ArrayList<Post> posts) {
        postAdapter = new PostAdapter(posts, requireContext(), this);

        binding.rvPosts.setItemAnimator(null);
        binding.rvPosts.setAdapter(postAdapter);
        binding.rvPosts.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    public void showComments(int postId){
        CommentFragment commentFragment = CommentFragment.newInstance(postId);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(binding.basePost.getId(), commentFragment, "COMMENTS");
        fragmentTransaction.commit();
    }

    public void showPhotos(int postId){
        PhotoFragment photoFragment = PhotoFragment.newInstance(postId);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(binding.basePost.getId(), photoFragment, "PHOTOS");
        fragmentTransaction.commit();
    }

}