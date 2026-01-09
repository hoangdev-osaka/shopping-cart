import { API_BASE } from "../../api/config.js";
export async function loadCartBadge() {
  const badge = document.getElementById("cartBadge");
  if (!badge) return;

  try {
    const res = await fetch(`${API_BASE}/api/my-cart/summary`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }
    });

    if (!res.ok) {
      badge.style.display = "none";
      throw res;
    }
    const data = await res.json();
    const qty = data.quantity || 0;

    if (qty <= 0) {
      badge.style.display = "none";
    } else {
      badge.textContent = qty > 99 ? "99+" : qty;
      badge.style.display = "flex";
    }
  } catch (e) {
    
  }
}