package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.w3c.dom.Text;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private static final String TAG = "TweetsAdapter";

    private Context context;
    private List<Tweet> tweets;
    private TwitterClient client;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
        client = TwitterApp.getRestClient(context);
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

        private Tweet tweetBinded;
        private ImageView ivProfileImage;
        private TextView tvBody;
        private TextView tvScreenName;
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvTime;
        private ImageButton btnReply;
        private ImageButton btnRetweet;
        private ImageButton btnLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            btnReply = itemView.findViewById(R.id.btnReply);
            btnRetweet = itemView.findViewById(R.id.btnRetweet);
            btnLike = itemView.findViewById(R.id.btnLike);

            setOnClickListeners();
        }

        private void bind(Tweet tweet) {
            this.tweetBinded = tweet;

            tvBody.setText(tweet.getBody());
            tvScreenName.setText(tweet.getUser().getName());
            tvName.setText("@" + tweet.getUser().getScreenName());

            tvTime.setText("• " + tweet.getRelativeTimeAgo());

            btnLike.setSelected(tweet.isFavorited());
            btnRetweet.setSelected(tweet.isRetweeted());

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

        private void reTweet(){
            Toast.makeText(context,"ReTweet!",Toast.LENGTH_SHORT).show();
            btnRetweet.setSelected(true);
            client.reTweet(tweetBinded.getId(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess ReTweet!");
                    tweets.get(getAdapterPosition()).setRetweeted(true);
                    notifyItemChanged(getAdapterPosition());
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "ReTweet Failed!", throwable);
                }
            });
        }

        private void like(){
            Toast.makeText(context,"Like!",Toast.LENGTH_SHORT).show();
            btnLike.setSelected(true);
            client.likeTweet(tweetBinded.getId(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess Like!");
                    tweets.get(getAdapterPosition()).setFavorited(true);
                    notifyItemChanged(getAdapterPosition());
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "ReTweet Failed!", throwable);
                }
            });
        }

        private void reply(){
            Toast.makeText(context,"Reply!",Toast.LENGTH_SHORT).show();
        }

        private void setOnClickListeners(){
            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reply();
                }
            });

            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reTweet();
                }
            });

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    like();
                }
            });
        }
    }
}
