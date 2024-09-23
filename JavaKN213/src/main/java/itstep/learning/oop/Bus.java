package itstep.learning.oop;

import itstep.learning.oop.annotations.Product;
import itstep.learning.oop.annotations.Required;

@Product
public class Bus  extends Vehicle {

    @Required("seats")
    private int capacity;

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Bus (String name, int capacity)
    {
        super(name);
        this.setCapacity(capacity);
    }


    public int getCapacity() {
        return capacity;
    }

    @Override
    public String getInfo() {
        return String.format("Bus %s has %d capacity", this.getName(), capacity);
    }
}
