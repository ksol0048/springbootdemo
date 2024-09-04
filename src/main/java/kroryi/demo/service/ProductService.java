package kroryi.demo.service;

import kroryi.demo.domain.Category;
import kroryi.demo.domain.Product;
import kroryi.demo.repository.ProductRepository;
import kroryi.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll() {
      return productRepository.findAll();
    }

    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> findByPriceGreaterThan(BigDecimal price) {
        return productRepository.findByPriceGreaterThan(price);
    }

    public List<Product> findByCategory_Name(String categoryName){
        return productRepository.findByCategory_Name(categoryName);
    }


}
