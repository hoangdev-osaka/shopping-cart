import { API_BASE } from "../api/config.js";
const token = localStorage.getItem("token");
const paymentMethodEl = document.getElementById("paymentMethod");
const fullAddressEl = document.getElementById("shippingAddress");
const subtotalEl = document.getElementById("subtotal");
const shippingFeeEl = document.getElementById("shippingFee");
const totalEl = document.getElementById("total");
const taxFeeEl = document.getElementById("taxFee");
const itemsBlockEl = document.getElementById("itemsBlock");
const url = new URL(window.location.href);
const orderNumber = url.searchParams.get("orderNumber");
const orderCodeEl = document.getElementById("orderCode");
const discountAmountEl = document.getElementById("discountAmount");
const grandTotalEl = document.getElementById("grandTotal");
const statusTextEl = document.getElementById("statusText");

const order = await loadComplete();
orderCodeEl.innerHTML = `${order.orderNumber}`;
paymentMethodEl.innerHTML = `${order.paymentMethod}`;
fullAddressEl.innerHTML = `${order.fullAddress} `;
grandTotalEl.innerHTML = `${yen(order.grandTotal)}`;
subtotalEl.innerHTML = `${yen(order.subtotalAmount)}`;
shippingFeeEl.innerHTML = `${yen(order.shippingFee)}`;
totalEl.innerHTML = `${yen(order.grandTotal)}`;
taxFeeEl.innerHTML = `${yen(order.taxAmount)}`;
discountAmountEl.innerHTML = `- ¥${order.discountAmount}`;
statusTextEl.innerHTML = `${order.orderStatus}`;

renderOrderItems(order);

async function loadComplete() {
  const res = await fetch(`${API_BASE}/api/my-order/orders/${orderNumber}`, {
    headers: {
      Accept: "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
  const data = await res.json();
  console.log(data, null, 4);
  return data;
}
function yen(n) {
  const v = Number(n);
  if (!v) return `無料`;
  return `¥${v.toLocaleString()}`;
}
function renderOrderItems(order) {
  const items = order.items;
  itemsBlockEl.innerHTML = items
    .map((it) => {
      return `
      <div class="order-item">
        <img class="order-item__img" src="${API_BASE}${it.imageUrl}">
        <div class="order-product-info">
          <div class="order-product-info__name">${it.productName}</div>
          <div><span>サイズ:${it.sizeName}</span> <span>/ 数量:${it.quantity}</span></div>
          <div>カラー:${it.colorName}</div>
        </div>
      </div>
    
    
    `;
    })
    .join("");
}
