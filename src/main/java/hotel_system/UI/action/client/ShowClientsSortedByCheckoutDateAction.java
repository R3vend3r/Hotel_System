package hotel_system.UI.action.client;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.OrderController;
import hotel_system.UI.action.Action;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowClientsSortedByCheckoutDateAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowClientsSortedByCheckoutDateAction.class);
    private final OrderController orderController;

    public ShowClientsSortedByCheckoutDateAction(OrderController orderController) {
        this.orderController = orderController;
    }

    @Override
    public void execute() {
        logger.debug("Начало отображения клиентов по дате выезда");
        try {
            System.out.println("\nКлиенты (по дате выезда):");
            long count = orderController.getAllActiveBookings(SortType.DATE_END)
                    .stream()
                    .peek(c -> System.out.printf("%s %s (выезд: %s)%n",
                            c.getClient().getName(), c.getClient().getSurname(), c.getCheckOutDate()))
                    .count();

            logger.info("Отображено {} клиентов по дате выезда", count);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при отображении клиентов по дате выезда: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отображении клиентов по дате выезда: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}