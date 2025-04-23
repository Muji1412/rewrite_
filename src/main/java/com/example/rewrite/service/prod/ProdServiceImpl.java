package com.example.rewrite.service.prod;

import com.example.rewrite.command.ProductDTO;
import com.example.rewrite.entity.Product;
import com.example.rewrite.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdServiceImpl implements ProdService {

    private final ProductRepository productRepository;

    @Autowired
    public ProdServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 상품 목록 조회
    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 상품 상세 조회
    @Override
    @Transactional
    public ProductDTO getProductById(int id) {
        // 조회수 증가
        productRepository.incrementViewCount(id);

        // 사용자 정보와 함께 상품 조회
        Product product = productRepository.findProductWithUserById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        return convertToDto(product);
    }

    // 상품 등록
    @Override
    @Transactional
    public ProductDTO registerProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .title(productDTO.getTitle())
                .categoryMax(productDTO.getCategoryMax())
                .categoryMin(productDTO.getCategoryMin())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .img1(productDTO.getImg1())
                .img2(productDTO.getImg2())
                .img3(productDTO.getImg3())
                .img4(productDTO.getImg4())
                .videoUrl(productDTO.getVideoUrl())
                .regDate(LocalDateTime.now())
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    // 엔티티를 DTO로 변환
    private ProductDTO convertToDto(Product product) {
        ProductDTO.ProductDTOBuilder build = ProductDTO.builder()
                .prodId(product.getProdId())
                .title(product.getTitle())
                .categoryMax(product.getCategoryMax())
                .categoryMin(product.getCategoryMin())
                .price(product.getPrice())
                .description(product.getDescription())
                .img1(product.getImg1())
                .img2(product.getImg2())
                .img3(product.getImg3())
                .img4(product.getImg4())
                .videoUrl(product.getVideoUrl())
                .regDate(product.getRegDate())
                .viewcount(product.getViewCount() != null ? product.getViewCount() : 0);

        // 연관된 사용자 정보가 있다면 추가
        if (product.getUser() != null) {
            build.uid(product.getUser().getUid())
                    .userNickname(product.getUser().getNickname())
                    .userImgUrl(product.getUser().getImgUrl());
        }

        return build.build();
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getProdId())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // 변경 가능한 필드 업데이트
        product.setTitle(productDTO.getTitle());
        product.setCategoryMax(productDTO.getCategoryMax());
        product.setCategoryMin(productDTO.getCategoryMin());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setImg1(productDTO.getImg1());
        product.setImg2(productDTO.getImg2());
        product.setImg3(productDTO.getImg3());
        product.setImg4(productDTO.getImg4());
        product.setVideoUrl(productDTO.getVideoUrl());

        Product updatedProduct = productRepository.save(product);
        return convertToDto(updatedProduct);
    }


}
