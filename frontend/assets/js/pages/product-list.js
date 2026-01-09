import { renderProducts } from "../core/render/product-renderer.js";
import { API_BASE } from "../api/config.js";

let currentPage = 0;
const PAGE_SIZE = 20;
const url = new URL(window.location.href);
const categorySlug = url.searchParams.get("categorySlug");
let isLastPage = false;
let isLoading = false;

const productListEl = document.getElementById("product-list");
const filterFormEl = document.getElementById("filter-form");
const categoryGroupEl = document.querySelector(".filter__categories");
const brandGroupEl = document.querySelector(".filter__brands");
const colorGroupEl = document.querySelector(".color-options");

const priceRangeEl = document.getElementById("priceRange");
const priceMaxLabelEl = document.getElementById("price-max");
const categoriesPathEl = document.getElementById("categoryPath");

if (!productListEl) throw new Error("Missing #product-list");

const sentinelEl = document.createElement("div");
sentinelEl.style.height = "1px";
productListEl.after(sentinelEl);

function formatYen(n) {
  return Number(n || 0).toLocaleString() + "¥";
}

async function fetchJson(url) {
  const res = await fetch(url, { headers: { Accept: "application/json" } });
  const data = await res.json().catch(() => null);
  if (!res.ok) {
    console.error("Fetch failed:", res.status, url, data);
    console.error("❌ HTTP", res.status, res.statusText);
    console.error("❌ URL:", url);
    console.error("❌ Body:", text);
    throw new Error(String(res.status));
  }
  return data;
}

