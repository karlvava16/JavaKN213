package itstep.learning.oop;

import itstep.learning.oop.annotations.Product;
import itstep.learning.oop.annotations.Required;

import java.util.Locale;

@Product
public class Truck extends Vehicle implements LargeSized, Trailer {

    @Required
    private double cargo;


    public Truck(String name, double cargo) {
        super(name);
        setCargo(cargo);
    }

    public void setCargo(double cargo) {
        this.cargo = cargo;
    }

    public double getCargo() {
        return cargo;
    }


    @Override
    public String trailerInfo() {
        return "Truck trailer";
    }

    @Override
    public String getInfo() {
        return String.format(
                Locale.ROOT,
                "Truck '%s' with cargo %.1f tone(s)",
                super.getName(),
                this.getCargo()
        );
    }
}
