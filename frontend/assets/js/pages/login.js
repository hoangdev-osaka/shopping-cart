import { login } from "../api/auth.js";
const loginBtnEl = document.getElementById("login-button");
const emailEl = document.getElementById("email");
const passwordEl = document.getElementById("password");
emailEl.value = sessionStorage.getItem("prefill_email");
passwordEl.value = sessionStorage.getItem("prefill_password");

loginBtnEl.addEventListener("click", function (event) {
  console.log("CLICK LOGIN");
  event.preventDefault();

  const email = emailEl.value.trim();
  const password = passwordEl.value.trim();

  document.getElementById("login-error").innerHTML = "";

  let hasError = false;

  if (!email) {
    document.getElementById("login-error").innerHTML = "メールアドレスを入力してください";
    hasError = true;
  }

  if (!password) {
    document.getElementById("login-error").innerHTML = "パスワードを入力してください";
    hasError = true;
  }
  if (password.length < 8) {
    document.getElementById("login-error").textContent = "パスワードは8文字以上で入力してください。";
    hasError = true;
  }

  if (hasError) return;

  login(email, password);
});
sessionStorage.removeItem("prefill_email");
sessionStorage.removeItem("prefill_password");
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
