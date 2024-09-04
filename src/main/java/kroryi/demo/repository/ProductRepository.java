package kroryi.demo.repository;

import kroryi.demo.domain.Category;
import kroryi.demo.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByPriceGreaterThan(BigDecimal price);
    List<Product> findByCategory_Name(String categoryName);
}

