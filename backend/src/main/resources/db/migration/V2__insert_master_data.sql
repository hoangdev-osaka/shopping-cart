-- V2__insert_master_data.sql
-- Master data / demo data
-- IMPORTANT: keep file encoding UTF-8 (no BOM)

SET NAMES utf8mb4;

-- -------------------------
-- Roles / Users
-- -------------------------
INSERT INTO roles (id, name, status)
VALUES
 (1, 'ADMIN', 'ACTIVE'),
 (2, 'USER',  'ACTIVE');

INSERT INTO users (
    id,
    email,
    password,
    full_name,
    email_verified,
    status,
    created_at
) VALUES (
    1,
    'admin@shopping.com',
    '$2a$10$SevTMkyFqXN39jTrszWhS.2FwudX6ZDaH8.U1sofCoVn3tkXl0JQy',
    'System Admin',
    1,
    'ACTIVE',
    NOW()
);

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);

-- -------------------------
-- Categories / Tags / Brands
-- -------------------------
INSERT INTO categories (id, name, slug, status)
VALUES
  (1, 'メンズ', 'mens', 'ACTIVE'),
  (2, 'レディース', 'ladies', 'ACTIVE'),
  (3, 'キッズ', 'kids', 'ACTIVE'),
  (4, 'セール', 'sale', 'ACTIVE'),
  (5, 'スポーツ', 'sports', 'ACTIVE')
  ON DUPLICATE KEY UPDATE
    slug   = VALUES(slug),
    status = VALUES(status);

INSERT INTO tags (
    id,
    name,
    slug,
    status,
    is_main
) VALUES (
    1,
    'New',
    'new',
    'ACTIVE',
    1
);

INSERT INTO brands (
    id,
    name,
    slug,
    description,
    logo_url
)
VALUES
    (1, 'アディダス', 'adidas',
     'ドイツ発のスポーツブランドで、スニーカー、アパレル、アクセサリーまで幅広く展開。',
     '/assets/images/brands/adidas.png'),
    (2, 'ナイキ', 'nike',
     'アメリカ発の世界的スポーツブランド。革新的なデザインと機能性で人気。',
     '/assets/images/brands/nike.png'),
    (3, 'バレンシアガ', 'balenciaga',
     'フランスのハイブランド。独創的でモード感のあるスニーカーが特徴。',
     '/assets/images/brands/balenciaga.png'),
    (4, 'プーマ', 'puma',
     'ドイツ発のスポーツブランド。カジュアルでスポーティーなスタイルが魅力。',
     '/assets/images/brands/puma.png'),
    (5, 'ヴァンズ', 'vans',
     'アメリカ発のスケートブランド。シンプルでストリート感のあるデザインが人気。',
     '/assets/images/brands/vans.png');

-- -------------------------
-- Products
-- -------------------------
INSERT INTO products (
    id,
    brand_id,
    category_id,
    name,
    slug,
    description,
    status
)
VALUES
(1, 1, 1, 'アディダス ランニングシューズ', 'adidas-running-shoes', '日常のランニングやトレーニングに適した軽量シューズ。', 'ACTIVE'),
(2, 1, 1, 'アディダス スニーカー', 'adidas-sneakers', 'カジュアルスタイルに合わせやすい定番スニーカー。', 'ACTIVE'),
(3, 1, 1, 'アディダス トレーニングシューズ', 'adidas-training-shoes', 'ジムや屋内トレーニング向けの安定感のあるシューズ。', 'ACTIVE'),
(4, 1, 1, 'アディダス スポーツウェア', 'adidas-sportswear', '快適な着心地を重視したスポーツウェア。', 'ACTIVE'),
(5, 1, 5, 'アディダス ライフスタイルシューズ', 'adidas-lifestyle-shoes', '日常使いに適したスタイリッシュなライフスタイルモデル。', 'ACTIVE'),

-- nike (2)
(6, 2, 1, 'ナイキ ランニングシューズ', 'nike-running-shoes', '反発力とクッション性を両立したランニングモデル。', 'ACTIVE'),
(7, 2, 2, 'ナイキ エア スニーカー', 'nike-air-sneakers', 'エアクッション搭載の定番スニーカー。', 'ACTIVE'),
(8, 2, 3, 'ナイキ トレーニングシューズ', 'nike-training-shoes', 'ジムトレーニング向けの安定した履き心地。', 'ACTIVE'),
(9, 2, 4, 'ナイキ ストリートシューズ', 'nike-street-shoes', 'ストリートファッションに合うデザイン。', 'ACTIVE'),

