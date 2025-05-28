document.addEventListener("DOMContentLoaded", function() {
    const palabraInput = document.getElementById("palabra");

    // Bloquear caracteres especiales en tiempo real
    palabraInput.addEventListener("input", function() {
        this.value = this.value.replace(/[^A-Za-z]/g, ""); // Permite solo letras
    });

    palabraInput.addEventListener("keydown", function(event) {
        if (!event.key.match(/^[A-Za-z]$/) && 
            event.key !== "Backspace" && 
            event.key !== "Tab" && 
            event.key !== "Enter") {
            event.preventDefault(); // Bloquea caracteres inválidos
        }
    });
});

// Función para cambiar el número máximo de intentos
function cambiarIntentos(cambio) {
    let input = document.getElementById("maxIntentos");
    let valorActual = parseInt(input.value);
    let nuevoValor = valorActual + cambio;

    if (nuevoValor >= 1 && nuevoValor <= 15) {
        input.value = nuevoValor;
    }
}
