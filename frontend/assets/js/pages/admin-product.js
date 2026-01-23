import { fetchAdminProducts } from "../api/admin.js";
import { API_BASE } from "../api/config.js";

const padminTableEl = document.getElementById("padminTable");
const loadingEl = document.getElementById("loading");
init();

async function init() {
  try {

    const data = await fetchAdminProducts();
    renderProductTable(data);
  } catch (error) {
    console.error(error);
    padminTableEl.innerHTML = `
      <tr class="padminTable__row">
        <td class="pcell" colspan="9">Failed to load products.</td>
      </tr>
    `;
  } finally {
    loadingEl.classList.toggle("hidden");
  }
}

function renderProductTable(data) {
  const products = data?.content ?? [];

  if (!products.length) {
    padminTableEl.innerHTML = `
      <tr class="padminTable__row">
        <td class="pcell" colspan="9">No products.</td>
      </tr>
    `;
    return;
  }

  const html = products.map(renderRow).join("");
  padminTableEl.innerHTML = html;
}

function renderRow(p) {
  const id = p.id ?? "";
  const name = escapeHtml(p.name ?? "-");
  const brandName = escapeHtml(p.brandName ?? "-");
  const imageUrl = `${API_BASE}${p.imageUrl}`;
  const createdAt = formatDateYMD(p.createdAt);
  const variants = p.variantsCount ?? "-";

  const statusText = normalizeStatusText(p.status);
  const statusClass = statusToBadgeClass(p.status);

  return `
    <tr class="padminTable__row" data-id="${id}">
      <td class="pcell pcol--checkbox">
        <input class="padminCheck" type="checkbox" aria-label="Select product #${id}" />
      </td>

      <td class="pcell pcol--id ptext-mono">#${id}</td>

      <td class="pcell pcol--product">
        <button type="button" class="pnameBtn" data-id="${id}" aria-label="Open product detail">
          ${name}
        </button>
      </td>

      <td class="pcell pcol--image">
        <img class="pthumb" src="${imageUrl}" alt="Product image"
             onerror="this.src='/assets/images/default/no-image.png'"/>
      </td>

      <td class="pcell pcol--brand">${brandName}</td>

      <td class="pcell pcol--variants ptext-center">${variants}</td>

      <td class="pcell pcol--date ptext-muted">${createdAt}</td>

      <td class="pcell pcol--status">
        <span class="pbadge ${statusClass}">${statusText}</span>
      </td>

      <td class="pcell pcol--actions ptext-right">
        <button class="piconBtn" type="button" aria-label="Row actions">
          <svg viewBox="0 0 24 24" width="18" height="18" aria-hidden="true">
            <circle cx="12" cy="5" r="1.8"></circle>
            <circle cx="12" cy="12" r="1.8"></circle>
            <circle cx="12" cy="19" r="1.8"></circle>
          </svg>
        </button>
      </td>
    </tr>
  `;
}

function formatDateYMD(iso) {
  if (!iso) return "-";
  const d = new Date(iso);
  if (Number.isNaN(d.getTime())) return "-";
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  return `${y}-${m}-${day}`;
}

function normalizeStatusText(status) {
  if (!status) return "Unknown";
  const s = String(status).toUpperCase();
  if (s === "ACTIVE") return "Active";
  if (s === "INACTIVE") return "Inactive";
  return escapeHtml(s);
}

function statusToBadgeClass(status) {
  const s = String(status || "").toUpperCase();
  if (s === "ACTIVE") return "pbadge--active";
  if (s === "INACTIVE") return "pbadge--inactive";
  return "pbadge--inactive";
}

function escapeHtml(str) {
  return String(str)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}
