package hotel_system.UI.action_factory;

import hotel_system.UI.action.Action;

public interface ActionFactory {
    public Action settleClientAction();
    public Action evictClientAction();
    public Action findClientByIdAction();

    public Action addRoomAction();
    public Action changeRoomStatusAction();
    public Action updateRoomPriceAction();

    public Action showAllRoomsAction();
    public Action showAllAvailableRoomsAction();

    public Action showRoomsSortedByPriceAction();
    public Action showRoomsSortedByCapacityAction();
    public Action showRoomsSortedByStarsAction();
    public Action showRoomsSortedByTypeAction();

    public Action showAvailableRoomsSortedByPriceAction();
    public Action showAvailableRoomsSortedByCapacityAction();
    public Action showAvailableRoomsSortedByStarsAction();
    public Action showAvailableRoomsSortedByTypeAction();

    public Action checkRoomAvailabilityAction();
    public Action showRoomDetailsAction();
    public Action showAvailableRoomsByDateAction();

    public Action showClientsSortedByNameAction();
    public Action showClientsSortedByCheckoutDateAction();
    public Action showAllClientsAction();

    public Action addAmenityAction();
    public Action updateAmenityPriceAction();
    public Action addAmenityToClientAction();
    public Action showAllAmenitiesAction();

    public Action showClientAmenitiesSortedByDateAction();
    public Action showClientAmenitiesSortedByPriceAction();
    public Action showClientAmenitiesAction();

    public Action showAmenitiesSortedByPriceAction();
    public Action showAmenitiesSortedByNameAction();

    public Action showLastThreeRoomBookingsAction();
    Action getFullRoomHistoryAction();
    public Action calculateRoomPaymentAction();
    public Action showClientCountAction();
    public Action showTotalRevenueAction();
    public Action showAvailableRoomsCountAction();

    public Action showAllCompletedBookingsAction();

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

}