package com.MotorbikeRental.service;




import com.MotorbikeRental.dto.RegisterMotorbikeDto;
import com.MotorbikeRental.entity.MotorbikeStatus;
import com.MotorbikeRental.entity.Motorbike;
import org.springframework.data.domain.Page;


import java.util.Date;
import java.util.List;

public interface MotorbikeService {
    public Page<RegisterMotorbikeDto> getAllMotorbike(int page,int pageSize,Long userId,List<String> roles, String status);

    Page<Motorbike> getMotorbikeWithPagination(int page, int pageSize);

    Page<RegisterMotorbikeDto> searchByPlate(String searchTerm,String status,Long userId,List<String> roles, int page, int size);

    void toggleMotorbikeStatus(Long id);

    public List<Motorbike>getMotorbikeByLessorId();

    public List<RegisterMotorbikeDto>getAllMotorbikeByStatus(MotorbikeStatus status);

    public Motorbike registerMotorbike(RegisterMotorbikeDto registerMotorbikeDto);

    public void approveMotorbike(int motorbikeId);

    public Motorbike checkExistPlate(String motorbikePlate);

    public void getMotorbikeByAddress(String address);

    public void sortMotorbikeBycriteria(String criteria);

    public List<Motorbike> getMotorbikeByBrand(String brandName);

    public List<Motorbike> getMotorbikeByModel(String modelName);

    public List<Motorbike> getMotorbikeByRequireTime(Date startDate,Date endDate);

    public List<Motorbike> getDeliveryMotorbike(boolean delivery);

    Page<RegisterMotorbikeDto> getPendingMotorbikes(MotorbikeStatus status, int page, int pageSize);
    Motorbike approveMotorbike(Long id);
    Motorbike rejectMotorbike(Long id);

}
