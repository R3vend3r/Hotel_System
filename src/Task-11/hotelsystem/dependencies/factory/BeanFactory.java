package hotelsystem.dependencies.factory;

import hotelsystem.dependencies.annotation.*;
import hotelsystem.dependencies.config.*;
import hotelsystem.dependencies.config.ConfigLoader;
import hotelsystem.dependencies.configurator.*;
import hotelsystem.dependencies.context.AppContext;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

public class BeanFactory {
    private final Configuration configuration;
    @Getter
    private final BeanConfigurator beanConfigurator;
    private final AppContext appContext;
    private final ConfigLoader configLoader;

    public BeanFactory(AppContext applicationContext) {
        this.configuration = new JavaConfiguration();
        this.beanConfigurator = new JavaBeanConfigurator(
                configuration.getPackageToScan(),
                configuration.getInterfaceToImplementation()
        );
        this.appContext = applicationContext;
        this.configLoader = new ConfigLoader();
    }

    @SneakyThrows
    public <T> T getBean(Class<T> clazz) {
        Class<? extends T> implementationClass = clazz.isInterface()
                ? beanConfigurator.getImplementationClass(clazz)
                : clazz;

        if (appContext.getBeanMap().containsKey(implementationClass)) {
            return (T) appContext.getBeanMap().get(implementationClass);
        }

        T bean = createInstance(implementationClass);
        appContext.getBeanMap().put(implementationClass, bean);

        injectDependencies(bean);

        processConfigProperties(bean);

        invokePostConstruct(bean);

        return bean;
    }

    @SneakyThrows
    private <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No default constructor found for " + clazz.getName(), e);
        }
    }

    private <T> void injectDependencies(T bean) throws Exception {
        for (Field field : getInjectableFields(bean.getClass())) {
            field.setAccessible(true);

            Object dependency = resolveDependency(field);
            field.set(bean, dependency);
        }
    }

    private List<Field> getInjectableFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
    }

    private Object resolveDependency(Field field) {
        Variant variant = field.getAnnotation(Variant.class);
        Class<?> dependencyType = field.getType();

        if (variant != null) {
            Class<?> implClass = beanConfigurator.getImplementationClass(dependencyType, variant.value());
            return appContext.getBean(implClass);
        }

        return dependencyType.isInterface()
                ? appContext.getBean(beanConfigurator.getImplementationClass(dependencyType))
                : appContext.getBean(dependencyType);
    }

    private <T> void processConfigProperties(T bean) throws Exception {
        for (Field field : getConfigPropertyFields(bean.getClass())) {
            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
            String configFile = annotation.configFileName();
            String propertyKey = annotation.propertyName().isEmpty()
                    ? bean.getClass().getSimpleName() + "." + field.getName()
                    : annotation.propertyName();

            String value = configLoader.getProperty(configFile, propertyKey);
            if (value != null) {
                Object convertedValue = convertValue(value, field.getType(), annotation.type());
                field.setAccessible(true);
                field.set(bean, convertedValue);
            }
        }
    }

    private List<Field> getConfigPropertyFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ConfigProperty.class))
                .collect(Collectors.toList());
    }

    private Object convertValue(String value, Class<?> fieldType, Class<?> targetType) {
        Class<?> conversionType = targetType != String.class ? targetType : fieldType;

        if (conversionType.isArray()) {
            return convertArray(value, conversionType.getComponentType());
        }
        if (Collection.class.isAssignableFrom(conversionType)) {
            return convertCollection(value, resolveCollectionType(fieldType), resolveGenericType(fieldType));
        }

        if (conversionType.isEnum()) {
            return Enum.valueOf((Class<? extends Enum>) conversionType, value);
        }

        return convertSingleValue(value, conversionType);
    }

    private Object convertArray(String value, Class<?> elementType) {
        String[] parts = value.split(";");
        Object array = Array.newInstance(elementType, parts.length);
        for (int i = 0; i < parts.length; i++) {
            Array.set(array, i, convertSingleValue(parts[i].trim(), elementType));
        }
        return array;
    }

    private Collection<?> convertCollection(String value, Class<?> collectionType, Class<?> elementType) {
        String[] parts = value.split(";");
        Collection<Object> collection = createCollectionInstance(collectionType);

        for (String part : parts) {
            collection.add(convertSingleValue(part.trim(), elementType));
        }

        return collection;
    }

    private Collection<Object> createCollectionInstance(Class<?> collectionType) {
        if (List.class.isAssignableFrom(collectionType)) {
            return new ArrayList<>();
        }
        if (Set.class.isAssignableFrom(collectionType)) {
            return new HashSet<>();
        }
        return new ArrayList<>();
    }

    private Class<?> resolveCollectionType(Class<?> fieldType) {
        if (List.class.isAssignableFrom(fieldType)) return List.class;
        if (Set.class.isAssignableFrom(fieldType)) return Set.class;
        return Collection.class;
    }

    private Class<?> resolveGenericType(Class<?> fieldType) {
        return String.class;
    }

    private Object convertSingleValue(String value, Class<?> type) {
        if (type == String.class) return value;
        if (type == Integer.class || type == int.class) return Integer.parseInt(value);
        if (type == Boolean.class || type == boolean.class) return Boolean.parseBoolean(value);
        if (type == Double.class || type == double.class) return Double.parseDouble(value);
        if (type == Long.class || type == long.class) return Long.parseLong(value);
        if (type == Float.class || type == float.class) return Float.parseFloat(value);
        if (type == Short.class || type == short.class) return Short.parseShort(value);
        if (type == Byte.class || type == byte.class) return Byte.parseByte(value);
        if (type == Character.class || type == char.class) return value.charAt(0);

        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }

    @SneakyThrows
    private void invokePostConstruct(Object bean) {
        Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(PostConstruct.class))
                .forEach(method -> {
                    method.setAccessible(true);
                    try {
                        method.invoke(bean);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}