package com.example.flashcard;

import android.os.Parcel;
import android.os.Parcelable;

class Flashcard implements Parcelable {
    private String frontSide;
    private String backSide;

    protected Flashcard(Parcel in) {
        frontSide = in.readString();
        backSide = in.readString();
    }

    public static final Creator<Flashcard> CREATOR = new Creator<Flashcard>() {
        @Override
        public Flashcard createFromParcel(Parcel in) {
            return new Flashcard(in);
        }

        @Override
        public Flashcard[] newArray(int size) {
            return new Flashcard[size];
        }
    };

    public String getFrontSide() {
        return frontSide;
    }
    public void setFrontSide(String frontSide) {
        this.frontSide = frontSide;
    }
    public String getBackSide() {
        return backSide;
    }
    public void setBackSide(String backSide) {
        this.backSide = backSide;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(frontSide);
        dest.writeString(backSide);
    }
}
