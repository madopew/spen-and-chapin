package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;

public class Item {
    private SimpleStringProperty name;
    private SimpleStringProperty amount;

    public Item(String name, String counter) {
        this.name = new SimpleStringProperty(name);
        this.amount = new SimpleStringProperty(counter);
    }

    public String getItem() {
        return name.get();
    }

    public String getAmount() {
        return amount.get();
    }

    public void setItem(String item) {
        this.name.set(item);
    }

    public void setAmount(String amount) {
        this.amount.set(amount);;
    }
}