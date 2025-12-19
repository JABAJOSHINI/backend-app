package com.marine.supplierservice.repository;

import com.marine.productservice.entity.OrderEntity;
import com.marine.supplierservice.entity.SupplierQuoteEntity;
import com.marine.supplierservice.model.SupplierQuoteDto;
import com.marine.supplierservice.model.SupplierQuotes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierQuoteRepository extends JpaRepository<SupplierQuoteEntity, Long> {
    @Query(
            value = """
                    SELECT sq.quote_id AS supplierQuote,sq.status AS status, sq.created_at AS createdAt, sd.email AS supplierEmail, sd.company AS company  FROM public.supplier_quote AS sq LEFT JOIN public.supplier_details AS sd\s
                     ON sd.id = sq.supplier_contact_id where status = 'ACTIVE' ORDER BY sq.quote_id ASC; """,
            countQuery = """
                    SELECT COUNT(*) AS active_quotes FROM public.supplier_quote WHERE status = 'ACTIVE';
                        """,
            nativeQuery = true
    )
    Page<SupplierQuotes> findAllSupplierPurchaseQuote(Pageable pageable);

    List<SupplierQuoteEntity> findByQuoteId(String quoteId);
}
