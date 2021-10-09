package br.com.zup.edu.meubanco.transferencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class TransferenciaController {

    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private TransferenciaRepository repository;

    @Transactional
    @PostMapping("/api/transferencias")
    public ResponseEntity<?> transfere(@RequestBody @Valid NovaTransferenciaRequest request, UriComponentsBuilder uriBuilder) {

        Transferencia transferencia = request.toModel(contaRepository);
        transferencia.transfere();

        repository.save(transferencia);

        URI location = uriBuilder
                .path("/api/transferencias/{id}")
                .buildAndExpand(transferencia.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(new NovaTransferenciaResponse(transferencia));
    }

}
