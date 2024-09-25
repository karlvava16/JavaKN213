package itstep.learning.oop;

import itstep.learning.oop.annotations.Product;
import itstep.learning.oop.annotations.Required;

import java.util.Locale;

@Product
public class Crossover extends Vehicle {
    public double getClearance() {
        return clearance;
    }

    public void setClearance(float clearance) {
        this.clearance = clearance;
    }

    @Required
    private double clearance;

    public Crossover(String name, float clearance) {
        super(name);
        setClearance(clearance);
    }

    public Crossover() {}

    @Override
    public String getInfo() {
        return String.format(
                Locale.ROOT,
                "Crossover '%s' with clearance %.1f cm",
                super.getName(),
                this.getClearance()
        );
    }
}
