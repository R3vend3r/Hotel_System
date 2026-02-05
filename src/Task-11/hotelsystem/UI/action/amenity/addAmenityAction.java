package hotelsystem.UI.action.amenity;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.model.Amenity;
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
        logger.info("addAmenityAction: Начало добавления новой услуги");
        try {
            System.out.println("\nДобавление услуги:");
            System.out.print("Название: ");
            String name = scanner.nextLine();
            System.out.print("Цена: ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            logger.info("addAmenityAction: Добавление услуги '{}' с ценой {}", name, price);
            manager.addAmenity(new Amenity(name, price));

            System.out.println("Услуга добавлена");
            logger.info("addAmenityAction: Услуга '{}' успешно добавлена", name);

        } catch (Exception e) {
            logger.error("addAmenityAction: Ошибка при добавлении услуги: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}