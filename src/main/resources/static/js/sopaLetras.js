
// La sopa de se implementa con una matriz de letras y permite a los usuarios seleccionar palabras de una lista que se irá creando dinámicamente.
document.addEventListener("DOMContentLoaded", function () {
    const sopaContainer = document.getElementById("sopaDeLetras");
    const listaPalabras = document.getElementById("listaPalabras");
    const mensaje = document.getElementById("mensaje");

    console.log("Juego cargado:", juego);
    console.log("Contenido de juego.palabras:", juego.palabras);

    let palabras = [...juego.palabras];
    let palabrasOriginales = [...juego.palabras];

    let gridSize = 10;

    let grid = [];
    let seleccionActual = "";
    let celdasSeleccionadas = [];
    let juegoTerminado = false;

    sopaContainer.style.display = "grid";
    sopaContainer.style.gridTemplateColumns = `repeat(${gridSize}, 40px)`;
    sopaContainer.style.gridTemplateRows = `repeat(${gridSize}, 40px)`;
    sopaContainer.style.width = `${gridSize * 45}px`;
    sopaContainer.style.height = `${gridSize * 40}px`;
    sopaContainer.style.gap = "5px";
    sopaContainer.style.margin = "auto";

    // Inicializar la cuadrícula
    function inicializarGrid() {
        grid = Array.from({ length: gridSize }, () => Array(gridSize).fill(null));
    }

    //Colocamos las palabras en la cuadrícula
    function colocarPalabras() {
        palabras.forEach((palabra) => {
            let colocada = false;
            let intentos = 0;

            while (!colocada && intentos < 100) {
                intentos++;
                let dir = Math.floor(Math.random() * 3); // 0: horizontal, 1: vertical, 2: diagonal
            
                let maxRow = dir === 1 || dir === 2 ? gridSize - palabra.length : gridSize;
                let maxCol = dir === 0 || dir === 2 ? gridSize - palabra.length : gridSize;
            
                let row = Math.floor(Math.random() * maxRow);
                let col = Math.floor(Math.random() * maxCol);
            
                let puedeColocar = true;
            
                for (let i = 0; i < palabra.length; i++) {
                    let r = row + (dir === 1 ? i : dir === 2 ? i : 0);
                    let c = col + (dir === 0 ? i : dir === 2 ? i : 0);
            
                    if (
                        r >= gridSize ||
                        c >= gridSize ||
                        (grid[r][c] !== null && grid[r][c] !== palabra[i])
                    ) {
                        puedeColocar = false;
                        break;
                    }
                }
            
                if (puedeColocar) {
                    for (let i = 0; i < palabra.length; i++) {
                        let r = row + (dir === 1 ? i : dir === 2 ? i : 0);
                        let c = col + (dir === 0 ? i : dir === 2 ? i : 0);
                        grid[r][c] = palabra[i];
                    }
                    colocada = true;
                }
            }
            
        });
    }

    // Completar la cuadrícula con letras aleatorias
    function completarGrid() {
        const letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (let i = 0; i < gridSize; i++) {
            for (let j = 0; j < gridSize; j++) {
                if (grid[i][j] === null) {
                    grid[i][j] = letras[Math.floor(Math.random() * letras.length)];
                }
            }
        }
    }

    // Renderizar la cuadrícula en el contenedor con un estilo atractivo
    function renderizarGrid() {
        sopaContainer.innerHTML = "";
        for (let i = 0; i < gridSize; i++) {
            for (let j = 0; j < gridSize; j++) {
                const cell = document.createElement("div");
                cell.classList.add("sopa-cell");
                cell.textContent = grid[i][j];
                cell.dataset.row = i;
                cell.dataset.col = j;
                cell.style.width = "40px"; 
                cell.style.height = "40px"; 
                cell.style.display = "flex";
                cell.style.alignItems = "center";
                cell.style.justifyContent = "center";
                cell.style.border = "1px solid #ccc";
                cell.style.fontSize = "20px";
                cell.style.cursor = "pointer";
                cell.style.backgroundColor = ""; // Inicialmente sin color
                cell.addEventListener("click", seleccionarLetra);
                sopaContainer.appendChild(cell);
            }
        }
    }

    // Función que al seleccionar una letra, la añade a la selección actual y verifica si forma una palabra al derecho o al revés, afectando el estilo de las celdas seleccionadas y con sus restricciones de adyacencia.
    function seleccionarLetra(event) {
        if (juegoTerminado) return;
    
        const cell = event.target;
        console.log(`Clic en letra: ${cell.textContent} (Fila: ${cell.dataset.row}, Col: ${cell.dataset.col})`);
    
        if (celdasSeleccionadas.includes(cell)) {
            console.log(`Letra ${cell.textContent} deseleccionada`);
            cell.classList.remove("seleccionada");
    
            if (cell.classList.contains("palabra-encontrada")) {
                console.log(`Letra ${cell.textContent} ya es parte de una palabra encontrada, restaurando borde`);
                cell.style.border = "1px solid #ccc"; // Restaurar borde normal
            } else {
                cell.style.backgroundColor = ""; // Restaurar sin color
            }
    
            seleccionActual = seleccionActual.replace(cell.textContent, "");
            celdasSeleccionadas = celdasSeleccionadas.filter(c => c !== cell);
        } else {
            console.log(`Letra ${cell.textContent} seleccionada`);
    
            //  Solo permitir letras adyacentes
            if (celdasSeleccionadas.length > 0) {
                let ultimaCelda = celdasSeleccionadas[celdasSeleccionadas.length - 1];
                let rowDiff = Math.abs(parseInt(cell.dataset.row) - parseInt(ultimaCelda.dataset.row));
                let colDiff = Math.abs(parseInt(cell.dataset.col) - parseInt(ultimaCelda.dataset.col));
    
                if (rowDiff > 1 || colDiff > 1) {
                    console.log(`No puedes seleccionar ${cell.textContent}, no es adyacente.`);
                    
                    // Efecto de borde rojo parpadeante si la letra no es adyacente
                    let originalBorder = cell.style.border;
                    cell.style.border = "2px solid red";
                    setTimeout(() => {
                        cell.style.border = originalBorder;
                    }, 300);
                    
                    return; // Bloquea la selección si la letra no es adyacente
                }
            }
    
            cell.classList.add("seleccionada");
    
            // Si la letra pertenece a una palabra encontrada, se le pone borde amarillo
            if (cell.classList.contains("palabra-encontrada")) {
                console.log(`Letra ${cell.textContent} ya es parte de una palabra encontrada, cambiando borde a amarillo`);
                cell.style.border = "2px solid yellow";
            }
    
            seleccionActual += cell.textContent;
            celdasSeleccionadas.push(cell);
        }
    
        console.log(`Selección actual: ${seleccionActual}`);
        let seleccionReversa = seleccionActual.split("").reverse().join(""); // Palabra al revés
    
        let palabraEncontrada = palabras.find(p => p === seleccionActual || p === seleccionReversa);
        if (palabraEncontrada) {
            console.log(`¡Palabra encontrada: ${palabraEncontrada}!`);
            mensaje.innerHTML = `<span class="text-success">¡Encontraste: ${palabraEncontrada}!</span>`;
    
            celdasSeleccionadas.forEach((c) => {
                c.classList.add("palabra-encontrada");
                c.style.backgroundColor = "#32CD32"; // Verde para palabras encontradas
                c.style.border = "1px solid #ccc"; // Restaurar borde normal después de selección
                console.log(`Letra ${c.textContent} ahora es parte de una palabra encontrada`);
            });
    
            actualizarListaPalabras(palabraEncontrada);
    
            palabras = palabras.filter(p => p !== palabraEncontrada);
    
            seleccionActual = "";
            celdasSeleccionadas = [];

            if (palabras.length === 0) {
                console.log("🎉 ¡Juego terminado, todas las palabras encontradas!");
                mensaje.innerHTML = `<span class="text-success fw-bold">¡Has encontrado todas las palabras!</span>`;
                juegoTerminado = true;
                sopaContainer.style.opacity = "1"; // Evita que se vea más opaco
                efectoMulticolor(); // Activa el efecto de victoria
            }
            
        }
    }

    // Actualiza la lista de palabras encontradas en la interfaz
    function actualizarListaPalabras(palabra) {
        let items = listaPalabras.querySelectorAll("li");
        items.forEach((item) => {
            let palabraTexto = item.textContent.trim(); // Asegura que no haya espacios extra
            if (palabraTexto === palabra) {
                console.log(`Cambiando fondo de la palabra encontrada: ${palabra}`);
                item.classList.remove("bg-light", "text-dark"); // Elimina estilos previos
                item.classList.add("bg-success", "text-white", "fw-bold"); // Cambia el fondo a verde
            }
        });
    }

    // Effecto de victoria multicolor
    function efectoMulticolor() {
        console.log("🎉 Activando efecto multicolor para la victoria");
    
        const cells = document.querySelectorAll(".sopa-cell");
        let colores = ["#ff0000", "#ff7300", "#ffeb00", "#47ff00", "#00ffcc", "#0033ff", "#8000ff", "#ff00aa"];
    
        let index = 0;
        let intervalo = setInterval(() => {
            cells.forEach((cell) => {
                cell.style.backgroundColor = colores[(index + parseInt(cell.dataset.row) + parseInt(cell.dataset.col)) % colores.length];
            });
            index = (index + 1) % colores.length;
        }, 300);
    
        setTimeout(() => {
            clearInterval(intervalo);
    
            // Restaurar SOLO las celdas que pertenecen a palabras encontradas
            cells.forEach((cell) => {
                if (cell.classList.contains("palabra-encontrada")) {
                    cell.style.backgroundColor = "#32CD32"; // Verde
                } else {
                    cell.style.backgroundColor = ""; // Restaurar normal (vacío)
                }
            });
        }, 5000);
    }
    
    
    // reiniciar el juego
    window.reiniciarJuego = function() {
    console.log(`🔄 Reiniciando juego con grid de tamaño: ${gridSize}x${gridSize}`);
    
    seleccionActual = "";
    celdasSeleccionadas = [];
    mensaje.innerHTML = "";
    palabras = [...palabrasOriginales];
    juegoTerminado = false;
    sopaContainer.style.opacity = "1";
    actualizarTamanoSopa();
    inicializarGrid();
    colocarPalabras();
    completarGrid();
    renderizarGrid();
    renderizarListaPalabras();
}

//descargar el PDF de la sopa de letras
window.descargarPDF = function () {
    const { jsPDF } = window.jspdf;
    const elementoJuego = document.querySelector(".container");

    const titulo = document.querySelector(".nombre-juego");
    const like = document.querySelector(".like-section");

    if (titulo) titulo.style.display = "none";
    if (like) like.style.display = "none";


    // Ocultar botones y mensajes para la captura
    document.querySelectorAll(".btn-primary, .btn-success, .btn-secondary, #mensaje").forEach(el => el.style.display = "none");
    document.getElementById("dificultadContainer").style.display = "none";

    // Ajuste de estilos para asegurar que los cuadros de la sopa se vean correctamente
    document.querySelectorAll(".sopa-cell").forEach(cell => {
        cell.style.border = "1px solid black"; // Asegurar bordes visibles
        cell.style.color = "black"; // Letras en negro
        cell.style.backgroundColor = "white"; // Fondo blanco para evitar colores extra
    });

    html2canvas(elementoJuego, { scale: 2 }).then(canvas => {
        const imgData = canvas.toDataURL("image/png");
        const pdf = new jsPDF("p", "mm", "a4");
        const imgWidth = 210;
        const imgHeight = (canvas.height * imgWidth) / canvas.width;

        // Agregar título
        pdf.setFontSize(20);
        pdf.text(juego.nombre, 10, 20); // Asegura que el título se mantenga

        // Agregar la imagen capturada de la sopa de letras
        pdf.addImage(imgData, "PNG", 0, 30, imgWidth, imgHeight);

        // Restaurar los elementos ocultos
        document.querySelectorAll(".btn-primary, .btn-success, .btn-secondary, #mensaje").forEach(el => el.style.display = "");
        document.getElementById("dificultadContainer").style.display = "";
        if (titulo) titulo.style.display = "";
        if (like) like.style.display = "";


        // Restaurar los colores originales de la sopa de letras
        document.querySelectorAll(".sopa-cell").forEach(cell => {
            cell.style.border = "";
            cell.style.color = "";
            cell.style.backgroundColor = "";
        });

        pdf.save(`${juego.nombre}.pdf`);
    });
};

// Seleccionar dificultad del juego para ajustar el tamaño de la matriz de la sopa de letras
window.seleccionarDificultad = function(dificultad) {
    let nuevoGridSize;

    switch (dificultad) {
        case 'normal':
            nuevoGridSize = 10;
            break;
        case 'dificil':
            nuevoGridSize = 15;
            break;
        default:
            nuevoGridSize = 10;
    }

    gridSize = nuevoGridSize;
    actualizarTamanoSopa();

    reiniciarJuego();
}

// actualizar el tamaño de la sopa de letras
function actualizarTamanoSopa() {
    const sopaDeLetras = document.getElementById("sopaDeLetras");
    
    sopaDeLetras.style.gridTemplateColumns = `repeat(${gridSize}, 40px)`;
    sopaDeLetras.style.gridTemplateRows = `repeat(${gridSize}, 40px)`;
    sopaDeLetras.style.width = `${gridSize * 45}px`;
    sopaDeLetras.style.height = `${gridSize * 40}px`;

    console.log(`📏 Nueva sopa de letras ajustada a ${gridSize}x${gridSize}`);
}


// Renderizar la lista de palabras originales
    function renderizarListaPalabras() {
        listaPalabras.innerHTML = "";
        palabrasOriginales.forEach((palabra) => {
            const li = document.createElement("li");
            li.classList.add("list-group-item", "bg-light", "text-dark", "fw-bold", "m-1");
            li.textContent = palabra;
            listaPalabras.appendChild(li);
        });
    }

    reiniciarJuego();
});

