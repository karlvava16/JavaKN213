package itstep.learning.async;

public class AsyncDemo {
    public void run() {
        System.out.println("Async demo");
        ThreadDemo();
    }

    private void ThreadDemo()
    {
        /*
        Багатопоточність - програмування з використанням об'єктів
        системного типу - Thread.
        Об'єкти приймають у конструктор інші об'єкти функціональних
        інтерфейсів.
        (У Java функціональними інтерфейсами називають інтерфейси, у
        яких декларовано лише один метод)
         */
        Thread thread = new Thread(
                new Runnable() {            // Анонімний тип, що імплементує Runnable
                    @Override               // переозначає його метод
                    public void run() {     // та інстанціюється (стає об'єктом)
                        System.out.println("Hello Thread");
                    }                       // Традиційно для Java, створення
                }                           // нового об'єкту (thread) не створює
        );                                  // сам потік, а лише програмну сутність
        thread.start();     // ассинхронний запуск
        // thread.run();    // синхронний запуск
        System.out.println("1 Hello Main");
        System.out.println("2 Hello Main");
        System.out.println("3 Hello Main");
        System.out.println("4 Hello Main");
        System.out.println("5 Hello Main");
        System.out.println("6 Hello Main");
        System.out.println("7 Hello Main");

    }
}
/*
Асинхронне програмування.
Синхронність - послідовне у часі виконання частин коду.
    ---------- ===========
Асинхронність = будь-яке відхилення від синхронності.
    ----------    - - - - - - -       -- -- -
    ==========      = = = = = = = =     =   = ====

Реалізації

- багатозадачність : використання об'єктів рівня мови програмування / платформ
    (як-то Promise, Task, Future, Coroutine тощо)
- багатопоточність : використання системних ресурсів - потоків (якщо вони
    існують у системі)
- багатопроцесність : використання системних ресурсів - процесів
- мережні технології
    = grid
    = network
*/