package com.bim.posts.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.Request;
import com.bim.posts.adapter.CommentAdapter;
import com.bim.posts.databinding.FragmentCommentBinding;
import com.bim.posts.model.Comment;
import com.bim.posts.utils.AppMgr;
import com.bim.posts.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class CommentFragment extends Fragment {

    FragmentCommentBinding binding;
    private static final String ARG_PARAM_POST_ID = "postId";
    private int postId;

    ArrayList<Comment> comments;
    CommentAdapter commentAdapter;

    public CommentFragment() {
        // Required empty public constructor
    }


    public static CommentFragment newInstance(int postId) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getInt(ARG_PARAM_POST_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommentBinding.inflate(inflater, container, false);
        binding.back.setOnClickListener(view -> actionBack());
        binding.svSearch.setOnQueryTextListener(setupSearch());
        getCommentsByPostId(postId);
        return binding.getRoot();
    }

    private SearchView.OnQueryTextListener setupSearch() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(commentAdapter != null){
                    commentAdapter.getFilter().filter(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(commentAdapter != null){
                    commentAdapter.getFilter().filter(s);
                }
                return false;
            }
        };
    }

    private void getCommentsByPostId(int postId) {
        AppMgr.requestGetWithCallback((response, statusCode, errorMessage) -> {
            System.out.println(response);
            if(statusCode == Common._success_request){
                try {
                    JSONArray commentsResponse = new JSONArray(response);
                    int countComments = commentsResponse.length();
                    if(countComments > 0){
                        int i;
                        comments = new ArrayList<>();
                        JSONObject commentResponse;
                        for(i = 0; i < countComments; i++){
                            commentResponse = commentsResponse.getJSONObject(i);

                            Comment comment = new Comment();
                            comment.setPostId(commentResponse.getInt("postId"));
                            comment.setId(commentResponse.getInt("id"));
                            comment.setName(commentResponse.getString("name"));
                            comment.setBody(commentResponse.getString("body"));
                            comment.setEmail(commentResponse.getString("email"));

                            comments.add(comment);
                        }
                        renderComments(comments);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }else{
                //
            }
        }, String.format(Locale.getDefault(), Common.URL_COMMENTS, postId), Request.Method.GET);
    }

    private void renderComments(ArrayList<Comment> comments) {
        commentAdapter = new CommentAdapter(comments, requireContext());

        binding.rvComments.setItemAnimator(null);
        binding.rvComments.setAdapter(commentAdapter);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void actionBack() {
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }
}