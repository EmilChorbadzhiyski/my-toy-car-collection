package app.car.service;

import app.car.model.Car;
import app.car.repository.CarRepository;
import app.exeptions.DomainException;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.wallet.model.Wallet;
import app.wallet.service.WalletService;
import app.web.dto.CreateCarRequest;
import app.web.dto.CreateTransactionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CarService {

    private final CarRepository carRepository;
    private final WalletService walletService;
    private final TransactionService transactionService;

    public CarService(CarRepository carRepository, WalletService walletService, TransactionService transactionService) {
        this.carRepository = carRepository;
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    public void createNewCar(CreateCarRequest createCarRequest, User user) {

        Car car = Car.builder()
                .owner(user)
                .type(createCarRequest.getType())
                .brand(createCarRequest.getBrand())
                .model(createCarRequest.getModel())
                .year(createCarRequest.getYear())
                .numberOfDoors(createCarRequest.getNumberOfDoors())
                .countryOfOrigin(createCarRequest.getCountryOfOrigin())
                .color(createCarRequest.getColor())
                .imageUrl(createCarRequest.getPictureUrl())
                .price(createCarRequest.getPrice())
                .build();

        carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public void buyCar(UUID carId, User buyer) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new DomainException("Car with ID [%s] not found.".formatted(carId)));

        if (car.getOwner().equals(buyer)) {
            throw new DomainException("You cannot buy your own car.");
        }

        BigDecimal carPrice = car.getPrice();
        Wallet buyerWallet = buyer.getWallet();
        Wallet sellerWallet = car.getOwner().getWallet();

        if (buyerWallet.getBalance().compareTo(carPrice) < 0) {
            throw new DomainException("Insufficient balance to purchase this car.");
        }

        walletService.decreaseBalance(buyerWallet, carPrice);
        walletService.increaseBalance(sellerWallet, carPrice);
        car.setOwner(buyer);
        carRepository.save(car);

        transactionService.createNewTransaction(
                new CreateTransactionRequest(
                        car.getOwner().getId(),
                        car.getOwner().getUsername(),
                        buyer.getUsername(),
                        car.getPrice(),
                        LocalDateTime.now()
                ), buyer
        );

    }

    public void deleteById(UUID id) {
        carRepository.deleteById(id);
    }

    public List<Car> getAllCarsForUser(User user) {
        return carRepository.findByOwner(user);
    }
}
