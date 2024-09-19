package itstep.learning;

import itstep.learning.oop.OopDemo;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
       new OopDemo().run();
        // new MatrixMult().run();
    }
}

/*
    У Java є прив'язка до файлової системи
    - package (аналог namespace) = директорія(папка): itstep.learning -> itstep/learning
    - public class = файл
     = Types: CapitalCamelCase
     = fiels/methods: lowerCamelCase
     = package: snake_case
 */
