package itstep.learning.oop;

import itstep.learning.oop.annotations.Product;
import itstep.learning.oop.annotations.Required;

import java.util.Locale;

@Product
public class Car extends Vehicle {
    public String getCarBody() {
        return carBody;
    }

    public void setCarBody(String carBody) {
        this.carBody = carBody;
    }

    @Required
    private String carBody;

    public Car(String name, String carBody) {
        super(name);
        setCarBody(carBody);
    }

    @Override
    public String getInfo() {
        return String.format(
                Locale.ROOT,
                "Car '%s' with car body %s",
                super.getName(),
                this.getCarBody()
        );
    }
}
