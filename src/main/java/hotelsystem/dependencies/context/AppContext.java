package hotelsystem.dependencies.context;

import hotelsystem.dependencies.factory.BeanFactory;
import hotelsystem.dependencies.postprocessor.BeanPostProcessor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppContext {

    @Setter
    private BeanFactory beanFactory;

    @Getter
    private final Map<Class, Object> beanMap = new ConcurrentHashMap<>();

    public  AppContext() {
    }

    public <T> T getBean(Class<T> clazz) {
        if (beanMap.containsKey(clazz)) {
            return (T) beanMap.get(clazz);
        }

        T bean = beanFactory.getBean(clazz);
        callPostProcessors(bean);
        beanMap.put(clazz, bean);

        return bean;
    }

    @SneakyThrows
    private void callPostProcessors(Object bean) {
        for (Class processor : beanFactory.getBeanConfigurator().getScanner()
                .getSubTypesOf(BeanPostProcessor.class)) {
            BeanPostProcessor postProcessor = (BeanPostProcessor) processor.getDeclaredConstructor().newInstance();
            postProcessor.process(bean);
        }
    }
}