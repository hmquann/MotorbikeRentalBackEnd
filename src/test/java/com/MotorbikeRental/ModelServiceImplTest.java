package com.MotorbikeRental;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.MotorbikeRental.dto.BrandDto;
import com.MotorbikeRental.dto.ModelDto;
import com.MotorbikeRental.entity.FuelType;
import com.MotorbikeRental.entity.Model;
import com.MotorbikeRental.entity.ModelType;
import com.MotorbikeRental.repository.ModelRepository;
import com.MotorbikeRental.service.BrandService;
import com.MotorbikeRental.service.impl.ModelServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.validation.ValidationException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ModelServiceImplTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private BrandService brandService;

    @InjectMocks
    private ModelServiceImpl modelService;

    private Model model;
    private ModelDto modelDto;
    private BrandDto brandDto;

    @BeforeEach
    public void setUp() {
        model = new Model();
        model.setId(1L);
        model.setModelName("TestModel");
        model.setCylinderCapacity(150);
        model.setFuelType(FuelType.GASOLINE);
        model.setModelType(ModelType.XeTayGa);
        model.setFuelConsumption(3.5F);


        brandDto = new BrandDto();
        brandDto.setBrandId(1L);
        brandDto.setBrandName("TestBrand");
        brandDto.setBrandOrigin("TestOrigin");

        modelDto = new ModelDto();
        modelDto.setModelId(1L);
        modelDto.setModelName("TestModel");
        modelDto.setCylinderCapacity(150);
        modelDto.setFuelType(FuelType.GASOLINE);
        modelDto.setModelType(ModelType.XeTayGa);
        modelDto.setFuelConsumption(3.5F);
        modelDto.setBrand(brandDto);
    }

    @Test
    public void testGetAllModels() {
        when(modelRepository.findAll()).thenReturn(Arrays.asList(model));

        List<ModelDto> models = modelService.getAllModels();

        assertEquals(1, models.size());
        assertEquals("TestModel", models.get(0).getModelName());
        verify(modelRepository, times(1)).findAll();
    }

    @Test
    public void testCreateModel_Success() {
        when(modelRepository.existsByModelName(anyString())).thenReturn(false);
        when(brandService.getBrand(anyLong())).thenReturn(brandDto);
        when(modelRepository.save(any(Model.class))).thenReturn(model);

        ModelDto result = modelService.createModel(modelDto);

        assertNotNull(result);
        assertEquals("TestModel", result.getModelName());
        verify(modelRepository, times(1)).existsByModelName(anyString());
        verify(brandService, times(1)).getBrand(anyLong());
        verify(modelRepository, times(1)).save(any(Model.class));
    }

    @Test
    public void testCreateModel_AlreadyExists() {
        when(modelRepository.existsByModelName(anyString())).thenReturn(true);

        com.MotorbikeRental.exception.ValidationException exception = assertThrows(com.MotorbikeRental.exception.ValidationException.class, () -> {
            modelService.createModel(modelDto);
        });

        assertEquals("Model name already exists: TestModel", exception.getMessage());
        verify(modelRepository, times(1)).existsByModelName(anyString());
        verify(brandService, never()).getBrand(anyLong());
        verify(modelRepository, never()).save(any(Model.class));
    }

    @Test
    public void testCreateModel_BrandNotFound() {
        when(modelRepository.existsByModelName(anyString())).thenReturn(false);
        when(brandService.getBrand(anyLong())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            modelService.createModel(modelDto);
        });

        assertEquals("Brand not found with id: 1", exception.getMessage());
        verify(modelRepository, times(1)).existsByModelName(anyString());
        verify(brandService, times(1)).getBrand(anyLong());
        verify(modelRepository, never()).save(any(Model.class));
    }

    @Test
    public void testGetModelById() {
        when(modelRepository.findById(anyLong())).thenReturn(Optional.of(model));

        ModelDto result = modelService.getModelById(1L);

        assertNotNull(result);
        assertEquals("TestModel", result.getModelName());
        verify(modelRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetModelById_NotFound() {
        when(modelRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            modelService.getModelById(1L);
        });

        assertEquals("Model not found", exception.getMessage());
        verify(modelRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testUpdateModel_Success() {
        modelDto.setModelName("UpdatedModel");
        when(modelRepository.findById(anyLong())).thenReturn(Optional.of(model));
        when(modelRepository.existsByModelName(anyString())).thenReturn(false);
        when(brandService.getBrand(anyLong())).thenReturn(brandDto);
        when(modelRepository.save(any(Model.class))).thenReturn(model);

        ModelDto result = modelService.updateModel(1L, modelDto);

        assertNotNull(result);
        assertEquals("UpdatedModel", result.getModelName());
        assertEquals("TestBrand", result.getBrand().getBrandName());

        verify(modelRepository, times(1)).findById(anyLong());
        verify(modelRepository, times(1)).existsByModelName(anyString());
        verify(brandService, times(1)).getBrand(anyLong());
        verify(modelRepository, times(1)).save(any(Model.class));
    }

    @Test
    public void testUpdateModel_NotFound() {
        when(modelRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            modelService.updateModel(1L, modelDto);
        });

        assertEquals("Model not found with id: 1", exception.getMessage());
        verify(modelRepository, times(1)).findById(anyLong());
        verify(modelRepository, never()).existsByModelName(anyString());
        verify(brandService, never()).getBrand(anyLong());
        verify(modelRepository, never()).save(any(Model.class));
    }

    @Test
    public void testSearchModel() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Model> modelPage = new PageImpl<>(Arrays.asList(model));

        when(modelRepository.searchByModelNameOrBrandName(anyString(), eq(pageable))).thenReturn(modelPage);

        Page<ModelDto> result = modelService.searchModel("Test", 0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("TestModel", result.getContent().get(0).getModelName());
        verify(modelRepository, times(1)).searchByModelNameOrBrandName(anyString(), eq(pageable));
    }

    @Test
    public void testGetBrandWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Model> modelPage = new PageImpl<>(Arrays.asList(model));

        when(modelRepository.findAll(pageable)).thenReturn(modelPage);

        Page<ModelDto> result = modelService.getBrandWithPagination(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("TestModel", result.getContent().get(0).getModelName());
        verify(modelRepository, times(1)).findAll(pageable);
    }
}
