//Script para mostrar overlay de espera al enviar el formulario de registro

document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector('form[action="/users/register"]');
    if (form) {
      form.addEventListener("submit", function () {
        const overlay = document.getElementById("overlay-espera");
        if (overlay) {
          overlay.style.display = "flex";
          document.body.style.overflow = "hidden";
        }
      });
    }
  });

// Script para limitar la entrada de edad a 2 dígitos

  document.addEventListener("DOMContentLoaded", () => {
    const edadInput = document.getElementById("edad");
    edadInput.addEventListener("input", function () {
      if (this.value.length > 2) {
        this.value = this.value.slice(0, 2);
      }
    });
  });