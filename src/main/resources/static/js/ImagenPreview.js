const LIMITE_MB = 5;
const LIMITE_BYTES = LIMITE_MB * 1024 * 1024;

let imagenOriginal = document.getElementById("previewImagen")?.src;

// Muestra la vista previa de la imagen seleccionada minimizada y validación de archivo seleccionado

window.mostrarVistaPrevia = function (event) {
  const input = event.target;
  const file = input.files[0];
  const preview = document.getElementById("previewImagen");

  if (!file) {
    preview.src = imagenOriginal;
    return;
  }

  if (!file.type.startsWith("image/")) {
    alert("Por favor, selecciona un archivo de imagen válido.");
    input.value = "";
    preview.src = imagenOriginal;
    return;
  }

  if (file.size > LIMITE_BYTES) {
    alert(`El archivo "${file.name}" es demasiado grande. Máximo permitido: ${LIMITE_MB} MB.`);
    input.value = "";
    preview.src = imagenOriginal;
    return;
  }

  const reader = new FileReader();
  reader.onload = function (e) {
    preview.src = e.target.result;
    preview.style.display = "block";
  };
  reader.readAsDataURL(file);
};

window.restaurarImagen = function () {
  const preview = document.getElementById("previewImagen");
  const input = document.getElementById("imagenCuaderno");
  preview.src = imagenOriginal;
  input.value = "";
};
