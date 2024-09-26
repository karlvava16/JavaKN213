package itstep.learning.async;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class AsyncDemo {
    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    private long startTime;

    public void run() {
        System.out.println("Async demo: make choise");
        System.out.println("1 - Thread demo");
        System.out.println("2 - Percent (thread) demo");
        System.out.println("3 - valueWithAllDigit");
        System.out.println("4 - Task demo");
        System.out.println("5 - taskPercentDemo");
        System.out.println("0 - Quit");

        Scanner kbScanner = new Scanner(System.in);
        int choice = kbScanner.nextInt();
        switch (choice) {
            case 1: ThreadDemo(); break;
            case 2: percentDemo(); break;
            case 3: valueWithAllDigit(); break;
            case 4: TaskDemo(); break;
            case 5: taskPercentDemo(); break;
        }
    }


    private double sum;

    private void taskPercentDemo() {
        startTime = System.currentTimeMillis();

        try{
            sum = 100;
            for (int i = 1; i <= 12; i++) {
                sum *= threadPool.submit(new RateTask(i)).get();
                System.out.println(
                        System.currentTimeMillis() - startTime + "ms" +
                                " Rate" + i + " sum = " + sum);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void TaskDemo()
    {
        // Багатозадачність. Особливості:
        // - задачі беруться на виконання спеціалізованим "виконавцем"
        // який треба початково створити
        // - у кінці програми виконавця необхідно зупиняти,
        //    інакше програма не завершується
        Future<?> task1 = threadPool.submit(new Rate(2));
        // - очікування виконання задачі - .get()
        try{                                                      //  Цей блок є розтлумаченням
            task1.get();
            System.out.println(System.currentTimeMillis() - startTime + "Task1");

                                                                    //  "цукрової" конструкції
        }                                                         //  await
        catch (InterruptedException | ExecutionException ex) {    //
            System.err.println( ex.getMessage() );                //
        }
        Future<String> task2 = threadPool.submit(
                new Callable<String>() {
                    public String call() throws Exception {            // метод повертває значення
                        TimeUnit.MILLISECONDS.sleep(500);      // а також містить Exception
                        return "Hello Callable";                       // у сигнатурі(у тілі немає потреби
                    }                                                  // try-catch)
                }
        );

        try{
            String res = task2.get();
            System.out.println(res);
        }
        catch (InterruptedException | ExecutionException ex) {
            System.err.println( "Task 2 finished with exception: "+ ex.getMessage() );
        }

        // - задачі можуть прийняти на виконання інші функціональні
        // інтерфейси, зокрема, Callable
        threadPool.shutdown();
        try{
            boolean isDone = threadPool.awaitTermination(300, TimeUnit.MILLISECONDS);
            if (!isDone)
            {
                List<Runnable> cancelledTasks = threadPool.shutdownNow(); // "жорстка" зупинка
                if (!cancelledTasks.isEmpty())
                {
                    System.out.println("Tasks cancelled:");
                    for (Runnable task : cancelledTasks)
                    {
                        System.out.println(task.toString());
                    }
                }
            }
        }
        catch (InterruptedException ignored) {}
    }

    private class RateTask implements Callable<Double> {
        private final int month;
        public RateTask(int month)
        {        this.month = month;
        }
        @Override
        public Double call()
                throws Exception {
            System.out.println(
                    System.currentTimeMillis() - startTime +  " RateTask " + month + " started" );
            double percent;
            Thread.sleep( 500 );  // імітація запиту
            percent = 10.0;
            return  (1 + percent / 100.0);
        }}


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

    private final Object sumLock = new Object();

    private StringBuilder digitStr;

    private void valueWithAllDigit()
    {
        digitStr = new StringBuilder();
        for (int i = 0; i <= 9; i++) {
            new Thread(new Digit(Integer.toString(i))).start();
        }
    }

    private  class Digit implements Runnable {
        private final String digit;

        public Digit(String digit) {
            this.digit = digit;
        }

        @Override
        public void run() {
            System.out.println("added " + digit + " started");
            try{
                Thread.sleep(500); // імітація запиту
            }
            catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
                return;
            }
            synchronized (sumLock) {
                digitStr.append(digit);
                System.out.println("added " + digit + ": " + digitStr.toString() + " finished");
            }
        }
}
    private void percentDemo()
    {
        sum = 100.0;
        for (int i = 1; i <= 12; i++) {
            new Thread(new Rate(i)).start();
        }
    }

    private  class Rate implements Runnable {
        private final int month;

        public Rate(int month) {
            this.month = month;
        }

        @Override
        public void run() {
            System.out.println("Rate " + month + " started");
            double percent;
            try{
                Thread.sleep(500); // імітація запиту
                percent = 10.0;
            }
            catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
                return;
            }
            synchronized (sumLock) {
                sum = sum * (1 + percent / 100.0);
                System.out.println("Rate " + month + " finished with sum " + sum);
            }
        }
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

  Задачі, які вигідно вирішувати в асинхронному режимі, це "переставні"
  задачі, в яких порядок врахування їх частин не грає ролі. Наприклад, задачі
  додавання чи множення чисел.
  Приклад:
  Нац. банк публікує відсоткові значення інфляції кожен місяць. Необхідно
  визначити річну інфляцію.
  ? чи можна враховувати відсотки у довільному порядку?
  (100 + 10%) + 20% =?= (100 + 20%) + 10%
  (100 x 1.1) x 1.2 =?= (100 x 1.2) x 1.1
  100 x 1.1 x 1.2 =!= 100 x 1.2 x 1.1
  Так, можна. Зауваження - при врахуванні відсотків 5-го місяця ми не
  гарантуємо, що це інфляція на 5-й місяць, гарантується лише загальний
  результат після врахування всіх складових.


  Д.З. Згенерувати число(рядок), що містить всі цифри від 0 до 9
  один з раз кожну в довільному порядку. Використати асинхронний підхід.
  за якого кожну цифру додає окремий потік.
*/