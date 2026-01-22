package hotelsystem.UI;

import hotelsystem.UI.action.Action;
import lombok.Getter;

@Getter
public class MenuItem {
    private String title;
    private Action action;
    private Menu nextMenu;

    public MenuItem(String title, Action action, Menu nextMenu) {
        this.title = title;
        this.action = action;
        this.nextMenu = nextMenu;
    }

    public void doAction(){
        if (action!=null)
            action.execute();
    }

}
