document.addEventListener("DOMContentLoaded", function () {
  const listaFichas = document.getElementById("listaFichas");
  const listaJuegos = document.getElementById("listaJuegos");
  const inputFichas = document.getElementById("fichasSeleccionadas");
  const inputJuegos = document.getElementById("juegosSeleccionados");

  const fichasSeleccionadas = new Set();
  const juegosSeleccionados = new Set();

  // Manejar selección de fichas en el modal
  document.querySelectorAll(".ficha-item").forEach((ficha) => {
    ficha.addEventListener("click", function () {
      const id = ficha.getAttribute("data-id");

      if (fichasSeleccionadas.has(id)) {
        fichasSeleccionadas.delete(id);
        ficha.classList.remove("ficha-seleccionada");
      } else {
        fichasSeleccionadas.add(id);
        ficha.classList.add("ficha-seleccionada");
      }

      actualizarListaFichas();
    });
  });

  // Manejar selección de juegos en el modal
  document.querySelectorAll(".juego-item").forEach((juego) => {
    juego.addEventListener("click", function () {
      const id = juego.getAttribute("data-id");

      if (juegosSeleccionados.has(id)) {
        juegosSeleccionados.delete(id);
        juego.classList.remove("juego-seleccionado");
      } else {
        juegosSeleccionados.add(id);
        juego.classList.add("juego-seleccionado");
      }

      actualizarListaJuegos();
    });
  });

  // Función para actualizar la lista de fichas seleccionadas
  function actualizarListaFichas() {
    listaFichas.innerHTML = "";
    fichasSeleccionadas.forEach((id) => {
      const ficha = document.querySelector(`.ficha-item[data-id='${id}']`);
      const nombre = ficha.getAttribute("data-nombre");

      const li = crearElementoLista(id, nombre, "eliminar-ficha");
      listaFichas.appendChild(li);
    });

    inputFichas.value = Array.from(fichasSeleccionadas).join(",");
    agregarEventosEliminar(
      "eliminar-ficha",
      fichasSeleccionadas,
      actualizarListaFichas,
      ".ficha-item"
    );
  }

  // Función para actualizar la lista de juegos seleccionados
  function actualizarListaJuegos() {
    listaJuegos.innerHTML = "";
    juegosSeleccionados.forEach((id) => {
      const juego = document.querySelector(`.juego-item[data-id='${id}']`);
      const nombre = juego.getAttribute("data-nombre");

      const li = crearElementoLista(id, nombre, "eliminar-juego");
      listaJuegos.appendChild(li);
    });

    inputJuegos.value = Array.from(juegosSeleccionados).join(",");
    agregarEventosEliminar(
      "eliminar-juego",
      juegosSeleccionados,
      actualizarListaJuegos,
      ".juego-item"
    );
  }

  // Función para crear elementos de la lista con una "×"
  function crearElementoLista(id, nombre, claseEliminar) {
    const li = document.createElement("li");
    li.classList.add(
      "list-group-item",
      "d-flex",
      "justify-content-between",
      "align-items-center",
      "item-seleccionado"
    );
    li.innerHTML = `
          ${nombre}
          <span class="${claseEliminar} close-icon" data-id="${id}">&times;</span>
      `;
    return li;
  }

  // Función para agregar eventos de eliminación a las "×"
  
  function agregarEventosEliminar(
    clase,
    setSeleccionados,
    actualizarLista,
    selectorItem
  ) {
    document.querySelectorAll(`.${clase}`).forEach((btn) => {
      btn.addEventListener("click", function () {
        const id = btn.getAttribute("data-id");
        setSeleccionados.delete(id);

        const item = document.querySelector(`${selectorItem}[data-id='${id}']`);
        if (item)
          item.classList.remove("ficha-seleccionada", "juego-seleccionado");

        actualizarLista();
      });
    });
  }
});
