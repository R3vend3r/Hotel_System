package hotel_system.UI;

import hotel_system.UI.action_factory.ActionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Scanner;

@Component
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private ActionFactory actionFactory;

    @Autowired
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
        logger.debug("Запуск основного цикла меню");
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