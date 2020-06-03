package com.example.flashcard;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FlashcardInfo implements Parcelable {
    private String flashcardTitle;
    private String numOfFlashcards;
    private List<Flashcard> flashCards;
    private int priority;
    private String dueDate;

    protected FlashcardInfo(Parcel in) {
        flashcardTitle = in.readString();
        numOfFlashcards = in.readString();
        flashCards = in.createTypedArrayList(Flashcard.CREATOR);
        priority = in.readInt();
        dueDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(flashcardTitle);
        dest.writeString(numOfFlashcards);
        dest.writeTypedList(flashCards);
        dest.writeInt(priority);
        dest.writeString(dueDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlashcardInfo> CREATOR = new Creator<FlashcardInfo>() {
        @Override
        public FlashcardInfo createFromParcel(Parcel in) {
            return new FlashcardInfo(in);
        }

        @Override
        public FlashcardInfo[] newArray(int size) {
            return new FlashcardInfo[size];
        }
    };

    public String getFlashcardTitle() {
        return flashcardTitle;
    }
    public void setFlashcardTitle(String flashcardTitle) {
        this.flashcardTitle = flashcardTitle;
    }
    public String getNumOfFlashcards() {
        return numOfFlashcards;
    }
    public void setNumOfFlashcards(String numOfFlashcards) {
        this.numOfFlashcards = numOfFlashcards;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public List<Flashcard> getFlashcard(){return flashCards;}
    public void setFlashcard(List<Flashcard> flashcard){
        flashCards = flashcard;}
    public String getDueDate() {
        return dueDate;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
