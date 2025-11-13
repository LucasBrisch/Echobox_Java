package com.EchoBox.service;

import com.EchoBox.model.User;
import com.EchoBox.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("plainPassword123");
        testUser.setIsAdmin(false);
    }

    @Test
    public void testRegisterUser_Success() {
        // Given
        String encodedPassword = "$2a$10$encodedPasswordHash";

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setEmail("test@example.com");
        savedUser.setPassword(encodedPassword);
        savedUser.setIsAdmin(false);

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.register(testUser);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(encodedPassword, result.getPassword());
        assertFalse(result.getIsAdmin());

        verify(passwordEncoder, times(1)).encode("plainPassword123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_PasswordIsEncoded() {
        // Given
        String plainPassword = "mySecurePassword";
        String encodedPassword = "$2a$10$hashedPassword";

        testUser.setPassword(plainPassword);

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setEmail("test@example.com");
        savedUser.setPassword(encodedPassword);

        when(passwordEncoder.encode(plainPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.register(testUser);

        // Then
        assertNotNull(result);
        assertEquals(encodedPassword, result.getPassword());
        assertNotEquals(plainPassword, result.getPassword());

        verify(passwordEncoder).encode(plainPassword);
    }

    @Test
    public void testRegisterUser_WithAdminRole() {
        // Given
        testUser.setIsAdmin(true);
        String encodedPassword = "$2a$10$encodedAdminPassword";

        User savedUser = new User();
        savedUser.setId(2);
        savedUser.setEmail("admin@example.com");
        savedUser.setPassword(encodedPassword);
        savedUser.setIsAdmin(true);

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.register(testUser);

        // Then
        assertNotNull(result);
        assertTrue(result.getIsAdmin());
        assertEquals("admin@example.com", result.getEmail());
    }

    @Test
    public void testRegisterUser_WithCompany() {
        // Given
        testUser.setCompanyId(5);
        String encodedPassword = "$2a$10$encodedPassword";

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setEmail("test@example.com");
        savedUser.setPassword(encodedPassword);
        savedUser.setCompanyId(5);

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.register(testUser);

        // Then
        assertNotNull(result);
        assertEquals(5, result.getCompanyId());
    }

    @Test
    public void testRegisterUser_WithPicture() {
        // Given
        testUser.setPicture("https://example.com/avatar.jpg");
        String encodedPassword = "$2a$10$encodedPassword";

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setEmail("test@example.com");
        savedUser.setPassword(encodedPassword);
        savedUser.setPicture("https://example.com/avatar.jpg");

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.register(testUser);

        // Then
        assertNotNull(result);
        assertEquals("https://example.com/avatar.jpg", result.getPicture());
    }

    @Test
    public void testRegisterUser_VerifyRepositorySaveIsCalled() {
        // Given
        String encodedPassword = "$2a$10$encodedPassword";

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setEmail("test@example.com");
        savedUser.setPassword(encodedPassword);

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        userService.register(testUser);

        // Then
        verify(userRepository, times(1)).save(argThat(user ->
            user.getEmail().equals("test@example.com") &&
            user.getPassword().equals(encodedPassword)
        ));
    }

    @Test
    public void testRegisterUser_DifferentEmails() {
        // Given
        String encodedPassword = "$2a$10$encodedPassword";

        testUser.setEmail("unique@example.com");

        User savedUser = new User();
        savedUser.setId(10);
        savedUser.setEmail("unique@example.com");
        savedUser.setPassword(encodedPassword);

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.register(testUser);

        // Then
        assertNotNull(result);
        assertEquals("unique@example.com", result.getEmail());
        assertEquals(10, result.getId());
    }
}