-- balenciaga (3)
(10, 3, 1, 'バレンシアガ ハイエンドスニーカー', 'balenciaga-highend-sneakers', '存在感のあるボリュームデザイン。', 'ACTIVE'),
(11, 3, 2, 'バレンシアガ モードシューズ', 'balenciaga-mode-shoes', 'ファッション性を重視したモードモデル。', 'ACTIVE'),
(12, 3, 3, 'バレンシアガ トラックシューズ', 'balenciaga-track-shoes', '重厚感のある独創的なデザイン。', 'ACTIVE'),
(13, 3, 4, 'バレンシアガ ラグジュアリースニーカー', 'balenciaga-luxury-sneakers', '高級感あふれる素材と仕上げ。', 'ACTIVE'),

-- puma (4)
(14, 4, 1, 'プーマ ランナーシューズ', 'puma-runner-shoes', '軽量で走りやすいランナーモデル。', 'ACTIVE'),
(15, 4, 2, 'プーマ カジュアルスニーカー', 'puma-casual-sneakers', 'デイリーに使えるカジュアルデザイン。', 'ACTIVE'),
(16, 4, 3, 'プーマ トレーニングモデル', 'puma-training-model', '安定性とグリップ力を重視。', 'ACTIVE'),
(17, 4, 4, 'プーマ スポーツシューズ', 'puma-sports-shoes', '多用途に使えるスポーツモデル。', 'ACTIVE'),

-- vans (5)
(18, 5, 1, 'ヴァンズ クラシックスニーカー', 'vans-classic-sneakers', 'スケートカルチャーを感じる定番モデル。', 'ACTIVE'),
(19, 5, 2, 'ヴァンズ ローカットシューズ', 'vans-lowcut-shoes', '軽快な履き心地のローカット。', 'ACTIVE'),
(20, 5, 3, 'ヴァンズ ハイカットスニーカー', 'vans-highcut-sneakers', '足首をしっかりサポートするハイカット。', 'ACTIVE'),
(21, 5, 4, 'ヴァンズ デイリーシューズ', 'vans-daily-shoes', '普段使いしやすいシンプルモデル。', 'ACTIVE');

-- -------------------------
-- Product Colors
-- -------------------------
INSERT INTO product_colors (id, product_id, is_main, name, color_family, hex)
VALUES
(1, 1, 1, 'ホワイト', 'WHITE', '#000000'),
(2, 1, 0, 'グレー', 'GRAY', '#FFFFFF'),
(3, 1, 0, 'クリーム/ブラック', 'BEIGE', '#0057B8'),

(4, 2, 1, 'ブラック', 'BLACK', '#000000'),
(5, 2, 0, 'ブラック', 'BROWN', '#FFFFFF'),
(6, 2, 0, 'ホワイト', 'WHITE', '#0057B8'),

(7, 3, 1, 'ブラック', 'BLACK', '#000000'),
(8, 3, 0, 'ホワイト', 'WHITE', '#FFFFFF'),
(9, 3, 0, 'ブルー', 'BLUE', '#0057B8'),

(10, 4, 0, 'ブラック', 'BLACK', '#000000'),
(11, 4, 0, 'ホワイト', 'WHITE', '#FFFFFF'),
(12, 4, 1, 'ブラウン', 'BROWN', '#0057B8'),

(13, 5, 1, 'グレー', 'GRAY', '#000000'),
(14, 5, 0, 'ホワイト', 'WHITE', '#FFFFFF'),

(15, 6, 1, 'ブラウン', 'BROWN', '#FFFFFF'),

(16, 7, 1, 'ライトオールウッドブラウン/ミディアムオリーブ/セイル/オフノワール', 'GREEN', '#FFFFFF'),
(17, 7, 0, 'ブラック/ホワイト/バーシティレッド', 'RED', '#FFFFFF'),

(18, 8, 1, 'ブラック', 'BLACK', '#000000'),

(19, 9, 1, 'ブラック', 'BLACK', '#000000'),
(20, 9, 0, 'ブラウン', 'BROWN', '#FFFFFF'),
(21, 9, 0, 'グリーン', 'GREEN', '#0057B8'),

(22, 10, 1, 'ブラック', 'BLACK', '#000000'),
(23, 10, 0, 'クリーム', 'BEIGE', '#FFFFFF'),
(24, 10, 0, 'ホワイト', 'WHITE', '#0057B8'),

(25, 11, 1, 'グレー', 'GRAY', '#000000'),
(26, 11, 0, 'クリーム', 'BEIGE', '#FFFFFF'),
(27, 11, 0, 'ホワイト', 'WHITE', '#0057B8'),

(28, 12, 1, 'ブラック', 'BLACK', '#0057B8'),

(29, 13, 1, 'ブラック', 'BLACK', '#0057B8'),


(30, 14, 1, 'ブルー', 'BLUE', '#0057B8'),
(31, 14, 0, 'ブラウン', 'BROWN', '#0057B8'),

(32, 15, 1, 'ホワイト', 'WHITE', '#0057B8'),
(33, 15, 0, 'ブラック', 'BLACK', '#0057B8'),

(34, 16, 0, 'ブラック', 'BLACK', '#0057B8'),
(35, 16, 1, 'ホワイト', 'WHITE', '#0057B8'),

