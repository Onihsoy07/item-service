package hello.itemservice.web.basic;

import hello.itemservice.domain.item.AddItemDto;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.UpdateItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findByAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId,
                       Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
//    public String save(AddItemDto addItemDto,
//                       Model model) {
//        Item item = new Item().builder()
//                .itemName(addItemDto.getItemName())
//                .price(addItemDto.getPrice())
//                .quantity(addItemDto.getQuantity())
//                .build();
//        itemRepository.save(item);
//
//        model.addAttribute("item", item);
//
//        return "/basic/item";
//    }

//    @PostMapping("/add")
//    public String saveV2(@ModelAttribute("item") Item item,
//                         Model model) {
//        itemRepository.save(item);
////        model.addAttribute("item", item); //@ModelAttribute의 value값은 Model.addAttribute 자동으로 넣어줌
//
//        return "/basic/item";
//    }

//    @PostMapping("/add")
//    public String saveV3(@ModelAttribute Item item) {
          //Model 생략 가능, @ModelAttribute value 생략하면 Item -> item 변경 후 자동 Model.addAttribute 넣어줌
//        itemRepository.save(item);
//        return "/basic/item";
//    }

    @PostMapping("/add")
    public String saveV4(Item item) {
        itemRepository.save(item);
        return "/basic/item";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId,
                           Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId,
                       UpdateItemDto updateItemDto,
                       Model model) {
        itemRepository.update(itemId, updateItemDto);
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "redirect:/basic/items/{itemId}";
    }


    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 2000, 20));
        itemRepository.save(new Item("itemB", 10000, 37));
    }

}
