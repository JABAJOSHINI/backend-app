package com.marine.productservice.repository;

import com.marine.productservice.DTO.OrderSummary;
import com.marine.productservice.entity.OrderEntity;
import com.marine.productservice.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {
        @Query(
                value = """
                        SELECT o.rfq_order_id AS orderId, o.client_id AS clientId, c.email AS clientEmail, o.created_at AS createdAt
                        FROM orders o
                        JOIN client c ON o.client_id = c.id
                        GROUP BY o.rfq_order_id, o.client_id, c.email, o.created_at
                        """,
                countQuery = """
                        SELECT COUNT(*) FROM (SELECT o.rfq_order_id
                        FROM orders o JOIN client c ON o.client_id = c.id
                        GROUP BY o.rfq_order_id, o.client_id, c.email, o.created_at) AS grouped;
                        """,
                nativeQuery = true
        )
        Page<OrderSummary> findGroupedOrders(Pageable pageable);

        List<OrderEntity> findByRfqOrderId(String rfqOrderId);

}
