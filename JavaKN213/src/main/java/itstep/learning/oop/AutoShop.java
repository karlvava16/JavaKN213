package itstep.learning.oop;

import java.util.ArrayList;
import java.util.List;

public class AutoShop {
    private final List<Vehicle> vehicles;
    public AutoShop() {
        vehicles = new ArrayList<>();vehicles.add( new Bike( "Kawasaki Ninja", "Sport" ) ) ;vehicles.add( new Bike( "Harley-Davidson Sportster", "Road" ) ) ;vehicles.add( new Bus( "Renault Master", 48 ) ) ;vehicles.add( new Bus( "Mercedes-Benz Sprinter", 21 ) ) ;vehicles.add( new Bus( "Bogdan A092", 24 ) ) ;vehicles.add( new Bus( "Volvo 9700", 54 ) ) ;
    }

    public void run()
    {
        for (Vehicle vehicle : vehicles)
        {
            System.out.println(vehicle.getInfo());
        }
    }
}
