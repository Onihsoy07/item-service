package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findByAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long id, UpdateItemDto updateItem) {
        Item findItem = findById(id);
        findItem.setItemName(updateItem.getItemName());
        findItem.setPrice(updateItem.getPrice());
        findItem.setQuantity(updateItem.getQuantity());
    }

    public void delete(Long id) {
        Item item = findById(id);
        if (item!=null) {
            store.remove(id);
        }
    }

    public void clearStore() {
        store.clear();
    }

}
