package com.zhangwx.z_utils.Z_AIDL;

import android.os.Parcel;
import android.os.Parcelable;

public class ContentData implements Parcelable {

    public String content;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
    }

    public void readFromParcel(Parcel in) {
        this.content = in.readString();
    }

    public ContentData() {
    }

    protected ContentData(Parcel in) {
        this.content = in.readString();
    }

    public static final Parcelable.Creator<ContentData> CREATOR = new Parcelable.Creator<ContentData>() {
        @Override
        public ContentData createFromParcel(Parcel source) {
            return new ContentData(source);
        }

        @Override
        public ContentData[] newArray(int size) {
            return new ContentData[size];
        }
    };
}