(36, 17, 0, 'ブルー', 'BLUE', '#0057B8'),

(37, 18, 1, 'クリーム', 'BEIGE', '#0057B8'),
(38, 18, 0, 'ホワイト', 'WHITE', '#0057B8'),
(39, 18, 0, 'ブラック', 'BLACK', '#0057B8'),

(40, 19, 1, 'ホワイト', 'WHITE', '#0057B8'),

(41, 20, 1, 'ブラック', 'BLACK', '#000000'),
(42, 20, 0, 'クリーム', 'BEIGE', '#FFFFFF'),
(43, 20, 0, 'ホワイト', 'WHITE', '#0057B8'),

(44, 21, 1, 'ホワイト', 'WHITE', '#000000'),
(45, 21, 0, 'グリーン', 'GREEN', '#FFFFFF'),
(46, 21, 0, 'レッド', 'RED', '#0057B8');



-- -------------------------
-- Variants
-- -------------------------
INSERT INTO product_variants (
    id,
    product_color_id,
    size,
    sort_order,
    regular_price,
    sale_price,
    stock
)
VALUES
(1, 1, '25.5', 1, 15000, 13500, 10),
(2, 1, '26.0', 2, 15000, 13500, 15),
(3, 1, '26.5', 3, 15000, 13500, 12),
(4, 1, '27.0', 4, 15000, 13500, 8),
(5, 1, '27.5', 5, 15000, 13500, 5),

(6, 2, '25.5', 1, 14000, 0, 12),
(7, 2, '26.0', 2, 14000, 0, 15),
(8, 2, '26.5', 3, 14000, 0, 10),
(9, 2, '27.0', 4, 14000, 0, 7),
(10, 2, '27.5', 5, 14000, 0, 5),

(11, 3, '25.5', 1, 16000, 14500, 10),
(12, 3, '26.0', 2, 16000, 14500, 14),
(13, 3, '26.5', 3, 16000, 14500, 10),
(14, 3, '27.0', 4, 16000, 14500, 6),
(15, 3, '27.5', 5, 16000, 14500, 4),

(16, 4, '25.5', 1, 13000, 0, 15),
(17, 4, '26.0', 2, 13000, 0, 18),
(18, 4, '26.5', 3, 13000, 0, 12),
(19, 4, '27.0', 4, 13000, 0, 8),
(20, 4, '27.5', 5, 13000, 0, 6),

(21, 5, '25.5', 1, 17000, 15500, 8),
(22, 5, '26.0', 2, 17000, 15500, 12),
(23, 5, '26.5', 3, 17000, 15500, 9),
(24, 5, '27.0', 4, 17000, 15500, 6),
(25, 5, '27.5', 5, 17000, 15500, 4),

-- PRODUCT 2 - COLOR 6
(31, 6, '25.5', 1, 13000, 0, 10),
(32, 6, '26.0', 2, 13000, 0, 14),
(33, 6, '26.5', 3, 13000, 0, 12),
(34, 6, '27.0', 4, 13000, 0, 8),
(35, 6, '27.5', 5, 13000, 0, 6),

-- PRODUCT 3 - COLOR 7
(36, 7, '25.5', 1, 16000, 14500, 10),
(37, 7, '26.0', 2, 16000, 14500, 12),
(38, 7, '26.5', 3, 16000, 14500, 10),
(39, 7, '27.0', 4, 16000, 14500, 6),
(40, 7, '27.5', 5, 16000, 14500, 4),

-- PRODUCT 3 - COLOR 8
(41, 8, '25.5', 1, 16000, 14500, 8),
(42, 8, '26.0', 2, 16000, 14500, 12),
(43, 8, '26.5', 3, 16000, 14500, 9),
(44, 8, '27.0', 4, 16000, 14500, 7),
(45, 8, '27.5', 5, 16000, 14500, 5),

-- PRODUCT 3 - COLOR 9
(46, 9, '25.5', 1, 16000, 14500, 9),
(47, 9, '26.0', 2, 16000, 14500, 13),
(48, 9, '26.5', 3, 16000, 14500, 11),
(49, 9, '27.0', 4, 16000, 14500, 7),
(50, 9, '27.5', 5, 16000, 14500, 5),

-- PRODUCT 4 - COLOR 10
(51, 10, '25.5', 1, 9000, 0, 12),
(52, 10, '26.0', 2, 9000, 0, 10),
(53, 10, '26.5', 3, 9000, 0, 8),
(54, 10, '27.0', 4, 9000, 0, 5),
(55, 10, '27.5', 5, 9000, 0, 4),

-- PRODUCT 4 - COLOR 11
(56, 11, '25.5', 1, 10000, 0, 12),
(57, 11, '26.0', 2, 10000, 0, 10),
(58, 11, '26.5', 3, 10000, 0, 8),
(59, 11, '27.0', 4, 10000, 0, 6),
(60, 11, '27.5', 5, 10000, 0, 4),

