import { renderProducts } from "../core/render/product-renderer.js";
import { API_BASE } from "../api/config.js";

const heroBtnEl = document.getElementById("heroBtn");

heroBtnEl.addEventListener("click", () => {
  window.location.href = "/pages/products/product-list.html";
});

async function loadHomeProducts() {
  try {
    const response = await fetch(`${API_BASE}/api/products`, {
      headers: {
        Accept: "application/json",
      },
    });

    if (!response.ok) {
      throw new Error("Fetch products failed: " + response.status);
    }

    const result = await response.json();
    const products = result?.content ?? [];

    const hotSaleEl = document.getElementById("productHotSale");

    renderProducts(products, hotSaleEl);
  } catch (error) {   
    console.error("ERROR:", error);
    alert("システムエラーが発生しました。");
  }
}

loadHomeProducts();

async function loadNewProducts() {
  try {
    const response = await fetch(`${API_BASE}/api/products/new`, {
      headers: {
        Accept: "application/json",
      },
    });
    if (!response.ok) {
      throw new Error("Fetch products failed: " + response.status);
    }
    const result = await response.json();
    const products = result?.content ?? [];
    const newEl = document.getElementById("productNew");
    renderProducts(products, newEl);
  } catch (error) {
    console.error("ERROR:", error);
    alert("システムエラーが発生しました。");
  }
}
loadNewProducts();
fetch(`${API_BASE}/api/brands`, {
  method: "GET",
  headers: { Accept: "application/json" },
})
  .then(async (response) => {
    let result = null;

    try {
      result = await response.json();
    } catch (e) {
      console.error("JSON parse error:", e);
    }

    if (!response.ok) {
      alert("サーバーエラーが発生しました。");
      throw new Error("Fetch products failed: " + response.status);
    }

    return result;
  })
  .then((result) => {
    const brands = result?.content || [];
    renderBrands(brands);
  })
  .catch((error) => {
    console.error("ERROR:", error);
    alert("システムエラーが発生しました。");
  });

function renderBrands(brands) {
  const container = document.getElementById("brand-list");
  container.innerHTML = "";

  brands.forEach((b) => {
    const brand = `
      <img class="brand-img" src="${b.logoUrl}" alt="${b.name}"></img>
    `;
    container.insertAdjacentHTML("beforeend", brand);
  });
}
