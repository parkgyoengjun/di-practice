package org.example.di;

import org.example.annotation.Inject;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.Set;

public class BeanFactoryUtills {

    public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
        Set<Constructor> injectedConstructors = ReflectionUtils.getAllConstructors(clazz, ReflectionUtils.withAnnotation(Inject.class));
        // 클래스타입의 모든 생성자를 가져온다. inject 애노테이션이 붙은 코드만 가져옴
        if (injectedConstructors.isEmpty()) {
            return null;
        }
        return injectedConstructors.iterator().next();
        /*
            Iterator는 이런 집합체로부터 정보를 얻어낸다고 볼 수 있다.
            자바의 컬렉션에 저장되어 있는 요소들을 읽어오는 방법의 인터페이스
            즉 쉽게 컬렉션으로부터 정보를 얻어내는 인터페이스 이다.
         */
    }
}
