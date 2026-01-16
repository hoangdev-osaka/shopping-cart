import { renderProducts } from "../core/render/product-renderer.js";
import { API_BASE } from "../api/config.js";

const heroBtnEl = document.getElementById("heroBtn");

const section1 = document.getElementById("section1");
const section2 = document.getElementById("section2");

const cards1 = section1.querySelectorAll(".categories-item__card--left1, .categories-item__card--right1");
const cards2 = section2.querySelectorAll(".categories-item__card--left2, .categories-item__card--right2");

window.addEventListener("load", () => {
  document.querySelector(".hero-section")?.classList.add("is-visible");
});

function openCards2() {
  cards2.forEach((el) => el.classList.add("is-open"));
}

function openCards1() {
  cards1.forEach((el) => el.classList.add("is-open"));
}

heroBtnEl.addEventListener("click", () => {
  window.location.href = "/pages/products/product-list.html";
});

const io1 = new IntersectionObserver(
  ([entry]) => {
    if (entry.isIntersecting) openCards1();
  },
  {
    root: null,
    rootMargin: "50px 0px",
    threshold: 0.2,
  }
);
const io2 = new IntersectionObserver(
  ([entry]) => {
    if (entry.isIntersecting) openCards2();
  },
  {
    root: null,
    rootMargin: "50px 0px",
    threshold: 0.4,
  }
);

io1.observe(section1);
io2.observe(section2);

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
