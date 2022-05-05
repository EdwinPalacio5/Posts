package com.bim.posts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bim.posts.databinding.ItemCommentBinding;
import com.bim.posts.model.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> implements Filterable {
    private final ArrayList<Comment> commentsListBK;
    public ArrayList<Comment> commentsList;
    private final Context context;

    Filter itemFilter;

    public CommentAdapter(ArrayList<Comment> commentsList, Context context) {
        this.commentsListBK = commentsList;
        this.commentsList = commentsList;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding binding;

        ViewHolder(ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.getRoot());
            binding = itemCommentBinding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding binding = ItemCommentBinding.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentsList.get(holder.getBindingAdapterPosition());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.binding.itemLayout.getLayoutParams();

        if(holder.getAbsoluteAdapterPosition()%2 == 0){
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            params.removeRule(RelativeLayout.ALIGN_PARENT_START);
        }else{
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            params.removeRule(RelativeLayout.ALIGN_PARENT_END);
        }

        holder.binding.itemLayout.setLayoutParams(params);

        holder.binding.tvTitlePost.setText(comment.getEmail());
        holder.binding.tvBodyPost.setText(comment.getBody());
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }


    @Override
    public Filter getFilter() {
        if(itemFilter == null) itemFilter = new CommentAdapter.ItemFilter();
        return itemFilter;
    }

    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String query = charSequence.toString().trim().toLowerCase();
            FilterResults filterResults = new FilterResults();
            ArrayList<Comment> list = commentsListBK;
            ArrayList<Comment> resultList = new ArrayList<>();

            int count = list.size();
            boolean addItem;
            for (int i = 0; i < count; i++){
                Comment item = list.get(i);

                //query by title or body
                if(!query.isEmpty()){
                    addItem = (item.getBody().trim().toLowerCase().contains(query)||
                            item.getEmail().trim().toLowerCase().contains(query));
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
            commentsList = (ArrayList<Comment>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
