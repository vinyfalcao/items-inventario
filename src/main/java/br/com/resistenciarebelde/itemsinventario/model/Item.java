package br.com.resistenciarebelde.itemsinventario.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {

    @Id
    private String id;
    private UUID uuid;
    private String nome;
    private Long pontos;

}