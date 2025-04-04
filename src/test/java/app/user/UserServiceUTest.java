package app.user;

import app.exeptions.DomainException;
import app.exeptions.PasswordValidationException;
import app.exeptions.UsernameAlreadyExistException;
import app.subscription.service.SubscriptionService;
import app.user.model.Country;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.wallet.service.WalletService;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SubscriptionService subscriptionService;
    @Mock
    private WalletService walletService;

    @InjectMocks
    private UserService userService;

    @Test
    void whenUserDoesNotExist_thenExceptionIsThrown() {

        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> userService.getById(userId));
        assertEquals("User with id [" + userId + "] does not exist.", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldReturnUser_whenUserExists() {
        UUID userId = UUID.randomUUID();
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setUsername("testUser");
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.getById(userId);
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void givenValidRegisterRequest_returnsInitializedUser() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("testUser")
                .password("password123")
                .country(Country.BULGARIA)
                .build();
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(encodedPassword);

        User user = userService.initializeUser(registerRequest);

        assertEquals(registerRequest.getUsername(), user.getUsername());
        assertEquals(encodedPassword, user.getPassword());
        assertEquals(UserRole.USER, user.getRole());
        assertEquals(registerRequest.getCountry(), user.getCountry());
        assertNotNull(user.getCreatedOn());
        assertNotNull(user.getUpdatedOn());
        assertTrue(user.getCreatedOn().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(user.getUpdatedOn().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertEquals(user.getCreatedOn(), user.getUpdatedOn());
    }

    @Test
    void givenValidInput_returnUserUpdated() {
        UUID userId = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("Old");
        existingUser.setLastName("Name");
        existingUser.setEmail("old@.com");
        existingUser.setProfilePicture("old_picture.jpg");

        UserEditRequest editRequest = UserEditRequest.builder()
                .firstName("New")
                .lastName("Name")
                .email("new@.com")
                .profilePicture("new_picture.png")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        userService.editUserDetails(userId, editRequest);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);

        assertEquals("New", existingUser.getFirstName());
        assertEquals("Name", existingUser.getLastName());
        assertEquals("new@.com", existingUser.getEmail());
        assertEquals("new_picture.png", existingUser.getProfilePicture());
    }

    @Test
    void givenExistingUsername_whenRegister_thenExceptionIsThrown() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("existingUser")
                .password("password123")
                .country(Country.BULGARIA)
                .build();
        User existingUser = new User();
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(existingUser));

        assertThrows(UsernameAlreadyExistException.class, () -> userService.register(registerRequest));
        verify(userRepository, times(1)).findByUsername(registerRequest.getUsername());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(subscriptionService, never()).createDefaultSubscription(any(User.class));
        verify(walletService, never()).createNewWallet(any(User.class));
    }

    @Test
    void passwordTooShort_whenRegister_thenExceptionIsThrown() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("User")
                .password("123")
                .country(Country.CANADA)
                .build();
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());

        assertThrows(PasswordValidationException.class, () -> userService.register(registerRequest));
        verify(userRepository, times(1)).findByUsername(registerRequest.getUsername());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(subscriptionService, never()).createDefaultSubscription(any(User.class));
        verify(walletService, never()).createNewWallet(any(User.class));
    }

    @Test
    void givenValidUserIdAndRole_whenChangeUserRole() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setRole(UserRole.USER);
        user.setUpdatedOn(LocalDateTime.now().minusDays(1));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserRole newRole = UserRole.ADMIN;
        userService.changeUserRole(userId, newRole);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        assertEquals(newRole, user.getRole());
        assertTrue(user.getUpdatedOn().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void changeUserRoleGivenWhenNonExistentId_thenExceptionIsThrown() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> userService.changeUserRole(userId, UserRole.ADMIN));
        assertEquals("User with id [" + userId + "] does not exist.", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }
}
