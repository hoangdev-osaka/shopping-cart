import { API_BASE } from "../api/config.js";
export async function login(email, password) {
  const errorEl = document.getElementById("login-error");

  const setError = (msg) => {
    if (!errorEl) return;
    errorEl.textContent = msg || "";
  };

  const clearError = () => setError("");

  const invalidMsg = "メールアドレスまたはパスワードが正しくありません。";
  const systemMsg = "システムエラーが発生しました。しばらくしてからもう一度お試しください。";

  try {
    clearError();

    const res = await fetch(`${API_BASE}/api/auth/login`, {
      method: "POST",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    const contentType = res.headers.get("content-type") || "";
    const payload = contentType.includes("application/json") ? await res.json().catch(() => null) : await res.text().catch(() => null);

    if (!res.ok) {
      if (res.status === 401 || res.status === 403) setError(invalidMsg);
      else setError(systemMsg);
      console.log("Backend error:", res.status, payload);
      return;
    }

    const token = payload?.token ?? payload?.accessToken;
    if (!token) {
      console.log("Login response missing token:", payload);
      setError(systemMsg);
      return;
    }

    localStorage.setItem("token", token);
    window.location.assign("/index.html");
  } catch (err) {
    console.error("Login error:", err);
    if (errorEl && !errorEl.textContent) setError(systemMsg);
  }
}
export async function logout() {
  try {
    const res = await fetch(`${API_BASE}/api/auth/logout`, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
    });
    if (!res.ok) {
      const err = new Error("error: logout");
      err.status = res.status;
      err.data = data;
      throw err;
    }
  } catch (e) {
    console.log(e);
  }
}
export async function refreshToken() {
  try {
    const res = await fetch(`${API_BASE}/api/auth/refresh`, {
      method: "POST",
      credentials: "include",
      headers: {
        Accept: "application/json",
      },
    });

    if (!res.ok) return null;
    const data = await res.json();
    localStorage.setItem("token", data.accessToken);
    console.log(JSON.stringify(data, null, 4));
  } catch (e) {
    console.log(e);
  }
}
