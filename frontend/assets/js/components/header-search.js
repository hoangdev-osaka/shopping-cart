import { API_BASE } from "../../js/api/config.js";
import { loadCartBadge } from "../core/render/loadCartBadge.js";
import { checkLogin } from "../components/check-login.js";

const suggestBox = document.getElementById("suggestList");
const searchInput = document.getElementById("searchInput");
const searchBtn = document.getElementById("searchButton");

searchInput.addEventListener("input", () => {
  const keyword = searchInput.value.trim();

  searchBtn.href = keyword
    ? `/pages/products/product-list.html?keyword=${encodeURIComponent(keyword)}`
    : `/pages/products/product-list.html`;
});

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

fetch(`${API_BASE}/api/categories`, {
  method: "GET",
  headers: {
    Accept: "application/json",
  },
})
  .then(async (response) => {
    let result = null;
    try {
      result = await response.json();
    } catch (e) {
      result = null;
    }
    if (!response.ok) {
      alert("サーバーエラーが発生しました。");
      throw new Error("Fetch products failed: " + response.status);
    }
    return result;
  })
  .then((result) => {
    const categories = result?.content || [];
    renderCategories(categories);
  })
  .catch((error) => {
    console.error("Error:", error);
    alert("システムエラーが発生しました。");
  });

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
}else{
  document.getElementById("hamburgerBtn").classList.add("hidden");
}
