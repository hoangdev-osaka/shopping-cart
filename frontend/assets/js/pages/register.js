import { API_BASE } from "../api/config.js";

const form = document.getElementById("registerForm");
const btn = document.getElementById("register-button");
const errEl = document.getElementById("registererror");

document.addEventListener("click", (e) => {
  const tbtn = e.target.closest(".toggle-password");
  if (!tbtn) return;

  const inputId = tbtn.dataset.target;
  const input = document.getElementById(inputId);
  if (!input) return;

  const isHidden = input.type === "password";
  input.type = isHidden ? "text" : "password";
  tbtn.textContent = isHidden ? "❌" : "👁";
});

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  errEl.textContent = "";

  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value;

  let hasError = false;

  if (!email) {
    errEl.textContent = "メールアドレスを入力してください。";
    hasError = true;
  } else if (!password) {
    errEl.textContent = "パスワードを入力してください。";
    hasError = true;
  }

  if (hasError) return;

  btn.classList.add("is-loading");
  btn.disabled = true;

  try {
    const res = await fetch(`${API_BASE}/api/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json", Accept: "application/json" },
      body: JSON.stringify({ email, password }),
    });

    const data = await res.json().catch(() => null);

    if (!res.ok) {
      errEl.textContent = data?.detail || "登録に失敗しました。";
      return;
    }
    console.log("đã đăng kí thành công");
    const messageEl = document.getElementById("registerSuccessModal");
    messageEl.classList.remove("hidden");
    sessionStorage.setItem("prefill_email", email);
    sessionStorage.setItem("prefill_password", password);
  } catch (err) {
    console.log(err);
    errEl.textContent = "ネットワークエラー";
  } finally {
    btn.classList.remove("is-loading");
    btn.disabled = false;
  }
});
