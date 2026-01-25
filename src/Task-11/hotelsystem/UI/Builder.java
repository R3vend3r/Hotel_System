package hotelsystem.UI;

import hotelsystem.UI.action.Action;
import hotelsystem.UI.action_factory.ActionFactory;
import hotelsystem.dependencies.annotation.Component;
import hotelsystem.model.ManagerHotel;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.dependencies.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class Builder implements Action {
    private static final Logger logger = LoggerFactory.getLogger(Builder.class);
    private Menu rootMenu;

    @Inject
    private ManagerHotel managerHotel;

    @Inject
    private ActionFactory actionFactory;

    private volatile boolean initialized = false;
    private final Object lock = new Object();

    @PostConstruct
    public void init() {
        logger.info("Builder: Начало инициализации");
        try {
            execute();
            logger.info("Builder: Инициализация успешно завершена");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при инициализации: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void buildMenu() {
        logger.info("Builder: Начало построения структуры меню");
        try {
            rootMenu = new Menu("Главное меню гостиницы");
            addMainMenuItems();
            logger.info("Builder: Структура меню успешно построена");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при построении меню: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addMainMenuItems() {
        logger.debug("Builder: Добавление основных пунктов меню");
        try {
            rootMenu.addMenuItem(createMenuItem("Номера", null, buildRoomsMenu()));
            rootMenu.addMenuItem(createMenuItem("Клиенты", null, buildClientsMenu()));
            rootMenu.addMenuItem(createMenuItem("Услуги", null, buildAmenitiesMenu()));
            rootMenu.addMenuItem(createMenuItem("Отчеты", null, buildReportsMenu()));
            rootMenu.addMenuItem(createMenuItem("Операции", null, buildOperationsMenu()));
            rootMenu.addMenuItem(new MenuItem("Импорт/Экспорт", null, buildImportExportMenu()));
            logger.debug("Builder: Основные пункты меню добавлены");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении основных пунктов меню: {}", e.getMessage(), e);
            throw e;
        }
    }

    private MenuItem createMenuItem(String title, Action action, Menu nextMenu) {
        logger.debug("Builder: Создание пункта меню '{}'", title);
        return new MenuItem(title, action, nextMenu);
    }

    private Menu buildRoomsMenu() {
        logger.debug("Builder: Построение меню 'Номера'");
        try {
            Menu roomsMenu = new Menu("Управление номерами");
            addRoomManagementItems(roomsMenu);
            addRoomViewingSubmenu(roomsMenu);
            roomsMenu.addMenuItem(createMenuItem("Детали номера", actionFactory.showRoomDetailsAction(), null));
            logger.debug("Builder: Меню 'Номера' построено успешно");
            return roomsMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при построении меню 'Номера': {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addRoomManagementItems(Menu roomsMenu) {
        logger.debug("Builder: Добавление элементов управления номерами");
        try {
            roomsMenu.addMenuItem(createMenuItem("Добавить номер", actionFactory.addRoomAction(), null));
            roomsMenu.addMenuItem(createMenuItem("Изменить статус номера", actionFactory.changeRoomStatusAction(), null));
            roomsMenu.addMenuItem(createMenuItem("Изменить цену номера", actionFactory.updateRoomPriceAction(), null));
            logger.debug("Builder: Элементы управления номерами добавлены");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении элементов управления номерами: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addRoomViewingSubmenu(Menu roomsMenu) {
        logger.debug("Builder: Добавление подменю просмотра номеров");
        try {
            Menu viewRoomsMenu = createViewRoomsMenu();
            Menu sortRoomsMenu = createSortRoomsMenu();
            Menu sortAvailableRoomsMenu = createSortAvailableRoomsMenu();

            viewRoomsMenu.addMenuItem(createMenuItem("Сортировка всех", null, sortRoomsMenu));
            viewRoomsMenu.addMenuItem(createMenuItem("Сортировка свободных", null, sortAvailableRoomsMenu));
            roomsMenu.addMenuItem(createMenuItem("Просмотр номеров", null, viewRoomsMenu));
            logger.debug("Builder: Подменю просмотра номеров добавлено");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении подменю просмотра номеров: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu createViewRoomsMenu() {
        logger.debug("Builder: Создание меню просмотра номеров");
        try {
            Menu viewRoomsMenu = new Menu("Просмотр номеров");
            viewRoomsMenu.addMenuItem(createMenuItem("Все номера", actionFactory.showAllRoomsAction(), null));
            viewRoomsMenu.addMenuItem(createMenuItem("Свободные номера", actionFactory.showAllAvailableRoomsAction(), null));
            logger.debug("Builder: Меню просмотра номеров создано");
            return viewRoomsMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при создании меню просмотра номеров: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu createSortRoomsMenu() {
        logger.debug("Builder: Создание меню сортировки номеров");
        try {
            Menu sortRoomsMenu = new Menu("Сортировка номеров");
            sortRoomsMenu.addMenuItem(createMenuItem("По цене", actionFactory.showRoomsSortedByPriceAction(), null));
            sortRoomsMenu.addMenuItem(createMenuItem("По вместимости", actionFactory.showRoomsSortedByCapacityAction(), null));
            sortRoomsMenu.addMenuItem(createMenuItem("По звездам", actionFactory.showRoomsSortedByStarsAction(), null));
            sortRoomsMenu.addMenuItem(createMenuItem("По типу", actionFactory.showRoomsSortedByTypeAction(), null));
            logger.debug("Builder: Меню сортировки номеров создано");
            return sortRoomsMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при создании меню сортировки номеров: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu createSortAvailableRoomsMenu() {
        logger.debug("Builder: Создание меню сортировки свободных номеров");
        try {
            Menu sortAvailableRoomsMenu = new Menu("Сортировка свободных номеров");
            sortAvailableRoomsMenu.addMenuItem(createMenuItem("По цене", actionFactory.showAvailableRoomsSortedByPriceAction(), null));
            sortAvailableRoomsMenu.addMenuItem(createMenuItem("По вместимости", actionFactory.showAvailableRoomsSortedByCapacityAction(), null));
            sortAvailableRoomsMenu.addMenuItem(createMenuItem("По звездам", actionFactory.showAvailableRoomsSortedByStarsAction(), null));
            sortAvailableRoomsMenu.addMenuItem(createMenuItem("По типу", actionFactory.showAvailableRoomsSortedByTypeAction(), null));
            logger.debug("Builder: Меню сортировки свободных номеров создано");
            return sortAvailableRoomsMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при создании меню сортировки свободных номеров: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu buildClientsMenu() {
        logger.debug("Builder: Построение меню 'Клиенты'");
        try {
            Menu clientsMenu = new Menu("Управление клиентами");
            addClientManagementItems(clientsMenu);
            addClientViewingSubmenu(clientsMenu);
            logger.debug("Builder: Меню 'Клиенты' построено успешно");
            return clientsMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при построении меню 'Клиенты': {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addClientManagementItems(Menu clientsMenu) {
        logger.debug("Builder: Добавление элементов управления клиентами");
        try {
            clientsMenu.addMenuItem(createMenuItem("Зарегистрировать и поселить", actionFactory.settleClientAction(), null));
            clientsMenu.addMenuItem(createMenuItem("Выселить клиента", actionFactory.evictClientAction(), null));
            clientsMenu.addMenuItem(createMenuItem("Найти клиента", actionFactory.findClientByIdAction(), null));
            logger.debug("Builder: Элементы управления клиентами добавлены");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении элементов управления клиентами: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addClientViewingSubmenu(Menu clientsMenu) {
        logger.debug("Builder: Добавление подменю просмотра клиентов");
        try {
            Menu viewClientsMenu = createViewClientsMenu();
            Menu sortClientsMenu = createSortClientsMenu();

            viewClientsMenu.addMenuItem(createMenuItem("Сортировка", null, sortClientsMenu));
            clientsMenu.addMenuItem(createMenuItem("Просмотр клиентов", null, viewClientsMenu));
            logger.debug("Builder: Подменю просмотра клиентов добавлено");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении подменю просмотра клиентов: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu createViewClientsMenu() {
        logger.debug("Builder: Создание меню просмотра клиентов");
        try {
            Menu viewClientsMenu = new Menu("Просмотр клиентов");
            viewClientsMenu.addMenuItem(createMenuItem("Все клиенты", actionFactory.showAllClientsAction(), null));
            logger.debug("Builder: Меню просмотра клиентов создано");
            return viewClientsMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при создании меню просмотра клиентов: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu createSortClientsMenu() {
        logger.debug("Builder: Создание меню сортировки клиентов");
        try {
            Menu sortClientsMenu = new Menu("Сортировка клиентов");
            sortClientsMenu.addMenuItem(createMenuItem("По алфавиту", actionFactory.showClientsSortedByNameAction(), null));
            sortClientsMenu.addMenuItem(createMenuItem("По дате выезда", actionFactory.showClientsSortedByCheckoutDateAction(), null));
            logger.debug("Builder: Меню сортировки клиентов создано");
            return sortClientsMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при создании меню сортировки клиентов: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu buildAmenitiesMenu() {
        logger.debug("Builder: Построение меню 'Услуги'");
        try {
            Menu amenitiesMenu = new Menu("Управление услугами");
            addAmenityManagementItems(amenitiesMenu);
            addAmenityViewingSubmenu(amenitiesMenu);
            addClientAmenitiesSubmenu(amenitiesMenu);
            logger.debug("Builder: Меню 'Услуги' построено успешно");
            return amenitiesMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при построении меню 'Услуги': {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addAmenityManagementItems(Menu amenitiesMenu) {
        logger.debug("Builder: Добавление элементов управления услугами");
        try {
            amenitiesMenu.addMenuItem(createMenuItem("Добавить услугу", actionFactory.addAmenityAction(), null));
            amenitiesMenu.addMenuItem(createMenuItem("Изменить цену услуги", actionFactory.updateAmenityPriceAction(), null));
            logger.debug("Builder: Элементы управления услугами добавлены");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении элементов управления услугами: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addAmenityViewingSubmenu(Menu amenitiesMenu) {
        logger.debug("Builder: Добавление подменю просмотра услуг");
        try {
            Menu viewAmenitiesMenu = createViewAmenitiesMenu();
            Menu sortAmenitiesMenu = createSortAmenitiesMenu();

            viewAmenitiesMenu.addMenuItem(createMenuItem("Сортировка", null, sortAmenitiesMenu));
            amenitiesMenu.addMenuItem(createMenuItem("Просмотр услуг", null, viewAmenitiesMenu));
            logger.debug("Builder: Подменю просмотра услуг добавлено");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении подменю просмотра услуг: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu createViewAmenitiesMenu() {
        logger.debug("Builder: Создание меню просмотра услуг");
        try {
            Menu viewAmenitiesMenu = new Menu("Просмотр услуг");
            viewAmenitiesMenu.addMenuItem(createMenuItem("Все услуги", actionFactory.showAllAmenitiesAction(), null));
            logger.debug("Builder: Меню просмотра услуг создано");
            return viewAmenitiesMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при создании меню просмотра услуг: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu createSortAmenitiesMenu() {
        logger.debug("Builder: Создание меню сортировки услуг");
        try {
            Menu sortAmenitiesMenu = new Menu("Сортировка услуг");
            sortAmenitiesMenu.addMenuItem(createMenuItem("По цене", actionFactory.showAmenitiesSortedByPriceAction(), null));
            sortAmenitiesMenu.addMenuItem(createMenuItem("По названию", actionFactory.showAmenitiesSortedByNameAction(), null));
            logger.debug("Builder: Меню сортировки услуг создано");
            return sortAmenitiesMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при создании меню сортировки услуг: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addClientAmenitiesSubmenu(Menu amenitiesMenu) {
        logger.debug("Builder: Добавление подменю услуг клиентов");
        try {
            Menu clientAmenitiesMenu = createClientAmenitiesMenu();
            amenitiesMenu.addMenuItem(createMenuItem("Услуги клиентов", null, clientAmenitiesMenu));
            logger.debug("Builder: Подменю услуг клиентов добавлено");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении подменю услуг клиентов: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu createClientAmenitiesMenu() {
        logger.debug("Builder: Создание меню услуг клиента");
        try {
            Menu clientAmenitiesMenu = new Menu("Услуги клиента");
            clientAmenitiesMenu.addMenuItem(createMenuItem("Все услуги клиента", actionFactory.showClientAmenitiesAction(), null));
            clientAmenitiesMenu.addMenuItem(createMenuItem("Сортировка по дате", actionFactory.showClientAmenitiesSortedByDateAction(), null));
            clientAmenitiesMenu.addMenuItem(createMenuItem("Сортировка по цене", actionFactory.showClientAmenitiesSortedByPriceAction(), null));
            logger.debug("Builder: Меню услуг клиента создано");
            return clientAmenitiesMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при создании меню услуг клиента: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu buildReportsMenu() {
        logger.debug("Builder: Построение меню 'Отчеты'");
        try {
            Menu reportsMenu = new Menu("Отчеты и аналитика");
            addReportItems(reportsMenu);
            addBookingHistorySubmenu(reportsMenu);
            logger.debug("Builder: Меню 'Отчеты' построено успешно");
            return reportsMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при построении меню 'Отчеты': {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addReportItems(Menu reportsMenu) {
        logger.debug("Builder: Добавление элементов отчетов");
        try {
            reportsMenu.addMenuItem(createMenuItem("Количество свободных номеров", actionFactory.showAvailableRoomsCountAction(), null));
            reportsMenu.addMenuItem(createMenuItem("Количество клиентов", actionFactory.showClientCountAction(), null));
            reportsMenu.addMenuItem(createMenuItem("Общий доход", actionFactory.showTotalRevenueAction(), null));
            logger.debug("Builder: Элементы отчетов добавлены");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении элементов отчетов: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addBookingHistorySubmenu(Menu reportsMenu) {
        logger.debug("Builder: Добавление подменю истории бронирований");
        try {
            Menu bookingHistoryMenu = createBookingHistoryMenu();
            reportsMenu.addMenuItem(createMenuItem("История бронирований", null, bookingHistoryMenu));
            logger.debug("Builder: Подменю истории бронирований добавлено");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении подменю истории бронирований: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu createBookingHistoryMenu() {
        logger.debug("Builder: Создание меню истории бронирований");
        try {
            Menu bookingHistoryMenu = new Menu("История бронирований");
            bookingHistoryMenu.addMenuItem(createMenuItem("Последние 3 постояльца", actionFactory.showLastThreeRoomBookingsAction(), null));
            bookingHistoryMenu.addMenuItem(new MenuItem("Полная история номера", actionFactory.getFullRoomHistoryAction(), null));
            bookingHistoryMenu.addMenuItem(createMenuItem("Все завершенные бронирования", actionFactory.showAllCompletedBookingsAction(), null));
            logger.debug("Builder: Меню истории бронирований создано");
            return bookingHistoryMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при создании меню истории бронирований: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Menu buildOperationsMenu() {
        logger.debug("Builder: Построение меню 'Операции'");
        try {
            Menu operationsMenu = new Menu("Дополнительные операции");
            addOperationItems(operationsMenu);
            logger.debug("Builder: Меню 'Операции' построено успешно");
            return operationsMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при построении меню 'Операции': {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addOperationItems(Menu operationsMenu) {
        logger.debug("Builder: Добавление операционных элементов");
        try {
            operationsMenu.addMenuItem(createMenuItem("Рассчитать стоимость проживания", actionFactory.calculateRoomPaymentAction(), null));
            operationsMenu.addMenuItem(createMenuItem("Найти свободные номера к дате", actionFactory.showAvailableRoomsByDateAction(), null));
            operationsMenu.addMenuItem(createMenuItem("Проверить доступность номера", actionFactory.checkRoomAvailabilityAction(), null));
            operationsMenu.addMenuItem(createMenuItem("Добавить услугу клиенту", actionFactory.addAmenityToClientAction(), null));
            logger.debug("Builder: Операционные элементы добавлены");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении операционных элементов: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Menu getRootMenu() {
        logger.debug("Builder: Получение корневого меню");
        if (rootMenu == null) {
            synchronized (lock) {
                if (rootMenu == null) {
                    logger.info("Builder: Корневое меню не найдено, выполняется построение");
                    buildMenu();
                }
            }
        }
        logger.debug("Builder: Корневое меню получено успешно");
        return rootMenu;
    }

    private Menu buildImportExportMenu() {
        logger.debug("Builder: Построение меню 'Импорт/Экспорт'");
        try {
            Menu importExportMenu = new Menu("Импорт/Экспорт данных");

            addExportSection(importExportMenu);
            addImportSection(importExportMenu);

            logger.debug("Builder: Меню 'Импорт/Экспорт' построено успешно");
            return importExportMenu;
        } catch (Exception e) {
            logger.error("Builder: Ошибка при построении меню 'Импорт/Экспорт': {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addExportSection(Menu importExportMenu) {
        logger.debug("Builder: Добавление секции экспорта");
        try {
            Menu exportMenu = new Menu("Экспорт данных");

            exportMenu.addMenuItem(createMenuItem("Экспорт номеров", actionFactory.exportRoomsCsvAction(), null));
            exportMenu.addMenuItem(createMenuItem("Экспорт клиентов", actionFactory.exportClientsCsvAction(), null));
            exportMenu.addMenuItem(createMenuItem("Экспорт услуг", actionFactory.exportAmenitiesCsvAction(), null));
            exportMenu.addMenuItem(createMenuItem("Экспорт бронирований", actionFactory.exportBookingsCsvAction(), null));
            exportMenu.addMenuItem(createMenuItem("Экспорт заказов услуг", actionFactory.exportAmenityOrdersCsvAction(), null));

            importExportMenu.addMenuItem(createMenuItem("Экспорт", null, exportMenu));
            logger.debug("Builder: Секция экспорта добавлена");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении секции экспорта: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void addImportSection(Menu importExportMenu) {
        logger.debug("Builder: Добавление секции импорта");
        try {
            Menu importMenu = new Menu("Импорт данных");

            importMenu.addMenuItem(createMenuItem("Импорт номеров", actionFactory.importRoomsCsvAction(), null));
            importMenu.addMenuItem(createMenuItem("Импорт клиентов", actionFactory.importClientsCsvAction(), null));
            importMenu.addMenuItem(createMenuItem("Импорт услуг", actionFactory.importAmenitiesCsvAction(), null));
            importMenu.addMenuItem(createMenuItem("Импорт бронирований", actionFactory.importBookingsCsvAction(), null));
            importMenu.addMenuItem(createMenuItem("Импорт заказов услуг", actionFactory.importAmenityOrdersCsvAction(), null));

            importExportMenu.addMenuItem(createMenuItem("Импорт", null, importMenu));
            logger.debug("Builder: Секция импорта добавлена");
        } catch (Exception e) {
            logger.error("Builder: Ошибка при добавлении секции импорта: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void execute() {
        logger.info("Builder: Начало выполнения команды построения меню");
        try {
            buildMenu();
            logger.info("Builder: Команда построения меню успешно выполнена");
        } catch (Exception e) {
            logger.error("Builder: Ошибка выполнения команды построения меню: {}", e.getMessage(), e);
            throw e;
        }
    }
}