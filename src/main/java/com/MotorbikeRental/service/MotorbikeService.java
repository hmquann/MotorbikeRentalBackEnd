package com.MotorbikeRental.service;




import com.MotorbikeRental.entity.MotorbikeStatus;
import com.MotorbikeRental.entity.Model;
import com.MotorbikeRental.entity.Motorbike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Date;
import java.util.List;

public interface MotorbikeService {
    public List<Motorbike>getAllMotorbike();

    Page<Motorbike> getMotorbikeWithPagination(int page, int pageSize);

    Page<Motorbike> searchByPlate(String searchTerm, int page, int size);

    void toggleMotorbikeStatus(Long id);

    public List<Motorbike>getMotorbikeByLessorId();

    public List<Motorbike>getAllMotorbikeByStatus(MotorbikeStatus status);

    public Motorbike registerMotorbike(Motorbike motorbike);

    public void approveMotorbike(int motorbikeId);

    public boolean checkExistPlate(String motorbikePlate);

    public void getMotorbikeByAddress(String address);

    public void sortMotorbikeBycriteria(String criteria);

    public List<Motorbike> getMotorbikeByBrand(String brandName);

    public List<Motorbike> getMotorbikeByModel(String modelName);

    public List<Motorbike> getMotorbikeByRequireTime(Date startDate,Date endDate);

    public List<Motorbike> getDeliveryMotorbike(boolean delivery);

    List<Motorbike> getPendingMotorbikes();
    Motorbike approveMotorbike(Long id);
    Motorbike rejectMotorbike(Long id);

}
