package com.MotorbikeRental.service;

import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.entity.Model;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ModelService {
    public List<Model> getAllModels();
    Model createModel(Model model);
    Page<Model> getBrandWithPagination(int page, int pageSize);
    Model getModelById(Long id);

}
