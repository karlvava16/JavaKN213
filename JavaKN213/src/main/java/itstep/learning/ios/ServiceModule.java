package itstep.learning.ios;

import com.google.inject.AbstractModule;
import itstep.learning.services.HashService;
import itstep.learning.services.Md5HashService;

public class ServiceModule  extends AbstractModule {
    @Override
    protected void configure() {
        bind(HashService.class).to(Md5HashService.class);
    }


    /*
    Moдyль peєcтpaції cepвіcів
*/
}
