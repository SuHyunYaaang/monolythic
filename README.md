# E-commerce Platform

모듈러 모놀리식 아키텍처와 헥사고날 패턴을 적용한 Spring Boot 기반 전자상거래 플랫폼입니다.

## 🏗️ 아키텍처

### 핵심 설계 원칙
- **모듈러 모놀리식**: Spring Modulith를 활용한 모듈 경계 명확화
- **헥사고날 아키텍처**: Ports & Adapters 패턴으로 도메인과 외부 시스템 분리
- **도메인 중심 설계**: 비즈니스 로직을 도메인 모델에 집중

### 주요 모듈
- **Catalog**: 상품, 카테고리, SKU, 재고 관리
- **Cart**: 장바구니 기능
- **Order**: 주문 처리 및 상태 관리
- **Payment**: 결제 처리 (향후 확장)
- **Customer**: 고객 관리 (향후 확장)

## 🛠️ 기술 스택

### Backend
- **Spring Boot 3.2.0**
- **Spring Security** + JWT 인증
- **Spring Data JPA** + Hibernate
- **PostgreSQL** 데이터베이스
- **Redis** 캐싱
- **Flyway** 데이터베이스 마이그레이션
- **OpenAPI 3** (Swagger UI)
- **Spring Modulith** 모듈 관리

### 개발 도구
- **Maven** 빌드 도구
- **Testcontainers** 통합 테스트
- **Micrometer** + **OpenTelemetry** 관측성

## 🚀 시작하기

### 사전 요구사항
- Java 17+
- Maven 3.6+
- PostgreSQL 13+
- Redis 6+

### 설치 및 실행

1. **저장소 클론**
```bash
git clone <repository-url>
cd ecommerce
```

2. **데이터베이스 설정**
```bash
# PostgreSQL 데이터베이스 생성
createdb ecommerce

# Redis 서버 시작
redis-server
```

3. **환경 변수 설정**
```bash
export DB_USERNAME=ecommerce
export DB_PASSWORD=password
export REDIS_HOST=localhost
export REDIS_PORT=6379
```

4. **애플리케이션 실행**
```bash
mvn spring-boot:run
```

5. **API 문서 확인**
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api/api-docs

## 📚 API 사용법

### 인증
```bash
# 로그인
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user", "password": "user123"}'

# 응답에서 accessToken 추출하여 사용
```

### 카탈로그 조회
```bash
# 카테고리 목록
curl http://localhost:8080/api/catalog/categories

# 상품 목록
curl http://localhost:8080/api/catalog/products

# 상품 상세 (SKU 포함)
curl http://localhost:8080/api/catalog/products/{productId}
```

### 장바구니 관리
```bash
# 장바구니 조회
curl -H "Authorization: Bearer {token}" \
  http://localhost:8080/api/cart/{customerId}

# 상품 추가
curl -X POST -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{"skuId": "sku-001", "quantity": 2}' \
  http://localhost:8080/api/cart/{customerId}/items
```

### 주문 처리
```bash
# 주문 생성
curl -X POST -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{"shippingAddress": "서울시 강남구", "billingAddress": "서울시 강남구", "notes": "빠른 배송 부탁드립니다"}' \
  http://localhost:8080/api/orders/customer/{customerId}

# 주문 상태 변경
curl -X PUT -H "Authorization: Bearer {token}" \
  http://localhost:8080/api/orders/{orderId}/confirm
```

## 🔐 인증 정보

### 테스트 계정
- **관리자**: `admin` / `admin123` (ROLE_ADMIN)
- **일반 사용자**: `user` / `user123` (ROLE_USER)
- **고객**: `customer1` / `password123` (ROLE_USER)

## 🏛️ 데이터베이스 스키마

### 주요 테이블
- `categories`: 카테고리 정보
- `products`: 상품 정보
- `skus`: SKU (재고 단위) 정보
- `carts`: 장바구니
- `cart_items`: 장바구니 상품
- `orders`: 주문 정보
- `order_items`: 주문 상품

### 샘플 데이터
애플리케이션 시작 시 다음 샘플 데이터가 자동으로 로드됩니다:
- 전자제품, 의류, 도서 카테고리
- iPhone, Galaxy, MacBook, Dell 노트북 등 상품
- 각 상품별 다양한 SKU 옵션

## 🔧 설정

### 주요 설정 파일
- `application.yml`: 애플리케이션 설정
- `pom.xml`: Maven 의존성
- `db/migration/`: Flyway 마이그레이션 스크립트

### 환경별 설정
```yaml
# 개발 환경
spring:
  profiles:
    active: dev
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce
  data:
    redis:
      host: localhost
      port: 6379
```

## 🧪 테스트

### 단위 테스트 실행
```bash
mvn test
```

### 통합 테스트 실행
```bash
mvn verify
```

### Testcontainers를 사용한 통합 테스트
- PostgreSQL과 Redis를 컨테이너로 실행
- 실제 데이터베이스 환경에서 테스트

## 📊 모니터링

### Actuator 엔드포인트
- Health Check: `/actuator/health`
- Metrics: `/actuator/metrics`
- Prometheus: `/actuator/prometheus`

### 로깅
- SLF4J + Logback
- 구조화된 JSON 로그
- 요청/응답 로깅

## 🚀 배포

### Docker 배포
```bash
# Docker 이미지 빌드
docker build -t ecommerce-platform .

# 컨테이너 실행
docker run -p 8080:8080 \
  -e DB_USERNAME=ecommerce \
  -e DB_PASSWORD=password \
  -e REDIS_HOST=redis \
  ecommerce-platform
```

### Kubernetes 배포
```bash
kubectl apply -f k8s/
```

## 🔄 확장 계획

### 단기 계획
- [ ] 결제 모듈 구현 (Stripe 연동)
- [ ] 고객 관리 모듈
- [ ] 배송 추적 기능
- [ ] 이메일 알림 서비스

### 장기 계획
- [ ] 마이크로서비스 분리
- [ ] 이벤트 소싱 도입
- [ ] CQRS 패턴 적용
- [ ] 다국어 지원

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 `LICENSE` 파일을 참조하세요.

## 📞 지원

- 이슈 리포트: [GitHub Issues](https://github.com/your-repo/issues)
- 이메일: support@ecommerce.com
- 문서: [Wiki](https://github.com/your-repo/wiki)

---

**참고**: 이 프로젝트는 교육 및 데모 목적으로 제작되었습니다. 프로덕션 환경에서 사용하기 전에 보안, 성능, 확장성 등을 충분히 검토해 주세요.



