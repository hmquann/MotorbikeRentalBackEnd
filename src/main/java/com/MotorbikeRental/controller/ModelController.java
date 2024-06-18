package com.MotorbikeRental.controller;


import com.MotorbikeRental.dto.ModelDto;
import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.entity.FuelType;
import com.MotorbikeRental.entity.Model;
import com.MotorbikeRental.entity.ModelType;
import com.MotorbikeRental.exception.ValidationException;
import com.MotorbikeRental.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/model")
@RequiredArgsConstructor
public class ModelController {

    @Autowired
    private final ModelService modelService;

    @GetMapping("/getAllModel")
    public ResponseEntity<List<ModelDto>> listModels() {
        List<ModelDto> modelDtos = modelService.getAllModels();
        return ResponseEntity.ok(modelDtos);
    }

    @GetMapping("/getAllModel/{page}/{pageSize}")
    public ResponseEntity<Page<ModelDto>> listBrandWithPagination(@PathVariable int page, @PathVariable int pageSize) {
        Page<ModelDto> modelPage = modelService.getBrandWithPagination(page, pageSize);
        return ResponseEntity.ok(modelPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelDto> getModelById(@PathVariable Long id) {
        ModelDto modelDto = modelService.getModelById(id);
        return ResponseEntity.ok(modelDto);
    }

    @GetMapping("/fuelTypes")
    public FuelType[] getFuelTypes() {
        return FuelType.values();
    }


    @GetMapping("/modelTypes")
    public ModelType[] getModelTypes() {
        return ModelType.values();
    }

    @PostMapping("/addModel")
    public ResponseEntity<?> createModel(@RequestBody ModelDto modelDto) {
        try {
            ModelDto createdModelDto = modelService.createModel(modelDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdModelDto);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
