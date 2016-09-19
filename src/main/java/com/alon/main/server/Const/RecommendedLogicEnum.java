package com.alon.main.server.Const;

/**
 * Created by alon_ss on 9/11/16.
 */
public enum RecommendedLogicEnum {

    RELATED("recommenderServiceByRelatedImpl"),
    COLLABORATIVE_FILTERING("recommenderServiceBySparkImpl"),
    CONTENT_FILTERING("content filtering");

    private final String text;

    RecommendedLogicEnum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
