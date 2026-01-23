import { API_BASE } from "../api/config.js";
export async function fetchOverview(range) {
  try {
    const res = await fetch(`${API_BASE}/api/admin/dashboard/overview?range=${range}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    if (!res.ok) throw new Error(data?.message || "API_ERROR");
    return await res.json();
  } catch (error) {
    console.log(error);
  }
}
export async function fetchAdminProducts() {
  try {
    const res = await fetch(`${API_BASE}/api/admin/products`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    if (!res.ok) throw new Error(data?.message || "API_ERROR");
    return await res.json();
  } catch (error) {
    console.log(error);
  }
}
