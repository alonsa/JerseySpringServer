package com.alon.main.server.Const;

/**
 * Created by alon_ss on 9/11/16.
 */
public enum YouTubeOrderEnum {

    DATE("date"),
    RATING("rating"),
    RELEVANCE("relevance"),
    TITLE("title"),
    VIDEO_COUNT("videocount"),
    VIEW_COUNT("viewcount");


    private final String text;

    YouTubeOrderEnum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }


}
