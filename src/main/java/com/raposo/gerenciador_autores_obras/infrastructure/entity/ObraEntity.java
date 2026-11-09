package com.raposo.gerenciador_autores_obras.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder - SEM Builder porque estou usando Mapper


@Entity
@Table(name = "obra")
public class ObraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao", length = 240)
    private String descricao;

    @Column(name = "dataPublicacao")
    private LocalDate dataPublicacao;

    @Column(name = "dataExposicao")
    private LocalDate dataExposicao;

    @ManyToMany(mappedBy = "obras")
    private Set<AutorEntity> autores = new HashSet<>();

}
