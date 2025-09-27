package com.ecommerce.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    
    @GetMapping("/")
    public String index() {
        return """
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>E-commerce Platform</title>
                <link rel="stylesheet" href="/css/style.css">
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
            </head>
            <body>
                <header class="header">
                    <div class="container">
                        <h1><i class="fas fa-shopping-cart"></i> E-commerce Platform</h1>
                        <nav>
                            <a href="#catalog" class="nav-link">카탈로그</a>
                            <a href="#cart" class="nav-link">장바구니</a>
                            <a href="#orders" class="nav-link">주문</a>
                            <a href="/api/swagger-ui.html" class="nav-link" target="_blank">API 문서</a>
                        </nav>
                    </div>
                </header>

                <main class="main">
                    <div class="container">
                        <section id="welcome" class="welcome-section">
                            <h2>🛍️ E-commerce Platform에 오신 것을 환영합니다!</h2>
                            <p>이 플랫폼은 Spring Boot로 구축된 모듈형 모놀리식 전자상거래 시스템입니다.</p>
                            
                            <div class="features">
                                <div class="feature-card">
                                    <i class="fas fa-box"></i>
                                    <h3>상품 카탈로그</h3>
                                    <p>카테고리, 상품, SKU 관리</p>
                                    <button onclick="loadCategories()" class="btn">카테고리 보기</button>
                                </div>
                                
                                <div class="feature-card">
                                    <i class="fas fa-shopping-cart"></i>
                                    <h3>장바구니</h3>
                                    <p>상품 추가, 수량 변경, 삭제</p>
                                    <button onclick="loadCart('customer1')" class="btn">장바구니 보기</button>
                                </div>
                                
                                <div class="feature-card">
                                    <i class="fas fa-receipt"></i>
                                    <h3>주문 관리</h3>
                                    <p>주문 생성, 상태 변경, 추적</p>
                                    <button onclick="loadOrders('customer1')" class="btn">주문 보기</button>
                                </div>
                            </div>
                        </section>

                        <section id="catalog" class="section">
                            <h2><i class="fas fa-box"></i> 상품 카탈로그</h2>
                            <div id="categories-container">
                                <p>카테고리를 불러오는 중...</p>
                            </div>
                        </section>

                        <section id="cart" class="section">
                            <h2><i class="fas fa-shopping-cart"></i> 장바구니</h2>
                            <div id="cart-container">
                                <p>장바구니를 불러오는 중...</p>
                            </div>
                        </section>

                        <section id="orders" class="section">
                            <h2><i class="fas fa-receipt"></i> 주문 내역</h2>
                            <div id="orders-container">
                                <p>주문 내역을 불러오는 중...</p>
                            </div>
                        </section>
                    </div>
                </main>

                <footer class="footer">
                    <div class="container">
                        <p>&copy; 2024 E-commerce Platform. Spring Boot + Hexagonal Architecture</p>
                    </div>
                </footer>

                <script src="/js/app.js"></script>
            </body>
            </html>
            """;
    }
}
