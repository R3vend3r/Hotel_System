package hotel_system.UI;

import hotel_system.UI.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public record MenuItem(String title, Action action, Menu nextMenu) {
    private static final Logger logger = LoggerFactory.getLogger(MenuItem.class);

    public void doAction() {
        if (action != null) {
            action.execute();
        }
    }

}
