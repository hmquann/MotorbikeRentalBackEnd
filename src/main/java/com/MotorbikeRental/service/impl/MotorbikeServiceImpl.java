
package com.MotorbikeRental.service.impl;

import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.*;

import com.MotorbikeRental.exception.ExistPlateException;
import com.MotorbikeRental.repository.ModelRepository;
import com.MotorbikeRental.repository.MotorbikeFilterRepository;
import com.MotorbikeRental.repository.MotorbikeRepository;
import com.MotorbikeRental.repository.UserRepository;

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


    @Override
    public Page<MotorbikeDto> getAllMotorbike(int page, int pageSize, Long userId, List<String> roles) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Motorbike> motorbikeList;
        if(roles.contains("ADMIN")){
            motorbikeList = motorbikeRepository.findAll(pageable);
        }else{
            motorbikeList = motorbikeRepository.findAllByOwner(roles, userId, pageable);
        }
        List<MotorbikeDto> dtoList = motorbikeList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, motorbikeList.getTotalElements());
    }

        private MotorbikeDto convertToDto(Motorbike motorbike) {
            MotorbikeDto dto = mapper.map(motorbike, MotorbikeDto.class);
            ModelDto modelDto = mapper.map(motorbike.getModel(), ModelDto.class);
            dto.setModel(modelDto);

            return dto;
        }

    @Override
    public Page<Motorbike> getMotorbikeWithPagination(int page, int pageSize){
        return motorbikeRepository.findAll(PageRequest.of(page,pageSize));
    }

    @Override
    public Page<MotorbikeDto> searchByPlate(String searchTerm,Long userId,List<String> roles, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Motorbike> motorbikePage;
        if (roles.contains("ADMIN")) {
            motorbikePage = motorbikeRepository.searchAllMotorbikePlate(searchTerm, pageable);
        } else {
            motorbikePage = motorbikeRepository.searchMotorbikePlateByLessor(searchTerm, userId, pageable);
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

            if (motorbike.getStatus() == MotorbikeStatus.ACTIVE) {
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
                .map(motorbike -> mapper.map(motorbike, MotorbikeDto.class))
                .collect(Collectors.toList());
        return dtoList;
    }
    @Override
    public Motorbike registerMotorbike(String accessToken,RegisterMotorbikeDto registerMotorbikeDto) {
        if(motorbikeRepository.existsByMotorbikePlate(registerMotorbikeDto.getMotorbikePlate())){
            throw  new ExistPlateException("The plate is exist in the system");
        }
        String token = accessToken.split(" ")[1];
        String username = jwtService.extractUsername(token);
        System.out.println(username);
        Optional<User> user = userRepository.findByEmail(username);

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
        Motorbike savedMotorbike=motorbikeRepository.save(motorbike);
        return savedMotorbike;
    }

    @Override
    public List<MotorbikeDto> listMotorbikeByFilter(FilterMotorbikeDto filterMotorbikeDto) {
        List <Motorbike> filter=motorbikeFilterRepository.listMotorbikeByFilter(
                filterMotorbikeDto.getStartTime(),
                filterMotorbikeDto.getEndTime(),
                filterMotorbikeDto.getAddress(),
        filterMotorbikeDto.getBrandId(),
        filterMotorbikeDto.getFuelType(),
        filterMotorbikeDto.getModelType(),
        filterMotorbikeDto.isDelivery()
        );
        List<MotorbikeDto> dtoList = filter.stream()
                .map(motorbike -> mapper.map(motorbike, MotorbikeDto.class))
                .collect(Collectors.toList());
        return dtoList;
    }



    @Override
    public Motorbike checkExistPlate( String motorbikePlate) {
        return motorbikeRepository.findByMotorbikePlate(motorbikePlate);
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

    private Motorbike updateMotorbikeStatus(Long id, MotorbikeStatus status) {
        Optional<Motorbike> motorbikeOpt = motorbikeRepository.findById(id);
        if (motorbikeOpt.isPresent()) {
            Motorbike motorbike = motorbikeOpt.get();
            motorbike.setStatus(status);
            return motorbikeRepository.save(motorbike);
        } else {
            throw new RuntimeException("Motorbike not found");
        }
    }



}

