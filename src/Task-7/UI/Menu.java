package UI;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final String name;
    private final List<MenuItem> menuItems;

    public Menu(String name) {
        this.name = name;
        this.menuItems=new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void addMenuItem(MenuItem menuItem){
        this.menuItems.add(menuItem);
    }
}
