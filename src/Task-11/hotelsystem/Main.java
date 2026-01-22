package hotelsystem;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.Builder;
import hotelsystem.UI.MenuController;
import hotelsystem.dependencies.context.AppContext;
import hotelsystem.dependencies.factory.BeanFactory;
import org.apache.log4j.BasicConfigurator;
import hotelsystem.Utils.DatabaseManager;

public class Main {
    public AppContext initializeContext() {
        AppContext context = new AppContext();
        BeanFactory beanFactory = new BeanFactory(context);
        context.setBeanFactory(beanFactory);
        return context;
    }

    public static void main(String[] args) {
        try {
            BasicConfigurator.configure();

            Main app = new Main();
            AppContext context = app.initializeContext();

            ManagerHotel manager = context.getBean(ManagerHotel.class);
            Builder builder = context.getBean(Builder.class);
            MenuController menuController = context.getBean(MenuController.class);

            builder.buildMenu();
            menuController.run();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Закрытие соединений...");
                try {
                    DatabaseManager.getInstance().closeConnection();
                } catch (Exception e) {
                    System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
                }
            }));

        } catch (Exception e) {
            System.err.println("Фатальная ошибка при запуске:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}