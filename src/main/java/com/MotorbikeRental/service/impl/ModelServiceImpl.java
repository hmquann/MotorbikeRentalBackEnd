package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.entity.Model;
import com.MotorbikeRental.exception.ValidationException;
import com.MotorbikeRental.repository.ModelRepository;
import com.MotorbikeRental.service.BrandService;
import com.MotorbikeRental.service.ModelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    @Autowired
    private final ModelRepository modelRepository;

    @Autowired
    private final BrandService brandService;

    @Override
    public List<Model> getAllModels() {
        return modelRepository.findAll();
    }

    @Override
    public Model createModel(Model model) {
        if (modelRepository.existsByModelName(model.getModelName())) {
            throw new ValidationException("Model name already exists: " + model.getModelName());
        }

        if (model.getBrand() != null && model.getBrand().getBrandId() != null) {
            Brand brand = brandService.getBrand(model.getBrand().getBrandId());
            if (brand != null) {
                brand.addModel(model); // Thêm model vào modelSet của brand
            } else {
                throw new RuntimeException("Brand not found with id: " + model.getBrand().getBrandId());
            }
        }
        return modelRepository.save(model);
    }

    @Override
    public Page<Model> getBrandWithPagination(int page, int pageSize) {
        return modelRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Model getModelById(Long id) {
        Optional<Model> modelOptional = modelRepository.findById(id);
        return modelOptional.orElseThrow(() -> new EntityNotFoundException("Model with ID " + id + " not found"));
    }


}
