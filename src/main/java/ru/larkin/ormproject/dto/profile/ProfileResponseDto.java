package ru.larkin.ormproject.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larkin.ormproject.entity.Gender;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    private Integer id;
    private String bio;
    private String city;
    private String avatarUrl;
    private LocalDate birthDate;
    private Gender gender;
    private String phoneNumber;
    private Instant createdAt;
    private Instant updatedAt;
}
