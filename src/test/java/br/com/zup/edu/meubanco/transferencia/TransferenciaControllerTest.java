package br.com.zup.edu.meubanco.transferencia;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class TransferenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private TransferenciaRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        contaRepository.deleteAll();
    }

    @Test
    public void deveTransferirEntreContas() throws Exception {
        // cenario
        Conta origem = contaRepository.save(new Conta("Rafael", "1234", "123456", new BigDecimal("1000.99")));
        Conta destino = contaRepository.save(new Conta("Ryan", "4321", "678901", new BigDecimal("20.20")));

        // açao
        NovaTransferenciaRequest request = new NovaTransferenciaRequest(1L, 2L, new BigDecimal("100.09"));
        mockMvc.perform(post("/api/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print())
                // TODO: validar JSON de resposta
                // TODO: extrair ID da transferencia
        ;

        // validação
        List<Transferencia> todas = repository.findAll();
        assertEquals(1, todas.size());

        Transferencia _transferencia = todas.get(0);
        assertEquals(origem.getId(), _transferencia.getOrigem().getId());
        assertEquals(destino.getId(), _transferencia.getDestino().getId());
        assertEquals(new BigDecimal("100.09"), _transferencia.getValor());

        Conta _origem = contaRepository.findById(origem.getId()).get();
        Conta _destino = contaRepository.findById(destino.getId()).get();

        assertEquals(new BigDecimal("900.90"), _origem.getSaldo());
        assertEquals(new BigDecimal("120.29"), _destino.getSaldo());
    }

    @Test
    public void naoDeveTransferirEntreContas_quandoValorForNegativo() throws Exception {
        // açao e  validação
        NovaTransferenciaRequest request = new NovaTransferenciaRequest(1L, 2L, new BigDecimal("-0.01"));
        mockMvc.perform(post("/api/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                // TODO: validar JSON de resposta de erro
        ;
    }

    @Test
    public void naoDeveTransferirEntreContas_quandoDadosDeEntradaForemInvalidos() throws Exception {
        // açao e  validação
        NovaTransferenciaRequest request = new NovaTransferenciaRequest(null, null, new BigDecimal("-0.01"));
        mockMvc.perform(post("/api/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                // TODO: validar JSON de resposta de erro
        ;
    }

    @Test
    public void naoDeveTransferirEntreContas_quandoContaDeOrigemNaoEncontrada() throws Exception {
        // açao e  validação
        NovaTransferenciaRequest request = new NovaTransferenciaRequest(-9999L, 2L, new BigDecimal("0.01"));
        mockMvc.perform(post("/api/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
        // TODO: validar JSON de resposta de erro
        ;
    }

    @Test
    public void naoDeveTransferirEntreContas_quandoContaDeDestinoNaoEncontrada() throws Exception {
        // açao e  validação
        NovaTransferenciaRequest request = new NovaTransferenciaRequest(1L, -9999L, new BigDecimal("0.01"));
        mockMvc.perform(post("/api/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
        // TODO: validar JSON de resposta de erro
        ;
    }

}