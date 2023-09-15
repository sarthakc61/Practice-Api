package com.api.practice.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(max = 12, min = 3, message = "name must be between 3 to 12 characters")
    private String name;
    @Size(min = 3, message = "password must be between 3 to 12 characters")
    private String password;
    @Email
    private String email;
    private String roles;
}
