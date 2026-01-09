import { API_BASE } from "../../js/api/config.js";

export async function checkLogin() {
  const token = localStorage.getItem("token");
  if (!token) return false;

  try {
    const res = await fetch(`${API_BASE}/api/my-account`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!res.ok) throw new Error("not logged in");

    const user = await res.json();
    return user; 
  } catch {
    localStorage.removeItem("token");
    return false;
  }
}
