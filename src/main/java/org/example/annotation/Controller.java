package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) // TYPE : 클래스, 애노테이션, 이놈에 붙을수있는 타입이다.
@Retention(RetentionPolicy.RUNTIME) // retestion 유지기간이 runtime 동안이다.
public @interface Controller {
}
