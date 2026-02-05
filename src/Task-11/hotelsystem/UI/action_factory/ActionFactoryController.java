package hotelsystem.UI.action_factory;

import hotelsystem.UI.action.amenity.*;
import hotelsystem.UI.action.client.*;
import hotelsystem.UI.action.import_export.*;
import hotelsystem.UI.action.room.*;
import hotelsystem.model.ManagerHotel;
import hotelsystem.UI.action.Action;
import hotelsystem.UI.action.order.evictClientAction;
import hotelsystem.UI.action.order.showAllCompletedBookingsAction;
import hotelsystem.UI.action.order.settleClientAction;
import hotelsystem.UI.action.order.showTotalRevenueAction;
import hotelsystem.dependencies.annotation.Inject;

public class ActionFactoryController implements ActionFactory {
    @Inject
    private ManagerHotel managerHotel;
    public ActionFactoryController(){}
    public ActionFactoryController(ManagerHotel managerHotel) {
        this.managerHotel = managerHotel;
    }

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

    @Override
    public Action showAllRoomsAction() {
        return new showAllRoomsAction(managerHotel);
    }

    @Override
    public Action showAllAvailableRoomsAction() {
        return new showAllAvailableRoomsAction(managerHotel);
    }

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

    @Override
    public Action showAmenitiesSortedByPriceAction() {
        return new showAmenitiesSortedByPriceAction(managerHotel);
    }

    @Override
    public Action showAmenitiesSortedByNameAction() {
        return new showAmenitiesSortedByNameAction(managerHotel);
    }

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

    @Override
    public Action showAllCompletedBookingsAction() {
        return new showAllCompletedBookingsAction(managerHotel);
    }

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

}