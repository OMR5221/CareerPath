package com.appsforprogress.android.careerpath;

import java.util.UUID;

/**
 * Created by ORamirez on 5/29/2017.
 */

public class Question
{
    private UUID mId;
    private String mText;
    private Integer mScore;
    private Boolean mAnswered;

    public Question()
    {
        // Generate unique ID:
        this(UUID.randomUUID());
    }

    public Question(UUID id)
    {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text)
    {
        mText = text;
    }

    public Integer getScore() {
        return mScore;
    }

    public void setScore(Integer score) {
        mScore = score;
    }

    public Boolean getAnswered() {
        return mAnswered;
    }

    public void setAnswered(Boolean answered) {
        mAnswered = answered;
    }
}
