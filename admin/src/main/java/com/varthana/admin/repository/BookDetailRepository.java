package com.varthana.admin.repository;

import com.varthana.admin.entity.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface BookDetailRepository extends JpaRepository<BookDetail,Integer> {

}
