package com.beyond.ordersystem.product.service;


import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.product.domain.Product;
import com.beyond.ordersystem.product.dtos.ProductCreateDto;
import com.beyond.ordersystem.product.dtos.ProductResDto;
import com.beyond.ordersystem.product.dtos.ProductSearchDto;
import com.beyond.ordersystem.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final S3Client s3Client;

    public ProductService(ProductRepository productRepository, S3Client s3Client) {
        this.productRepository = productRepository;
        this.s3Client = s3Client;
    }

    public Long save(ProductCreateDto productCreateDto) {
        return product.getId();
    }

    @Transactional(readOnly = true)
    public Page<ProductResDto> findAll(Pageable pageable, ProductSearchDto searchDto){

        Specification<Product> specification = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if(searchDto.getProductName() != null){
                    predicateList.add(criteriaBuilder.like(root.get("name"), "%"+searchDto.getProductName()+"%"));
                }
                if(searchDto.getCategory() != null){
                    predicateList.add(criteriaBuilder.equal(root.get("category"), searchDto.getCategory()));
                }
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                for (int i=0; i<predicateArr.length; i++){
                    predicateArr[i] = predicateList.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };

        Page<Product> postList = productRepository.findAll(specification, pageable);
//        Page객체 안에 Entity->Dto로 쉽게 변환할수 있는 편의 제공
        return postList.map(p->.fromEntity(p));
    }

    @Transactional(readOnly = true)
    public ProductResDto findById(Long id){
        Product product = productRepository.findById(id).orElseThrow(()->new EntityNotFoundException("상품정보없음"));
        return ProductResDto.fromEntity(product);
    }


}
