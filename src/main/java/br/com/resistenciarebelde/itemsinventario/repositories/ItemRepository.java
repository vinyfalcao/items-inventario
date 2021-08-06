package br.com.resistenciarebelde.itemsinventario.repositories;

import br.com.resistenciarebelde.itemsinventario.model.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {

    Flux<Item> findAllBy(Pageable pageable);

}
