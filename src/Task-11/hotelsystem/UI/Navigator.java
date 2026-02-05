package hotelsystem.UI;

import hotelsystem.dependencies.annotation.Inject;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Stack;

public class Navigator {
    private static final Logger logger = LoggerFactory.getLogger(Navigator.class);

    @Getter
    @Setter
    private Menu currentMenu;
    private final Stack<Menu> history = new Stack<>();

    public Navigator() {
    }

    @Inject
    public Navigator(Menu currentMenu) {
        if (currentMenu == null) {
            throw new IllegalArgumentException("Menu cannot be null");
        }
        this.currentMenu = currentMenu;
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public void printMenu() {
        logger.info("Navigator: Отображение меню '{}'", currentMenu.getName());
        try {
            printMenuHeader();
            printMenuItems();
            printBackOption();
        } catch (Exception e) {
            logger.error("Navigator: Ошибка при отображении меню: {}", e.getMessage(), e);
        }
    }

    private void printMenuHeader() {
        String menuName = currentMenu.getName();
        System.out.println("\nЭто раздел " + menuName);
        System.out.println("Выберите действие");
    }

    private void printMenuItems() {
        int index = 1;
        List<MenuItem> items = currentMenu.getMenuItems();

        for (MenuItem item : items) {
            System.out.println(index + " - " + item.getTitle());
            index++;
        }
    }

    private void printBackOption() {
        System.out.println("0 - Назад");
    }

    public void navigate(int number) {
        logger.info("Navigator: Начало навигации к пункту {}", number);

        if (isInvalidMenuChoice(number)) {
            logger.warn("Navigator: Некорректный выбор пункта меню: {}", number);
            handleInvalidInput();
            return;
        }

        try {
            MenuItem selectedItem = getSelectedMenuItem(number);
            logger.info("Navigator: Выбран пункт '{}'", selectedItem.getTitle());

            executeMenuItemAction(selectedItem);
            navigateToNextMenuIfAvailable(selectedItem);

            logger.info("Navigator: Навигация к пункту {} завершена успешно", number);

        } catch (Exception e) {
            logger.error("Navigator: Ошибка при навигации к пункту {}: {}", number, e.getMessage(), e);
        }
    }

    private boolean isInvalidMenuChoice(int number) {
        List<MenuItem> itemList = currentMenu.getMenuItems();
        return number < 1 || number > itemList.size();
    }

    private void handleInvalidInput() {
        logger.error("Некорректный ввод! Выберите пункт из списка.");
    }

    private MenuItem getSelectedMenuItem(int number) {
        List<MenuItem> itemList = currentMenu.getMenuItems();
        return itemList.get(number - 1);
    }

    private void executeMenuItemAction(MenuItem menuItem) {
        if (menuItem.getAction() != null) {
            logger.info("Navigator: Выполнение действия для пункта '{}'", menuItem.getTitle());
            try {
                menuItem.getAction().execute();
                logger.info("Navigator: Действие для пункта '{}' выполнено успешно", menuItem.getTitle());
            } catch (Exception e) {
                logger.error("Navigator: Ошибка при выполнении действия для пункта '{}': {}",
                        menuItem.getTitle(), e.getMessage(), e);
                throw e;
            }
        }
    }

    private void navigateToNextMenuIfAvailable(MenuItem menuItem) {
        if (menuItem.getNextMenu() != null) {
            logger.info("Navigator: Переход к следующему меню '{}'", menuItem.getNextMenu().getName());
            history.push(currentMenu);
            currentMenu = menuItem.getNextMenu();
        }
    }

    public void backMenu() {
        logger.info("Navigator: Выполнение команды 'Назад'");

        if (!history.isEmpty()) {
            currentMenu = history.pop();
            logger.info("Navigator: Возврат к меню '{}'", currentMenu.getName());
        } else {
            logger.warn("Navigator: Попытка вернуться назад из корневого меню");
            handleMainMenuNavigation();
        }
    }

    private void handleMainMenuNavigation() {
        System.out.println("Это главное меню. Для выхода используйте '0' в корневом меню.");
    }
}