    package app.car.repository;

    import app.car.model.Car;
    import app.user.model.User;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.util.List;
    import java.util.UUID;

    public interface CarRepository extends JpaRepository<Car, UUID> {
        List<Car> findByOwner(User user);
    }
