// Validaciones de acciones cuando estás en el grupo, toggles para información, copiar código, editar grupo y eliminar grupo

function toggleEditForm() {
    const form = document.getElementById("editForm");
    form.style.display = form.style.display === "none" ? "block" : "none";
  }
  
  function confirmarEliminacion() {
    return confirm("¿Estás seguro de que quieres eliminar este grupo?");
  }
  
  function copiarCodigo(boton) {
    const input = document.getElementById("codigoGrupo");
    const mensaje = document.getElementById("mensajeCopiado");
  
    // Copiar el código
    input.select();
    input.setSelectionRange(0, 99999); // Compatibilidad móvil
    document.execCommand("copy");
  
    // Mostrar mensaje de copiado
    mensaje.style.display = "block";
    mensaje.classList.add("show");
  
    // Ocultar después de 1.5 segundos
    setTimeout(() => {
      mensaje.classList.remove("show");
      mensaje.style.display = "none";
    }, 1500);
  }
  