-- PRODUCT 4 - COLOR 12
(61, 12, '25.5', 1, 11000, 0, 10),
(62, 12, '26.0', 2, 11000, 0, 12),
(63, 12, '26.5', 3, 11000, 0, 9),
(64, 12, '27.0', 4, 11000, 0, 6),
(65, 12, '27.5', 5, 11000, 0, 4),

-- PRODUCT 5 - COLOR 13
(66, 13, '25.5', 1, 17000, 15500, 8),
(67, 13, '26.0', 2, 17000, 15500, 10),
(68, 13, '26.5', 3, 17000, 15500, 9),
(69, 13, '27.0', 4, 17000, 15500, 6),
(70, 13, '27.5', 5, 17000, 15500, 4),

-- PRODUCT 5 - COLOR 14
(71, 14, '25.5', 1, 17500, 16000, 8),
(72, 14, '26.0', 2, 17500, 16000, 10),
(73, 14, '26.5', 3, 17500, 16000, 9),
(74, 14, '27.0', 4, 17500, 16000, 6),
(75, 14, '27.5', 5, 17500, 16000, 4),

-- PRODUCT 6 - COLOR 15
(76, 15, '25.5', 1, 17500, 16000, 8),
(77, 15, '26.0', 2, 17500, 16000, 10),
(78, 15, '26.5', 3, 17500, 16000, 9),
(79, 15, '27.0', 4, 17500, 16000, 6),
(80, 15, '27.5', 5, 17500, 16000, 4),

-- PRODUCT 7 - COLOR 16
(81, 16, '25.5', 1, 17500, 16000, 8),
(82, 16, '26.0', 2, 17500, 16000, 10),
(83, 16, '26.5', 3, 17500, 16000, 9),
(84, 16, '27.0', 4, 17500, 16000, 6),
(85, 16, '27.5', 5, 17500, 16000, 4),

-- PRODUCT 7 - COLOR 17
(86, 17, '25.5', 1, 17500, 16000, 8),
(87, 17, '26.0', 2, 17500, 16000, 10),
(88, 17, '26.5', 3, 17500, 16000, 9),
(89, 17, '27.0', 4, 17500, 16000, 6),
(90, 17, '27.5', 5, 17500, 16000, 4),

-- PRODUCT 8 - COLOR 18
(91, 18, '25.5', 1, 17500, 16000, 8),
(92, 18, '26.0', 2, 17500, 16000, 10),
(93, 18, '26.5', 3, 17500, 16000, 9),
(94, 18, '27.0', 4, 17500, 16000, 6),
(95, 18, '27.5', 5, 17500, 16000, 4),

-- PRODUCT 9 - COLOR 19
(96, 19, '25.5', 1, 17500, 16000, 8),
(97, 19, '26.0', 2, 17500, 16000, 10),
(98, 19, '26.5', 3, 17500, 16000, 9),
(99, 19, '27.0', 4, 17500, 16000, 6),
(100, 19, '27.5', 5, 17500, 16000, 4),

-- PRODUCT 9 - COLOR 20
(101, 20, '25.5', 1, 17500, 16000, 8),
(102, 20, '26.0', 2, 17500, 16000, 10),
(103, 20, '26.5', 3, 17500, 16000, 9),
(104, 20, '27.0', 4, 17500, 16000, 6),
(105, 20, '27.5', 5, 17500, 16000, 4),

-- PRODUCT 9 - COLOR 21
(106, 21, '25.5', 1, 17500, 16000, 8),
(107, 21, '26.0', 2, 17500, 16000, 10),
(108, 21, '26.5', 3, 17500, 16000, 9),
(109, 21, '27.0', 4, 17500, 16000, 6),
(110, 21, '27.5', 5, 17500, 16000, 4),


(111,22,'25.5',1,190500,0,8),
(112,22,'26.0',2,190500,0,10),
(113,22,'26.5',3,190500,0,9),
(114,22,'27.0',4,190500,0,6),
(115,22,'27.5',5,190500,171500,4),

(116,23,'25.5',1,157000,0,8),
(117,23,'26.0',2,157000,0,10),
(118,23,'26.5',3,157000,0,9),
(119,23,'27.0',4,157000,0,6),
(120,23,'27.5',5,157000,142000,4),

(121,24,'25.5',1,164000,0,8),
(122,24,'26.0',2,164000,0,10),
(123,24,'26.5',3,164000,0,9),
(124,24,'27.0',4,164000,0,6),
(125,24,'27.5',5,164000,149000,4),

(126,25,'25.5',1,153000,0,8),
(127,25,'26.0',2,153000,0,10),
(128,25,'26.5',3,153000,0,9),
(129,25,'27.0',4,153000,0,6),
(130,25,'27.5',5,153000,138000,4),