function escapeHtml(s) {
  return String(s ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function getKeywordFromUrl() {
  const u = new URL(window.location.href);
  return u.searchParams.get("keyword") || "";
}

function getCategorySlugsFromUrl() {
  const u = new URL(window.location.href);
  const legacySingle = u.searchParams.get("categorySlug");
  const multi = u.searchParams.getAll("categorySlugs");
  return [...multi, ...(legacySingle ? [legacySingle] : [])].filter(Boolean);
}

function getBrandSlugsFromUrl() {
  const u = new URL(window.location.href);
  return u.searchParams.getAll("brandSlugs").filter(Boolean);
}

function getColorsFromUrl() {
  const u = new URL(window.location.href);
  return u.searchParams.getAll("colors").filter(Boolean);
}

function getMaxPriceFromUrl() {
  const u = new URL(window.location.href);
  const raw = u.searchParams.get("maxPrice");
  if (!raw) return null;
  if (!/^\d+(\.\d+)?$/.test(raw)) return null;
  return raw;
}

function getSelectedCategorySlugs() {
  if (!categoryGroupEl) return [];
  const checked = categoryGroupEl.querySelectorAll('input[type="checkbox"]:checked');
  return Array.from(checked)
    .map((i) => i.value)
    .filter(Boolean);
}

function getSelectedBrandSlugs() {
  if (!brandGroupEl) return [];
  const checked = brandGroupEl.querySelectorAll('input[type="checkbox"]:checked');
  return Array.from(checked)
    .map((i) => i.value)
    .filter(Boolean);
}

function getSelectedColors() {
  if (!colorGroupEl) return [];
  const checked = colorGroupEl.querySelectorAll('input[type="checkbox"]:checked');
  return Array.from(checked)
    .map((i) => i.value)
    .filter(Boolean);
}

function getMaxPriceFromSlider() {
  if (!priceRangeEl) return null;
  const raw = String(priceRangeEl.value ?? "").trim();
  if (!raw) return null;
  if (!/^\d+(\.\d+)?$/.test(raw)) return null;
  return raw;
}
function renderCategoryPathFromSelected() {
  if (!categoriesPathEl) return;
  if (!categoryGroupEl) return;

  const checked = categoryGroupEl.querySelectorAll('input[type="checkbox"]:checked');
  const arr = Array.from(checked).map((cb) => cb.getAttribute("name") || cb.value);

  const base = `
    <button class="category-path__back-btn">←BACK</button>
    <a class="category-path__back-home" href="/index.html">Home</a>
  `;

  if (arr.length === 0) {
    categoriesPathEl.innerHTML = base;
    return;
  }

  categoriesPathEl.innerHTML = base + arr.map((name) => ` / <span class="category-path__name">${escapeHtml(name)}</span>`).join("");
}

function applyChecksFromUrl() {
  if (categoryGroupEl) {
    const urlCats = new Set(getCategorySlugsFromUrl());
    categoryGroupEl.querySelectorAll('input[type="checkbox"]').forEach((cb) => {
      cb.checked = urlCats.has(cb.value);
    });
  }

  if (brandGroupEl) {
    const urlBrands = new Set(getBrandSlugsFromUrl());
    brandGroupEl.querySelectorAll('input[type="checkbox"]').forEach((cb) => {
      cb.checked = urlBrands.has(cb.value);
    });
  }

  if (colorGroupEl) {
    const urlColors = new Set(getColorsFromUrl());
    colorGroupEl.querySelectorAll('input[type="checkbox"]').forEach((cb) => {
      cb.checked = urlColors.has(cb.value);
    });
  }

  const maxPrice = getMaxPriceFromUrl();
  if (priceRangeEl && maxPrice !== null) {
    priceRangeEl.value = maxPrice;
  }

  if (priceRangeEl && priceMaxLabelEl) {
    priceMaxLabelEl.textContent = formatYen(priceRangeEl.value);
  }

  renderCategoryPathFromSelected();
}

function pushCurrentFiltersToUrl() {
  const u = new URL(window.location.href);
  u.searchParams.delete("categorySlugs");
  u.searchParams.delete("categorySlug");
  u.searchParams.delete("brandSlugs");
  u.searchParams.delete("colors");
  u.searchParams.delete("maxPrice");

  getSelectedCategorySlugs().forEach((slug) => u.searchParams.append("categorySlugs", slug));

  getSelectedBrandSlugs().forEach((slug) => u.searchParams.append("brandSlugs", slug));

  getSelectedColors().forEach((c) => u.searchParams.append("colors", c));

  const maxPrice = getMaxPriceFromSlider();
  if (maxPrice !== null) u.searchParams.set("maxPrice", maxPrice);

  const nextUrl = u.toString();
  const curUrl = window.location.href;

  if (nextUrl !== curUrl) history.pushState(null, "", nextUrl);
}

function buildProductsUrl(page) {
  const params = new URLSearchParams();
  params.set("page", String(page));
  params.set("size", String(PAGE_SIZE));

  const keyword = getKeywordFromUrl();
  if (keyword) params.set("keyword", keyword);

  const selectedCats = getSelectedCategorySlugs();
  const cats = selectedCats.length ? selectedCats : getCategorySlugsFromUrl();
  cats.forEach((slug) => params.append("categorySlugs", slug));

  getSelectedBrandSlugs().forEach((slug) => params.append("brandSlugs", slug));

  getSelectedColors().forEach((c) => params.append("colors", c));

  const maxPrice = getMaxPriceFromSlider();
  if (maxPrice !== null) params.set("maxPrice", maxPrice);

  const fullUrl = `${API_BASE}/api/products?${params.toString()}`;
  console.log("Products URL:", fullUrl);
  return fullUrl;
}

async function loadCategoriesToLeftFilter() {
  if (!categoryGroupEl) return;

  const title = categoryGroupEl.querySelector("h4")?.outerHTML || "<h4>カテゴリー</h4>";
  categoryGroupEl.innerHTML = title;

  try {
    const categories = await fetchJson(`${API_BASE}/api/categories`);
    const items = Array.isArray(categories?.content) ? categories.content : categories;

    const top = (items || []).filter((c) => !c.parent);
    const list = top.length ? top : items || [];

    const html = list
      .map((c) => {
        const slug = c.slug ?? "";
        const name = c.name ?? slug;
        return `
          <label class="checkbox-item">
            <input type="checkbox" value="${escapeHtml(slug)}" name="${escapeHtml(name)}" />
            <span class="checkbox-custom"></span> ${escapeHtml(name)}
          </label>
        `;
      })
      .join("");

    categoryGroupEl.insertAdjacentHTML("beforeend", html);
  } catch (e) {
    console.error("Load categories failed:", e);
  }
}

async function loadBrandsToLeftFilter() {
  if (!brandGroupEl) return;

  const title = brandGroupEl.querySelector("h4")?.outerHTML || "<h4>ブランド</h4>";
  brandGroupEl.innerHTML = title;

  try {
    const brands = await fetchJson(`${API_BASE}/api/brands`);
    const items = Array.isArray(brands?.content) ? brands.content : brands;

    const html = (items || [])
      .map((b) => {
        const slug = b.slug ?? "";
        const name = b.name ?? slug;
        return `
          <label class="checkbox-item">
            <input type="checkbox" value="${escapeHtml(slug)}" name="${escapeHtml(name)}" />
            <span class="checkbox-custom"></span> ${escapeHtml(name)}
          </label>
        `;
      })
      .join("");

    brandGroupEl.insertAdjacentHTML("beforeend", html);
  } catch (e) {
    console.error("Load brands failed:", e);
  }
}

async function loadNextPage() {
  if (isLoading || isLastPage) return;

  isLoading = true;
  try {
    const requestUrl = buildProductsUrl(currentPage);
    const data = await fetchJson(requestUrl);
    const items = data?.content || [];

    renderProducts(items, productListEl, { append: true });

    isLastPage = Boolean(data?.last) || (typeof data?.totalPages === "number" && currentPage >= data.totalPages - 1);

    currentPage++;
  } catch (e) {
    console.error(e);
    alert("システムエラーが発生しました。");
  } finally {
    isLoading = false;
  }
}

function resetAndReload() {
  currentPage = 0;
  isLastPage = false;
  productListEl.innerHTML = "";
  loadNextPage();
}

const io = new IntersectionObserver(
  (entries) => {
    if (!entries[0]?.isIntersecting) return;
    loadNextPage();
  },
  { root: null, rootMargin: "300px 0px", threshold: 0 }
);
io.observe(sentinelEl);

document.addEventListener("click", (e) => {
  const filterPanelEl = document.getElementById("filterPanel");
  const el = e.target.closest("[data-action]");
  if (!el) return;
  switch (el.dataset.action) {
    case "open-filter":
      filterPanelEl.style.display = "block";
      if (filterPanelEl.classList.contains("hidden")) {
        filterPanelEl.classList.toggle("hidden");
      } else filterPanelEl.classList.toggle("hidden");

      break;
  }
  const btn = e.target.closest(".category-path__back-btn");
  if (!btn) return;
  e.preventDefault();
  history.back();
});

if (categoryGroupEl) {
  categoryGroupEl.addEventListener("change", () => {
    pushCurrentFiltersToUrl();
    renderCategoryPathFromSelected();
    resetAndReload();
  });
}

if (colorGroupEl) {
  colorGroupEl.addEventListener("change", () => {
    pushCurrentFiltersToUrl();
    resetAndReload();
  });
}

if (filterFormEl) {
  filterFormEl.addEventListener("change", (e) => {
    const target = e.target;
    if (target?.closest?.(".color-options")) return;
    if (target?.closest?.(".filter__categories")) return;

    pushCurrentFiltersToUrl();
    resetAndReload();
  });
}

if (priceRangeEl && priceMaxLabelEl) {
  priceMaxLabelEl.textContent = formatYen(priceRangeEl.value);

  priceRangeEl.addEventListener("input", () => {
    priceMaxLabelEl.textContent = formatYen(priceRangeEl.value);
  });

  priceRangeEl.addEventListener("change", () => {
    pushCurrentFiltersToUrl();
    resetAndReload();
  });
}

window.addEventListener("popstate", () => {
  applyChecksFromUrl();
  resetAndReload();
});

(async function init() {
  await loadCategoriesToLeftFilter();
  await loadBrandsToLeftFilter();

  applyChecksFromUrl();

  resetAndReload();
})();
