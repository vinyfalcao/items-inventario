package br.com.resistenciarebelde.itemsinventario.controllers;

import br.com.resistenciarebelde.itemsinventario.model.Item;
import br.com.resistenciarebelde.itemsinventario.services.ItemService;
import br.com.resistenciarebelde.itemsinventario.services.ItemsAgrupados;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public Flux<Item> findAll(@RequestParam final Integer pageIndex,
                              @RequestParam final Integer pageSize){
        return itemService.findAll(pageIndex, pageSize);
    }

    @GetMapping("/nomes")
    public Flux<NomeItemDTO> findAllNames(){
        return itemService.findAllNames();
    }

    @GetMapping("/somaPontos")
    public Mono<Long> sumAllPontos(){
        return itemService.sumAllPontos();
    }

    @GetMapping("/group")
    public Flux<ItemsAgrupados> groupItems(){
        return itemService.groupItems();
    }

    @GetMapping("/{id}")
    public Mono<Item> findById(@PathVariable final String id){
        return itemService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> save(@RequestBody final Item item){
        return itemService.save(item);
    }


}
