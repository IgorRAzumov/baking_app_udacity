package udacity.com.baking_app.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import butterknife.BindView;
import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Step;


public class RecipeStepFragment extends BaseFragment {
    @BindView(R.id.tv_fragment_recipe_step_description)
    TextView stepDescriptionText;
    @BindView(R.id.plv_fragment_recipe_step_player_view)
    PlayerView playerView;

    private SimpleExoPlayer player;
    private Step step;


    public RecipeStepFragment() {

    }

    public static RecipeStepFragment newInstance(@NonNull Bundle bundle) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putBundle(BUNDLE_FRAGMENT_PARAMS_KEY, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle stepBundle = Objects
                .requireNonNull(getArguments()).getBundle(BUNDLE_FRAGMENT_PARAMS_KEY);
        step = Objects.requireNonNull(stepBundle).getParcelable(getString(R.string.step_key));
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_recipe_step;
    }

    @Override
    protected void initUi() {
        stepDescriptionText.setText(step.getDescription());
    }


    private void checkVideoData() {
        String mediaUri = step.getVideoURL();
        if (mediaUri == null || TextUtils.isEmpty(mediaUri)) {
            showDefaultImage();
        } else {
            initPlayer(mediaUri);
        }

    }

    private void initPlayer(String mediaUri) {
        String thumbnailUrl = step.getThumbnailURL();
        if (thumbnailUrl == null || TextUtils.isEmpty(thumbnailUrl)) {
            playerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),
                    R.drawable.generic));
        } else {
            loadThumbnail(thumbnailUrl);
        }

        Context context = getContext();
        if (player == null && context != null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            playerView.setPlayer(player);
            player.setPlayWhenReady(false);

            DataSource.Factory dataSource =
                    new DefaultHttpDataSourceFactory(
                            Util.getUserAgent(context, getString(R.string.app_name)));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSource)
                    .createMediaSource(Uri.parse(mediaUri));

            player.prepare(videoSource);

        }
    }

    private void showDefaultImage() {
    }

    private void loadThumbnail(String thumbnailUrl) {

    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 && player == null) {
            checkVideoData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 && player == null) {
            checkVideoData();
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

    public void imVisibleNow() {
        if (step != null) {
            checkVideoData();
        }
    }

    public void imHiddenNow() {
        if (player != null ) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        player.stop();
        player.release();
        player = null;
        playerView.setPlayer(null);
    }


}
