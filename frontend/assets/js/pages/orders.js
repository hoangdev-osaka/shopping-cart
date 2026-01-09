import { API_BASE } from "../api/config.js";
import { loadCartBadge } from "../core/render/loadCartBadge.js";
import { renderProducts } from "../core/render/product-renderer.js";
const ordersListEl = document.getElementById("ordersList");
const loadingEl = document.getElementById("addressLoading")
const token = localStorage.getItem("token");
const orders = await loadOrders();
renderOrder(orders);
async function loadOrders() {
  try {
    const res = await fetch(`${API_BASE}/api/my-order/orders`, {
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    if (res.ok) {
      loadingEl.classList.toggle("hidden");
      const data = await res.json();
      console.log(JSON.stringify(data, null, 4));
      return data.content;
    }
    return false;
  } catch (e) {
    console.log(e);
  }finally{
    loadingEl.classList.toggle("hidden")
  }
}
function renderOrder(orders) {
  const html = orders
    .map((order) => {
      return `
    <article class="order-card">
                <div class="order-card__top">
                  <div class="order-meta">
                    <div class="order-no">
                      <span class="muted">注文番号</span>
                      <strong>${order.orderNumber}</strong>
                      <button class="pill-btn" type="button">コピー</button>
                    </div>
                    <div class="order-sub">
                      <span class="muted">注文日</span>
                      <span>${formatJst(order.placedAt)}</span>
                    </div>
                  </div>

                  <div class="order-pills">
                    <span class="pill pill--money">合計 ${yen(order.grandTotal)}</span>
                    <span class="pill pill--status">ステータス ${order.orderStatus}</span>
                  </div>
                </div>

                <div class="order-card__body">
                  <section class="order-left">
                    <h3 class="section-title">ご注文内容</h3>

                    ${renderOrderItem(order)}
                  </section>

                  <aside class="order-right">
                    <h3 class="section-title">配送・支払い</h3>

                    <div class="kv">
                      <div class="kv-row">
                        <span class="muted">受取人</span>
                        <strong>${order.name}</strong>
                      </div>
                      <div class="kv-row">
                        <span class="muted">郵便番号</span>
                        <strong>${order.postalCode}</strong>
                      </div>
                    
                      <div class="kv-row">
                        <span class="muted">配送先</span>
                        
                        <strong>${order.fullAddress}</strong>
                      </div>
                    </div>

                    <div class="order-actions">
                      <a class="btn btn-primary btn-small" href="/pages/cart/complete.html?orderNumber=${order.orderNumber}">注文詳細を見る</a>
                      <a class="btn btn-outline btn-small" href="/pages/products/product-list.html">買い物を続ける</a>
                    </div>
                  </aside>
                </div>
              </article>`;
    })
    .join("");
  ordersListEl.insertAdjacentHTML("beforeend", html);
}
function renderOrderItem(order) {
  return order.items
    .map((item) => {
      return `
      <div class="item-row">
        <img class="item-thumb" src="${API_BASE}${item.imageUrl}" alt="" />
        <div class="item-info">
          <div class="item-name">アディダス スニーカー</div>
          <div class="item-opts muted">サイズ: ${item.sizeName}cm / 数量: ${item.quantity}</div>
          <div class="item-opts muted">カラー: ${item.colorName}</div>
        </div>
        <div class="item-price">${yen(item.lineTotal)}</div>            
      </div>
    `;
    })
    .join("");
}
function yen(n) {
  const v = Number(n);
  if (!v) return `無料`;
  return `¥${v.toLocaleString()}`;
}

function formatJst(iso) {
  if (!iso) return "";
  const d = new Date(iso);

  return d.toLocaleString("ja-JP", {
    timeZone: "Asia/Tokyo",
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
  });
}
