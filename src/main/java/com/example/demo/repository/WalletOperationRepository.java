package com.example.demo.repository;

import com.example.demo.entity.WalletOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletOperationRepository extends JpaRepository<WalletOperation, Long> {
}
