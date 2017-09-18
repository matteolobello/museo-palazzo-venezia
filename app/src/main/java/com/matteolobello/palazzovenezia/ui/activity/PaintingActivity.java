package com.matteolobello.palazzovenezia.ui.activity;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetImageSetter;
import com.matteolobello.palazzovenezia.data.asset.AssetSoundManager;
import com.matteolobello.palazzovenezia.data.model.Painting;
import com.matteolobello.palazzovenezia.data.service.AppRemovedFromRecentAppsListDetectorService;
import com.matteolobello.palazzovenezia.ui.scroll.ScrollHandler;
import com.matteolobello.palazzovenezia.ui.systembar.SystemBars;
import com.matteolobello.palazzovenezia.util.DpPxUtil;

import app.minimize.com.seek_bar_compat.SeekBarCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import rm.com.youtubeplayicon.PlayIconDrawable;
import rm.com.youtubeplayicon.PlayIconView;

public class PaintingActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private static final int NOTIFICATION_ID = 340;

    private static final IntentFilter RECEIVER_INTENT_FILTER
            = new IntentFilter(PaintingActivity.class.getPackage() + ".TOGGLE_AUDIO");

    private final PlayPauseBroadcastReceiver mPlayPauseNotificationActionReceiver = new PlayPauseBroadcastReceiver();

    private final Handler mUpdateSeekBarHandler = new Handler();

    private final Runnable mEverySecondRunnable = new EverySecondRunnable();

    @BindView(R.id.nested_scroll_view)             protected NestedScrollView        mNestedScrollView;
    @BindView(R.id.painting_image_view)            protected ImageView               mPaintingImageView;
    @BindView(R.id.fabtoolbar_fab)                 protected FloatingActionButton    mHeadsetFab;
    @BindView(R.id.fabtoolbar_toolbar)             protected View                    mFabToolbar;
    @BindView(R.id.toolbar)                        protected Toolbar                 mToolbar;
    @BindView(R.id.toolbar_layout)                 protected CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.painting_description_text_view) protected TextView                mDescriptionTextView;
    @BindView(R.id.play_pause)                     protected PlayIconView            mPlayIconView;
    @BindView(R.id.seek_bar)                       protected SeekBarCompat           mSeekBar;
    @BindView(R.id.fab_toolbar)                    protected FABToolbarLayout        mFabToolbarLayout;

    private Intent mServiceIntent;

    private Painting mPainting;

    private AssetSoundManager mSoundManager;

    private final View.OnClickListener mPlayPauseClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MediaPlayer mediaPlayer = mSoundManager.getMediaPlayer();
            if (mediaPlayer != null) {
                boolean isPlaying = mediaPlayer.isPlaying() && !mSoundManager.isFinished();
                if (isPlaying) {
                    mediaPlayer.pause();

                    if (mPlayIconView.getIconState() != PlayIconDrawable.IconState.PLAY) {
                        mPlayIconView.animateToState(PlayIconDrawable.IconState.PLAY);
                    }

                    handleNotification();

                    return;
                }
            }

            mSoundManager.playAudio(getApplicationContext(), mPainting, PaintingActivity.this, 0);

            if (mPlayIconView.getIconState() != PlayIconDrawable.IconState.PAUSE) {
                mPlayIconView.animateToState(PlayIconDrawable.IconState.PAUSE);
            }

            handleNotification();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting);

        ButterKnife.bind(this);

        SystemBars.setFullyTransparentStatusBar(this);

        mServiceIntent = new Intent(getApplicationContext(), AppRemovedFromRecentAppsListDetectorService.class);

        registerReceiver(mPlayPauseNotificationActionReceiver, RECEIVER_INTENT_FILTER);
        startService(mServiceIntent);

        mPainting = getIntent().getParcelableExtra("painting");

        mSoundManager = AssetSoundManager.get();

        mToolbar.setTitle(mPainting.getName().replace("_", " "));
        setSupportActionBar(mToolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPaintingImageView.setTransitionName("painting");
            mHeadsetFab.setTransitionName("fab");

            ActivityManager.TaskDescription taskDescription =
                    new ActivityManager.TaskDescription(mPainting.getName(),
                            BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                            ContextCompat.getColor(this, R.color.colorPrimary));
            setTaskDescription(taskDescription);
        }

        AssetImageSetter.setImageByPaintingId(mPaintingImageView, mPainting.getId());
        mPaintingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FullscreenPaintingActivity.class);
                intent.putExtra("painting_path", "img_" + mPainting.getId());
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(PaintingActivity.this, view, "painting");
                startActivity(intent, options.toBundle());
            }
        });

        mDescriptionTextView.setText(mPainting.getDescription());

        mHeadsetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFabToolbarLayout.isToolbar()) {
                    return;
                }

                SystemBars.setNavigationBarColor(PaintingActivity.this,
                        ContextCompat.getColor(getApplicationContext(), R.color.colorAccentDark), true);
                mFabToolbarLayout.show();

                changeNestedScrollViewPadding(DpPxUtil.convertDpToPixel(56 + 12));
            }
        });

        mPlayIconView.setOnClickListener(mPlayPauseClickListener);

        mSeekBar.setProgressColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentDark));
        mSeekBar.setProgressBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        mSeekBar.setThumbColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentDark));
        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MediaPlayer exoPlayer = mSoundManager.getMediaPlayer();

                int progress = seekBar.getProgress();

                long newDuration = exoPlayer != null
                        ? progress != 0 ? ((progress * exoPlayer.getDuration()) / 100) : 1
                        : 1;

                mSoundManager.playAudio(getApplicationContext(), mPainting, PaintingActivity.this, newDuration);

                if (mPlayIconView.getIconState() != PlayIconDrawable.IconState.PAUSE) {
                    mPlayIconView.animateToState(PlayIconDrawable.IconState.PAUSE);
                }

                everySecondUIUpdater(true);

                handleNotification();
            }
        });

        ScrollHandler.setOnScrollListener(mNestedScrollView, new ScrollHandler.OnScrollListener() {
            @Override
            public void onScroll(@ScrollHandler.ScrollDirection int scrollDirection) {
                if (scrollDirection == ScrollHandler.UP) {
                    mHeadsetFab.show();
                } else {
                    mHeadsetFab.hide();
                }
            }
        });

        mUpdateSeekBarHandler.post(mEverySecondRunnable);
    }

    private void changeNestedScrollViewPadding(int newBottomPadding) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mNestedScrollView.getPaddingBottom(), newBottomPadding);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mNestedScrollView.setPadding(
                        mNestedScrollView.getPaddingStart(),
                        mNestedScrollView.getPaddingTop(),
                        mNestedScrollView.getPaddingEnd(),
                        ((int) animation.getAnimatedValue()));
            }
        });
        valueAnimator.start();
    }

    private void handleNotification() {
        Intent notificationIntent = new Intent(getApplicationContext(), PaintingActivity.class).putExtra("painting", mPainting);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 90, notificationIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.ic_headset)
                        .setContentTitle(mPainting.getName())
                        .setContentIntent(pendingIntent);
        notificationBuilder.addAction(new NotificationCompat.Action(0,
                getString(mPlayIconView.getIconState() == PlayIconDrawable.IconState.PAUSE
                        ? R.string.play
                        : R.string.pause),
                PendingIntent.getBroadcast(getApplicationContext(), 0,
                        new Intent(RECEIVER_INTENT_FILTER.getAction(0)),
                        PendingIntent.FLAG_CANCEL_CURRENT)));

        Notification notification = notificationBuilder.build();

        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mPlayPauseNotificationActionReceiver);
        stopService(mServiceIntent);

        mSoundManager.releasePlayer();

        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mFabToolbarLayout.isToolbar()) {
            mFabToolbarLayout.hide();

            changeNestedScrollViewPadding(DpPxUtil.convertDpToPixel(16));

            SystemBars.setNavigationBarColor(this, ContextCompat.getColor(getApplicationContext(), android.R.color.black), true);

            return;
        }

        AssetImageSetter.setImageByPaintingId(mPaintingImageView, mPainting.getId());

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void everySecondUIUpdater(boolean dontUpdateSeekBar) {
        if (isDestroyed()) {
            mUpdateSeekBarHandler.removeCallbacks(mEverySecondRunnable);

            return;
        }

        MediaPlayer mediaPlayer = mSoundManager.getMediaPlayer();
        if (mediaPlayer != null) {
            if (!dontUpdateSeekBar) {
                if (mSeekBar != null) {
                    mSeekBar.setProgress(mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration());
                }
            }
        }

        mUpdateSeekBarHandler.postDelayed(mEverySecondRunnable, 1000);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mPlayIconView.getIconState() != PlayIconDrawable.IconState.PLAY) {
            mPlayIconView.animateToState(PlayIconDrawable.IconState.PLAY);
        }

        mSoundManager.setAudioFinished(true);
    }

    private class EverySecondRunnable implements Runnable {

        @Override
        public void run() {
            everySecondUIUpdater(false);
        }
    }

    public class PlayPauseBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mPlayPauseClickListener.onClick(null);
        }
    }
}
