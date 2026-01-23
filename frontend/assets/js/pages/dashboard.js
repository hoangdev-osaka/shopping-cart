import { fetchOverview } from "../api/admin.js";
import { checkLogin } from "../components/check-login.js";
const revenueEl = document.getElementById("revenueDelta");
const revenuePercentEl = document.getElementById("revenuePecent");
const filterLabelEls = document.querySelectorAll(".filter-lable");
const rangeEl = document.getElementById("rangeFilter");
const cardTitleEl = document.getElementById("cardTitle");
const yoyChartTitle = document.getElementById("yoyChartTitle");
const orderCountEl = document.getElementById("orderCount");
const orderCountPercentEl = document.getElementById("orderCountPercent");
const newUserCountEl = document.getElementById("newUserCount");
const newUserPercentEl = document.getElementById("newUserPercent");
const visitsCountEl = document.getElementById("visitsCount");
const visitsPercentEl = document.getElementById("visitsPercent");
const adminSidebarEl = document.getElementById("adminSidebar");
checkLogin();
const RANGE_LABEL = {
  LAST_7_DAYS: "過去7日間",
  TODAY: "今日",
  WTD: "今週",
  MTD: "今月",
  YTD: "今年",
};

const RANGE_TO_CHART_KEY = {
  LAST_7_DAYS: "7d",
  TODAY: "1d",
  WTD: "wtd",
  MTD: "mtd",
  YTD: "ytd",
};

document.addEventListener("DOMContentLoaded", async () => {
  await loadOverview();
  rangeEl.addEventListener("change", async (e) => {
    const enumRange = e.target.value;
    filterLabelEls.forEach((el) => (el.textContent = RANGE_LABEL[enumRange] ?? enumRange));
    updateSalesChart(enumRange);
    const overview = await fetchOverview(enumRange);
    renderRevenue(overview);
    renderOrderCount(overview);
    renderVisitsCount(overview);
    renderNewUserCount(overview);
  });
  loadBottomChart();
});
document.addEventListener("click", (e) => {
  const el = e.target.closest("[data-metric]");
  if (!el) return;
  switch (el.dataset.metric) {
    case "revenue":
      cardTitleEl.textContent = "売上推移";
      yoyChartTitle.textContent = "売上分析";
      break;
    case "orders":
      cardTitleEl.textContent = "オーダー推移";
      yoyChartTitle.textContent = "オーダー分析";
      break;
    case "visits":
      cardTitleEl.textContent = "訪問数推移";
      yoyChartTitle.textContent = "訪問数分析";
      break;
    case "signups":
      cardTitleEl.textContent = "新規登録数推移";
      yoyChartTitle.textContent = "新規登録数分析";
      break;
  }
});
document.addEventListener("click", async (e) => {
  const el = e.target.closest("[data-action]");
  if (!el) return;
  switch (el.dataset.action) {
    case "toggle-categories-popup":
      adminSidebarEl.classList.toggle("is-open");
      break;
  }
});
function initYoYBarChart() {
  const canvas = document.getElementById("revenueYoYBarChart");
  if (!canvas || !window.Chart) return;

  const labels = ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"];
  const thisYear = [420000, 380000, 510000, 460000, 530000, 600000, 720000, 680000, 640000, 760000, 820000, 900000];
  const lastYear = [390000, 360000, 480000, 430000, 500000, 560000, 690000, 650000, 610000, 730000, 780000, 860000];
  const adminBg = getComputedStyle(document.documentElement).getPropertyValue("--admin-color-bg");
  new Chart(canvas, {
    type: "bar",
    data: {
      labels,
      datasets: [
        {
          label: "今年",
          data: thisYear,
          backgroundColor: adminBg,
          borderRadius: 2,
          borderSkipped: false,
          barThickness: 20,
        },
        {
          label: "前年",
          data: lastYear,
          backgroundColor: "rgba(108, 107, 113, 0.35)",
          borderRadius: 2,
          borderSkipped: false,
          barThickness: 12,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: "top",
          labels: {
            boxWidth: 10,
            boxHeight: 10,
            usePointStyle: true,
            font: { weight: "600" },
          },
        },
        tooltip: {
          backgroundColor: "#ffffff",
          titleColor: "#111",
          bodyColor: "#111",
          borderColor: "#ddd",
          borderWidth: 1,
          cornerRadius: 10,
          padding: 16,
          titleFont: { size: 15, weight: "700" },
          bodyFont: { size: 14, weight: "600" },
          callbacks: {
            title: (items) => items[0].label,
            label: (ctx) => ` ${ctx.dataset.label}：¥${ctx.parsed.y.toLocaleString("ja-JP")}`,
          },
        },
      },
      scales: {
        x: { grid: { display: false }, ticks: { font: { weight: "600" } } },
        y: {
          beginAtZero: true,
          grid: { drawBorder: false },
          ticks: { callback: (v) => `${v / 10000}万円` },
        },
      },
      animation: { duration: 700, easing: "easeOutQuart" },
    },
  });
}
function bindOverviewBoxes() {
  const boxes = document.querySelectorAll(".overview-box");
  if (!boxes.length) return;

  boxes.forEach((box) => {
    box.addEventListener("click", () => {
      boxes.forEach((b) => b.classList.remove("is-active"));
      box.classList.add("is-active");
    });
  });
}

