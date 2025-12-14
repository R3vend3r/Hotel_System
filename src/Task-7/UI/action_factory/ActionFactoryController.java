package UI.action_factory;

import Controller.ManagerHotel;
import UI.action.Action;
import UI.action.amenity.*;
import UI.action.client.*;
import UI.action.import_export.*;
import UI.action.order.evictClientAction;
import UI.action.order.showAllCompletedBookingsAction;
import UI.action.order.settleClientAction;
import UI.action.order.showTotalRevenueAction;
import UI.action.room.*;

public class ActionFactoryController implements ActionFactory {
    private final ManagerHotel managerHotel;

    public ActionFactoryController(ManagerHotel managerHotel) {
        this.managerHotel = managerHotel;
    }

    // Клиенты
    @Override
    public Action settleClientAction() {
        return new settleClientAction(managerHotel);
    }

    @Override
    public Action evictClientAction() {
        return new evictClientAction(managerHotel);
    }

    @Override
    public Action findClientByIdAction() {
        return new findClientByIdAction(managerHotel);
    }

    // Комнаты - управление
    @Override
    public Action addRoomAction() {
        return new addRoomAction(managerHotel);
    }

    @Override
    public Action changeRoomStatusAction() {
        return new changeRoomStatusAction(managerHotel);
    }

    @Override
    public Action updateRoomPriceAction() {
        return new updateRoomPriceAction(managerHotel);
    }

    // Комнаты - просмотр
    @Override
    public Action showAllRoomsAction() {
        return new showAllRoomsAction(managerHotel);
    }

    @Override
    public Action showAllAvailableRoomsAction() {
        return new showAllAvailableRoomsAction(managerHotel);
    }

    // Комнаты - сортировка всех
    @Override
    public Action showRoomsSortedByPriceAction() {
        return new showRoomsSortedByPriceAction(managerHotel);
    }

    @Override
    public Action showRoomsSortedByCapacityAction() {
        return new showRoomsSortedByCapacityAction(managerHotel);
    }

    @Override
    public Action showRoomsSortedByStarsAction() {
        return new showRoomsSortedByStarsAction(managerHotel);
    }

    @Override
    public Action showRoomsSortedByTypeAction() {
        return new showRoomsSortedByTypeAction(managerHotel);
    }

    // Комнаты - сортировка доступных
    @Override
    public Action showAvailableRoomsSortedByPriceAction() {
        return new showAvailableRoomsSortedByPriceAction(managerHotel);
    }

    @Override
    public Action showAvailableRoomsSortedByCapacityAction() {
        return new showAvailableRoomsSortedByCapacityAction(managerHotel);
    }

    @Override
    public Action showAvailableRoomsSortedByStarsAction() {
        return new showAvailableRoomsSortedByStarsAction(managerHotel);
    }

    @Override
    public Action showAvailableRoomsSortedByTypeAction() {
        return new showAvailableRoomsSortedByTypeAction(managerHotel);
    }

    // Комнаты - дополнительные операции
    @Override
    public Action checkRoomAvailabilityAction() {
        return new checkRoomAvailabilityAction(managerHotel);
    }

    @Override
    public Action showRoomDetailsAction() {
        return new showRoomDetailsAction(managerHotel);
    }

    @Override
    public Action showAvailableRoomsByDateAction() {
        return new showAvailableRoomsByDateAction(managerHotel);
    }

    // Клиенты - просмотр и сортировка
    @Override
    public Action showClientsSortedByNameAction() {
        return new showClientsSortedByNameAction(managerHotel);
    }

    @Override
    public Action showClientsSortedByCheckoutDateAction() {
        return new showClientsSortedByCheckoutDateAction(managerHotel);
    }

    @Override
    public Action showAllClientsAction() {
        return new showAllClientsAction(managerHotel);
    }

    // Услуги - управление
    @Override
    public Action addAmenityAction() {
        return new addAmenityAction(managerHotel);
    }

    @Override
    public Action updateAmenityPriceAction() {
        return new updateAmenityPriceAction(managerHotel);
    }

    @Override
    public Action addAmenityToClientAction() {
        return new addAmenityToClientAction(managerHotel);
    }

    @Override
    public Action showAllAmenitiesAction() {
        return new showAllAmenitiesAction(managerHotel);
    }

    // Услуги клиентов
    @Override
    public Action showClientAmenitiesSortedByDateAction() {
        return new showClientAmenitiesSortedByDateAction(managerHotel);
    }

    @Override
    public Action showClientAmenitiesSortedByPriceAction() {
        return new showClientAmenitiesSortedByPriceAction(managerHotel);
    }

    @Override
    public Action showClientAmenitiesAction() {
        return new showClientAmenitiesAction(managerHotel);
    }

    // Услуги - сортировка
    @Override
    public Action showAmenitiesSortedByPriceAction() {
        return new showAmenitiesSortedByPriceAction(managerHotel);
    }

    @Override
    public Action showAmenitiesSortedByNameAction() {
        return new showAmenitiesSortedByNameAction(managerHotel);
    }

    // Отчеты и аналитика
    @Override
    public Action showLastThreeRoomBookingsAction() {
        return new showLastThreeRoomBookingsAction(managerHotel);
    }

    @Override
    public Action calculateRoomPaymentAction() {
        return new calculateRoomPaymentAction(managerHotel);
    }

    @Override
    public Action showClientCountAction() {
        return new showClientCountAction(managerHotel);
    }

    @Override
    public Action showTotalRevenueAction() {
        return new showTotalRevenueAction(managerHotel);
    }

    @Override
    public Action showAvailableRoomsCountAction() {
        return new showAvailableRoomsCountAction(managerHotel);
    }

    // Бронирования
    @Override
    public Action showAllCompletedBookingsAction() {
        return new showAllCompletedBookingsAction(managerHotel);
    }

    //Import/Export CSV
    @Override
    public Action importRoomsCsvAction() {
        return new importRoomsCsvAction(managerHotel);
    }

    @Override
    public Action exportRoomsCsvAction() {
        return new exportRoomsCsvAction(managerHotel);
    }

    @Override
    public Action exportClientsCsvAction() {
        return new exportClientsCsvAction(managerHotel);
    }

    @Override
    public Action importClientsCsvAction() {
        return new importClientsCsvAction(managerHotel);
    }

    @Override
    public Action exportAmenitiesCsvAction() {
        return new exportAmenitiesCsvAction(managerHotel);
    }

    @Override
    public Action importAmenitiesCsvAction() {
        return new importAmenitiesCsvAction(managerHotel);
    }

    @Override
    public Action exportBookingsCsvAction() {
        return new exportBookingsCsvAction(managerHotel);
    }

    @Override
    public Action importBookingsCsvAction() {
        return new importBookingsCsvAction(managerHotel);
    }

    @Override
    public Action exportAmenityOrdersCsvAction() {
        return new exportAmenityOrdersCsvAction(managerHotel);
    }

    @Override
    public Action importAmenityOrdersCsvAction() {
        return new importAmenityOrdersCsvAction(managerHotel);
    }

    @Override
    public Action getFullRoomHistoryAction() {
        return new getFullRoomHistoryAction(managerHotel);
    }

    @Override
    public Action saveStateAction() {
        return new saveStateAction(managerHotel);
    }

    @Override
    public Action loadStateAction() {
        return new loadStateAction(managerHotel);
    }
}