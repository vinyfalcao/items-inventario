package br.com.resistenciarebelde.itemsinventario.services;

import br.com.resistenciarebelde.itemsinventario.ItemNaoEncontradoException;
import br.com.resistenciarebelde.itemsinventario.controllers.NomeItemDTO;
import br.com.resistenciarebelde.itemsinventario.model.Item;
import br.com.resistenciarebelde.itemsinventario.producers.ItemEventProducer;
import br.com.resistenciarebelde.itemsinventario.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemEventProducer itemEventProducer;

    public Flux<Item> findAll(@RequestParam final Integer pageIndex,
                              @RequestParam final Integer pageSize){

        return itemRepository.findAllBy(PageRequest.of(pageIndex, pageSize));
    }

    public Mono<Item> findById(final String id){
        return itemRepository.findById(id)
                .switchIfEmpty(Mono.error(new ItemNaoEncontradoException(id)));
    }

    public Mono<Item> save(final Item item) {
        item.setUuid(UUID.randomUUID());
        return itemRepository.save(item)
                .doOnSuccess(itemEventProducer::sendEvent);
    }

    public Flux<NomeItemDTO> findAllNames() {
        return itemRepository.findAll()
                .map(Item::getNome)
                .map(NomeItemDTO::new);
    }

    public Mono<Long> sumAllPontos(){
        return itemRepository.findAll()
                .map(Item::getPontos)
                .reduce(Long::sum);
    }

    public Flux<ItemsAgrupados> groupItems(){
        return itemRepository.findAll()
                .map(Item::getNome)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .flux()
                .flatMap(map -> Flux.fromIterable(map.entrySet()))
                .map(entrySet -> new ItemsAgrupados(entrySet.getKey(), entrySet.getValue()));
    }
}