(131,26,'25.5',1,196000,0,8),
(132,26,'26.0',2,196000,0,10),
(133,26,'26.5',3,196000,0,9),
(134,26,'27.0',4,196000,0,6),
(135,26,'27.5',5,196000,176000,4),

(136,27,'25.5',1,152000,0,8),
(137,27,'26.0',2,152000,0,10),
(138,27,'26.5',3,152000,0,9),
(139,27,'27.0',4,152000,0,6),
(140,27,'27.5',5,152000,137000,4),

(141,28,'25.5',1,171500,0,8),
(142,28,'26.0',2,171500,0,10),
(143,28,'26.5',3,171500,0,9),
(144,28,'27.0',4,171500,0,6),
(145,28,'27.5',5,171500,154000,4),

(146,29,'25.5',1,186500,0,8),
(147,29,'26.0',2,186500,0,10),
(148,29,'26.5',3,186500,0,9),
(149,29,'27.0',4,186500,0,6),
(150,29,'27.5',5,186500,168000,4),

(151,30,'25.5',1,14700,0,8),
(152,30,'26.0',2,14700,0,10),
(153,30,'26.5',3,14700,0,9),
(154,30,'27.0',4,14700,0,6),
(155,30,'27.5',5,14700,12900,4),

(156,31,'25.5',1,7600,0,8),
(157,31,'26.0',2,7600,0,10),
(158,31,'26.5',3,7600,0,9),
(159,31,'27.0',4,7600,0,6),
(160,31,'27.5',5,7600,6900,4),

(161,32,'25.5',1,10600,0,8),
(162,32,'26.0',2,10600,0,10),
(163,32,'26.5',3,10600,0,9),
(164,32,'27.0',4,10600,0,6),
(165,32,'27.5',5,10600,9400,4),

(166,33,'25.5',1,16200,0,8),
(167,33,'26.0',2,16200,0,10),
(168,33,'26.5',3,16200,0,9),
(169,33,'27.0',4,16200,0,6),
(170,33,'27.5',5,16200,14600,4),

(171,34,'25.5',1,13600,0,8),
(172,34,'26.0',2,13600,0,10),
(173,34,'26.5',3,13600,0,9),
(174,34,'27.0',4,13600,0,6),
(175,34,'27.5',5,13600,11100,4),

(176,35,'25.5',1,8300,0,8),
(177,35,'26.0',2,8300,0,10),
(178,35,'26.5',3,8300,0,9),
(179,35,'27.0',4,8300,0,6),
(180,35,'27.5',5,8300,7000,4),

(181,36,'25.5',1,17500,0,8),
(182,36,'26.0',2,17500,0,10),
(183,36,'26.5',3,17500,0,9),
(184,36,'27.0',4,17500,0,6),
(185,36,'27.5',5,17500,15600,4),

(186,37,'25.5',1,9800,0,8),
(187,37,'26.0',2,9800,0,10),
(188,37,'26.5',3,9800,0,9),
(189,37,'27.0',4,9800,0,6),
(190,37,'27.5',5,9800,9300,4),

(191,38,'25.5',1,11700,0,8),
(192,38,'26.0',2,11700,0,10),
(193,38,'26.5',3,11700,0,9),
(194,38,'27.0',4,11700,0,6),
(195,38,'27.5',5,11700,8800,4),

(196,39,'25.5',1,14000,0,8),
(197,39,'26.0',2,14000,0,10),
(198,39,'26.5',3,14000,0,9),
(199,39,'27.0',4,14000,0,6),
(200,39,'27.5',5,14000,11300,4),

(201,40,'25.5',1,17700,0,8),
(202,40,'26.0',2,17700,0,10),
(203,40,'26.5',3,17700,0,9),
(204,40,'27.0',4,17700,0,6),
(205,40,'27.5',5,17700,16900,4),

(206,41,'25.5',1,20000,0,8),
(207,41,'26.0',2,20000,0,10),
(208,41,'26.5',3,20000,0,9),
(209,41,'27.0',4,20000,0,6),
(210,41,'27.5',5,20000,18500,4),

(211,42,'25.5',1,16100,0,8),
(212,42,'26.0',2,16100,0,10),
(213,42,'26.5',3,16100,0,9),
(214,42,'27.0',4,16100,0,6),
(215,42,'27.5',5,16100,14000,4),

(216,43,'25.5',1,19000,0,8),
(217,43,'26.0',2,19000,0,10),
(218,43,'26.5',3,19000,0,9),
(219,43,'27.0',4,19000,0,6),
(220,43,'27.5',5,19000,16300,4),

(221,44,'25.5',1,15600,0,8),
(222,44,'26.0',2,15600,0,10),
(223,44,'26.5',3,15600,0,9),
(224,44,'27.0',4,15600,0,6),
(225,44,'27.5',5,15600,12400,4),

(226,45,'25.5',1,13100,0,8),
(227,45,'26.0',2,13100,0,10),
(228,45,'26.5',3,13100,0,9),
(229,45,'27.0',4,13100,0,6),
(230,45,'27.5',5,13100,10800,4),

