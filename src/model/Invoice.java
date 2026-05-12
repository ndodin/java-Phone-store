package model;

import java.util.Date;

public class Invoice {

    private int id;
    private int customerId;
    private int userId;
    private Date date;
    private double total;
    private String status;

    public Invoice() {
    }

    public Invoice(int id, int customerId, int userId,
                   Date date, double total, String status) {

        this.id = id;
        this.customerId = customerId;
        this.userId = userId;
        this.date = date;
        this.total = total;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}