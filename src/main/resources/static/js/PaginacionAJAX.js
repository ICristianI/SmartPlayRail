// esta función permite cargar una página específica de fichas, juegos, cuadernos o usuarios

function cargarPagina(button, tipo) {
  let pagina = button.getAttribute("data-page");
  if (!pagina) {
    console.warn("No se encontró data-page en el botón.");
    return;
  }

  let size;
  if (tipo === "fichas") {
    size = document.getElementById("pageSizeFichas")?.value || "3";
  } else if (tipo === "juegos") {
    size = document.getElementById("pageSizeJuegos")?.value || "3";
  } else if (
    tipo === "cuadernos" ||
    tipo === "cuadernos2" ||
    tipo === "cuadernosGrupo"
  ) {
    size = document.getElementById("pageSizeCuadernos")?.value || "3";
  } else if (tipo === "usuariosGrupo") {
    size = document.getElementById("pageSizeUsuarios")?.value || "3";
  }

  let urlParams = new URLSearchParams(window.location.search);

  if (tipo === "fichas") {
    urlParams.set("pageFichas", pagina);
  } else if (tipo === "juegos") {
    urlParams.set("pageJuegos", pagina);
  } else if (
    tipo === "cuadernos" ||
    tipo === "cuadernos2" ||
    tipo === "cuadernosGrupo"
  ) {
    urlParams.set("pageCuadernos", pagina);
  } else if (tipo === "usuariosGrupo") {
    urlParams.set("pageUsuarios", pagina);
  }

  urlParams.set("size", size);

  if (tipo === "cuadernos2") {
    const juegoId = document.getElementById("juegoId")?.value;
    const tipoJuego = document.getElementById("tipoJuegoActual")?.value;
    const csrfToken = document.querySelector('input[name="_csrf"]')?.value;
  
    if (!juegoId || !tipoJuego) {
      console.error("Faltan juegoId o tipoJuego");
      return;
    }
  
    urlParams.set("juegoId", juegoId);
    const url = `/${tipoJuego}/ver?${urlParams.toString()}`;
  
    fetch(url, {
      method: "GET",
      headers: {
        "X-Requested-With": "XMLHttpRequest",
        "X-CSRF-TOKEN": csrfToken
      }
    })
      .then((response) => {
        if (response.status === 403) {
          throw new Error("Acceso denegado (403). ¿Falta el token CSRF?");
        }
        return response.text();
      })
      .then((html) => {
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, "text/html");
  
        actualizarContenido("#contenedorCuadernos", doc);
        actualizarContenido("#navPaginacionCuadernos", doc);
        actualizarContenido("#navPaginacionCuadernos2", doc);

        actualizarBotonesPaginacion();
      })
      .catch((error) => console.error("Error cargando cuadernos2:", error));
    
    return;
  }
  
  // Resto de tipos normales
  let url = "";
  if (tipo === "cuadernos") {
    url = `/f/verFicha?${urlParams.toString()}`;
  } else if (tipo === "fichas" || tipo === "juegos") {
    url = `/cuadernos/ver?${urlParams.toString()}`;
  } else if (tipo === "cuadernosGrupo" || tipo === "usuariosGrupo") {
    url = window.location.pathname + "?" + urlParams.toString();
  }

  fetch(url, {
    method: "GET",
    headers: { "X-Requested-With": "XMLHttpRequest" },
  })
    .then((response) => response.text())
    .then((html) => {
      let parser = new DOMParser();
      let doc = parser.parseFromString(html, "text/html");

      if (tipo === "fichas") {
        actualizarContenido("#contenedorFichas", doc);
        actualizarContenido("#navPaginacionFichas", doc);
      } else if (tipo === "juegos") {
        actualizarContenido("#contenedorJuegos", doc);
        actualizarContenido("#navPaginacionJuegos", doc);
      } else if (
        tipo === "cuadernos" ||
        tipo === "cuadernosGrupo"
      ) {
        actualizarContenido("#contenedorCuadernos", doc);
        actualizarContenido("#navPaginacionCuadernos", doc);
      } else if (tipo === "usuariosGrupo") {
        actualizarContenido("#contenedorUsuarios", doc);
        actualizarContenido("#navPaginacionUsuarios", doc);
      }

      actualizarBotonesPaginacion();
    })
    .catch((error) => console.error("Error cargando la página:", error));
}

/// Actualiza el contenido de un selector específico con el nuevo contenido del documento

function actualizarContenido(selector, doc) {
  let nuevoContenido = doc.querySelector(selector);
  let contenedor = document.querySelector(selector);
  if (nuevoContenido && contenedor) {
    contenedor.innerHTML = nuevoContenido.innerHTML;
  }
}

// Actualiza los botones de paginación para que funcionen con AJAX

function actualizarBotonesPaginacion() {
  document
    .querySelectorAll(".pagination .page-link, .pagination .btn")
    .forEach((button) => {
      button.onclick = function () {
        let nav = button.closest("nav");
        if (!nav) return;

        let tipo = nav.id.replace("navPaginacion", "").toLowerCase();

        if (tipo === "cuadernos" && window.location.pathname.includes("/grupos/")) {
          tipo = "cuadernosGrupo";
        }

        if (tipo === "usuarios" && window.location.pathname.includes("/grupos/")) {
          tipo = "usuariosGrupo";
        }

        cargarPagina(this, tipo);
      };
    });
}

document.addEventListener("DOMContentLoaded", actualizarBotonesPaginacion);
window.cargarPagina = cargarPagina;
