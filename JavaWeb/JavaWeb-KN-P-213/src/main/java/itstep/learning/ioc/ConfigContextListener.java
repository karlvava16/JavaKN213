package itstep.learning.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class ConfigContextListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new ServletModule(),
                new WebModule()
        );
    }
}
/*
IоC інверсія управління: веб-версія
Особливості
- веб-проєкт має інший життєвий цикл, ніж консольний/віконний
кожен запит оброблюється наче як перезапуск
однак, якась частина проєкту є постійною і не змінюється
при обробленнях запитів
- окрім реєстрації служб (сервісів) також бажано інжектувати
залежності і в фільтри/сервлети


Загальна схема
[створення контексту - deploy]
реєструємо залежності
[запит]
інжектуємо залежності
*/
