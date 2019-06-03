package it.matteolobello.palazzovenezia.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Painting implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Painting> CREATOR = new Parcelable.Creator<Painting>() {
        @Override
        public Painting createFromParcel(Parcel in) {
            return new Painting(in);
        }

        @Override
        public Painting[] newArray(int size) {
            return new Painting[size];
        }
    };
    private String mId;
    private String mName;
    private String mDescription;
    private String mAudioPath;

    public Painting() {
    }

    public Painting(String id, String name, String description, String audioPath) {
        mId = id;
        mName = name.replace(".txt", "");
        mDescription = description;
        mAudioPath = audioPath;
    }

    protected Painting(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mDescription = in.readString();
        mAudioPath = in.readString();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name.replace(".txt", "");
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getAudioPath() {
        return mAudioPath;
    }

    public void setAudioPath(String audioPath) {
        mAudioPath = audioPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeString(mAudioPath);
    }
}