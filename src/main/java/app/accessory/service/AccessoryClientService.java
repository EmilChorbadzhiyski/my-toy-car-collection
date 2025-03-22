package app.accessory.service;

import app.accessory.client.AccessoryClient;
import app.web.dto.AccessoryCreateRequest;
import app.web.dto.AccessoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AccessoryClientService {

    private final AccessoryClient accessoryClient;

    @Value("http://localhost:8081/api/accessories")
    private String accessoryServiceUrl;

    @Autowired
    public AccessoryClientService(AccessoryClient accessoryClient) {
        this.accessoryClient = accessoryClient;
    }

    public void createAccessory(AccessoryCreateRequest createRequest) {
        accessoryClient.createAccessory(createRequest);
    }

    public List<AccessoryResponse> getAllAccessories() {
        return accessoryClient.getAllAccessories();
    }

    public void deleteAccessory(UUID id) {
        accessoryClient.deleteAccessory(id);
    }
}
