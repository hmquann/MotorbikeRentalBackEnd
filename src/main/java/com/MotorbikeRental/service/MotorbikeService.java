package com.MotorbikeRental.service;




import com.MotorbikeRental.dto.FilterMotorbikeDto;
import com.MotorbikeRental.dto.MotorbikeDto;
import com.MotorbikeRental.dto.RegisterMotorbikeDto;
import com.MotorbikeRental.dto.UpdateMotorbikeDto;
import com.MotorbikeRental.entity.MotorbikeStatus;
import com.MotorbikeRental.entity.Motorbike;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;


import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MotorbikeService {
    public Page<MotorbikeDto> getAllMotorbike(int page, int pageSize, Long userId, List<String> roles, String status);

    Page<Motorbike> getMotorbikeWithPagination(int page, int pageSize);

    Page<MotorbikeDto> searchByPlate(String searchTerm,String status,Long userId,List<String> roles, int page, int size);

    void toggleMotorbikeStatus(Long id);

    public List<Motorbike>getMotorbikeByLessorId();

    public List<MotorbikeDto>getAllMotorbikeByStatus(MotorbikeStatus status);

    public Motorbike registerMotorbike(String accessToken,RegisterMotorbikeDto registerMotorbikeDto);
    public Page<MotorbikeDto> listMotorbikeByFilter(FilterMotorbikeDto filterMotorbikeDto, int page, int pageSize);

    public Motorbike checkExistPlate(String motorbikePlate);

    Page<MotorbikeDto> getPendingMotorbikes(MotorbikeStatus status, int page, int pageSize);
    Motorbike approveMotorbike(Long id);
    Motorbike rejectMotorbike(Long id);
    List<MotorbikeDto> listFiveStar();

    MotorbikeDto existMotorbikeByUserId(Long motorbikeId, Long userId);
    MotorbikeDto updateMotorbike(Long id, UpdateMotorbikeDto updateMotorbikeDto);
    MotorbikeDto getMotorbikeById(Long id);
    List<MotorbikeDto> getMotorbikeByUserId(Long userId);

}
