package itstep.learning.ioc;

import com.google.inject.AbstractModule;
import itstep.learning.filters.*;
import itstep.learning.services.hash.HashService;
import itstep.learning.services.hash.Md5HashService;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HashService.class).to(Md5HashService.class);
    }
}
