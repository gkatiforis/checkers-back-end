package com.katiforis.top10.DTO.request;


public class GetNotifications extends BaseRequest {
    long from;

    public GetNotifications(){}
    public GetNotifications(String playerId, long from) {
        super(playerId);
        this.from = from;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }
}