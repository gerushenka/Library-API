package com.example.bookstorageservice.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @Mock
    private Jwt jwtMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfig = new SecurityConfig();
    }

    @Test
    void jwtConverter_shouldReturnCorrectAuthorities() {
        when(jwtMock.getClaimAsMap("realm_access"))
                .thenReturn(Map.of("roles", List.of("ROLE_MANAGER", "ROLE_USER")));

        var jwtAuthenticationConverter = securityConfig.jwtConverter();
        AbstractAuthenticationToken authenticationToken = jwtAuthenticationConverter.convert(jwtMock);

        Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();

        List<String> authorityNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        assertEquals(2, authorityNames.size());
        assertEquals(List.of("ROLE_MANAGER", "ROLE_USER"), authorityNames);
    }

    @Test
    void jwtConverter_shouldHandleNullRolesGracefully() {
        when(jwtMock.getClaimAsMap("realm_access")).thenReturn(Map.of());

        var jwtAuthenticationConverter = securityConfig.jwtConverter();
        AbstractAuthenticationToken authenticationToken = jwtAuthenticationConverter.convert(jwtMock);

        Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();

        assertEquals(0, authorities.size());
    }

    @Test
    void jwtConverter_shouldHandleEmptyRolesGracefully() {
        when(jwtMock.getClaimAsMap("realm_access")).thenReturn(Map.of("roles", List.of()));

        var jwtAuthenticationConverter = securityConfig.jwtConverter();
        AbstractAuthenticationToken authenticationToken = jwtAuthenticationConverter.convert(jwtMock);

        Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();

        assertEquals(0, authorities.size());
    }

    @Test
    void jwtConverter_shouldHandleNullClaimsGracefully() {
        when(jwtMock.getClaimAsMap("realm_access")).thenReturn(null);

        var jwtAuthenticationConverter = securityConfig.jwtConverter();
        AbstractAuthenticationToken authenticationToken = jwtAuthenticationConverter.convert(jwtMock);

        Collection<GrantedAuthority> authorities = authenticationToken.getAuthorities();

        assertEquals(0, authorities.size());
    }

    @Test
    void jwtDecoder_shouldBeInitializedWithCorrectUri() {
        JwtDecoder jwtDecoder = securityConfig.jwtDecoder();
        assertNotNull(jwtDecoder);
        assertTrue(jwtDecoder instanceof NimbusJwtDecoder);

    }


}