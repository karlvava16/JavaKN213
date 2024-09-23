package itstep.learning.oop.annotations;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Required {
    String value() default "";
    boolean isAlternative() default true;
}
