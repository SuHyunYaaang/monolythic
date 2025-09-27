// API Base URL
const API_BASE = '/api';

// Utility Functions
function showLoading(elementId) {
    const element = document.getElementById(elementId);
    if (element) {
        element.innerHTML = '<div class="loading"></div> <span>로딩 중...</span>';
    }
}

function showError(elementId, message) {
    const element = document.getElementById(elementId);
    if (element) {
        element.innerHTML = `
            <div class="api-card">
                <div class="status error">오류</div>
                <h3>API 호출 실패</h3>
                <p>${message}</p>
            </div>
        `;
    }
}

function showSuccess(elementId, title, data) {
    const element = document.getElementById(elementId);
    if (element) {
        element.innerHTML = `
            <div class="api-card">
                <div class="status success">성공</div>
                <h3>${title}</h3>
                <div class="json-display">${JSON.stringify(data, null, 2)}</div>
            </div>
        `;
    }
}

// API Functions
async function fetchAPI(url, options = {}) {
    try {
        const response = await fetch(`${API_BASE}${url}`, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('API 호출 오류:', error);
        throw error;
    }
}

// Load Categories
async function loadCategories() {
    showLoading('categories-container');
    
    try {
        const data = await fetchAPI('/catalog/categories');
        showSuccess('categories-container', '카테고리 목록', data);
    } catch (error) {
        showError('categories-container', error.message);
    }
}

// Load Products
async function loadProducts(categoryId = null) {
    showLoading('categories-container');
    
    try {
        const url = categoryId ? `/catalog/products?categoryId=${categoryId}` : '/catalog/products';
        const data = await fetchAPI(url);
        showSuccess('categories-container', '상품 목록', data);
    } catch (error) {
        showError('categories-container', error.message);
    }
}

// Load Cart
async function loadCart(customerId) {
    showLoading('cart-container');
    
    try {
        const data = await fetchAPI(`/cart/${customerId}`);
        showSuccess('cart-container', `고객 ${customerId}의 장바구니`, data);
    } catch (error) {
        showError('cart-container', error.message);
    }
}

// Load Orders
async function loadOrders(customerId) {
    showLoading('orders-container');
    
    try {
        const data = await fetchAPI(`/orders/customer/${customerId}`);
        showSuccess('orders-container', `고객 ${customerId}의 주문 내역`, data);
    } catch (error) {
        showError('orders-container', error.message);
    }
}

// Add to Cart
async function addToCart(customerId, skuId, quantity = 1) {
    try {
        const data = await fetchAPI(`/cart/${customerId}/items`, {
            method: 'POST',
            body: JSON.stringify({
                skuId: skuId,
                quantity: quantity
            })
        });
        
        alert('상품이 장바구니에 추가되었습니다!');
        loadCart(customerId); // 장바구니 새로고침
        return data;
    } catch (error) {
        alert(`장바구니 추가 실패: ${error.message}`);
        throw error;
    }
}

// Create Order
async function createOrder(customerId, shippingAddress, billingAddress, notes = '') {
    try {
        const data = await fetchAPI(`/orders/customer/${customerId}`, {
            method: 'POST',
            body: JSON.stringify({
                shippingAddress: shippingAddress,
                billingAddress: billingAddress,
                notes: notes
            })
        });
        
        alert('주문이 생성되었습니다!');
        loadOrders(customerId); // 주문 내역 새로고침
        return data;
    } catch (error) {
        alert(`주문 생성 실패: ${error.message}`);
        throw error;
    }
}

// Health Check
async function checkHealth() {
    try {
        const data = await fetchAPI('/actuator/health');
        console.log('애플리케이션 상태:', data);
        return data;
    } catch (error) {
        console.error('헬스 체크 실패:', error);
        return null;
    }
}

// Demo Functions
function runDemo() {
    console.log('🚀 E-commerce Platform Demo 시작');
    
    // 헬스 체크
    checkHealth().then(health => {
        if (health) {
            console.log('✅ 애플리케이션 정상 작동');
        } else {
            console.log('❌ 애플리케이션 문제 있음');
        }
    });
    
    // 자동으로 카테고리 로드
    setTimeout(() => {
        loadCategories();
    }, 1000);
}

// Navigation
function scrollToSection(sectionId) {
    const section = document.getElementById(sectionId);
    if (section) {
        section.scrollIntoView({ behavior: 'smooth' });
    }
}

// Event Listeners
document.addEventListener('DOMContentLoaded', function() {
    console.log('🎉 E-commerce Platform UI 로드 완료');
    
    // 네비게이션 링크 이벤트
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', function(e) {
            const href = this.getAttribute('href');
            if (href.startsWith('#')) {
                e.preventDefault();
                scrollToSection(href.substring(1));
            }
        });
    });
    
    // 데모 실행
    runDemo();
    
    // 자동 새로고침 (선택사항)
    // setInterval(() => {
    //     checkHealth();
    // }, 30000); // 30초마다 헬스 체크
});

// Export for global use
window.loadCategories = loadCategories;
window.loadProducts = loadProducts;
window.loadCart = loadCart;
window.loadOrders = loadOrders;
window.addToCart = addToCart;
window.createOrder = createOrder;
window.checkHealth = checkHealth;
