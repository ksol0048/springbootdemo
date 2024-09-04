package kroryi.demo;

import kroryi.demo.domain.Category;
import kroryi.demo.domain.Product;
import kroryi.demo.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.findAll();
        log.info("products: {}" , products);
        model.addAttribute("products", products);

        Category category=new Category();
        category.setId(4L);

        List<Product> product_category = productService.findByCategory(category);
        log.info("product_category:{}",product_category);

        for (Product product : product_category) {
            log.info("product_category:{}",product);
        }

        model.addAttribute("product_category", product_category);

        List<Product> product_price = productService.findByPriceGreaterThan(BigDecimal.valueOf(1900));
        for (Product product : product_price) {
            log.info("product_price:{}",product.getName());
        }

        model.addAttribute("product_price", product_price);
        return "products/product_list";
    }
}
