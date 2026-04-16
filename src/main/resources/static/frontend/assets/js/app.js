(() => {
  const STORAGE_KEY = "srads_theme";
  const AUTH_KEY = "srads_auth";

  function setTheme(theme) {
    const t = theme === "dark" ? "dark" : "light";
    document.documentElement.setAttribute("data-bs-theme", t);
    localStorage.setItem(STORAGE_KEY, t);
    const icon = document.querySelector("[data-theme-icon]");
    if (icon) icon.className = t === "dark" ? "fa-solid fa-moon" : "fa-solid fa-sun";
  }

  function initTheme() {
    const saved = localStorage.getItem(STORAGE_KEY);
    if (saved === "dark" || saved === "light") return setTheme(saved);
    const prefersDark = window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches;
    setTheme(prefersDark ? "dark" : "light");
  }

  function initSidebar() {
    const openBtn = document.querySelector("[data-sidebar-open]");
    const closeBtn = document.querySelector("[data-sidebar-close]");
    const backdrop = document.querySelector(".sidebar-backdrop");

    const open = () => document.body.classList.add("sidebar-open");
    const close = () => document.body.classList.remove("sidebar-open");

    if (openBtn) openBtn.addEventListener("click", open);
    if (closeBtn) closeBtn.addEventListener("click", close);
    if (backdrop) backdrop.addEventListener("click", close);

    document.addEventListener("keydown", (e) => {
      if (e.key === "Escape") close();
    });
  }

  function initRoleChips() {
    const chips = document.querySelectorAll("[data-role-chip]");
    const roleInput = document.querySelector("#role");
    if (!chips.length || !roleInput) return;

    const setActive = (value) => {
      chips.forEach((c) => c.classList.toggle("active", c.dataset.roleChip === value));
      roleInput.value = value;
    };

    chips.forEach((chip) => {
      chip.addEventListener("click", () => setActive(chip.dataset.roleChip));
    });

    setActive(roleInput.value || "STUDENT");
  }

  function initPasswordToggle() {
    const btn = document.querySelector("[data-toggle-password]");
    const input = document.querySelector("#password");
    if (!btn || !input) return;

    btn.addEventListener("click", () => {
      const isPassword = input.getAttribute("type") === "password";
      input.setAttribute("type", isPassword ? "text" : "password");
      btn.innerHTML = isPassword ? '<i class="fa-solid fa-eye-slash"></i>' : '<i class="fa-solid fa-eye"></i>';
    });
  }

  function initThemeToggle() {
    const btn = document.querySelector("[data-theme-toggle]");
    if (!btn) return;
    btn.addEventListener("click", () => {
      const current = document.documentElement.getAttribute("data-bs-theme");
      setTheme(current === "dark" ? "light" : "dark");
    });
  }

  window.SRADS = window.SRADS || {};
  window.SRADS.auth = {
    save(session) {
      localStorage.setItem(AUTH_KEY, JSON.stringify(session));
    },
    load() {
      try {
        return JSON.parse(localStorage.getItem(AUTH_KEY) || "null");
      } catch {
        return null;
      }
    },
    clear() {
      localStorage.removeItem(AUTH_KEY);
    },
    token() {
      return window.SRADS.auth.load()?.token || null;
    },
    async api(path, options = {}) {
      const headers = new Headers(options.headers || {});
      headers.set("Content-Type", "application/json");
      const token = window.SRADS.auth.token();
      if (token) headers.set("Authorization", `Bearer ${token}`);
      const res = await fetch(path, { ...options, headers });
      const text = await res.text();
      let data = null;
      try { data = text ? JSON.parse(text) : null; } catch { data = { message: text }; }
      if (!res.ok) {
        const msg = data?.message || "Request failed";
        throw new Error(msg);
      }
      return data;
    },
    redirectForRole(role) {
      if (role === "ADMIN") window.location.href = "/frontend/admin-dashboard.html";
      else if (role === "FACULTY") window.location.href = "/frontend/faculty-dashboard.html";
      else window.location.href = "/frontend/student-dashboard.html";
    },
    guard(allowedRoles = []) {
      const session = window.SRADS.auth.load();
      if (!session?.token) {
        window.location.href = "/frontend/login.html";
        return;
      }
      if (allowedRoles.length && !allowedRoles.includes(session.role)) {
        window.SRADS.auth.redirectForRole(session.role);
      }
    }
  };

  window.SRADS.charts = {
    studentCharts() {
      if (!window.Chart) return;

      const barEl = document.getElementById("chartSubjectMarks");
      const pieEl = document.getElementById("chartGradePie");
      const lineEl = document.getElementById("chartTrend");

      const gridColor = getComputedStyle(document.documentElement)
        .getPropertyValue("--app-border")
        .trim() || "rgba(0,0,0,.08)";

      const tickColor = getComputedStyle(document.body).color;

      if (barEl) {
        new Chart(barEl, {
          type: "bar",
          data: {
            labels: ["Math", "OOPS Java", "OS", "DSA", "DBMS", "English"],
            datasets: [{
              label: "Marks",
              data: [86, 78, 74, 92, 88, 81],
              backgroundColor: ["#0d6efd","#20c997","#6f42c1","#fd7e14","#198754","#0dcaf0"],
              borderRadius: 10
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
              x: { grid: { display: false }, ticks: { color: tickColor } },
              y: { grid: { color: gridColor }, ticks: { color: tickColor }, suggestedMax: 100 }
            }
          }
        });
      }

      if (pieEl) {
        new Chart(pieEl, {
          type: "doughnut",
          data: {
            labels: ["A", "B", "C", "D", "F"],
            datasets: [{
              data: [4, 2, 0, 0, 0],
              backgroundColor: ["#198754","#0d6efd","#ffc107","#fd7e14","#dc3545"],
              borderWidth: 0
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: "70%",
            plugins: { legend: { position: "bottom", labels: { color: tickColor } } }
          }
        });
      }

      if (lineEl) {
        new Chart(lineEl, {
          type: "line",
          data: {
            labels: ["Sem 1", "Sem 2", "Sem 3", "Sem 4", "Sem 5", "Sem 6"],
            datasets: [{
              label: "CGPA",
              data: [7.6, 7.9, 8.2, 8.4, 8.6, 8.7],
              borderColor: "#0d6efd",
              backgroundColor: "rgba(13,110,253,.15)",
              fill: true,
              tension: .35,
              pointRadius: 4,
              pointHoverRadius: 6
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
              x: { grid: { display: false }, ticks: { color: tickColor } },
              y: { grid: { color: gridColor }, ticks: { color: tickColor }, suggestedMax: 10 }
            }
          }
        });
      }
    },

    adminCharts() {
      if (!window.Chart) return;
      const el = document.getElementById("chartSystemUsage");
      if (!el) return;

      const gridColor = getComputedStyle(document.documentElement).getPropertyValue("--app-border").trim();
      const tickColor = getComputedStyle(document.body).color;

      new Chart(el, {
        type: "line",
        data: {
          labels: ["Mon","Tue","Wed","Thu","Fri","Sat","Sun"],
          datasets: [
            {
              label: "Logins",
              data: [120, 180, 150, 220, 260, 190, 240],
              borderColor: "#0d6efd",
              backgroundColor: "rgba(13,110,253,.12)",
              fill: true,
              tension: .35,
            },
            {
              label: "Reports",
              data: [20, 35, 28, 42, 60, 40, 55],
              borderColor: "#20c997",
              backgroundColor: "rgba(32,201,151,.10)",
              fill: true,
              tension: .35,
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { position: "bottom", labels: { color: tickColor } } },
          scales: {
            x: { grid: { display: false }, ticks: { color: tickColor } },
            y: { grid: { color: gridColor }, ticks: { color: tickColor } }
          }
        }
      });
    }
  };

  document.addEventListener("DOMContentLoaded", () => {
    initTheme();
    initThemeToggle();
    initSidebar();
    initRoleChips();
    initPasswordToggle();
  });
})();

