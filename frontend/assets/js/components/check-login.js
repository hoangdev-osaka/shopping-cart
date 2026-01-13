import { API_BASE } from "../../js/api/config.js";

export async function checkLogin() {
  try {
    const token = localStorage.getItem("token");
    if (!token) throw new Error("not logged in");
    const res = await fetch(`${API_BASE}/api/my-account`, {
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!res) throw new Error();

    const user = await res.json();
    return user;
  } catch (e) {
    console.log(e);
    refreshToken();
    return false;
  }
}
async function refreshToken() {
  try {
    const res = await fetch(`${API_BASE}/api/auth/refresh`, {
      method: "POST",
      credentials: "include",
      headers: {
        Accept: "application/json",
      },
    });

    if (!res.ok) throw new Error("token invalid");
    const data = await res.json();
    localStorage.setItem("token", data.accessToken);
    console.log(JSON.stringify(data, null, 4));
  } catch {
    return false;
  }
}
