package com.MotorbikeRental.service.impl;


import com.MotorbikeRental.config.VNPayConfig;
import com.MotorbikeRental.dto.*;
import com.MotorbikeRental.entity.*;
import com.MotorbikeRental.exception.UserNotFoundException;
import com.MotorbikeRental.repository.BookingRepository;
import com.MotorbikeRental.repository.RoleRepository;
import com.MotorbikeRental.repository.TransactionRepository;
import com.MotorbikeRental.repository.UserRepository;
import com.MotorbikeRental.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private final VNPayConfig vnpayConfig;

    @Autowired
    private final TransactionRepository transactionRepository;
    @Autowired
    private final ModelMapper mapper;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByEmailOrPhone(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                if (!user.isActive()) {
                    throw new LockedException("User is locked");
                }
                return user;
            }

        };
    }


    @Override
    public void activeUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }
    public void toggleUserActiveStatus(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }



    @Override
    public UserDto getUserDtoById(Long id) {
        User user= userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        return convertToDto(user);
}

    @Override
    public UserDto getAdmin() {
        User user= userRepository.getAdmin()
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return convertToDto(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public UserDto getUserDtoByEmail(String email) {
         User user=userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
      return mapper.map(user,UserDto.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public UserDto getUserDtoByToken(String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new UserNotFoundException("User with token " + token + " not found"));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public User getUserByToken(String token) {
        return userRepository.findByToken(token)
                .orElseThrow(() -> new UserNotFoundException("User with token " + token + " not found"));
    }

    @Override
    public User updateUser(Long id, User user) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        if (optionalUser.isPresent()) {
            User existUser = optionalUser.get();

            existUser.setFirstName(user.getFirstName());
            existUser.setLastName(user.getLastName());
            existUser.setEmail(user.getEmail());
            existUser.getRoles().clear();
            for (Role role : user.getRoles()) {
                // Kiểm tra xem vai trò đã tồn tại chưa
                Role existingRole = roleRepository.findByName(role.getName());
                if (existingRole != null) {
                    existUser.getRoles().add(existingRole);
                } else {
                    // Nếu vai trò chưa tồn tại, thêm vai trò mới vào cơ sở dữ liệu
                    roleRepository.save(role);
                    existUser.getRoles().add(role);
                }
            }
            return userRepository.save(existUser);

        }
        return null;

    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUser() {
        return List.of();
    }

    public Page<UserDto> getAllUser(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<User> userPage = userRepository.findAllUsersWithRoles(pageable);
        List<UserDto> dtoList = userPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, userPage.getTotalElements());
    }
    public UserDto convertToDto(User user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        userDto.setRole(roleNames);

        List<MotorbikeDto> motorbikeDtos = user.getMotorbikes().stream()
                .map(motorbike -> mapper.map(motorbike, MotorbikeDto.class))
                .collect(Collectors.toList());

        List<BookingDto> bookingDtos = user.getBookingList().stream()
                .map(booking -> mapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());

        if (user.getId() != null) {
            Long totalTripCount = bookingRepository.countBookingsByUserId(user.getId());
            userDto.setTotalTripCount(totalTripCount);
        } else {
            userDto.setTotalTripCount(0L); // or handle appropriately if user.getId() is null
        }

        userDto.setBookings(bookingDtos);
        userDto.setMotorbikes(motorbikeDtos);
        return userDto;
    }

    @Override
    public void addLessor(User user) {
        Role lessor=roleRepository.findByName("LESSOR");
        user.getRoles().add(lessor);
        userRepository.save(user);
    }

    @Override
    public Page<User> getUserByPagination(int page, int pageSize) {
        return userRepository.findAll(PageRequest.of(page,pageSize));
    }

    @Override
    public Page<UserDto> searchUserByEmailOrPhone(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByEmailOrPhone(searchTerm, pageable);
        List<UserDto> dtoList = userPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, userPage.getTotalElements());
    }

    public void updateUserBalance(Long userId, BigDecimal amount) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            BigDecimal currentBalance = user.getBalance();
            if (currentBalance != null) {

                BigDecimal newBalance = currentBalance.add(amount);
                user.setBalance(newBalance);
                userRepository.save(user);
            } else {
              
            }
        } else {
            System.out.println("User with ID " + userId + " not found.");
        }

    }

    @Override
    @Transactional
    public void withdrawMoney(Long userId, BigDecimal amount, String accountNumber, String bankName) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BigDecimal currentBalance = user.getBalance();
            if (currentBalance.compareTo(amount) < 0) {
                throw new Exception("Insufficient money");
            }
            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ADMIN"));

            if (isAdmin) {
                user.setBalance(currentBalance.subtract(amount));
                userRepository.save(user);

                Transaction transaction = new Transaction();
                transaction.setUsers(user);
                transaction.setAmount(amount);
                transaction.setProcessed(true);
                transaction.setTransactionDate(LocalDateTime.now());
                transaction.setType(TransactionType.WITHDRAW);
                transaction.setStatus(TransactionStatus.SUCCESS);
                transaction.setDescription("Rút tiền khỏi ví " + "- Mã giao dịch: " + generateTransactionCode(userId));
                transaction.setAccountNumber(accountNumber);
                transaction.setBankName(bankName);
                transactionRepository.save(transaction);
            } else {
                user.setBalance(currentBalance.subtract(amount));
                userRepository.save(user);

                String transactionCode = generateTransactionCode(userId);
                Transaction transaction = new Transaction();
                transaction.setUsers(user);
                transaction.setAmount(amount);
                transaction.setProcessed(false);
                transaction.setTransactionDate(LocalDateTime.now());
                transaction.setType(TransactionType.WITHDRAW);
                transaction.setStatus(TransactionStatus.PENDING);
                transaction.setDescription("Rút tiền khỏi ví " + "- Mã giao dịch: " + transactionCode);
                transaction.setAccountNumber(accountNumber);
                transaction.setBankName(bankName);
                transactionRepository.save(transaction);


                Long mainAdminId = 1L;
                Optional<User> optionalAdmin = userRepository.findById(mainAdminId);
                if (!optionalAdmin.isPresent()) {
                    throw new Exception("Admin account not found");
                }

                User admin = optionalAdmin.get();

                BigDecimal adminCurrentBalance = admin.getBalance();
                admin.setBalance(adminCurrentBalance.add(amount));
                userRepository.save(admin);

                Transaction adminTransaction = new Transaction();
                adminTransaction.setUsers(admin);
                adminTransaction.setAmount(amount);
                adminTransaction.setProcessed(true);
                adminTransaction.setTransactionDate(LocalDateTime.now());
                adminTransaction.setType(TransactionType.WITHDRAW_REQUEST);
                adminTransaction.setStatus(TransactionStatus.SUCCESS);
                adminTransaction.setDescription("Yêu cầu rút tiền từ " + user.getFirstName() + " " + user.getLastName() + " - " +
                        "Mã giao dịch :" + transactionCode);
                transactionRepository.save(adminTransaction);
            }
        }else {
            throw new Exception("User not found");
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public void approveWithdrawal(Long transactionId) throws Exception {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
        if (!optionalTransaction.isPresent()) {
            throw new Exception("Transaction not found");
        }

        Transaction transaction = optionalTransaction.get();

        if (!transaction.getStatus().equals(TransactionStatus.PENDING)) {
            throw new Exception("Transaction has expired and cannot be approved");
        }

        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(transaction);
        transaction.setProcessed(true);
        transactionRepository.save(transaction);
    }

    @Scheduled(fixedRate = 14400000) //test để hàm chạy mỗi 1 tiếng
    @Transactional
    public void checkPendingTransactions() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Transaction> page;
        LocalDateTime now = LocalDateTime.now();
        do {
            page = transactionRepository.findByTypeAndStatus(TransactionType.WITHDRAW, TransactionStatus.PENDING, pageable);

            for (Transaction transaction : page.getContent()) {
                if (Duration.between(transaction.getTransactionDate(), now).getSeconds() > 60 * 60 * 24) { // tính toán xem đã quá 1 ngày hay chưa
                    User user = transaction.getUsers();
                    user.setBalance(user.getBalance().add(transaction.getAmount()));
                    userRepository.save(user);

                    Long mainAdminId = 1L;
                    Optional<User> optionalAdmin = userRepository.findById(mainAdminId);
                    if (optionalAdmin.isPresent()) {
                        User admin = optionalAdmin.get();
                        BigDecimal adminCurrentBalance = admin.getBalance();
                        admin.setBalance(adminCurrentBalance.subtract(transaction.getAmount()));
                        userRepository.save(admin);

                        Transaction adminTransaction = new Transaction();
                        adminTransaction.setUsers(admin);
                        adminTransaction.setAmount(transaction.getAmount());
                        adminTransaction.setProcessed(true);
                        adminTransaction.setTransactionDate(LocalDateTime.now());
                        adminTransaction.setType(TransactionType.REFUND_WITHDRAW);
                        adminTransaction.setStatus(TransactionStatus.SUCCESS);
                        adminTransaction.setDescription("Hoàn trả tiền rút cho " + user.getFirstName() + " " + user.getLastName());
                        transactionRepository.save(adminTransaction);
                    }

                    transaction.setStatus(TransactionStatus.FAILED);
                    transaction.setProcessed(true);
                    transactionRepository.save(transaction);

                }
            }

            pageable = page.nextPageable();

        } while (page.hasNext());
    }

    @Override
    public void subtractMoney(Long userId, Long receiverId, BigDecimal amount, String motorbikeName, String motorbikePlate) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BigDecimal currentBalance = user.getBalance();
            if (currentBalance.compareTo(amount) < 0) {
                throw new Exception("Insufficient money");
            }
            BigDecimal newBalance = currentBalance.subtract(amount);
            user.setBalance(newBalance);
            userRepository.save(user);

            String transactionCode = generateTransactionCode(userId);

            Transaction transaction = new Transaction();
            transaction.setUsers(user);
            transaction.setAmount(amount);
            transaction.setProcessed(true);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setType(TransactionType.DEPOSIT);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setDescription("Đặt cọc xe " + motorbikeName + '(' + motorbikePlate + ')' + " - Mã giao dịch: " + transactionCode);
            transactionRepository.save(transaction);


            addMoney(receiverId, amount, motorbikeName, motorbikePlate, transactionCode);
        }
    }

    @Override
    public void addMoney(Long userId, BigDecimal amount, String motorbikeName, String motorbikePlate, String transactionCode) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BigDecimal currentBalance = user.getBalance();
            BigDecimal newBalance = currentBalance.add(amount);
            user.setBalance(newBalance);
            userRepository.save(user);

            Transaction transaction = new Transaction();
            transaction.setUsers(user);
            transaction.setAmount(amount);
            transaction.setProcessed(true);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setType(TransactionType.DEPOSIT_RECEIVE);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setDescription("Nhận đặt cọc xe " + motorbikeName + '(' + motorbikePlate + ')' + " - Mã giao dịch: " + transactionCode);
            transactionRepository.save(transaction);
        }
    }

    public void activeUserStatus(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void updateUserEmail(Long id, String email) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    public String getUserNameByEmail(String email) {
        return userRepository.getUserNameByEmail(email);
    }

    @Override
    public String generateTransactionCode(Long userId) {
        // Get the current date in yyyyMMdd format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDate = LocalDate.now().format(dateFormatter);

        // Combine date, user ID, and a random UUID for uniqueness
        String uniquePart = UUID.randomUUID().toString().substring(0, 8); // Take first 8 characters for brevity
        return currentDate + userId  + uniquePart;
     }
  
    public UserDto updateUserNotifications(Long userId, Boolean systemNoti, Boolean emailNoti, Boolean minimizeNoti) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Chỉ cập nhật thuộc tính nào có giá trị khác null
            if (systemNoti != null) {
                user.setSystemNoti(systemNoti);
            }
            if (emailNoti != null) {
                user.setEmailNoti(emailNoti);
            }
            if (minimizeNoti != null) {
                user.setMinimizeNoti(minimizeNoti);
            }
            User user1 = userRepository.save(user);
            return  mapper.map(user, UserDto.class);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
}

    @Override
    public void refundSubtractMoney(Long userId, Long receiverId, BigDecimal amount, String motorbikeName, String motorbikePlate) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BigDecimal currentBalance = user.getBalance();
            if (currentBalance.compareTo(amount) < 0) {
                throw new Exception("Insufficient money");
            }
            BigDecimal newBalance = currentBalance.subtract(amount);
            user.setBalance(newBalance);
            userRepository.save(user);

            String transactionCode = generateTransactionCode(userId);

            Transaction transaction = new Transaction();
            transaction.setUsers(user);
            transaction.setAmount(amount);
            transaction.setProcessed(true);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setType(TransactionType.REFUND);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setDescription("Hoàn cọc xe " + motorbikeName + '(' + motorbikePlate + ')' + " - Mã giao dịch: " + transactionCode);
            transactionRepository.save(transaction);


            refundAddMoney(receiverId, amount, motorbikeName, motorbikePlate, transactionCode);
        }
    }

    @Override
    public void refundAddMoney(Long userId, BigDecimal amount, String motorbikeName, String motorbikePlate, String transactionCode) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BigDecimal currentBalance = user.getBalance();
            BigDecimal newBalance = currentBalance.add(amount);
            user.setBalance(newBalance);
            userRepository.save(user);

            Transaction transaction = new Transaction();
            transaction.setUsers(user);
            transaction.setAmount(amount);
            transaction.setProcessed(true);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setType(TransactionType.REFUND_RECEIVE);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setDescription("Nhận tiền hoàn cọc xe " + motorbikeName + '(' + motorbikePlate + ')' + " - Mã giao dịch: " + transactionCode);
            transactionRepository.save(transaction);
        }
    }

    @Override
    public void punishSubtractMoney(Long userId, Long receiverId, BigDecimal amount, String motorbikeName, String motorbikePlate) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BigDecimal currentBalance = user.getBalance();
            if (currentBalance.compareTo(amount) < 0) {
                throw new Exception("Insufficient money");
            }
            BigDecimal newBalance = currentBalance.subtract(amount);
            user.setBalance(newBalance);
            userRepository.save(user);

            String transactionCode = generateTransactionCode(userId);

            Transaction transaction = new Transaction();
            transaction.setUsers(user);
            transaction.setAmount(amount);
            transaction.setProcessed(true);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setType(TransactionType.PUNISH);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setDescription("Phạt tiền hủy chuyến xe " + motorbikeName + '(' + motorbikePlate + ')' + " - Mã giao dịch: " + transactionCode);
            transactionRepository.save(transaction);

            punishAddMoney(receiverId, amount, motorbikeName, motorbikePlate, transactionCode);
        }
    }

    @Override
    public void punishAddMoney(Long userId, BigDecimal amount, String motorbikeName, String motorbikePlate, String transactionCode) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            BigDecimal currentBalance = user.getBalance();
            BigDecimal newBalance = currentBalance.add(amount);
            user.setBalance(newBalance);
            userRepository.save(user);

            Transaction transaction = new Transaction();
            transaction.setUsers(user);
            transaction.setAmount(amount);
            transaction.setProcessed(true);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setType(TransactionType.PUNISH_RECEIVE);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setDescription("Nhận tiền phạt hủy chuyến xe " + motorbikeName + '(' + motorbikePlate + ')' + " - Mã giao dịch: " + transactionCode);
            transactionRepository.save(transaction);

        }
    }

}


