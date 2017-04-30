package com.e9pay.e9pay.api.mapper;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * Mapper class to register different module needed to add in default Object Mapper
 *
 * @author Vivek Adhikari
 * @since 4/21/2017
 */
public class HibernateAwareObjectMapper extends ObjectMapper {

    public HibernateAwareObjectMapper() {
        registerModule(new Hibernate4Module());
        registerModule(new E9PayJodaTimeModule());
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
    }

    public void setPrettyPrint(boolean prettyPrint) {
        configure(SerializationFeature.INDENT_OUTPUT, prettyPrint);
    }
}
