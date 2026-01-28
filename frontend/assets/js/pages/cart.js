import { API_BASE } from "../api/config.js";
import { loadCartBadge } from "../core/render/loadCartBadge.js";
import { checkLogin } from "../components/check-login.js";

const token = localStorage.getItem("token");

const elLoading = document.getElementById("cartLoading");
const elEmpty = document.getElementById("cartEmpty");
const elList = document.getElementById("cartList");

const elSub = document.getElementById("cartSub");
const elSubtotal = document.getElementById("cartSubtotal");
const elTaxAmount = document.getElementById("taxFee");
const elTotal = document.getElementById("cartTotal");
const elCheckoutBtn = document.getElementById("checkoutBtn");
const loadingEl = document.getElementById("loading");
const cartAlert = document.getElementById("cartAlert");

function yen(n) {
  const v = Number(n);
  if (!v) return `無料`;
  return `¥${v.toLocaleString()}`;
}

function showState({ loading = false, empty = false, list = false }) {
  elLoading.hidden = !loading;
  elEmpty.hidden = !empty;
  elList.hidden = !list;
}

function wait(ms) {
  return new Promise((r) => setTimeout(r, ms));
}

async function fetchText(url, options = {}) {
  const res = await fetch(url, options);
  const raw = await res.text();
  let data = null;
  try {
    data = raw ? JSON.parse(raw) : null;
  } catch {}
  return { res, raw, data };
}

function normalizeImageUrl(url) {
  if (!url) {
    return `${API_BASE}/uploads/default/no-image.png`;
  }
  if (url.startsWith("http://") || url.startsWith("https://")) {
    return url;
  }
  if (url.startsWith("/")) {
    return `${API_BASE}${url}`;
  }
  return `${API_BASE}/${url}`;
}

function renderCart(cart) {
  const items = cart?.cartItems || [];
  const lineCount = items.length;
  const totalQty = Number(cart?.totalQuantity || 0);
  loadCartBadge();

  if (lineCount === 0) {
    elSub.textContent = "カートは空です";
    showState({ empty: true });
    elCheckoutBtn.disabled = true;

    elSubtotal.textContent = yen(0);
    elShipping.textContent = yen(0);
    elTotal.textContent = yen(0);
    return;
  }

  elSub.textContent = `${lineCount}件（合計${totalQty}点）`;
  showState({ list: true });

  elList.innerHTML = items
    .map((it) => {
      const itemId = it.cartItemId;
      const img = normalizeImageUrl(it.imageUrl);
      const name = it.nameLabel || "商品";
      const size = it.sizeLabel ? `${it.sizeLabel}cm` : "";
      const color = it.colorLabel || "";
      const qty = Number(it.quantity ?? 1);
      const unit = Number(it.unitPriceAtOrder ?? it.unitPriceBefore ?? 0);
      const lineTotal = Number(it.lineTotal ?? unit * qty);

      return `
      <article class="cart-item" data-item-id="${itemId}">
        <a href = "/pages/products/product-detail.html?slug=${encodeURIComponent(it.productSlug)}">
        <img class="cart-item__img" src="${img}" alt="${name}">
        </a>
        <div class="cart-item__info">
          <a href = "/pages/products/product-detail.html?slug=${encodeURIComponent(it.productSlug)}">
          <div class="cart-item__name" title="${name}">${name}</div></a>
          <div class="cart-item__meta">
            ${color ? `<span>カラー: ${color}</span>` : ""}
            ${size ? `<span>サイズ: ${size}</span>` : ""}
          </div>
          <div class="cart-item__meta">
            <span>単価: ${yen(unit)}</span>
          </div>
        </div>

        <div class="cart-item__actions">
          <div class="cart-item__price">${yen(lineTotal)}</div>

          <div class="cart-item__qty">
            <button class="cart-qty__btn" data-action="dec" type="button">−</button>
            <input class="cart-qty__input" data-action="input" type="number" min="1" max="5" value="${qty}">
            <button class="cart-qty__btn" data-action="inc" type="button">＋</button>
          </div>

          <button class="cart-item__remove" data-action="remove" type="button">削除</button>
        </div>
      </article>
    `;
    })
    .join("");

  const subtotalAmount = Number(cart?.subtotalAmount);
  const taxAmount = Number(cart?.taxAmount);

  const grandTotal = Number(cart?.grandTotal);

  elSubtotal.textContent = yen(subtotalAmount);
  elTaxAmount.textContent = yen(taxAmount);
  elTotal.textContent = yen(grandTotal);

  elCheckoutBtn.disabled = false;
}

