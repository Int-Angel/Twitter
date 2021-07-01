package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {

    private static final String TAG = "TweetDetails";

    private ImageView ivProfileImage;
    private TextView tvScreenName;
    private TextView tvName;
    private TextView tvBody;
    private ImageView ivImage;
    private TextView tvTime;
    private ImageButton btnReply;
    private ImageButton btnRetweet;
    private ImageButton btnLike;

    private Tweet tweet;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        ivProfileImage = findViewById(R.id.ivProfileImage_details);
        tvScreenName = findViewById(R.id.tvScreenName_details);
        tvName = findViewById(R.id.tvName_details);
        tvBody = findViewById(R.id.tvBody_details);
        ivImage = findViewById(R.id.ivImage_details);
        tvTime = findViewById(R.id.tvTime_details);
        btnReply = findViewById(R.id.btnReply_details);
        btnRetweet = findViewById(R.id.btnRetweet_details);
        btnLike = findViewById(R.id.btnLike_details);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        client = TwitterApp.getRestClient(this);

        setOnClickListeners();
        initializeTweet();
    }

    private void reTweet() {
        if (!tweet.isRetweeted()) {
            Toast.makeText(this, "ReTweet!", Toast.LENGTH_SHORT).show();
            btnRetweet.setSelected(true);
            client.reTweet(tweet.getId(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess ReTweet!");
                    //tweets.get(getAdapterPosition()).setRetweeted(true);
                    //notifyItemChanged(getAdapterPosition());
                    tweet.setRetweeted(true);
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "ReTweet Failed!", throwable);
                }
            });
        } else {
            Toast.makeText(this, "unreTweet!", Toast.LENGTH_SHORT).show();
            btnRetweet.setSelected(false);
            client.unreTweet(tweet.getId(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess unreTweet!");
                    //tweets.get(getAdapterPosition()).setRetweeted(false);
                    //notifyItemChanged(getAdapterPosition());
                    tweet.setRetweeted(false);
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "unRrTweet Failed!", throwable);
                }
            });
        }

    }

    private void like() {
        if (!tweet.isFavorited()) {
            Toast.makeText(this, "Like!", Toast.LENGTH_SHORT).show();
            btnLike.setSelected(true);
            client.likeTweet(tweet.getId(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess Like!");
                    //tweets.get(getAdapterPosition()).setFavorited(true);
                    //notifyItemChanged(getAdapterPosition());
                    tweet.setFavorited(true);
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "like Failed!", throwable);
                }
            });
        } else {
            Toast.makeText(this, "unLike!", Toast.LENGTH_SHORT).show();
            btnLike.setSelected(false);
            client.unlikeTweet(tweet.getId(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess unLike!");
                    //tweets.get(getAdapterPosition()).setFavorited(false);
                    //notifyItemChanged(getAdapterPosition());
                    tweet.setFavorited(false);
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "unlike Failed!", throwable);
                }
            });
        }
    }

    private void reply() {
        Toast.makeText(this, "Reply!", Toast.LENGTH_SHORT).show();
    }

    private void setOnClickListeners() {
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

    private void initializeTweet() {
        tvBody.setText(tweet.getBody());
        tvScreenName.setText(tweet.getUser().getName());
        tvName.setText("@" + tweet.getUser().getScreenName());

        //tvTime.setText("• " + tweet.getRelativeTimeAgo());
        tvTime.setText(tweet.getRelativeTimeAgo2());

        btnLike.setSelected(tweet.isFavorited());
        btnRetweet.setSelected(tweet.isRetweeted());

        Glide.with(this)
                .load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCorners(200))
                .into(ivProfileImage);


        if (!tweet.getImageUrl().equals("")) {
            ivImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(tweet.getImageUrl())
                    .transform(new RoundedCorners(20))
                    .into(ivImage);
        } else {
            ivImage.setVisibility(View.GONE);
        }
    }
}