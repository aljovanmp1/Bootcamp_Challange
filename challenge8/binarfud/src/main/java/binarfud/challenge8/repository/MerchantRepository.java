package binarfud.challenge8.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import binarfud.challenge8.dto.merchant.others.OrderAnalysisMonthly;
import binarfud.challenge8.dto.merchant.others.OrderAnalysisWeekly;
import binarfud.challenge8.model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    Page<Merchant> findByOpenAndIsDeleted(boolean b, boolean deleted, Pageable pageable);
    Page<Merchant> findByIsDeleted(boolean deleted, Pageable pageable);
    Optional<Merchant> findByIdAndIsDeleted(UUID id, Boolean deleted);

    @Query(value = """
                select
                    sum(od.total_price) as price,
                    sum(od.quantity) as qty,
                    date_part('month', o.order_time) as "monthCount",
                    m.merchant_name as "merchantName"
                from orderdetail od
                inner join orders o on od.order_id = o.id
                inner join product p on od.product_id = p.id
                inner join merchant m on p.merchant_id = m.id
                where
                    m.id = :merchantId
                group by
                    "monthCount",
                    m.merchant_name
                order by "monthCount"
            """, nativeQuery = true)
    List<OrderAnalysisMonthly> getOrderAnalysisMonthlyByMerchantId(@Param("merchantId") UUID merchantId);

    @Query(value = """
            select
                sum(od.total_price) as price,
                sum(od.quantity) as qty,
                date_part('week', o.order_time) as "weekCount",
                date_part('week', o.order_time) - date_part('week', date_trunc('month', o.order_time)) + 1 AS "weekMonth",
                date_part('month', o.order_time) as "monthCount",
                m.merchant_name as "merchantName"
            from orderdetail od
            inner join orders o on od.order_id = o.id
            inner join product p on od.product_id = p.id
            inner join merchant m on p.merchant_id = m.id
            where
                m.id = 'de558ec4-edb2-4ed5-846d-a953e501e49e'
            group by
                "weekCount",
                "weekMonth",
                "monthCount",
                m.merchant_name
            order by "weekCount"
            """, nativeQuery = true)
    List<OrderAnalysisWeekly> getOrderAnalysisWeeklyByMerchantId(@Param("merchantId") UUID merchantId);
}
