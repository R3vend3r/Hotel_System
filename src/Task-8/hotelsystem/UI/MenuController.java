package hotelsystem.UI;

import hotelsystem.controller.ManagerHotel;
import hotelsystem.UI.action_factory.ActionFactory;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.dependencies.annotation.PostConstruct;

import java.util.Scanner;

public class MenuController {
    @Inject
    private ManagerHotel dataManager;

    @Inject
    private ActionFactory actionFactory;

    @Inject
    private Builder builder;

    @Inject
    private Navigator navigator;

    @PostConstruct
    public void init() {
        System.out.println("Manager в MenuController: " + dataManager);

        this.navigator = new Navigator(builder.getRootMenu());
    }

    public void run() {
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
        Scanner scanner = new Scanner(System.in);
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
        Scanner scanner = new Scanner(System.in);
        scanner.close();
    }
}