(231,46,'25.5',1,12800,0,8),
(232,46,'26.0',2,12800,0,10),
(233,46,'26.5',3,12800,0,9),
(234,46,'27.0',4,12800,0,6),
(235,46,'27.5',5,12800,10200,4);
-- -------------------------
-- Product Color Images (boolean -> 1/0)
-- -------------------------
INSERT INTO product_color_images (id, color_id, image_url, sort_order, is_main)
VALUES
-- Product 1
(1, 1, '/images/products/product1a.png', 1, 1),
(2, 1, '/images/products/product1b.png', 2, 0),
(3, 1, '/images/products/product1c.png', 3, 0),

(4, 2, '/images/products/product1d.png', 1, 1),
(5, 2, '/images/products/product1e.png', 2, 0),
(6, 2, '/images/products/product1f.png', 3, 0),

(7, 3, '/images/products/product1g.png', 1, 1),
(8, 3, '/images/products/product1h.png', 2, 0),
(9, 3, '/images/products/product1j.png', 3, 0),

-- Product 2
(10, 4, '/images/products/product2_0.png', 1, 1),
(11, 4, '/images/products/product2_1.png', 2, 0),
(12, 4, '/images/products/product2_2.png', 3, 0),

(13, 5, '/images/products/product2_3.png', 1, 1),
(14, 5, '/images/products/product2_4.png', 2, 0),
(15, 5, '/images/products/product2_5.png', 3, 0),

(16, 6, '/images/products/product2_6.png', 1, 1),
(17, 6, '/images/products/product2_7.png', 2, 0),
(18, 6, '/images/products/product2_8.png', 3, 0),

-- Product 3
(19, 7, '/images/products/product3_0.png', 1, 1),
(20, 7, '/images/products/product3_1.png', 2, 0),
(21, 7, '/images/products/product3_2.png', 3, 0),

(22, 8, '/images/products/product3_3.png', 1, 1),
(23, 8, '/images/products/product3_4.png', 2, 0),
(24, 8, '/images/products/product3_5.png', 3, 0),

(25, 9, '/images/products/product3_6.png', 1, 1),
(26, 9, '/images/products/product3_7.png', 2, 0),
(27, 9, '/images/products/product3_8.png', 3, 0),

-- Product 4
(28, 10, '/images/products/product4_0.png', 1, 1),
(29, 10, '/images/products/product4_1.png', 2, 0),
(30, 10, '/images/products/product4_2.png', 3, 0),

(31, 11, '/images/products/product4_3.png', 1, 1),
(32, 11, '/images/products/product4_4.png', 2, 0),
(33, 11, '/images/products/product4_5.png', 3, 0),

(34, 12, '/images/products/product4_6.png', 1, 1),
(35, 12, '/images/products/product4_7.png', 2, 0),
(36, 12, '/images/products/product4_8.png', 3, 0),

-- Product 5
(37, 13, '/images/products/product5_0.png', 1, 1),
(38, 13, '/images/products/product5_1.png', 2, 0),
(39, 13, '/images/products/product5_2.png', 3, 0),

(40, 14, '/images/products/product5_3.png', 1, 1),
(41, 14, '/images/products/product5_4.png', 2, 0),
(42, 14, '/images/products/product5_5.png', 3, 0),

-- Product 6
(43, 15, '/images/products/product6_0.png', 1, 1),
(44, 15, '/images/products/product6_1.png', 2, 0),
(45, 15, '/images/products/product6_2.png', 3, 0),

-- Product 7
(46, 16, '/images/products/product7_0.png', 1, 1),
(47, 16, '/images/products/product7_1.png', 2, 0),
(48, 16, '/images/products/product7_2.png', 3, 0),

(49, 17, '/images/products/product7_3.png', 1, 1),
(50, 17, '/images/products/product7_4.png', 2, 0),
(51, 17, '/images/products/product7_5.png', 3, 0),

-- Product 8
(52, 18, '/images/products/product8_0.png', 1, 1),
(53, 18, '/images/products/product8_1.png', 2, 0),
(54, 18, '/images/products/product8_2.png', 3, 0),

-- Product 9
(55, 19, '/images/products/product9_0.png', 1, 1),
(56, 19, '/images/products/product9_1.png', 2, 0),
(57, 19, '/images/products/product9_2.png', 3, 0),

(58, 20, '/images/products/product9_3.png', 1, 1),
(59, 20, '/images/products/product9_4.png', 2, 0),
(60, 20, '/images/products/product9_5.png', 3, 0),

(61, 21, '/images/products/product9_6.png', 1, 1),
(62, 21, '/images/products/product9_7.png', 2, 0),
(63, 21, '/images/products/product9_8.png', 3, 0),

-- Product 10

