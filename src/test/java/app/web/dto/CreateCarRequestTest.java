package app.web.dto;

import app.car.model.CarOrigin;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateCarRequestTest {

    @Test
    void buildCreateCarRequest_shouldSetAllFieldsCorrectly() {
        UUID ownerId = UUID.randomUUID();
        String type = "Sedan";
        String brand = "Toyota";
        String model = "Camry";
        String year = "2023";
        int numberOfDoors = 4;
        CarOrigin countryOfOrigin = CarOrigin.JAPAN;
        String color = "Silver";
        String pictureUrl = "camry.jpg";
        BigDecimal price = new BigDecimal("25000.00");

        CreateCarRequest request = CreateCarRequest.builder()
                .ownerId(ownerId)
                .type(type)
                .brand(brand)
                .model(model)
                .year(year)
                .numberOfDoors(numberOfDoors)
                .countryOfOrigin(countryOfOrigin)
                .color(color)
                .pictureUrl(pictureUrl)
                .price(price)
                .build();

        assertEquals(ownerId, request.getOwnerId());
        assertEquals(type, request.getType());
        assertEquals(brand, request.getBrand());
        assertEquals(model, request.getModel());
        assertEquals(year, request.getYear());
        assertEquals(numberOfDoors, request.getNumberOfDoors());
        assertEquals(countryOfOrigin, request.getCountryOfOrigin());
        assertEquals(color, request.getColor());
        assertEquals(pictureUrl, request.getPictureUrl());
        assertEquals(price, request.getPrice());
    }

    @Test
    void createCreateCarRequest_withNoArgsConstructor_shouldHaveNullFields() {
        CreateCarRequest request = new CreateCarRequest();

        assertEquals(null, request.getOwnerId());
        assertEquals(null, request.getType());
        assertEquals(null, request.getBrand());
        assertEquals(null, request.getModel());
        assertEquals(null, request.getYear());
        assertEquals(0, request.getNumberOfDoors());
        assertEquals(null, request.getCountryOfOrigin());
        assertEquals(null, request.getColor());
        assertEquals(null, request.getPictureUrl());
        assertEquals(null, request.getPrice());
    }

    @Test
    void createCreateCarRequest_withAllArgsConstructor_shouldSetAllFields() {
        UUID ownerId = UUID.randomUUID();
        String type = "SUV";
        String brand = "Honda";
        String model = "CRV";
        String year = "2022";
        int numberOfDoors = 5;
        CarOrigin countryOfOrigin = CarOrigin.USA;
        String color = "Blue";
        String pictureUrl = "crv.png";
        BigDecimal price = new BigDecimal("30000.00");

        CreateCarRequest request = new CreateCarRequest(ownerId, type, brand, model, year, numberOfDoors, countryOfOrigin, color, pictureUrl, price);

        assertEquals(ownerId, request.getOwnerId());
        assertEquals(type, request.getType());
        assertEquals(brand, request.getBrand());
        assertEquals(model, request.getModel());
        assertEquals(year, request.getYear());
        assertEquals(numberOfDoors, request.getNumberOfDoors());
        assertEquals(countryOfOrigin, request.getCountryOfOrigin());
        assertEquals(color, request.getColor());
        assertEquals(pictureUrl, request.getPictureUrl());
        assertEquals(price, request.getPrice());
    }

    @Test
    void setFields_usingSetterMethods_shouldSetFieldsCorrectly() {
        CreateCarRequest request = new CreateCarRequest();

        UUID ownerId = UUID.randomUUID();
        String type = "Hatchback";
        String brand = "Ford";
        String model = "Focus";
        String year = "2021";
        int numberOfDoors = 3;
        CarOrigin countryOfOrigin = CarOrigin.GERMANY;
        String color = "Red";
        String pictureUrl = "focus.jpeg";
        BigDecimal price = new BigDecimal("22000.00");

        request.setOwnerId(ownerId);
        request.setType(type);
        request.setBrand(brand);
        request.setModel(model);
        request.setYear(year);
        request.setNumberOfDoors(numberOfDoors);
        request.setCountryOfOrigin(countryOfOrigin);
        request.setColor(color);
        request.setPictureUrl(pictureUrl);
        request.setPrice(price);

        assertEquals(ownerId, request.getOwnerId());
        assertEquals(type, request.getType());
        assertEquals(brand, request.getBrand());
        assertEquals(model, request.getModel());
        assertEquals(year, request.getYear());
        assertEquals(numberOfDoors, request.getNumberOfDoors());
        assertEquals(countryOfOrigin, request.getCountryOfOrigin());
        assertEquals(color, request.getColor());
        assertEquals(pictureUrl, request.getPictureUrl());
        assertEquals(price, request.getPrice());
    }

    @Test
    void getFields_usingGetterMethods_shouldRetrieveFieldsCorrectly() {
        UUID ownerId = UUID.randomUUID();
        String type = "Truck";
        String brand = "Chevrolet";
        String model = "Silverado";
        String year = "2020";
        int numberOfDoors = 4;
        CarOrigin countryOfOrigin = CarOrigin.USA;
        String color = "Black";
        String pictureUrl = "silverado.gif";
        BigDecimal price = new BigDecimal("35000.00");

        CreateCarRequest request = CreateCarRequest.builder()
                .ownerId(ownerId)
                .type(type)
                .brand(brand)
                .model(model)
                .year(year)
                .numberOfDoors(numberOfDoors)
                .countryOfOrigin(countryOfOrigin)
                .color(color)
                .pictureUrl(pictureUrl)
                .price(price)
                .build();

        assertEquals(ownerId, request.getOwnerId());
        assertEquals(type, request.getType());
        assertEquals(brand, request.getBrand());
        assertEquals(model, request.getModel());
        assertEquals(year, request.getYear());
        assertEquals(numberOfDoors, request.getNumberOfDoors());
        assertEquals(countryOfOrigin, request.getCountryOfOrigin());
        assertEquals(color, request.getColor());
        assertEquals(pictureUrl, request.getPictureUrl());
        assertEquals(price, request.getPrice());
    }
}