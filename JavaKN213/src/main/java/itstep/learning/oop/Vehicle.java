package itstep.learning.oop;

import itstep.learning.oop.annotations.Required;


public abstract class Vehicle{

    @Required
    private String name;


    public Vehicle(String name) {
        setName(name);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getInfo();
}
