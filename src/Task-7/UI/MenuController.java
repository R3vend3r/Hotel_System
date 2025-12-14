package UI;

import Controller.ManagerHotel;
import UI.action_factory.ActionFactory;
import UI.action_factory.ActionFactoryController;

import java.util.Scanner;

public class MenuController {
    private static MenuController instance;
    private final ManagerHotel dataManager;
    private Scanner scanner;
    private Builder builder;
    private Navigator navigator;

    private MenuController() {
        this.dataManager = ManagerHotel.getInstance();
        initializeUIComponents();
    }

    private void initializeUIComponents() {
        ActionFactory actionFactory = new ActionFactoryController(dataManager);
        this.builder = new Builder(actionFactory);
        this.navigator = new Navigator(builder.getRootMenu());
    }

    public static MenuController getInstance() {
        if (instance == null) {
            instance = new MenuController();
        }
        return instance;
    }

    public void run() {
        scanner = new Scanner(System.in);
        runMainLoop();
        closeScanner();
    }

    private void runMainLoop() {
        boolean isRunning = true;

        while (isRunning) {
            navigator.printMenu();
            int userChoice = getUserInput();
            isRunning = processUserChoice(userChoice);
        }
    }

    private int getUserInput() {
        try {
            return scanner.nextInt();
        } catch (Exception exception) {
            handleInputError();
            return -1;
        } finally {
            scanner.nextLine();
        }
    }

    private void handleInputError() {
        System.out.println("Ошибка ввода! ");
    }

    private boolean processUserChoice(int userChoice) {
        if (userChoice == 0) {
            return handleBackNavigation();
        } else {
            handleMenuNavigation(userChoice);
            return true;
        }
    }

    private boolean handleBackNavigation() {
        if (navigator.isEmpty()) {
            return false;
        } else {
            navigator.backMenu();
            return true;
        }
    }

    private void handleMenuNavigation(int userChoice) {
        navigator.navigate(userChoice);
    }

    private void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
