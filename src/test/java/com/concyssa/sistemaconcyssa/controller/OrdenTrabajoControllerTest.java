package com.concyssa.sistemaconcyssa.controller;

import com.concyssa.sistemaconcyssa.security.JwtProvider;
import com.concyssa.sistemaconcyssa.service.OrdenTrabajoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrdenTrabajoController.class)
class OrdenTrabajoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenTrabajoService ordenTrabajoService;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void listarOrdenes_DeberiaRetornarStatus200() throws Exception {
        mockMvc.perform(get("/api/ordenes"))
                .andExpect(status().isOk());
    }
}