package com.MotorbikeRental.service;


import com.MotorbikeRental.dto.ModelDto;
import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.entity.Model;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ModelService {

    public List<ModelDto> getAllModels();
    ModelDto createModel(ModelDto modelDto);
    Page<ModelDto> getBrandWithPagination(int page, int pageSize);
    ModelDto getModelById(Long id);
    Page<ModelDto> searchModel(String searchTerm, int page, int size);

}

