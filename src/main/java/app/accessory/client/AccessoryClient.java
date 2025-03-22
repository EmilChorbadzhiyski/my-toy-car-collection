package app.accessory.client;

import app.web.dto.AccessoryCreateRequest;
import app.web.dto.AccessoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "toy-car-accessory", url = "http://localhost:8081/api/accessories")
public interface AccessoryClient {

    @GetMapping
    List<AccessoryResponse> getAllAccessories();

    @PostMapping("/add")
    void createAccessory(@RequestBody AccessoryCreateRequest accessoryRequest);

    @GetMapping("/{id}")
    ResponseEntity<AccessoryResponse> getAccessoryById(@PathVariable UUID id);

    @DeleteMapping("/{id}")
    void deleteAccessory(@PathVariable UUID id);
}
