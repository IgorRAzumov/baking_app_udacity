package udacity.com.baking_app.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class StepMedia implements Parcelable {
    private final String videoUrl;
    private final String thumbnailURL;

    public static StepMedia initStepMedia(Step step) {
        return new StepMedia(step.getVideoURL(), step.getThumbnailURL());
    }

    private StepMedia(String videoUrl, String thumbnailURL) {
        this.videoUrl = videoUrl;
        this.thumbnailURL = thumbnailURL;
    }

    public boolean containsVideo() {
        return videoUrl != null && !TextUtils.isEmpty(videoUrl);
    }

    public boolean containsVideoThumb() {
        return thumbnailURL != null && !TextUtils.isEmpty(thumbnailURL);
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoUrl);
        dest.writeString(this.thumbnailURL);
    }

    private StepMedia(Parcel in) {
        this.videoUrl = in.readString();
        this.thumbnailURL = in.readString();
    }

    public static final Parcelable.Creator<StepMedia> CREATOR = new Parcelable.Creator<StepMedia>() {
        @Override
        public StepMedia createFromParcel(Parcel source) {
            return new StepMedia(source);
        }

        @Override
        public StepMedia[] newArray(int size) {
            return new StepMedia[size];
        }
    };
}
