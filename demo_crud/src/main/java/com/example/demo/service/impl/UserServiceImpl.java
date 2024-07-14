package com.example.demo.service.impl;

import com.example.demo.Const.AppConst;
import com.example.demo.dto.request.AddressDTO;
import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserDetailResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Address;
import com.example.demo.model.User;
import com.example.demo.repository.SearchRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.UserStatus;
import com.example.demo.util.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SearchRepository searchRepository;

    @Override
    public UserDetailResponse saveUse(UserRequestDto request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfbirth())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .status(request.getStatus())
                .type(UserType.valueOf(request.getType().toUpperCase()))
                .build();
        request.getAddresses().forEach(a ->
                user.saveAddress(Address.builder()
                        .apartmentNumber(a.getApartmentNumber())
                        .floor(a.getFloor())
                        .building(a.getBuilding())
                        .streetNumber(a.getStreetNumber())
                        .street(a.getStreet())
                        .city(a.getCity())
                        .country(a.getCountry())
                        .addressType(a.getAddressType())
                        .build()));

        userRepository.save(user);

        log.info("User has save!");

        return modelMapper.map(user, UserDetailResponse.class);
    }

    @Override
    public UserDetailResponse updateUser(long userId, UserRequestDto request) {
        User user = getUserById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfbirth());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        if (!request.getEmail().equals(user.getEmail())) {
            user.setEmail(request.getEmail());
        }
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setStatus(request.getStatus());
        user.setType(UserType.valueOf(request.getType().toUpperCase()));
        user.setAddresses(convertToAddress(request.getAddresses()));
        userRepository.save(user);

        log.info("User updated successfully");
        return modelMapper.map(user, UserDetailResponse.class);
    }


    @Override
    public void changeStatus(long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        userRepository.save(user);
        log.info("success status");
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
        log.info("delete success");

    }

    @Override
    public UserDetailResponse getUser(long userId) {
        User user = getUserById(userId);
        return UserDetailResponse.builder()
                .id(userId)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }

    @Override
    public List<UserDetailResponse> getAllUser(int pageNo, int pageSize) {
//        int p = 0;
//        if (pageNo > 0) {
//            p = pageNo-1;
//        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> users = userRepository.findAll(pageable);
        return users.stream().map(user -> UserDetailResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build()).toList();
    }

    @Override
    public List<UserDetailResponse> getAllUserBySort(int pageNo, int pageSize, String sortBy) {
        int p = 0;
        if (pageNo > 0) {
            p = pageNo - 1;
        }
        List<Sort.Order> sorts = new ArrayList<>();
        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile(AppConst.SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sorts));  // Sort.by(Sort.Direction.ASC)
        Page<User> users = userRepository.findAll(pageable);

        return users.stream().map(user -> UserDetailResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build()).toList();
//        return convertToAddress(users, pageable);
    }

    @Override
    public PageResponse<?> getAllUserWithSortByMultipleColums(int pageNo, int pageSize, String... sorts) {
//        if (pageNo > 0) {
//            pageNo = pageNo -1;
//        }
        List<Sort.Order> orders = new ArrayList<>();
        for (String sortBy: sorts) {
            log.info("sort By: {}", sortBy);
            Pattern pattern = Pattern.compile(AppConst.SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orders));
        Page<User> users = userRepository.findAll(pageable);

        List<UserDetailResponse> responses = users.stream().map(user -> UserDetailResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build()).toList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(users.getTotalPages())
                .items(responses)
                .build();
    }

    @Override
    public PageResponse<?> getAllUserWithSortByColumsAndSearch(int pageNo, int pageSize, String search, String sortBy) {

        return null;
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Set<Address> convertToAddress(Set<AddressDTO> addresses) {
        Set<Address> result = new HashSet<>();
        addresses.forEach(a ->
                result.add(Address.builder()
                        .apartmentNumber(a.getApartmentNumber())
                        .floor(a.getFloor())
                        .building(a.getBuilding())
                        .streetNumber(a.getStreetNumber())
                        .street(a.getStreet())
                        .city(a.getCity())
                        .country(a.getCountry())
                        .addressType(a.getAddressType())
                        .build())
        );
        return result;
    }

//    private PageResponse<?> convertToPageResponse(Page<User> users, Pageable pageable) {
//        List<UserDetailResponse> response = users.stream().map(user -> UserDetailResponse.builder()
//                .id(user.getId())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .email(user.getEmail())
//                .phone(user.getPhone())
//                .build()).toList();
//        return PageResponse.builder()
//                .page(pageable.getPageNumber())
//                .size(pageable.getPageSize())
//                .total(users.getTotalPages())
//                .items(response)
//                .build();
//    }
}