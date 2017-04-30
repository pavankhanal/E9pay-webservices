package com.e9pay.e9pay.api.mapper;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import com.e9pay.e9pay.api.utils.BeanPropertyUtils;
import com.e9pay.e9pay.api.core.TargetProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * A {@link PropertyMappingStrategy} implementation that uses the {@link TargetProperty} annotation and JavaBean semantics to map properties from a
 * POJO to another (unknown) POJO.  The strategy depends upon a template class which will be used to construct the mapping based on its exposed
 * JavaBean properties.  The template is expected to be a well-behaved JavaBean with a public no-arg constructor and proper getters and setters
 * exposed for its properties.  By default, properties on the template will be mapped to properties on the source with the same name and path in their
 * object graph.  Customized mapping behavior can be supplied via the {@link TargetProperty} annotation.
 *
 * @author Vivek Adhikari
 * @see PropertyMappingStrategy
 * @see PropertyMappings
 * @see PropertyMapping
 * @see TargetProperty
 */
@AllArgsConstructor
@EqualsAndHashCode
class AnnotationPropertyMappingStrategy implements PropertyMappingStrategy {

    private Class<?> templateType;
    private MappingDirection mappingDirection;

    /**
     * Find the annotations for a specific POJO property by scanning its getter, setter and field, in that order and stopping at the first located
     * match.
     *
     * @param parentType
     *     The type to search for the annotation.
     * @param descriptor
     *     The property descriptor to search.
     * @param annotation
     *     The annotation type to find.
     * @param <T>
     *     The parameterized {@link Annotation} type.
     *
     * @return The annotation if found, otherwise null.
     */
    private static <T extends Annotation> T getAnnotation(Class<?> parentType, PropertyDescriptor descriptor, Class<T> annotation) {
        T found = descriptor.getReadMethod().getAnnotation(annotation);

        if (found == null) {
            final Method setter = descriptor.getWriteMethod();
            if (setter != null) {
                found = setter.getAnnotation(annotation);
            }
            if (found == null) {
                final String name = descriptor.getName();

                final Field field = FieldUtils.getField(parentType, name, true);

                if (field != null) {
                    found = field.getAnnotation(annotation);
                }

                if (found == null) {

                    final Field declaredField = FieldUtils.getDeclaredField(parentType, name, true);

                    if (declaredField != null) {
                        found = declaredField.getAnnotation(annotation);
                    }
                }
            }
        }
        return found;
    }

    @Override
    public void populate(PropertyMappings mappings, String parentSourcePath, String parentTargePath) {
        populate(mappings, parentSourcePath, parentTargePath, templateType);
    }

    /**
     * This private implementation of populate recursively walks the object graph of the supplied template type and determines mappings based on the
     * properties it contains that follow proper POJO semantics.
     *
     * @param mappings
     *     The mappings to populate.
     * @param parentSourcePath
     *     The parent node's path for the source.
     * @param parentTargetPath
     *     The parent node's path for the target.
     * @param templateType
     *     The template type for this iteration, remembering that this method uses recursion.
     */
    private void populate(PropertyMappings mappings, String parentSourcePath, String parentTargetPath, Class<?> templateType) {

        final PropertyDescriptor[] templateDescriptors = PropertyUtils.getPropertyDescriptors(templateType);

        final boolean hasParentSourcePath = StringUtils.length(parentSourcePath) > 0;
        final boolean hasParentTargetPath = StringUtils.length(parentTargetPath) > 0;

        // Walk all child properties and scan for annotated properties.
        for (PropertyDescriptor templateDescriptor : templateDescriptors) {

            if (templateDescriptor.getWriteMethod() == null ||
                templateDescriptor.getReadMethod() == null) {
                // Skip properties that aren't POJO-based.
                continue;
            }

            final TargetProperty targetPropertyAnn = getAnnotation(templateType, templateDescriptor, TargetProperty.class);

            final boolean ignore = targetPropertyAnn != null && targetPropertyAnn.ignore();

            // Skip properties marked ignore.
            if (!ignore) {

                final String sourceName = templateDescriptor.getName();
                final String targetName = (targetPropertyAnn != null && StringUtils.isNotBlank(targetPropertyAnn.value()))
                    ? targetPropertyAnn.value()
                    : sourceName;
                boolean readOnly = targetPropertyAnn != null && targetPropertyAnn.readOnly();

                final String newSourcePath = hasParentSourcePath ? parentSourcePath + "." + sourceName : sourceName;
                final String newTargetPath = hasParentTargetPath ? parentTargetPath + "." + targetName : targetName;

                if (MappingDirection.DEFAULT.equals(mappingDirection)) {
                    mappings.put(newSourcePath, newTargetPath, readOnly);
                } else {
                    // We're never read-only when the relationship is inverse.
                    mappings.put(newTargetPath, newSourcePath, false);
                }

                // If this is a collection type, we actually need to skip it and move down to the type of
                // items being stored in the collection.
                Class<?> newSourceType = templateDescriptor.getPropertyType();

                if (!BeanPropertyUtils.isSingleValueType(newSourceType)) {
                    newSourceType = BeanPropertyUtils.parameterizedReturnType(templateDescriptor);
                }

                // Recurse into child properties of the current type.
                populate(mappings, newSourcePath, newTargetPath, newSourceType);
            }
        }
    }
}
