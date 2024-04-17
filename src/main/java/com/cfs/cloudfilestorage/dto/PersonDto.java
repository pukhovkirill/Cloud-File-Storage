package com.cfs.cloudfilestorage.dto;

import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.validator.ValidEmail;
import com.cfs.cloudfilestorage.validator.ValidPassword;
import com.cfs.cloudfilestorage.validator.ValidPhone;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    private Long id;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    @ValidPhone
    private String phone;

    @ValidPassword
    @NotNull
    @NotEmpty
    private String password;

    public PersonDto(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.phone = person.getPhone();
        this.password = person.getPassword();
    }
}
