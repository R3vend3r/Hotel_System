package UI;

import UI.action.Action;
import UI.action_factory.ActionFactory;

public class Builder implements Action {
    private Menu rootMenu;
    private final ActionFactory actionFactory;

    public Builder(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
        execute();
    }

    private void buildMenu() {
        rootMenu = new Menu("Главное меню гостиницы");
        addMainMenuItems();
    }

    private void addMainMenuItems() {
        rootMenu.addMenuItem(createMenuItem("Номера", null, buildRoomsMenu()));
        rootMenu.addMenuItem(createMenuItem("Клиенты", null, buildClientsMenu()));
        rootMenu.addMenuItem(createMenuItem("Услуги", null, buildAmenitiesMenu()));
        rootMenu.addMenuItem(createMenuItem("Отчеты", null, buildReportsMenu()));
        rootMenu.addMenuItem(createMenuItem("Операции", null, buildOperationsMenu()));
        rootMenu.addMenuItem(new MenuItem("Импорт/Экспорт", null, buildImportExportMenu()));
        rootMenu.addMenuItem(new MenuItem("Сохранение/загрузка состояния", null, buildSaveLoadMenu()));

    }


    private MenuItem createMenuItem(String title, Action action, Menu nextMenu) {
        return new MenuItem(title, action, nextMenu);
    }

    private Menu buildRoomsMenu() {
        Menu roomsMenu = new Menu("Управление номерами");
        addRoomManagementItems(roomsMenu);
        addRoomViewingSubmenu(roomsMenu);
        roomsMenu.addMenuItem(createMenuItem("Детали номера", actionFactory.showRoomDetailsAction(), null));
        return roomsMenu;
    }

    private void addRoomManagementItems(Menu roomsMenu) {
        roomsMenu.addMenuItem(createMenuItem("Добавить номер", actionFactory.addRoomAction(), null));
        roomsMenu.addMenuItem(createMenuItem("Изменить статус номера", actionFactory.changeRoomStatusAction(), null));
        roomsMenu.addMenuItem(createMenuItem("Изменить цену номера", actionFactory.updateRoomPriceAction(), null));
    }

    private void addRoomViewingSubmenu(Menu roomsMenu) {
        Menu viewRoomsMenu = createViewRoomsMenu();
        Menu sortRoomsMenu = createSortRoomsMenu();
        Menu sortAvailableRoomsMenu = createSortAvailableRoomsMenu();

        viewRoomsMenu.addMenuItem(createMenuItem("Сортировка всех", null, sortRoomsMenu));
        viewRoomsMenu.addMenuItem(createMenuItem("Сортировка свободных", null, sortAvailableRoomsMenu));
        roomsMenu.addMenuItem(createMenuItem("Просмотр номеров", null, viewRoomsMenu));
    }

    private Menu createViewRoomsMenu() {
        Menu viewRoomsMenu = new Menu("Просмотр номеров");
        viewRoomsMenu.addMenuItem(createMenuItem("Все номера", actionFactory.showAllRoomsAction(), null));
        viewRoomsMenu.addMenuItem(createMenuItem("Свободные номера", actionFactory.showAllAvailableRoomsAction(), null));
        return viewRoomsMenu;
    }

    private Menu createSortRoomsMenu() {
        Menu sortRoomsMenu = new Menu("Сортировка номеров");
        sortRoomsMenu.addMenuItem(createMenuItem("По цене", actionFactory.showRoomsSortedByPriceAction(), null));
        sortRoomsMenu.addMenuItem(createMenuItem("По вместимости", actionFactory.showRoomsSortedByCapacityAction(), null));
        sortRoomsMenu.addMenuItem(createMenuItem("По звездам", actionFactory.showRoomsSortedByStarsAction(), null));
        sortRoomsMenu.addMenuItem(createMenuItem("По типу", actionFactory.showRoomsSortedByTypeAction(), null));
        return sortRoomsMenu;
    }

    private Menu createSortAvailableRoomsMenu() {
        Menu sortAvailableRoomsMenu = new Menu("Сортировка свободных номеров");
        sortAvailableRoomsMenu.addMenuItem(createMenuItem("По цене", actionFactory.showAvailableRoomsSortedByPriceAction(), null));
        sortAvailableRoomsMenu.addMenuItem(createMenuItem("По вместимости", actionFactory.showAvailableRoomsSortedByCapacityAction(), null));
        sortAvailableRoomsMenu.addMenuItem(createMenuItem("По звездам", actionFactory.showAvailableRoomsSortedByStarsAction(), null));
        sortAvailableRoomsMenu.addMenuItem(createMenuItem("По типу", actionFactory.showAvailableRoomsSortedByTypeAction(), null));
        return sortAvailableRoomsMenu;
    }

