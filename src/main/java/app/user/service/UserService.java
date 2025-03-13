package app.user.service;

import app.exeptions.DomainException;
import app.exeptions.LoginFailedException;
import app.exeptions.PasswordValidationException;
import app.exeptions.UsernameAlreadyExistException;
import app.subscription.model.Subscription;
import app.subscription.service.SubscriptionService;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.wallet.service.WalletService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionService subscriptionService;
    private final WalletService walletService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       SubscriptionService subscriptionService,
                       WalletService walletService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.subscriptionService = subscriptionService;
        this.walletService = walletService;
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        Optional<User> optionUser = userRepository.findByUsername(registerRequest.getUsername());
        if (optionUser.isPresent()) {
            throw new UsernameAlreadyExistException("Username [%s] already exists.".formatted(registerRequest.getUsername()));
        }
//        Optional.of(registerRequest.getPassword())
//                .filter(password -> password.length() >= 6)
//                .orElseThrow(() -> new PasswordValidationException("Password must be at least 6 characters long."));
        if (registerRequest.getPassword().length() < 6) {
            throw new PasswordValidationException("Password must be at least 6 characters long.");
        }
        User user = userRepository.save(initializeUser(registerRequest));
        Subscription defaultSubscription = subscriptionService.createDefaultSubscription(user);
        user.setSubscriptions(List.of(defaultSubscription));
        walletService.createNewWallet(user);
    }

    public void editUserDetails(UUID userId, UserEditRequest userEditRequest) {

        User user = getById(userId);

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setEmail(userEditRequest.getEmail());
        user.setProfilePicture(userEditRequest.getProfilePicture());

        userRepository.save(user);
    }

    private User initializeUser(RegisterRequest registerRequest) {

        return User.builder().username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .country(registerRequest.getCountry())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    public User login(LoginRequest loginRequest) {
        Optional<User> optionUser = userRepository.findByUsername(loginRequest.getUsername());
        if (optionUser.isEmpty()) {
            throw new LoginFailedException("Username or password are incorrect.");
        }
        User user = optionUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new LoginFailedException("Username or password are incorrect.");
        }
        return user;
    }

    @Transactional
    public User getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(id)));
        user.getCars().size();

        return user;
    }
}
