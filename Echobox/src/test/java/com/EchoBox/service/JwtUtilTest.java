package com.EchoBox.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;
    private UserDetails adminDetails;
    private final String testSecret = "mySecretKeyForTestingJwtTokenGenerationAndValidation12345";

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);

        userDetails = new User(
            "user@example.com",
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        adminDetails = new User(
            "admin@example.com",
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    @Test
    public void testGenerateToken_RegularUser() {
        // When
        String token = jwtUtil.generateToken(userDetails, false);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(3, token.split("\\.").length); // JWT has 3 parts separated by dots
    }

    @Test
    public void testGenerateToken_AdminUser() {
        // When
        String token = jwtUtil.generateToken(adminDetails, true);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testExtractUsername() {
        // Given
        String token = jwtUtil.generateToken(userDetails, false);

        // When
        String username = jwtUtil.extractUsername(token);

        // Then
        assertEquals("user@example.com", username);
    }

    @Test
    public void testExtractExpiration() {
        // Given
        String token = jwtUtil.generateToken(userDetails, false);

        // When
        Date expiration = jwtUtil.extractExpiration(token);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date())); // Token should not be expired yet
    }

    @Test
    public void testValidateToken_ValidToken() {
        // Given
        String token = jwtUtil.generateToken(userDetails, false);

        // When
        Boolean isValid = jwtUtil.validateToken(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    public void testValidateToken_WrongUser() {
        // Given
        String token = jwtUtil.generateToken(userDetails, false);
        UserDetails differentUser = new User(
            "different@example.com",
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // When
        Boolean isValid = jwtUtil.validateToken(token, differentUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    public void testIsAdmin_RegularUser() {
        // Given
        String token = jwtUtil.generateToken(userDetails, false);

        // When
        Boolean isAdmin = jwtUtil.isAdmin(token);

        // Then
        assertFalse(isAdmin);
    }

    @Test
    public void testIsAdmin_AdminUser() {
        // Given
        String token = jwtUtil.generateToken(adminDetails, true);

        // When
        Boolean isAdmin = jwtUtil.isAdmin(token);

        // Then
        assertTrue(isAdmin);
    }

    @Test
    public void testExtractClaim_Subject() {
        // Given
        String token = jwtUtil.generateToken(userDetails, false);

        // When
        String subject = jwtUtil.extractClaim(token, Claims::getSubject);

        // Then
        assertEquals("user@example.com", subject);
    }

    @Test
    public void testExtractClaim_IssuedAt() {
        // Given
        String token = jwtUtil.generateToken(userDetails, false);

        // When
        Date issuedAt = jwtUtil.extractClaim(token, Claims::getIssuedAt);

        // Then
        assertNotNull(issuedAt);
        assertTrue(issuedAt.before(new Date()) || issuedAt.equals(new Date()));
    }

    @Test
    public void testTokenExpiration_NotExpired() {
        // Given
        String token = jwtUtil.generateToken(userDetails, false);

        // When
        Date expiration = jwtUtil.extractExpiration(token);

        // Then
        assertTrue(expiration.after(new Date()));
    }

    @Test
    public void testGenerateToken_DifferentUsers() {
        // Given
        UserDetails user1 = new User("user1@example.com", "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        UserDetails user2 = new User("user2@example.com", "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // When
        String token1 = jwtUtil.generateToken(user1, false);
        String token2 = jwtUtil.generateToken(user2, false);

        // Then
        assertNotEquals(token1, token2);
        assertEquals("user1@example.com", jwtUtil.extractUsername(token1));
        assertEquals("user2@example.com", jwtUtil.extractUsername(token2));
    }

    @Test
    public void testValidateToken_SameUserDifferentTokens() {
        // Given
        String token1 = jwtUtil.generateToken(userDetails, false);
        String token2 = jwtUtil.generateToken(userDetails, false);

        // When/Then
        assertTrue(jwtUtil.validateToken(token1, userDetails));
        assertTrue(jwtUtil.validateToken(token2, userDetails));
    }

    @Test
    public void testIsAdmin_VerifyClaimPresent() {
        // Given
        String userToken = jwtUtil.generateToken(userDetails, false);
        String adminToken = jwtUtil.generateToken(adminDetails, true);

        // When
        Boolean userIsAdmin = jwtUtil.isAdmin(userToken);
        Boolean adminIsAdmin = jwtUtil.isAdmin(adminToken);

        // Then
        assertFalse(userIsAdmin);
        assertTrue(adminIsAdmin);
    }

    @Test
    public void testGenerateToken_TokenStructure() {
        // When
        String token = jwtUtil.generateToken(userDetails, false);

        // Then
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length); // Header, Payload, Signature
        assertFalse(parts[0].isEmpty());
        assertFalse(parts[1].isEmpty());
        assertFalse(parts[2].isEmpty());
    }

    @Test
    public void testValidateToken_CorrectUsernameExtraction() {
        // Given
        String token = jwtUtil.generateToken(userDetails, false);

        // When
        String extractedUsername = jwtUtil.extractUsername(token);
        Boolean isValid = jwtUtil.validateToken(token, userDetails);

        // Then
        assertEquals(userDetails.getUsername(), extractedUsername);
        assertTrue(isValid);
    }

    @Test
    public void testAdminClaim_ConsistentAcrossExtractions() {
        // Given
        String adminToken = jwtUtil.generateToken(adminDetails, true);

        // When
        Boolean isAdmin1 = jwtUtil.isAdmin(adminToken);
        Boolean isAdmin2 = jwtUtil.isAdmin(adminToken);

        // Then
        assertTrue(isAdmin1);
        assertTrue(isAdmin2);
        assertEquals(isAdmin1, isAdmin2);
    }
}

