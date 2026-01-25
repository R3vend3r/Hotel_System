package hotelsystem.UI;

import hotelsystem.UI.action.Action;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class MenuItem {
    private static final Logger logger = LoggerFactory.getLogger(MenuItem.class);
    private final String title;
    private final Action action;
    private final Menu nextMenu;

    public MenuItem(String title, Action action, Menu nextMenu) {
        this.title = title;
        this.action = action;
        this.nextMenu = nextMenu;
    }

    public void doAction(){
        if (action!=null) {
            action.execute();
        }
    }

}
