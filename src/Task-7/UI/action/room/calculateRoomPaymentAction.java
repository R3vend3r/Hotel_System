package UI.action.room;

import Controller.ManagerHotel;
import UI.action.Action;

import java.text.SimpleDateFormat;
import java.util.Scanner;

public class calculateRoomPaymentAction implements Action {
    private final ManagerHotel manager;
    private final Scanner scanner = new Scanner(System.in);

    public calculateRoomPaymentAction(ManagerHotel manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        try {
            System.out.print("\nРасчет оплаты\nНомер комнаты: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Дата выезда (дд.мм.гггг): ");
            String endDate = scanner.nextLine();

            double cost = manager.calculateRoomPayment(roomNumber,
                    new SimpleDateFormat("dd.MM.yyyy").parse(endDate));

            System.out.printf("Итого к оплате: %.2f руб.%n", cost);
        } catch (Exception e) {
            System.out.println("Ошибка расчета");
        }
    }
}