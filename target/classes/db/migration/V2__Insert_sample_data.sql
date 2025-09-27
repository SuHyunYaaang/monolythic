-- Insert sample categories
INSERT INTO categories (category_id, name, description, parent_category_id, sort_order) VALUES
('cat-001', 'Electronics', '전자제품', NULL, 1),
('cat-002', 'Clothing', '의류', NULL, 2),
('cat-003', 'Books', '도서', NULL, 3),
('cat-004', 'Smartphones', '스마트폰', 'cat-001', 1),
('cat-005', 'Laptops', '노트북', 'cat-001', 2),
('cat-006', 'Men''s Clothing', '남성 의류', 'cat-002', 1),
('cat-007', 'Women''s Clothing', '여성 의류', 'cat-002', 2),
('cat-008', 'Fiction', '소설', 'cat-003', 1),
('cat-009', 'Non-Fiction', '비소설', 'cat-003', 2);

-- Insert sample products
INSERT INTO products (product_id, name, description, category_id, base_price, currency, digital, image_url) VALUES
('prod-001', 'iPhone 15 Pro', 'Apple iPhone 15 Pro 128GB', 'cat-004', 999.00, 'USD', false, 'https://example.com/iphone15pro.jpg'),
('prod-002', 'Samsung Galaxy S24', 'Samsung Galaxy S24 256GB', 'cat-004', 899.00, 'USD', false, 'https://example.com/galaxys24.jpg'),
('prod-003', 'MacBook Pro 14"', 'Apple MacBook Pro 14-inch M3', 'cat-005', 1999.00, 'USD', false, 'https://example.com/macbookpro14.jpg'),
('prod-004', 'Dell XPS 13', 'Dell XPS 13 9320', 'cat-005', 1299.00, 'USD', false, 'https://example.com/dellxps13.jpg'),
('prod-005', 'Nike Air Max 270', 'Nike Air Max 270 운동화', 'cat-006', 150.00, 'USD', false, 'https://example.com/nikeairmax270.jpg'),
('prod-006', 'Adidas Ultraboost 22', 'Adidas Ultraboost 22 운동화', 'cat-006', 180.00, 'USD', false, 'https://example.com/adidasultraboost22.jpg'),
('prod-007', 'The Great Gatsby', 'F. Scott Fitzgerald의 대표작', 'cat-008', 12.99, 'USD', true, 'https://example.com/greatgatsby.jpg'),
('prod-008', 'Clean Code', 'Robert C. Martin의 프로그래밍 서적', 'cat-009', 45.99, 'USD', true, 'https://example.com/cleancode.jpg');

-- Insert sample SKUs
INSERT INTO skus (sku_id, sku_code, name, description, product_id, price, currency, stock_quantity, track_inventory) VALUES
('sku-001', 'IPHONE15PRO-128-BLK', 'iPhone 15 Pro 128GB Black', 'Apple iPhone 15 Pro 128GB Space Black', 1, 999.00, 'USD', 50, true),
('sku-002', 'IPHONE15PRO-128-WHT', 'iPhone 15 Pro 128GB White', 'Apple iPhone 15 Pro 128GB White Titanium', 1, 999.00, 'USD', 30, true),
('sku-003', 'GALAXYS24-256-BLK', 'Galaxy S24 256GB Black', 'Samsung Galaxy S24 256GB Phantom Black', 2, 899.00, 'USD', 40, true),
('sku-004', 'GALAXYS24-256-WHT', 'Galaxy S24 256GB White', 'Samsung Galaxy S24 256GB Marble Gray', 2, 899.00, 'USD', 35, true),
('sku-005', 'MACBOOKPRO14-M3-512', 'MacBook Pro 14" M3 512GB', 'Apple MacBook Pro 14-inch M3 512GB SSD', 3, 1999.00, 'USD', 20, true),
('sku-006', 'MACBOOKPRO14-M3-1TB', 'MacBook Pro 14" M3 1TB', 'Apple MacBook Pro 14-inch M3 1TB SSD', 3, 2199.00, 'USD', 15, true),
('sku-007', 'DELLXPS13-512', 'Dell XPS 13 512GB', 'Dell XPS 13 9320 512GB SSD', 4, 1299.00, 'USD', 25, true),
('sku-008', 'DELLXPS13-1TB', 'Dell XPS 13 1TB', 'Dell XPS 13 9320 1TB SSD', 4, 1499.00, 'USD', 20, true),
('sku-009', 'NIKEAIRMAX270-10', 'Nike Air Max 270 Size 10', 'Nike Air Max 270 운동화 사이즈 10', 5, 150.00, 'USD', 100, true),
('sku-010', 'NIKEAIRMAX270-11', 'Nike Air Max 270 Size 11', 'Nike Air Max 270 운동화 사이즈 11', 5, 150.00, 'USD', 80, true),
('sku-011', 'ADIDASULTRABOOST22-10', 'Adidas Ultraboost 22 Size 10', 'Adidas Ultraboost 22 운동화 사이즈 10', 6, 180.00, 'USD', 60, true),
('sku-012', 'ADIDASULTRABOOST22-11', 'Adidas Ultraboost 22 Size 11', 'Adidas Ultraboost 22 운동화 사이즈 11', 6, 180.00, 'USD', 50, true),
('sku-013', 'GREATGATSBY-EBOOK', 'The Great Gatsby (eBook)', 'F. Scott Fitzgerald의 대표작 전자책', 7, 12.99, 'USD', 999, false),
('sku-014', 'CLEANCODE-EBOOK', 'Clean Code (eBook)', 'Robert C. Martin의 프로그래밍 서적 전자책', 8, 45.99, 'USD', 999, false);



