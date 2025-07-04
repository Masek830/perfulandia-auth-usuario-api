package com.perfulandia.perfulandia_auth_usuario_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.gestion.dto.CrearUsuarioRequest;
import com.gestion.dto.UsuarioDTO;
import com.gestion.models.Usuario;
import com.gestion.services.UsuarioService;
import com.gestion.controllers.UsuarioController;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.perfulandia.perfulandia_auth_usuario_api.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService service;

    @InjectMocks
    private UsuarioController controller;

    private UsuarioDTO usuarioDTO;
    private Usuario usuario;
    private CrearUsuarioRequest crearUsuarioRequest;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(1);
        usuarioDTO.setNombreUsuario("Juan Pérez");
        usuarioDTO.setEmail("juan@correo.com");

        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNombreUsuario("Juan Pérez");
        usuario.setEmail("juan@correo.com");

        crearUsuarioRequest = new CrearUsuarioRequest();
        crearUsuarioRequest.setNombreUsuario("Juan Pérez");
        crearUsuarioRequest.setEmail("juan@correo.com");
        crearUsuarioRequest.setContrasena("secreta123");
    }

    @Test
    void testGetAll() {
        when(service.listarUsuarios()).thenReturn(Arrays.asList(usuarioDTO));

        List<UsuarioDTO> usuarios = controller.getAll();

        assertEquals(1, usuarios.size());
        assertEquals("Juan Pérez", usuarios.get(0).getNombreUsuario());
        verify(service).listarUsuarios();
    }

    @Test
    void testGetById_existente() {
        when(service.buscarUsuarioPorId(1)).thenReturn(usuarioDTO);

        ResponseEntity<?> response = controller.getById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioDTO, response.getBody());
        verify(service).buscarUsuarioPorId(1);
    }

    @Test
    void testGetById_noExistente() {
        when(service.buscarUsuarioPorId(2)).thenThrow(new RuntimeException("Usuario no encontrado"));

        ResponseEntity<?> response = controller.getById(2);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((Map<?, ?>) response.getBody()).get("mensaje").toString().contains("Usuario no encontrado"));
        verify(service).buscarUsuarioPorId(2);
    }

    @Test
    void testCrear() {
        when(service.crearUsuario(crearUsuarioRequest)).thenReturn(usuario);

        ResponseEntity<Usuario> response = controller.crear(crearUsuarioRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(usuario, response.getBody());
        verify(service).crearUsuario(crearUsuarioRequest);
    }

    @Test
    void testEditarUsuario() {
        when(service.actualizarUsuario(1, usuarioDTO)).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = controller.editarUsuario(1, usuarioDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioDTO, response.getBody());
        verify(service).actualizarUsuario(1, usuarioDTO);
    }

    @Test
    void testEliminarUsuario() {
        doNothing().when(service).eliminarUsuario(1);

        ResponseEntity<Void> response = controller.eliminar(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).eliminarUsuario(1);
    }
}
