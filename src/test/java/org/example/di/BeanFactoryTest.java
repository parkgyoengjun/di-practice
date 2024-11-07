package org.example.di;

import org.assertj.core.api.Assertions;
import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;



class BeanFactoryTest {
    private Reflections reflections;
    private BeanFactory beanFactory;

    /*
       탑다운 방식
       - 구현은 하지않았지만 미리 있다고 가정하고 코드를 작성하는 방식
     */

    @BeforeEach // setupmethod - 테스트 메소드가 호출되기전에 호출되는 메서드
    void setUp() {  // reflections 와 beanFactory 부분을 초기화해 주는 메서드
        reflections = new Reflections("org.example");
        // basepackage( org.example 밑에 있는 클래스들을 대상으로 Reflection 기술을 사용한다는 의미 )
        // 리플렉션(reflection)은 기본적으로 Java에서 클래스의 정보를 효율적으로 얻기 위해 개발된 기법입니다.

        Set<Class<?>> preInstantiatedClazz = getTypesAnnotatedWith(Controller.class, Service.class);
                                                                // UserController  ,  UserService

        beanFactory = new BeanFactory(preInstantiatedClazz);
    }

    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        // Class<? extends Annotation>... annotations   -> annotation 객체타입이 몇개 들어올지 모르니 여러개 받을수있게(...) 만든것

        Set<Class<?>> beans = new HashSet<>();
        for (Class<? extends Annotation> annotation : annotations) {
            // org.example 밑에 있는 클래스중 annotation 이 붙은 클래스 타입 객체를 조회하라는 의미( Controller, Service 순으로 )
            beans.addAll(reflections.getTypesAnnotatedWith(annotation)); // 조회하고 beans 에 추가
        }
        return beans; // beans 에 추가된 annotation 을 리턴
    }

    @Test
    void diTest() {

        UserController userController = beanFactory.getBean(UserController.class);

        Assertions.assertThat(userController).isNotNull();
        Assertions.assertThat(userController.getUserService()).isNotNull();
    }
}