package itstep.learning.oop;

public class Bike  extends Vehicle {
    private String type;


    public Bike(String name, String type) {
        super(name);
        this.setType(type);

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
