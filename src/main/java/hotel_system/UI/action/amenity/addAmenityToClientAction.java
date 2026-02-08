package hotel_system.UI.action.amenity;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Scanner;

public class addAmenityToClientAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(addAmenityToClientAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public addAmenityToClientAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало добавления услуги клиенту");
        try {
            System.out.println("\nДобавление услуги клиенту:");
            System.out.print("Номер комнаты: ");
            Integer roomNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Название услуги: ");
            String amenityName = scanner.nextLine();
            logger.info("Добавление услуги '{}' клиенту в комнате {}",
                    amenityName, roomNumber);
            manager.addAmenityToClient(roomNumber,
                    manager.findAmenityByName(amenityName).orElseThrow(),
                    new Date());
            System.out.println("Услуга добавлена");
            logger.info("Услуга '{}' успешно добавлена клиенту в комнате {}",
                    amenityName, roomNumber);
        }  catch (ManagerHotelException e) {
            logger.error("Ошибка при добавлении услуги клиенту: {}", e.getMessage(), e);
            System.out.println("Ошибка при добавление услуги клиенту: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при добавлении услуги клиенту: {}",
                    e.getMessage(), e);
            System.out.println("Неожиданная шибка: " + e.getMessage());
        }
    }
}