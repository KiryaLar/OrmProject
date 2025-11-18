package ru.larkin.ormproject.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larkin.ormproject.dto.profile.ProfileResponseDto;
import ru.larkin.ormproject.entity.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Integer id;
    private String name;
    private String email;
    private Role role;
    private ProfileResponseDto profile;
}
