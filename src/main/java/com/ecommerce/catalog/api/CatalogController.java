package com.ecommerce.catalog.api;

import com.ecommerce.catalog.application.CatalogService;
import com.ecommerce.catalog.domain.Category;
import com.ecommerce.catalog.domain.Product;
import com.ecommerce.catalog.domain.Sku;
import com.ecommerce.shared.api.ApiResponse;
import com.ecommerce.shared.domain.EntityId;
import com.ecommerce.shared.domain.Money;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/catalog")
@Tag(name = "Catalog", description = "상품 카탈로그 관리 API")
public class CatalogController {
    
    private final CatalogService catalogService;
    
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }
    
    // Category endpoints
    @GetMapping("/categories")
    @Operation(summary = "카테고리 목록 조회", description = "활성화된 카테고리 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getRootCategories() {
        List<Category> categories = catalogService.getRootCategories();
        List<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryDto::from)
                .toList();
        
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(ApiResponse.success(categoryDtos));
    }
    
    @GetMapping("/categories/{categoryId}")
    @Operation(summary = "카테고리 상세 조회", description = "특정 카테고리의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategory(
            @Parameter(description = "카테고리 ID") @PathVariable String categoryId) {
        
        return catalogService.getCategory(EntityId.of(categoryId))
                .map(category -> ResponseEntity.ok()
                        .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                        .body(ApiResponse.success(CategoryDto.from(category))))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/categories/{categoryId}/children")
    @Operation(summary = "하위 카테고리 조회", description = "특정 카테고리의 하위 카테고리 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getChildCategories(
            @Parameter(description = "부모 카테고리 ID") @PathVariable String categoryId) {
        
        List<Category> categories = catalogService.getChildCategories(EntityId.of(categoryId));
        List<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryDto::from)
                .toList();
        
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .body(ApiResponse.success(categoryDtos));
    }
    
    @PostMapping("/categories")
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        Category category;
        
        if (request.getParentCategoryId() != null) {
            category = catalogService.createChildCategory(
                    request.getName(),
                    request.getDescription(),
                    EntityId.of(request.getParentCategoryId())
            );
        } else {
            category = catalogService.createRootCategory(
                    request.getName(),
                    request.getDescription()
            );
        }
        
        return ResponseEntity.ok(ApiResponse.success("카테고리가 생성되었습니다.", CategoryDto.from(category)));
    }
    
    // Product endpoints
    @GetMapping("/products")
    @Operation(summary = "상품 목록 조회", description = "상품 목록을 페이지네이션으로 조회합니다.")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getProducts(
            @Parameter(description = "카테고리 ID") @RequestParam(required = false) String categoryId,
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Product> products;
        
        if (categoryId != null) {
            products = catalogService.getProductsByCategory(EntityId.of(categoryId), pageable);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            products = catalogService.searchProducts(keyword, pageable);
        } else {
            products = catalogService.getAllActiveProducts(pageable);
        }
        
        Page<ProductDto> productDtos = products.map(ProductDto::from);
        
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES))
                .body(ApiResponse.success(productDtos));
    }
    
    @GetMapping("/products/{productId}")
    @Operation(summary = "상품 상세 조회", description = "특정 상품의 상세 정보와 SKU 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(
            @Parameter(description = "상품 ID") @PathVariable String productId) {
        
        return catalogService.getProductWithSkus(EntityId.of(productId))
                .map(product -> ResponseEntity.ok()
                        .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES))
                        .body(ApiResponse.success(ProductDto.fromWithSkus(product))))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/products")
    @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다.")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = catalogService.createProduct(
                request.getName(),
                request.getDescription(),
                EntityId.of(request.getCategoryId()),
                Money.of(request.getBasePrice(), request.getCurrency())
        );
        
        return ResponseEntity.ok(ApiResponse.success("상품이 생성되었습니다.", ProductDto.from(product)));
    }
    
    // SKU endpoints
    @GetMapping("/skus/{skuId}")
    @Operation(summary = "SKU 상세 조회", description = "특정 SKU의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<SkuDto>> getSku(
            @Parameter(description = "SKU ID") @PathVariable String skuId) {
        
        return catalogService.getSku(EntityId.of(skuId))
                .map(sku -> ResponseEntity.ok()
                        .cacheControl(CacheControl.maxAge(2, TimeUnit.MINUTES))
                        .body(ApiResponse.success(SkuDto.from(sku))))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/skus/code/{skuCode}")
    @Operation(summary = "SKU 코드로 조회", description = "SKU 코드로 SKU 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<SkuDto>> getSkuByCode(
            @Parameter(description = "SKU 코드") @PathVariable String skuCode) {
        
        return catalogService.getSkuByCode(skuCode)
                .map(sku -> ResponseEntity.ok()
                        .cacheControl(CacheControl.maxAge(2, TimeUnit.MINUTES))
                        .body(ApiResponse.success(SkuDto.from(sku))))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/skus")
    @Operation(summary = "SKU 생성", description = "새로운 SKU를 생성합니다.")
    public ResponseEntity<ApiResponse<SkuDto>> createSku(@Valid @RequestBody CreateSkuRequest request) {
        Sku sku = catalogService.createSku(
                request.getSkuCode(),
                request.getName(),
                request.getDescription(),
                EntityId.of(request.getProductId()),
                Money.of(request.getPrice(), request.getCurrency())
        );
        
        return ResponseEntity.ok(ApiResponse.success("SKU가 생성되었습니다.", SkuDto.from(sku)));
    }
}