    private Menu buildClientsMenu() {
        Menu clientsMenu = new Menu("Управление клиентами");
        addClientManagementItems(clientsMenu);
        addClientViewingSubmenu(clientsMenu);
        return clientsMenu;
    }

    private void addClientManagementItems(Menu clientsMenu) {
        clientsMenu.addMenuItem(createMenuItem("Зарегистрировать и поселить", actionFactory.settleClientAction(), null));
        clientsMenu.addMenuItem(createMenuItem("Выселить клиента", actionFactory.evictClientAction(), null));
        clientsMenu.addMenuItem(createMenuItem("Найти клиента", actionFactory.findClientByIdAction(), null));
    }

    private void addClientViewingSubmenu(Menu clientsMenu) {
        Menu viewClientsMenu = createViewClientsMenu();
        Menu sortClientsMenu = createSortClientsMenu();

        viewClientsMenu.addMenuItem(createMenuItem("Сортировка", null, sortClientsMenu));
        clientsMenu.addMenuItem(createMenuItem("Просмотр клиентов", null, viewClientsMenu));
    }

    private Menu createViewClientsMenu() {
        Menu viewClientsMenu = new Menu("Просмотр клиентов");
        viewClientsMenu.addMenuItem(createMenuItem("Все клиенты", actionFactory.showAllClientsAction(), null));
        return viewClientsMenu;
    }

    private Menu createSortClientsMenu() {
        Menu sortClientsMenu = new Menu("Сортировка клиентов");
        sortClientsMenu.addMenuItem(createMenuItem("По алфавиту", actionFactory.showClientsSortedByNameAction(), null));
        sortClientsMenu.addMenuItem(createMenuItem("По дате выезда", actionFactory.showClientsSortedByCheckoutDateAction(), null));
        return sortClientsMenu;
    }

    private Menu buildAmenitiesMenu() {
        Menu amenitiesMenu = new Menu("Управление услугами");
        addAmenityManagementItems(amenitiesMenu);
        addAmenityViewingSubmenu(amenitiesMenu);
        addClientAmenitiesSubmenu(amenitiesMenu);
        return amenitiesMenu;
    }

    private void addAmenityManagementItems(Menu amenitiesMenu) {
        amenitiesMenu.addMenuItem(createMenuItem("Добавить услугу", actionFactory.addAmenityAction(), null));
        amenitiesMenu.addMenuItem(createMenuItem("Изменить цену услуги", actionFactory.updateAmenityPriceAction(), null));
    }

    private void addAmenityViewingSubmenu(Menu amenitiesMenu) {
        Menu viewAmenitiesMenu = createViewAmenitiesMenu();
        Menu sortAmenitiesMenu = createSortAmenitiesMenu();

        viewAmenitiesMenu.addMenuItem(createMenuItem("Сортировка", null, sortAmenitiesMenu));
        amenitiesMenu.addMenuItem(createMenuItem("Просмотр услуг", null, viewAmenitiesMenu));
    }

    private Menu createViewAmenitiesMenu() {
        Menu viewAmenitiesMenu = new Menu("Просмотр услуг");
        viewAmenitiesMenu.addMenuItem(createMenuItem("Все услуги", actionFactory.showAllAmenitiesAction(), null));
        return viewAmenitiesMenu;
    }

    private Menu createSortAmenitiesMenu() {
        Menu sortAmenitiesMenu = new Menu("Сортировка услуг");
        sortAmenitiesMenu.addMenuItem(createMenuItem("По цене", actionFactory.showAmenitiesSortedByPriceAction(), null));
        sortAmenitiesMenu.addMenuItem(createMenuItem("По названию", actionFactory.showAmenitiesSortedByNameAction(), null));
        return sortAmenitiesMenu;
    }

    private void addClientAmenitiesSubmenu(Menu amenitiesMenu) {
        Menu clientAmenitiesMenu = createClientAmenitiesMenu();
        amenitiesMenu.addMenuItem(createMenuItem("Услуги клиентов", null, clientAmenitiesMenu));
    }

    private Menu createClientAmenitiesMenu() {
        Menu clientAmenitiesMenu = new Menu("Услуги клиента");
        clientAmenitiesMenu.addMenuItem(createMenuItem("Все услуги клиента", actionFactory.showClientAmenitiesAction(), null));
        clientAmenitiesMenu.addMenuItem(createMenuItem("Сортировка по дате", actionFactory.showClientAmenitiesSortedByDateAction(), null));
        clientAmenitiesMenu.addMenuItem(createMenuItem("Сортировка по цене", actionFactory.showClientAmenitiesSortedByPriceAction(), null));
        return clientAmenitiesMenu;
    }

    private Menu buildReportsMenu() {
        Menu reportsMenu = new Menu("Отчеты и аналитика");
        addReportItems(reportsMenu);
        addBookingHistorySubmenu(reportsMenu);
        return reportsMenu;
    }

