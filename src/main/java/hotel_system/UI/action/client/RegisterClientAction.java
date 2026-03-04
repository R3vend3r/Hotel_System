package hotel_system.UI.action.client;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.UI.action.Action;
import hotel_system.controller.ClientController;
import hotel_system.dto.ClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class RegisterClientAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(RegisterClientAction.class);
    private final ClientController clientController;
    private final Scanner scanner = new Scanner(System.in);

    public RegisterClientAction(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void execute() {
        logger.debug("Начало регистрации клиента");
        try {
            System.out.println("\n=== Регистрация нового клиента ===");

            System.out.print("Имя: ");
            String name = scanner.nextLine().trim();

            System.out.print("Фамилия: ");
            String surname = scanner.nextLine().trim();

            if (name.isEmpty() || surname.isEmpty()) {
                logger.warn("Введены пустые имя или фамилия");
                System.out.println("Ошибка: имя и фамилия не могут быть пустыми");
                return;
            }

            ClientRequest request = new ClientRequest(name, surname);

            logger.info("Регистрация клиента: {} {}", name, surname);
            clientController.registerClient(request);

            System.out.println("Клиент успешно зарегистрирован!");
            logger.info("Клиент {} {} успешно зарегистрирован", name, surname);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при регистрации клиента: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при регистрации клиента: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}