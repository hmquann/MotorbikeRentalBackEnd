package com.MotorbikeRental.dto;

import lombok.Data;

@Data
public class NotificationChangeRequest {
    private Boolean systemNoti;
    private Boolean emailNoti;
    private Boolean minimizeNoti;
}
