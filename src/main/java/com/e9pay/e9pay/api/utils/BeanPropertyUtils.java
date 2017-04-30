package com.e9pay.e9pay.api.utils;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Utilities for working with JavaBean properties in a variety of ways not supported by libraries such as Commons Lang or BeanUtils.
 *
 * @author Vivek Adhikari
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanPropertyUtils {

    /**
     * Given the path to a property, return its type.  The path is defined using a simple dot-delimitted notation, such as "parent.child.name".
     *
     * @param sourceType
     *     The source type to located the property with.
     * @param property
     *     The period-delimited property path.
     *
     * @return The {@link Class} object representing the property.
     */
    public static Class<?> getNestedPropertyType(Class<?> sourceType, String property) {

        final String[] path = split(property, ".");

        Class<?> type = sourceType;

        for (int i = 0; i < path.length; i++) {
           final PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(type);

            type = null;

            for (PropertyDescriptor pd : descriptors) {
                final String name = pd.getName();
                if (name.equals(path[i])) {
                    type = pd.getPropertyType();

                    // If this is a collection, we introspect the actual object type in the collection.
                    if (Collection.class.isAssignableFrom(type)) {
                        type = parameterizedReturnType(pd);
                    }

                    break;
                }
            }

            if (i == path.length - 1) {
                return type;
            }
        }

        return null;
    }

    /**
     * Given two objects and two paths to sub-properties of each object, try to set the property from the source object to the property on the target
     * object without requiring that the source and target types match.  This method assumes the source, target and any intermediate types are well
     * behaved JavaBeans with appropriate getters and setters and a public no-argument constructor.
     *
     * @param source
     *     The source object.
     * @param sourcePropertyPath
     *     The path of the property on the source object.
     * @param target
     *     The target object.
     * @param targetPropertyPath
     *     The path of the property on the target object.
     *
     * @throws IllegalAccessException
     *     Thrown when a reflective operation fails.
     * @throws NoSuchMethodException
     *     Thrown when a reflective operation fails.
     * @throws InvocationTargetException
     *     Thrown when a reflective operation fails.
     * @throws InstantiationException
     *     Thrown when a reflective operation fails.
     */
    public static void setNestedProperties(Object source, String sourcePropertyPath, Object target, String targetPropertyPath) throws
        IllegalAccessException,
        NoSuchMethodException,
        InvocationTargetException,
        InstantiationException {

        final String sourceProperty = substringBefore(sourcePropertyPath, ".");
        final String targetProperty = substringBefore(targetPropertyPath, ".");

        final String sourceSubProperty = substringAfter(sourcePropertyPath, ".");
        final String targetSubProperty = substringAfter(targetPropertyPath, ".");

        final boolean hasSourceSubProperty = isNotBlank(sourceSubProperty);
        final boolean hasTargetSubProperty = isNotBlank(targetSubProperty);

        final PropertyDescriptor sourceDescriptor = PropertyUtils.getPropertyDescriptor(source, sourceProperty);
        final PropertyDescriptor targetDescriptor = PropertyUtils.getPropertyDescriptor(target, targetProperty);

        assertDescriptor(sourceDescriptor, source, sourceProperty);
        assertDescriptor(targetDescriptor, target, targetProperty);

        final Class<?> sourcePropertyType = sourceDescriptor.getPropertyType();
        final Class<?> targetPropertyType = targetDescriptor.getPropertyType();

        final Object sourceValue = get(sourceDescriptor, source);

        if (sourceValue == null) {
            set(targetDescriptor, target, null);
            return;
        }

        if (isSingleValueType(sourcePropertyType)) {
            if (!hasSourceSubProperty && !hasTargetSubProperty) {
                set(targetDescriptor, target, sourceValue);
            }

            if (hasSourceSubProperty && !hasTargetSubProperty) {
                setNestedProperties(sourceValue, sourceSubProperty, target, targetProperty);
            }

            if (!hasSourceSubProperty && hasTargetSubProperty) {
                Object targetValue = get(targetDescriptor, target);

                if (targetValue == null) {
                    targetValue = newInstance(targetPropertyType);
                    set(targetDescriptor, target, targetValue);
                }

                setNestedProperties(source, sourceProperty, targetValue, targetSubProperty);
            }

            if (hasSourceSubProperty && hasTargetSubProperty) {

                Object targetValue = get(targetDescriptor, target);

                if (targetValue == null) {
                    targetValue = newInstance(targetPropertyType);
                    set(targetDescriptor, target, targetValue);
                }

                setNestedProperties(sourceValue, sourceSubProperty, targetValue, targetSubProperty);
            }
        } else {
            final List<Object> sourceCollection = (List<Object>) sourceValue;

            final Class<?> targetCollectionElementType = parameterizedReturnType(targetDescriptor);

            if (!hasSourceSubProperty && !hasTargetSubProperty) {
                // We're at the leaf node, the type must be assignable.
                set(targetDescriptor, target, sourceCollection);
            } else {

                // Determine if the collection exists.
                List<Object> targetCollection = (List<Object>) get(targetDescriptor, target);

                if (targetCollection == null) {
                    // It doesn't exist, create a new empty collection.
                    targetCollection = new ArrayList<>();
                    set(targetDescriptor, target, targetCollection);
                }

                // Loop through the source collection and materialize it in the target.
                for (int i = 0; i < sourceCollection.size(); i++) {
                    final Object sourceCollectionElement = sourceCollection.get(i);

                    Object targetCollectionElement = null;

                    // Is there already an object at this index?
                    if (targetCollection.size() > i) {
                        targetCollectionElement = targetCollection.get(i);
                    }

                    // Create a new object at this index.
                    if (targetCollectionElement == null) {
                        targetCollectionElement = newInstance(targetCollectionElementType);
                        targetCollection.add(targetCollectionElement);
                    }

                    setNestedProperties(sourceCollectionElement, sourceSubProperty, targetCollectionElement, targetSubProperty);
                }
            }
        }
    }

    // Shortcut to call a setter method.
    private static void set(PropertyDescriptor descriptor, Object object, Object value) throws InvocationTargetException, IllegalAccessException {
        descriptor.getWriteMethod().invoke(object, value);
    }

    // Shortcut to call a getter method.
    private static Object get(PropertyDescriptor descriptor, Object object) throws InvocationTargetException, IllegalAccessException {
        return descriptor.getReadMethod().invoke(object);
    }

    /**
     * Given a {@link PropertyDescriptor}, determine the type its getter returns.
     *
     * @param descriptor
     *     The descriptor to query.
     *
     * @return The type returned by the getter method.
     */
    public static Class<?> parameterizedReturnType(PropertyDescriptor descriptor) {
        return (Class<?>) ((ParameterizedType) descriptor.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0];
    }

    private static Object newInstance(Class<?> _class) throws
        NoSuchMethodException,
        IllegalAccessException,
        InvocationTargetException,
        InstantiationException {
        return _class.getConstructor().newInstance();
    }

    private static void assertDescriptor(PropertyDescriptor descriptor, Object object, String property) {
        if (descriptor == null) {
            throw new IllegalArgumentException(String.format(
                "Cannot find target property [%s.%s] via object [%s]",
                object.getClass(),
                property,
                object
            ));
        }
    }

    public static boolean isSingleValueType(Class<?> type) {
        return !List.class.isAssignableFrom(type);
    }
}
