package itstep.learning.oop;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import itstep.learning.oop.annotations.Product;
import itstep.learning.oop.annotations.Required;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AutoShop {
    private List<Vehicle> vehicles;

    public AutoShop() {
        try {
            vehicles = new VehicleFactory().loadFromJson( "shop.json" );
        }
        catch( Exception ex ) {
            System.err.println( ex.getMessage() );
        }
    }

    public void run() {
        printAll();
        System.out.println("----------- LARGE SIZED ---------------");
        printLargeSized();
        System.out.println("----------- NON - LARGE SIZED ---------------");
        printNonLargeSized();
        System.out.println("----------- TRAILER-ABLE ---------------");
        printTrailers();
    }

    private String readAsString( InputStream stream ) throws IOException {
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream();
        int length;
        while( ( length = stream.read( buffer ) ) != -1 ) {
            byteBuilder.write( buffer, 0, length );
        }
        return byteBuilder.toString();
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
