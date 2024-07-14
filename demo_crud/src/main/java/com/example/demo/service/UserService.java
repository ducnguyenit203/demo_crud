package com.example.demo.service;

import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserDetailResponse;
import com.example.demo.util.UserStatus;

import java.util.List;

public interface UserService {

    UserDetailResponse saveUse(UserRequestDto request);

    UserDetailResponse updateUser(long userId, UserRequestDto request);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    List<UserDetailResponse> getAllUser(int pageNo, int pageSize);

    List<UserDetailResponse>getAllUserBySort(int pageNo, int pageSize, String sortBy);

//    List<UserDetailResponse>List<UserDetailResponse>getAllUserWithSortByMultipleColums(int pageNo, int pageSize, String... sorts);

    PageResponse<?>getAllUserWithSortByMultipleColums(int pageNo, int pageSize, String... sorts);

    PageResponse<?>getAllUserWithSortByColumsAndSearch(int pageNo, int pageSize, String search, String sortBy);

}
