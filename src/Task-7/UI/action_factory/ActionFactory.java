package UI.action_factory;

import UI.action.Action;

public interface ActionFactory {
    // Клиенты
    public Action settleClientAction();
    public Action evictClientAction();
    public Action findClientByIdAction();

    // Комнаты - управление
    public Action addRoomAction();
    public Action changeRoomStatusAction();
    public Action updateRoomPriceAction();

    // Комнаты - просмотр
    public Action showAllRoomsAction();
    public Action showAllAvailableRoomsAction();

    // Комнаты - сортировка всех
    public Action showRoomsSortedByPriceAction();
    public Action showRoomsSortedByCapacityAction();
    public Action showRoomsSortedByStarsAction();
    public Action showRoomsSortedByTypeAction();

    // Комнаты - сортировка доступных
    public Action showAvailableRoomsSortedByPriceAction();
    public Action showAvailableRoomsSortedByCapacityAction();
    public Action showAvailableRoomsSortedByStarsAction();
    public Action showAvailableRoomsSortedByTypeAction();

    // Комнаты - дополнительные операции
    public Action checkRoomAvailabilityAction();
    public Action showRoomDetailsAction();
    public Action showAvailableRoomsByDateAction();

    // Клиенты - просмотр и сортировка
    public Action showClientsSortedByNameAction();
    public Action showClientsSortedByCheckoutDateAction();
    public Action showAllClientsAction();

    // Услуги - управление
    public Action addAmenityAction();
    public Action updateAmenityPriceAction();
    public Action addAmenityToClientAction();
    public Action showAllAmenitiesAction();

    // Услуги клиентов
    public Action showClientAmenitiesSortedByDateAction();
    public Action showClientAmenitiesSortedByPriceAction();
    public Action showClientAmenitiesAction();

    // Услуги - сортировка
    public Action showAmenitiesSortedByPriceAction();
    public Action showAmenitiesSortedByNameAction();

    // Отчеты и аналитика
    public Action showLastThreeRoomBookingsAction();
    Action getFullRoomHistoryAction();
    public Action calculateRoomPaymentAction();
    public Action showClientCountAction();
    public Action showTotalRevenueAction();
    public Action showAvailableRoomsCountAction();

    // Бронирования
    public Action showAllCompletedBookingsAction();

    //Import and Export Csv
    public Action importRoomsCsvAction();
    public Action exportRoomsCsvAction();
    public Action exportClientsCsvAction();
    public Action importClientsCsvAction();
    public Action exportAmenitiesCsvAction();
    public Action importAmenitiesCsvAction();
    public Action exportBookingsCsvAction();
    public Action importBookingsCsvAction();
    public Action exportAmenityOrdersCsvAction();
    public Action importAmenityOrdersCsvAction();

    //
    Action saveStateAction();
    Action loadStateAction();
}