import { API_BASE } from "../../js/api/config.js";
import { loadCartBadge } from "../core/render/loadCartBadge.js";
import { checkLogin } from "../components/check-login.js";

const suggestBox = document.getElementById("suggestList");
const searchInput = document.getElementById("searchInput");
const popup = document.getElementById("categoriesPopup");
const categoriesItemEl = document.getElementById("categoriesItem");
const searchBoxEl = document.getElementById("searchBox");
const searchFormEl = document.getElementById("searchForm");
try {
  const categories = await fetchCategories();
  console.log(JSON.stringify(categories, null, 4));

  renderCategories(categories);
  renderPopupCategories(categories);
} catch (err) {
  console.error(err);
}
const keyword = searchInput.value.trim();

document.addEventListener("click", async (e) => {
  const el = e.target.closest("[data-action]");
  if (!el) return;
  switch (el.dataset.action) {
    case "toggle-categories-popup":
      popup.classList.toggle("is-open");
      break;
    case "close-popup-categories":
      popup.classList.toggle("is-open");
      break;
    case "open-search-box":
      searchBoxEl.classList.toggle("is-open");
      setTimeout(() => {
        searchInput?.focus();
      }, 0);
      break;
    case "search-product": {
      window.location.href = keyword
        ? `/pages/products/product-list.html?keyword=${encodeURIComponent(keyword)}`
        : `/pages/products/product-list.html`;
      break;
    }
    case "logout":
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      sessionStorage.clear();
      window.location.replace("/pages/auth/login.html");
      break;
  }
});
searchFormEl.addEventListener("submit", (e) => {
  e.preventDefault();
  const url = keyword ? `/pages/products/product-list.html?keyword=${encodeURIComponent(keyword)}` : `/pages/products/product-list.html`;
  window.location.assign(url);
});
async function renderPopupCategories(categories) {
  const html = categories
    .map((it) => {
      return `  <article>
                  <a class="category-item" href="../../../pages/products/product-list.html?categorySlug=${encodeURIComponent(it.slug)}">
                    <span class="category-item__name">${it.name}</span>
                    <button class="category-item__arrow" type="button" aria-label="Open category" data-action="open-subcategory">
                      <svg viewBox="0 0 24 24" width="35" height="35" aria-hidden="true">
                        <path
                          d="M9.5 6.5L15 12L9.5 17.5"
                          fill="none"
                          stroke="currentColor"
                          stroke-width="1.5"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                        />
                      </svg>
                    </button>
                  </a>
                </article>`;
    })
    .join("");
  categoriesItemEl.insertAdjacentHTML("beforeend", html);
}

searchInput.addEventListener("input", async function () {
  const keyword = searchInput.value.trim();

  if (!keyword) {
    suggestBox.style.display = "none";
    return;
  }

  const res = await fetch(`${API_BASE}/api/products?keyword=${encodeURIComponent(keyword)}`);
  const data = await res.json();

  const products = data.content || [];

  suggestBox.innerHTML = "";

  products.slice(0, 6).forEach((item) => {
    const li = document.createElement("li");

    li.innerHTML = `
      <a href="/pages/products/product-detail.html?slug=${encodeURIComponent(item.slug)}" class = suggest__inner>  
        <img src="${API_BASE}${item.imageUrl || "/uploads/default/no-image.png"}">
        <span class="suggest__name">${item.name}</span>
      </a>`;

    suggestBox.appendChild(li);
  });

  suggestBox.style.display = products.length ? "block" : "none";
});

async function fetchCategories() {
  try {
    const res = await fetch(`${API_BASE}/api/categories`, {
      method: "GET",
      headers: {
        Accept: "application/json",
      },
    });

    if (!res.ok) {
      throw new Error(`Fetch categories failed: ${res.status}`);
    }

    const data = await res.json();
    return data?.content ?? [];
  } catch (err) {
    console.error("Load categories error:", err);
    return [];
  }
}

function renderCategories(categories) {
  const container = document.getElementById("categoriesList");
  container.innerHTML = "";
  categories.forEach((c) => {
    const name = c.name;
    const card = `
    <a class="categories-list__text" 
      href="/pages/products/product-list.html?categorySlug=${encodeURIComponent(c.slug)}">${name}</a>`;
    container.innerHTML += card;
  });
}

loadCartBadge();
const user = await checkLogin();
if (user) {
  console.log("đã đăng nhập");
  document.addEventListener("click", (e) => {
    const btn = e.target.closest('[data-action="toggle-menu"]');
    const menu = document.getElementById("menuList");

    if (btn) {
      document.body.classList.toggle("is-menu-open");

      return;
    }

    if (document.body.classList.contains("is-menu-open") && menu && !menu.contains(e.target)) {
      document.body.classList.remove("is-menu-open");
    }
  });
} else {
  checkLogin();
  document.addEventListener("click", (e) => {
    const el = e.target.closest("[data-action]");
    if (!el) return;
    switch (el.dataset.action) {
      case "toggle-menu":
        window.location.href = "/pages/auth/login.html";
        break;
    }
  });
}
