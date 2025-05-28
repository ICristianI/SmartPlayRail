// Define primero applyGradient que nos dará los distintos gradientes de color seleccionables por el usuario

function applyGradient(gradientType) {
  if (gradientType === "bluepurple") {
    document.documentElement.style.setProperty("--bg-navbar", "linear-gradient(to right, #5729e1, #9622d5)");
    document.documentElement.style.setProperty("--bg-dropdown-hover", "linear-gradient(to right, #4b1cba, #7f1d9f)");
    document.documentElement.style.setProperty("--bg-dropdown-item-hover", "linear-gradient(to right, #4b1cba, #7f1d9f)");
  } else if (gradientType === "redyellow") {
    document.documentElement.style.setProperty("--bg-navbar", "linear-gradient(to right, #f0381f, #f8b500)");
    document.documentElement.style.setProperty("--bg-dropdown-hover", "linear-gradient(to right, #c02e18, #c69300)");
    document.documentElement.style.setProperty("--bg-dropdown-item-hover", "linear-gradient(to right, #c02e18, #c69300)");
  } else if (gradientType === "greenTurquoise") {
    document.documentElement.style.setProperty("--bg-navbar", "linear-gradient(to right, #20c997, #007bff)");
    document.documentElement.style.setProperty("--bg-dropdown-hover", "linear-gradient(to right, #199874, #0056b3)");
    document.documentElement.style.setProperty("--bg-dropdown-item-hover", "linear-gradient(to right, #199874, #0056b3)");
  }
}

// Luego define restoreThemeAndGradient para restaurar el tema y el gradiente guardados en localStorage
function restoreThemeAndGradient() {
  const savedTheme = localStorage.getItem("theme");
  if (savedTheme === "dark") {
    document.body.classList.add("dark-mode");
  } else {
    document.body.classList.remove("dark-mode");
  }

  const savedGradient = localStorage.getItem("selectedGradient");
  if (savedGradient) {
    applyGradient(savedGradient);
  }
}

// Y por último llama cuando todo está definido
document.addEventListener("DOMContentLoaded", function () {
  restoreThemeAndGradient();
});


document.addEventListener("DOMContentLoaded", function () {
  restoreThemeAndGradient();

  // Botones para cambiar color
  document.querySelectorAll(".btn[data-gradient]").forEach((button) => {
    button.addEventListener("click", function () {
      const gradientType = this.getAttribute("data-gradient");
      applyGradient(gradientType);
      localStorage.setItem("selectedGradient", gradientType);
    });
  });

  // Botón para alternar modo
  const toggleButton = document.getElementById("toggle-mode");
  if (toggleButton) {
    toggleButton.addEventListener("click", function () {
      const isDark = document.body.classList.toggle("dark-mode");
      toggleButton.innerHTML = isDark
        ? '<i class="fas fa-sun fa-2x"></i>'
        : '<i class="fas fa-moon fa-2x"></i>';
      localStorage.setItem("theme", isDark ? "dark" : "light");
    });
  }
});


// Cargar el color guardado desde localStorage si existe
document.addEventListener("DOMContentLoaded", function () {
  const savedGradient = localStorage.getItem("selectedGradient");
  if (savedGradient) {
    applyGradient(savedGradient);
  }
});