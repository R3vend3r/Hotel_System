package hotel_system.UI;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Menu {
    private static final Logger logger = LoggerFactory.getLogger(Menu.class);
    private final String name;
    private final List<MenuItem> menuItems;

    public Menu(String name) {
        this.name = name;
        this.menuItems=new ArrayList<>();
    }

    public void addMenuItem(MenuItem menuItem){
        this.menuItems.add(menuItem);
    }
}
