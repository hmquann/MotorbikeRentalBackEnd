package com.MotorbikeRental;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.MotorbikeRental.dto.BrandDto;
import com.MotorbikeRental.entity.Brand;
import com.MotorbikeRental.exception.ValidationException;
import com.MotorbikeRental.repository.BrandRepository;
import com.MotorbikeRental.service.impl.BrandServiceImpl;
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


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImpl brandService;

    private Brand brand;
    private BrandDto brandDto;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setBrandId(1L);
        brand.setBrandName("Yamaha");
        brand.setOrigin("Japan");

        brandDto = new BrandDto();
        brandDto.setBrandId(1L);
        brandDto.setBrandName("Yamaha");
        brandDto.setBrandOrigin("Japan");
    }

    @Test
    void testGetAllBrand() {
        when(brandRepository.findAll()).thenReturn(Arrays.asList(brand));

        List<Brand> result = brandService.getAllBrand();

        assertEquals(1, result.size());
        assertEquals("Yamaha", result.get(0).getBrandName());
    }

    @Test
    void testGetBrandWithPagination() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<Brand> brandPage = new PageImpl<>(Arrays.asList(brand), pageRequest, 1);

        when(brandRepository.findAll(pageRequest)).thenReturn(brandPage);

        Page<Brand> result = brandService.getBrandWithPagination(0, 1);

        assertEquals(1, result.getTotalElements());
        assertEquals("Yamaha", result.getContent().get(0).getBrandName());
    }

    @Test
    void testCreateNewBrand() {
        when(brandRepository.existsByBrandName(brand.getBrandName())).thenReturn(false);
        when(brandRepository.save(brand)).thenReturn(brand);

        Brand result = brandService.createNewBrand(brand);

        assertNotNull(result);
        assertEquals("Yamaha", result.getBrandName());
    }

    @Test
    void testCreateNewBrandAlreadyExists() {
        when(brandRepository.existsByBrandName(brand.getBrandName())).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            brandService.createNewBrand(brand);
        });

        assertEquals("Brand name already exists", exception.getMessage());
    }

    @Test
    void testDeleteBrand() {
        when(brandRepository.existsByBrandId(1L)).thenReturn(true);
        doNothing().when(brandRepository).deleteById(1L);

        brandService.deleteBrand(1L);

        verify(brandRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBrandNotFound() {
        when(brandRepository.existsByBrandId(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            brandService.deleteBrand(1L);
        });

        assertEquals("Brand with ID 1 not found", exception.getMessage());
    }

    @Test
    void testUpdateBrand() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(brandRepository.existsByBrandName("Honda")).thenReturn(false);

        Brand updatedBrand = new Brand();
        updatedBrand.setBrandName("Honda");
        updatedBrand.setOrigin("Japan");

        when(brandRepository.save(brand)).thenReturn(brand);

        Brand result = brandService.updateBrand(1L, updatedBrand);

        assertNotNull(result);
        assertEquals("Honda", result.getBrandName());
    }

    @Test
    void testUpdateBrandNotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            brandService.updateBrand(1L, brand);
        });

        assertEquals("Brand with ID 1 not found", exception.getMessage());
    }

    @Test
    void testGetBrand() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        BrandDto result = brandService.getBrand(1L);

        assertNotNull(result);
        assertEquals("Yamaha", result.getBrandName());
    }

    @Test
    void testGetBrandNotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            brandService.getBrand(1L);
        });

        assertEquals("Brand not found with id: 1", exception.getMessage());
    }
}
