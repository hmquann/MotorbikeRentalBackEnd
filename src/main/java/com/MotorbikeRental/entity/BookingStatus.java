package com.MotorbikeRental.entity;

public enum BookingStatus {
        PENDING, PENDING_DEPOSIT, DEPOSIT_MADE, RENTING, REJECTED, CANCELED, DONE;


        public static BookingStatus fromString(String status) {
                try {
                        return BookingStatus.valueOf(status.toUpperCase());
                } catch (IllegalArgumentException e) {
                        // Handle the case where the provided string does not match any enum constant
                        return null; // or throw an exception, or return a default value
                }
        }

}
