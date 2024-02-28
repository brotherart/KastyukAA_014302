package com.autodocservice.repo;

import com.autodocservice.model.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentsRepo extends JpaRepository<Documents, Long> {
    Documents findByNumber(int number);

    List<Documents> findAllByName(String name);

    List<Documents> findAllByDate(String date);

    List<Documents> findAllByNameContainingAndDateContaining(String name, String date);
}
