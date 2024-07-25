
package com.MotorbikeRental.service.impl;


import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.dto.ModelDto;
import com.MotorbikeRental.dto.RegisterMotorbikeDto;
import com.MotorbikeRental.dto.UserDto;

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

        Motorbike savedMotorbike=motorbikeRepository.save(motorbike);
        return savedMotorbike;
    }

    @Override
    public List<MotorbikeDto> listMotorbikeByFilter(FilterMotorbikeDto filterMotorbikeDto) {
        List <Motorbike> filter=motorbikeFilterRepository.listMotorbikeByFilter(
        filterMotorbikeDto.getStartDate(),
        filterMotorbikeDto.getEndDate(),
        filterMotorbikeDto.getAddress(),
        filterMotorbikeDto.getBrandId(),
        filterMotorbikeDto.getModelType(),
        filterMotorbikeDto.getIsDelivery(),
        filterMotorbikeDto.getMinPrice(),
        filterMotorbikeDto.getMaxPrice()
        );
        if(filter.size()<5) {
            String district = "";
            String province = "";
            if (filterMotorbikeDto.getAddress() != null && !filterMotorbikeDto.getAddress().isEmpty()) {
                int commaIndex = filterMotorbikeDto.getAddress().indexOf(",");
                if (commaIndex != -1) {
                    district = filterMotorbikeDto.getAddress().substring(0, commaIndex).trim();
                    province = filterMotorbikeDto.getAddress().substring(1, commaIndex).trim();
                }
            }

            filter=motorbikeFilterRepository.listMotorbikeByFilter(
                    filterMotorbikeDto.getStartDate(),
                    filterMotorbikeDto.getEndDate(),
                    province,
                    filterMotorbikeDto.getBrandId(),
                    filterMotorbikeDto.getModelType(),
                    filterMotorbikeDto.getIsDelivery(),
                    filterMotorbikeDto.getMinPrice(),
                    filterMotorbikeDto.getMaxPrice()
            );
            final String selectedDistrict = district;
            Collections.sort(filter, new Comparator<Motorbike>() {
                @Override
                public int compare(Motorbike m1, Motorbike m2) {
                    boolean m1ContainsDistrict = m1.getMotorbikeAddress().contains(selectedDistrict);
                    boolean m2ContainsDistrict = m2.getMotorbikeAddress().contains(selectedDistrict);

                    // Nếu m1 chứa district mà m2 không chứa, m1 sẽ được đặt lên trước
                    if (m1ContainsDistrict && !m2ContainsDistrict) {
                        return -1;
                    } else if (!m1ContainsDistrict && m2ContainsDistrict) {
                        return 1;
                    } else {
                        return 0;
                    }

        });
        List<MotorbikeDto> dtoList = filter.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        if(filterMotorbikeDto.getIsFiveStar()!=null){
            List<Long>fiveStarUserIdList=motorbikeFilterRepository.getFiveStarLessor();
            for(MotorbikeDto motorbikeDto:dtoList){
                if(!fiveStarUserIdList.contains(motorbikeDto.getUserId())){
                  dtoList.remove(motorbikeDto);
                }
            });
        }
            List<MotorbikeDto> dtoList = filter.stream()
                    .map(motorbike -> mapper.map(motorbike, MotorbikeDto.class))
                    .collect(Collectors.toList());
            if (filterMotorbikeDto.getIsFiveStar() != null) {
                List<Long> fiveStarUserIdList = motorbikeFilterRepository.getFiveStarLessor();
                for (MotorbikeDto motorbikeDto : dtoList) {
                    if (!fiveStarUserIdList.contains(motorbikeDto.getUserId())) {
                        dtoList.remove(motorbikeDto);
                    }
                }
            }

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


    @Override
    public MotorbikeDto existMotorbikeByUserId(Long motorbikeId, Long userId) {
        Motorbike motorbike = motorbikeRepository.existsMotorbikeByUserId(motorbikeId, userId);
        return convertToDto(motorbike);
    }

    public MotorbikeDto updateMotorbike(Long id,UpdateMotorbikeDto updateMotorbikeDto) {
        Motorbike motorbike=motorbikeRepository.findById(id).orElseThrow();
         mapper.map(updateMotorbikeDto,motorbike);
         motorbikeRepository.save(motorbike);
         MotorbikeDto motorbikeDto=mapper.map(motorbike,MotorbikeDto.class);
         return motorbikeDto;
    }

        public MotorbikeDto updateMotorbike (Long id, UpdateMotorbikeDto updateMotorbikeDto){
            Motorbike motorbike = motorbikeRepository.findById(id).orElseThrow();
            mapper.map(updateMotorbikeDto, motorbike);
            motorbikeRepository.save(motorbike);
            MotorbikeDto motorbikeDto = mapper.map(motorbike, MotorbikeDto.class);
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


    }

