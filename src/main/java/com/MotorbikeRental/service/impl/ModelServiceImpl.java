package com.MotorbikeRental.service.impl;


import com.MotorbikeRental.dto.BrandDto;
import com.MotorbikeRental.dto.ModelDto;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    @Autowired
    private final ModelRepository modelRepository;

    @Autowired
    private final BrandService brandService;

    @Override
    public List<ModelDto> getAllModels() {
        List<Model> models = modelRepository.findAll();
        return models.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ModelDto createModel(ModelDto modelDto) {
        // Kiểm tra xem tên model đã tồn tại chưa
        if (modelRepository.existsByModelName(modelDto.getModelName())) {
            throw new ValidationException("Model name already exists: " + modelDto.getModelName());
        }

        // Nếu có thông tin về brand và brandId, kiểm tra và thêm model vào brand
        if (modelDto.getBrand().getBrandId() != null) {
            BrandDto brandDto = brandService.getBrand(modelDto.getBrand().getBrandId());
            if (brandDto != null) {
                // Tạo một đối tượng Model từ ModelDto
                Model model = new Model();
                model.setModelName(modelDto.getModelName());
                model.setCylinderCapacity(modelDto.getCylinderCapacity());
                model.setFuelType(modelDto.getFuelType());
                model.setFuelConsumption(modelDto.getFuelConsumption());
                model.setModelType(modelDto.getModelType());

                Brand brand = new Brand();
                brand.setBrandId(brandDto.getBrandId());
                brand.setBrandName(brandDto.getBrandName());
                brand.setOrigin(brandDto.getBrandOrigin());

                model.setBrand(brand);

                // Lưu model vào cơ sở dữ liệu
                modelRepository.save(model);

                // Chuyển đổi từ Model sang ModelDto và trả về
                return convertToDto(model);
            } else {
                throw new RuntimeException("Brand not found with id: " + modelDto.getBrand().getBrandId());
            }
        } else {
            throw new ValidationException("BrandId must not be null");
        }

    }

    @Override
    public Page<ModelDto> getBrandWithPagination(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Model> modelPage = modelRepository.findAll(pageable);

        List<ModelDto> modelDtoList = modelPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(modelDtoList, pageable, modelPage.getTotalElements());
    }

    @Override
    public ModelDto getModelById(Long id) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Model not found"));

        return convertToDto(model);
    }

    @Override
    public List<Model> getListModel() {
        return modelRepository.findAll();
    }


    private ModelDto convertToDto(Model model) {
        ModelDto modelDto = new ModelDto();
        modelDto.setModelId(model.getId());
        modelDto.setModelName(model.getModelName());
        modelDto.setCylinderCapacity(model.getCylinderCapacity());
        modelDto.setFuelType(model.getFuelType());
        modelDto.setModelType(model.getModelType());
        modelDto.setFuelConsumption(model.getFuelConsumption());
        Brand brand = model.getBrand();
        if (brand != null) {
            BrandDto brandDto = new BrandDto();
            brandDto.setBrandId(brand.getBrandId());
            brandDto.setBrandName(brand.getBrandName());
            brandDto.setBrandOrigin(brand.getOrigin());
            modelDto.setBrand(brandDto);
        }
        return modelDto;

    }


}
