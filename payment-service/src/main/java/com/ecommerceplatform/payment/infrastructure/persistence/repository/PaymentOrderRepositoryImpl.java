package com.ecommerceplatform.payment.infrastructure.persistence.repository;

import com.ecommerceplatform.payment.domain.model.PaymentOrder;
import com.ecommerceplatform.payment.infrastructure.persistence.entity.OutboxEventEntity;
import com.ecommerceplatform.payment.infrastructure.persistence.entity.PaymentEntity;
import com.ecommerceplatform.payment.infrastructure.persistence.entity.PaymentOrderEntity;
import com.ecommerceplatform.payment.infrastructure.persistence.mapper.OutboxEventMapper;
import com.ecommerceplatform.payment.infrastructure.persistence.mapper.PaymentOrderMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PaymentOrderRepositoryImpl implements PaymentOrderRepository {

    private final JpaPaymentOrderRepository jpaRepository;
    private final EntityManager entityManager;

    public PaymentOrderRepositoryImpl(JpaPaymentOrderRepository jpaRepository, EntityManager entityManager) {
        this.jpaRepository = jpaRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<PaymentOrder> findById(String id) {
        return jpaRepository.findById(id)
                .map(PaymentOrderMapper::toDomain);
    }


    // Custom method to check if a PaymentOrder exists by paymentId and status
    @Override
    public boolean existsByPaymentIdAndStatus(String paymentId, String status) {
        String jpql = "SELECT COUNT(po) > 0 FROM PaymentOrder po WHERE po.paymentId = :paymentId AND po.status = :status";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("paymentId", paymentId);
        query.setParameter("status", status);

        // Return true if count > 0, otherwise false
        return (boolean) query.getSingleResult();
    }

    /*
        @Override
    public OutboxEvent save(OutboxEvent event) {
        OutboxEventEntity entity = OutboxEventMapper.toEntity(event);
        OutboxEventEntity saved = jpaRepository.save(entity);
        return OutboxEventMapper.toDomain(saved);
    }
     */

    @Override
    public void saveAll(List<PaymentOrder> paymentOrders) {
        List<PaymentOrderEntity> entities = paymentOrders.stream()
                .map(PaymentOrderMapper::toEntity)
                .collect(Collectors.toList());
        jpaRepository.saveAll(entities);
    }

    @Override
    public void save(PaymentOrder paymentOrder) {
        PaymentOrderEntity entity = PaymentOrderMapper.toEntity(paymentOrder);
        jpaRepository.save(entity);

    }

    // Custom method to count PaymentOrders by paymentId
    @Override
    public long countByPaymentId(String paymentId) {
        String jpql = "SELECT COUNT(po) FROM PaymentOrder po WHERE po.paymentId = :paymentId";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("paymentId", paymentId);
        return (long) query.getSingleResult();
    }

    // Custom method to count PaymentOrders by paymentId and statuses (SUCCESS, FAILED)
    @Override
    public long countByPaymentIdAndStatusIn(String paymentId, List<String> statuses) {
        String jpql = "SELECT COUNT(po) FROM PaymentOrder po WHERE po.paymentId = :paymentId AND po.status IN :statuses";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("paymentId", paymentId);
        query.setParameter("statuses", statuses);
        return (long) query.getSingleResult();
    }
}