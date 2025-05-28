let pasosTutorial = [];
let esPaginaPrincipal = false;
let tutorialActivo = false;

const path = window.location.pathname;

// Recopilación de los enlaces que se van a utilizar en el tutorial.

if (path === "/") {
  esPaginaPrincipal = true;
  pasosTutorial = [
    { id: "card-start", texto: "Desde aquí puedes registrarte o iniciar sesión.", centrar: false },
    { id: "card-creacion", texto: "Desde aquí accedes al panel de creación.", centrar: false },
    { id: "card-grupos", texto: "Aquí puedes gestionar tus grupos.", centrar: false },
    { id: "card-invf", texto: "Explora fichas educativas creadas por otros.", centrar: false },
    { id: "card-invj", texto: "Explora juegos interactivos.", centrar: false }
  ];
} else if (path === "/regin") {
  pasosTutorial = [
    { id: "register-menu", texto: "Aquí puedes registrarte para crear una cuenta y comenzar a usar SmartPlay.", centrar: true },
    { id: "login-menu", texto: "Si ya tienes cuenta, haz clic aquí para iniciar sesión.", centrar: true }
  ];
} else if (path === "/f/investigar") {
  pasosTutorial = [
    { id: "buscar-fichas", texto: "Usa este campo para buscar fichas por su nombre.", centrar: true },
    { id: "orden-fecha", texto: "Haz clic para ver primero las fichas más recientes.", centrar: true },
    { id: "orden-popularidad", texto: "Haz clic para ordenar por fichas más populares.", centrar: true },
    { id: "zona-fichas", texto: "Aquí aparecerán las fichas disponibles para explorar.", centrar: false }
  ];
} else if (path.startsWith("/juegos/investigar")) {
  pasosTutorial = [
    { id: "busqueda-juegos", texto: "Puedes buscar juegos por su nombre aquí.", centrar: true },
    { id: "orden-fecha-juegos", texto: "Haz clic para ver primero los juegos más recientes.", centrar: true },
    { id: "orden-popularidad-juegos", texto: "O bien, ordena por los más populares.", centrar: true },
    { id: "contenedor-juegos", texto: "Aquí aparecerán los juegos disponibles para jugar.", centrar: false }
  ];
} else if (path.startsWith("/signup")) {
  pasosTutorial = [
    { id: "registro-formulario", texto: "Completa este formulario para registrarte en SmartPlay.", centrar: true },
    { id: "registro-login", texto: "Si ya tienes cuenta, haz clic aquí para iniciar sesión.", centrar: true }
  ];
} else if (path.startsWith("/login")) {
  pasosTutorial = [
    { id: "login-formulario", texto: "Introduce tu correo y contraseña para acceder a SmartPlay.", centrar: true },
    { id: "login-registro", texto: "¿Aún no tienes cuenta? Haz clic aquí para registrarte.", centrar: true }
  ];
} else if (path.startsWith("/verify")) {
  pasosTutorial = [
    { id: "verificacion-link", texto: "Si no has podido verificar tu usuario entra en tu correo y revisa la parte de SPAM. Si ya verificaste tu cuenta en el correo, haz clic aquí para iniciar sesión.", centrar: true }
  ];
} else if (path === "/creacion") {
  pasosTutorial = [
    { id: "panel-fichas", texto: "Aquí puedes gestionar y crear nuevas fichas educativas.", centrar: true },
    { id: "panel-juegos", texto: "Desde aquí accedes al panel de gestión y creación de juegos.", centrar: true },
    { id: "panel-cuadernos", texto: "Y aquí puedes organizar fichas y juegos dentro de cuadernos.", centrar: true }
  ];
} else if (path.startsWith("/users/perfil")) {
  pasosTutorial = [
    { id: "perfil-usuario", texto: "Aquí puedes ver tu información personal.", centrar: true },
    { id: "profilePhotoForm", texto: "Haz clic en tu imagen para subir una nueva foto de perfil. Máximo 5 Mb", centrar: true },
    { id: "editFormButton", texto: "Aquí puedes editar tu nombre y edad. Recuerda hacer clic en 'Guardar'.", centrar: true },
    { id: "eliminar-cuenta", texto: "Haz clic aquí si deseas eliminar tu cuenta.", centrar: true },
    { id: "logout-form-tutorial", texto: "Este botón te permite cerrar sesión.", centrar: true }
  ];
} else if (path === "/juegos") {
  pasosTutorial = [
    { id: "card-ahorcado", texto: "Haz clic aquí para crear un juego del tipo Ahorcado.", centrar: true },
    { id: "card-sopa", texto: "Selecciona esta opción si quieres diseñar una sopa de letras.", centrar: true },
    { id: "card-crucigrama", texto: "Y aquí puedes crear un crucigrama personalizado.", centrar: true }
  ];
} else if (path.startsWith("/sopaletras/listar")) {
  pasosTutorial = [
    { id: "titulo-sopa", texto: "Esta es tu lista de juegos de sopa de letras creados.", centrar: true },
    { id: "zona-juegos-sopa", texto: "Aquí puedes ver tus juegos existentes y entrar a visualizarlos.", centrar: true },
    { id: "crear-sopa", texto: "Si no has creado ninguno, haz clic aquí para crear uno nuevo.", centrar: true }
  ];
} else if (path === "/sopa") {
  pasosTutorial = [
    { id: "card-crear-sopa", texto: "Haz clic aquí para crear un nuevo juego de sopa de letras.", centrar: true },
    { id: "card-ver-sopa", texto: "Haz clic aquí para ver los juegos que ya has creado.", centrar: true }
  ];
} else if (path === "/sopaletras/crear") {
  pasosTutorial = [
    { id: "crear-sopa-container", texto: "Aquí puedes crear tu propio juego de sopa de letras educativa.", centrar: false },
    { id: "imagen-juego-sopa", texto: "Puedes añadir una imagen representativa para tu juego (opcional).", centrar: true },
    { id: "lista-palabras-sopa", texto: "Aquí se mostrarán las palabras que agregues a la sopa de letras.", centrar: true }
  ];
} else if (path.startsWith("/sopaletras/ver")) {
  pasosTutorial = [
    { id: "Contenido", texto: "Este es tu juego aquí se mostrarán tus datos relevantes como nombre, likes, palabras", centrar: false },
    { id: "Privacidad", texto: "La privacidad del juego determina si todos podrán verla o solo tu", centrar: true },
    { id: "editFormButton", texto: "Este es el formulario para editar el juego. Haz clic en 'Editar Juego' para mostrarlo.", centrar: true },
    { id: "btnEliminarJuego", texto: "Este botón elimina el juego permanentemente. ¡Úsalo con cuidado!", centrar: true },
    { id: "btnJugarJuego", texto: "Haz clic aquí para comenzar a jugar este juego.", centrar: true },
    { id: "btnVolverListado", texto: "Este botón te lleva de vuelta al listado de juegos de sopa de letras.", centrar: true },
    { id: "contenedorCuadernos", texto: "Aquí puedes ver a qué cuadernos pertenece este juego. Puedes navegar y entrar a ellos.", centrar: true }
  ];
} else if (path.startsWith("/sopaletras/jugar")) {
  pasosTutorial = [
    { id: "sopaLetrasTutorial", texto: "Aquí aparece la sopa de letras. Busca las palabras ocultas horizontal, vertical o diagonalmente. Recuerda! Solo podrás presionar las casillas adyacentes a la primera que selecciones, además las palabras se pueden superponer, Mucha Suerte!", centrar: false },
    { id: "likeButton", texto: "Haz clic en el corazón para indicar que te gusta este juego. Solo puedes darle me gusta una vez.", centrar: false },
    { id: "listaPalabras", texto: "Estas son las palabras que debes encontrar en la sopa.", centrar: true },
    { id: "dificultadTutorial", texto: "Puedes cambiar la dificultad del juego aquí. En difícil habrá más espacio para las palabras.", centrar: true },
    { id: "btnReiniciarJuego", texto: "Si quieres empezar de nuevo, haz clic aquí para reiniciar la sopa. Cuidado! la disposición de la sopa cambiará cuando le des", centrar: true },
    { id: "btnDescargarPDF", texto: "Haz clic aquí para descargar la sopa de letras como un PDF e imprimirla.", centrar: true },
    { id: "btnVolverJuego", texto: "Cuando termines, haz clic aquí para volver a la vista anterior.", centrar: true }
  ];
}else if (path === "/crucigrama") {
  pasosTutorial = [
    { id: "card-crear-crucigrama", texto: "Haz clic aquí para crear un nuevo juego de crucigrama personalizado.", centrar: true },
    { id: "card-ver-crucigrama", texto: "Haz clic aquí para ver todos los crucigramas que ya has creado.", centrar: true }
  ];
}else if (path === "/crucigrama/crear") {
  pasosTutorial = [
    { id: "crear-crucigrama-container", texto: "Aquí puedes crear tu propio juego de crucigrama educativo.", centrar: false },
    { id: "imagen-juego-crucigrama", texto: "Puedes añadir una imagen representativa para tu crucigrama (opcional).", centrar: true },
    { id: "lista-pistas-crucigrama", texto: "Aquí aparecerán las pistas y respuestas que vayas agregando al crucigrama.", centrar: true }
  ];
}else if (path.startsWith("/crucigrama/listar")) {
  pasosTutorial = [
    { id: "titulo-crucigrama", texto: "Esta es tu lista de juegos de crucigrama creados.", centrar: false },
    { id: "zona-juegos-crucigrama", texto: "Aquí puedes ver tus juegos existentes y acceder a verlos o editarlos.", centrar: false },
    { id: "crear-crucigrama", texto: "Si no has creado ninguno, haz clic aquí para crear uno nuevo.", centrar: true }
  ];
}else if (path.startsWith("/crucigrama/ver")) {
  pasosTutorial = [
    { id: "Contenido", texto: "Aquí puedes ver la descripción, asignatura e información general del crucigrama.", centrar: false },
    { id: "Privacidad", texto: "La privacidad del juego determina si todos podrán verlo o solo tú.", centrar: true },
    { id: "editFormTutorial", texto: "Este es el formulario para editar el juego. Haz clic en 'Editar Juego' para mostrarlo.", centrar: true },
    { id: "btnEliminarJuego", texto: "Este botón elimina el juego permanentemente. ¡Úsalo con cuidado!", centrar: true },
    { id: "btnJugarJuego", texto: "Haz clic aquí para comenzar a jugar este crucigrama como alumno.", centrar: true },
    { id: "btnVolverListado", texto: "Este botón te lleva de vuelta al listado de crucigramas que has creado.", centrar: true },
    { id: "contenedorCuadernos", texto: "Aquí se muestran los cuadernos a los que pertenece este juego. Puedes navegar hacia ellos.", centrar: true }
  ];
}else if (path.startsWith("/crucigrama/jugar")) {
  pasosTutorial = [
    { id: "crucigramaTutorial", texto: "Aquí aparece tu crucigrama. Las casillas se completarán cuando escribas las respuestas correctas.", centrar: false },
    { id: "likeButtonTutorial", texto: "Haz clic en el corazón para indicar que te gusta este juego. Solo puedes darle me gusta una vez.", centrar: false },
    { id: "pistasTutorial", texto: "Estas son las pistas para completar el crucigrama. Están enumeradas para ayudarte a ubicar cada palabra, al lado de ellas podrás escribir tu respuesta.", centrar: true },
    { id: "btnVerificar", texto: "Cuando termines, haz clic aquí para verificar si tus respuestas son correctas.", centrar: true },
    { id: "btnReiniciarJuego", texto: "Puedes reiniciar el crucigrama. ¡Ojo! se limpiarán todas las respuestas.", centrar: true },
    { id: "btnDescargarPDF", texto: "Haz clic aquí para descargar el crucigrama como PDF.", centrar: true },
    { id: "btnVolverJuego", texto: "Haz clic aquí para volver a la vista anterior.", centrar: true }
  ];
}else if (path === "/ahorcado") {
  pasosTutorial = [
    { id: "card-crear-ahorcado", texto: "Haz clic aquí para crear un nuevo juego de Ahorcado personalizado.", centrar: true },
    { id: "card-ver-ahorcado", texto: "Haz clic aquí para ver todos los juegos de Ahorcado que ya has creado.", centrar: true }
  ];
}else if (path === "/ahorcado/crear") {
  pasosTutorial = [
    { id: "crear-ahorcado-container", texto: "Aquí puedes crear tu propio juego del Ahorcado educativo.", centrar: false },
    { id: "imagen-juego-ahorcado", texto: "Puedes añadir una imagen representativa para tu juego (opcional).", centrar: true },
    { id: "palabra-secreta", texto: "Escribe la palabra que el jugador tendrá que adivinar. Máximo 15 letras, solo letras.", centrar: true },
    { id: "input-intentos", texto: "Aquí puedes definir cuántos intentos tiene el jugador antes de perder.", centrar: true }
  ];
}else if (path.startsWith("/ahorcado/listar")) {
  pasosTutorial = [
    { id: "titulo-ahorcado", texto: "Esta es tu lista de juegos de Ahorcado creados.", centrar: false },
    { id: "zona-juegos-ahorcado", texto: "Aquí puedes ver todos tus juegos y acceder a cada uno de ellos para editar o jugar.", centrar: false },
    { id: "crear-ahorcado", texto: "Si no has creado ninguno, haz clic aquí para crear uno nuevo.", centrar: true }
  ];
}else if (path.startsWith("/ahorcado/ver")) {
  pasosTutorial = [
    { id: "Contenido", texto: "Aquí se muestra la descripción y datos principales del juego de Ahorcado.", centrar: false },
    { id: "Privacidad", texto: "Esta etiqueta indica si el juego es privado (solo tú lo ves) o público.", centrar: true },
    { id: "editFormTutorial", texto: "Este es el formulario para editar el juego. Haz clic en 'Editar Juego' para mostrarlo.", centrar: true },
    { id: "btnEliminarJuego", texto: "Este botón elimina el juego permanentemente. ¡Ten cuidado al usarlo!", centrar: true },
    { id: "btnJugarJuego", texto: "Haz clic aquí para jugar el juego y adivinar la palabra secreta.", centrar: true },
    { id: "btnVolverListado", texto: "Este botón te devuelve a la lista de todos tus juegos de Ahorcado.", centrar: true },
    { id: "contenedorCuadernos", texto: "Aquí se muestran los cuadernos en los que este juego está incluido.", centrar: true }
  ];
} else if (path.startsWith("/ahorcado/jugar")) {
  pasosTutorial = [
    { id: "ahorcadoTutorial", texto: "Aquí podrás jugar al juego del ahorcado.", centrar: false },
    { id: "likeButtonTutorial", texto: "Haz clic en el corazón para dar me gusta a este juego. Solo puedes hacerlo una vez.", centrar: false },
    { id: "intentosAhorcadoTutorial", texto: "Aquí se muestran los intentos que te quedan antes de perder.", centrar: true },
    { id: "palabraAdivinadaTutorial", texto: "Aquí se muestra la palabra que debes adivinar. Se irá revelando con cada acierto.", centrar: true },
    { id: "tecladoAhorcado", texto: "Usa este teclado para adivinar letras. ¡Cuidado con los errores!", centrar: true },
    { id: "btnReiniciarAhorcado", texto: "Haz clic aquí para reiniciar el juego. Volverás a intentarlo desde cero.", centrar: true },
    { id: "btnDescargarAhorcadoPDF", texto: "Puedes descargar este juego en PDF para imprimirlo o guardarlo.", centrar: true },
    { id: "btnVolverAhorcado", texto: "Haz clic aquí para volver a la página anterior.", centrar: true }
  ];
}else if (path === "/grupo") {
  pasosTutorial = [
    { id: "card-crear-grupo", texto: "Haz clic aquí para crear un nuevo grupo donde podrás gestionar alumnos, tareas y actividades.", centrar: true },
    { id: "card-unirse-grupo", texto: "Si tienes un código de grupo, puedes unirte a él desde aquí.", centrar: true },
    { id: "card-ver-grupo", texto: "Desde aquí puedes acceder y gestionar los grupos a los que perteneces.", centrar: true }
  ];
}else if (path === "/grupos/crear") {
  pasosTutorial = [
    { id: "crear-grupo", texto: "Desde este formulario podrás crear tus grupos, estas serán como las clases donde estarán tus cuadernos y tus alumnos", centrar: false },
    { id: "btn-modal-cuadernos", texto: "Haz clic aquí para seleccionar los cuadernos que quieres incluir en este grupo.", centrar: false },
    { id: "btn-crear-grupo", texto: "Haz clic aquí para crear tu nuevo grupo con los datos proporcionados.", centrar: false }
  ];
} else if (path === ("/grupos/unirse")) {
  pasosTutorial = [
    { id: "titulo-unirse-grupo", texto: "Aquí puedes unirte a un grupo introduciendo un código de acceso de un grupo de un profesor.", centrar: true },
    { id: "campo-codigo", texto: "Ingresa aquí el código de acceso que te han proporcionado.", centrar: true },
    { id: "btn-unirse-grupo", texto: "Haz clic para unirte al grupo con el código ingresado.", centrar: true },
    { id: "btn-volver-unirse", texto: "Puedes volver a la vista anterior si prefieres no unirte por ahora.", centrar: true }
  ];
} else if (path === ("/grupos")) {
  pasosTutorial = [
    { id: "titulo-ver-grupos", texto: "Esta es tu lista de grupos. Aquí puedes ver los grupos a los que perteneces.", centrar: true },
    { id: "zona-grupos", texto: "Aquí se muestran tus grupos. Puedes entrar a ver sus detalles.", centrar: false },
    { id: "crear-grupo-ver", texto: "Haz clic aquí para crear un grupo nuevo si no tienes ninguno.", centrar: true }
  ];
} else if (path === ("/grupos/ver")) {
  pasosTutorial = [
    { id: "titulo-grupo", texto: "Aquí puedes ver la información del grupo al que perteneces.", centrar: false },
    { id: "codigoGrupoTutorial", texto: "Este es el código que otros usuarios pueden usar para unirse a tu grupo. Puedes copiarlo con el botón.", centrar: true },
    { id: "editFormTutorial", texto: "Este es el botón para editar el nombre, descripción o cuadernos del grupo.", centrar: true },
    { id: "eliminarTutorial", texto: "Con este otro botón podrás eliminar el grupo", centrar: true },
    { id: "contenedorCuadernosTutorial", texto: "Aquí aparecen los cuadernos que forman parte del grupo. Puedes verlos, resolverlos o eliminarlos si eres el propietario.", centrar: true },
    { id: "contenedorUsuariosTutorial", texto: "Estos son los usuarios del grupo. Si eres el propietario, puedes ver su perfil o eliminarlos.", centrar: true },
  ];
}else if (path === "/fichas") {
  pasosTutorial = [
    { id: "crearFichasCard", texto: "Haz clic aquí para crear una ficha interactiva nueva.", centrar: true },
    { id: "verFichasCard", texto: "Aquí puedes ver, editar o eliminar las fichas que ya has creado.", centrar: true }
  ];
} else if (path === "/f/crearFichas") {
  pasosTutorial = [
    { id: "ContenidoTutorial", texto: "Este es el panel de creación de fichas.", centrar: true },
    { id: "nombreFichaTutorial", texto: "Aquí introduces el nombre de tu ficha interactiva.", centrar: true },
    { id: "idiomaFichaTutorial", texto: "Especifica el idioma de la ficha, como Español o Inglés.", centrar: true },
    { id: "asignaturaFichaTutorial", texto: "Indica la asignatura a la que pertenece la ficha.", centrar: true },
    { id: "contenidoFichaTutorial", texto: "Describe el contenido que trabajará la ficha.", centrar: true },
    { id: "descripcionFichaTutorial", texto: "Aquí puedes añadir una breve descripción general.", centrar: true },
    { id: "imagenFichaTutorial", texto: "Sube una imagen que servirá como fondo de la ficha. Recuerda que no puedes subir imágenes de más de 5 Mb", centrar: true },
    { id: "privacidadFichaTutorial", texto: "Marca esto si deseas que la ficha sea privada (solo tú la verás).", centrar: true },
    { id: "guardarFichaTutorial", texto: "Cuando hayas completado todo, haz clic aquí para guardar la ficha.", centrar: true }
  ];
} else if (path === "/f/modificarFicha") {
  pasosTutorial = [
    { id: "imagenFichaTutorial", texto: "Esta es la imagen base sobre la que se construye la ficha interactiva.", centrar: false },
    { id: "botonesElementosTutorial", texto: "Desde aquí puedes añadir distintos tipos de elementos interactivos. Pasa el cursor por el icono para conocer su función.", centrar: false },
    { id: "guardarCambiosFichaTutorial", texto: "Una vez termines de editar, pulsa aquí para guardar los cambios realizados.", centrar: true }
  ];
} else if (path === "/f/listarFichas") {
  pasosTutorial = [
    { id: "titulo-mis-fichas", texto: "Esta es tu lista de fichas interactivas. Aquí puedes ver todas las que has creado.", centrar: true },
    { id: "zona-fichas", texto: "Aquí se muestran tus fichas. Puedes verlas, editarlas o eliminarlas.", centrar: false },
    { id: "crear-ficha-nueva", texto: "Haz clic aquí para crear una ficha nueva si aún no tienes ninguna.", centrar: true }
  ];
} else if (path === "/f/verFichaInteractiva") {
  pasosTutorial = [
    { id: "contenedorFichaTutorial", texto: "En esta pestaña verás la ficha a resolver. Puedes escribir, seleccionar o emparejar según el tipo.", centrar: false },
    { id: "likeFormTutorial", texto: "Puedes dar me gusta a la ficha y ver cuántos usuarios lo han hecho.", centrar: false },
    { id: "btnCorregir", texto: "Haz clic en este botón para corregir la ficha y ver tu puntuación.", centrar: true },
    { id: "btnReiniciarFicha", texto: "Si quieres intentarlo de nuevo, puedes reiniciar la ficha desde aquí.", centrar: true },
    { id: "btnDescargarPDF", texto: "Haz clic aquí si deseas descargar la ficha con tus respuestas en formato PDF.", centrar: true },
    { id: "btnVolverFicha", texto: "Haz clic aquí para volver a la vista anterior.", centrar: true },
    { id: "btnVerFicha", texto: "Haz clic aquí para ver la ficha interactiva.", centrar: true }
  ];
} else if (path.startsWith("/f/verFicha")) {
  pasosTutorial = [
    { id: "Contenido", texto: "Aquí puedes ver toda la información principal de tu ficha: nombre, idioma, asignatura, descripción y contenido.", centrar: false },
    { id: "Privacidad", texto: "Esta etiqueta indica si la ficha es pública o privada. Puedes cambiarlo desde el formulario de edición.", centrar: true },
    { id: "EditForm", texto: "Haz clic aquí para editar los datos de la ficha. El botón se transforma en 'Cancelar Edición' mientras esté abierto.", centrar: true },
    { id: "anadirElementos", texto: "Desde aquí puedes añadir elementos interactivos a la ficha. Haz clic en el icono para ver su función.", centrar: true },
    { id: "resolverFicha", texto: "Haz clic aquí para resolver la ficha interactiva.", centrar: true },
    { id: "btnEliminarFicha", texto: "Este botón elimina la ficha de forma permanente. Úsalo solo si estás seguro.", centrar: true },
    { id: "btnVolverListado", texto: "Haz clic aquí para volver a tu listado de fichas interactivas.", centrar: true },
    { id: "contenedorCuadernos", texto: "Aquí puedes ver los cuadernos a los que pertenece esta ficha. Puedes acceder a ellos desde este mismo panel.", centrar: true }
  ];
} else if (path === "/Cuadernos") {
  pasosTutorial = [
    { id: "card-crear-cuaderno", texto: "Haz clic en esta tarjeta para crear un nuevo cuaderno y comenzar a organizar tus fichas y juegos.", centrar: true },
    { id: "card-ver-cuadernos", texto: "Haz clic aquí para ver los cuadernos que ya has creado, acceder a ellos o editarlos.", centrar: true }
  ];
} else if (path === "/cuadernos/crear") {
  pasosTutorial = [
    { id: "crear-cuaderno", texto: "Desde este formulario podrás crear tus cuadernos, estos te permitirán guardar tus fichas y juegos", centrar: false },
    { id: "imagen-cuaderno", texto: "Puedes añadir una imagen representativa para tu cuaderno (opcional).", centrar: true },
    { id: "btn-modal-fichas", texto: "Haz clic aquí para seleccionar las fichas que quieras incluir en este cuaderno.", centrar: true },
    { id: "btn-modal-juegos", texto: "Lo mismo pero para seleccionar las juegos que quieras incluir en este cuaderno.", centrar: true },
    { id: "btn-crear-cuaderno", texto: "Haz clic aquí para crear tu nuevo cuaderno con los datos proporcionados.", centrar: true }
  ];
} else if (path === "/cuadernos") {
  pasosTutorial = [
    { id: "titulo-mis-cuadernos", texto: "Esta es tu lista de cuadernos. Aquí puedes ver todos los que has creado.", centrar: false },
    { id: "zona-cuadernos", texto: "Aquí se muestran tus cuadernos. Puedes ver su contenido haciendo clic en 'Ver Cuaderno'.", centrar: false },
    { id: "crear-cuaderno-nuevo", texto: "Haz clic aquí para crear un nuevo cuaderno si aún no tienes ninguno.", centrar: true }
  ];
} else if (path === "/cuadernos/ver") {
  pasosTutorial = [
    { id: "titulo-grupo", texto: "Aquí puedes ver la información del cuaderno, como su nombre y propietario.", centrar: false },
    { id: "editFormTutorial", texto: "Haz clic en este botón para editar el nombre, fichas o juegos de este cuaderno.", centrar: false },
    { id: "eliminarTutorial", texto: "Con este botón puedes eliminar el cuaderno permanentemente. ¡Cuidado!", centrar: true },
    { id: "contenedorFichas", texto: "Aquí se muestran las fichas que pertenecen al cuaderno. Puedes verlas o eliminarlas.", centrar: true },
    { id: "contenedorJuegos", texto: "Aquí se muestran los juegos agregados a este cuaderno. También puedes verlos o eliminarlos.", centrar: true }
  ];
} else if (path === "/config") {
  pasosTutorial = [
    { id: "config-menu", texto: "Esta es la sección de configuración general de SmartPlay.", centrar: false },
    { id: "colorbtns-container", texto: "Haz clic en cualquiera de estos círculos de color para cambiar la apariencia de la plataforma.", centrar: true },
    { id: "contacto-info", texto: "¿Tienes dudas o sugerencias? Aquí puedes ver el correo de contacto.", centrar: true }
  ];
}

