package com.logitrack.services;

import com.logitrack.dto.request.ProductRequestDTO;
import com.logitrack.dto.response.ProductResponseDTO;
import com.logitrack.entities.Product;
import com.logitrack.exception.ResourceNotFoundException;
import com.logitrack.mapper.ProductMapper;
import com.logitrack.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDTO addProduct(ProductRequestDTO request) {
        Product product = productMapper.toEntity(request);
        product = productRepository.save(product);
        return productMapper.toResponseDTO(product);
    }

    public Page<ProductResponseDTO> getAllProducts(Pageable pageable, String category, Double maxPrice, Boolean lowStock) {
        Specification<Product> spec = Specification.where(null);

        if (category != null && !category.isBlank()) {
            String searchTerm = "%" + category.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("category")), searchTerm));
        }

        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        if (Boolean.TRUE.equals(lowStock)) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("stockAmount"), 5));
        }

        return productRepository.findAll(spec, pageable).map(productMapper::toResponseDTO);
    }

    public ProductResponseDTO getProductById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID : " + id));
        return productMapper.toResponseDTO(product);
    }

    public ProductResponseDTO updateProduct(int id, ProductRequestDTO request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé avec l'ID : " + id));
        productMapper.updateEntity(request, product);
        product = productRepository.save(product);
        return productMapper.toResponseDTO(product);
    }

    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produit non trouvé avec l'ID : " + id);
        }
        productRepository.deleteById(id);
    }

    public List<ProductResponseDTO> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(productMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByPriceLessThanEqual(Double price) {
        return productRepository.findByPriceLessThanEqual(price).stream()
                .map(productMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getLowStockProducts(int threshold) {
        return productRepository.findProductsLowStock(threshold).stream()
                .map(productMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}