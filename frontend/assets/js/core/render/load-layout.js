async function loadPartial(id, url) {
  const el = document.getElementById(id);
  if (!el) return false;

  const res = await fetch(url);
  if (!res.ok) throw new Error(`Cannot GET ${url} (${res.status})`);

  el.innerHTML = await res.text();
  return true;
}

document.addEventListener("DOMContentLoaded", async () => {
  await loadPartial("app-footer", "/pages/partials/footer.html");
});
