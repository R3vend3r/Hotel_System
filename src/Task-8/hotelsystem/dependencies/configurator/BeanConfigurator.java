package hotelsystem.dependencies.configurator;

import org.reflections.Reflections;

public interface BeanConfigurator {
    Reflections getScanner();
    <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass);
    <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass, String variant);
}