package br.com.resistenciarebelde.itemsinventario.controllers;

import br.com.resistenciarebelde.itemsinventario.ItemNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ItemNaoEncontradoException.class)
    public ResponseEntity<DefaultHttpResponseObject> handleItemNaoEncontrado(final ItemNaoEncontradoException itemNaoEncontradoException) {
        return new ResponseEntity<DefaultHttpResponseObject>(
                new DefaultHttpResponseObject(itemNaoEncontradoException.getMessage(),
                        HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }


}
