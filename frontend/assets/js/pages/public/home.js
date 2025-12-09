fetch("http://localhost:8080/api/products", {
  method: "GET",
  headers: {
    "Accept": "application/json",
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
    const products = result?.content || [];
    renderProducts(products);
  })
  .catch((error) => {
    console.error("Lỗi:", error);
    alert("システムエラーが発生しました。");
  });
function renderProducts(products) {
  const container = document.getElementById("product-list");
  container.innerHTML = ""; // clear cũ nếu có

  products.forEach((p) => {
    const card = `
      <div class="card">
        <img src="${p.imageUrl || '/uploads/default/no-image.png'}" class="card-img" />

        <div class="card-rating">
          <span class="stars">${renderStars(p.averageRating)}</span>
          <span class="rating-count">(${p.reviewCount}件)</span>
        </div>

        <h3 class="card-title">${p.name}</h3>

        <div class="card-price">
          <p id="price-current">${p.regularPrice}¥</p>
          <p id="price-old">${p.salePrice}¥</p>
          <span class="discount-percent">| -${calcDiscount(p.salePrice, p.regularPrice)}% OFF</span>
        </div>

        <button class="btn btn-primary btn-small">カートに追加</button>
      </div>
    `;

    container.innerHTML += card;
  });
}
function renderStars(rating) {
  const full = Math.floor(rating);
  const half = rating % 1 >= 0.5 ? 1 : 0;
  const empty = 5 - full - half;

  return "★".repeat(full) + "☆".repeat(empty);
}
function calcDiscount(min, max) {
  if (!max || max <= min) return 0;
  return Math.round(((max - min) / max) * 100);
}
