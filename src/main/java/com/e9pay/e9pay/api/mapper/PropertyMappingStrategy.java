package com.e9pay.e9pay.api.mapper;

/**
 * A strategy interface used to implement property mapping behavior between two JavaBeans.
 * @author Vivek Adhikari
 */
public interface PropertyMappingStrategy {

    /**
     * Populates this instance based on the supplied meta-data.
     *
     * @param mappings
     *     The {@link PropertyMappings} instance to populate.
     * @param parentSourcePath
     *     The source path of the parent type.
     * @param parentTargetPath
     *     The target path of the parent type.
     */
    void populate(PropertyMappings mappings, String parentSourcePath, String parentTargetPath);
}
