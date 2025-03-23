package app.web.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccessoryCreateRequestTest {

    @Test
    void buildAccessoryCreateRequest_shouldSetAllFieldsCorrectly() {
        UUID userId = UUID.randomUUID();
        String accessoryName = "Spoiler";
        String description = "Rear Spoiler for Toy Car";
        BigDecimal price = new BigDecimal("12.50");
        String imageUrl = "spoiler.jpg";

        AccessoryCreateRequest request = AccessoryCreateRequest.builder()
                .userId(userId)
                .accessoryName(accessoryName)
                .description(description)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        assertEquals(userId, request.getUserId());
        assertEquals(accessoryName, request.getAccessoryName());
        assertEquals(description, request.getDescription());
        assertEquals(price, request.getPrice());
        assertEquals(imageUrl, request.getImageUrl());
    }

    @Test
    void createAccessoryCreateRequest_withNoArgsConstructor_shouldHaveNullFields() {
        AccessoryCreateRequest request = new AccessoryCreateRequest();

        assertEquals(null, request.getUserId());
        assertEquals(null, request.getAccessoryName());
        assertEquals(null, request.getDescription());
        assertEquals(null, request.getPrice());
        assertEquals(null, request.getImageUrl());
    }

    @Test
    void createAccessoryCreateRequest_withAllArgsConstructor_shouldSetAllFields() {
        UUID userId = UUID.randomUUID();
        String accessoryName = "Racing Wheels";
        String description = "Set of Racing Wheels for Toy Car";
        BigDecimal price = new BigDecimal("8.99");
        String imageUrl = "wheels.png";

        AccessoryCreateRequest request = new AccessoryCreateRequest(userId, accessoryName, description, price, imageUrl);

        assertEquals(userId, request.getUserId());
        assertEquals(accessoryName, request.getAccessoryName());
        assertEquals(description, request.getDescription());
        assertEquals(price, request.getPrice());
        assertEquals(imageUrl, request.getImageUrl());
    }

    @Test
    void setFields_usingSetterMethods_shouldSetFieldsCorrectly() {
        AccessoryCreateRequest request = new AccessoryCreateRequest();

        UUID userId = UUID.randomUUID();
        String accessoryName = "Light Bar";
        String description = "LED Light Bar for Toy Car";
        BigDecimal price = new BigDecimal("15.75");
        String imageUrl = "lightbar.gif";

        request.setUserId(userId);
        request.setAccessoryName(accessoryName);
        request.setDescription(description);
        request.setPrice(price);
        request.setImageUrl(imageUrl);

        assertEquals(userId, request.getUserId());
        assertEquals(accessoryName, request.getAccessoryName());
        assertEquals(description, request.getDescription());
        assertEquals(price, request.getPrice());
        assertEquals(imageUrl, request.getImageUrl());
    }

    @Test
    void getFields_usingGetterMethods_shouldRetrieveFieldsCorrectly() {
        UUID userId = UUID.randomUUID();
        String accessoryName = "Engine Upgrade Kit";
        String description = "Performance Engine Upgrade Kit for Toy Car";
        BigDecimal price = new BigDecimal("25.00");
        String imageUrl = "engine.jpeg";

        AccessoryCreateRequest request = AccessoryCreateRequest.builder()
                .userId(userId)
                .accessoryName(accessoryName)
                .description(description)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        assertEquals(userId, request.getUserId());
        assertEquals(accessoryName, request.getAccessoryName());
        assertEquals(description, request.getDescription());
        assertEquals(price, request.getPrice());
        assertEquals(imageUrl, request.getImageUrl());
    }

    @Test
    void builder_shouldCreateAccessoryCreateRequest_withAllFieldsSet() {
        UUID userId = UUID.randomUUID();
        String accessoryName = "Test Accessory";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("100.00");
        String imageUrl = "test.jpg";

        AccessoryCreateRequest request = AccessoryCreateRequest.builder()
                .userId(userId)
                .accessoryName(accessoryName)
                .description(description)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        assertEquals(userId, request.getUserId());
        assertEquals(accessoryName, request.getAccessoryName());
        assertEquals(description, request.getDescription());
        assertEquals(price, request.getPrice());
        assertEquals(imageUrl, request.getImageUrl());
    }

    @Test
    void builder_shouldCreateAccessoryCreateRequest_withPartialFieldsSet() {
        UUID userId = UUID.randomUUID();
        String accessoryName = "Partial Accessory";

        AccessoryCreateRequest request = AccessoryCreateRequest.builder()
                .userId(userId)
                .accessoryName(accessoryName)
                .build();

        assertEquals(userId, request.getUserId());
        assertEquals(accessoryName, request.getAccessoryName());
        assertEquals(null, request.getDescription());
        assertEquals(null, request.getPrice());
        assertEquals(null, request.getImageUrl());
    }

    @Test
    void builder_shouldCreateAccessoryCreateRequest_withNoFieldsSet() {
        AccessoryCreateRequest request = AccessoryCreateRequest.builder().build();

        assertEquals(null, request.getUserId());
        assertEquals(null, request.getAccessoryName());
        assertEquals(null, request.getDescription());
        assertEquals(null, request.getPrice());
        assertEquals(null, request.getImageUrl());
    }
}