package com.bim.posts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bim.posts.databinding.ItemPostBinding;
import com.bim.posts.fragment.PostFragment;
import com.bim.posts.model.Post;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements Filterable {
    private final ArrayList<Post> postsListBK;
    public ArrayList<Post> postsList;
    private final Context context;
    PostFragment postFragment;

    Filter itemFilter;

    public PostAdapter(ArrayList<Post> postsList, Context context, PostFragment postFragment) {
        this.postsListBK = postsList;
        this.postsList = postsList;
        this.postFragment = postFragment;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;

        ViewHolder(ItemPostBinding itemPostBinding) {
            super(itemPostBinding.getRoot());
            binding = itemPostBinding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postsList.get(holder.getBindingAdapterPosition());

        holder.binding.tvTitlePost.setText(post.getTitle());
        holder.binding.tvBodyPost.setText(post.getBody());
        holder.binding.ivComments.setOnClickListener(view -> postFragment.showComments(post.getId()));
        holder.binding.ivPhotos.setOnClickListener(view -> postFragment.showPhotos(post.getId()));
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


    @Override
    public Filter getFilter() {
        if(itemFilter == null) itemFilter = new PostAdapter.ItemFilter();
        return itemFilter;
    }

    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String query = charSequence.toString().trim().toLowerCase();
            FilterResults filterResults = new FilterResults();
            ArrayList<Post> list = postsListBK;
            ArrayList<Post> resultList = new ArrayList<>();

            int count = list.size();
            boolean addItem;
            for (int i = 0; i < count; i++){
                Post item = list.get(i);

                //query by title or body
                if(!query.isEmpty()){
                    addItem = (item.getTitle().trim().toLowerCase().contains(query)||
                            item.getBody().trim().toLowerCase().contains(query));
                }else{
                    addItem = true;
                }
                if(addItem) resultList.add(item);
            }

            filterResults.values = resultList;
            filterResults.count = resultList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            postsList = (ArrayList<Post>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
