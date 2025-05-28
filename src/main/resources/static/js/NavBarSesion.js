// Permite cambiar el estado de los enlaces de registro, inicio de sesión y cierre de sesión
// según el estado de autenticación del usuario

document.addEventListener("DOMContentLoaded", () => {
  const urlParams = new URLSearchParams(window.location.search);
  if (urlParams.get("loggedIn") === "true") {
    const authToken = document.cookie
      .split("; ")
      .find((cookie) => cookie.startsWith("authToken="))
      ?.split("=")[1];

    const registerLink = document.getElementById("register");
    const loginLink = document.getElementById("login");
    const logoutLink = document.getElementById("log-out");

    if (authToken) {
      registerLink.style.display = "none";
      loginLink.style.display = "none";
      logoutLink.style.display = "block";
    } else {
      registerLink.style.display = "block";
      loginLink.style.display = "block";
      logoutLink.style.display = "none";
    }
  }
});
