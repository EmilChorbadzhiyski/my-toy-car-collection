package app.user.model;

import app.post.model.Post;
import app.car.model.Car;
import app.subscription.model.Subscription;
import app.transaction.model.Transaction;
import app.wallet.model.Wallet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String profilePicture;

    @Column(nullable = false, unique = true)
    private String username;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country country;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    @OrderBy("createdOn DESC")
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToOne(mappedBy = "owner")
    private Wallet wallet;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Car> cars = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER)
    private List<Post> posts ;

}
