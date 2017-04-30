package com.e9pay.e9pay.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.e9pay.e9pay.api.core.EntityService;
import com.e9pay.e9pay.api.core.Identifiable;
import com.e9pay.e9pay.api.mapper.PropertyMappings;
import com.e9pay.e9pay.api.mapper.Summary;
import com.e9pay.e9pay.api.utils.FieldOperation;
import com.e9pay.e9pay.api.utils.FieldOperations;
import com.e9pay.e9pay.api.utils.QueryCriteria;
import com.e9pay.e9pay.web.exception.RestSecurityException;
import com.e9pay.e9pay.web.exception.RestValidationException;
import com.e9pay.e9pay.web.mapper.DtoMapper;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class should be used as the base for creating any new REST-based controlllers.  It provides the most commonly required out-of-the-box
 * features:
 * <ul>
 * <li>Standard REST operations for GET, PUT, POST and DELETE</li>
 * <li>Straight-forward payload mapping between business entities and data-transfer objects</li>
 * <li>Windowing, sorting and filtering functions for search APIs</li>
 * </ul>
 *
 * @author Vivek Adhikari
 */
public abstract class BaseRestController<E extends Identifiable, D extends Identifiable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRestController.class);

    private ConversionService conversionService;

    @Autowired
    @Qualifier("restConversionService")
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * Method to delete an entity with the supplied ID.
     * <p>
     * If an entity with the supplied ID can be found and deleted, the entity as it was right before the delete will be returned with a status of
     * {@link org.springframework.http.HttpStatus#OK}
     * <p>
     * If an entity with the supplied ID cannot be found, a status of {@link org.springframework.http.HttpStatus#NO_CONTENT} will be returned.
     *
     * @param id
     *     The ID of the entity to be deleted.
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public final ResponseEntity<Payload<D>> delete(@PathVariable Long id) {
        if (!hasDeleteAuthority()) {
            throw new RestSecurityException("You do not have permission to perform that action.");
        }

        // validate the path variable input.  this needs to be done manually since annotation validations do not work on these types of variables.
        if (id == null) {
            throw RestValidationException.withIssue("id", "The ID must be provided.");
        }

        return doDelete(id);
    }

    /**
     * Method to delete all entities
     * <p>
     * <b>This option is not allowed and will return a {@link org.springframework.http.HttpStatus#NOT_IMPLEMENTED} error.</b>
     * <p>
     */
    @RequestMapping(method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public final ResponseEntity<Payload<D>> deleteAll() {
        throw new UnsupportedOperationException("An ID must be provided.");
    }

    /**
     * Method to patch all entities
     * <p>
     * <b>This option is not allowed and will return a {@link org.springframework.http.HttpStatus#NOT_IMPLEMENTED} error.</b>
     * <p>
     */
    @RequestMapping(method = RequestMethod.PATCH, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public final ResponseEntity<Payload<D>> patchAll() {
        throw new UnsupportedOperationException("An ID must be provided.");
    }

    /**
     * Delete callback which can be overridden in subclasses to provide custom endpoint behavior without breaking the {@link RequestMapping}
     * contract.
     */
    protected ResponseEntity<Payload<D>> doDelete(Long id) {

        final E deletedEntity = getEntityService().delete(id);

        if (deletedEntity == null) {
            return Response.noContent();
        }

        return Response.ok(getDtoMapper().toDto(deletedEntity));
    }

    /**
     * This endpoint will return a count of matching entities
     *
     * @param filter
     *     The request parameters used to filter the query.
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity count(@RequestParam(required = false) Map<String, String> filter) {

        if (!hasFindAuthority()) {
            throw new RestSecurityException("You do not have permission to perform that action.");
        }

        return doCount(filter);
    }

    @SuppressWarnings("WeakerAccess")
    protected ResponseEntity doCount(@RequestParam(required = false) Map<String, String> filter) {
        final DtoMapper<E, D> dtoMapper = getDtoMapper();

        final QueryCriteria criteria = QueryCriteria.create(createFieldOperations(dtoMapper, filter, null), 1, 1);

        final Long count = getEntityService().getCount(criteria);

        return Response.ok(count);
    }

    /**
     * This endpoint will return all matching entities via their DTO representation.  The returned objects contain the full object graph.
     * <p>
     * If entities exist, all entities will be returned with a status of HttpStatus.OK<br/>
     * If no entities exist, an empty list with a status of HttpStatus.OK will be returned.<br/>
     *
     * @param page
     *     the page to retrieve
     * @param pageSize
     *     the size of the page to return
     * @param filter
     *     The request parameters used to filter the query.
     * @param sort
     *     The sort specification for the query.
     */
    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE, params = "!summary")
    @ResponseBody
    public final ResponseEntity<Payload<List<D>>> findAllDetail(
        @RequestParam(required = false, defaultValue = "1") Integer page,
        @RequestParam(required = false) Integer pageSize,
        @RequestParam(required = false) Map<String, String> filter,
        @RequestParam(required = false) String sort
    ) {
        if (!hasFindAuthority()) {
            throw new RestSecurityException("You do not have permission to perform that action.");
        }

        return doFindAll(page, pageSize, filter, sort);
    }

    /**
     * This endpoint will return all matching entities via their DTO representation.
     * <p>
     * If entities exist, all entities will be returned with a status of HttpStatus.OK<br/>
     * If no entities exist, an empty list with a status of HttpStatus.OK will be returned.<br/>
     *
     * @param page
     *     the page to retrieve
     * @param pageSize
     *     the size of the page to return
     * @param filter
     *     The request parameters used to filter the query.
     * @param sort
     *     The sort specification for the query.
     */
    @JsonView(Summary.class)
    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE, params = "summary")
    @ResponseBody
    public final ResponseEntity<Payload<List<D>>> findAllSummary(
        @RequestParam(required = false, defaultValue = "1") Integer page,
        @RequestParam(required = false) Integer pageSize,
        @RequestParam(required = false) Map<String, String> filter,
        @RequestParam(required = false) String sort
    ) {
        if (!hasFindAuthority()) {
            throw new RestSecurityException("You do not have permission to perform that action.");
        }

        return doFindAll(page, pageSize, filter, sort);
    }

    /**
     * Find All callback which can be overridden in subclasses to provide custom endpoint behavior without breaking the {@link RequestMapping}
     * contract.
     */
    @SuppressWarnings("WeakerAccess")
    protected ResponseEntity<Payload<List<D>>> doFindAll(Integer page, Integer pageSize, Map<String, String> filter, String sort) {

        final DtoMapper<E, D> dtoMapper = getDtoMapper();

        final QueryCriteria criteria = QueryCriteria.create(createFieldOperations(dtoMapper, filter, sort), page, pageSize);

        final List<E> entities = getEntityService().findAll(criteria);

        final List<D> results = new ArrayList<>();

        for (E entity : entities) {
            results.add(dtoMapper.toDto(entity));
        }

        return Response.ok(results, page, pageSize);
    }

    @SuppressWarnings("WeakerAccess")
    protected FieldOperations createFieldOperations(DtoMapper<E, D> dtoMapper, Map<String, String> filter, String sort) {

        final PropertyMappings mappings = dtoMapper.getSourceTargetPropertyMappings();

        final RestControllerConversionCallback callback = new RestControllerConversionCallback(
            conversionService,
            dtoMapper.getEntityClass()
        );

        final List<FieldOperation> filterOps = FieldOperation.createFromFilter(
            filter,
            mappings,
            callback
        );

        if (callback.hasIssues()) {
            throw new RestValidationException(callback.getIssues());
        }

        final List<FieldOperation> sortOps = FieldOperation.createFromSort(sort, mappings);

        return FieldOperations.create(filterOps, sortOps);
    }

    /**
     * Method to get an entity with the supplied id<br/>
     * If an entity with the supplied id can be found, the entity will be returned with a status of HttpStatus.OK<br/>
     * If an entity with the supplied id cannot be found, a status of HttpStatus.NO_CONTENT will be returned.<br/>
     *
     * @param id
     *     the id of the entity to be retrieved
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public final ResponseEntity<Payload<D>> find(@PathVariable Long id) {
        if (!hasFindAuthority()) {
            throw new RestSecurityException("You do not have permission to perform that action.");
        }

        return doFind(id);
    }

    /**
     * Find callback which can be overridden in subclasses to provide custom endpoint behavior without breaking the {@link RequestMapping}
     * contract.
     */
    @SuppressWarnings("WeakerAccess")
    protected ResponseEntity<Payload<D>> doFind(Long id) {

        if (id == null) {
            throw RestValidationException.withIssue("id", "The ID must be provided");
        }

        final E entity = getEntityService().findById(id);

        if (entity == null) {
            return Response.noContent();
        }

        return Response.ok(getDtoMapper().toDto(entity));
    }

    /**
     * Method to insert an entity with data in the request body<br/>
     *
     * @param dto
     *     the entity as it should be inserted into the database.
     */
    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public final ResponseEntity<Payload<D>> create(@RequestBody D dto, HttpServletRequest request, UriComponentsBuilder builder) {
        if (!hasCreateAuthority()) {
            throw new RestSecurityException("You do not have permission to perform that action.");
        }

        return doCreate(dto, request, builder);
    }

    /**
     * Method to insert an entity with an ID in the URL
     * <p>
     * <b>This option is not allowed and will return a {@link org.springframework.http.HttpStatus#NOT_IMPLEMENTED} error.</b>
     * <p>
     */
    @RequestMapping(method = RequestMethod.POST, value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public final ResponseEntity<Payload<D>> createWithId(@SuppressWarnings("unused") @PathVariable Long id) {
        throw new UnsupportedOperationException("Entities cannot be created with an ID in the URL");
    }

    /**
     * Create callback which can be overridden in subclasses to provide custom endpoint behavior without breaking the {@link RequestMapping}
     * contract.
     */
    protected ResponseEntity<Payload<D>> doCreate(D dto, HttpServletRequest request, UriComponentsBuilder builder) {

        final DtoMapper<E, D> dtoMapper = getDtoMapper();

        final E entity = dtoMapper.toEntity(dto);

        final E inserted = getEntityService().saveOrUpdate(entity);

        return Response.ok(dtoMapper.toDto(inserted), buildHeaders(inserted, request, builder));
    }

    /**
     * Method to update an entity with the supplied id with data in the request body<br/>
     *
     * @param dto
     *     the entity as it should be updated in the database.
     * @param id
     *     the id of the entity to be updated
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public final ResponseEntity<Payload<D>> update(
        @RequestBody D dto,
        @PathVariable Long id,
        HttpServletRequest request,
        UriComponentsBuilder builder
    ) {
        if (!hasUpdateAuthority()) {
            throw new RestSecurityException("You do not have permission to perform that action.");
        }

        if (id == null) {
            throw RestValidationException.withIssue("id", "The ID must be provided.");
        }

        return doUpdate(dto, id, request, builder);
    }

    /**
     * Update callback which can be overridden in subclasses to provide custom endpoint behavior without breaking the {@link RequestMapping}
     * contract.
     */
    @SuppressWarnings("WeakerAccess")
    protected ResponseEntity<Payload<D>> doUpdate(
        D dto,
        Long id,
        HttpServletRequest request,
        UriComponentsBuilder builder
    ) {

        // We need to work around the fact that different entities have different identifier types.
        final Object dtoId = dto.getId();
        long longId = -1;

        //noinspection ChainOfInstanceofChecks
        if (dtoId instanceof Integer) {
            longId = (Integer) dtoId;
        }
        if (dtoId instanceof Long) {
            longId = (Long) dtoId;
        }

        if (!id.equals(longId)) {
            throw RestValidationException.withIssue(
                "id",
                String.format(
                    "The ID in the request [%s: %s] does not match the ID on the URL [%s: %s].",
                    dto.getId().getClass(),
                    dto.getId(),
                    id.getClass(),
                    id
                )
            );
        }

        final DtoMapper<E, D> dtoMapper = getDtoMapper();

        final E entity = dtoMapper.toEntity(dto);

        final E savedEntity = getEntityService().saveOrUpdate(entity);

        if (savedEntity == null) {
            return Response.noContent();
        }

        return Response.ok(dtoMapper.toDto(savedEntity), buildHeaders(savedEntity, request, builder));
    }

    /**
     * Method to update an entity with the supplied id with data in the request body<br/>
     *
     * @param dtos
     *     the entity as it should be updated in the database.
     */
    @RequestMapping(method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public final ResponseEntity<Payload<List<D>>> updateList(@RequestBody List<D> dtos) {
        if (!hasUpdateAuthority()) {
            throw new RestSecurityException("You do not have permission to perform that action.");
        }

        return doUpdateList(dtos);
    }

    /**
     * Update list callback which can be overridden in subclasses to provide custom endpoint behavior without breaking the {@link RequestMapping}
     * contract.
     */
    @SuppressWarnings("WeakerAccess")
    protected ResponseEntity<Payload<List<D>>> doUpdateList(List<D> dtos) {
        final DtoMapper<E, D> dtoMapper = getDtoMapper();

        final List<E> entitiesToUpdate = new ArrayList<>();

        // convert the dtos to entities
        for (D dto : dtos) {
            entitiesToUpdate.add(dtoMapper.toEntity(dto));
        }

        // save all of the entities as a single batch in a single transaction
        final List<E> savedEntities = getEntityService().saveOrUpdate(entitiesToUpdate);

        // convert all the saved entities back to dtos
        final List<D> updatedItems = new ArrayList<>();
        for (E entity : savedEntities) {
            updatedItems.add(dtoMapper.toDto(entity));
        }

        return Response.ok(updatedItems);
    }

    /**
     * This method will construct any standard headers that should be set (such as Location) for create or update operations.
     *
     * @param entity
     *     The entity object that is being operated upon.
     * @param request
     *     The current request.
     * @param builder
     *     The builder object used to create the headers.
     *
     * @return The newly
     */
    @SuppressWarnings("Duplicates")
    protected HttpHeaders buildHeaders(E entity, HttpServletRequest request, UriComponentsBuilder builder) {
        final String location = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        final UriComponents components = builder
            .path(location)
            .pathSegment(entity.getId().toString())
            .build();

        final HttpHeaders headers = new HttpHeaders();

        headers.setLocation(components.toUri());

        return headers;
    }

    /**
     * Subclasses should implement this method to provide the business service this controller will use.
     *
     * @return The appropriate {@link EntityService} subclass specific to this controller.
     */
    protected abstract EntityService<E> getEntityService();

    /**
     * Subclasses should implement this method to provide the entity to DTO mapper they require.
     *
     * @return The {@link DtoMapper} strategy that applies to this controller.
     */
    protected abstract DtoMapper<E, D> getDtoMapper();

    /**
     * Override this method in a subclass to return validate for the create authority.
     */
    protected boolean hasCreateAuthority() {
        return true;
    }

    /**
     * Override this method in a subclass to return validate for the find authority.
     */
    protected boolean hasFindAuthority() {
        return true;
    }

    /**
     * Override this method in a subclass to return validate for the update authority.
     */
    protected boolean hasUpdateAuthority() {
        return true;
    }

    /**
     * Override this method in a subclass to return validate for the delete authority.
     */
    protected boolean hasDeleteAuthority() {
        return true;
    }
}
