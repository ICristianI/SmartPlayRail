// Permite seleccionar los cuadernos a agregar o quitar de un grupo que estás creando o editando.

document.addEventListener("DOMContentLoaded", function () {
    const listaCuadernos = document.getElementById("listaCuadernos");
    const inputCuadernos = document.getElementById("cuadernosSeleccionados");
  
    const cuadernosSeleccionados = new Set();
  
    document.querySelectorAll(".cuaderno-item").forEach((cuaderno) => {
      cuaderno.addEventListener("click", function () {
        const id = cuaderno.getAttribute("data-id");
  
        if (cuadernosSeleccionados.has(id)) {
          cuadernosSeleccionados.delete(id);
          cuaderno.classList.remove("cuaderno-seleccionado");
        } else {
          cuadernosSeleccionados.add(id);
          cuaderno.classList.add("cuaderno-seleccionado");
        }
  
        actualizarListaCuadernos();
      });
    });
  
    function actualizarListaCuadernos() {
      listaCuadernos.innerHTML = "";
      cuadernosSeleccionados.forEach((id) => {
        const cuaderno = document.querySelector(`.cuaderno-item[data-id='${id}']`);
        const nombre = cuaderno.getAttribute("data-nombre");
  
        const li = crearElementoLista(id, nombre, "eliminar-cuaderno");
        listaCuadernos.appendChild(li);
      });
  
      inputCuadernos.value = Array.from(cuadernosSeleccionados).join(",");
      agregarEventosEliminar(
        "eliminar-cuaderno",
        cuadernosSeleccionados,
        actualizarListaCuadernos,
        ".cuaderno-item"
      );
    }
  
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
  
    function agregarEventosEliminar(clase, setSeleccionados, actualizarLista, selectorItem) {
      document.querySelectorAll(`.${clase}`).forEach((btn) => {
        btn.addEventListener("click", function () {
          const id = btn.getAttribute("data-id");
          setSeleccionados.delete(id);
  
          const item = document.querySelector(`${selectorItem}[data-id='${id}']`);
          if (item) item.classList.remove("cuaderno-seleccionado");
  
          actualizarLista();
        });
      });
    }
  });
  