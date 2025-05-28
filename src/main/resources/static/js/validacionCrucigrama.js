document.addEventListener("DOMContentLoaded", function () {
    const listaPistas = document.getElementById("listaPistas");
    const inputPistas = document.getElementById("pistas");
    const inputRespuestas = document.getElementById("respuestas");
    const pistaInput = document.getElementById("pistaInput");
    const respuestaInput = document.getElementById("respuestaInput");
  
    const paresSeleccionados = new Set();
  
    // Validar input: pistas pueden ser cualquier texto, respuestas solo letras y mayúsculas
    respuestaInput.addEventListener("input", function () {
      this.value = this.value.replace(/[^a-zA-Z]/g, "").toUpperCase();
    });
  
    // Agregar par de pista y respuesta a la lista
    window.agregarPista = function () {
      const pista = pistaInput.value.trim();
      const respuesta = respuestaInput.value.trim().toUpperCase();
  
      if (pista.length < 5) {
        alert("La pista debe tener al menos 5 caracteres.");
        return;
      }
      if (pista.length > 50) {
        alert("La pista no puede tener más de 50 caracteres.");
        return;
      }
      if (respuesta.length < 2) {
        alert("La respuesta debe tener al menos 2 letras.");
        return;
      }
      if (respuesta.length > 10) {
        alert("La respuesta no puede tener más de 10 letras.");
        return;
      }
      if (paresSeleccionados.size >= 10) {
        alert("Solo puedes agregar hasta 10 pares de pista y respuesta.");
        return;
      }
      if (paresSeleccionados.has(respuesta)) {
        alert("Esta respuesta ya fue agregada.");
        return;
      }
  
      // Agregar el par a la lista visual y al conjunto
      paresSeleccionados.add(respuesta);
      const li = crearElementoLista(pista, respuesta, "eliminar-pista");
      listaPistas.appendChild(li);
  
      pistaInput.value = "";
      respuestaInput.value = "";
      actualizarPistasRespuestas();
    };
  
    // Función para actualizar los inputs ocultos con las pistas y respuestas seleccionadas
      function actualizarPistasRespuestas() {
    const pistasArray = [];
    const respuestasArray = [];

    listaPistas.querySelectorAll("li").forEach((li) => {
      const pista = li.querySelector(".pista-texto").textContent;
      const respuesta = li.querySelector(".respuesta-texto").textContent;
      pistasArray.push(pista);
      respuestasArray.push(respuesta);
    });

    inputPistas.value = pistasArray.join(",");
    inputRespuestas.value = respuestasArray.join(",");
  }

  // Delegación para manejar clicks en botones "×"
  listaPistas.addEventListener("click", function (event) {
    if (event.target.classList.contains("eliminar-pista")) {
      const respuesta = event.target.getAttribute("data-respuesta");
      paresSeleccionados.delete(respuesta);
      event.target.closest("li").remove();
      actualizarPistasRespuestas();
    }
  });

    
  
    // Función para crear elementos de la lista con "×"
    function crearElementoLista(pista, respuesta, claseEliminar) {
        const li = document.createElement("li");
        li.classList.add(
          "list-group-item",
          "d-flex",
          "justify-content-between",
          "align-items-center",
          "item-seleccionado"
        );
        li.innerHTML = `
          <span class="pista-texto">${pista}</span>
          <strong class="respuesta-texto">${respuesta}</strong>
          <span class="${claseEliminar} close-icon" data-respuesta="${respuesta}" style="cursor:pointer;">&times;</span>
        `;
        return li;
      }
      
  
    // Función para eliminar pistas y respuestas
    window.agregarEventosEliminar = function(clase, setSeleccionados, actualizarLista) {
        document.querySelectorAll(`.${clase}`).forEach((btn) => {
          btn.addEventListener("click", function () {
            const respuesta = btn.getAttribute("data-respuesta");
            setSeleccionados.delete(respuesta);
            btn.closest("li").remove();
            actualizarLista();
          });
        });
      }
      
  
    // Validar mínimo de 3 pares antes de enviar el formulario
    window.guardarPistas = function (event) {
        const nuevasPistas = pistaInput.value.trim();
        const nuevasRespuestas = respuestaInput.value.trim();
    
        // Si no hay cambios, no hacer nada
        if (!nuevasPistas && !nuevasRespuestas && listaPistas.children.length === 0) {
            alert("No puedes guardar sin añadir pistas y respuestas.");
            event.preventDefault();
            return;
        }
    
        actualizarPistasRespuestas();
    };

    // Cargar pistas y respuestas existentes al inicio
    function cargarPistasExistentes() {
        const pistasRaw = inputPistas.value.trim();
        const respuestasRaw = inputRespuestas.value.trim();
      
        if (pistasRaw && respuestasRaw) {
          const pistas = pistasRaw.split(",");
          const respuestas = respuestasRaw.split(",");
      
          for (let i = 0; i < pistas.length; i++) {
            const pista = pistas[i];
            const respuesta = respuestas[i] || "";
            paresSeleccionados.add(respuesta);
            const li = crearElementoLista(pista, respuesta, "eliminar-pista");
            listaPistas.appendChild(li);
          }
        }
      }

      cargarPistasExistentes();
    
  });

  
  
  