import { API_BASE } from "../../api/config.js";

export function renderProducts(products, container, { append = false } = {}) {
  if (!container) return;

  let html = "";

  products.forEach((p) => {
    const regular = p.regularPrice;
    const sale = p.salePrice;
    const hasRegular = regular != null;
    const hasSale = sale != null && sale > 0 && hasRegular && sale < regular;
    const hasAnyPrice = hasRegular || (sale != null && sale > 0);
    
    let priceHtml = "";

    if (!hasAnyPrice) {
      priceHtml = `
        <div class="card-price">
          <span class="price-unavailable">価格情報なし</span>
        </div>
      `;
    } else if (hasSale) {
      priceHtml = `
        <div class="card-price">
          <span class="discount-percent">-${calcDiscount(sale, regular)}% OFF</span>
          <span class="price-current">¥${sale.toLocaleString()}</span>
          <span class="price-old">¥${regular.toLocaleString()}</span>
        </div>
      `;
    } else {
      priceHtml = `
        <div class="card-price">
          <span class="price-current">¥${regular.toLocaleString()}</span>
        </div>
      `;
    }

    const defaultImg = API_BASE + "/uploads/default/no-image.png";

    html += `
      <article class="cards" >
        <a href="/pages/products/product-detail.html?slug=${encodeURIComponent(p.slug)}&colorId=${encodeURIComponent(p.colorId)}" class="card">
          <div class="card-inner">
            <img 
              src="${p.imageUrl ? API_BASE + p.imageUrl : defaultImg}"
              class="card-img"
              alt="${escapeHtml(p.name)}"
              onerror="this.onerror=null; this.src='${defaultImg}';"
            />
          </div>
          <h3 class="card-title">${escapeHtml(p.name)}</h3>
          ${priceHtml}
          <div class="card-rating">
            <span class="stars">${renderStars(p.averageRating)}</span>
            <span class="rating-count">
              <svg class="rating-icon" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
              </svg>
              ${p.reviewCount ?? 0}
            </span>
          </div>
        </a>
      </article>
    `;
  });

  if (append) {
    container.insertAdjacentHTML("beforeend", html);
  } else {
    container.innerHTML = html;
  }
}

function renderStars(rating) {
  const full = Math.floor(rating || 0);
  const empty = 5 - full;
  return "★".repeat(full) + "☆".repeat(empty);
}

function calcDiscount(sale, regular) {
  if (!sale || !regular || regular <= sale) return 0;
  return Math.round(((regular - sale) / regular) * 100);
}

function escapeHtml(str) {
  return String(str ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}
