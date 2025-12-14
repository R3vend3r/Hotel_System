package hotelsystem;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.Builder;
import hotelsystem.UI.MenuController;
import hotelsystem.dependencies.context.AppContext;
import hotelsystem.dependencies.factory.BeanFactory;
import org.apache.log4j.BasicConfigurator;

public class Main {
    public AppContext run() {
        AppContext applicationContext = new AppContext();
        BeanFactory beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);

        return applicationContext;
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Main main = new Main();
        AppContext applicationContext = main.run();

        ManagerHotel dataManager = applicationContext.getBean(ManagerHotel.class);

        dataManager.loadStateFromJson("hotel_db.json");

        Builder builder = applicationContext.getBean(Builder.class);
        builder.buildMenu();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Сохраняем состояние...");
            dataManager.saveStateToJson("hotel_db.json");
        }));

        MenuController menuController = applicationContext.getBean(MenuController.class);
        menuController.run();
    }
}