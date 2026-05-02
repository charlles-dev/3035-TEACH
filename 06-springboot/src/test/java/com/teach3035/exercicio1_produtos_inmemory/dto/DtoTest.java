package com.teach3035.exercicio1_produtos_inmemory.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void produtoRequestDTO_deveConterCampos() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO("Nome", new BigDecimal("10.00"));
        
        assertEquals("Nome", dto.getNome());
        assertEquals(new BigDecimal("10.00"), dto.getPreco());
    }

    @Test
    void produtoResponseDTO_deveConterCampos() {
        ProdutoResponseDTO dto = new ProdutoResponseDTO(1L, "Nome", new BigDecimal("10.00"));
        
        assertEquals(1L, dto.getId());
        assertEquals("Nome", dto.getNome());
        assertEquals(new BigDecimal("10.00"), dto.getPreco());
    }

    @Test
    void produtoPatchDTO_devePermitirNulos() {
        ProdutoPatchDTO dto = new ProdutoPatchDTO("Nome", null);
        
        assertEquals("Nome", dto.getNome());
        assertNull(dto.getPreco());
    }

    @Test
    void apiResponse_deveFuncionar() {
        ProdutoResponseDTO data = new ProdutoResponseDTO(1L, "Teste", new BigDecimal("10.00"));
        ApiResponse<ProdutoResponseDTO> response = ApiResponse.success(data, "Sucesso");
        
        assertTrue(response.isSuccess());
        assertEquals("Sucesso", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    void errorResponse_deveConterCampos() {
        ErrorResponse error = new ErrorResponse(404, "Not Found", "Mensagem", "/path");
        
        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getError());
        assertEquals("Mensagem", error.getMessage());
    }
}
