/*

package com.tfg.SmartPlay.UserTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.entity.User.Rol;
import com.tfg.SmartPlay.controller.UserController;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserController userService;

    @Test
    void testAgregarUsuario() {
        
        // Crear un usuario de prueba
        User user = new User();
        user.setNombre("Test User");
        user.setEdad(25);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRol(Rol.ALUMNO);

        // Guardar el usuario usando el servicio
        User usuarioGuardado = userService.crearUsuario(user);

        // Validar que el usuario fue guardado correctamente
        assertNotNull(usuarioGuardado.getId()); // Verifica que se generó un ID
        assertEquals("test@example.com", usuarioGuardado.getEmail());
        assertEquals(Rol.ALUMNO, usuarioGuardado.getRol());
    
    
    }
}
*/
