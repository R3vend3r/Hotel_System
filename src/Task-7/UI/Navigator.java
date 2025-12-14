package UI;

import java.util.List;
import java.util.Stack;

public class Navigator {
    private Menu currentMenu;
    private final Stack<Menu> history;

    public Navigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
        history = new Stack<>();
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public void printMenu() {
        printMenuHeader();
        printMenuItems();
        printBackOption();
    }

    private void printMenuHeader() {
        System.out.println("Это раздел " + currentMenu.getName());
        System.out.println("Выберите действие");
    }

    private void printMenuItems() {
        int index = 1;
        for (MenuItem item : currentMenu.getMenuItems()) {
            System.out.println(index + " - " + item.getTitle());
            index++;
        }
    }

    private void printBackOption() {
        System.out.println("0 - Назад");
    }

    public void navigate(int number) {
        if (isInvalidMenuChoice(number)) {
            handleInvalidInput();
            return;
        }

        MenuItem selectedItem = getSelectedMenuItem(number);
        executeMenuItemAction(selectedItem);
        navigateToNextMenuIfAvailable(selectedItem);
    }

    private boolean isInvalidMenuChoice(int number) {
        List<MenuItem> itemList = currentMenu.getMenuItems();
        return number < 1 || number > itemList.size();
    }

    private void handleInvalidInput() {
        System.out.println("Некорректный ввод!");
    }

    private MenuItem getSelectedMenuItem(int number) {
        List<MenuItem> itemList = currentMenu.getMenuItems();
        return itemList.get(number - 1);
    }

    private void executeMenuItemAction(MenuItem menuItem) {
        if (menuItem.getAction() != null) {
            menuItem.getAction().execute();
        }
    }

    private void navigateToNextMenuIfAvailable(MenuItem menuItem) {
        if (menuItem.getNextMenu() != null) {
            history.push(currentMenu);
            currentMenu = menuItem.getNextMenu();
        }
    }

    public void backMenu() {
        if (!history.isEmpty()) {
            currentMenu = history.pop();
        } else {
            handleMainMenuNavigation();
        }
    }

    private void handleMainMenuNavigation() {
        System.out.println("Это главное меню.");
    }
}