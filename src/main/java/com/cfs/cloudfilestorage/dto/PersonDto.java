package com.cfs.cloudfilestorage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotEmpty(message = "Email should not be empty")
    private String email;

    private String phone;

    @NotEmpty(message = "Password should not be empty")
    private String password;
}
