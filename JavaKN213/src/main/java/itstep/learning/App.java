package itstep.learning;

import com.google.inject.Injector;
import com.sun.javaws.jnl.IconDesc;
import itstep.learning.async.AsyncDemo;
import itstep.learning.ios.IocDemo;
import itstep.learning.ios.Resolver;
import itstep.learning.oop.OopDemo;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        // new Basics().run();
        // new OopDemo().run();
         new AsyncDemo().run();
//        Resolver resolver = new Resolver();
//        Injector injector = resolver.getInjector();
//        IocDemo iocDemo = injector.getInstance(IocDemo.class);
//        iocDemo.run();
    }
}
/*
У Java є прив'язка до файлової системи
- package (аналог namespace) = директорія (папка): itstep.learning -> itstep/learning
- public клас = файл
- casing:
 = Types: CapitalCamelCase
 = fields/methods: lowerCamelCase
 = package: snake_case
 */
