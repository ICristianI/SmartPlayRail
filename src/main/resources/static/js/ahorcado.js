// Este script permite jugar al ahorcado con una palabra específica, mostrando un teclado virtual y permitiendo al usuario adivinar letras. 

document.addEventListener("DOMContentLoaded", function () {
    const palabra = juego.palabra.toUpperCase();
    let intentosRestantes = parseInt(juego.maxIntentos);
    let letrasAdivinadas = new Set();

    inicializarJuego();

    // Inicializa el juego al cargar la página
    function inicializarJuego() {
        document.getElementById("intentosRestantes").textContent = intentosRestantes;
        actualizarPalabra();
        crearTeclado();
    }

    // Actualiza la visualización de la palabra adivinada
    function actualizarPalabra() {
        const contenedor = document.getElementById("palabraAdivinada");
        contenedor.innerHTML = "";
        palabra.split("").forEach(letra => {
            const span = document.createElement("span");
            span.textContent = letrasAdivinadas.has(letra) || letra === " " ? letra : "_";
            span.classList.add("mx-2", "fs-2", "fw-bold", "border-bottom", "border-3", "px-2");
            contenedor.appendChild(span);
        });

        if (![...palabra].some(letra => !letrasAdivinadas.has(letra) && letra !== " ")) {
            mostrarMensaje("🎉 ¡Ganaste!", "success");
            activarEfectoMulticolor();
        }
    }

    // Crea el teclado virtual
    function crearTeclado() {
        const teclado = document.getElementById("teclado");
        teclado.innerHTML = "";
        
        const letrasFila1 = "QWERTYUIOP".split("");
        const letrasFila2 = "ASDFGHJKL".split("");
        const letrasFila3 = "ZXCVBNM".split("");

        const fila1 = document.createElement("div");
        const fila2 = document.createElement("div");
        const fila3 = document.createElement("div");

        fila1.classList.add("d-flex", "justify-content-center", "mb-2");
        fila2.classList.add("d-flex", "justify-content-center", "mb-2");
        fila3.classList.add("d-flex", "justify-content-center");

        [letrasFila1, letrasFila2, letrasFila3].forEach((fila, index) => {
            fila.forEach(letra => {
                const boton = document.createElement("button");
                boton.textContent = letra;
                boton.classList.add("btn", "btn-outline-dark", "m-1", "px-3", "py-2");
                boton.onclick = () => intentarLetra(letra, boton);
                
                if (index === 0) fila1.appendChild(boton);
                else if (index === 1) fila2.appendChild(boton);
                else fila3.appendChild(boton);
            });
        });

        teclado.appendChild(fila1);
        teclado.appendChild(fila2);
        teclado.appendChild(fila3);
    }

    // Intenta adivinar una letra, actualizando el estado del juego y el teclado
    function intentarLetra(letra, boton) {
        boton.disabled = true;

        if (![...palabra].some(letra => !letrasAdivinadas.has(letra) && letra !== " ")) {
            setTimeout(activarEfectoMulticolor, 100);
        }
        
        if (palabra.includes(letra)) {
            letrasAdivinadas.add(letra);
            actualizarPalabra();
            boton.classList.remove("btn-outline-dark");
            boton.classList.add("btn-success", "text-white");
        } else {
            intentosRestantes--;
            document.getElementById("intentosRestantes").textContent = intentosRestantes;
            boton.classList.remove("btn-outline-dark");
            boton.classList.add("btn-secondary", "text-white")
        }
    
        if (intentosRestantes === 0) {
            mostrarMensaje("Perdiste! La palabra era: " + palabra, "danger");
            document.querySelectorAll("#teclado button").forEach(btn => btn.disabled = true);
        }
    }
    

    // Muestra un mensaje en la pantalla
    function mostrarMensaje(mensaje, tipo) {
        const mensajeDiv = document.getElementById("mensaje");
        mensajeDiv.innerHTML = `<div class="alert alert-${tipo} mt-3">${mensaje}</div>`;
    
        if (tipo === "danger") {
            document.querySelectorAll("#palabraAdivinada span").forEach(span => {
                span.style.color = "red";
            });
    
            document.querySelectorAll("#teclado button").forEach(btn => {
                btn.style.backgroundColor = "red";
                btn.style.color = "white";
            });
        }
    }
    
    // Reinicia el juego, reseteando las letras adivinadas y los intentos restantes
    function reiniciarJuego() {
        letrasAdivinadas.clear();
        intentosRestantes = parseInt(juego.maxIntentos);
        document.getElementById("mensaje").innerHTML = "";
        inicializarJuego();
    }

    // Descarga un PDF con la información del juego
    function descargarPDF() {
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();

        doc.setFontSize(20);
        doc.text(juego.nombre, 10, 20);

        doc.setFontSize(14);
        doc.text(`Descripción: ${juego.descripcion}`, 10, 40);
        doc.text(`Intentos máximos: ${juego.maxIntentos}`, 10, 50);
        doc.text(`Palabra: ${"_ ".repeat(juego.palabra.length)}`, 10, 60);

        const img = new Image();
        img.src = "/images/generalImages/CREARAHORCADO.png";
        img.onload = function () {
            doc.addImage(img, "PNG", 10, 70, 60, 60);
            doc.save(`${juego.nombre}.pdf`);
        };
    }

    window.reiniciarJuego = reiniciarJuego;
    window.descargarPDF = descargarPDF;

    function descargarPDF() {
        const { jsPDF } = window.jspdf;
        const elementoJuego = document.querySelector(".container");
    
        // Ocultar elementos
        document.querySelectorAll(".btn-primary, .btn-success, .btn-secondary").forEach(el => el.style.display = "none");
        const likeSection = document.querySelector(".like-section");
        const intentosRestantes = document.querySelector(".intentos-restantes");
        const nombreJuego = document.querySelector(".nombre-juego");
        
        if (nombreJuego) nombreJuego.style.display = "none";
        if (likeSection) likeSection.style.display = "none";
        if (intentosRestantes) intentosRestantes.style.display = "none";
    
        const mensajeDiv = document.getElementById("mensaje");
        const mensajeOriginal = mensajeDiv.innerHTML;
        mensajeDiv.innerHTML = "";
    
        const imagenJuego = document.getElementById("imagenJuego");
        const clasesOriginales = imagenJuego.className;
        imagenJuego.classList.remove("navbar-custom");
    
        document.querySelectorAll("#palabraAdivinada span").forEach(span => {
            span.classList.add("border-dark");
        });
    
        html2canvas(elementoJuego, { scale: 2 }).then(canvas => {
            const imgData = canvas.toDataURL("image/png");
            const pdf = new jsPDF("p", "mm", "a4");
            const imgWidth = 210;
            const imgHeight = (canvas.height * imgWidth) / canvas.width;
    
            pdf.setFontSize(20);
            pdf.text(juego.nombre, 10, 20);
    
            pdf.addImage(imgData, "PNG", 0, 30, imgWidth, imgHeight);
            pdf.save(`${juego.nombre}.pdf`);
    
            // Restaurar elementos
            document.querySelectorAll(".btn-primary, .btn-success, .btn-secondary").forEach(el => el.style.display = "");
            if (likeSection) likeSection.style.display = "";
            if (intentosRestantes) intentosRestantes.style.display = "";
            if (nombreJuego) nombreJuego.style.display = "";
            imagenJuego.classList.add("navbar-custom");
            mensajeDiv.innerHTML = mensajeOriginal;
    
            document.querySelectorAll("#palabraAdivinada span").forEach(span => {
                span.classList.remove("border-dark");
            });
        });
    }
    
    // Activa un efecto multicolor en las teclas correctas y la palabra adivinada
    function activarEfectoMulticolor() {
        const teclasCorrectas = document.querySelectorAll(".btn-success");
        const palabraSpan = Array.from(document.querySelectorAll("#palabraAdivinada span")).filter(span => span.textContent !== " ");
        
        let colores = ["red", "blue", "green", "yellow", "purple", "orange"];
        let index = 0;
    
        // Desactiva todas las teclas para que no se puedan seguir pulsando
        document.querySelectorAll("#teclado button").forEach(btn => btn.disabled = true);
    
        // Inicia efecto multicolor
        const intervalo = setInterval(() => {
            const color = colores[index % colores.length];
            teclasCorrectas.forEach(btn => btn.style.backgroundColor = color);
            palabraSpan.forEach(span => span.style.color = color);
            index++;
        }, 300);
    
        // Detener efecto y restaurar color verde después de 5 segundos
        setTimeout(() => {
            clearInterval(intervalo);
            teclasCorrectas.forEach(btn => btn.style.backgroundColor = "#198754"); // Bootstrap green
            palabraSpan.forEach(span => span.style.color = "#198754");
        }, 5000);
    }
    
    
    
    
    
    
});


