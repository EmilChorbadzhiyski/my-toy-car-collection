package app.car;

import app.car.model.Car;
import app.car.model.CarOrigin;
import app.car.repository.CarRepository;
import app.car.service.CarService;
import app.exeptions.DomainException;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.wallet.model.Wallet;
import app.wallet.service.WalletService;
import app.web.dto.CreateCarRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceUTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private CarService carService;

    @Test
    void shouldCreateNewCar() {
        User user = mock(User.class);
        CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .type("Sedan")
                .brand("Toyota")
                .model("Camry")
                .year("2020")
                .numberOfDoors(4)
                .countryOfOrigin(CarOrigin.JAPAN)
                .color("Red")
                .pictureUrl("example.com/image.jpg")
                .price(new BigDecimal("25000.00"))
                .build();

        carService.createNewCar(createCarRequest, user);

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);
        verify(carRepository, times(1)).save(carCaptor.capture());

        Car capturedCar = carCaptor.getValue();

        assertNotNull(capturedCar);
        assertEquals(createCarRequest.getType(), capturedCar.getType());
        assertEquals(createCarRequest.getBrand(), capturedCar.getBrand());
        assertEquals(createCarRequest.getModel(), capturedCar.getModel());
        assertEquals(createCarRequest.getYear(), capturedCar.getYear());
        assertEquals(createCarRequest.getNumberOfDoors(), capturedCar.getNumberOfDoors());
        assertEquals(createCarRequest.getCountryOfOrigin(), capturedCar.getCountryOfOrigin());
        assertEquals(createCarRequest.getColor(), capturedCar.getColor());
        assertEquals(createCarRequest.getPictureUrl(), capturedCar.getImageUrl());
        assertEquals(createCarRequest.getPrice(), capturedCar.getPrice());
    }

    @Test
    void shouldReturnAllCars() {
        List<Car> cars = List.of(mock(Car.class), mock(Car.class));
        when(carRepository.findAll()).thenReturn(cars);
        List<Car> result = carService.getAllCars();
        assertEquals(cars, result);
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void shouldBuyCarSuccessfully() {
        UUID carId = UUID.randomUUID();
        User buyer = mock(User.class);
        User seller = mock(User.class);
        Wallet buyerWallet = mock(Wallet.class);
        Wallet sellerWallet = mock(Wallet.class);

        Car car = mock(Car.class);
        when(car.getOwner()).thenReturn(seller);
        when(car.getPrice()).thenReturn(new BigDecimal("20000.00"));

        when(buyer.getWallet()).thenReturn(buyerWallet);
        when(seller.getWallet()).thenReturn(sellerWallet);

        when(buyerWallet.getBalance()).thenReturn(new BigDecimal("30000.00"));
        when(carRepository.findById(carId)).thenReturn(java.util.Optional.of(car));
        carService.buyCar(carId, buyer);
        verify(walletService, times(1)).decreaseBalance(buyerWallet, new BigDecimal("20000.00"));
        verify(walletService, times(1)).increaseBalance(sellerWallet, new BigDecimal("20000.00"));
        verify(car, times(1)).setOwner(buyer);
        verify(carRepository, times(1)).save(car);
        verify(transactionService, times(1)).createNewTransaction(any(), eq(buyer));
    }

    @Test
    void WhenCarNotFound_ShouldThrowException() {
        UUID carId = UUID.randomUUID();
        User buyer = mock(User.class);
        when(carRepository.findById(carId)).thenReturn(java.util.Optional.empty());
        DomainException exception = assertThrows(DomainException.class, () -> carService.buyCar(carId, buyer));
        assertEquals("Car with ID [%s] not found.".formatted(carId), exception.getMessage());
    }

    @Test
    void WhenBuyingOwnCar_ShouldThrowException() {
        UUID carId = UUID.randomUUID();
        User buyer = mock(User.class);
        Car car = mock(Car.class);
        when(car.getOwner()).thenReturn(buyer);
        when(carRepository.findById(carId)).thenReturn(java.util.Optional.of(car));
        DomainException exception = assertThrows(DomainException.class, () -> carService.buyCar(carId, buyer));
        assertEquals("You cannot buy your own car.", exception.getMessage());
    }

    @Test
    void WhenBalanceIsInsufficient_shouldThrowException() {
        UUID carId = UUID.randomUUID();
        User buyer = mock(User.class);
        User seller = mock(User.class);
        Wallet buyerWallet = mock(Wallet.class);
        Wallet sellerWallet = mock(Wallet.class);
        Car car = mock(Car.class);
        when(car.getOwner()).thenReturn(seller);
        when(car.getPrice()).thenReturn(new BigDecimal("50000.00"));
        when(buyer.getWallet()).thenReturn(buyerWallet);
        when(seller.getWallet()).thenReturn(sellerWallet);
        when(buyerWallet.getBalance()).thenReturn(new BigDecimal("20000.00"));
        when(carRepository.findById(carId)).thenReturn(java.util.Optional.of(car));
        DomainException exception = assertThrows(DomainException.class, () -> carService.buyCar(carId, buyer));
        assertEquals("Insufficient balance to purchase this car.", exception.getMessage());
    }

    @Test
    void shouldDeleteCarById() {
        UUID carId = UUID.randomUUID();
        carService.deleteById(carId);
        verify(carRepository, times(1)).deleteById(carId);
    }

    @Test
    void shouldReturnAllCarsForUser() {
        User user = mock(User.class);
        List<Car> cars = List.of(mock(Car.class), mock(Car.class));
        when(carRepository.findByOwner(user)).thenReturn(cars);
        List<Car> result = carService.getAllCarsForUser(user);
        assertEquals(cars, result);
        verify(carRepository, times(1)).findByOwner(user);
    }
}
