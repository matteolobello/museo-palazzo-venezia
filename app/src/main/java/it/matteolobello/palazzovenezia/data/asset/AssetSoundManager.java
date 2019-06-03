package it.matteolobello.palazzovenezia.data.asset;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import it.matteolobello.palazzovenezia.data.model.Painting;

public class AssetSoundManager {

    private static AssetSoundManager sInstance;
    private MediaPlayer mMediaPlayer;
    private boolean mAudioFinished;

    public static AssetSoundManager get() {
        if (sInstance == null) {
            sInstance = new AssetSoundManager();
        }

        return sInstance;
    }

    public void playAudio(Context context, Painting painting,
                          MediaPlayer.OnCompletionListener onCompletionListener, long seekBarValue) {
        long seekTo = seekBarValue;
        if (mMediaPlayer != null && mMediaPlayer.getCurrentPosition() > 0 && seekTo == 0) {
            seekTo = mMediaPlayer.getCurrentPosition();
        }

        try {
            releasePlayer();

            mMediaPlayer = new MediaPlayer();

            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(painting.getAudioPath());
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            assetFileDescriptor.close();

            mMediaPlayer.prepare();
            mMediaPlayer.setVolume(1f, 1f);
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(onCompletionListener);

            if (seekTo > 0 || mAudioFinished) {
                if (!mAudioFinished) {
                    mMediaPlayer.seekTo((int) seekTo);
                } else {
                    mAudioFinished = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releasePlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.setOnCompletionListener(null);
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public boolean isFinished() {
        return mAudioFinished;
    }

    public void setAudioFinished(boolean audioFinished) {
        mAudioFinished = audioFinished;
    }
}
