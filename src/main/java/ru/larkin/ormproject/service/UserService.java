package ru.larkin.ormproject.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.larkin.ormproject.dto.user.UserCreateDto;
import ru.larkin.ormproject.dto.user.UserResponseDto;
import ru.larkin.ormproject.entity.Enrollment;
import ru.larkin.ormproject.entity.Profile;
import ru.larkin.ormproject.entity.Role;
import ru.larkin.ormproject.entity.User;
import ru.larkin.ormproject.exception.NotFoundException;
import ru.larkin.ormproject.repository.EnrollmentRepository;
import ru.larkin.ormproject.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ModelMapper modelMapper;

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponseDto getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> NotFoundException.forUser(id));
    }

    public List<UserResponseDto> getByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public UserResponseDto createUser(UserCreateDto dto) {
        userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("User with email '%s' already exists".formatted(dto.getEmail()));
        });
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        Profile profile = new Profile();
        profile.setUser(user);
        user.setProfile(profile);

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    @Transactional
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.forUser(id));
        userRepository.delete(user);
    }

    public List<UserResponseDto> getStudentsByCourse(Integer courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(Enrollment::getStudent)
                .map(this::toResponse)
                .toList();
    }

    private UserResponseDto toResponse(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}
