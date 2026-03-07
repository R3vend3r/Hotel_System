package hotel_system.UI.action_factory;

import hotel_system.UI.action.amenity.*;
import hotel_system.UI.action.client.*;
import hotel_system.UI.action.import_export.*;
import hotel_system.UI.action.room.*;
import hotel_system.controller.*;
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
    private AmenityController amenityController;

    @Autowired
    private OrderController orderController;

    @Autowired
    private ClientController clientController;

    @Autowired
    private RoomController roomController;

    @Autowired
    private CsvTestController csvTestController;

    public ActionFactoryController(){}

    public ActionFactoryController(AmenityController amenityController,ClientController clientController,
                                   CsvTestController csvTestController, RoomController roomController,
                                   OrderController orderController) {
        this.amenityController = amenityController;
        this.orderController = orderController;
        this.clientController = clientController;
        this.roomController = roomController;
        this.csvTestController = csvTestController;
    }

    @Override
    public Action settleClientAction() {
        return new SettleClientAction(roomController, orderController, clientController);
    }

    @Override
    public Action evictClientAction() {
        return new EvictClientAction(clientController, orderController);
    }

    @Override
    public Action registerClient() {
        return new RegisterClientAction(clientController);
    }

    @Override
    public Action findClientByIdAction() {
        return new FindClientByIdAction(clientController);
    }

    @Override
    public Action addRoomAction() {
        return new AddRoomAction(roomController);
    }

    @Override
    public Action changeRoomStatusAction() {
        return new ChangeRoomStatusAction(roomController);
    }

    @Override
    public Action updateRoomPriceAction() {
        return new UpdateRoomPriceAction(roomController);
    }

    @Override
    public Action showAllRoomsAction() {
        return new ShowAllRoomsAction(roomController);
    }

    @Override
    public Action showAllAvailableRoomsAction() {
        return new ShowAllAvailableRoomsAction(roomController);
    }

    @Override
    public Action showRoomsSortedByPriceAction() {
        return new ShowRoomsSortedByPriceAction(roomController);
    }

    @Override
    public Action showRoomsSortedByCapacityAction() {
        return new ShowRoomsSortedByCapacityAction(roomController);
    }

    @Override
    public Action showRoomsSortedByStarsAction() {
        return new ShowRoomsSortedByStarsAction(roomController);
    }

    @Override
    public Action showRoomsSortedByTypeAction() {
        return new ShowRoomsSortedByTypeAction(roomController);
    }

    @Override
    public Action showAvailableRoomsSortedByPriceAction() {
        return new ShowAvailableRoomsSortedByPriceAction(roomController);
    }

    @Override
    public Action showAvailableRoomsSortedByCapacityAction() {
        return new ShowAvailableRoomsSortedByCapacityAction(roomController);
    }

    @Override
    public Action showAvailableRoomsSortedByStarsAction() {
        return new ShowAvailableRoomsSortedByStarsAction(roomController);
    }

    @Override
    public Action showAvailableRoomsSortedByTypeAction() {
        return new ShowAvailableRoomsSortedByTypeAction(roomController);
    }

    @Override
    public Action checkRoomAvailabilityAction() {
        return new CheckRoomAvailabilityAction(roomController);
    }

    @Override
    public Action showRoomDetailsAction() {
        return new ShowRoomDetailsAction(roomController);
    }

    @Override
    public Action showAvailableRoomsByDateAction() {
        return new ShowAvailableRoomsByDateAction(roomController);
    }

    @Override
    public Action showClientsSortedByNameAction() {
        return new ShowClientsSortedByNameAction(orderController, clientController);
    }

    @Override
    public Action showClientsSortedByCheckoutDateAction() {
        return new ShowClientsSortedByCheckoutDateAction(orderController, clientController);
    }

    @Override
    public Action showAllClientsAction() {
        return new ShowAllClientsAction(clientController);
    }

    @Override
    public Action addAmenityAction() {
        return new AddAmenityAction(amenityController);
    }

    @Override
    public Action updateAmenityPriceAction() {
        return new UpdateAmenityPriceAction(amenityController);
    }

    @Override
    public Action addAmenityToClientAction() {
        return new AddAmenityToClientAction(orderController);
    }

    @Override
    public Action showAllAmenitiesAction() {
        return new ShowAllAmenitiesAction(amenityController);
    }

    @Override
    public Action showClientAmenitiesSortedByDateAction() {
        return new ShowClientAmenitiesSortedByDateAction(orderController, clientController, amenityController);
    }

    @Override
    public Action showClientAmenitiesSortedByPriceAction() {
        return new ShowClientAmenitiesSortedByPriceAction(orderController, clientController, amenityController);
    }

    @Override
    public Action showClientAmenitiesAction() {
        return new ShowClientAmenitiesAction(orderController, clientController, amenityController);
    }

    @Override
    public Action showAmenitiesSortedByPriceAction() {
        return new ShowAmenitiesSortedByPriceAction(amenityController);
    }

    @Override
    public Action showAmenitiesSortedByNameAction() {
        return new ShowAmenitiesSortedByNameAction(amenityController);
    }

    @Override
    public Action showLastThreeRoomBookingsAction() {
        return new ShowLastThreeRoomBookingsAction(orderController, clientController);
    }

    @Override
    public Action calculateRoomPaymentAction() {
        return new CalculateRoomPaymentAction(orderController);
    }

    @Override
    public Action showClientCountAction() {
        return new ShowClientCountAction(clientController);
    }

    @Override
    public Action showTotalRevenueAction() {
        return new ShowTotalRevenueAction(orderController);
    }

    @Override
    public Action showAvailableRoomsCountAction() {
        return new ShowAvailableRoomsCountAction(roomController);
    }

    @Override
    public Action showAllCompletedBookingsAction() {
        return new ShowAllCompletedBookingsAction(orderController, clientController);
    }

    @Override
    public Action importRoomsCsvAction() {
        return new ImportRoomsCsvAction(csvTestController);
    }

    @Override
    public Action exportRoomsCsvAction() {
        return new ExportRoomsCsvAction(csvTestController);
    }

    @Override
    public Action exportClientsCsvAction() {
        return new ExportClientsCsvAction(csvTestController, clientController);
    }

    @Override
    public Action importClientsCsvAction() {
        return new ImportClientsCsvAction(csvTestController);
    }

    @Override
    public Action exportAmenitiesCsvAction() {
        return new ExportAmenitiesCsvAction(amenityController, csvTestController);
    }

    @Override
    public Action importAmenitiesCsvAction() {
        return new ImportAmenitiesCsvAction(csvTestController);
    }

    @Override
    public Action exportBookingsCsvAction() {
        return new ExportBookingsCsvAction(csvTestController, orderController);
    }

    @Override
    public Action importBookingsCsvAction() {
        return new ImportBookingsCsvAction(csvTestController);
    }

    @Override
    public Action exportAmenityOrdersCsvAction() {
        return new ExportAmenityOrdersCsvAction(csvTestController);
    }

    @Override
    public Action importAmenityOrdersCsvAction() {
        return new ImportAmenityOrdersCsvAction(csvTestController);
    }

    @Override
    public Action getFullRoomHistoryAction() {
        return new GetFullRoomHistoryAction(orderController);
    }

}