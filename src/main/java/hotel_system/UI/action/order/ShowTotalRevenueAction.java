package hotel_system.UI.action.order;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.controller.OrderController;
import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowTotalRevenueAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ShowTotalRevenueAction.class);
    private final OrderController orderController;

    public ShowTotalRevenueAction(OrderController orderController) {
        this.orderController = orderController;
    }

    @Override
    public void execute() {
        logger.debug("Начало расчета общего дохода");
        try {
            double revenue = orderController.calculateTotalRevenue();
            System.out.printf("\nОбщий доход: %.2f руб.%n", revenue);
            logger.info("Общий доход составляет {} руб.", revenue);

        } catch (ManagerHotelException e) {
            logger.error("Ошибка при расчете общего дохода: {}", e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при расчете общего дохода: {}", e.getMessage(), e);
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}