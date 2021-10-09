package br.com.zup.edu.meubanco.transferencia;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Optional;

public class NovaTransferenciaRequest {

    @NotNull
    private Long contaOrigemId;

    @NotNull
    private Long contaDestinoId;

    @NotNull
    @Positive
    private BigDecimal valor;

    public NovaTransferenciaRequest(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) {
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
        this.valor = valor;
    }

    public Long getContaOrigemId() {
        return contaOrigemId;
    }
    public Long getContaDestinoId() {
        return contaDestinoId;
    }
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * Converte request para entidade de dominio
     */
    public Transferencia toModel(ContaRepository repository) {

        Optional<Conta> origem = repository.findById(contaOrigemId);
        if (origem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "conta origem não encontrada");
        }

        Optional<Conta> destino = repository.findById(contaDestinoId);
        if (destino.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "conta destino não encontrada");
        }

        return new Transferencia(origem.get(), destino.get(), valor);
    }
}
