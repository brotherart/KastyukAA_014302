package com.autodocservice.repo;

import com.autodocservice.model.DocumentComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentCommentsRepo extends JpaRepository<DocumentComments, Long> {
}
