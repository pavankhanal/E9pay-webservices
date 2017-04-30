package com.e9pay.e9pay.api.mapper;

import java.util.Map;

/**
 * This type should be used in conjunction with {@link com.fasterxml.jackson.annotation.JsonView} to include name and identifier fields during Jackson
 * JSON serialization.  The {@link com.e9pay.e9pay.web.controller.BaseRestController} will use this view to determine which fields to send back to
 * the client when the summary of an entity is requested.
 * <p>
 * Example:
 * <pre>
 *     public class Nintendo64Dto extends NintendoDto {
 *         {@literal @}JsonView(Summary.class)
 *         private Integer id;
 *
 *         {@literal @}JsonView(Summary.class)
 *         private String name;
 *
 *         private boolean fieldX;
 *         ...
 *     }
 * </pre>
 *
 * @author Vivek Adhikari
 * @see com.e9pay.e9pay.web.controller.BaseRestController#findAllDetail(Integer, Integer, Map, String) (Integer, Integer, java.utils.Map, String)
 * @see com.fasterxml.jackson.annotation.JsonView
 */
public interface Summary {

}
