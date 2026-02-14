package hotel_system.UI.action_factory;

import hotel_system.UI.action.amenity.*;
import hotel_system.UI.action.client.*;
import hotel_system.UI.action.import_export.*;
import hotel_system.UI.action.room.*;
import hotel_system.model.ManagerHotel;
import hotel_system.UI.action.Action;
import hotel_system.UI.action.order.EvictClientAction;
import hotel_system.UI.action.order.ShowAllCompletedBookingsAction;
import hotel_system.UI.action.order.SettleClientAction;
import hotel_system.UI.action.order.ShowTotalRevenueAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActionFactoryController implements ActionFactory {

    @Autowired
    private ManagerHotel managerHotel;

    public ActionFactoryController(){}

    public ActionFactoryController(ManagerHotel managerHotel) {
        this.managerHotel = managerHotel;
    }

    @Override
    public Action settleClientAction() {
        return new SettleClientAction(managerHotel);
    }

    @Override
    public Action evictClientAction() {
        return new EvictClientAction(managerHotel);
    }

    @Override
    public Action findClientByIdAction() {
        return new FindClientByIdAction(managerHotel);
    }

    @Override
    public Action addRoomAction() {
        return new AddRoomAction(managerHotel);
    }

    @Override
    public Action changeRoomStatusAction() {
        return new ChangeRoomStatusAction(managerHotel);
    }

    @Override
    public Action updateRoomPriceAction() {
        return new UpdateRoomPriceAction(managerHotel);
    }

    @Override
    public Action showAllRoomsAction() {
        return new ShowAllRoomsAction(managerHotel);
    }

    @Override
    public Action showAllAvailableRoomsAction() {
        return new ShowAllAvailableRoomsAction(managerHotel);
    }

    @Override
    public Action showRoomsSortedByPriceAction() {
        return new ShowRoomsSortedByPriceAction(managerHotel);
    }

    @Override
    public Action showRoomsSortedByCapacityAction() {
        return new ShowRoomsSortedByCapacityAction(managerHotel);
    }

    @Override
    public Action showRoomsSortedByStarsAction() {
        return new ShowRoomsSortedByStarsAction(managerHotel);
    }

    @Override
    public Action showRoomsSortedByTypeAction() {
        return new ShowRoomsSortedByTypeAction(managerHotel);
    }

    @Override
    public Action showAvailableRoomsSortedByPriceAction() {
        return new ShowAvailableRoomsSortedByPriceAction(managerHotel);
    }

    @Override
    public Action showAvailableRoomsSortedByCapacityAction() {
        return new ShowAvailableRoomsSortedByCapacityAction(managerHotel);
    }

    @Override
    public Action showAvailableRoomsSortedByStarsAction() {
        return new ShowAvailableRoomsSortedByStarsAction(managerHotel);
    }

    @Override
    public Action showAvailableRoomsSortedByTypeAction() {
        return new ShowAvailableRoomsSortedByTypeAction(managerHotel);
    }

    @Override
    public Action checkRoomAvailabilityAction() {
        return new CheckRoomAvailabilityAction(managerHotel);
    }

    @Override
    public Action showRoomDetailsAction() {
        return new ShowRoomDetailsAction(managerHotel);
    }

    @Override
    public Action showAvailableRoomsByDateAction() {
        return new ShowAvailableRoomsByDateAction(managerHotel);
    }

    @Override
    public Action showClientsSortedByNameAction() {
        return new ShowClientsSortedByNameAction(managerHotel);
    }

    @Override
    public Action showClientsSortedByCheckoutDateAction() {
        return new ShowClientsSortedByCheckoutDateAction(managerHotel);
    }

    @Override
    public Action showAllClientsAction() {
        return new ShowAllClientsAction(managerHotel);
    }

    @Override
    public Action addAmenityAction() {
        return new AddAmenityAction(managerHotel);
    }

    @Override
    public Action updateAmenityPriceAction() {
        return new UpdateAmenityPriceAction(managerHotel);
    }

    @Override
    public Action addAmenityToClientAction() {
        return new AddAmenityToClientAction(managerHotel);
    }

    @Override
    public Action showAllAmenitiesAction() {
        return new ShowAllAmenitiesAction(managerHotel);
    }

    @Override
    public Action showClientAmenitiesSortedByDateAction() {
        return new ShowClientAmenitiesSortedByDateAction(managerHotel);
    }

    @Override
    public Action showClientAmenitiesSortedByPriceAction() {
        return new ShowClientAmenitiesSortedByPriceAction(managerHotel);
    }

    @Override
    public Action showClientAmenitiesAction() {
        return new ShowClientAmenitiesAction(managerHotel);
    }

    @Override
    public Action showAmenitiesSortedByPriceAction() {
        return new ShowAmenitiesSortedByPriceAction(managerHotel);
    }

    @Override
    public Action showAmenitiesSortedByNameAction() {
        return new ShowAmenitiesSortedByNameAction(managerHotel);
    }

    @Override
    public Action showLastThreeRoomBookingsAction() {
        return new ShowLastThreeRoomBookingsAction(managerHotel);
    }

    @Override
    public Action calculateRoomPaymentAction() {
        return new CalculateRoomPaymentAction(managerHotel);
    }

    @Override
    public Action showClientCountAction() {
        return new ShowClientCountAction(managerHotel);
    }

    @Override
    public Action showTotalRevenueAction() {
        return new ShowTotalRevenueAction(managerHotel);
    }

    @Override
    public Action showAvailableRoomsCountAction() {
        return new ShowAvailableRoomsCountAction(managerHotel);
    }

    @Override
    public Action showAllCompletedBookingsAction() {
        return new ShowAllCompletedBookingsAction(managerHotel);
    }

    @Override
    public Action importRoomsCsvAction() {
        return new ImportRoomsCsvAction(managerHotel);
    }

    @Override
    public Action exportRoomsCsvAction() {
        return new ExportRoomsCsvAction(managerHotel);
    }

    @Override
    public Action exportClientsCsvAction() {
        return new ExportClientsCsvAction(managerHotel);
    }

    @Override
    public Action importClientsCsvAction() {
        return new ImportClientsCsvAction(managerHotel);
    }

    @Override
    public Action exportAmenitiesCsvAction() {
        return new ExportAmenitiesCsvAction(managerHotel);
    }

    @Override
    public Action importAmenitiesCsvAction() {
        return new ImportAmenitiesCsvAction(managerHotel);
    }

    @Override
    public Action exportBookingsCsvAction() {
        return new ExportBookingsCsvAction(managerHotel);
    }

    @Override
    public Action importBookingsCsvAction() {
        return new ImportBookingsCsvAction(managerHotel);
    }

    @Override
    public Action exportAmenityOrdersCsvAction() {
        return new ExportAmenityOrdersCsvAction(managerHotel);
    }

    @Override
    public Action importAmenityOrdersCsvAction() {
        return new ImportAmenityOrdersCsvAction(managerHotel);
    }

    @Override
    public Action getFullRoomHistoryAction() {
        return new GetFullRoomHistoryAction(managerHotel);
    }

}