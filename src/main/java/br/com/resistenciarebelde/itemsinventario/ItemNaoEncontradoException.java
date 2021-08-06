package br.com.resistenciarebelde.itemsinventario;

public class ItemNaoEncontradoException extends RuntimeException{

    public ItemNaoEncontradoException(String id) {
        super("Item " + id + " não encontrado");
    }
}
