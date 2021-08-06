package br.com.resistenciarebelde.itemsinventario.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DefaultHttpResponseObject {

    private String message;
    private Integer statusCode;

}