(64, 22, '/images/products/product10_1.jpg', 1, 1),
(65, 22, '/images/products/product10_2.jpg', 2, 0),
(66, 22, '/images/products/product10_3.jpg', 3, 0),

(67, 23, '/images/products/product10_4.jpg', 1, 1),
(68, 23, '/images/products/product10_5.jpg', 2, 0),
(69, 23, '/images/products/product10_6.jpg', 3, 0),

(70, 24, '/images/products/product10_7.jpg', 1, 1),
(71, 24, '/images/products/product10_8.jpg', 2, 0),
(72, 24, '/images/products/product10_9.jpg', 3, 0),

(73, 25, '/images/products/product11_1.jpg', 1, 1),
(74, 25, '/images/products/product11_2.jpg', 2, 0),
(75, 25, '/images/products/product11_3.jpg', 3, 0),

(76, 26, '/images/products/product11_4.jpg', 1, 1),
(77, 26, '/images/products/product11_5.jpg', 2, 0),
(78, 26, '/images/products/product11_6.jpg', 3, 0),

(79, 27, '/images/products/product11_7.jpg', 1, 1),
(80, 27, '/images/products/product11_8.jpg', 2, 0),
(81, 27, '/images/products/product11_9.jpg', 3, 0),

(82, 28, '/images/products/product12_1.jpg', 1, 1),
(83, 28, '/images/products/product12_2.jpg', 2, 0),
(84, 28, '/images/products/product12_3.jpg', 3, 0),

(85, 29, '/images/products/product13_1.jpg', 1, 1),
(86, 29, '/images/products/product13_2.jpg', 2, 0),
(87, 29, '/images/products/product13_3.jpg', 3, 0),

(88, 30, '/images/products/product14_1.jpg', 1, 1),
(89, 30, '/images/products/product14_2.jpg', 2, 0),
(90, 30, '/images/products/product14_3.jpg', 3, 0),

(91, 31, '/images/products/product14_4.jpg', 1, 1),
(92, 31, '/images/products/product14_5.jpg', 2, 0),
(93, 31, '/images/products/product14_6.jpg', 3, 0),

(94, 32, '/images/products/product15_1.jpg', 1, 1),
(95, 32, '/images/products/product15_2.jpg', 2, 0),
(96, 32, '/images/products/product15_3.jpg', 3, 0),

(97, 33, '/images/products/product15_4.jpg', 1, 1),
(98, 33, '/images/products/product15_5.jpg', 2, 0),
(99, 33, '/images/products/product15_6.jpg', 3, 0),

(100, 34, '/images/products/product16_1.jpg', 1, 1),
(101, 34, '/images/products/product16_2.jpg', 2, 0),
(102, 34, '/images/products/product16_3.jpg', 3, 0),

(103, 35, '/images/products/product16_4.jpg', 1, 1),
(104, 35, '/images/products/product16_5.jpg', 2, 0),
(105, 35, '/images/products/product16_6.jpg', 3, 0),

(106, 36, '/images/products/product17_1.jpg', 1, 1),
(107, 36, '/images/products/product17_2.jpg', 2, 0),
(108, 36, '/images/products/product17_3.jpg', 3, 0),

(109, 37, '/images/products/product18_1.jpg', 1, 1),
(110, 37, '/images/products/product18_2.jpg', 2, 0),
(111, 37, '/images/products/product18_3.jpg', 3, 0),

(112, 38, '/images/products/product18_4.jpg', 1, 1),
(113, 38, '/images/products/product18_5.jpg', 2, 0),
(114, 38, '/images/products/product18_6.jpg', 3, 0),

(115, 39, '/images/products/product18_7.jpg', 1, 1),
(116, 39, '/images/products/product18_8.jpg', 2, 0),
(117, 39, '/images/products/product18_9.jpg', 3, 0),

(118, 40, '/images/products/product19_1.jpg', 1, 1),
(119, 40, '/images/products/product19_2.jpg', 2, 0),
(120, 40, '/images/products/product19_3.jpg', 3, 0),

(121, 41, '/images/products/product20_1.jpg', 1, 1),
(122, 41, '/images/products/product20_2.jpg', 2, 0),
(123, 41, '/images/products/product20_3.jpg', 3, 0),

(124, 42, '/images/products/product20_4.jpg', 1, 1),
(125, 42, '/images/products/product20_5.jpg', 2, 0),
(126, 42, '/images/products/product20_6.jpg', 3, 0),

(127, 43, '/images/products/product20_7.jpg', 1, 1),
(128, 43, '/images/products/product20_8.jpg', 2, 0),
(129, 43, '/images/products/product20_9.jpg', 3, 0),

(130, 44, '/images/products/product21_1.jpg', 1, 1),
(131, 44, '/images/products/product21_2.jpg', 2, 0),
(132, 44, '/images/products/product21_3.jpg', 3, 0),

