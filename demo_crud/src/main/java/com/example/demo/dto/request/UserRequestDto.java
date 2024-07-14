package com.example.demo.dto.request;

import com.example.demo.util.Gender;
import com.example.demo.util.UserStatus;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
public class UserRequestDto implements Serializable {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Date dateOfbirth;

    private Gender gender;

    private String username;

    private String password;

    private String type;

    private Set<AddressDTO> addresses;

    private UserStatus status;

    public UserRequestDto(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
}
