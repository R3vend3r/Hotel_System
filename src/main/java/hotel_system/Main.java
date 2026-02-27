package hotel_system;

import hotel_system.Utils.SpringConfig;
import hotel_system.UI.Builder;
import hotel_system.UI.MenuController;
import hotel_system.Utils.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(Main.class);
        try {
            AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(SpringConfig.class);

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