function bindMaskRemoveOnLoad() {
  window.addEventListener("load", () => {
    const mask = document.querySelector(".page-mask");
    if (!mask) return;

    mask.addEventListener("animationend", () => {
      mask.remove();
      document.body.style.overflow = "";
    });
  });
}
function loadBottomChart() {
  bindMaskRemoveOnLoad();
  bindOverviewBoxes();
  requestAnimationFrame(() => {
    requestAnimationFrame(() => {
      document.body.classList.add("is-loaded");
      if (typeof startAnimation === "function") startAnimation();

      setTimeout(() => {
        initSalesChart();
      }, 280);
      setTimeout(() => {
        initYoYBarChart();
      }, 420);
    });
  });
}

function formatYen(v) {
  const n = Number(v ?? 0);
  return `¥${n.toLocaleString("ja-JP")}`;
}

function renderRevenue(overview) {
  const revenue = overview?.revenue;
  if (!revenue) return;

  revenueEl.textContent = formatYen(revenue.currentRevenue);

  revenuePercentEl.classList.remove("is-negative");

  const rate = Number(revenue.changeRate ?? 0);
  if (rate < 0) revenuePercentEl.classList.add("is-negative");

  const sign = rate > 0 ? "+" : "";
  revenuePercentEl.textContent = `${sign}${rate.toFixed(2)}%`;
}

function renderOrderCount(overview) {
  const order = overview.orderCount;
  if (!order) return;
  orderCountEl.textContent = order.current;
  orderCountPercentEl.classList.remove("is-negative");

  const rate = Number(order.changeRate ?? 0);
  if (rate < 0) orderCountPercentEl.classList.add("is-negative");

  const sign = rate > 0 ? "+" : "";
  orderCountPercentEl.textContent = `${sign}${rate.toFixed(2)}%`;
}
function renderVisitsCount(overview) {
  const visits = overview.visitorCount;
  if (!visits) return;
  visitsCountEl.textContent = visits.current;
  visitsPercentEl.classList.remove("is-negative");

  const rate = Number(visits.changeRate ?? 0);
  if (rate < 0) visitsPercentEl.classList.add("is-negative");

  const sign = rate > 0 ? "+" : "";
  visitsPercentEl.textContent = `${sign}${rate.toFixed(2)}%`;
}
function renderNewUserCount(overview) {
  const user = overview.newUserCount;
  if (!user) return;
  newUserCountEl.textContent = user.current;
  newUserPercentEl.classList.remove("is-negative");

  const rate = Number(user.changeRate ?? 0);
  if (rate < 0) newUserPercentEl.classList.add("is-negative");

  const sign = rate > 0 ? "+" : "";
  newUserPercentEl.textContent = `${sign}${rate.toFixed(2)}%`;
}
let salesChart = null;
const DATA_BY_RANGE = {
  "1d": [
    13000, 12000, 18000, 9000, 22000, 15000, 26000, 14000, 12000, 18000, 9000, 22000, 15000, 26000, 14000, 12000, 18000, 9000, 22000, 15000,
    26000, 14000, 12000, 18000,
  ],
  "7d": [12000, 1300, 19000, 18000, 9000, 22000, 15000, 26000, 14000, 12000, 1300, 19000, 18000, 9000, 22000, 15000, 26000, 14000],
  wtd: [12000, 18000, 9000, 22000, 15000],
  mtd: [9000, 14000, 16000, 22000, 12000, 18000, 26000, 15000, 17000],
  ytd: [8000, 12000, 15000, 11000, 20000, 24000, 18000, 26000, 22000, 30000, 28000, 32000],
};

