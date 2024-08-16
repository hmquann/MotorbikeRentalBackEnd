
package com.MotorbikeRental.service.impl;


import com.MotorbikeRental.algorithm.Haversine;
import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.dto.ModelDto;
import com.MotorbikeRental.dto.RegisterMotorbikeDto;
import com.MotorbikeRental.dto.UserDto;

import com.MotorbikeRental.entity.*;

import com.MotorbikeRental.exception.ExistPlateException;
import com.MotorbikeRental.exception.ValidationException;
import com.MotorbikeRental.repository.*;

import com.MotorbikeRental.service.JWTService;
import com.MotorbikeRental.service.MotorbikeImageService;
import com.MotorbikeRental.service.MotorbikeService;
import com.MotorbikeRental.service.UserService;
import com.cloudinary.Cloudinary;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotorbikeServiceImpl  implements MotorbikeService {
   @Autowired
   private final Cloudinary cloudinary;
    @Autowired
    private final ModelMapper mapper;
    @Autowired
    private final MotorbikeRepository motorbikeRepository;
    @Autowired
    private final ModelRepository modelRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private final MotorbikeImageService motorbikeImageService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final MotorbikeFilterRepository motorbikeFilterRepository;

    private final UserService userService;

    private final BookingRepository bookingRepository;
    private  final Haversine haversine;
    @Override
    public Page<MotorbikeDto> getAllMotorbike(int page, int pageSize, Long userId, List<String> roles,String status) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Motorbike> motorbikeList;
        if ("All".equalsIgnoreCase(status)) {
            if (roles.contains("ADMIN")) {
                motorbikeList = motorbikeRepository.findAll(pageable);
            } else {
                motorbikeList = motorbikeRepository.findAllByOwner(roles, userId, pageable);
            }
        } else if (roles.contains("ADMIN")) {
            motorbikeList = motorbikeRepository.findAllByStatus(MotorbikeStatus.valueOf(status), pageable);
        } else {
            motorbikeList = motorbikeRepository.findAllByStatusByLessor(MotorbikeStatus.valueOf(status), userId, pageable);
        }
            List<MotorbikeDto> dtoList = motorbikeList.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new PageImpl<>(dtoList, pageable, motorbikeList.getTotalElements());
    }

        private MotorbikeDto convertToDto(Motorbike motorbike) {
            MotorbikeDto dto = mapper.map(motorbike, MotorbikeDto.class);

            if (motorbike.getModel() != null) {
                ModelDto modelDto = mapper.map(motorbike.getModel(), ModelDto.class);
                dto.setModel(modelDto);
            } else {
                dto.setModel(null);
            }
            UserDto userDto = userService.convertToDto(motorbike.getUser());
//            dto.setModel(modelDto);
            dto.setUser(userDto);

            double avgRate = motorbike.getBookingList().stream()
                    .map(Booking::getFeedback)
                    .filter(Objects::nonNull)
                    .mapToInt(FeedBack::getRate)
                    .average()
                    .orElse(0.0);

            dto.setAvgRate(avgRate);

            return dto;
        }

    @Override
    public MotorbikeDto getMotorbikeById(Long id) {
        Motorbike motorbike = motorbikeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Motorbike not found"));
        return convertToDto(motorbike);
    }

    @Override
    public Page<Motorbike> getMotorbikeWithPagination(int page, int pageSize){
        return motorbikeRepository.findAll(PageRequest.of(page,pageSize));
    }

    @Override
    public Page<MotorbikeDto> searchByPlate(String searchTerm,String status,Long userId,List<String> roles, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Motorbike> motorbikePage;
        if("All".equalsIgnoreCase(status)) {
            if (roles.contains("ADMIN")) {
                motorbikePage = motorbikeRepository.searchAllMotorbikePlate(searchTerm, pageable);
            } else {
                motorbikePage = motorbikeRepository.searchMotorbikePlateByLessor(searchTerm, userId, pageable);
            }
        } else if (roles.contains("ADMIN")) {
            motorbikePage = motorbikeRepository.searchMotorbikePlateAndStatus(searchTerm, MotorbikeStatus.valueOf(status), pageable);
        } else{
            motorbikePage = motorbikeRepository.searchMotorbikePlateAndStatusByLessor(searchTerm,MotorbikeStatus.valueOf(status),userId,pageable);
        }
        List<MotorbikeDto> dtoList = motorbikePage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, motorbikePage.getTotalElements());
    }

    @Override
    public void toggleMotorbikeStatus(Long id) {
        Motorbike motorbike = motorbikeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorbike not found with id: " + id));

        List<Booking> bookings = bookingRepository.findByMotorbikeId(id);


            if (motorbike.getStatus() == MotorbikeStatus.ACTIVE) {
                for (Booking booking : bookings) {
                    if (booking.getStatus() == BookingStatus.DEPOSIT_MADE) {
                        throw new ValidationException("Cannot de-active a motorbike in DEPOSIT_MADE state.");
                    } else if (booking.getStatus() == BookingStatus.BUSY) {
                        throw new ValidationException("Cannot de-active a motorbike in BUSY state.");
                    } else if (booking.getStatus() == BookingStatus.RENTING) {
                        throw new ValidationException("Cannot de-active a motorbike in RENTING state.");
                    }
                }
                motorbike.setStatus(MotorbikeStatus.DEACTIVE);
            } else if (motorbike.getStatus() == MotorbikeStatus.DEACTIVE) {
                motorbike.setStatus(MotorbikeStatus.ACTIVE);
            }
            motorbikeRepository.save(motorbike);

        }



    @Override
    public List<Motorbike> getMotorbikeByLessorId() {
        return null;
    }

    @Override
    public List<MotorbikeDto> getAllMotorbikeByStatus(MotorbikeStatus status) {

        List<Motorbike> motorbikeList = motorbikeRepository.getAllMotorbikeByStatus(status);
        List<MotorbikeDto> dtoList = motorbikeList.stream()
                .map(motorbike -> {
                    MotorbikeDto dto = mapper.map(motorbike, MotorbikeDto.class);
                    if (motorbike.getUser() != null) {
                        UserDto userDto = userService.convertToDto(motorbike.getUser());
                        dto.setUser(userDto);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        return dtoList;
    }
    @Override
    public Motorbike registerMotorbike(String accessToken,RegisterMotorbikeDto registerMotorbikeDto) {
        System.out.println(registerMotorbikeDto.getMotorbikeAddress()+registerMotorbikeDto.getLatitude()+registerMotorbikeDto.getLongitude());
        if(motorbikeRepository.existsByMotorbikePlate(registerMotorbikeDto.getMotorbikePlate())){
            throw  new ExistPlateException("The plate is exist in the system");
        }
        String token = accessToken.split(" ")[1];
        String username = jwtService.extractUsername(token);
        System.out.println(username);
        Optional<User> user = userRepository.findByEmail(username);
        if(motorbikeRepository.countMotorbikeByUser(user.get().getId())==0){
            userService.addLessor(user.get());
        }
        Motorbike motorbike=new Motorbike();
        motorbike.setMotorbikeAddress(registerMotorbikeDto.getMotorbikeAddress());
        motorbike.setMotorbikePlate(registerMotorbikeDto.getMotorbikePlate());
        motorbike.setConstraintMotorbike(registerMotorbikeDto.getConstraintMotorbike());
        motorbike.setModel(modelRepository.findById(registerMotorbikeDto.getModelId()).get());
        motorbike.setUser(user.get());
        motorbike.setDelivery(registerMotorbikeDto.isDelivery());
        motorbike.setPrice(registerMotorbikeDto.getPrice());
        motorbike.setOvertimeLimit(registerMotorbikeDto.getOvertimeLimit());
        motorbike.setOvertimeFee(registerMotorbikeDto.getOvertimeFee());
        motorbike.setDeliveryFee(registerMotorbikeDto.getDeliveryFee());
        motorbike.setFreeShipLimit(registerMotorbikeDto.getFreeShipLimit());
        motorbike.setYearOfManuFacture(registerMotorbikeDto.getYearOfManufacture());
        motorbike.setStatus(MotorbikeStatus.PENDING);
        motorbike.setTripCount(Long.valueOf(0));
        motorbike.setLatitude(registerMotorbikeDto.getLatitude());
        motorbike.setLongitude(registerMotorbikeDto.getLongitude());
        Motorbike savedMotorbike=motorbikeRepository.save(motorbike);
        return savedMotorbike;
    }

    @Override
    public Page<MotorbikeDto> listMotorbikeByFilter(FilterMotorbikeDto filterMotorbikeDto, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Motorbike> filter = motorbikeFilterRepository.listMotorbikeByFilter(
                filterMotorbikeDto.getStartDate(),
                filterMotorbikeDto.getEndDate(),
                filterMotorbikeDto.getBrandId(),
                filterMotorbikeDto.getModelType(),
                filterMotorbikeDto.getIsDelivery(),
                filterMotorbikeDto.getMinPrice(),
                filterMotorbikeDto.getMaxPrice()
        );

            List<MotorbikeDto> dtoList = filter.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        if (!dtoList.isEmpty()) {
            Iterator<MotorbikeDto> iterator = dtoList.iterator();
            while (iterator.hasNext()) {
                MotorbikeDto motorbikeDto = iterator.next();

                if (filterMotorbikeDto.getIsFiveStar() != null && filterMotorbikeDto.getIsFiveStar()) {
                    List<Long> fiveStarUserIdList = motorbikeFilterRepository.getFiveStarLessor();
                    if (!fiveStarUserIdList.contains(motorbikeDto.getUserId())) {
                        iterator.remove(); // Sử dụng iterator.remove() để xóa phần tử an toàn
                        continue; // Bỏ qua kiểm tra tiếp theo nếu phần tử đã bị xóa
                    }
                }

                if (filterMotorbikeDto.getLongitude() != null && filterMotorbikeDto.getLatitude() != null &&
                        motorbikeDto.getLatitude() != null && motorbikeDto.getLongitude() != null) {
                    if (haversine.CalculateTheDistanceAsTheCrowFlies(
                            motorbikeDto.getLatitude(), motorbikeDto.getLongitude(),
                            filterMotorbikeDto.getLatitude(), filterMotorbikeDto.getLongitude()) > 30) {
                        iterator.remove(); // Sử dụng iterator.remove() để xóa phần tử an toàn
                    }
                }
            }
        }
        int start = (int) pageable.getOffset();
        if (start >= dtoList.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, dtoList.size());
        }
        int end = Math.min((start + pageable.getPageSize()), dtoList.size());
        List<MotorbikeDto> pagedDtoList = dtoList.subList(start, end);
            return  new PageImpl<>(pagedDtoList, pageable, dtoList.size());
    }



    @Override
    public MotorbikeDto checkExistPlate( String motorbikePlate) {
        Motorbike motorbike=motorbikeRepository.findByMotorbikePlate(motorbikePlate);
        return motorbike==null?null:convertToDto(motorbike);
    }

    @Override
    public Page<MotorbikeDto> getPendingMotorbikes(MotorbikeStatus status,int page,int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Motorbike> motorbikeList = motorbikeRepository.getAllMotorbikeByStatus(status);
        List<MotorbikeDto> dtoList = motorbikeList.stream()
                .map(motorbike -> mapper.map(motorbike, MotorbikeDto.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }

    @Override
    public Motorbike approveMotorbike(Long id) {
        return updateMotorbikeStatus(id, MotorbikeStatus.ACTIVE);
    }

    @Override
    public Motorbike rejectMotorbike(Long id) {
        return updateMotorbikeStatus(id, MotorbikeStatus.DEACTIVE);
    }

    @Override
    public List<MotorbikeDto> listFiveStar() {
        List<MotorbikeDto>motorbikeList=new ArrayList<>();
          List<Long> users=motorbikeFilterRepository.getFiveStarLessor();
          for(MotorbikeDto motorbike:getAllMotorbikeByStatus(MotorbikeStatus.ACTIVE)){
              for(Long id:users){
                  if(motorbike.getUserId()==id){
                      motorbikeList.add(motorbike);
                  }
              }
          }
          return motorbikeList;
    }


    @Override
    public MotorbikeDto existMotorbikeByUserId(Long motorbikeId, Long userId) {
        Motorbike motorbike = motorbikeRepository.existsMotorbikeByUserId(motorbikeId, userId);
        if (motorbike == null) {
            return null;
        }
        return convertToDto(motorbike);
    }

    public MotorbikeDto updateMotorbike(Long id,UpdateMotorbikeDto updateMotorbikeDto) {
        Motorbike motorbike=motorbikeRepository.findById(id).orElseThrow();
         mapper.map(updateMotorbikeDto,motorbike);
         motorbikeRepository.save(motorbike);
         MotorbikeDto motorbikeDto=mapper.map(motorbike,MotorbikeDto.class);
         return motorbikeDto;
    }



        private Motorbike updateMotorbikeStatus (Long id, MotorbikeStatus status){
            Optional<Motorbike> motorbikeOpt = motorbikeRepository.findById(id);
            if (motorbikeOpt.isPresent()) {
                Motorbike motorbike = motorbikeOpt.get();
                motorbike.setStatus(status);
                return motorbikeRepository.save(motorbike);
            } else {
                throw new RuntimeException("Motorbike not found");
            }
        }


        public List<MotorbikeDto> getMotorbikeByUserId(Long userId) {
             List<Motorbike> motorbikeList = motorbikeRepository.findByUserId(userId);
            List<MotorbikeDto> dtoList = motorbikeList.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
             return dtoList;
        }

    }

