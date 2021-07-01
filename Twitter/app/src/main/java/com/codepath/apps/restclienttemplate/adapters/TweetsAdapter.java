package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.w3c.dom.Text;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private Context context;
    private List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tweets.get(position));
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> tweets) {
        this.tweets = tweets;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProfileImage;
        private TextView tvBody;
        private TextView tvScreenName;
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
        }

        private void bind(Tweet tweet) {
            tvBody.setText(tweet.getBody());
            tvScreenName.setText(tweet.getUser().getName());
            tvName.setText("@" + tweet.getUser().getScreenName());

            tvTime.setText("• " + tweet.getRelativeTimeAgo());

            Glide.with(context)
                    .load(tweet.getUser().getProfileImageUrl())
                    .transform(new RoundedCorners(200))
                    .into(ivProfileImage);

            if (tweet.getImageUrl() != "") {
                ivImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(tweet.getImageUrl())
                        .transform(new RoundedCorners(20))
                        .into(ivImage);
            } else {
                ivImage.setVisibility(View.GONE);
            }
        }
    }
}
