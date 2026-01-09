import { API_BASE } from "../api/config.js";
import { loadCartBadge } from "../core/render/loadCartBadge.js";

const token = localStorage.getItem("token");

const url = new URL(window.location.href);
const slug = url.searchParams.get("slug");
const colorId = url.searchParams.get("colorId");
if (!slug) {
  alert("商品が見つかりません。");
  throw new Error("Missing product slug");
}
const DEFAULT_IMG = "/assets/images/default/no-image.png";

const nameEl = document.getElementById("productName");

const mainImgEl = document.querySelector(".product-img__main img");
const thumbWrapEl = document.querySelector(".product-img__thumbnails");

const priceCurrentEl = document.querySelector(".price-current");
const priceOldEl = document.querySelector(".price-old");
const discountEl = document.querySelector(".discount-percent");

const colorListEl = document.getElementById("colorList");
const sizeListEl = document.querySelector(".size-selector");
const addToCartBtn = document.getElementById("addToCartBtn");
const productDescriptionEl = document.getElementById("productDescription");
const reviewListEl = document.getElementById("reviewList");

let currentProduct = null;
let currentColor = null;
let currentVariant = null;

function formatYen(n) {
  const num = Number(n);
  if (!Number.isFinite(num)) return "";
  return `¥${num.toLocaleString()}`;
}

function resolveImageUrl(path) {
  if (!path || path === "null" || path === "undefined") return DEFAULT_IMG;

  if (/^https?:\/\//i.test(path)) return path;

  if (path.startsWith("/")) return API_BASE + path;

  return API_BASE + "/" + path;
}

function bindImageFallback(imgEl) {
  if (!imgEl) return;
  imgEl.onerror = function () {
    this.onerror = null;
    this.src = DEFAULT_IMG;
  };
}

function formatDateJP(isoInstant) {
  if (!isoInstant) return "";
  const d = new Date(isoInstant);
  if (Number.isNaN(d.getTime())) return "";
  return d.toLocaleDateString("ja-JP", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
}

fetch(`${API_BASE}/api/products/${encodeURIComponent(slug)}`, {
  headers: { Accept: "application/json" },
})
  .then(async (res) => {
    const data = await res.json().catch(() => null);
    if (!res.ok) throw new Error(String(res.status));
    return data?.content || data;
  })
  .then((product) => {
    currentProduct = product;
    renderProduct(product);
  })
  .catch((e) => {
    console.error(e);
    alert("システムエラーが発生しました。");
  });

function renderProduct(product) {
  nameEl.textContent = product?.name ?? "";

  renderColors(product?.colors || [], colorId);
  renderReviewSection(product);
  renderReviewList(product?.reviews || []);
  renderDescription(product?.description ?? "");

  const selected = product?.colors?.find((c) => c.id == colorId) ?? product?.colors?.[0];

  if (selected) selectColor(selected);

  syncAddToCartBtn();
}
function renderDescription(description) {
  if (!productDescriptionEl) return;
  productDescriptionEl.textContent = description ?? "";
}

function renderColors(colors, selectedColorId) {
  colorListEl.innerHTML = "";

  colors.forEach((color) => {
    const id = `color-${color.id}`;

    const input = document.createElement("input");
    input.type = "radio";
    input.name = "color";
    input.id = id;
    input.value = String(color.id);
    input.checked = String(color.id) === String(selectedColorId);

    const label = document.createElement("label");
    label.className = "color-option__item";
    label.htmlFor = id;

    const preview = document.createElement("img");
    preview.src = resolveImageUrl(color.images?.find((i) => i.main)?.imageUrl);
    preview.alt = color.name || "";
    bindImageFallback(preview);

    label.appendChild(preview);

    input.addEventListener("change", () => selectColor(color));

    colorListEl.appendChild(input);
    colorListEl.appendChild(label);
  });
}

function selectColor(color) {
  currentColor = color;

  renderImages(color.images || []);
  renderSizes(color.variants || []);

  const firstAvailable = (color.variants || []).find((v) => Number(v.stock) > 0);
  if (firstAvailable) {
    const input = document.getElementById(`size-${firstAvailable.id}`);
    if (input) input.checked = true;
    selectVariant(firstAvailable);
  } else {
    currentVariant = null;
    clearPrice();
  }

  syncAddToCartBtn();
}
function renderImages(images) {
  thumbWrapEl.innerHTML = "";
  const track = document.querySelector(".product-img__track");
  track.innerHTML = "";

  images.forEach((img) => {
    const slide = document.createElement("div");
    slide.className = "product-img__slide";

    const image = document.createElement("img");
    image.src = resolveImageUrl(img.imageUrl);
    bindImageFallback(image);

    slide.appendChild(image);
    track.appendChild(slide);

    const thumb = document.createElement("img");
    thumb.src = resolveImageUrl(img.imageUrl);
    thumb.className = "product-img__thumbnail";
    bindImageFallback(thumb);

    thumb.addEventListener("mousedown", (e) => e.preventDefault()); 

    thumb.addEventListener("click", () => {
      slide.scrollIntoView({ behavior: "smooth", block: "nearest", inline: "nearest" });
    });

    thumbWrapEl.appendChild(thumb);
  });
}


function renderSizes(variants) {
  sizeListEl.innerHTML = "";

  (variants ?? []).forEach((v) => {
    const id = `size-${v.id}`;

    const input = document.createElement("input");
    input.type = "radio";
    input.name = "size";
    input.id = id;
    input.value = String(v.id);

    const stock = Number(v.stock) || 0;
    const soldOut = stock <= 0;

    input.disabled = soldOut;
    input.addEventListener("change", () => selectVariant(v));

    const label = document.createElement("label");
    label.htmlFor = id;
    label.textContent = `${v.size}cm`;

    if (soldOut) label.classList.add("is-disabled", "is-sold-out");

    sizeListEl.appendChild(input);
    sizeListEl.appendChild(label);
  });
}

function selectVariant(variant) {
  currentVariant = variant;
  renderPrice(variant);
  syncAddToCartBtn();
}

function renderPrice(variant) {
  const regular = Number(variant?.regularPrice);
  const sale = Number(variant?.salePrice);

  if (!Number.isFinite(regular)) {
    clearPrice();
    return;
  }

  if (Number.isFinite(sale) && sale > 0 && sale < regular) {
    priceCurrentEl.textContent = formatYen(sale);
    priceOldEl.textContent = formatYen(regular);
    discountEl.textContent = `-${Math.round(((regular - sale) / regular) * 100)}% OFF`;
  } else {
    priceCurrentEl.textContent = formatYen(regular);
    priceOldEl.textContent = "";
    discountEl.textContent = "";
  }
}

function clearPrice() {
  priceCurrentEl.textContent = "";
  priceOldEl.textContent = "";
  discountEl.textContent = "";
}

function setBtnText(btn, text) {
  if (!btn) return;
  const textNode = [...btn.childNodes].find((n) => n.nodeType === Node.TEXT_NODE && n.textContent.trim());
  if (textNode) textNode.textContent = ` ${text} `;
}

function syncAddToCartBtn() {
  if (!addToCartBtn) return;
  addToCartBtn.disabled = !(token && currentVariant?.id);
}

addToCartBtn?.addEventListener("click", async () => {
  const btn = addToCartBtn;
  if (!btn || !token || !currentVariant?.id) return;

  const baseTextEl = btn.querySelector(".btn-text");
  const baseText = baseTextEl ? baseTextEl.textContent.trim() : btn.textContent.trim();

  btn.classList.add("loading");
  btn.disabled = true;
  setBtnText(btn, "追加中");

  await new Promise((r) => setTimeout(r, 250));
  await new Promise(requestAnimationFrame);

  try {
    const res = await fetch(`${API_BASE}/api/my-cart/items`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ variantId: currentVariant.id, quantity: 1 }),
    });

    if (!res.ok) {
      const err = await res.json().catch(() => null);
      console.log("ADD TO CART ERROR", res.status, err);
      setBtnText(btn, "失敗しました");
      return;
    }

    loadCartBadge();
  } catch (e) {
    console.error("[AddToCart] Failed:", e);
    setBtnText(btn, "失敗しました");
  } finally {
    btn.classList.remove("loading");
    setBtnText(btn, "追加完了");
    await new Promise((r) => setTimeout(r, 500));
    setBtnText(btn, baseText);
    syncAddToCartBtn();
  }
});

