package com.MotorbikeRental.controller;


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
    public ResponseEntity<List<Model>> listModels() {
        return ResponseEntity.ok(modelService.getAllModels());
    }

    @GetMapping("/getAllModel/{page}/{pageSize}")
    public ResponseEntity<Page<Model>> listBrandWithPagination(@PathVariable int page, @PathVariable int pageSize) {
        Page<Model> modelPage = modelService.getBrandWithPagination(page,pageSize);
        return ResponseEntity.ok(modelPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getModelById(@PathVariable Long id) {
        return ResponseEntity.ok(modelService.getModelById(id));
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
    public ResponseEntity<?> createModel(@RequestBody Model model) {
        try {
            Model createdModel = modelService.createModel(model);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdModel);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
