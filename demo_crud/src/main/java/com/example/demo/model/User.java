package com.example.demo.model;

import com.example.demo.util.Gender;
import com.example.demo.util.UserStatus;
import com.example.demo.util.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user")
public class User extends BaseEntity{

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

//    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    @Column(name ="email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    //    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType type;

    //    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @OneToMany(cascade =CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Address> addresses = new HashSet<>();

    public void saveAddress(Address address) {
        if (address != null) {
            if (addresses == null) {
                addresses = new HashSet<>();
            }
            addresses.add(address);
            address.setUser(this); //
        }
    }

    @JsonIgnore // dung vong lap vo han
    public Set<Address> getAddresses(){
        return addresses;
    }
}
