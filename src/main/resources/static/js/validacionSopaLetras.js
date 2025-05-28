
document.addEventListener("DOMContentLoaded", function () {
  const listaPalabras = document.getElementById("listaPalabras");
  const inputPalabras = document.getElementById("palabras");
  const palabraInput = document.getElementById("palabraInput");

  const palabrasSeleccionadas = new Set();

  // Validar input: solo letras y mayúsculas
  
  palabraInput.addEventListener("input", function () {
    this.value = this.value.replace(/[^a-zA-Z]/g, "").toUpperCase();
  });

  // Agregar palabra a la lista
  window.agregarPalabra = function () {
    const palabra = palabraInput.value.trim();

    if (palabra.length < 2) {
      alert("La palabra debe tener al menos 2 letras.");
      return;
    }
    if (palabra.length > 10) {
      alert("La palabra no puede tener más de 10 letras.");
      return;
    }
    if (palabrasSeleccionadas.size >= 6) {
      alert("Solo puedes agregar hasta 6 palabras.");
      return;
    }
    if (palabrasSeleccionadas.has(palabra)) {
      alert("Esta palabra ya fue agregada.");
      return;
    }

    palabrasSeleccionadas.add(palabra);
    const li = crearElementoLista(palabra, "eliminar-palabra");
    listaPalabras.appendChild(li);
    palabraInput.value = "";
    actualizarPalabrasSeleccionadas();
  };

  // Crear elemento visual
  function crearElementoLista(nombre, claseEliminar) {
    const li = document.createElement("li");
    li.classList.add(
      "list-group-item",
      "d-flex",
      "justify-content-between",
      "align-items-center",
      "item-seleccionado"
    );
    li.innerHTML = `
      <span class="text-break">${nombre}</span>
      <span class="${claseEliminar} close-icon" data-nombre="${nombre}" style="cursor:pointer;">&times;</span>
    `;
    return li;
  }

  // Eliminar palabra
  function agregarEventosEliminar(clase, setSeleccionados, actualizarLista) {
    document.querySelectorAll(`.${clase}`).forEach((btn) => {
      btn.addEventListener("click", function () {
        const nombre = btn.getAttribute("data-nombre");
        setSeleccionados.delete(nombre);
        btn.parentElement.remove();
        actualizarLista();
      });
    });
  }

  // Actualizar input oculto
  function actualizarPalabrasSeleccionadas() {
    inputPalabras.value = JSON.stringify(Array.from(palabrasSeleccionadas));
    agregarEventosEliminar("eliminar-palabra", palabrasSeleccionadas, actualizarPalabrasSeleccionadas);
  }

  // Guardar en formulario de creación
  window.guardarPalabras = function (event) {
    if (palabrasSeleccionadas.size < 2) {
      alert("Debes agregar al menos 2 palabras.");
      event.preventDefault();
      return;
    }
    inputPalabras.value = JSON.stringify(Array.from(palabrasSeleccionadas));
  };

  // Guardar en formulario de edición
  window.guardarPalabrasEditar = function (event) {
    if (palabrasSeleccionadas.size < 2) {
      alert("Debes agregar al menos 2 palabras antes de guardar.");
      event.preventDefault();
      return;
    }
    inputPalabras.value = JSON.stringify(Array.from(palabrasSeleccionadas));
  };

  // Cargar palabras ya existentes (modo edición)
  function cargarPalabrasExistentes() {
    try {
      const palabrasExistentes = JSON.parse(inputPalabras.value || "[]");
      palabrasExistentes.forEach((palabra) => {
        if (!palabrasSeleccionadas.has(palabra)) {
          palabrasSeleccionadas.add(palabra);
          const li = crearElementoLista(palabra, "eliminar-palabra");
          listaPalabras.appendChild(li);
        }
      });
      actualizarPalabrasSeleccionadas();
    } catch (error) {
      console.error("Error al cargar palabras existentes:", error);
    }
  }

  cargarPalabrasExistentes();
});
