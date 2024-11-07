package org.example.di;

import org.example.annotation.Inject;
import org.example.controller.UserController;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class BeanFactory { // 빈의 등록, 조회, 생성, 소멸 등을 관리하는 기능
    private final Set<Class<?>> preInstantiatedClazz;  // 인스턴스 변수로 만들어줌
    /*
         Set은 List와는 다르게 객체(데이터)를 중복해서 저장할 수 없다.
         또한 저장된 객체(데이터)를 인덱스로 관리하지 않기 때문에 저장 순서가 보장되지 않는다.
         수학의 집합과 비슷한 내용이다 ( Set 은 중복허용X, 순서보장X )

         리플렉션(reflection)은 기본적으로 Java에서 Class의 정보를 효율적으로 얻기 위해 개발된 기법입니다.
         ( Class<?> 또한 Reflection 때문에 쓸수있는것 )
     */
    private Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(Set<Class<?>> preInstantiatedClazz) {
        this.preInstantiatedClazz = preInstantiatedClazz;
        initialize(); // 초기화
    }

    private void initialize() { // 초기화 beans 를 초기화해주는 메서드
        for (Class<?> clazz : preInstantiatedClazz) {
            Object instance = createInstance(clazz);
            beans.put(clazz, instance);

        }
    }

    // UserController
    private Object createInstance(Class<?> clazz) {
        // 생성자 ( 왜 조회해주나면 인스턴스를 생성하기 위해서는 생성자가 필요하다
        Constructor<?> constructor = findConstructor(clazz);

        // 파라미터
        List<Object> parameters = new ArrayList<>();
        for (Class<?> typeClass : constructor.getParameterTypes()) {

            // UserService
            parameters.add(getParameterByClass(typeClass));
        }
        // 인스턴스 생성
        try {
            return constructor.newInstance(parameters.toArray());
            /*
                    ???????? 여기서 부터
             */

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /*
        private Constructor<?> findConstructor(Class<?> clazz) {
        Set<Constructor> injectedConstructors = ReflectionUtils.getAllConstructors(clazz, ReflectionUtils.withAnnotation(Inject.class));
        // 클래스타입의 모든 생성자를 가져온다. inject 애노테이션이 붙은 코드만 가져옴
        if (injectedConstructors.isEmpty()) {
            return null;
        }
        return injectedConstructors.iterator().next();
    }
     */

    private Constructor<?> findConstructor(Class<?> clazz) {
        Constructor<?> constructor = BeanFactoryUtills.getInjectedConstructor(clazz);
        /*
        Constructor<?>는 리플렉션(Reflection) API에서 사용되는 제네릭 타입으로, 특정 클래스의 생성자를 나타냅니다
         */

        if (Objects.nonNull(constructor)) {
            return constructor;
        }
        return clazz.getConstructors()[0];
        // getConstructors 는 생성자 정보를 가져온다
    }


    private Object getParameterByClass(Class<?> typeClass) {
        Object instanceBean = getBean(typeClass);
        if (Objects.nonNull(instanceBean)) {
            return instanceBean;
        }
        return createInstance(typeClass);
    }

    public <T> T  getBean(Class<T> requiredType) { // 어떤 인자를 받을지 모르기 때문에( Class<T> ) 제네릭타입으로 받게 끔 만듬 <T>
        return (T) beans.get(requiredType);
        /*
            제네릭(T) : 타입을 모르지만, 타입을 정해지면 그 타입의 특성에 맞게 사용한다.
            // 제네릭을 사용하지 않은 예
                class Box {
                    private final String content;
                    public String getContent;
                }
            // 제네릭을 사용 예
              class Box<T> {
                private final T content;
                    public T getContent {
                        return this.content;
                    }
             }
Box<String> boxOfString = new Box<String>(); // 좌변과 우변이 모두 String이면 -> 우변의 String 생략 가능
Box<Integer> boxOfInteger = new Box<Integer>();


            와일드 카드(?) : 무슨 타입인지 모르고, 무슨 타입인지 신경쓰지 않는다. 타입을 확정하지 않고 가능성을 열어둔다.
         */
    }
}
