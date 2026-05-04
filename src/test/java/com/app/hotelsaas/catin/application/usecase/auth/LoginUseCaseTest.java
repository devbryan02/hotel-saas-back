package com.app.hotelsaas.catin.application.usecase.auth;

import com.app.hotelsaas.catin.domain.enums.RoleEnum;
import com.app.hotelsaas.catin.domain.model.AppUser;
import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.domain.port.AppUserRepository;
import com.app.hotelsaas.catin.infrastructure.security.JwtService;
import com.app.hotelsaas.catin.web.rest.auth.request.LoginRequest;
import com.app.hotelsaas.catin.web.rest.auth.response.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Caso de uso: Login")
class LoginUseCaseTest {

    @Mock AppUserRepository appUserRepository;
    @Mock AuthenticationManager authenticationManager;
    @Mock JwtService jwtService;

    @InjectMocks LoginUseCase loginUseCase;

    // --- Datos compartidos ---
    LoginRequest loginRequest;
    AppUser appUserExistente;
    String token;
    UUID tenantId;

    @BeforeEach
    void setUp() {
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.fake.token";
        tenantId = UUID.randomUUID();

        loginRequest = new LoginRequest("testhotel@gmail.com", "test123");

        Tenant tenant = new Tenant(
                tenantId,
                "Juan",
                "Hotel A",
                "FREE",
                "ACTIVE"
        );

        appUserExistente = new AppUser(
                UUID.randomUUID(),
                tenant,
                "testhotel@gmail.com",
                "passwordhash",
                RoleEnum.ADMIN,
                "ACTIVE",
                LocalDateTime.now()
        );
    }

    @Nested
    @DisplayName("Cuando el login es exitoso")
    class LoginExitoso {

        @Test
        @DisplayName("Debe retornar un AuthResponse con token, email, rol y tenantId correctos")
        void deberiaRetornarAuthResponseConDatosCorrectos() {
            // Arrange
            when(appUserRepository.findByEmail(loginRequest.email()))
                    .thenReturn(Optional.of(appUserExistente));
            when(jwtService.generateToken(
                    appUserExistente.getId(),
                    appUserExistente.getEmail(),
                    appUserExistente.getTenant().getId(),
                    appUserExistente.getRole().name()
            )).thenReturn(token);

            // Act
            AuthResponse result = loginUseCase.execute(loginRequest);

            // Assert - verificamos campo por campo para mensajes de error claros
            assertThat(result.token()).isEqualTo(token);
            assertThat(result.email()).isEqualTo(appUserExistente.getEmail());
            assertThat(result.role()).isEqualTo(RoleEnum.ADMIN.name());
            assertThat(result.tenantId()).isEqualTo(tenantId.toString());

            // Verificamos que los colaboradores fueron invocados correctamente
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(appUserRepository).findByEmail(loginRequest.email());
            verify(jwtService).generateToken(
                    appUserExistente.getId(),
                    appUserExistente.getEmail(),
                    tenantId,
                    RoleEnum.ADMIN.name()
            );
        }
    }

    @Nested
    @DisplayName("Cuando las credenciales son incorrectas")
    class CredencialesIncorrectas {

        @Test
        @DisplayName("Debe lanzar BadCredentialsException y no consultar la base de datos")
        void deberiaLanzarBadCredentialsException() {
            // Arrange - simulamos que el AuthenticationManager rechaza las credenciales
            doThrow(new BadCredentialsException("Bad credentials"))
                    .when(authenticationManager)
                    .authenticate(any(UsernamePasswordAuthenticationToken.class));

            // Act & Assert
            assertThatThrownBy(() -> loginUseCase.execute(loginRequest))
                    .isInstanceOf(BadCredentialsException.class);

            // Si la auth falla, NUNCA debemos tocar la BD ni generar token
            verifyNoInteractions(appUserRepository);
            verifyNoInteractions(jwtService);
        }
    }

    @Nested
    @DisplayName("Cuando el usuario no existe en base de datos")
    class UsuarioNoEncontrado {

        @Test
        @DisplayName("Debe lanzar UsernameNotFoundException y no generar token")
        void deberiaLanzarUsernameNotFoundException() {
            // Arrange - auth pasa OK, pero el usuario no está en BD
            when(appUserRepository.findByEmail(loginRequest.email()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> loginUseCase.execute(loginRequest))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage("User not found");

            // Si el user no existe, NUNCA debemos generar un token
            verifyNoInteractions(jwtService);
        }
    }
}