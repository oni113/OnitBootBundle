package net.nonworkspace.demo.utils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

public class QuerydslUtil {

    public static OrderSpecifier<?>[] toOrderSpecifiers(Sort sort, PathBuilder<?> entityPath) {
        return sort.stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC; // TODO: how to set nulls first/last
                    return new OrderSpecifier<>(direction, entityPath.get(order.getProperty(), Comparable.class));
                })
                .toArray(OrderSpecifier[]::new);
    }
}
