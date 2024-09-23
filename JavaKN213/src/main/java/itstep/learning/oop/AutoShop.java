package itstep.learning.oop;

import itstep.learning.oop.annotations.Required;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class AutoShop {
    private final List<Vehicle> vehicles;

    public AutoShop() {
        vehicles = new ArrayList<>();
        vehicles.add( new Bike( "Kawasaki Ninja", "Sport" ) ) ;
        vehicles.add( new Bike( "Harley-Davidson Sportster", "Road" ) ) ;
        vehicles.add( new Bus( "Renault Master", 48 ) ) ;
        vehicles.add( new Bus( "Mercedes-Benz Sprinter", 21 ) ) ;
        vehicles.add( new Bus( "Bogdan A092", 24 ) ) ;
        vehicles.add( new Bus( "Volvo 9700", 54 ) ) ;
        vehicles.add( new Truck( "Renault C-Truck", 7.5 ) ) ;
        vehicles.add( new Truck( "DAF XF 106 2018", 3.5 ) ) ;
        vehicles.add( new Truck( "Mercedes Actros L", 15.0 ) ) ;
        vehicles.add( new Crossover("Audi Q5", 110) ) ;
        vehicles.add( new Crossover("Lamborghini Urus", 90) ) ;
        vehicles.add( new Crossover("Honda CR-V", 110) ) ;
        vehicles.add( new Crossover("BMW X5", 120) ) ;
        vehicles.add( new Car("Toyota Corolla", "Hatchback ") ) ;
        vehicles.add( new Car("BMW 320", "Hearse") ) ;
        vehicles.add( new Car("VW Golf", "Fastback") ) ;
        vehicles.add( new Car("Honda Accord", "Convertible") ) ;
        vehicles.add( new Car("Audi RS6", "Hatchback") ) ;


    }

    public void run() {
//        printAll();
//        System.out.println("----------- LARGE SIZED ---------------");
//        printLargeSized();
//        System.out.println("----------- NON - LARGE SIZED ---------------");
//        printNonLargeSized();
//        System.out.println("----------- TRAILER-ABLE ---------------");
//        printTrailers();
//        System.out.println("----------- BIKE-REQUIRED ---------------");
//        printRequired();

        showAllClasses();
    }

    // вивести всі поля Bike, помічені анотацією @Required
    private void printRequired()
    {
        for(Field field : Bike.class.getDeclaredFields())
        {
            if(field.isAnnotationPresent(Required.class))
            {
                System.out.println(field.getName());
            }
        }
    }

    // показує всі файли-класи, що є у даному пакеті
    private void showAllClasses() {
        URL classLocation = this.getClass().getClassLoader().getResource(".");
        if (classLocation == null) {
            System.err.println("Class not found!");
            return;
        }
        File classRoot = null;
        File[] files;


        try {
            classRoot = new File(
                    URLDecoder.decode(classLocation.getPath(), "UTF-8"),
                    "itstep/learning/oop/"
            );
        }
        catch (Exception ignored) {}

        if (classRoot == null || (files = classRoot.listFiles()) == null) {
            System.err.println("Error resource traversing!");
            return;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith(".class") && file.isFile() && file.canRead()) {
                String className = fileName.substring(0, fileName.length() - 6);
                System.out.println(className);
            }
        }
    }


    public void printAll() {
        for( Vehicle vehicle : vehicles ) {
            System.out.println( vehicle.getInfo() );
        }
    }

    public void printLargeSized() {
        for( Vehicle vehicle : vehicles ) {
            if( vehicle instanceof LargeSized ) {
                System.out.println( vehicle.getInfo() );
            }
        }
    }

    public void printNonLargeSized() {
        for( Vehicle vehicle : vehicles ) {
            if( ! ( vehicle instanceof LargeSized ) ) {
                System.out.println( vehicle.getInfo() );
            }
        }
    }

    private void printTrailers() {
        for( Vehicle vehicle : vehicles ) {
            if( vehicle instanceof Trailer ) {
                System.out.print( vehicle.getInfo() );
                System.out.println( " could have a trailer of type " +
                        ((Trailer) vehicle).trailerInfo() );
            }
        }
    }
}