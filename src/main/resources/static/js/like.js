//Personalización del botón de "Me gusta" para juegos y fichas y funcionalidad (llamando a la API correspondiente)

function darMeGusta() {
  const csrfToken = document.querySelector('input[name="_csrf"]').value;
  const corazon = document.getElementById("corazon");
  const contador = document.getElementById("contadorLikes");

  // Detectar si es juego o ficha
  const juegoIdInput = document.getElementById("juegoId");
  const fichaIdInput = document.getElementById("fichaId");

  let url = "";
  let id = "";

  if (juegoIdInput) {
    id = juegoIdInput.value;
    url = "/juegos/like";
  } else if (fichaIdInput) {
    id = fichaIdInput.value;
    url = "/f/like";
  } else {
    console.error("No se encontró ni juegoId ni fichaId.");
    return;
  }

  fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
      "X-CSRF-TOKEN": csrfToken,
    },
    body: `${juegoIdInput ? "juegoId" : "fichaId"}=${id}`,
  })
    .then((response) => {
      if (!response.ok) throw new Error("Error al dar me gusta");
      return response.text();
    })
    .then(() => {
      let total = parseInt(contador.innerText);
      let liked = corazon.getAttribute("data-liked") === "true";

      if (liked) {
        corazon.innerText = "🤍";
        corazon.setAttribute("data-liked", "false");
        contador.innerText = total - 1;
      } else {
        corazon.innerText = "❤️";
        corazon.setAttribute("data-liked", "true");
        contador.innerText = total + 1;

        corazon.classList.add("like-animation");
        setTimeout(() => corazon.classList.remove("like-animation"), 300);
      }
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}
