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
@Table(name = "autor")
public class AutorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "dataNascimento")
    private LocalDate dataNascimento;

    @Column(name = "paisOrigem", nullable = false)
    private String paisOrigem;

    @Column(name = "cpf")
    private String cpf;

    @ManyToMany
    @JoinTable(
            name = "autor_obra", //nome tabela relacional
            joinColumns = @JoinColumn(name = "autor_id"), // FK que aponta para autor
            inverseJoinColumns = @JoinColumn(name = "obra_id") //FK que aponta para obra

    )
    private Set<ObraEntity> obras = new HashSet<>();

    //Metodos auxiliares para sincronizar N:N

    public void adicionarObra(ObraEntity obra) {
        this.obras.add(obra);
        obra.getAutores().add(this);
    }

    public void removerObra(ObraEntity obra) {
        this.obras.remove(obra);
        obra.getAutores().remove(this);
    }
}