    private void addReportItems(Menu reportsMenu) {
        reportsMenu.addMenuItem(createMenuItem("Количество свободных номеров", actionFactory.showAvailableRoomsCountAction(), null));
        reportsMenu.addMenuItem(createMenuItem("Количество клиентов", actionFactory.showClientCountAction(), null));
        reportsMenu.addMenuItem(createMenuItem("Общий доход", actionFactory.showTotalRevenueAction(), null));
    }

    private void addBookingHistorySubmenu(Menu reportsMenu) {
        Menu bookingHistoryMenu = createBookingHistoryMenu();
        reportsMenu.addMenuItem(createMenuItem("История бронирований", null, bookingHistoryMenu));
    }

    private Menu createBookingHistoryMenu() {
        Menu bookingHistoryMenu = new Menu("История бронирований");
        bookingHistoryMenu.addMenuItem(createMenuItem("Последние 3 постояльца", actionFactory.showLastThreeRoomBookingsAction(), null));
        bookingHistoryMenu.addMenuItem(new MenuItem("Полная история номера", actionFactory.getFullRoomHistoryAction(), null));
        bookingHistoryMenu.addMenuItem(createMenuItem("Все завершенные бронирования", actionFactory.showAllCompletedBookingsAction(), null));
        return bookingHistoryMenu;
    }

    private Menu buildOperationsMenu() {
        Menu operationsMenu = new Menu("Дополнительные операции");
        addOperationItems(operationsMenu);
        return operationsMenu;
    }

    private void addOperationItems(Menu operationsMenu) {
        operationsMenu.addMenuItem(createMenuItem("Рассчитать стоимость проживания", actionFactory.calculateRoomPaymentAction(), null));
        operationsMenu.addMenuItem(createMenuItem("Найти свободные номера к дате", actionFactory.showAvailableRoomsByDateAction(), null));
        operationsMenu.addMenuItem(createMenuItem("Проверить доступность номера", actionFactory.checkRoomAvailabilityAction(), null));
        operationsMenu.addMenuItem(createMenuItem("Добавить услугу клиенту", actionFactory.addAmenityToClientAction(), null));
    }

    public Menu getRootMenu() {
        return rootMenu;
    }

    private Menu buildImportExportMenu() {
        Menu importExportMenu = new Menu("Импорт/Экспорт данных");

        addExportSection(importExportMenu);
        addImportSection(importExportMenu);

        return importExportMenu;
    }

    private void addExportSection(Menu importExportMenu) {
        Menu exportMenu = new Menu("Экспорт данных");

        exportMenu.addMenuItem(createMenuItem("Экспорт номеров", actionFactory.exportRoomsCsvAction(), null));
        exportMenu.addMenuItem(createMenuItem("Экспорт клиентов", actionFactory.exportClientsCsvAction(), null));
        exportMenu.addMenuItem(createMenuItem("Экспорт услуг", actionFactory.exportAmenitiesCsvAction(), null));
        exportMenu.addMenuItem(createMenuItem("Экспорт бронирований", actionFactory.exportBookingsCsvAction(), null));
        exportMenu.addMenuItem(createMenuItem("Экспорт заказов услуг", actionFactory.exportAmenityOrdersCsvAction(), null));

        importExportMenu.addMenuItem(createMenuItem("Экспорт", null, exportMenu));
    }

    private void addImportSection(Menu importExportMenu) {
        Menu importMenu = new Menu("Импорт данных");

        importMenu.addMenuItem(createMenuItem("Импорт номеров", actionFactory.importRoomsCsvAction(), null));
        importMenu.addMenuItem(createMenuItem("Импорт клиентов", actionFactory.importClientsCsvAction(), null));
        importMenu.addMenuItem(createMenuItem("Импорт услуг", actionFactory.importAmenitiesCsvAction(), null));
        importMenu.addMenuItem(createMenuItem("Импорт бронирований", actionFactory.importBookingsCsvAction(), null));
        importMenu.addMenuItem(createMenuItem("Импорт заказов услуг", actionFactory.importAmenityOrdersCsvAction(), null));

        importExportMenu.addMenuItem(createMenuItem("Импорт", null, importMenu));
    }

    private Menu buildSaveLoadMenu() {
        Menu stateMenu = new Menu("Дополнительные операции");
        addStateItems(stateMenu);
        return stateMenu;
    }

    private void addStateItems(Menu stateMenu) {

        stateMenu.addMenuItem(new MenuItem("Сохранить состояние", actionFactory.saveStateAction(), null));
        stateMenu.addMenuItem(new MenuItem("Загрузить состояние", actionFactory.loadStateAction(), null));
    }
    @Override
    public void execute() {
        buildMenu();
    }
}