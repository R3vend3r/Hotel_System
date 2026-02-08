package hotel_system.UI.action.amenity;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class updateAmenityPriceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(updateAmenityPriceAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public updateAmenityPriceAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало изменения цены услуги");
        try {
            System.out.println("\nИзменение цены услуги:");
            System.out.print("Название услуги: ");
            String name = scanner.nextLine();
            System.out.print("Новая цена: ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            logger.info("Изменение цены услуги '{}' на {}", name, price);
            manager.updateAmenityPrice(name, price);

            System.out.println("Цена обновлена");
            logger.info("Цена услуги '{}' успешно обновлена", name);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при изменении цены услуги: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при изменении цены услуги: {}",
                    e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}