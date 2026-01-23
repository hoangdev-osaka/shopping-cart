package hoang.shop.cart.service;

import hoang.shop.cart.dto.request.CheckoutRequest;
import hoang.shop.cart.dto.response.CartSummary;
import hoang.shop.cart.dto.response.ShippingEstimateResponse;
import hoang.shop.categories.model.ProductColorImage;
import hoang.shop.common.enums.ShippingRegion;
import hoang.shop.common.enums.status.AddressStatus;
import hoang.shop.common.enums.status.CartItemStatus;
import hoang.shop.common.enums.status.ProductVariantStatus;
import hoang.shop.identity.model.Address;
import hoang.shop.identity.repository.AddressRepository;
import hoang.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import hoang.shop.cart.dto.request.CartItemCreateRequest;
import hoang.shop.cart.dto.request.CartItemUpdateRequest;
import hoang.shop.cart.dto.response.CartResponse;
import hoang.shop.cart.mapper.CartItemMapper;
import hoang.shop.cart.mapper.CartMapper;
import hoang.shop.cart.model.Cart;
import hoang.shop.cart.model.CartItem;
import hoang.shop.cart.repository.CartItemRepository;
import hoang.shop.cart.repository.CartRepository;
import hoang.shop.categories.model.ProductVariant;
import hoang.shop.categories.repository.ProductVariantRepository;
import hoang.shop.common.enums.status.CartStatus;
import hoang.shop.common.exception.BadRequestException;
import hoang.shop.common.exception.NotFoundException;
import hoang.shop.identity.model.User;
import hoang.shop.identity.repository.UserRepository;
import hoang.shop.order.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final AddressRepository addressRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private static final Map<String, ShippingRegion> PREFECTURE_REGION_MAP = Map.ofEntries(
            // KANTO
            Map.entry("東京都", ShippingRegion.KANTO),
            Map.entry("神奈川県", ShippingRegion.KANTO),
            Map.entry("千葉県", ShippingRegion.KANTO),
            Map.entry("埼玉県", ShippingRegion.KANTO),
            Map.entry("茨城県", ShippingRegion.KANTO),
            Map.entry("栃木県", ShippingRegion.KANTO),
            Map.entry("群馬県", ShippingRegion.KANTO),

            // KANSAI
            Map.entry("大阪府", ShippingRegion.KANSAI),
            Map.entry("京都府", ShippingRegion.KANSAI),
            Map.entry("兵庫県", ShippingRegion.KANSAI),
            Map.entry("奈良県", ShippingRegion.KANSAI),
            Map.entry("滋賀県", ShippingRegion.KANSAI),
            Map.entry("和歌山県", ShippingRegion.KANSAI),

            // CHUBU
            Map.entry("愛知県", ShippingRegion.CHUBU),
            Map.entry("静岡県", ShippingRegion.CHUBU),
            Map.entry("新潟県", ShippingRegion.CHUBU),
            Map.entry("富山県", ShippingRegion.CHUBU),
            Map.entry("石川県", ShippingRegion.CHUBU),
            Map.entry("福井県", ShippingRegion.CHUBU),
            Map.entry("山梨県", ShippingRegion.CHUBU),
            Map.entry("長野県", ShippingRegion.CHUBU),
            Map.entry("岐阜県", ShippingRegion.CHUBU),

            // TOHOKU
            Map.entry("青森県", ShippingRegion.TOHOKU),
            Map.entry("岩手県", ShippingRegion.TOHOKU),
            Map.entry("宮城県", ShippingRegion.TOHOKU),
            Map.entry("秋田県", ShippingRegion.TOHOKU),
            Map.entry("山形県", ShippingRegion.TOHOKU),
            Map.entry("福島県", ShippingRegion.TOHOKU),

            // SHIKOKU
            Map.entry("徳島県", ShippingRegion.SHIKOKU),
            Map.entry("香川県", ShippingRegion.SHIKOKU),
            Map.entry("愛媛県", ShippingRegion.SHIKOKU),
            Map.entry("高知県", ShippingRegion.SHIKOKU),

            // KYUSHU
            Map.entry("福岡県", ShippingRegion.KYUSHU),
            Map.entry("佐賀県", ShippingRegion.KYUSHU),
            Map.entry("長崎県", ShippingRegion.KYUSHU),
            Map.entry("熊本県", ShippingRegion.KYUSHU),
            Map.entry("大分県", ShippingRegion.KYUSHU),
            Map.entry("宮崎県", ShippingRegion.KYUSHU),
            Map.entry("鹿児島県", ShippingRegion.KYUSHU),

            // HOKKAIDO / OKINAWA
            Map.entry("北海道", ShippingRegion.HOKKAIDO),
            Map.entry("沖縄県", ShippingRegion.OKINAWA)
    );
    private static final Map<ShippingRegion, int[]> REGION_DAYS = Map.of(
            ShippingRegion.KANTO, new int[]{1, 2},
            ShippingRegion.KANSAI, new int[]{1, 2},
            ShippingRegion.CHUBU, new int[]{1, 2},
            ShippingRegion.TOHOKU, new int[]{2, 3},
            ShippingRegion.SHIKOKU, new int[]{2, 3},
            ShippingRegion.KYUSHU, new int[]{2, 3},
            ShippingRegion.HOKKAIDO, new int[]{3, 4},
            ShippingRegion.OKINAWA, new int[]{3, 5}
    );

    @Override
    public CartResponse getMyCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("error.user.id.not-found"));
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> createEmptyCart(userId));
        return cartMapper.toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(Long userId, CartItemCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("error.user.id.not-found"));

        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> createEmptyCart(userId));

        ProductVariant variant = productVariantRepository.findById(request.variantId())
                .orElseThrow(() -> new NotFoundException("{error.product-variant.id.not-found}"));
        // 足すしかできない
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new BadRequestException("{error.cart-item.quantity.invalid}");
        }

        String nameLabel = variant.getColor().getProduct().getName()
                + " " + variant.getColor().getName()
                + " " + variant.getSize();


        List<ProductColorImage> images = variant.getColor().getImages();
        String imageUrl = null;
        if (images != null && !images.isEmpty()) {
            imageUrl = images.stream()
                    .filter(ProductColorImage::isMain)
                    .map(ProductColorImage::getImageUrl)
                    .findFirst()
                    .orElse("/uploads/default/no-image.png");
        }

        BigDecimal sale = variant.getSalePrice();
        BigDecimal regular = variant.getRegularPrice();

        BigDecimal unitPriceAtOrder =
                sale.compareTo(BigDecimal.ZERO) > 0
                        ? sale
                        : regular;

        if (unitPriceAtOrder.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("{error.product-variant.price.invalid}");
        }

        CartItem item = cartItemRepository
                .findByCart_IdAndProductVariant_IdAndStatus(cart.getId(), variant.getId(), CartItemStatus.ACTIVE)
                .orElse(null);
        Integer stock = variant.getStock();
        if (item == null) {
            BigDecimal lineTotal = unitPriceAtOrder.multiply(BigDecimal.valueOf(request.quantity()));
            if (request.quantity() > stock) throw new BadRequestException("{error.product-variant.stock.invalid}");
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(variant)
                    .sizeLabel(variant.getSize())
                    .nameLabel(nameLabel)
                    .colorLabel(variant.getColor().getName())
                    .hexLabel(variant.getColor().getHex())
                    .quantity(request.quantity())
                    .unitPriceAtOrder(unitPriceAtOrder)
                    .unitPriceBefore(variant.getRegularPrice())
                    .lineTotal(lineTotal)
                    .imageUrl(imageUrl)
                    .build();
            cart.getCartItems().add(newItem);
            newItem.setCart(cart);
            cartItemRepository.save(newItem);
        } else {
            int oldLineTotalQuantity = item.getQuantity();
            int freshLineTotalQuantity = oldLineTotalQuantity + request.quantity();
            if (freshLineTotalQuantity > stock) throw new BadRequestException("{error.product-variant.stock.invalid}");

            item.setUnitPriceAtOrder(unitPriceAtOrder);
            item.setUnitPriceBefore(variant.getRegularPrice());
            item.setQuantity(freshLineTotalQuantity);
            item.recalculateLineTotals();

        }
        List<Integer> items = cart.getCartItems().stream().map(CartItem::getQuantity).toList();
        int oldTotalQuantity = items.stream().reduce(0, Integer::sum);
        cart.setTotalQuantity(oldTotalQuantity);
        cart.recalculateTotals();
        cart.setCreatedBy(userId);
        cart.setLastActivityAt(Instant.now());
        cartRepository.save(cart);
        return cartMapper.toResponse(cart);
    }


    @Override
    public CartResponse updateItem(Long userId, Long itemId, CartItemUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("{error.user.id.not-found}"));

        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("error.cart.active.not-found"));
        CartItem item = cartItemRepository.findByCart_IdAndId(cart.getId(), itemId)
                .orElseThrow(() -> new NotFoundException("{error.cart-item.id.not-found}"));
        ProductVariant variant = productVariantRepository.findByIdAndStatus(item.getProductVariant().getId(), ProductVariantStatus.ACTIVE).orElseThrow(() -> new BadRequestException("{error.product-variant.id.not-found}"));
        Integer stock = variant.getStock();
        if (stock < request.quantity()) throw new BadRequestException("{error.product-variant.stock.invalid}");
        cartItemMapper.merge(request, item);
        cartItemRepository.save(item);
        item.recalculateLineTotals();
        cart.recalculateTotals();
        cart.setLastActivityAt(Instant.now());
        return cartMapper.toResponse(cart);
    }

    @Override
    public void removeItem(Long userId, Long itemId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("{error.cart-active.user-id.not-found}"));
        CartItem cartItem = cartItemRepository.findByCart_User_IdAndId(userId, itemId)
                .orElseThrow(() -> new NotFoundException("{error.item.id.not-found}"));
        cartItem.setCart(null);
        cart.getCartItems().remove(cartItem);
        cart.setLastActivityAt(Instant.now());
        cart.setUpdatedBy(userId);
        cart.recalculateTotals();

    }

    @Override
    public void clearItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("{error.user.id.not-found}"));
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> createEmptyCart(userId));
        if (cart.getCartItems() != null) {
            cart.getCartItems().clear();
        }
        cart.setGrandTotal(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    @Override
    public OrderResponse checkout(Long userId, CheckoutRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("{error.user.id.not-found}"));

        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("{error.card.user-id.not-found}"));

        return orderService.createOrderFromCart(user.getId(), request);
    }

    @Override
    public CartSummary getQuantity(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("{error.user.id.not-found}"));

        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("{error.card.user-id.not-found}"));

        List<CartItem> items = cartItemRepository.findByCart_Id(cart.getId());
        Integer quantity = items.stream().mapToInt(CartItem::getQuantity).sum();
        return new CartSummary(quantity);
    }

    @Override
    public ShippingEstimateResponse estimate(Long userId, Long addressId) {
        Address address = addressRepository.findByIdAndUser_IdAndStatus(addressId, userId, AddressStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("{error.address.id.not-found}"));
        ShippingRegion region = PREFECTURE_REGION_MAP.get(address.getPrefecture());
        int[] days = REGION_DAYS.getOrDefault(region, new int[]{2, 4});
        LocalDate from = LocalDate.now().plusDays(days[0]);
        LocalDate to = LocalDate.now().plusDays(days[1]);
        return new ShippingEstimateResponse(from, to);
    }


    private Cart createEmptyCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("{error.user.id.not-found}"));
        Cart cart = Cart.builder()
                .user(user)
                .build();
        return cartRepository.save(cart);
    }


}
