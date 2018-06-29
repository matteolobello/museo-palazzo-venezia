package com.matteolobello.palazzovenezia.ui.activity;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.matteolobello.palazzovenezia.R;
import com.matteolobello.palazzovenezia.data.asset.AssetImageSetter;
import com.matteolobello.palazzovenezia.data.asset.AssetSoundManager;
import com.matteolobello.palazzovenezia.data.bundle.BundleKeys;
import com.matteolobello.palazzovenezia.data.model.Painting;
import com.matteolobello.palazzovenezia.data.service.AppRemovedFromRecentAppsListDetectorService;
import com.matteolobello.palazzovenezia.util.DpPxUtil;
import com.matteolobello.palazzovenezia.util.ScrollUtil;
import com.matteolobello.palazzovenezia.util.SystemBarsUtil;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import app.minimize.com.seek_bar_compat.SeekBarCompat;
import rm.com.youtubeplayicon.PlayIconDrawable;
import rm.com.youtubeplayicon.PlayIconView;

public class PaintingActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private static final int NOTIFICATION_ID = 340;
    private static final String NOTIFICATION_CHANNEL = "channel_01";

    private static final IntentFilter RECEIVER_INTENT_FILTER
            = new IntentFilter(PaintingActivity.class.getPackage() + ".TOGGLE_AUDIO");

    private final PlayPauseBroadcastReceiver mPlayPauseNotificationActionReceiver = new PlayPauseBroadcastReceiver();

    private final Handler mUpdateSeekBarHandler = new Handler();

    private final Runnable mEverySecondRunnable = new EverySecondRunnable();

    private NestedScrollView mNestedScrollView;
    private ImageView mPaintingImageView;
    private FloatingActionButton mHeadsetFab;
    private View mFabToolbar;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView mDescriptionTextView;
    private PlayIconView mPlayIconView;
    private SeekBarCompat mSeekBar;
    private FABToolbarLayout mFabToolbarLayout;

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

        SystemBarsUtil.setFullyTransparentStatusBar(this);
        SystemBarsUtil.setNavigationBarColor(this, ContextCompat.getColor(this, android.R.color.white), false);

        if (!SystemBarsUtil.hasNavigationBar(this)) {
            findViewById(R.id.navigation_bar_divider).setVisibility(View.GONE);
        }

        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        mPaintingImageView = findViewById(R.id.painting_image_view);
        mHeadsetFab = findViewById(R.id.fabtoolbar_fab);
        mFabToolbar = findViewById(R.id.fabtoolbar_toolbar);
        mToolbar = findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        mDescriptionTextView = findViewById(R.id.painting_description_text_view);
        mPlayIconView = findViewById(R.id.play_pause);
        mSeekBar = findViewById(R.id.seek_bar);
        mFabToolbarLayout = findViewById(R.id.fab_toolbar);

        mServiceIntent = new Intent(getApplicationContext(), AppRemovedFromRecentAppsListDetectorService.class);

        registerReceiver(mPlayPauseNotificationActionReceiver, RECEIVER_INTENT_FILTER);
        startService(mServiceIntent);

        mPainting = getIntent().getParcelableExtra(BundleKeys.EXTRA_PAINTING);

        mSoundManager = AssetSoundManager.get();

        mToolbar.setTitle(mPainting.getName());
        setSupportActionBar(mToolbar);

        Typeface font = ResourcesCompat.getFont(this, R.font.radnika);
        mCollapsingToolbarLayout.setCollapsedTitleTypeface(font);
        mCollapsingToolbarLayout.setExpandedTitleTypeface(font);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                intent.putExtra(BundleKeys.EXTRA_PAINTING_PATH, "img_" + mPainting.getId());
                startActivity(intent);
            }
        });

        mDescriptionTextView.setText(mPainting.getDescription());

        mHeadsetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFabToolbarLayout.isToolbar()) {
                    return;
                }

                SystemBarsUtil.setNavigationBarColor(PaintingActivity.this,
                        ContextCompat.getColor(getApplicationContext(), R.color.colorAccentDark), true);
                mFabToolbarLayout.show();

                if (SystemBarsUtil.hasNavigationBar(PaintingActivity.this)) {
                    findViewById(R.id.navigation_bar_divider).animate()
                            .alpha(0f)
                            .setDuration(250)
                            .start();
                }

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
                MediaPlayer mediaPlayer = mSoundManager.getMediaPlayer();

                int progress = seekBar.getProgress();

                long newDuration = mediaPlayer != null
                        ? progress != 0 ? ((progress * mediaPlayer.getDuration()) / 100) : 1
                        : 1;

                mSoundManager.playAudio(getApplicationContext(), mPainting, PaintingActivity.this, newDuration);

                if (mPlayIconView.getIconState() != PlayIconDrawable.IconState.PAUSE) {
                    mPlayIconView.animateToState(PlayIconDrawable.IconState.PAUSE);
                }

                everySecondUIUpdater(true);

                handleNotification();
            }
        });

        ScrollUtil.setOnScrollListener(mNestedScrollView, new ScrollUtil.OnScrollListener() {
            @Override
            public void onScroll(@ScrollUtil.ScrollDirection int scrollDirection) {
                if (scrollDirection == ScrollUtil.UP) {
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
        valueAnimator.setDuration(250);
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
        Intent notificationIntent = new Intent(getApplicationContext(), PaintingActivity.class).putExtra(BundleKeys.EXTRA_PAINTING, mPainting);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 90, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL)
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.ic_headset)
                        .setContentTitle(mPainting.getName())
                        .setContentIntent(pendingIntent);
        notificationBuilder.addAction(new NotificationCompat.Action(0,
                getString(mSoundManager.getMediaPlayer().isPlaying()
                        ? R.string.pause
                        : R.string.play),
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

            changeNestedScrollViewPadding(0);

            SystemBarsUtil.setNavigationBarColor(PaintingActivity.this,
                    ContextCompat.getColor(getApplicationContext(), android.R.color.white), true);

            if (SystemBarsUtil.hasNavigationBar(this)) {
                findViewById(R.id.navigation_bar_divider).animate()
                        .alpha(0.12f)
                        .setDuration(200)
                        .start();
            }

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
