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

    private Navigator navigator;
    private final Object navigatorLock = new Object();
    private boolean initialized = false;

    @PostConstruct
    public void init() {
        if (initialized) {
            return;
        }

        logger.info("MenuController: Начало инициализации");
        try {
            logger.debug("MenuController: Получен менеджер данных: {}", dataManager);
            logger.debug("MenuController: Получен билдер меню: {}", builder);

            builder.getRootMenu();

            initialized = true;
            logger.info("MenuController: Инициализация успешно завершена");

        } catch (Exception e) {
            logger.error("MenuController: Ошибка при инициализации: {}", e.getMessage(), e);
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

    public void run() {
        logger.info("MenuController: Запуск основного цикла меню");
        try {
            runMainLoop();
            closeScanner();
            logger.info("MenuController: Работа меню завершена успешно");

        } catch (Exception e) {
            logger.error("MenuController: Критическая ошибка при работе меню: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void runMainLoop() {
        logger.debug("MenuController: Вход в основной цикл обработки команд");
        boolean isRunning = true;
        int iterationCount = 0;
        Navigator currentNavigator = getNavigator();

        while (isRunning) {
            iterationCount++;
            logger.debug("MenuController: Итерация цикла #{}", iterationCount);

            currentNavigator.printMenu();

            int userChoice = getUserInput();
            if (userChoice == -1) {
                continue;
            }

            logger.info("MenuController: Пользователь выбрал пункт {}", userChoice);
            isRunning = processUserChoice(userChoice, currentNavigator);
        }

        logger.debug("MenuController: Выход из основного цикла, всего итераций: {}", iterationCount);
    }

    private int getUserInput() {
        logger.debug("MenuController: Получение ввода от пользователя");
        Scanner scanner = new Scanner(System.in);
        try {
            int input = scanner.nextInt();
            logger.debug("MenuController: Пользователь ввел: {}", input);
            return input;

        } catch (Exception exception) {
            logger.error("MenuController: Ошибка ввода данных пользователем: {}", exception.getMessage(), exception);
            handleInputError();
            return -1;
        } finally {
            scanner.nextLine();
            logger.debug("MenuController: Очистка буфера сканера");
        }
    }

    private void handleInputError() {
        logger.warn("MenuController: Некорректный ввод от пользователя");
        System.out.println("Ошибка ввода! Пожалуйста, введите число.");
    }

    private boolean processUserChoice(int userChoice, Navigator navigator) {
        logger.debug("MenuController: Обработка выбора пользователя: {}", userChoice);

        if (userChoice == 0) {
            logger.info("MenuController: Пользователь выбрал 'Назад'");
            return handleBackNavigation(navigator);
        } else {
            logger.info("MenuController: Навигация к пункту меню {}", userChoice);
            handleMenuNavigation(userChoice, navigator);
            return true;
        }
    }

    private boolean handleBackNavigation(Navigator navigator) {
        logger.debug("MenuController: Обработка команды 'Назад'");

        if (navigator.isEmpty()) {
            logger.info("MenuController: Выход из приложения (корневое меню)");
            System.out.println("Выход из программы...");
            return false;
        } else {
            logger.info("MenuController: Возврат на предыдущий уровень меню");
            navigator.backMenu();
            return true;
        }
    }

    private void handleMenuNavigation(int userChoice, Navigator navigator) {
        logger.info("MenuController: Начало обработки навигации к пункту {}", userChoice);
        try {
            navigator.navigate(userChoice);
            logger.info("MenuController: Навигация к пункту {} выполнена успешно", userChoice);

        } catch (Exception e) {
            logger.error("MenuController: Ошибка при навигации к пункту {}: {}", userChoice, e.getMessage(), e);
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void closeScanner() {
        logger.debug("MenuController: Закрытие сканера ввода");
        try {
            Scanner scanner = new Scanner(System.in);
            scanner.close();
            logger.debug("MenuController: Сканер успешно закрыт");

        } catch (Exception e) {
            logger.warn("MenuController: Ошибка при закрытии сканера: {}", e.getMessage());
        }
    }
}