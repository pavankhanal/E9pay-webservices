package com.e9pay.e9pay.api.utils;

import static com.e9pay.e9pay.api.utils.FieldOperation.BETWEEN;
import static com.e9pay.e9pay.api.utils.FieldOperation.EQUALS;
import static com.e9pay.e9pay.api.utils.FieldOperation.EQUALS_OR_IS_NULL;
import static com.e9pay.e9pay.api.utils.FieldOperation.GREATER_THAN;
import static com.e9pay.e9pay.api.utils.FieldOperation.GREATER_THAN_OR_EQUALS;
import static com.e9pay.e9pay.api.utils.FieldOperation.GREATER_THAN_OR_EQUALS_OR_NULL;
import static com.e9pay.e9pay.api.utils.FieldOperation.GREATER_THAN_OR_NULL;
import static com.e9pay.e9pay.api.utils.FieldOperation.IN;
import static com.e9pay.e9pay.api.utils.FieldOperation.IS_NOT_NULL;
import static com.e9pay.e9pay.api.utils.FieldOperation.IS_NULL;
import static com.e9pay.e9pay.api.utils.FieldOperation.LESS_THAN;
import static com.e9pay.e9pay.api.utils.FieldOperation.LESS_THAN_OR_EQUALS;
import static com.e9pay.e9pay.api.utils.FieldOperation.LESS_THAN_OR_EQUALS_OR_NULL;
import static com.e9pay.e9pay.api.utils.FieldOperation.LESS_THAN_OR_NULL;
import static com.e9pay.e9pay.api.utils.FieldOperation.LIKE;
import static com.e9pay.e9pay.api.utils.FieldOperation.NOT_EQUALS;
import static com.e9pay.e9pay.api.utils.FieldOperation.NOT_EQUALS_OR_IS_NOT_NULL;
import static com.e9pay.e9pay.api.utils.FieldOperation.NOT_LIKE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 * A utility class for working with Hibernate {@link Criteria} instances.
 *
 * @author Vivek Adhikari
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaUtils {

    /**
     * Given a criteria, apply a pagination window to the pending query.
     *
     * @param criteria
     *     The criteria object to paginate.
     * @param page
     *     The page to return.
     * @param pageSize
     *     The number of records per page.
     */
    public static void applyPagination(Criteria criteria, Integer page, Integer pageSize) {

        final Integer actualPage = (page == null ? 1 : page) - 1;
        final Integer actualPageSize = (pageSize == null ? Integer.MAX_VALUE : pageSize);

        final Integer firstResult = actualPage * (pageSize == null ? 1 : actualPageSize);

        criteria
            .setFirstResult(firstResult)
            .setMaxResults(actualPageSize);
    }

    /**
     * Given a list of fully-qualified field paths, create criteria aliases for each node, being careful not to create duplicates.
     * <p>
     * For example, the following field mapping (www.xxx.yyy.zzz.field) would create these aliases:
     * <pre>
     * www -> www
     * www.xxx -> www_xxx
     * www_xxx.yyy -> www_xxx_yyy
     * www_xxx_yyy.zzz -> www_xxx_yyy_zzz
     * </pre>
     *
     * @param criteria
     *     The criteria that will be modified with the supplied field paths.
     * @param fieldPaths
     *     A {@link List} of fully-qualified field paths to create aliases for.
     */
    public static void applyQueryAliases(Criteria criteria, JoinType joinType, List<String> fieldPaths) {

        if (CollectionUtils.isNotEmpty(fieldPaths)) {

            List<String> aliases = new ArrayList<>();

            for (String fieldPath : fieldPaths) {
                String[] split = StringUtils.split(fieldPath, ".");

                if (split.length > 1) {

                    String prevAlias = "";

                    for (int i = 0; i < split.length - 1; i++) {
                        String curPath;
                        String curAlias;

                        if (i == 0) {
                            curPath = split[i];
                            curAlias = split[i];
                        } else {
                            curPath = prevAlias + "." + split[i];
                            curAlias = prevAlias + "_" + split[i];
                        }

                        if (!aliases.contains(curAlias)) {
                            criteria.createAlias(curPath, curAlias, joinType);
                            aliases.add(curAlias);
                        }

                        prevAlias = curAlias;
                    }
                }
            }
        }
    }

    /**
     * Filter a criteria object based on a set of supplied fields and their operations.
     *
     * @param criteria
     *     The criteria object to operate upon.
     * @param entityType
     *     The {@link Class} of the entity.
     * @param fieldOperations
     *     The {@link List} of fields and the operations to perform on each field.
     */
    public static void applyFilterCriteria(
        Criteria criteria,
        Class<?> entityType,
        List<FieldOperation> fieldOperations
    ) {

        try {
            if (CollectionUtils.isNotEmpty(fieldOperations)) {

                for (FieldOperation op : fieldOperations) {

                    final Object value = op.getValue();

                    if (op.hasOperation()) {
                        switch (op.getOperation()) {
                        case GREATER_THAN:
                            criteria.add(Restrictions.gt(op.getField(), value));
                            break;
                        case GREATER_THAN_OR_NULL:
                            criteria.add(Restrictions.or(Restrictions.gt(op.getField(), value), Restrictions.isNull(op.getField())));
                            break;
                        case GREATER_THAN_OR_EQUALS:
                            criteria.add(Restrictions.ge(op.getField(), value));
                            break;
                        case GREATER_THAN_OR_EQUALS_OR_NULL:
                            criteria.add(Restrictions.or(Restrictions.ge(op.getField(), value), Restrictions.isNull(op.getField())));
                            break;
                        case LESS_THAN:
                            criteria.add(Restrictions.lt(op.getField(), value));
                            break;
                        case LESS_THAN_OR_NULL:
                            criteria.add(Restrictions.or(Restrictions.lt(op.getField(), value), Restrictions.isNull(op.getField())));
                            break;
                        case LESS_THAN_OR_EQUALS:
                            criteria.add(Restrictions.le(op.getField(), value));
                            break;
                        case LESS_THAN_OR_EQUALS_OR_NULL:
                            criteria.add(Restrictions.or(Restrictions.le(op.getField(), value), Restrictions.isNull(op.getField())));
                            break;
                        case LIKE:
                            criteria.add(Restrictions.like(op.getField(), value));
                            break;
                        case NOT_LIKE:
                            criteria.add(Restrictions.not(Restrictions.like(op.getField(), value)));
                            break;
                        case NOT_EQUALS:
                            criteria.add(Restrictions.ne(op.getField(), value));
                            break;
                        case EQUALS_OR_IS_NULL:
                            criteria.add(Restrictions.or(Restrictions.eq(op.getField(), value), Restrictions.isNull(op.getField())));
                            break;
                        case NOT_EQUALS_OR_IS_NOT_NULL:
                            criteria.add(Restrictions.or(Restrictions.ne(op.getField(), value), Restrictions.isNotNull(op.getField())));
                            break;
                        case EQUALS:
                            criteria.add(Restrictions.eq(op.getField(), value));
                            break;
                        case IS_NULL:
                            criteria.add(Restrictions.isNull(op.getField()));
                            break;
                        case IS_NOT_NULL:
                            criteria.add(Restrictions.isNotNull(op.getField()));
                            break;
                        case IN:
                            criteria.add(Restrictions.in(op.getField(), (Object[]) value));
                            break;
                        case BETWEEN:
                            Object[] values = (Object[]) value;
                            criteria.add(Restrictions.between(op.getField(), values[0], values[1]));
                            break;
                        default:
                            break;
                        }
                    } else {
                        criteria.add(Restrictions.eq(op.getField(), value));
                    }
                }
            }
        } catch (Exception ex) {
            throw new FilterCriteriaException(String.format(
                "Unable to map field operations [%s] to criteria [%s] using template type [%s].",
                fieldOperations,
                criteria,
                entityType
            ), ex);
        }
    }

    /**
     * Apply sorting aggregation to the supplied {@link Criteria} instance.
     *
     * @param criteria
     *     The criteria object to operate upon.
     * @param sortOperations
     *     The sorting operations to perform, in the same order as the list.
     */
    public static void applySortCriteria(Criteria criteria, List<FieldOperation> sortOperations) {
        if (CollectionUtils.isNotEmpty(sortOperations)) {
            for (FieldOperation op : sortOperations) {
                criteria.addOrder(op.getOperation().equals(FieldOperation.SORT_ASC) ? Order.asc(op.getField()) : Order.desc(op.getField()));
            }
        }
    }

    public static void generateDatatablesOrderSearchCriteria(
        String term, List<String> searchColumns,
        Map<String, Integer> orderByColumns, Criteria criteria,
        List<String> sortDirections, List<Integer> sortedColumns
    ) {
        generateDatatablesOrderSearchCriteria(term, searchColumns, orderByColumns, criteria, sortDirections, sortedColumns, MatchMode.ANYWHERE);
    }

    public static void generateDatatablesOrderSearchCriteria(
        String term,
        List<String> searchColumns,
        Map<String, Integer> orderByColumns,
        Criteria criteria,
        List<String> sortDirections,
        List<Integer> sortedColumns,
        MatchMode matchMode
    ) {
        if (StringUtils.isNotEmpty(term)) {

            final Disjunction disjunction = Restrictions.disjunction();

            for (String column : searchColumns) {
                disjunction.add(Restrictions.like(column, term, matchMode));
            }
            criteria.add(disjunction);
        }

        if (sortDirections != null) {
            for (int index = 0; index <= sortDirections.size(); index++) {
                if (index >= sortedColumns.size()) {
                    continue;
                }

                for (Entry<String, Integer> column : orderByColumns.entrySet()) {
                    if (sortedColumns.get(index) == column.getValue() && "asc".equals(sortDirections.get(index))) {
                        criteria.addOrder(Order.asc(column.getKey()));
                    }
                    if (sortedColumns.get(index) == column.getValue() && "desc".equals(sortDirections.get(index))) {
                        criteria.addOrder(Order.desc(column.getKey()));
                    }
                }
            }
        }
    }

    public static void generateDatatablesOrderSearchCriteria(
        String term, Map<String, MatchMode> searchColumns,
        Map<String, Integer> orderByColumns, Criteria criteria,
        List<String> sortDirections, List<Integer> sortedColumns
    ) {
        if (StringUtils.isNotEmpty(term)) {

            final Disjunction disjunction = Restrictions.disjunction();

            for (Entry<String, MatchMode> column : searchColumns.entrySet()) {
                disjunction.add(Restrictions.like(column.getKey(), term, column.getValue()));
            }
            criteria.add(disjunction);
        }

        generateDataTablesOrderCriteria(sortDirections, sortedColumns, orderByColumns, criteria);
    }

    public static void generateDataTablesOrderCriteria(
        List<String> sortDirections,
        List<Integer> sortedColumns,
        Map<String, Integer> orderByColumns,
        Criteria criteria
    ) {
        if (sortDirections == null || sortDirections.isEmpty()) {
            return;
        }

        for (int index = 0; index < sortDirections.size(); index++) {
            final String sortDirection = sortDirections.get(index);

            for (Entry<String, Integer> column : orderByColumns.entrySet()) {
                if (sortedColumns.get(index).equals(column.getValue()) && SortDirection.ASC.name().equalsIgnoreCase(sortDirection)) {
                    criteria.addOrder(Order.asc(column.getKey()));
                }
                if (sortedColumns.get(index).equals(column.getValue()) && SortDirection.DESC.name().equalsIgnoreCase(sortDirection)) {
                    criteria.addOrder(Order.desc(column.getKey()));
                }
            }
        }
    }

    public static String generateHQLDatatablesOrderSearchCriteria(
        String termAlias, Map<String, MatchMode> searchColumns,
        Map<String, Integer> orderByColumns, String query,
        List<String> sortDirections, List<Integer> sortedColumns
    ) {
        final StringBuilder sb = new StringBuilder(query);

        if (StringUtils.isNotEmpty(termAlias)) {

            if (query.contains("where")) {
                sb.append(" and");
            } else {
                sb.append(" where");
            }
            sb.append(" (");

            int i = 0;
            for (Entry<String, MatchMode> column : searchColumns.entrySet()) {
                if (i > 0) {
                    sb.append(" or ");
                }
                sb.append(column.getKey()).append(" like ");
                switch (column.getValue()) {
                case START:
                    sb.append("CONCAT(:").append(termAlias).append(",'%')");
                    break;
                case END:
                    sb.append("CONCAT('%',:").append(termAlias).append(")");
                    break;
                case ANYWHERE:
                    sb.append("CONCAT('%',:").append(termAlias).append(",'%')");
                    break;
                default:
                    sb.append("\":").append(termAlias).append("\"");
                }
                i++;
            }
            sb.append(")");
        }

        if (sortDirections != null) {
            boolean initialOrder = true;
            for (int index = 0; index <= sortDirections.size(); index++) {
                if (index >= sortedColumns.size()) {
                    continue;
                }

                if (initialOrder) {
                    sb.append(" order by ");
                }

                for (Entry<String, Integer> column : orderByColumns.entrySet()) {
                    if (sortedColumns.get(index).equals(column.getValue()) && SortDirection.ASC.name().equalsIgnoreCase(sortDirections.get(index))) {
                        if (!initialOrder) {
                            sb.append(", ");
                        }
                        sb.append(column.getKey()).append(" asc");
                        initialOrder = false;
                    }
                    if (sortedColumns.get(index).equals(column.getValue()) && SortDirection.DESC.name().equalsIgnoreCase(sortDirections.get(index))) {
                        if (!initialOrder) {
                            sb.append(", ");
                        }
                        sb.append(column.getKey()).append(" desc");
                        initialOrder = false;
                    }
                }
            }
        }

        return sb.toString();
    }

    private static class FilterCriteriaException extends RuntimeException {

        private static final long serialVersionUID = -8771464694456233672L;

        FilterCriteriaException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
