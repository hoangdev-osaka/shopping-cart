import { API_BASE } from "../api/config.js";

const token = localStorage.getItem("token");

const postalInput = document.getElementById("postalCode");
const prefectureInput = document.getElementById("prefecture");
const municipality = document.getElementById("municipality");
const postalField = document.querySelector(".postal-field");
const form = document.querySelector(".information__address");
const loadingSpin = document.getElementById("addressLoading");
const deliveryComfirm = document.getElementById("deliveryComfirm");
const deliveryAddress = document.getElementById("deliveryAddress");
const addressEl = document.querySelector(".address-confirm");
const checkoutSummaryBody = document.querySelector(".checkout-summary__body");
const priceTotal = document.querySelector(".price-total");
const informationEmail = document.querySelector(".information__email");
loadDefaultAddress();
loadCheckoutSummaryBody();
loadInformationEmail();
document.addEventListener("click", async (e) => {
  const el = e.target.closest("[data-action]");
  if (!el) return;
  switch (el.dataset.action) {
    case "open-payment":
      const paymentEl = document.querySelector(".payment-confirm");
      document.getElementById("paymentTitle")?.classList.remove("text-muted");

      paymentEl.classList.remove("hidden");
      paymentEl.scrollIntoView({
        behavior: "smooth",
        block: "start",
      });
      paymentEl.focus({ preventScroll: true });
      break;
    case "confirm-order":
      const result = await createOrder();
      if (result?.orderNumber) {
        window.location.href = `/pages/cart/complete.html?orderNumber=${result.orderNumber}`;
      }
      break;
    case "edit-address":
      form.classList.remove("hidden");

      break;
  }
});
postalInput.addEventListener("input", async () => {
  const zip = (postalInput.value || "").replace(/[^0-9]/g, "").trim();

  if (zip.length !== 7) return;

  postalField.classList.add("loading");

  try {
    const res = await fetch(`https://zipcloud.ibsnet.co.jp/api/search?zipcode=${zip}`);
    if (!res.ok) return;

    const data = await res.json();
    const r = data.results?.[0];
    console.log(JSON.stringify(r, null, 4));
    if (!r) return;
    await new Promise((resolve) => setTimeout(resolve, 1000));
    prefectureInput.value = r.address1 || "";
    municipality.value = r.address2 + r.address3 || "";
  } catch (e) {
    console.log("postal lookup failed", e);
  } finally {
    postalField.classList.remove("loading");
  }
});
async function loadInformationEmail() {
  try {
    const res = await fetch(`${API_BASE}/api/my-account`, {
      headers: {
        "Content-type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    if (!res.ok) return;

    const data = await res.json();
    informationEmail.innerHTML = `${data.email}`;
  } catch (e) {
    console.log("postal lookup failed", e);
  } finally {
  }
}
form.addEventListener("submit", async (e) => {
  e.preventDefault();
  form.classList.add("hidden");
  loadingSpin.classList.remove("hidden");
  const formData = new FormData(form);
  const addressData = Object.fromEntries(formData.entries());

  try {
    const res = await fetch(`${API_BASE}/api/my-account/addresses`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(addressData),
    });
    if (!res.ok) return;
    const data = await res.json();
    renderConfirmAddress(data);
    console.table(data);
  } catch (e) {
    console.log("postal lookup failed", e);
  } finally {
    await new Promise((resolve) => setTimeout(resolve, 500));
    loadingSpin.classList.add("hidden");
    addressConfirm.classList.remove("hidden");
    deliveryAddress.classList.remove("text-muted");
    deliveryComfirm.classList.remove("hidden");
  }
});
async function loadDefaultAddress() {
  console.log("loadDefaultAddress start");
  loadingSpin.classList.remove("hidden");
  await new Promise((resolve) => setTimeout(resolve, 500));

  try {
    const res = await fetch(`${API_BASE}/api/my-account/addresses/default`, {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    console.log("res.status =", res.status);
    if (!res.ok) return;
    const data = await res.json();
    const r = data;
    if (!r) return;
    form.classList.add("hidden");
    for (const [key, value] of Object.entries(r)) {
      const el = form.elements[key];
      if (!el) continue;
      el.value = value ?? "";
    }
    addressConfirm.classList.remove("hidden");
    deliveryComfirm.classList.remove("hidden");

    console.log("loadDefaultAddress success");
    renderConfirmAddress(r);
  } catch (e) {
    console.log(e);
    deliveryComfirm.classList.add("hidden");
    deliveryAddress.classList.add("hidden");
  } finally {
    loadingSpin.classList.add("hidden");

    deliveryAddress.classList.remove("text-muted");
  }
}
async function loadCheckoutSummaryBody() {
  try {
    const res = await fetch(`${API_BASE}/api/my-cart`, {
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    if (!res.ok) return;
    const data = await res.json();
    renderCheckoutSummaryBody(data);
    renderPriceTotal(data);
  } catch (e) {
  } finally {
  }
}
function renderPriceTotal(data) {
  priceTotal.innerHTML = `
      <span>(${data.totalQuantity}点) </span><span>${yen(data.grandTotal)}</span>
  `;
}

function yen(n) {
  const v = Number(n);
  if (!v) return `無料`;
  return `¥${v.toLocaleString()}`;
}
function renderCheckoutSummaryBody(data) {
  checkoutSummaryBody.innerHTML = `
            <div class="cart-summary__row">
              <span class="cart-summary__label">${data.totalQuantity}点</span>
              <span class="cart-summary__value" id="cartSubtotal">${yen(data.grandTotal)}</span>
            </div>
  
            <div class="cart-summary__row">
              <span class="cart-summary__label">送料</span>
              <span class="cart-summary__value" id="cartShipping">無料</span>
            </div>
  
            <div class="cart-summary__divider"></div>
  
            <div class="cart-summary__row cart-summary__row--total">
              <span class="cart-summary__label">合計</span>
              <span class="cart-summary__value" id="cartTotal">${yen(data.grandTotal)}</span>
            </div>
            <div class="cart-summary__products">
              ${renderCart(data.cartItems)}
            </div>
    `;
}
function renderCart(cartItems) {
  return cartItems
    .map(
      (e) =>
        `
    <div class="checkout-item">
      <img class="checkout-item__img" src="${API_BASE}${e.imageUrl}">
      <div class="checkout-item__body">
        <div class="checkout-item__body-name">${e.nameLabel}</div>
        <div class="checkout-item__body-variant"><span>サイズ:${e.sizeLabel}cm </span><span>/ 数量:${e.quantity}</span></div>
        <div class="checkout-item__body-variant" >カラー:${e.colorLabel}</div>
      </div>
    </div>
  `
    )
    .join("");
}
function renderConfirmAddress(address) {
  addressEl.dataset.addressId = address.id;
  console.log(address.id);
  addressEl.querySelector(".confirm__name--last-name").textContent = address.lastName;
  addressEl.querySelector(".confirm__name--last-name").textContent = address.lastName;
  addressEl.querySelector(".confirm__name--first-name").textContent = address.firstName;
  addressEl.querySelector(".confirm__postal-code").textContent = "〒" + address.postalCode;
  addressEl.querySelector(".confirm__address1").textContent = address.fullAddress;
  addressEl.querySelector(".confirm__address2").textContent = address.building;
  addressEl.querySelector(".confirm__phone").textContent = "TEL: " + address.phone;
}

async function createOrder() {
  const id = addressEl.dataset.addressId;
  const checked = document.querySelector("input[name='paymentMethod']:checked");
  const method = checked?.value;
  const btn = document.querySelector(".payment-btn");
  if (!btn) return;
  const order = {
    addressId: id,
    paymentMethod: method,
    note: "",
  };
  try {
    btn.classList.add("is-loading");
    btn.disabled = true;
    document.querySelector(".btn-text").innerHTML = "";
    const res = await fetch(`${API_BASE}/api/my-cart/checkout`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(order),
    });
    const data = await res.json();
    if (!res.ok) {
      throw data;
    }
    return data;
    console.log(JSON.stringify(data, null, 4));
  } catch (e) {
    await new Promise((r) => setTimeout(r, 1000));
    const errors = e.errors || {};
    const paymentErrorKey = errors.paymentMethod;
    console.log(JSON.stringify(e, null, 4));
    if (errors.paymentMethod === "error.order.payment-method.not-null") {
      document.querySelector(".checkout-log").innerHTML = "※お支払い方法を選択してください。";
    } else if (e.detail === "error.cart.empty") {
      document.querySelector(".checkout-log").innerHTML = "※カートが空いています。";
    } else {
      document.querySelector(".checkout-log").innerHTML = "※注文処理に失敗しました。";
    }
  } finally {
    btn.classList.remove("is-loading");
    btn.disabled = false;
    document.querySelector(".btn-text").innerHTML = "注文を確認する";
  }
}
