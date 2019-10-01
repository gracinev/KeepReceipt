package com.example.nevadagraci.keepreceipt.model;

/* The Receipt class is responsible for dealing with individual receipt objects */
// Author: Nevada Graci
public class Receipt implements Comparable<Receipt>{

    // define variables
    private String title;
    private String date;
    private double total;
    private String paymentType;
    private String category;
    private String picture;
    private long dbId;

    // constructor
    public Receipt(String title, String date, double total, String paymentType, String category, String picture) {
        this.title = title;
        this.date = date;
        this.total = total;
        this.paymentType = paymentType;
        this.category = category;
        this.picture = picture;
    }

    // setters and getters of class values
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String isPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    // overriden compareTo method sorts the receipt values according to total
    @Override
    public int compareTo(Receipt receipt) {
        double compareTotal = ((Receipt) receipt).getTotal();
        return (int) (compareTotal - this.total);
    }
}
