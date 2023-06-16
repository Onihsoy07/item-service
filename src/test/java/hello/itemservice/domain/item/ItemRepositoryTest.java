package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Item item = new Item().builder()
                .itemName("itemA")
                .price(2000)
                .quantity(12)
                .build();

        //when
        itemRepository.save(item);

        //then
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isEqualTo(item);
    }

    @Test
    void findByAll() {
        //given
        Item itemA = new Item().builder()
                .itemName("itemA")
                .price(2000)
                .quantity(12)
                .build();
        Item itemB = new Item().builder()
                .itemName("itemB")
                .price(10000)
                .quantity(74)
                .build();

        itemRepository.save(itemA);
        itemRepository.save(itemB);

        //when
        List<Item> result = itemRepository.findByAll();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(itemA, itemB);
    }

    @Test
    void update() {
        //given
        Item itemA = new Item().builder()
                .itemName("itemA")
                .price(2000)
                .quantity(12)
                .build();
        Item savedItem = itemRepository.save(itemA);
        Long itemAId = savedItem.getId();

        //when
        UpdateItemDto updateItem = new UpdateItemDto("itemB", 10000, 24);
        itemRepository.update(itemAId, updateItem);

        //then
        Item findItem = itemRepository.findById(itemAId);

        assertThat(findItem.getItemName()).isEqualTo(updateItem.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateItem.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateItem.getQuantity());
    }
}