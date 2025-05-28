//Chequeo de valores del perfil del usuario y tamaño del archivo de foto de perfil

document.addEventListener("DOMContentLoaded", () => {
  const fileInput = document.getElementById("photo");
  if (!fileInput) {
    console.error("No se encontró el input con ID 'photo'.");
    return;
  }

  fileInput.addEventListener("change", (event) => {
    const file = event.target.files[0];
    if (file && file.size > 5242880) { // 5 MB
      alert("El archivo es demasiado grande. Por favor, selecciona uno menor a 5 MB.");
      event.preventDefault();
      fileInput.value = ""; // Limpia el archivo seleccionado
    } else {
      fileInput.form.submit();
    }
  });

  document.getElementById("edad").addEventListener("input", function () {
    if (this.value.length > 3) {
      this.value = this.value.slice(0, 3);
    }
  });
});
