package hotel_system.UI.action.room;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class calculateRoomPaymentAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(calculateRoomPaymentAction.class);
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public calculateRoomPaymentAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        logger.debug("Начало расчета оплаты за комнату");
        try {
            System.out.print("\nРасчет оплаты\nНомер комнаты: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();

            logger.info("Расчет для комнаты {}", roomNumber);
            double cost = manager.calculateRoomPayment(roomNumber);

            System.out.printf("Итого к оплате: %.2f руб.%n", cost);
            logger.info("Рассчитанная стоимость для комнаты {}: {} руб.", roomNumber, cost);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка расчета оплаты: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка расчета: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка расчета");
        }
    }
}