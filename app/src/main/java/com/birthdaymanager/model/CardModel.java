package com.birthdaymanager.model;

public class CardModel {
    private String bid;
    private String day;
    private String month;
    private String cardName;
    private String daysLeft;
    private int clipImage;

    public CardModel(String bid, String day, String month, String cardName, String daysLeft, int clipImage) {
        this.bid = bid;
        this.day = day;
        this.month = month;
        this.cardName = cardName;
        this.daysLeft = daysLeft;
        this.clipImage = clipImage;
    }

    public String getBid() { return bid; }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getCardName() {
        return cardName;
    }

    public String getDaysLeft() {
        return daysLeft;
    }

    public int getClipImage() {
        return clipImage;
    }
}
