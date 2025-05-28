// Este script se encarga de mostrar los elementos superpuestos en la ficha completa

//Cada tipo de elemento tiene su propio if que decide cómo se muestra y qué eventos se asignan
document.addEventListener("DOMContentLoaded", () => {
  const contenedor = document.getElementById("contenedorFicha");

  const elementosRaw = JSON.parse(document.getElementById("elementosSuperpuestosRaw").textContent);

  let joinSeleccionado = null;
  let conexiones = [];

  elementosRaw.forEach((elemento, i) => {
    const div = document.createElement("div");
    div.classList.add("elemento-editable");
    div.dataset.tipo = elemento.tipo;
    div.style.position = "absolute";
    div.style.left = `${elemento.x}px`;
    div.style.top = `${elemento.y}px`;
    div.style.zIndex = 10;

    if (elemento.tipo === "evaluado") {
      const input = document.createElement("input");
      input.className = "form-control";
      input.dataset.correcto = (elemento.texto || "").trim().toLowerCase();
      input.style.width = `${elemento.width}px`;
      input.style.height = `${elemento.height}px`;
      input.dataset.tipo = "evaluado";
    
      if (elemento.colorFondo) {
        input.style.backgroundColor = elemento.colorFondo;
    
        const transparente = elemento.colorFondo === "transparent" || elemento.colorFondo === "rgba(0, 0, 0, 0)";
        if (transparente) {
          input.style.border = "none";
          input.style.boxShadow = "none";
        }
      }
    
      if (elemento.colorTexto) input.style.color = elemento.colorTexto;
      if (elemento.tamanoLetra) input.style.fontSize = `${elemento.tamanoLetra}px`;

    
      div.appendChild(input);
    }
    
    

    if (elemento.tipo === "decorativo") {
      const textarea = document.createElement("textarea");
      textarea.className = "form-control";
      textarea.value = elemento.texto || "";
      textarea.readOnly = true;
      textarea.style.resize = "none";
      textarea.style.width = `${elemento.width}px`;
      textarea.style.height = `${elemento.height}px`;
    
      if (elemento.colorFondo) {
        textarea.style.backgroundColor = elemento.colorFondo;
        const transparente = elemento.colorFondo === "transparent" || elemento.colorFondo === "rgba(0, 0, 0, 0)";
        if (transparente) {
          textarea.style.border = "none";
          textarea.style.boxShadow = "none";
        }
      }
    
      if (elemento.colorTexto) textarea.style.color = elemento.colorTexto;
      if (elemento.tamanoLetra) textarea.style.fontSize = `${elemento.tamanoLetra}px`;
    
      div.appendChild(textarea);
    }
    
    
    

    if (elemento.tipo === "seleccion") {
      const name = `grupo_${i}`;
      (elemento.opciones || []).forEach((op, idx) => {
        const opt = document.createElement("div");
        const input = document.createElement("input");
        input.type = "radio";
        input.name = name;
        input.value = op.texto;
        input.dataset.correcta = op.correcta ? "true" : "false";
        input.dataset.tipo = "seleccion";
        
        const label = document.createElement("label");
        label.textContent = op.texto;
        label.style.marginLeft = "0.5rem";
        label.style.cursor = "pointer";
        
        opt.appendChild(input);
        opt.appendChild(label);
        div.appendChild(opt);
      });
    
      if (elemento.colorFondo) {
        div.style.backgroundColor = elemento.colorFondo;
    
        const transparente = elemento.colorFondo === "transparent" || elemento.colorFondo === "rgba(0, 0, 0, 0)";
        if (transparente) {
          div.style.border = "none";
        }
      }
    
      if (elemento.colorTexto) div.style.color = elemento.colorTexto;
      if (elemento.tamanoLetra) {
        div.style.fontSize = `${elemento.tamanoLetra}px`;
        div.querySelectorAll("label").forEach(label => {
          label.style.fontSize = `${elemento.tamanoLetra}px`;
        });
      }
            if (elemento.opcionesWidth) div.style.width = `${elemento.opcionesWidth}px`;
      if (elemento.opcionesHeight) div.style.height = `${elemento.opcionesHeight}px`;


    }
    
    if (elemento.tipo === "desplegable") {
      const select = document.createElement("select");
      select.className = "form-select";
      select.dataset.tipo = "desplegable";
      const correcta = (elemento.opciones.find(op => op.correcta) || {}).texto;
      select.dataset.correcto = (correcta ?? "").toString().trim().toLowerCase();
          
      (elemento.opciones || []).forEach(op => {
        const option = document.createElement("option");
        option.textContent = op.texto;
        select.appendChild(option);
      });
    
      if (elemento.colorFondo) {
        div.style.backgroundColor = elemento.colorFondo;
    
        const transparente = elemento.colorFondo === "transparent" || elemento.colorFondo === "rgba(0, 0, 0, 0)";
        if (transparente) {
          div.style.setProperty("border", "none", "important");
          div.style.setProperty("box-shadow", "none", "important");
          select.style.setProperty("background-color", "transparent", "important");
          select.style.setProperty("appearance", "none", "important");
          select.style.setProperty("-webkit-appearance", "none", "important");
          select.style.setProperty("-moz-appearance", "none", "important");
          select.style.setProperty("border", "none", "important");
          select.style.setProperty("box-shadow", "none", "important");
        } else {
          select.style.backgroundColor = elemento.colorFondo;
        }
      }
    
      if (elemento.colorTexto) {
        div.style.color = elemento.colorTexto;
        select.style.color = elemento.colorTexto;
      }
      if (elemento.tamanoLetra) {
        select.style.fontSize = `${elemento.tamanoLetra}px`;
      }
      
      if (elemento.opcionesWidth) select.style.width = `${elemento.opcionesWidth}px`;
      if (elemento.opcionesHeight) select.style.height = `${elemento.opcionesHeight}px`;

      div.appendChild(select);
    }
    
    

    if (elemento.tipo === "checkbox") {
      const cuadrado = document.createElement("div");
      cuadrado.className = "checkbox-cuadro";
      cuadrado.dataset.estado = elemento.estado;
      cuadrado.dataset.tipo = "checkbox";
      cuadrado.dataset.marcado = "false";
      const size = elemento.width || 25;
      cuadrado.style.width = `${size}px`;
      cuadrado.style.height = `${elemento.height || size}px`;          
      cuadrado.style.border = "2px solid #000";
      cuadrado.style.borderRadius = "4px";
      cuadrado.style.backgroundColor = "#fff";
      cuadrado.style.cursor = "pointer";

      cuadrado.addEventListener("click", () => {
        const marcado = cuadrado.dataset.marcado === "true";
        cuadrado.dataset.marcado = (!marcado).toString();
        cuadrado.style.backgroundColor = !marcado ? "#add8e6" : "#fff";
      });

      div.appendChild(cuadrado);
    }

    if (elemento.tipo === "join") {
      const joinBox = document.createElement("div");
      joinBox.innerHTML = "&nbsp;";
      joinBox.className = "join-box";
      joinBox.dataset.joinId = elemento.joinId;
      joinBox.style.padding = "5px";
      joinBox.style.border = "1px dashed #aaa";
      joinBox.style.background = "#f9f9f9";
      joinBox.style.cursor = "pointer";
      div.appendChild(joinBox);

      joinBox.addEventListener("click", () => {
        if (joinSeleccionado === joinBox) {
          joinBox.style.borderColor = "#aaa";
          joinSeleccionado = null;
        } else if (!joinSeleccionado) {
          joinSeleccionado = joinBox;
          joinBox.style.borderColor = "blue";
        } else {
          const parExistente = conexiones.find(c =>
            (c[0] === joinSeleccionado && c[1] === joinBox) ||
            (c[0] === joinBox && c[1] === joinSeleccionado)
          );
      
          if (parExistente) {
            conexiones = conexiones.filter(c =>
              !( (c[0] === joinSeleccionado && c[1] === joinBox) ||
                 (c[0] === joinBox && c[1] === joinSeleccionado) )
            );
            clearLines();
            drawAllLines();
          } else {
            const yaConectadoA = conexiones.some(c => c[0] === joinSeleccionado || c[1] === joinSeleccionado);
            const yaConectadoB = conexiones.some(c => c[0] === joinBox || c[1] === joinBox);
      
            if (!yaConectadoA && !yaConectadoB) {
              conexiones.push([joinSeleccionado, joinBox]);
              drawLineBetween(joinSeleccionado, joinBox);
            }
          }
      
          joinSeleccionado.style.borderColor = "#aaa";
          joinSeleccionado = null;
        }
      });
      
    }

    contenedor.appendChild(div);
  });

  // Crear capa SVG para las líneas del join
  function clearLines() {
    const svg = document.getElementById("svgLines");
    if (svg) svg.innerHTML = "";
  }

  // Dibujar todas las líneas al cargar las lineas del join
  function drawAllLines() {
    conexiones.forEach(([a, b]) => drawLineBetween(a, b));
  }

  // Dibujar líneas entre elementos join
  function drawLineBetween(div1, div2) {
    const svg = document.getElementById("svgLines") || createSvgLayer();
    const rect1 = div1.getBoundingClientRect();
    const rect2 = div2.getBoundingClientRect();
    const contRect = contenedor.getBoundingClientRect();

    const x1 = rect1.left + rect1.width / 2 - contRect.left;
    const y1 = rect1.top + rect1.height / 2 - contRect.top;
    const x2 = rect2.left + rect2.width / 2 - contRect.left;
    const y2 = rect2.top + rect2.height / 2 - contRect.top;

    const line = document.createElementNS("http://www.w3.org/2000/svg", "line");
    line.setAttribute("x1", x1);
    line.setAttribute("y1", y1);
    line.setAttribute("x2", x2);
    line.setAttribute("y2", y2);
    line.setAttribute("stroke", "blue");
    line.setAttribute("stroke-width", "2");

    svg.appendChild(line);
  }

  // Crear la capa SVG si no existe
  function createSvgLayer() {
    const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    svg.setAttribute("id", "svgLines");
    svg.style.position = "absolute";
    svg.style.top = 0;
    svg.style.left = 0;
    svg.style.width = "100%";
    svg.style.height = "100%";
    svg.style.zIndex = 5;
    contenedor.appendChild(svg);
    return svg;
  }

  // corregir los elementos de la ficha
  document.getElementById("btnCorregir")?.addEventListener("click", () => {
    let total = 0;
    let correctos = 0;

    document.querySelectorAll('[data-tipo="evaluado"]').forEach(input => {
      total++;
      input.style.backgroundColor = "";
      const esperado = input.dataset.correcto;
      const valor = (input?.value ?? "").toString().trim().toLowerCase();
      if (valor === esperado) {
        input.style.backgroundColor = "lightgreen";
        correctos++;
      } else {
        input.style.backgroundColor = "lightcoral";
      }
    });

    const grupos = new Set();
    document.querySelectorAll('input[data-tipo="seleccion"]').forEach(radio => {
      const label = radio.nextSibling;
      if (label) label.style.color = "";
      if (!grupos.has(radio.name)) {
        total++;
        const seleccionado = document.querySelector(`input[name="${radio.name}"]:checked`);
        if (seleccionado) {
          const lbl = seleccionado.nextSibling;
          if (lbl) {
            if (seleccionado.dataset.correcta === "true") {
              lbl.style.color = "green";
              correctos++;
            } else {
              lbl.style.color = "red";
            }
          }
        }
        grupos.add(radio.name);
      }
    });

    document.querySelectorAll('[data-tipo="checkbox"]').forEach(cuadro => {
      total++;
      const estado = cuadro.dataset.estado;
      const marcado = cuadro.dataset.marcado === "true";
      if ((estado === "correcto" && marcado) || ((estado === "incorrecto" || estado === "neutral") && !marcado)) {
        cuadro.style.backgroundColor = "lightgreen";
        correctos++;
      } else {
        cuadro.style.backgroundColor = "lightcoral";
      }
    });

    document.querySelectorAll('select[data-tipo="desplegable"]').forEach(select => {
      total++;
      select.style.backgroundColor = "";
    
      const correcto = (select?.dataset.correcto ?? "").toString().trim().toLowerCase();
      const valor = (select?.value ?? "").toString().trim().toLowerCase();

      if (valor === correcto) {
        select.style.backgroundColor = "lightgreen";
        correctos++;
      } else {
        select.style.backgroundColor = "lightcoral";
      }
    });
    

    document.querySelectorAll('.join-box').forEach(box => box.style.backgroundColor = "#f9f9f9");

    const evaluados = new Set();
    conexiones.forEach(([a, b]) => {
      const idA = a.dataset.joinId;
      const idB = b.dataset.joinId;
      if (idA === idB) {
        a.style.backgroundColor = "lightgreen";
        b.style.backgroundColor = "lightgreen";
        correctos++;
      } else {
        a.style.backgroundColor = "lightcoral";
        b.style.backgroundColor = "lightcoral";
      }
      evaluados.add(a);
      evaluados.add(b);
    });

    const joinBoxes = document.querySelectorAll('.join-box');
    if (joinBoxes.length > 0) {
      total += joinBoxes.length / 2;
    } else {
      conexiones = [];
    }
    
    
    total -= 2;

    const nota = total > 0 ? ((correctos / total) * 10).toFixed(1) : "0.0";

    // Mostrar nota al lado del botón
    document.getElementById("notaResultado").textContent = `Nota: ${nota} / 10`;
    
    // Mostrar modal con la nota
    document.getElementById("textoNotaModal").textContent = `Tu nota es: ${nota} / 10`;
    const modalNota = new bootstrap.Modal(document.getElementById("modalNota"));
    modalNota.show();


// Guardar nota en el backend
    fetch('/f/guardarNota', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-CSRF-TOKEN': window.fichaData.csrfToken
      },
      body: JSON.stringify({
        fichaId: window.fichaData.fichaId,
        nota: parseFloat(nota)
      })
    })
    .then(res => {
      if (!res.ok) throw new Error("Error al guardar nota");
      console.log("Nota guardada");
    })
    .catch(err => {
      console.error("Error al guardar nota", err);
    });

      });
  
  // Descargar PDF de la ficha
  window.descargarPDF = async function () {
    const contenedor = document.getElementById("contenedorFicha");
    const reemplazos = [];
  
    // Reemplazar solo inputs de texto
    contenedor.querySelectorAll("input[type='text']").forEach(input => {
      const div = document.createElement("div");
      div.textContent = input.value;
      copiarEstilos(input, div);
      div.style.whiteSpace = "nowrap";
      div.style.overflow = "hidden";
      div.style.textOverflow = "ellipsis";
  
      input.style.visibility = "hidden";
      input.parentNode.appendChild(div);
      reemplazos.push(() => {
        div.remove();
        input.style.visibility = "visible";
      });
    });
  
    // Reemplazar textareas (decorativos)
    contenedor.querySelectorAll("textarea").forEach(textarea => {
      const div = document.createElement("div");
      div.textContent = textarea.value;
      copiarEstilos(textarea, div);
      div.style.whiteSpace = "pre-wrap";
      div.style.wordBreak = "break-word";
  
      textarea.style.visibility = "hidden";
      textarea.parentNode.appendChild(div);
      reemplazos.push(() => {
        div.remove();
        textarea.style.visibility = "visible";
      });
    });
  
    await new Promise(resolve => setTimeout(resolve, 100));
  
    const canvas = await html2canvas(contenedor, {
      scale: 2,
      useCORS: true,
      backgroundColor: null
    });
  
    const imgData = canvas.toDataURL("image/png");
    const pdf = new window.jspdf.jsPDF("p", "mm", "a4");
    const imgWidth = 210;
    const imgHeight = (canvas.height * imgWidth) / canvas.width;
  
    pdf.addImage(imgData, "PNG", 0, 0, imgWidth, imgHeight);
    pdf.save("ficha_interactiva.pdf");
  
    reemplazos.forEach(restore => restore());
  };
  
  

  
  function copiarEstilos(origen, destino) {
    const estilos = window.getComputedStyle(origen);
    destino.style.position = "absolute";
    destino.style.left = "0";
    destino.style.top = "0";
    destino.style.width = estilos.width;
    destino.style.height = estilos.height;
    destino.style.fontSize = estilos.fontSize;
    destino.style.fontFamily = estilos.fontFamily;
    destino.style.color = estilos.color;
    destino.style.backgroundColor = estilos.backgroundColor;
    destino.style.border = estilos.border;
    destino.style.padding = "0";
    destino.style.margin = "0";
    destino.style.lineHeight = estilos.lineHeight;
    destino.style.boxSizing = estilos.boxSizing;
    destino.style.zIndex = estilos.zIndex;
    destino.style.whiteSpace = "pre-wrap";
    destino.style.wordBreak = "break-word";
    destino.style.display = "inline-block";
    destino.style.verticalAlign = "top";
  }
  
  
  
  
  
  
  
});

