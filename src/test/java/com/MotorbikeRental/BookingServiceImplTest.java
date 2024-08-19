package com.MotorbikeRental;

import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.*;
import com.MotorbikeRental.repository.*;
import com.MotorbikeRental.service.BookingService;
import com.MotorbikeRental.service.impl.BookingServiceImpl;
import com.MotorbikeRental.service.MotorbikeService;
import com.MotorbikeRental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookingServiceImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private MotorbikeService motorbikeService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private BookingFilterRepository bookingFilterRepository;

    @Test
    public void testCreateBooking() throws Exception {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setStartDate(LocalDateTime.now());
        bookingRequest.setEndDate(LocalDateTime.now().plusDays(2));
        bookingRequest.setRenterId(1L);
        bookingRequest.setMotorbikeId(1L);
        bookingRequest.setBookingTime(LocalDateTime.now());
        bookingRequest.setReceiveLocation("Location A");
        bookingRequest.setTotalPrice(500.0);
        bookingRequest.setLongitude(12.34);
        bookingRequest.setLatitude(56.78);

        User mockUser = new User();
        mockUser.setId(1L);

        MotorbikeDto mockMotorbike = new MotorbikeDto();
        mockMotorbike.setId(1L);

        Booking mockBooking = new Booking();
        mockBooking.setBookingId(1L);

        Mockito.when(userRepository.getUserById(bookingRequest.getRenterId())).thenReturn(mockUser);
        Mockito.when(motorbikeService.getMotorbikeById(bookingRequest.getMotorbikeId())).thenReturn(mockMotorbike);

        Mockito.when(bookingService.saveBooking(Mockito.any(BookingRequest.class))).thenReturn("booking done");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"renterId\": 1, \"motorbikeId\": 1, \"startDate\": \"2024-08-19T00:00:00\", \"endDate\": \"2024-08-21T00:00:00\", \"bookingTime\": \"2024-08-18T00:00:00\", \"receiveLocation\": \"Location A\", \"totalPrice\": 500.0, \"longitude\": 12.34, \"latitude\": 56.78}"))
                .andExpect(status().isOk())
                .andExpect(content().string("booking done"));
    }


    @Test
    public void testChangeStatusBooking() throws Exception {
        Long bookingId = 1L;

        // Test case 1: Change status to PENDING_DEPOSIT
        String statusPendingDeposit = "PENDING_DEPOSIT";
        when(bookingService.changeStatusBooking(bookingId, statusPendingDeposit))
                .thenReturn("Change to PENDING_DEPOSIT done");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/booking/changeStatus/{id}/{status}", bookingId, statusPendingDeposit)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Change to PENDING_DEPOSIT done"));

        verify(bookingService, times(1)).changeStatusBooking(bookingId, statusPendingDeposit);

        // Test case 2: Change status to DEPOSIT_MADE
        String statusDepositMade = "DEPOSIT_MADE";
        when(bookingService.changeStatusBooking(bookingId, statusDepositMade))
                .thenReturn("Change to DEPOSIT_MADE done");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/booking/changeStatus/{id}/{status}", bookingId, statusDepositMade)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Change to DEPOSIT_MADE done"));

        verify(bookingService, times(1)).changeStatusBooking(bookingId, statusDepositMade);

        // Test case 3: Change status to RENTING
        String statusRenting = "RENTING";
        when(bookingService.changeStatusBooking(bookingId, statusRenting))
                .thenReturn("Change to RENTING done");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/booking/changeStatus/{id}/{status}", bookingId, statusRenting)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Change to RENTING done"));

        verify(bookingService, times(1)).changeStatusBooking(bookingId, statusRenting);

        // Test case 4: Change status to REJECTED
        String statusRejected = "REJECTED";
        when(bookingService.changeStatusBooking(bookingId, statusRejected))
                .thenReturn("Change to REJECTED done");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/booking/changeStatus/{id}/{status}", bookingId, statusRejected)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Change to REJECTED done"));

        verify(bookingService, times(1)).changeStatusBooking(bookingId, statusRejected);

        // Test case 5: Change status to CANCELED
        String statusCanceled = "CANCELED";
        when(bookingService.changeStatusBooking(bookingId, statusCanceled))
                .thenReturn("Change to CANCELED done");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/booking/changeStatus/{id}/{status}", bookingId, statusCanceled)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Change to CANCELED done"));

        verify(bookingService, times(1)).changeStatusBooking(bookingId, statusCanceled);

        // Test case 6: Change status to DONE
        String statusDone = "DONE";
        when(bookingService.changeStatusBooking(bookingId, statusDone))
                .thenReturn("Change to DONE done");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/booking/changeStatus/{id}/{status}", bookingId, statusDone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Change to DONE done"));

        verify(bookingService, times(1)).changeStatusBooking(bookingId, statusDone);
    }

//    @Test
//    public void testFilterBookings() {
//        // Prepare test data
//        String tripType = "renter";
//        Long userId = 1L;
//        String statusString = "PENDING_DEPOSIT";
//        BookingStatus status = BookingStatus.fromString(statusString);
//        LocalDateTime startTime = LocalDateTime.of(2024, 8, 1, 10, 0);
//        LocalDateTime endTime = LocalDateTime.of(2024, 8, 2, 18, 0);
//        String sort = "sortByBookingTimeAsc";
//
//        FilterBookingDto filterBookingDto = new FilterBookingDto();
//        filterBookingDto.setTripType(tripType);
//        filterBookingDto.setUserId(userId);
//        filterBookingDto.setStatus(statusString);
//        filterBookingDto.setStartTime(startTime);
//        filterBookingDto.setEndTime(endTime);
//        filterBookingDto.setSort(sort);
//
//        // Create a list of mock bookings
//        List<Booking> mockBookings = new ArrayList<>();
//        Booking booking1 = new Booking();
//        booking1.setBookingId(1L);
//        booking1.setStatus(BookingStatus.PENDING_DEPOSIT);
//        booking1.setRenter(userRepository.getUserById(userId)); // Assume the userId is associated with the booking.
//        mockBookings.add(booking1);
//
//        // Mock the behavior of the filter method
//        when(bookingFilterRepository.filterBookings(
//                tripType, userId, status, startTime, endTime, sort))
//                .thenReturn(mockBookings);
//
//        // Mock the behavior of the mapper
//        BookingRequest bookingRequest1 = new BookingRequest();
//        bookingRequest1.setBookingId(1L);
//        bookingRequest1.setStatus(BookingStatus.PENDING_DEPOSIT);
//        bookingRequest1.setRenterId(userId);
//
//        when(mapper.map(booking1, BookingRequest.class)).thenReturn(bookingRequest1);
//
//        // Call the service method
//        List<BookingRequest> result = bookingService.filterBookings(filterBookingDto);
//
//        // Verify the result
//        assertNotNull(result);
//        assertEquals(true, result.isEmpty());
//
//        // Verify interactions with mocks
//        verify(bookingFilterRepository, times(1)).filterBookings(
//                tripType, userId, status, startTime, endTime, sort);
//        verify(mapper, times(1)).map(booking1, BookingRequest.class);
//    }

}
