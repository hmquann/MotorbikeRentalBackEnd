package com.MotorbikeRental.entity;

public enum BookingStatus {
        PENDING, ACCEPTED, DENIED, DONE, CANCELED;

        public static BookingStatus fromString(String status) {
                try {
                        return BookingStatus.valueOf(status.toUpperCase());
                } catch (IllegalArgumentException e) {
                        // Handle the case where the provided string does not match any enum constant
                        return null; // or throw an exception, or return a default value
                }
        }
}