// Funciones para reiniciar la ficha y corregirla
window.reiniciarFicha = function () {
  // Elimina todos los inputs, selecciones, colores, líneas y respuestas del usuario
  document.querySelectorAll('[data-tipo="evaluado"]').forEach(input => {
    input.value = "";
    input.style.backgroundColor = "";
  });

  document.querySelectorAll('input[data-tipo="seleccion"]').forEach(radio => {
    radio.checked = false;
    const label = radio.nextSibling;
    if (label) label.style.color = "";
  });

  document.querySelectorAll('[data-tipo="checkbox"]').forEach(cuadro => {
    cuadro.dataset.marcado = "false";
    cuadro.style.backgroundColor = "#fff";
  });

  document.querySelectorAll('select[data-tipo="desplegable"]').forEach(select => {
    select.selectedIndex = 0;
    select.style.backgroundColor = "";
  });

  document.querySelectorAll('.join-box').forEach(box => {
    box.style.borderColor = "#aaa";
    box.style.backgroundColor = "#f9f9f9";
  });

  const svg = document.getElementById("svgLines");
  if (svg) svg.innerHTML = "";

  // Reinicia variables globales
  conexiones = [];
  joinSeleccionado = null;

  // Limpia la nota mostrada
  const notaElem = document.getElementById("notaResultado");
  if (notaElem) notaElem.textContent = "";
};

window.corregirFicha = function () {
  document.getElementById("btnCorregir")?.click();
};

