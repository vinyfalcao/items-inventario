package br.com.resistenciarebelde.itemsinventario.controllers;

import br.com.resistenciarebelde.itemsinventario.model.Item;
import br.com.resistenciarebelde.itemsinventario.producers.ItemEventProducer;
import br.com.resistenciarebelde.itemsinventario.repositories.ItemRepository;
import br.com.resistenciarebelde.itemsinventario.services.ItemService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import(ItemService.class)
public class ItemControllerTest {

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ItemEventProducer itemEventProducer;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldFindAllItems(){
        final Item item1 = itemInstance("Item 1", 1L);
        final Item item2 = itemInstance("Item 2", 2l);
        var expectedItems = Flux.just(
                item1,
                item2
        );

        when(itemRepository.findAllBy(PageRequest.of(0, 2)))
                .thenReturn(expectedItems);

        webTestClient.get()
                .uri("/items?pageIndex=0&pageSize=2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].nome").isEqualTo(item1.getNome())
                .jsonPath("$[0].pontos").isEqualTo(item1.getPontos())
                .jsonPath("$[1].nome").isEqualTo(item2.getNome())
                .jsonPath("$[1].pontos").isEqualTo(item2.getPontos());
    }

    @Test
    public void shouldFindAllItemsWithPagination(){
        final Item item1 = itemInstance("Item 1", 1L);
        final Item item2 = itemInstance("Item 2", 2l);
        var expectedItems = Flux.just(
                item1
        );

        when(itemRepository.findAllBy(PageRequest.of(0, 1)))
                .thenReturn(expectedItems);

        webTestClient.get()
                .uri("/items?pageIndex=0&pageSize=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1);
    }

    @Test
    public void shouldFindById(){
        final Item itemInstance = itemInstance("Item 1", 1L);
        final Mono<Item> expectedItem = Mono.just(itemInstance);
        when(itemRepository.findById(itemInstance.getId()))
                .thenReturn(expectedItem);
        webTestClient.get()
                .uri("/items/" + itemInstance.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nome").isEqualTo(itemInstance.getNome())
                .jsonPath("$.pontos").isEqualTo(itemInstance.getPontos());
    }

    @Test
    public void whenItemWasNotFoundHttpResponseShouldBeNotFound(){
        when(itemRepository.findById(anyString())).thenReturn(Mono.empty());
        webTestClient.get()
                .uri("/items/qualquerId")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldSaveNewItem(){
        final Item item = itemInstance("Item 1", 1L);
        when(itemRepository.save(any(Item.class))).thenReturn(Mono.just(item));

        webTestClient.post()
                .uri("/items")
                .header(HttpHeaders.ACCEPT, "application/json")
                .body(BodyInserters.fromValue(item))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(item.getId());

        verify(itemEventProducer).sendEvent(item);
    }

    @Test
    public void shouldFindAllNomes(){
        final Item item1 = itemInstance("Item 1", 1L);
        final Item item2 = itemInstance("Item 2", 2l);
        when(itemRepository.findAll()).thenReturn(Flux.just(item1, item2));

        webTestClient.get()
                .uri("/items/nomes")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].nome").isEqualTo(item1.getNome())
                .jsonPath("$[1].nome").isEqualTo(item2.getNome());
    }

    @Test
    public void shouldSumAllPontos(){
        final Item item1 = itemInstance("Item 1", 5L);
        final Item item2 = itemInstance("Item 2", 10l);
        final Item item3 = itemInstance("Item 2", 20l);
        when(itemRepository.findAll()).thenReturn(Flux.just(item1, item2, item3));
        final long expectedSum =
                LongStream.of(item1.getPontos(), item2.getPontos(), item3.getPontos()).sum();

        webTestClient.get()
                .uri("/items/somaPontos")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(Long.toString(expectedSum));
    }

    @Test
    public void shouldGroupItems(){
        final Item item1 = itemInstance("Item 1", 5L);
        final Item item2 = itemInstance("Item 2", 10l);
        when(itemRepository.findAll()).thenReturn(Flux.just(item1, item2, item2));

        webTestClient.get()
                .uri("/items/group")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[1].nomeItem").isEqualTo(item1.getNome())
                .jsonPath("$[1].count").isEqualTo(1)
                .jsonPath("$[0].nomeItem").isEqualTo(item2.getNome())
                .jsonPath("$[0].count").isEqualTo(2);
    }

    private Item itemInstance(final String nome, final Long pontos){
        return new Item(new ObjectId().toString(), UUID.randomUUID(), nome, pontos);
    }





}