async function loadCart() {
  const user = await checkLogin();
  if (!user) {
    cartAlert.textContent = "ログインしてください！";

    showState({ empty: true });
    elCheckoutBtn.disabled = true;
    return;
  }

  showState({ loading: true });
  elCheckoutBtn.disabled = true;

  const start = Date.now();

  try {
    loadingEl.classList.toggle("hidden");
    const { res, data, raw } = await fetchText(`${API_BASE}/api/my-cart`, {
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    const elapsed = Date.now() - start;
    if (elapsed < 300) await wait(300 - elapsed);

    if (!res.ok) {
      elSub.textContent = `読み込み失敗 (${res.status})`;
      console.log("CART LOAD ERROR", raw);
      showState({ empty: true });
      return;
    }

    renderCart(data);
  } catch (e) {
    console.log(e);
  } finally {
    loadingEl.classList.toggle("hidden");
  }
}

async function updateQty(cartItemId, newQty) {
  const qty = Math.max(1, Number(newQty || 1));

  try {
    const { res, raw } = await fetchText(`${API_BASE}/api/my-cart/items/${cartItemId}`, {
      method: "PUT",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ quantity: qty }),
    });

    if (!res.ok) {
      console.log("UPDATE QTY ERROR", raw);
      throw new Error();
    }
    return true;
  } catch (e) {
    loadCart();
    cartAlert.textContent = "在庫数を超えています";

    return false;
  }
}

async function removeItem(cartItemId) {
  const { res, raw } = await fetchText(`${API_BASE}/api/my-cart/items/${cartItemId}`, {
    method: "DELETE",
    headers: {
      Accept: "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!res.ok) {
    console.log("REMOVE ERROR", raw);
    return false;
  }
  return true;
}

elList?.addEventListener("click", async (e) => {
  const btn = e.target.closest("[data-action]");
  if (!btn) return;

  const action = btn.dataset.action;
  const itemEl = btn.closest(".cart-item");
  const itemId = itemEl?.dataset.itemId;
  if (!itemId) return;

  const input = itemEl.querySelector('.cart-qty__input[data-action="input"]');
  if (!input) return;

  const min = Number(input.min || 1);
  const max = Number(input.max || 999999);

  const clamp = (v) => Math.min(Math.max(v, min), max);

  if (action === "inc" || action === "dec") {
    const cur = Number(input.value || min);
    const rawNext = action === "inc" ? cur + 1 : cur - 1;
    const next = clamp(rawNext);

    if (next === cur) {
      return;
    }

    input.value = String(next);

    const ok = await updateQty(itemId, next);

    if (!ok) {
      input.value = String(cur);
      return;
    }

    loadCart();
    return;
  }

  if (action === "remove") {
    itemEl.style.opacity = "0.6";
    const ok = await removeItem(itemId);
    if (ok) loadCart();
    else itemEl.style.opacity = "1";
  }
});

elList?.addEventListener("change", async (e) => {
  const input = e.target.closest('.cart-qty__input[data-action="input"]');
  if (!input) return;

  const itemEl = input.closest(".cart-item");
  const itemId = itemEl?.dataset.itemId;
  if (!itemId) return;

  const ok = await updateQty(itemId, input.value);
  if (ok) loadCart();
});

elCheckoutBtn?.addEventListener("click", async () => {
  elCheckoutBtn.classList.add("loading");
  elCheckoutBtn.disabled = true;

  await wait(100);

  elCheckoutBtn.classList.remove("loading");
  elCheckoutBtn.disabled = false;

  window.location.href = "/pages/cart/checkout.html";
});
loadCart();
