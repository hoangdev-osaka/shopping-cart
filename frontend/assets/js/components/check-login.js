import { API_BASE } from "../../js/api/config.js";
import { refreshToken } from "../api/auth.js";
const loginBtnEl = document.getElementById("loginBtn");
const registerBtnEl = document.getElementById("registerBtn");
const toastLoginEl = document.getElementById("toastLogin");

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

    if (!res.ok) throw new Error("login false");
    loginBtnEl?.classList.add("hidden");
    registerBtnEl?.classList.add("hidden");
    toastLoginEl?.classList.remove("hidden");
    const user = await res.json();
    if (user.email && toastLoginEl) {
      const name = user?.email?.split("@")[0];
      toastLoginEl.textContent = `${name} さん`;
    }
    return user;
  } catch {
    refreshToken();
    return false;
  }
}

