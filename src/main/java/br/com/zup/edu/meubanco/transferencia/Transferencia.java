package br.com.zup.edu.meubanco.transferencia;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transferencia {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Conta origem;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Conta destino;

    @Column(nullable = false, updatable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TipoDeTransferencia tipo = TipoDeTransferencia.PADRAO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Deprecated
    public Transferencia(){}

    public Transferencia(Conta origem, Conta destino, BigDecimal valor) {
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public Conta getOrigem() {
        return origem;
    }
    public Conta getDestino() {
        return destino;
    }
    public BigDecimal getValor() {
        return this.valor;
    }
    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    /**
     * Efetua transferencia entre contas
     */
    public void transfere() {
        origem.debita(valor);
        destino.credita(valor);
    }

}
