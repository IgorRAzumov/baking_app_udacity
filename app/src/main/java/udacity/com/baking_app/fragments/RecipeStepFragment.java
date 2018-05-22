package udacity.com.baking_app.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.Objects;

import butterknife.BindView;
import udacity.com.baking_app.R;
import udacity.com.baking_app.data.Step;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class RecipeStepFragment extends BaseFragment {
    @BindView(R.id.fl_fragment_recipe_step_media_container)
    FrameLayout mediaContainerFrame;
    @BindView(R.id.plv_fragment_recipe_step_player_view)
    PlayerView playerView;
    @BindView(R.id.iv_fragment_recipe_step_default_image)
    ImageView defaultImageView;
    @BindView(R.id.cv_fragment_recipe_step_content)
    CardView descriptionCard;
    @BindView(R.id.tv_fragment_recipe_step_description)
    TextView stepDescriptionText;
    @BindView(R.id.vi_fragment_recipe_step_divider)
    View dividerView;
    @BindView(R.id.tv_fragment_recipe_step_nav_help)
    TextView navigationHelpText;


    private SimpleExoPlayer player;
    private OnFragmentInteractionListener fragmentInteractionListener;
    private Step step;
    private long savedPlayerPosition;
    private boolean playWhenReady;

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
        onAttachToParentFragment();
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putLong(getString(R.string.player_position_key), player.getContentPosition());
            outState.putBoolean(getString(R.string.player_play_when_ready_key), player.getPlayWhenReady());
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String videoUrl = step.getVideoURL();
        if (videoUrl == null || TextUtils.isEmpty(videoUrl)) {
            return;
        }

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentInteractionListener.showFullScreenMode();
            changePlayerScreenMode(MATCH_PARENT);
        } else {
            fragmentInteractionListener.showDefaultMode();
            changePlayerScreenMode(resources
                    .getInteger(R.integer.recipe_step_fr_player_def_size));
        }
    }

    private void onAttachToParentFragment() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof OnFragmentInteractionListener) {
            fragmentInteractionListener = (OnFragmentInteractionListener) parentFragment;

        }

        if (fragmentInteractionListener == null) {
            throw new RuntimeException(getString(
                    R.string.error_implements_fragment_interaction_listener));
        }
    }

    private void changePlayerScreenMode(int size) {
        LinearLayout.LayoutParams playerLayoutParams = (size == MATCH_PARENT)
                ? new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                : new LinearLayout.LayoutParams(size, MATCH_PARENT, 3);
       mediaContainerFrame.setLayoutParams(playerLayoutParams);

        if (size == MATCH_PARENT) {
            dividerView.setVisibility(View.GONE);
            descriptionCard.setVisibility(View.GONE);
            navigationHelpText.setVisibility(View.GONE);
        } else {
            dividerView.setVisibility(View.INVISIBLE);
            descriptionCard.setVisibility(View.VISIBLE);
            navigationHelpText.setVisibility(View.VISIBLE);
        }

    }

    private void showDefaultImage() {
        playerView.setVisibility(View.GONE);
        defaultImageView.setVisibility(View.VISIBLE);
    }

    private void loadThumbnail(String thumbnailUrl) {

    }

    private void releasePlayer() {
        player.stop();
        player.release();
        player = null;
        playerView.setPlayer(null);
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
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            player.setPlayWhenReady(playWhenReady);


            DataSource.Factory dataSource =
                    new DefaultHttpDataSourceFactory(
                            Util.getUserAgent(context, getString(R.string.app_name)));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSource)
                    .createMediaSource(Uri.parse(mediaUri));

            player.prepare(videoSource);
////////////////////////////////////////////////////////////////////////////////////////////////////
            if (savedPlayerPosition != getResources().getInteger(R.integer.no_saved_position)) {
                player.seekTo(savedPlayerPosition);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void showFullScreenMode();

        void showDefaultMode();
    }
}
