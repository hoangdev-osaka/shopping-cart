import { API_BASE } from "./config.js";
export async function createReview(productSlug, body) {
  try {
    const res = await fetch(`${API_BASE}/api/products/${productSlug}/my-reviews`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: body,
    });

    if (!res.ok) {
      const err = await res.text();
      throw new Error(err || "Create review failed");
    }

    return await res.json();
  } catch (err) {
    console.error("createProductReview failed:", err);
  }
}
