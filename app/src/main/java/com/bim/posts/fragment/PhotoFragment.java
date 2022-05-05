package com.bim.posts.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.bim.posts.adapter.CommentAdapter;
import com.bim.posts.adapter.PhotoAdapter;
import com.bim.posts.databinding.FragmentPhotoBinding;
import com.bim.posts.model.Comment;
import com.bim.posts.model.Photo;
import com.bim.posts.utils.AppMgr;
import com.bim.posts.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class PhotoFragment extends Fragment {

    FragmentPhotoBinding binding;
    private static final String ARG_PARAM_POST_ID = "postId";
    private int postId;

    ArrayList<Photo> photos;
    PhotoAdapter photoAdapter;

    public PhotoFragment() {
        // Required empty public constructor
    }

    public static PhotoFragment newInstance(int postId) {
        PhotoFragment fragment = new PhotoFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPhotoBinding.inflate(inflater, container, false);
        binding.back.setOnClickListener(view -> actionBack());
        getPhotosByPostId(postId);
        return binding.getRoot();
    }

    private void getPhotosByPostId(int postId) {
        AppMgr.requestGetWithCallback((response, statusCode, errorMessage) -> {
            System.out.println(response);
            if(statusCode == Common._success_request){
                try {
                    JSONArray photosResponse = new JSONArray(response);
                    int countPhotos = photosResponse.length();
                    if(countPhotos > 0){
                        int i;
                        photos = new ArrayList<>();
                        JSONObject photoResponse;
                        for(i = 0; i < countPhotos; i++){
                            photoResponse = photosResponse.getJSONObject(i);

                            Photo photo = new Photo();
                            photo.setAlbumId(photoResponse.getInt("albumId"));
                            photo.setId(photoResponse.getInt("id"));
                            photo.setTitle(photoResponse.getString("title"));
                            photo.setUrl(photoResponse.getString("url"));
                            photo.setThumbnailUrl(photoResponse.getString("thumbnailUrl"));

                            photos.add(photo);
                        }
                        renderPhotos(photos);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }else{
                //
            }
        }, String.format(Locale.getDefault(), Common.URL_PHOTOS, postId), Request.Method.GET);

    }

    private void renderPhotos(ArrayList<Photo> photos) {
        photoAdapter = new PhotoAdapter(photos, requireContext(), requireActivity());
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.rvPhotos.setItemAnimator(null);
        binding.rvPhotos.setAdapter(photoAdapter);
        binding.rvPhotos.setLayoutManager(linearLayoutManager);
    }

    private void actionBack() {
        getParentFragmentManager().beginTransaction().remove(this).commit();
    }
}