package br.com.zup.edu.meubanco.transferencia;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NovaTransferenciaResponse {

    private final Long contaOrigemId;
    private final Long contaDestinoId;
    private final BigDecimal valorTransferido;
    private final LocalDateTime transferidoEm;

    public NovaTransferenciaResponse(Transferencia transferencia) {
        this.contaOrigemId = transferencia.getOrigem().getId();
        this.contaDestinoId = transferencia.getDestino().getId();
        this.valorTransferido = transferencia.getValor();
        this.transferidoEm = transferencia.getCriadoEm();
    }

    public Long getContaOrigemId() {
        return contaOrigemId;
    }
    public Long getContaDestinoId() {
        return contaDestinoId;
    }
    public BigDecimal getValorTransferido() {
        return valorTransferido;
    }
    public LocalDateTime getTransferidoEm() {
        return transferidoEm;
    }

    @Override
    public String toString() {
        return "NovaTransferenciaResponse{" +
                "contaOrigemId=" + contaOrigemId +
                ", contaDestinoId=" + contaDestinoId +
                ", valorTransferido=" + valorTransferido +
                '}';
    }
}
