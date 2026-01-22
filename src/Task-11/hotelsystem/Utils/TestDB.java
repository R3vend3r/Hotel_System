package hotelsystem.Utils;


import org.apache.log4j.PropertyConfigurator;
import hotelsystem.dependencies.context.AppContext;
import hotelsystem.dependencies.factory.BeanFactory;
import hotelsystem.model.Client;
import hotelsystem.model.Room;
import hotelsystem.model.RoomBooking;
import hotelsystem.service.*;

import java.util.Date;

public class TestDB {
    public static void main(String[] args) {
        try {
            AppContext applicationContext = new AppContext();
            BeanFactory beanFactory = new BeanFactory(applicationContext);
            applicationContext.setBeanFactory(beanFactory);

            ClientService clientService = applicationContext.getBean(ClientService.class);
            RoomService roomService = applicationContext.getBean(RoomService.class);
            AmenityService amenityService = applicationContext.getBean(AmenityService.class);
            OrderService roomBookingService = applicationContext.getBean(OrderService.class);
            OrderService amenityOrderService = applicationContext.getBean(OrderService.class);

            System.out.println("=== Тестирование клиентов ===");
            System.out.println("Все клиенты: " + clientService.getAllClients());

            System.out.println("\n=== Тестирование комнат ===");
            System.out.println("Все комнаты: " + roomService.getAllRooms());

            System.out.println("\n=== Тестирование удобств ===");
            System.out.println("Все удобства: " + amenityService.getAllAmenities());

            System.out.println("\n=== Тестирование бронирований ===");

            Room room = roomService.findRoom(101).orElseThrow();
            Client client = clientService.findClientById("cl1").orElseThrow();

            RoomBooking booking = new RoomBooking(
                    "BOOKING001",
                    client,
                    room,
                    500.0,
                    new Date(),
                    new Date(System.currentTimeMillis() + 86400000)
            );

            roomBookingService.createRoomBooking(client, room, new Date(), new Date(System.currentTimeMillis() + 86400000) );
            System.out.println("Создано бронирование: " + booking.getId());


            System.out.println("\n=== Тестирование заказов удобств ===");
            System.out.println("Все заказы удобств: " + amenityOrderService.getAllAmenityOrders());

        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}