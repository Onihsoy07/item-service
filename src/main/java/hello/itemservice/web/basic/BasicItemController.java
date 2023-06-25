package hello.itemservice.web.basic;

import hello.itemservice.domain.item.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    //현제 클래스 모든 컨트롤러 model에 addAtrribute됨
    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        //enum의 모든 정보를 배열로 반환
        return ItemType.values();
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

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
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
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


    /**
     * 현재 저장 후 /basic/item view 던지는 것을 서버 내부에서 실행 url은 그대로
     * 그렇기 떄문에 새로고침(마지막 요청 다시 요청)을 하면 post로 요청되어 item 하나가 더 생김
     * 그래서 PRG(POST/REDIRECT/GET)을 사용해야 한다. PRG는 GET요청을 redirect를 이용하여 불러온다 .
     * 아래에 수정코드
     */
//    @PostMapping("/add")
//    public String saveV4(Item item) {
//        itemRepository.save(item);
//        return "/basic/item";
//    }

    //POST 후 redirect하여 GET 호출됨(PRG)
//    @PostMapping("/add")
//    public String saveV5(Item item) {
//        itemRepository.save(item);
//        return "redirect:/basic/items/" + item.getId();
//    }

    @PostMapping("/add")
    public String saveV5(Item item, RedirectAttributes redirectAttributes, Model model) {

        //검증 오류 결과 보관
        Map<String, String> errors = new HashMap<>();

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.put("price", "가격은 1,000~1,000,000까지 허용합니다");
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999 || item.getQuantity() <= 0) {
            errors.put("quantity", "수량은 0~9,999까지 허용합니다.");
        }
        
        //복합 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.put("globalError", "가격 * 수량의 합이 10,000원 이상 허용합니다. 현재 총 가격 : " + resultPrice);
            }
        }

        //검증 실패하면 model 넣어주고 입력폼으로
        if (!errors.isEmpty()) {
            for (String error : errors.values()) {
                log.info("error = {}", error);
            }
            model.addAttribute("errors", errors);
            return "basic/addForm";
        }

        //성공로직
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId,
                           Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,
                       Item updateItemDto,
                       Model model) {
        itemRepository.update(itemId, updateItemDto);
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "redirect:/basic/items/{itemId}";
    }

    //일단 대충 만듬
    @GetMapping("/{itemId}/delete")
    public String delete(@PathVariable Long itemId) {
        itemRepository.delete(itemId);

        return "redirect:/basic/items";
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
