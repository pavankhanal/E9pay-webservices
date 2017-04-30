package com.e9pay.e9pay.api.mapper;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.e9pay.e9pay.api.utils.BeanPropertyUtils;

/**
 * This type represents a collection of {@link PropertyMapping} objects that are associated with a specific source and target type.  This container
 * allows for a set of source properties to be mapped to a set of target properties without any other form of coupling.
 *
 * @author Vivek Adhikari
 * @see PropertyMapping
 * @see AnnotationPropertyMappingStrategy
 * @see TargetProperty
 */
@ToString
public class PropertyMappings implements Iterable<PropertyMapping> {

    // An internal cache for storing FieldMapping meta-data once its been computed.
    private static final Map<CacheKey, PropertyMappings> mappingCache = new ConcurrentHashMap<>();

    private final SortedMap<String, PropertyMapping> sourceTargetMappings;

    // Use the static factory methods for construction.
    private PropertyMappings() {
        sourceTargetMappings = new TreeMap<>();
    }

    /**
     * @return A new instance without any mappings.
     */
    public static PropertyMappings create() {
        return new PropertyMappings();
    }

    /**
     * Creates an instance based on a template class with the mapping being performed in the default direction.  The template is expected to follow
     * well-behaved JavaBean conventions with a public no-arg constructor and fields exposed as getters and setters.  By default, all of the
     * template's POJO properties will be mapped by name to properties with the same name.  Properties may override these mappings by specifying the
     * {@link TargetProperty} annotation at the field or getter/setter method level to provide customized mapping behavior.
     *
     * @param templateType
     *     The class which contains properties annotated with {@link TargetProperty}.
     *
     * @return A fully initialized instance of mappings.
     */
    public static PropertyMappings create(Class<?> templateType) {
        return create(templateType, MappingDirection.DEFAULT);
    }

    /**
     * Creates an instance based on a template class.  The template is expected to follow well-behaved JavaBean conventions with a public no-arg
     * constructor and fields exposed as getters and setters.  By default, all of the template's POJO properties will be mapped by name to
     * properties with the same name.  Properties may override these mappings by specifying the {@link TargetProperty} annotation at the field or
     * getter/setter method level to provide customized mapping behavior.
     *
     * @param templateType
     *     The class which contains properties annotated with {@link TargetProperty}.
     * @param mappingDirection
     *     The direction in which the fields should be mapped, i.e. source -> target or target -> source.  This will have an impact on how the {@link
     *     TargetProperty} annotation fields are interpreted.
     *
     * @return A fully initialized instance of mappings.
     */
    public static PropertyMappings create(Class<?> templateType, MappingDirection mappingDirection) {
        return create(new AnnotationPropertyMappingStrategy(templateType, mappingDirection));
    }

    /**
     * Create an instance of mappings and populate it based on a custom {@link PropertyMappingStrategy}.  This method can be used to implement custom
     * mapping behavior of fields between a series of source and target paths.
     * <p>
     * Custom strategies supplied to this method MUST be immutable and have well-behaved equals and hashCode implementations.
     *
     * @param strategy
     *     The strategy to apply when populating the instance.
     *
     * @return A fully initialized instance of mappings.
     */
    public static PropertyMappings create(PropertyMappingStrategy strategy) {
        final CacheKey cacheKey = new CacheKey(strategy);

        if (!mappingCache.containsKey(cacheKey)) {

            final PropertyMappings propertyMappings = new PropertyMappings();

            strategy.populate(propertyMappings, null, null);

            mappingCache.put(cacheKey, propertyMappings);
        }

        return mappingCache.get(cacheKey);
    }