const LABELS_BY_RANGE = {
  "1d": [
    "0時",
    "1時",
    "2時",
    "3時",
    "4時",
    "5時",
    "6時",
    "7時",
    "8時",
    "9時",
    "10時",
    "11時",
    "12時",
    "13時",
    "14時",
    "15時",
    "16時",
    "17時",
    "18時",
    "19時",
    "20時",
    "21時",
    "22時",
    "23時",
  ],
  "7d": getLast7DaysLabels(),
  wtd: ["月", "火", "水", "木", "金", "土", "日"],
  mtd: Array.from({ length: 9 }, (_, i) => `${i + 1}`),
  ytd: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
};
function getLast7DaysLabels() {
  const daysJa = ["日", "月", "火", "水", "木", "金", "土"];
  const today = new Date();

  return Array.from({ length: 7 }, (_, i) => {
    const d = new Date(today);
    d.setDate(today.getDate() - (6 - i));
    return daysJa[d.getDay()];
  });
}

function getChartKeyFromEnum(enumRange) {
  return RANGE_TO_CHART_KEY[enumRange] || "7d";
}
function getLabels(chartKey) {
  return LABELS_BY_RANGE[chartKey] ?? LABELS_BY_RANGE["7d"];
}
function getValues(chartKey) {
  return DATA_BY_RANGE[chartKey] ?? DATA_BY_RANGE["7d"];
}

function initSalesChart() {
  const canvas = document.getElementById("salesBarChart");
  if (!canvas || !window.Chart) return;

  const chartKey = getChartKeyFromEnum(rangeEl?.value || "LAST_7_DAYS");

  if (salesChart) salesChart.destroy();

  salesChart = new Chart(canvas, {
    type: "line",
    data: {
      labels: getLabels(chartKey),
      datasets: [
        {
          data: getValues(chartKey),
          tension: 0.4,
          borderColor: "rgb(57, 36, 16)",
          backgroundColor: "rgba(88, 85, 67, 0.25)",
          fill: true,
          pointRadius: 0,
          pointHitRadius: 12,
          pointHoverRadius: 4,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: { duration: 900, easing: "easeOutQuart" },
      plugins: {
        legend: { display: false },
        tooltip: {
          backgroundColor: "#ffffff",
          titleColor: "#111",
          bodyColor: "#111",
          borderColor: "#ddd",
          borderWidth: 1,
          cornerRadius: 10,
          padding: 16,
          titleFont: { size: 15, weight: "700" },
          bodyFont: { size: 14, weight: "600" },
          callbacks: { label: (ctx) => `¥${Number(ctx.raw ?? 0).toLocaleString("ja-JP")}` },
        },
      },
      interaction: {
        mode: "nearest",
        intersect: false,
        axis: "x",
      },
      hover: {
        mode: "nearest",
        intersect: false,
      },

      scales: {
        x: { grid: { display: false }, ticks: { color: "rgb(0, 0, 0)", size: 15, weight: "700" } },
        y: {
          grid: { color: "rgba(0,0,0,0.05)" },
          ticks: { callback: (v) => `¥${Number(v).toLocaleString("ja-JP")}` },
        },
      },
    },
  });
}

function updateSalesChart(enumRange) {
  if (!salesChart) return;

  const chartKey = getChartKeyFromEnum(enumRange);
  salesChart.data.labels = getLabels(chartKey);
  salesChart.data.datasets[0].data = getValues(chartKey);
  salesChart.update({ duration: 700, easing: "easeOutQuart" });
}

async function loadOverview() {
  const enumRange = rangeEl.value;
  const label = RANGE_LABEL[enumRange] ?? enumRange;

  filterLabelEls.forEach((el) => (el.textContent = label));

  const overview = await fetchOverview(enumRange);
  renderRevenue(overview);
  renderOrderCount(overview);
  renderVisitsCount(overview);
  renderNewUserCount(overview);
}
