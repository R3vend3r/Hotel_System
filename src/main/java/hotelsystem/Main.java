package hotelsystem;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.Builder;
import hotelsystem.UI.MenuController;
import hotelsystem.dependencies.context.AppContext;
import hotelsystem.dependencies.factory.BeanFactory;
import hotelsystem.Utils.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public AppContext initializeContext() {
        AppContext context = new AppContext();
        BeanFactory beanFactory = new BeanFactory(context);
        context.setBeanFactory(beanFactory);
        return context;
    }

    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(ManagerHotel.class);
        try {
            Main app = new Main();
            AppContext context = app.initializeContext();

            ManagerHotel manager = context.getBean(ManagerHotel.class);
            Builder builder = context.getBean(Builder.class);
            MenuController menuController = context.getBean(MenuController.class);

            builder.buildMenu();
            menuController.run();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.debug("Закрытие соединений...");
                try {
                    DatabaseManager.getInstance().closeConnection();
                } catch (Exception e) {
                    logger.error("Ошибка при закрытии соединения: {}", e.getMessage());
                }
            }));
        } catch (Exception e) {
            logger.error("Фатальная ошибка при запуске:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}