    /**
     * Convert a source object into a new instance of the {@code targetClass}.  The mapping is specified by the supplied {@code templateType}
     * parameter.  This method assumes that the types on the target object are assignable from the corresponding types on the source object.
     *
     * @param source
     *     The source object to map to the target object.
     * @param targetClass
     *     The type of the newly created target class.
     * @param <S>
     *     The source type parameter.
     * @param <T>
     *     The target type parameter.
     *
     * @return The newly created target object.
     *
     * @throws PropertyMappingException
     *     When the fields cannot be mapped because the types are not convertable or the meta-data mapping is incorrect.
     */
    public <S, T> T map(S source, Class<T> targetClass) {
        T target = null;
        try {
            target = targetClass.getConstructor().newInstance();
            return map(source, target);
        } catch (Exception ex) {
            throw new PropertyMappingException("Could not map fields from " + source + " to " + target, ex);
        }
    }

    /**
     * Map the specified fields from the source object onto the target object.  This method assumes that the types on the target object are
     * assignable from the corresponding types on the source object.
     *
     * @param source
     *     The source object as referred to by the meta-data.
     * @param target
     *     The target object as referred to by the meta-data.
     * @param <S>
     *     The source type parameter.
     * @param <T>
     *     The target type parameter.
     *
     * @return The updated target object.  This is the same reference as {@code target}.
     *
     * @throws PropertyMappingException
     *     When the fields cannot be mapped because the types are not convertable or the meta-data mapping is incorrect.
     */
    public <S, T> T map(S source, T target) {

        for (PropertyMapping propertyMapping : sourceTargetMappings.values()) {
            final String sourceProperty = propertyMapping.getSourceProperty();
            final String targetProperty = propertyMapping.getTargetProperty();
            final boolean readOnly = propertyMapping.isReadOnly();

            if (!readOnly) {
                try {
                    BeanPropertyUtils.setNestedProperties(source, sourceProperty, target, targetProperty);
                } catch (Exception ex) {
                    throw new PropertyMappingException(String.format(
                        "Could not map field value from Source:\n\n[%s]\n\nTarget:\n\n[%s]\n\nMapping [%s].\n\n",
                        source,
                        target,
                        propertyMapping
                    ), ex);
                }
            }
        }

        return target;
    }

    /**
     * Insert a mapping of source to target mappings in read-only mode, discarding any existing mappings that contain intermediate nodes for the
     * supplied nodes.
     *
     * @param source
     *     The source path to map.
     * @param target
     *     The target path to map.
     *
     * @return The 'this' instance.
     */
    public PropertyMappings put(String source, String target) {
        put(source, target, true);
        return this;
    }

    /**
     * Insert a mapping of source to target mappings, discarding any existing mappings that contain intermediate nodes for the supplied nodes.
     *
     * @param source
     *     The source path to map.
     * @param target
     *     The target path to map.
     * @param readOnly
     *     If the mapping should be read-only.
     *
     * @return The 'this' instance.
     */
    public PropertyMappings put(String source, String target, boolean readOnly) {

        // Only terminal nodes can be kept in the map.
        int index = source.lastIndexOf('.');

        if (index > -1) {
            String parent = source.substring(0, index);

            if (sourceTargetMappings.containsKey(parent)) {
                sourceTargetMappings.remove(parent);
            }
        }

        boolean put = true;
        for (String existing : sourceTargetMappings.keySet()) {
            if (existing.startsWith(source + ".")) {
                put = false;
                break;
            }
        }

        if (put) {
            sourceTargetMappings.put(source, new PropertyMapping(source, target, readOnly));
        }

        return this;
    }

    /**
     * Retrieve a {@link PropertyMapping} based on its sourcePath path.
     *
     * @param sourcePath
     *     The sourcePath path to query.
     *
     * @return The {@link PropertyMapping} associated with the sourcePath.
     */
    public PropertyMapping get(String sourcePath) {
        return sourceTargetMappings.get(sourcePath);
    }

    /**
     * @return True if the supplied sourcePath has been mapped.
     */
    public boolean containsSource(String sourcePath) {
        return sourceTargetMappings.containsKey(sourcePath);
    }

    @Override
    public Iterator<PropertyMapping> iterator() {
        return sourceTargetMappings.values().iterator();
    }

    /**
     * An internal type to be used as the key for the {@link PropertyMappings} cache.
     */
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class CacheKey {

        private PropertyMappingStrategy strategy;
    }
}
