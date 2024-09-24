package itstep.learning.oop;

import itstep.learning.oop.annotations.Product;
import itstep.learning.oop.annotations.Required;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        try (
                InputStream stream = Objects.requireNonNull(
                        this.getClass().getClassLoader().getResourceAsStream("shop.json")
                ))
        {
            System.out.println(readAsString(stream));

            System.out.println();

        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        for (String name : new File("./").list())
        {
            System.out.println( name );
        }
        System.out.println("-----------------------------------------------------");
       for (Class<?> cls : getProductClasses("itstep.learning.oop")) {
           System.out.println("Class: " + cls.getName());
           printRequired(cls);
       }
    }

    private String readAsString ( InputStream stream ) throws IOException
    {
        byte[] buffer = new byte[4];
        ByteArrayOutputStream byteBuilder =  new ByteArrayOutputStream();
        int length;
        while((length = stream.read(buffer)) != -1)
        {
            byteBuilder.write(buffer, 0, length);
            System.out.print(new String(buffer, 0, length));
        }
        return byteBuilder.toString();
    }

    // вивести всі поля Bike, помічені анотацією @Required
    private void printRequired(Class<?> cls)
    {
        for(Field field : cls.getDeclaredFields())
        {
            if(field.isAnnotationPresent(Required.class))
            {
                String requiredName = field.getAnnotation(Required.class).value();
                boolean isAlter = field.getAnnotation(Required.class).isAlternative();
                System.out.println("".equals(requiredName) ? field.getName() : requiredName + " or " + field.getName() + "=" + isAlter);
            }
        }
    }


    private List<Class<?>> getProductClasses(String packageName) {
        URL classLocation = this.getClass().getClassLoader().getResource(".");
        if (classLocation == null) {
            throw new RuntimeException("Class not found!");
        }
        File classRoot = null;
        try {
            classRoot = new File(URLDecoder.decode(classLocation.getPath(), "UTF-8"),
                    packageName.replace(".", "/"));
        } catch (Exception ignored) {
        }
        if (classRoot == null) {
            throw new RuntimeException("Error resource traversing");
        }

        List<String> classNames = new ArrayList<>();
        // Викликаємо рекурсивний метод для збору всіх класів
        findClasses(classRoot, packageName, classNames);

        // Фільтруємо лише класи, що мають анотацію @Product
        List<Class<?>> classes = new ArrayList<>();
        for (String className : classNames) {
            Class<?> cls;
            try {
                cls = Class.forName(className);
            } catch (Exception ignored) {
                continue;
            }
            if (cls.isAnnotationPresent(Product.class)) {
                classes.add(cls);
            }
        }

        return classes;
    }


    private void findClasses(File directory, String packageName, List<String> classNames) {
        if (!directory.exists()) {
            return;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // Якщо це директорія - викликаємо рекурсію
                findClasses(file, packageName + "." + file.getName(), classNames);
            } else if (file.getName().endsWith(".class") && file.isFile() && file.canRead()) {
                String className = file.getName().substring(0, file.getName().length() - 6);
                classNames.add(packageName + "." + className);
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

        List<String> classNames = new ArrayList<>();
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith(".class") && file.isFile() && file.canRead()) {
                classNames.add(
                      "itstep.learning.oop." +
                              fileName.substring(0, fileName.length() - 6)
              );
            }
        }

        List<Class<?>> classes = new ArrayList<>();
        for (String className : classNames) {
            Class<?> cls;
            try {
                cls = Class.forName(className);
            }
            catch (ClassNotFoundException ignored) {continue;}

            if(cls.isAnnotationPresent(Product.class))
            {
                classes.add(cls);
            }
        }
        for (Class<?> cls : classes)
        {
            System.out.println("Class: " + cls.getName());
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