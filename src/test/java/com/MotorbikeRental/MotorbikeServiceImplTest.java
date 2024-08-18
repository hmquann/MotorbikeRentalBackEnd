package com.MotorbikeRental;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.MotorbikeRental.algorithm.Haversine;
import com.MotorbikeRental.dto.FilterMotorbikeDto;
import com.MotorbikeRental.dto.MotorbikeDto;
import com.MotorbikeRental.dto.RegisterMotorbikeDto;
import com.MotorbikeRental.exception.ExistPlateException;
import com.MotorbikeRental.entity.*;
import com.MotorbikeRental.exception.ValidationException;
import com.MotorbikeRental.repository.*;
import com.MotorbikeRental.service.JWTService;
import com.MotorbikeRental.service.UserService;
import com.MotorbikeRental.service.impl.MotorbikeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class MotorbikeServiceImplTest {

    @Mock
    private MotorbikeRepository motorbikeRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private UserService userService;
    @Mock
    private MotorbikeFilterRepository motorbikeFilterRepository;

    @Mock
    private Haversine haversine;
    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private MotorbikeServiceImpl motorbikeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void registerMotorbike_ShouldThrowExistPlateException_WhenPlateExists() {
        RegisterMotorbikeDto dto = new RegisterMotorbikeDto();
        dto.setMotorbikePlate("ABC123");

        when(motorbikeRepository.existsByMotorbikePlate("ABC123")).thenReturn(true);

        assertThrows(ExistPlateException.class, () -> {
            motorbikeService.registerMotorbike("Bearer someToken", dto);
        });

        verify(motorbikeRepository, times(1)).existsByMotorbikePlate("ABC123");
    }

    @Test
    void registerMotorbike_ShouldRegisterMotorbike_WhenPlateDoesNotExist() {
        RegisterMotorbikeDto dto = new RegisterMotorbikeDto();

        dto.setMotorbikePlate("29-A1-68324");
        dto.setMotorbikeAddress("132 Cầu Giấy, Đường Cầu Giấy, Quan Hoa, Cầu Giấy, Hà Nội");
        dto.setLatitude(10.12345);
        dto.setLongitude(106.67890);
        dto.setModelId(1L);
        dto.setPrice(100000L);
        dto.setDelivery(true);
        dto.setDeliveryFee(5000L);
        dto.setFreeShipLimit(5L);
        dto.setYearOfManufacture(2020L);
        dto.setConstraintMotorbike("Cần có CCCD/CMND hoặc gplx còn thời hạn");
        // Set other properties as needed

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        Motorbike model = new Motorbike();
        model.setId(1L);

        when(motorbikeRepository.existsByMotorbikePlate("29-A1-68324")).thenReturn(false);
        when(jwtService.extractUsername("someToken")).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(motorbikeRepository.save(any(Motorbike.class))).thenReturn(model);
        Motorbike result = motorbikeService.registerMotorbike("Bearer someToken", dto);

        assertEquals(model, result);
        verify(motorbikeRepository, times(1)).existsByMotorbikePlate("29-A1-68324");
        verify(motorbikeRepository, times(1)).save(any(Motorbike.class));
    }

    @Test
    void listMotorbikeByFilter_ShouldReturnFilteredMotorbikes() {
        FilterMotorbikeDto filterDto = new FilterMotorbikeDto();
        filterDto.setIsFiveStar(true);
        filterDto.setLatitude(10.0);
        filterDto.setLongitude(106.0);

        Motorbike motorbike1 = new Motorbike();
        Motorbike motorbike2 = new Motorbike();

        List<Motorbike> motorbikeList = Arrays.asList(motorbike1, motorbike2);
        List<Long> fiveStarUserIdList = Arrays.asList(1L, 2L);

        when(motorbikeFilterRepository.listMotorbikeByFilter(
                any(), any(), any(), any(), any(), any(), any())
        ).thenReturn(motorbikeList);

        when(motorbikeFilterRepository.getFiveStarLessor()).thenReturn(fiveStarUserIdList);
        when(haversine.CalculateTheDistanceAsTheCrowFlies(
                anyDouble(), anyDouble(), anyDouble(), anyDouble())
        ).thenReturn(20.0); // Mocking distance less than 30km

        Page<MotorbikeDto> result = motorbikeService.listMotorbikeByFilter(filterDto, 0, 10);

        assertEquals(2, result.getTotalElements());
        verify(motorbikeFilterRepository, times(1)).listMotorbikeByFilter(
                any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void listMotorbikeByFilter_ShouldFilterOutMotorbikesBeyondDistance() {
        FilterMotorbikeDto filterDto = new FilterMotorbikeDto();
        filterDto.setLatitude(10.0);
        filterDto.setLongitude(106.0);

        Motorbike motorbike1 = new Motorbike();
        Motorbike motorbike2 = new Motorbike();

        List<Motorbike> motorbikeList = Arrays.asList(motorbike1, motorbike2);

        when(motorbikeFilterRepository.listMotorbikeByFilter(
                any(), any(), any(), any(), any(), any(), any())
        ).thenReturn(motorbikeList);

        when(haversine.CalculateTheDistanceAsTheCrowFlies(
                anyDouble(), anyDouble(), anyDouble(), anyDouble())
        ).thenReturn(40.0); // Mocking distance more than 30km

        Page<MotorbikeDto> result = motorbikeService.listMotorbikeByFilter(filterDto, 0, 10);

        assertEquals(0, result.getTotalElements());
        verify(motorbikeFilterRepository, times(1)).listMotorbikeByFilter(
                any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void listMotorbikeByFilter_ShouldReturnEmptyPage_WhenNoMotorbikesMatch() {
        FilterMotorbikeDto filterDto = new FilterMotorbikeDto();
        Pageable pageable = PageRequest.of(0, 10);

        when(motorbikeFilterRepository.listMotorbikeByFilter(
                any(), any(), any(), any(), any(), any(), any())
        ).thenReturn(Collections.emptyList());

        Page<MotorbikeDto> result = motorbikeService.listMotorbikeByFilter(filterDto, 0, 10);

        assertEquals(0, result.getTotalElements());
        verify(motorbikeFilterRepository, times(1)).listMotorbikeByFilter(
                any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void toggleMotorbikeStatus_ShouldThrowValidationException_WhenStatusIsActiveAndBookingInProgress() {
        Motorbike motorbike = new Motorbike();
        motorbike.setStatus(MotorbikeStatus.ACTIVE);

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.DEPOSIT_MADE);

        when(motorbikeRepository.findById(1L)).thenReturn(Optional.of(motorbike));
        when(bookingRepository.findByMotorbikeId(1L)).thenReturn(Arrays.asList(booking));

        assertThrows(ValidationException.class, () -> {
            motorbikeService.toggleMotorbikeStatus(1L);
        });

        verify(motorbikeRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findByMotorbikeId(1L);
    }

    @Test
    void toggleMotorbikeStatus_ShouldToggleToDeactive_WhenStatusIsActiveAndNoBookingInProgress() {
        Motorbike motorbike = new Motorbike();
        motorbike.setStatus(MotorbikeStatus.ACTIVE);

        when(motorbikeRepository.findById(1L)).thenReturn(Optional.of(motorbike));
        when(bookingRepository.findByMotorbikeId(1L)).thenReturn(Arrays.asList());

        motorbikeService.toggleMotorbikeStatus(1L);

        assertEquals(MotorbikeStatus.DEACTIVE, motorbike.getStatus());
        verify(motorbikeRepository, times(1)).save(motorbike);
    }

    @Test
    void toggleMotorbikeStatus_ShouldToggleToActive_WhenStatusIsDeactive() {
        Motorbike motorbike = new Motorbike();
        motorbike.setStatus(MotorbikeStatus.DEACTIVE);

        when(motorbikeRepository.findById(1L)).thenReturn(Optional.of(motorbike));

        motorbikeService.toggleMotorbikeStatus(1L);

        assertEquals(MotorbikeStatus.ACTIVE, motorbike.getStatus());
        verify(motorbikeRepository, times(1)).save(motorbike);
    }

    @Test
    void getAllMotorbikeByStatus_ShouldReturnMotorbikeDtos() {
        Motorbike motorbike1 = new Motorbike();
        Motorbike motorbike2 = new Motorbike();

        List<Motorbike> motorbikeList = Arrays.asList(motorbike1, motorbike2);

        MotorbikeDto motorbikeDto1 = new MotorbikeDto();
        MotorbikeDto motorbikeDto2 = new MotorbikeDto();

        when(motorbikeRepository.getAllMotorbikeByStatus(MotorbikeStatus.ACTIVE)).thenReturn(motorbikeList);
        when(mapper.map(motorbike1, MotorbikeDto.class)).thenReturn(motorbikeDto1);
        when(mapper.map(motorbike2, MotorbikeDto.class)).thenReturn(motorbikeDto2);

        List<MotorbikeDto> result = motorbikeService.getAllMotorbikeByStatus(MotorbikeStatus.ACTIVE);

        assertEquals(2, result.size());
        verify(motorbikeRepository, times(1)).getAllMotorbikeByStatus(MotorbikeStatus.ACTIVE);
    }
}


