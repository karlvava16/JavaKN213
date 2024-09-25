package itstep.learning.async;

public class AsyncDemo {
    public void run() {
        System.out.println("Async demo");
        // ThreadDemo();
        percentDemo();
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

    private void percentDemo()
    {
        for (int i = 1; i <= 12; i++) {
            new Thread(new Rate(i)).start();
        }
    }

    private static class Rate implements Runnable {
        private final int month;

        public Rate(int month) {
            this.month = month;
        }

        @Override
        public void run() {
            System.out.println("Rate " + month + " started");
            try{
                Thread.sleep(500); // імітація запиту
            }
            catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
            System.out.println("Rate " + month + " finished");
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
*/