package hotelsystem.UI;

import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action_factory.ActionFactory;
import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.dependencies.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

@Component
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Inject
    private ManagerHotel dataManager;

    @Inject
    private ActionFactory actionFactory;

    @Inject
    private Builder builder;

    private volatile Navigator navigator;
    private final Object navigatorLock = new Object();
    private boolean initialized = false;

    @PostConstruct
    public void init() {
        if (initialized) {
            return;
        }

        try {
            logger.debug("Инициализация MenuController");
            builder.getRootMenu();

            initialized = true;
            logger.info("MenuController инициализирован");

        } catch (Exception e) {
            logger.error("Ошибка при инициализации", e);
            throw e;
        }
    }

    public void run() {
        logger.info("Запуск основного цикла меню");
        try {
            runMainLoop();
            logger.info("Работа меню завершена успешно");

        } catch (Exception e) {
            logger.error("Критическая ошибка при работе меню", e);
            throw e;
        }
    }

    private Navigator getNavigator() {
        if (navigator == null) {
            synchronized (navigatorLock) {
                if (navigator == null) {
                    navigator = new Navigator(builder.getRootMenu());
                }
            }
        }
        return navigator;
    }

    private void runMainLoop() {
        logger.debug("Начало основного цикла меню");
        boolean isRunning = true;
        int iterationCount = 0;
        Navigator currentNavigator = getNavigator();
        while (isRunning) {
            iterationCount++;
            currentNavigator.printMenu();
            int userChoice = getUserInput();
            if (userChoice == -1) {
                continue;
            }
            logger.info("Пользователь выбрал пункт {}", userChoice);
            isRunning = processUserChoice(userChoice, currentNavigator);
        }
        logger.debug("Завершение основного цикла");
    }

    private int getUserInput() {
        Scanner scanner = new Scanner(System.in);
        try {
            return scanner.nextInt();
        } catch (Exception exception) {
            logger.warn("Ошибка ввода данных пользователем: {}", exception.getMessage());
            return -1;
        } finally {
            scanner.nextLine();
        }
    }

    private boolean processUserChoice(int userChoice, Navigator navigator) {
        if (userChoice == 0) {
            logger.info("Пользователь выбрал команду 'Назад'");
            return handleBackNavigation(navigator);
        } else {
            logger.info("Навигация к пункту меню {}", userChoice);
            handleMenuNavigation(userChoice, navigator);
            return true;
        }
    }

    private boolean handleBackNavigation(Navigator navigator) {
        if (navigator.isEmpty()) {
            logger.info("Выход из приложения (корневое меню)");
            return false;
        } else {
            logger.info("Возврат на предыдущий уровень меню");
            navigator.backMenu();
            return true;
        }
    }

    private void handleMenuNavigation(int userChoice, Navigator navigator) {
        try {
            navigator.navigate(userChoice);
            logger.info("Навигация к пункту {} выполнена", userChoice);

        } catch (Exception e) {
            logger.error("Ошибка при навигации к пункту {}", userChoice, e);
        }
    }
}