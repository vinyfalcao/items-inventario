package br.com.resistenciarebelde.itemsinventario.services;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class ItemsAgrupados implements Serializable {

    private String nomeItem;
    private Long count;

}
