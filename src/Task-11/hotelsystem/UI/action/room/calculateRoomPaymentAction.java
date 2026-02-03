package hotelsystem.UI.action.room;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
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
        logger.info("calculateRoomPaymentAction: Начало расчета оплаты за комнату");
        try {
            System.out.print("\nРасчет оплаты\nНомер комнаты: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Дата выезда (дд.мм.гггг): ");
            String endDate = scanner.nextLine();

            logger.info("calculateRoomPaymentAction: Расчет для комнаты {} до даты {}", roomNumber, endDate);

            double cost = manager.calculateRoomPayment(roomNumber);

            System.out.printf("Итого к оплате: %.2f руб.%n", cost);
            logger.info("calculateRoomPaymentAction: Рассчитанная стоимость для комнаты {}: {} руб.",
                    roomNumber, cost);

        } catch (Exception e) {
            logger.error("calculateRoomPaymentAction: Ошибка расчета оплаты: {}", e.getMessage(), e);
            System.out.println("Ошибка расчета");
        }
    }
}