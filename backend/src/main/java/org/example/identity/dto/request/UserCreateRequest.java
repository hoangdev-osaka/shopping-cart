package org.example.identity.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import org.example.common.enums.Gender;

import java.time.LocalDate;

public record UserCreateRequest(
        @NotBlank(message = "{user.firstName.notBlank}")
        @Size(min = 1, max = 100, message = "{user.firstName.size}")
        String firstName,

        @NotBlank(message = "{user.lastName.notBlank}")
        @Size(min = 1, max = 100, message = "{user.lastName.size}")
        String lastName,

        Gender gender,

        @Past(message = "{user.birthday.past}")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthday,

        @NotBlank(message = "{user.email.notBank}")
        @Email(message = "{user.email.invalid}")
        @Size(max=255,message = "{user.email.size}")
        String email,
        @NotBlank(message = "{user.password.notBlank}")
        String password,

        @Size(max = 16,message = "{user.phone.size}")
        String phone,

        @Size(max = 255, message ="{user.avatarUrl.size}")
        String avatarUrl

        ) {
}
