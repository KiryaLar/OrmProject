package ru.larkin.ormproject.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larkin.ormproject.entity.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    @Size(max = 255)
    private String name;

    @Email
    @Size(max = 255)
    private String email;

    private Role role;

    private Integer profileId;
}
