package udacity.com.baking_app.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import udacity.com.baking_app.R;
import udacity.com.baking_app.data.StepMedia;


public class PlayerFragment extends BaseFragment {
    @BindView(R.id.plv_fragment_recipe_player_view)
    PlayerView playerView;
    @BindView(R.id.iv_fragment_recipe_step_default_image)
    ImageView defaultImageView;

    StepMedia stepMedia;
    private long savedPlayerPosition;
    private boolean playWhenReady;

    private Target thumbImageTarget;
    private SimpleExoPlayer player;

    public PlayerFragment() {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_player;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Resources resources = getResources();
            savedPlayerPosition = savedInstanceState
                    .getLong(getString(R.string.player_position_key),
                            (long) resources.getInteger(R.integer.no_saved_position));
            playWhenReady = savedInstanceState
                    .getBoolean(getString(R.string.player_play_when_ready_key),
                            resources.getBoolean(R.bool.play_when_ready_default));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 && player == null && stepMedia != null) {
            initMedia();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 && player == null && stepMedia != null) {
            initMedia();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putLong(getString(R.string.player_position_key), player.getContentPosition());
            outState.putBoolean(getString(R.string.player_play_when_ready_key), player.getPlayWhenReady());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23 && player != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 && player != null) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        thumbImageTarget = null;
    }

    private void initMedia() {
        if(player != null){
            releasePlayer();
        }

        if (stepMedia != null && stepMedia.containsVideo()) {
            showPlayer();
            initPlayer();
        } else {
            showDefaultImage();
        }
    }

    private void initPlayer() {
        Context context = getContext();
        if (player == null && context != null) {
            showVideoThumb();

            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            playerView.setPlayer(player);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            player.setPlayWhenReady(playWhenReady);

            DataSource.Factory dataSource =
                    new DefaultHttpDataSourceFactory(
                            Util.getUserAgent(context, getString(R.string.app_name)));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSource)
                    .createMediaSource(Uri.parse(stepMedia.getVideoUrl()));
            player.prepare(videoSource);

            if (savedPlayerPosition != getResources().getInteger(R.integer.no_saved_position)) {
                player.seekTo(savedPlayerPosition);
            }
        }
    }

    private void showVideoThumb() {
        if (!stepMedia.containsVideoThumb()) {
            showDefaultArtwork();
        } else {
            loadThumbnail(stepMedia.getThumbnailURL());

        }
    }

    private void showDefaultArtwork() {
        playerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),
                R.drawable.generic));
    }

    private void showPlayer() {
        defaultImageView.setVisibility(View.GONE);
        playerView.setVisibility(View.VISIBLE);

    }

    private void showDefaultImage() {
        playerView.setVisibility(View.GONE);
        defaultImageView.setVisibility(View.VISIBLE);
    }

    private void loadThumbnail(String thumbnailUrl) {
        thumbImageTarget = createThumbImageTarget();
        Picasso.with(getContext())
                .load(thumbnailUrl)
                .fit()
                .into(thumbImageTarget);
    }

    @NonNull
    private Target createThumbImageTarget() {
        return new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                playerView.setDefaultArtwork(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                showDefaultArtwork();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                showDefaultArtwork();
            }
        };
    }

    private void releasePlayer() {
        player.stop();
        player.release();
        player = null;
        playerView.setPlayer(null);
    }

    public void initMedia(StepMedia stepMedia) {
        this.stepMedia = stepMedia;
        initMedia();
    }
}
