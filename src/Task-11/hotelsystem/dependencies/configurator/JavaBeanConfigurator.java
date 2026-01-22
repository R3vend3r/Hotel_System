package hotelsystem.dependencies.configurator;

import hotelsystem.dependencies.annotation.Variant;
import lombok.Getter;
import org.reflections.Reflections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JavaBeanConfigurator implements BeanConfigurator {
    @Getter
    private final Reflections scanner;
    private final Map<Class<?>, Map<String, Class<?>>> interfaceToImplementation;


    public JavaBeanConfigurator(String packageToScan, Map<String, Class> manualConfig) {
        this.scanner = new Reflections(packageToScan);
        this.interfaceToImplementation = new ConcurrentHashMap<>();

        manualConfig.forEach((qualifier, impl) -> {
            for (Class<?> iface : impl.getInterfaces()) {
                interfaceToImplementation
                        .computeIfAbsent(iface, k -> new ConcurrentHashMap<>())
                        .put(qualifier, impl);
            }
        });

        for (Class<?> iface : scanner.getSubTypesOf(Object.class)) {
            if (iface.isInterface()) continue;
            for (Class<?> implemented : iface.getInterfaces()) {
                Variant qualifier = iface.getAnnotation(Variant.class);
                if (qualifier != null) {
                    interfaceToImplementation
                            .computeIfAbsent(implemented, k -> new ConcurrentHashMap<>())
                            .put(qualifier.value(), iface);
                }
            }
        }
    }

    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {
        Map<String, Class<?>> implementations = interfaceToImplementation.get(interfaceClass);
        if (implementations == null || implementations.size() != 1) {
            throw new RuntimeException("Expected exactly 1 implementation for: " + interfaceClass.getName());
        }
        return (Class<? extends T>) implementations.values().iterator().next();
    }



    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass, String qualifier) {
        Map<String, Class<?>> implementations = interfaceToImplementation.get(interfaceClass);
        if (implementations == null || !implementations.containsKey(qualifier)) {
            throw new RuntimeException("No implementation found with qualifier '" + qualifier + "' for interface " + interfaceClass.getName());
        }
        return (Class<? extends T>) implementations.get(qualifier);
    }
}