let pasoActual = 0;

// Al activar el botón de inicio del tutorial se quita el scroll de la página
function iniciarTutorial() {
  if (pasosTutorial.length === 0) {
    alert("Esta página no tiene soporte para un tutorial.");
    return;
  }
  pasoActual = 0;
  tutorialActivo = true; // ACTIVAR
  document.getElementById("tutorial-overlay").style.display = "block";
  document.body.style.overflow = "hidden";
  mostrarPaso(pasoActual);
}

// Función para mostrar el paso actual del tutorial
function mostrarPaso(indice) {
  const paso = pasosTutorial[indice];
  if (!paso) return finalizarTutorial();

  const elemento = document.getElementById(paso.id);

  if (!elemento) return avanzarPaso();

  const rect = elemento.getBoundingClientRect();
  const highlight = document.getElementById("tutorial-highlight");
  const tooltip = document.getElementById("tutorial-tooltip");

  // Scroll condicional según el flag "centrar", a veces se tiene que mostrar algo en el fondo y se necesita scrollear hasta el punto correcto
  if (paso.centrar) {
    window.scrollTo({
      top: rect.top + window.scrollY - window.innerHeight / 2 + rect.height / 2,
      behavior: "smooth"
    });
  }

  // Posicionamiento del cuadro de resaltado
  highlight.style.position = "fixed";
  highlight.style.top = `${rect.top - 10}px`;
  highlight.style.left = `${rect.left - 10}px`;
  highlight.style.width = `${rect.width + 20}px`;
  highlight.style.height = `${rect.height + 20}px`;
  highlight.style.border = "3px solid yellow";
  highlight.style.borderRadius = "10px";
  highlight.style.zIndex = "9999";

  // Tooltip
  const espacioAbajo = window.innerHeight - rect.bottom;
  const espacioArriba = rect.top;
  const mostrarArriba = espacioAbajo < 100 && espacioArriba > 100;

  const tooltipTop = esPaginaPrincipal
    ? rect.top
    : (mostrarArriba ? rect.top - 70 : rect.bottom + 10);

  const tooltipLeft = esPaginaPrincipal
    ? rect.right + 20
    : rect.left;

  tooltip.style.position = "fixed";
  tooltip.style.top = `${tooltipTop}px`;
  tooltip.style.left = `${tooltipLeft}px`;
  tooltip.style.backgroundColor = "white";
  tooltip.style.padding = "10px";
  tooltip.style.borderRadius = "5px";
  tooltip.style.zIndex = "10000";
  tooltip.style.maxWidth = "300px";
  tooltip.style.boxShadow = "0 0 10px rgba(0,0,0,0.5)";
  tooltip.textContent = paso.texto;
}

// Avanzar al siguiente paso del tutorial
function avanzarPaso() {
  pasoActual++;
  if (pasoActual >= pasosTutorial.length) {
    finalizarTutorial();
  } else {
    mostrarPaso(pasoActual);
  }
}

// Finalizar el tutorial y limpiar el estado

function finalizarTutorial() {
  tutorialActivo = false; // DESACTIVAR
  document.body.style.overflow = "";
  document.getElementById("tutorial-overlay").style.display = "none";
}


document.getElementById("tutorial-overlay").addEventListener("click", (e) => {
  if (!document.getElementById("tutorial-tooltip").contains(e.target)) {
    avanzarPaso();
  }
});

window.addEventListener("scroll", () => {
  if (tutorialActivo) mostrarPaso(pasoActual);
});
window.addEventListener("resize", () => {
  if (tutorialActivo) mostrarPaso(pasoActual);
});

