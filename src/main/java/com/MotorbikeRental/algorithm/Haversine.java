package com.MotorbikeRental.algorithm;
import org.springframework.stereotype.Component;

@Component
public class Haversine {
    private static final double R = 6371; // Bán kính trung bình của Trái Đất theo km

    public  double toRadians(double degrees) {
        return degrees * (Math.PI / 180);
    }

    public  Double CalculateTheDistanceAsTheCrowFlies(Double lat1, Double lon1, Double lat2, Double lon2) {
        Double dLat = toRadians(lat2 - lat1);
        Double dLon = toRadians(lon2 - lon1);
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Khoảng cách theo km
    }
}
