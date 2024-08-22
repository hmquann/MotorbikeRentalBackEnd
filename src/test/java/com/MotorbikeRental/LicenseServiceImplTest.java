package com.MotorbikeRental;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.MotorbikeRental.dto.LicenseDto;
import com.MotorbikeRental.entity.License;
import com.MotorbikeRental.entity.LicenseStatus;
import com.MotorbikeRental.exception.LicenseNotFound;
import com.MotorbikeRental.repository.LicenseRepository;
import com.MotorbikeRental.service.impl.LicenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LicenseServiceImplTest {

    @Mock
    private LicenseRepository licenseRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private LicenseServiceImpl licenseService;

    private License license;
    private LicenseDto licenseDto;
    private List<License> licenseList;
    private Page<License> licensePage;
    private final Long userId = 1L;
    private final String licenseNumber = "011111123456";
    private final LicenseStatus status = LicenseStatus.PENDING;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        license = new License();
        license.setId(1L);
        license.setLicenseNumber(licenseNumber);
        license.setStatus(LicenseStatus.PENDING);

        licenseDto = new LicenseDto();
        licenseDto.setLicenseNumber(licenseNumber);
        licenseDto.setStatus(LicenseStatus.PENDING);

        licenseList = Arrays.asList(license);
        licensePage = new PageImpl<>(licenseList);
    }

    @Test
    public void testApproveLicense() {
        doNothing().when(licenseRepository).changeLicense(licenseNumber, LicenseStatus.APPROVED);

        licenseService.approveLicense(licenseNumber);

        verify(licenseRepository, times(1)).changeLicense(licenseNumber, LicenseStatus.APPROVED);
    }

    @Test
    public void testRejectLicense() {
        doNothing().when(licenseRepository).changeLicense(licenseNumber, LicenseStatus.REJECTED);

        licenseService.rejectLicense(licenseNumber);

        verify(licenseRepository, times(1)).changeLicense(licenseNumber, LicenseStatus.REJECTED);
    }

    @Test
    public void testGetLicenseByUserId_Success() {
        when(licenseRepository.getLicenseByuserId(userId)).thenReturn(license);
        when(mapper.map(license, LicenseDto.class)).thenReturn(licenseDto);

        LicenseDto result = licenseService.getLicenseByUserId(userId);

        assertNotNull(result);
        assertEquals(licenseDto.getLicenseNumber(), result.getLicenseNumber());
        verify(licenseRepository, times(1)).getLicenseByuserId(userId);
        verify(mapper, times(1)).map(license, LicenseDto.class);
    }

    @Test
    public void testGetLicenseByUserId_NotFound() {
        when(licenseRepository.getLicenseByuserId(userId)).thenReturn(null);

        LicenseNotFound exception = assertThrows(LicenseNotFound.class, () -> {
            licenseService.getLicenseByUserId(userId);
        });

        assertEquals("You don't have license. Please upload and verify!!!", exception.getMessage());
        verify(licenseRepository, times(1)).getLicenseByuserId(userId);
        verify(mapper, never()).map(any(License.class), eq(LicenseDto.class));
    }

    @Test
    public void testUpdateLicense() {
        when(licenseRepository.getLicenseByuserId(anyLong())).thenReturn(license);
        when(mapper.map(license, LicenseDto.class)).thenAnswer(invocation -> {
            LicenseDto dto = invocation.getArgument(0);
            License l = invocation.getArgument(1);
            l.setLicenseNumber(dto.getLicenseNumber());
            return l;
        });
        when(licenseRepository.save(any(License.class))).thenReturn(license);

        licenseService.updateLicense(1L, licenseDto);

        verify(licenseRepository, times(1)).getLicenseByuserId(1L);
        verify(mapper, times(1)).map(licenseDto, license);
        verify(licenseRepository, times(1)).save(license);
        assertEquals(LicenseStatus.PENDING, license.getStatus());
    }

    @Test
    public void testGetPendingLicenseWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);
        when(licenseRepository.getPendingLicenseList(status)).thenReturn(licenseList);
        when(mapper.map(license, LicenseDto.class)).thenReturn(licenseDto);

        Page<LicenseDto> result = licenseService.getPendingLicenseWithPagination(0, 10, status);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(licenseDto, result.getContent().get(0));
        verify(licenseRepository, times(1)).getPendingLicenseList(status);
        verify(mapper, times(1)).map(license, LicenseDto.class);
    }
}
