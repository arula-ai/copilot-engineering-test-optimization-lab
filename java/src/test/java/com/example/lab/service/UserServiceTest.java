package com.example.lab.service;

import com.example.lab.model.dto.CreateUserRequest;
import com.example.lab.model.entity.User;
import com.example.lab.model.enums.UserStatus;
import com.example.lab.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // REDUNDANT: This setup is repeated in each test instead of using @BeforeEach properly
    private void setupMocks() {
        // Duplicate setup logic
    }

    @BeforeEach
    void setUp() {
        setupMocks();
    }

    // REDUNDANT: These tests should use @ParameterizedTest
    // Instead, each email is tested individually with duplicate code

    @Test
    void shouldValidateTestAtExampleDotComAsValid() {
        // Redundant pattern
        String email = "test@example.com";
        boolean result = userService.isValidEmail(email);
        assertTrue(result);
    }

    @Test
    void shouldValidateUserDotNameAtDomainDotOrgAsValid() {
        // Redundant pattern
        String email = "user.name@domain.org";
        boolean result = userService.isValidEmail(email);
        assertTrue(result);
    }

    @Test
    void shouldValidateAPlusBAtTestDotCoAsValid() {
        // Redundant pattern
        String email = "a+b@test.co";
        boolean result = userService.isValidEmail(email);
        assertTrue(result);
    }

    @Test
    void shouldValidateInvalidEmailWithoutAtAsInvalid() {
        // Redundant pattern
        String email = "invalidemail";
        boolean result = userService.isValidEmail(email);
        assertFalse(result);
    }

    @Test
    void shouldValidateEmailWithoutDomainAsInvalid() {
        // Redundant pattern
        String email = "test@";
        boolean result = userService.isValidEmail(email);
        assertFalse(result);
    }

    @Test
    void shouldValidateEmailWithoutLocalPartAsInvalid() {
        // Redundant pattern
        String email = "@domain.com";
        boolean result = userService.isValidEmail(email);
        assertFalse(result);
    }

    // THESE SHOULD BE PARAMETERIZED LIKE:
    // @ParameterizedTest
    // @CsvSource({
    //     "test@example.com, true",
    //     "user.name@domain.org, true",
    //     "invalidemail, false",
    //     "test@, false"
    // })
    // void shouldValidateEmail(String email, boolean expected) {
    //     assertEquals(expected, userService.isValidEmail(email));
    // }

    // REDUNDANT password validation tests - same issue
    @Test
    void shouldRejectPasswordShorterThan8Characters() {
        var result = userService.validatePassword("Short1!");
        assertFalse(result.valid());
        assertTrue(result.error().contains("8 characters"));
    }

    @Test
    void shouldRejectPasswordWithoutUppercase() {
        var result = userService.validatePassword("lowercase1!");
        assertFalse(result.valid());
        assertTrue(result.error().contains("uppercase"));
    }

    @Test
    void shouldRejectPasswordWithoutLowercase() {
        var result = userService.validatePassword("UPPERCASE1!");
        assertFalse(result.valid());
        assertTrue(result.error().contains("lowercase"));
    }

    @Test
    void shouldRejectPasswordWithoutNumber() {
        var result = userService.validatePassword("NoNumber!");
        assertFalse(result.valid());
        assertTrue(result.error().contains("number"));
    }

    @Test
    void shouldRejectPasswordWithoutSpecialCharacter() {
        var result = userService.validatePassword("NoSpecial1");
        assertFalse(result.valid());
        assertTrue(result.error().contains("special"));
    }

    @Test
    void shouldAcceptValidPassword() {
        var result = userService.validatePassword("ValidPass1!");
        assertTrue(result.valid());
    }

    // REDUNDANT phone validation tests
    @Test
    void shouldValidate10DigitPhoneAsValid() {
        assertTrue(userService.isValidPhone("1234567890"));
    }

    @Test
    void shouldValidatePhoneWithDashesAsValid() {
        assertTrue(userService.isValidPhone("123-456-7890"));
    }

    @Test
    void shouldValidatePhoneWithCountryCodeAsValid() {
        assertTrue(userService.isValidPhone("+1 234 567 8900"));
    }

    @Test
    void shouldValidateShortPhoneAsInvalid() {
        assertFalse(userService.isValidPhone("12345"));
    }

    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserRequest request = CreateUserRequest.builder()
            .email("new@example.com")
            .password("ValidPass1!")
            .firstName("New")
            .lastName("User")
            .build();

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("user-123");
            return user;
        });

        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
    }

    // MISSING TESTS:
    // - createUser with invalid email
    // - createUser with weak password
    // - createUser with short first name
    // - createUser with short last name
    // - createUser with duplicate email
    // - getUser
    // - getUserByEmail
    // - getAllUsers
    // - updateUser
    // - updateUserStatus
    // - changePassword
    // - deactivateUser
}
