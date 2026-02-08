package hotel_system.UI;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    public Navigator(Menu currentMenu) {
        if (currentMenu == null) {
            throw new IllegalArgumentException("Menu cannot be null");
        }
        this.currentMenu = currentMenu;
        logger.debug("Navigator инициализирован с меню: {}", currentMenu.getName());
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public void printMenu() {
        logger.debug("Navigator: Отображение меню '{}'", currentMenu.getName());
        printMenuHeader();
        printMenuItems();
        printBackOption();

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
            System.out.println(index + " - " + item.title());
            index++;
        }
    }

    private void printBackOption() {
        System.out.println("0 - Назад");
    }

    public void navigate(int number) {
        logger.info("Navigator: Начало навигации к пункту {}", number);

        if (isInvalidMenuChoice(number)) {
            System.out.println("Ошибка: выберите пункт из списка (1-" +
                    currentMenu.getMenuItems().size() + ")");
            logger.warn("Navigator: Некорректный выбор пункта меню: {}", number);
            handleInvalidInput();
            return;
        }

        try {
            MenuItem selectedItem = getSelectedMenuItem(number);
            logger.debug("Navigator: Выбран пункт '{}'", selectedItem.title());

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
        if (menuItem.action() != null) {
            logger.info("Navigator: Выполнение действия для пункта '{}'", menuItem.title());
            try {
                menuItem.action().execute();
                logger.info("Navigator: Действие для пункта '{}' выполнено успешно", menuItem.title());
            } catch (Exception e) {
                logger.error("Navigator: Ошибка при выполнении действия для пункта '{}': {}",
                        menuItem.title(), e.getMessage(), e);
                throw e;
            }
        }
    }

    private void navigateToNextMenuIfAvailable(MenuItem menuItem) {
        if (menuItem.nextMenu() != null) {
            logger.info("Navigator: Переход к следующему меню '{}'", menuItem.nextMenu().getName());
            history.push(currentMenu);
            currentMenu = menuItem.nextMenu();
        }
    }

    public void backMenu() {
        logger.debug("Navigator: Выполнение команды 'Назад'");

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