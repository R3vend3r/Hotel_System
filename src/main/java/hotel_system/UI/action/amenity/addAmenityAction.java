package hotel_system.UI.action.amenity;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.model.entity.Amenity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class addAmenityAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(addAmenityAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public addAmenityAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало добавления новой услуги");
        try {
            System.out.println("\nДобавление услуги:");
            System.out.print("Название: ");
            String name = scanner.nextLine();
            System.out.print("Цена: ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            logger.info("Добавление услуги '{}' с ценой {}", name, price);
            manager.addAmenity(new Amenity(name, price));

            System.out.println("Услуга добавлена");
            logger.info("Услуга '{}' успешно добавлена", name);

        }
        catch (ManagerHotelException e) {
            logger.error("Ошибка при добавлении услуги: {}", e.getMessage(), e);
            System.out.println("Ошибка при добавление услуги: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при добавление услуги", e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}