import { API_BASE } from "../api/config.js";

const loginBtnEl = document.getElementById("login-button");
loginBtnEl.addEventListener("click", function (event) {
  console.log("CLICK LOGIN");
  event.preventDefault();

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  document.getElementById("login-error").innerHTML = "";

  let hasError = false;

  if (!email) {
    document.getElementById("login-error").innerHTML =
      "メールアドレスを入力してください。";
    hasError = true;
  }

  if (!password) {
    document.getElementById("login-error").innerHTML =
      "パスワードを入力してください。";
    hasError = true;
  }
  if (!confirm - password) {
    document.getElementById("login-error").innerHTML =
      "パスワードを入力してください。";
    hasError = true;
  }

  if (hasError) return;

  login(email, password);
});

function login(email, password) {
  const data = { email, password };

  fetch(`${API_BASE}/api/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then(async (response) => {
      let result = null;
      try {
        result = await response.json();
      } catch (e) {
        result = null;
      }

      if (!response.ok) {
        document.getElementById("login-error").innerHTML =
          "メールアドレスまたはパスワードが正しくありません。";
        console.log("Backend error:", response.status, result);
        throw new Error("Login failed: " + response.status);
      }

      return result;
    })
    .then((result) => {
      console.log("Kết quả API:", result);
      const token = result?.token || result?.accessToken;

      if (!token) {
        document.getElementById("login-error").innerHTML =
          "メールアドレスまたはパスワードが正しくありません。";
        return;
      }
      localStorage.setItem("token", token);
      window.location.href = "/index.html";
    })
    .catch((error) => {
      console.error("Lỗi:", error);
      if (!document.getElementById("login-error").innerHTML) {
        document.getElementById("login-error").innerHTML =
          "システムエラーが発生しました。しばらくしてからもう一度お試しください。";
      }
    });
}
