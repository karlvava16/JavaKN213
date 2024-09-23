package itstep.learning.oop;

import itstep.learning.oop.annotations.Product;
import itstep.learning.oop.annotations.Required;
import java.util.Locale;

//done
@Product
public class Bike  extends Vehicle {

    @Required
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

    @Override
    public String getInfo()
    {
        return String.format(Locale.ROOT,  "Bike %s, type: %s", this.getName(), this.getType());
    }


}
