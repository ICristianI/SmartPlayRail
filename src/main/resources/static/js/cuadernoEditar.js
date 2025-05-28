document.addEventListener("DOMContentLoaded", function () {
  console.log("Script de edición de cuadernos cargado correctamente.");

  // Manejo de selección de fichas
  
  document.querySelectorAll(".ficha-item").forEach(function (item) {
    item.addEventListener("click", function () {
      var id = this.getAttribute("data-id");
      var nombre = this.getAttribute("data-nombre");
      agregarFicha(id, nombre);
    });
  });

  // Manejo de selección de juegos
  document.querySelectorAll(".juego-item").forEach(function (item) {
    item.addEventListener("click", function () {
      var id = this.getAttribute("data-id");
      var nombre = this.getAttribute("data-nombre");
      agregarJuego(id, nombre);
    });
  });
});

// Alternar el formulario de edición
function toggleEditForm() {
  var form = document.getElementById("editForm");
  form.style.display = form.style.display === "none" ? "block" : "none";
}

// Confirmación antes de eliminar el cuaderno
function confirmarEliminacion() {
  return confirm(
    "¿Estás seguro de que quieres eliminar este cuaderno? Esta acción no se puede deshacer."
  );
}

// Agregar ficha seleccionada a la lista
function agregarFicha(id, nombre) {
  var lista = document.getElementById("listaFichas");
  var itemExistente = document.querySelector(
    "#listaFichas li[data-id='" + id + "']"
  );

  if (!itemExistente) {
    lista.appendChild(li);
    actualizarFichasSeleccionadas();
  }
}

// Eliminar ficha de la lista seleccionada
function eliminarFicha(id) {
  var item = document.querySelector("#listaFichas li[data-id='" + id + "']");
  if (item) {
    item.remove();
    actualizarFichasSeleccionadas();
  }
}

// Actualizar el input oculto con las fichas seleccionadas
function actualizarFichasSeleccionadas() {
  var ids = [];
  document.querySelectorAll("#listaFichas li").forEach((ficha) => {
    ids.push(ficha.getAttribute("data-id"));
  });
  document.getElementById("fichasSeleccionadas").value = ids.join(",");
}

// Agregar juego seleccionado a la lista
function agregarJuego(id, nombre) {
  var lista = document.getElementById("listaJuegos");
  var itemExistente = document.querySelector(
    "#listaJuegos li[data-id='" + id + "']"
  );

  if (!itemExistente) {
    lista.appendChild(li);
    actualizarJuegosSeleccionados();
  }
}

// Eliminar juego de la lista seleccionada
function eliminarJuego(id) {
  var item = document.querySelector("#listaJuegos li[data-id='" + id + "']");
  if (item) {
    item.remove();
    actualizarJuegosSeleccionados();
  }
}

// Actualizar el input oculto con los juegos seleccionados
function actualizarJuegosSeleccionados() {
  var ids = [];
  document.querySelectorAll("#listaJuegos li").forEach((juego) => {
    ids.push(juego.getAttribute("data-id"));
  });
  document.getElementById("juegosSeleccionados").value = ids.join(",");
}
