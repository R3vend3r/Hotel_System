package hotelsystem.dependencies.config;

import hotelsystem.UI.action_factory.ActionFactoryController;
import hotelsystem.csv.*;
import hotelsystem.repository.*;
import hotelsystem.service.*;


import java.util.Map;

public class JavaConfiguration implements Configuration {

    @Override
    public String getPackageToScan() {
        return "hotelsystem";
    }

    @Override
    public Map<String, Class> getInterfaceToImplementation() {
        return Map.ofEntries(
                Map.entry("IRoomRepository", RoomRepository.class),
                Map.entry("IClientRepository", ClientRepository.class),
                Map.entry("IAmenityRepository", AmenityRepository.class),
                Map.entry("IOrderRepository", OrderRepository.class),

                Map.entry("amenityCsvService", AmenityCsvService.class),
                Map.entry("amenityOrderCsvService", AmenityOrderCsvService.class),
                Map.entry("clientCsvService", ClientCsvService.class),
                Map.entry("roomCsvService", RoomCsvService.class),
                Map.entry("roomBookingCsvService", RoomBookingCsvService.class),

                Map.entry("ActionFactory", ActionFactoryController.class)
        );
    }
}