function renderReviewSection(product) {
  const avgEl = document.getElementById("avgRating");
  const countEl = document.getElementById("reviewCount");
  const starsEl = document.getElementById("avgStars");
  const breakdownEl = document.getElementById("ratingBreakdown");

  if (!avgEl || !countEl || !starsEl || !breakdownEl) return;

  const avg = Number(product?.averageRating || 0);
  const count = Number(product?.reviewCount || 0);
  const stats = product?.ratingStats || {};

  avgEl.textContent = avg.toFixed(1);
  countEl.textContent = `(${count}件のレビュー)`;

  renderStars(starsEl, avg);
  renderRatingBreakdown(breakdownEl, stats, count);
}

function renderStars(container, rating) {
  container.innerHTML = "";
  const full = Math.floor(Number(rating) || 0);

  for (let i = 1; i <= 5; i++) {
    const star = document.createElement("span");
    star.textContent = i <= full ? "★" : "☆";
    container.appendChild(star);
  }
}

function renderRatingBreakdown(container, stats, total) {
  container.innerHTML = "";

  for (let i = 5; i >= 1; i--) {
    const count = Number(stats?.[`rating${i}`] || 0);
    const percent = total > 0 ? (count / total) * 100 : 0;

    const row = document.createElement("div");
    row.className = "review-bar";
    row.innerHTML = `
      <span>${i}★</span>
      <div class="review-bar__track">
        <div class="review-bar__fill" style="width:${percent}%"></div>
      </div>
      <span>${count}</span>
    `;

    container.appendChild(row);
  }
}

function renderReviewList(reviews) {
  if (!reviewListEl) {
    console.warn("Missing #reviewList in HTML");
    return;
  }

  reviewListEl.innerHTML = "";

  if (!Array.isArray(reviews) || reviews.length === 0) {
    reviewListEl.innerHTML = `<p class="review-empty">まだレビューはありません。</p>`;
    return;
  }

  const html = reviews
    .map((r) => {
      const rating = Number(r?.rating || 0);
      const title = r?.title ?? "";
      const content = r?.content ?? "";
      const img = r?.imageUrl ? resolveImageUrl(r.imageUrl) : "";
      const userName = r?.userName ?? "匿名";
      const date = formatDateJP(r?.createdAt);

      const imgHtml = img
        ? `<img class="review-item__img" src="${img}" alt="review image"
              onerror="this.onerror=null;this.src='${DEFAULT_IMG}'" />`
        : "";

      return `
        <article class="review-item">
          <div class="review-item__header">
            <div class="review-item__meta">
              <strong class="review-item__title">${title}</strong>
              <div class="review-item__sub">
                <span class="review-item__user">${userName}</span>
                <span class="review-item__date">${date}</span>
              </div>
            </div>
            <div class="review-item__stars">
              ${"★".repeat(rating)}${"☆".repeat(Math.max(0, 5 - rating))}
            </div>
          </div>
          <p class="review-item__content">${content}</p>
          ${imgHtml}
        </article>
      `;
    })
    .join("");

  reviewListEl.insertAdjacentHTML("beforeend", html);
}
