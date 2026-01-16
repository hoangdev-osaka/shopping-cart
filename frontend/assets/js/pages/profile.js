import { API_BASE } from "../api/config.js";
import { loadCartBadge } from "../core/render/loadCartBadge.js";

const token = localStorage.getItem("token");

const pageEl = document.getElementById("profilePage");

const loadingEl = document.getElementById("profileLoading");
const errorEl = document.getElementById("profileError");
const errorTextEl = document.getElementById("profileErrorText");

const viewEl = document.getElementById("profileView");
const formEl = document.getElementById("profileForm");
const msgEl = document.getElementById("profileMsg");

const nameViewEl = document.getElementById("profileName");
const emailViewEl = document.getElementById("profileEmail");
const phoneViewEl = document.getElementById("profilePhone");
const dobViewEl = document.getElementById("profileDob");
const noteViewEl = document.getElementById("profileNote");

const nameInput = document.getElementById("nameInput");
const emailInput = document.getElementById("emailInput");
const phoneInput = document.getElementById("phoneInput");
const dobInput = document.getElementById("dobInput");
const noteInput = document.getElementById("noteInput");

if (!pageEl) throw new Error("Missing #profilePage");

function show(el) {
  el?.classList.remove("hidden");
}
function hide(el) {
  el?.classList.add("hidden");
}

function setMode(mode) {
  if (mode === "edit") {
    hide(viewEl);
    show(formEl);
    return;
  }
  show(viewEl);
  hide(formEl);
  hide(msgEl);
}

function setState(state, message) {
  if (state === "loading") {
    show(loadingEl);
    hide(errorEl);
    hide(viewEl);
    hide(formEl);
    return;
  }
  if (state === "error") {
    hide(loadingEl);
    show(errorEl);
    hide(viewEl);
    hide(formEl);
    errorTextEl.textContent = message || "エラーが発生しました。";
    return;
  }
  hide(loadingEl);
  hide(errorEl);
  show(viewEl);
}

function safeText(v, fallback = "-") {
  const s = (v ?? "").toString().trim();
  return s ? s : fallback;
}

function formatDateISOToJP(iso) {
  if (!iso) return "-";
  // expects yyyy-mm-dd
  return iso;
}

function fillView(profile) {
  nameViewEl.textContent = safeText(profile?.name);
  emailViewEl.textContent = safeText(profile?.email);
  phoneViewEl.textContent = safeText(profile?.phone);
  dobViewEl.textContent = safeText(formatDateISOToJP(profile?.dob));
  noteViewEl.textContent = safeText(profile?.note);
}

function fillForm(profile) {
  nameInput.value = profile?.name ?? "";
  emailInput.value = profile?.email ?? "";
  phoneInput.value = profile?.phone ?? "";
  dobInput.value = profile?.dob ?? "";
  noteInput.value = profile?.note ?? "";
}

async function fetchProfile() {
  if (!token) throw new Error("UNAUTHORIZED");

  const res = await fetch(`${API_BASE}/api/my-account`, {
    method: "GET",
    headers: {
      Accept: "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (res.status === 401) throw new Error("UNAUTHORIZED");
  if (!res.ok) throw new Error(`HTTP_${res.status}`);

  return await res.json();
}

async function updateProfile(payload) {
  if (!token) throw new Error("UNAUTHORIZED");

  const res = await fetch(`${API_BASE}/api/my-account`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(payload),
  });

  if (res.status === 401) throw new Error("UNAUTHORIZED");
  if (!res.ok) throw new Error(`HTTP_${res.status}`);

  return await res.json();
}

function buildPayload() {
  return {
    name: nameInput.value.trim(),
    email: emailInput.value.trim(),
    phone: phoneInput.value.trim(),
    dob: dobInput.value, 
    note: noteInput.value.trim(),
  };
}

function validatePayload(p) {
  if (!p.name) return "氏名を入力してください。";
  if (!p.email) return "メールを入力してください。";
  return null;
}

async function load() {
  try {
    loadCartBadge?.();
    setState("loading");
    const profile = await fetchProfile();
    fillView(profile);
    fillForm(profile);
    setState("ready");
    setMode("view");
  } catch (e) {
    if (String(e?.message || e) === "UNAUTHORIZED") {
      setState("error", "ログインが必要です。ログインしてからご利用ください。");
      return;
    }
    setState("error", "プロフィールを取得できませんでした。");
    console.error(e);
  }
}

pageEl.addEventListener("click", async (e) => {
  const btn = e.target.closest("[data-action]");
  if (!btn) return;

  const action = btn.dataset.action;

  if (action === "reload-profile") {
    await load();
    return;
  }

  if (action === "toggle-edit") {
    const isEditing = !formEl.classList.contains("hidden");
    setMode(isEditing ? "view" : "edit");
    return;
  }

  if (action === "cancel-edit") {
    // reset về dữ liệu đang hiển thị
    // (option: refetch lại cho chắc)
    setMode("view");
    return;
  }
});

formEl.addEventListener("submit", async (e) => {
  e.preventDefault();

  try {
    hide(msgEl);

    const payload = buildPayload();
    const err = validatePayload(payload);
    if (err) {
      msgEl.textContent = err;
      show(msgEl);
      return;
    }

    msgEl.textContent = "保存中...";
    show(msgEl);

    const updated = await updateProfile(payload);

    fillView(updated);
    fillForm(updated);

    msgEl.textContent = "保存しました。";
    show(msgEl);

    setMode("view");
  } catch (e) {
    console.error(e);
    msgEl.textContent = "保存に失敗しました。もう一度お試しください。";
    show(msgEl);
  }
});

load();
