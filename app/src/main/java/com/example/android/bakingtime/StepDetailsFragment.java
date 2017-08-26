package com.example.android.bakingtime;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingtime.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * The fragment for displaying the details of the steps in a recipe
 */
public class StepDetailsFragment extends Fragment {

    private static String STEPS = "steps";
    private static String INDEX = "steps";
    private static String SHOW_STEP_BUTTONS = "steps";

    private ArrayList<Step> mSteps;
    private int mIndex = -1;
    private boolean mShowStepButtons;
    private SimpleExoPlayer mExoPlayer;
    private ImageView mThumbnailView;
    private SimpleExoPlayerView mPlayerView;
    private TextView mStepDescriptionView;
    private TextView mStepShortDescriptionView;
    private LinearLayout mStepButtonContainer;
    private Button mPrevStepButton;
    private Button mNextStepButton;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        // Initialize the player view.
        mThumbnailView = (ImageView)view.findViewById(R.id.thumbnail);
        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);
        mStepShortDescriptionView = (TextView) view.findViewById(R.id.step_short_description);
        mStepDescriptionView = (TextView) view.findViewById(R.id.step_description);
        mStepButtonContainer = (LinearLayout) view.findViewById(R.id.step_button_container);
        mPrevStepButton = (Button) view.findViewById(R.id.prev_step_button);
        mPrevStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndex--;
                setStep(mSteps.get(mIndex));
            }
        });
        mNextStepButton = (Button) view.findViewById(R.id.next_step_button);
        mNextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndex++;
                setStep(mSteps.get(mIndex));
            }
        });
        initializePlayer();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPlayerView != null && mExoPlayer == null) {
            /*
             * this makes sure that if a video was playing when the activity was paused, it starts
             * playing after the activity is brought up again
             */
            initializePlayer();
            if (mSteps != null && mIndex >= 0) {
                setSteps(mSteps, mIndex,  mShowStepButtons);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(STEPS);
            mIndex = savedInstanceState.getInt(INDEX, -1);
            mShowStepButtons = savedInstanceState.getBoolean(SHOW_STEP_BUTTONS);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STEPS, mSteps);
        outState.putInt(INDEX, mIndex);
        outState.putBoolean(SHOW_STEP_BUTTONS, mShowStepButtons);
        super.onSaveInstanceState(outState);
    }

    /*
         * This method is called by activities to pass data to the fragment
         */
    public void setSteps(ArrayList<Step> steps, int index, boolean showStepButtons) {
        mSteps = steps;
        mIndex = index;
        mShowStepButtons = showStepButtons;
        if (mSteps != null && mIndex >= 0) {
            setStep(mSteps.get(mIndex));
        } else {
            setStep(null);
        }
        mStepButtonContainer.setVisibility(mShowStepButtons ? View.VISIBLE : View.GONE);
    }

    /**
     * Initialize ExoPlayer
     */
    private void initializePlayer() {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
        }
    }

    /*
     * Sets the data from the passed step into the widgets in the fragment
     */
    private void setStep(Step step) {
        Uri uri = null;
        boolean isThumbnail;
        isThumbnail = false;
        if (step != null) {
            if (!TextUtils.isEmpty(step.getVideoURL())) {
                uri = Uri.parse(step.getVideoURL());
            } else if (!TextUtils.isEmpty(step.getThumbnailURL())) {
                uri = Uri.parse(step.getThumbnailURL());
                isThumbnail = true;
            }
        }
        showMedia(uri, isThumbnail);
        mStepShortDescriptionView.setText(step != null ? step.getShortDescription() : null);
        mStepDescriptionView.setText(step != null ? step.getDescription() : null);
        if (mShowStepButtons) {
            mPrevStepButton.setEnabled(mIndex > 0);
            mNextStepButton.setEnabled(mIndex < mSteps.size()-1);
        }
    }

    /**
     * Shows the file located at the passed uri in the image view or ExoPlayer; isThumbnail, if true,
     * specifies that the file is an image file; otherwise it's a video file; if mediaUri is null,
     * shows the default image in the image view
     */
    private void showMedia(Uri mediaUri, boolean isThumbnail) {
        if (mediaUri != null) {
            if (isThumbnail) {
                hideExoPlayer();
                Picasso.with(getContext()).load(mediaUri.toString()).placeholder(R.drawable.cupcake).
                        error(R.drawable.cupcake).into(mThumbnailView);
            } else {
                mPlayerView.setVisibility(View.VISIBLE);
                mThumbnailView.setVisibility(View.GONE);
                // Prepare the MediaSource.
                String userAgent = Util.getUserAgent(this.getContext(), "BakingTime");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        this.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
            }
        } else {
            hideExoPlayer();
            Picasso.with(getContext()).load(R.drawable.cupcake).into(mThumbnailView);
        }
    }

    /**
     * Stops playback on ExoPlayer and hides it
     */
    private void hideExoPlayer() {
        mExoPlayer.stop();
        mExoPlayer.prepare(null);
        mPlayerView.setVisibility(View.GONE);
        mThumbnailView.setVisibility(View.VISIBLE);
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
