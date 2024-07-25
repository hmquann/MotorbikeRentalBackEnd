package com.MotorbikeRental.dto;

public class BookingCountDto {
    private int month;
    private int year;
    private long bookingCount;

    public BookingCountDto(int month, int year, long bookingCount) {
        this.month = month;
        this.year = year;
        this.bookingCount = bookingCount;
    }
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(long bookingCount) {
        this.bookingCount = bookingCount;
    }
    // Getters and setters
}
