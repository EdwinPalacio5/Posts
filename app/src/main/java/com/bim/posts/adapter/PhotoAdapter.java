package com.bim.posts.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bim.posts.R;
import com.bim.posts.databinding.ItemPhotoBinding;
import com.bim.posts.model.Photo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private final ArrayList<Photo> photosListBK;
    public ArrayList<Photo> photosList;
    private final Context context;
    private final Activity activity;

    public PhotoAdapter(ArrayList<Photo> photosList, Context context, Activity activity) {
        this.photosListBK = photosList;
        this.photosList = photosList;
        this.context = context;
        this.activity = activity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemPhotoBinding binding;

        ViewHolder(ItemPhotoBinding itemPhotoBinding) {
            super(itemPhotoBinding.getRoot());
            binding = itemPhotoBinding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPhotoBinding binding = ItemPhotoBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = photosList.get(holder.getBindingAdapterPosition());
        GlideUrl url = new GlideUrl(photo.getThumbnailUrl(), new LazyHeaders.Builder()
                .addHeader("User-Agent", "user-agent")
                .build());
        Glide.with(context)
                .load(url)
                .placeholder(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_default,null))
                .into(holder.binding.photo);
    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }
}
