package com.EchoBox.service;

import com.EchoBox.model.User;
import com.EchoBox.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    private User testUser;
    private User adminUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("user@example.com");
        testUser.setPassword("$2a$10$encodedPassword");
        testUser.setIsAdmin(false);

        adminUser = new User();
        adminUser.setId(2);
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("$2a$10$encodedAdminPassword");
        adminUser.setIsAdmin(true);
    }

    @Test
    public void testLoadUserByUsername_RegularUser_Success() {
        // Given
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("user@example.com");

        // Then
        assertNotNull(userDetails);
        assertEquals("user@example.com", userDetails.getUsername());
        assertEquals("$2a$10$encodedPassword", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertFalse(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));

        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    public void testLoadUserByUsername_AdminUser_Success() {
        // Given
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminUser));

        // When
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("admin@example.com");

        // Then
        assertNotNull(userDetails);
        assertEquals("admin@example.com", userDetails.getUsername());
        assertEquals("$2a$10$encodedAdminPassword", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertFalse(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));

        verify(userRepository, times(1)).findByEmail("admin@example.com");
    }

    @Test
    public void testLoadUserByUsername_UserNotFound_ThrowsException() {
        // Given
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When/Then
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> myUserDetailsService.loadUserByUsername(email)
        );

        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testLoadUserByUsername_VerifyAuthoritiesForUser() {
        // Given
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("user@example.com");

        // Then
        boolean hasUserRole = userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
        boolean hasAdminRole = userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        assertTrue(hasUserRole);
        assertFalse(hasAdminRole);
    }

    @Test
    public void testLoadUserByUsername_VerifyAuthoritiesForAdmin() {
        // Given
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminUser));

        // When
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("admin@example.com");

        // Then
        boolean hasUserRole = userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
        boolean hasAdminRole = userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        assertFalse(hasUserRole);
        assertTrue(hasAdminRole);
    }

    @Test
    public void testLoadUserByUsername_DifferentEmails() {
        // Given
        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword("$2a$10$anotherPassword");
        anotherUser.setIsAdmin(false);

        when(userRepository.findByEmail("another@example.com")).thenReturn(Optional.of(anotherUser));

        // When
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("another@example.com");

        // Then
        assertNotNull(userDetails);
        assertEquals("another@example.com", userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_NullEmail_ThrowsException() {
        // Given
        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(
            UsernameNotFoundException.class,
            () -> myUserDetailsService.loadUserByUsername(null)
        );
    }

    @Test
    public void testLoadUserByUsername_EmptyEmail_ThrowsException() {
        // Given
        String emptyEmail = "";
        when(userRepository.findByEmail(emptyEmail)).thenReturn(Optional.empty());

        // When/Then
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> myUserDetailsService.loadUserByUsername(emptyEmail)
        );

        assertTrue(exception.getMessage().contains("User not found with email:"));
    }

    @Test
    public void testLoadUserByUsername_VerifyUserDetailsAttributes() {
        // Given
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));

        // When
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("user@example.com");

        // Then
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
    }
}

