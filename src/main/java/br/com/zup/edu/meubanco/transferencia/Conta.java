package br.com.zup.edu.meubanco.transferencia;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Conta {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 120, updatable = false)
    private String titular;

    @Column(nullable = false, length = 4, updatable = false)
    private String agencia;

    @Column(nullable = false, length = 6, updatable = false)
    private String numero;

    @Column(nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    @Version
    @Column(nullable = false)
    private Long version;

    @Deprecated
    public Conta() {}

    public Conta(String titular, String agencia, String numero, BigDecimal saldo) {
        this.titular = titular;
        this.agencia = agencia;
        this.numero = numero;
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getSaldo() {
        return this.saldo;
    }

    /**
     * Debita da conta
     */
    public void debita(BigDecimal valor) {
        this.saldo = saldo.subtract(valor);
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Credita da conta
     */
    public void credita(BigDecimal valor) {
        this.saldo = saldo.add(valor);
        this.atualizadoEm = LocalDateTime.now();
    }


}