(133, 45, '/images/products/product21_4.jpg', 1, 1),
(134, 45, '/images/products/product21_5.jpg', 2, 0),
(135, 45, '/images/products/product21_6.jpg', 3, 0),

(136, 46, '/images/products/product21_7.jpg', 1, 1),
(137, 46, '/images/products/product21_8.jpg', 2, 0),
(138, 46, '/images/products/product21_9.jpg', 3, 0);

-- -------------------------
-- Extra demo users (boolean -> 1/0)
-- -------------------------
INSERT IGNORE INTO users (
    id, email, password, full_name, email_verified, status, created_at
) VALUES
(2, 'customer@shopping.com', '$2a$10$SevTMkyFqXN39jTrszWhS.2FwudX6ZDaH8.U1sofCoVn3tkXl0JQy', 'Demo Customer', 1, 'ACTIVE', NOW()),
(3, 'user3@shopping.com',    '$2a$10$SevTMkyFqXN39jTrszWhS.2FwudX6ZDaH8.U1sofCoVn3tkXl0JQy', 'User Three',    1, 'ACTIVE', NOW());

INSERT IGNORE INTO user_roles (user_id, role_id)
VALUES
(2, 2),
(3, 2);

-- -------------------------
-- Reviews / Stats
-- -------------------------
INSERT INTO product_reviews (
    product_id, user_id, rating, title, image_url, content, status, created_at
) VALUES
(1, 1, 5, 'とても軽くて快適！', NULL,
 '毎日のランニングで使用しています。とても軽くてクッション性も良いので足が疲れにくいです。おすすめ！',
 'ACTIVE', NOW()),
(1, 2, 4, 'コスパ最高', NULL,
 '値段以上の品質です。通気性も良くて夏でも快適に走れます。',
 'ACTIVE', NOW()),
(1, 3, 5, '最高のランニング体験', '/assets/images/reviews/r1.jpg',
 'クッションが素晴らしく、膝への負担が本当に減りました。',
 'ACTIVE', NOW()),

(2, 1, 4, 'デザインが最高', NULL,
 'どんな服にも合わせやすくて外出が楽しくなりました。サイズ感もぴったり。',
 'ACTIVE', NOW()),
(2, 2, 5, '歩きやすい！', NULL,
 '長時間歩いても疲れにくいです。普段使いに最適です。',
 'ACTIVE', NOW()),
(2, 3, 4, '普段使いにちょうどいい', '/assets/images/reviews/r2.jpg',
 '通勤にもプライベートにも使いやすいスニーカーです。',
 'ACTIVE', NOW()),

(3, 1, 5, '安定感バツグン', '/assets/images/reviews/r3.jpg',
 'ジムでのトレーニングに使用。グリップ力が高くて安心して動けます。',
 'ACTIVE', NOW()),
(3, 2, 4, 'とても使いやすい', NULL,
 '室内トレーニングとの相性が良い。フィット感が良くて気に入っています。',
 'ACTIVE', NOW()),
(3, 3, 5, '気に入りました', NULL,
 'フィット感が最高で、毎日のトレーニングが楽しくなりました。',
 'ACTIVE', NOW()),

(4, 1, 4, '着心地が良い', NULL,
 '肌触りがよくて軽い素材。運動中もストレスがありません。',
 'ACTIVE', NOW()),
(4, 2, 3, '悪くない', NULL,
 '少し大きめのサイズですが、動きやすいので満足しています。',
 'ACTIVE', NOW()),
(4, 3, 5, 'リピート確定', '/assets/images/reviews/r4.jpg',
 '動きやすく、ランニングでもジムでも使える優秀なウェアです。',
 'ACTIVE', NOW()),

(5, 1, 5, 'おしゃれで快適', '/assets/images/reviews/r5.jpg',
 'デザインが本当に良くて、街歩きに最適です。',
 'ACTIVE', NOW()),
(5, 2, 4, '普段使いに最高！', NULL,
 'スタイリッシュで歩きやすく、毎日こればかり履いています。',
 'ACTIVE', NOW()),
(5, 3, 5, '買って良かった', NULL,
 '軽くて履き心地が良い。サイズもぴったりで大満足です。',
 'ACTIVE', NOW());

INSERT INTO product_review_stats (
    product_id,
    created_at,
    created_by,
    updated_at,
    updated_by,
    average_rating,
    rating1, rating2, rating3, rating4, rating5,
    review_count
) VALUES
(1, NOW(), NULL, NOW(), NULL, 4.67, 0, 0, 0, 1, 2, 3),
(2, NOW(), NULL, NOW(), NULL, 4.33, 0, 0, 0, 2, 1, 3),
(3, NOW(), NULL, NOW(), NULL, 4.67, 0, 0, 0, 1, 2, 3),
(4, NOW(), NULL, NOW(), NULL, 4.00, 0, 0, 1, 1, 1, 3),
(5, NOW(), NULL, NOW(), NULL, 4.67, 0, 0, 0, 1, 2, 3);
