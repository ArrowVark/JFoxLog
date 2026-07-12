package io.github.hudsoncrisp.jfoxlog.annotation.FieldTransformation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface FieldTransformation {
    Class<?> returnedType();
    String transform();
}
