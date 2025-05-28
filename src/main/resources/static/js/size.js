
//Gestiona los archivos subidos y valida su tamaño

document.addEventListener("DOMContentLoaded", () => {
    const LIMITE_MB = 5;
    const LIMITE_BYTES = LIMITE_MB * 1024 * 1024;
  
    const archivos = document.querySelectorAll("input[type='file']");
  
    archivos.forEach((input) => {
      input.addEventListener("change", (event) => {
        const archivo = event.target.files[0];
        if (archivo && archivo.size > LIMITE_BYTES) {
          alert(`El archivo "${archivo.name}" es demasiado grande. Máximo permitido: ${LIMITE_MB} MB.`);
          event.target.value = "";
        }
      });
    });
  });
  