package hello.itemservice.web.basic;

import hello.itemservice.domain.item.*;
import hello.itemservice.web.validation.ItemValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
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
//    private final ItemValidator itemValidator;

    //현재 컨트롤러에서 @Validater로 검증 사용가능
//    @InitBinder
//    public void init(WebDataBinder webDataBinder) {
//        webDataBinder.addValidators(itemValidator);
//    }

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

//    @PostMapping("/add")
//    public String saveV5(Item item,
//                         RedirectAttributes redirectAttributes,
//                         Model model) {
//
//        //검증 오류 결과 보관
//        Map<String, String> errors = new HashMap<>();
//
//        //검증 로직
//        if (!StringUtils.hasText(item.getItemName())) {
//            errors.put("itemName", "상품 이름은 필수입니다.");
//        }
//        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            errors.put("price", "가격은 1,000~1,000,000까지 허용합니다");
//        }
//        if (item.getQuantity() == null || item.getQuantity() >= 9999 || item.getQuantity() <= 0) {
//            errors.put("quantity", "수량은 0~9,999까지 허용합니다.");
//        }
//
//        //복합 검증
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if (resultPrice < 10000) {
////                errors.put("globalError", "가격 * 수량의 합이 10,000원 이상 허용합니다. 현재 총 가격 : " + resultPrice);
//            }
//        }
//
//        //검증 실패하면 model 넣어주고 입력폼으로
//        if (!errors.isEmpty()) {
//            log.info("error = {}", errors);
//            return "basic/addForm";
//        }
//
//        //성공로직
//        log.info("item.open={}", item.getOpen());
//        log.info("item.regions={}", item.getRegions());
//        log.info("item.itemType={}", item.getItemType());
//
//
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/basic/items/{itemId}";
//    }

//    @PostMapping("/add")
    public String addItemV1(Item item,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000~1,000,000까지 허용합니다"));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999 || item.getQuantity() <= 0) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 0~9,999까지 허용합니다."));
        }

        //복합 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합이 10,000원 이상 허용합니다. 현재 총 가격 : " + resultPrice));
            }
        }

        //검증 실패하면 model 넣어주고 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
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

//    @PostMapping("/add")
    public String addItemV2(Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000~1,000,000까지 허용합니다"));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999 || item.getQuantity() <= 0) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 0~9,999까지 허용합니다."));
        }

        //복합 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합이 10,000원 이상 허용합니다. 현재 총 가격 : " + resultPrice));
            }
        }

        //검증 실패하면 model 넣어주고 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
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

//    @PostMapping("/add")
    public String addItemV3(Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999 || item.getQuantity() <= 0) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        //복합 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
            }
        }

        //검증 실패하면 model 넣어주고 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
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

//    @PostMapping("/add")
    public String addItemV4(Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.rejectValue("itemName", "required");
        }
//        ==>위의 코드 아래 한줄로 치환가능(Empty 공백일때)
//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999 || item.getQuantity() <= 0) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //복합 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증 실패하면 model 넣어주고 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
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

//    @PostMapping("/add")
    public String addItemV5(Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

//        if (itemValidator.supports(item.getClass())) {
//            itemValidator.validate(item, bindingResult);
//        }

        //검증 실패하면 model 넣어주고 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
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

//    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        //복합 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합이 10,000원 이상 허용합니다. 현재 총 가격 : " + resultPrice));
            }
        }

        //검증 실패하면 model 넣어주고 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
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

//    @PostMapping("/add")
    public String addItemV7(@Validated(SaveCheck.class) @ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        //복합 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합이 10,000원 이상 허용합니다. 현재 총 가격 : " + resultPrice));
            }
        }

        //검증 실패하면 model 넣어주고 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
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

    @PostMapping("/add")
    public String addItemV8(@Validated @ModelAttribute("item") SaveItemDto saveItemDto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        //복합 검증
        if (saveItemDto.getPrice() != null && saveItemDto.getQuantity() != null) {
            int resultPrice = saveItemDto.getPrice() * saveItemDto.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합이 10,000원 이상 허용합니다. 현재 총 가격 : " + resultPrice));
            }
        }

        //검증 실패하면 model 넣어주고 입력폼으로
        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
            return "basic/addForm";
        }

        Item item = new Item().builder()
                .itemName(saveItemDto.getItemName())
                .price(saveItemDto.getPrice())
                .quantity(saveItemDto.getQuantity())
                .build();

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

//    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,
                       @Validated @ModelAttribute Item item,
                       BindingResult bindingResult,
                       Model model) {

        //복합 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합이 10,000원 이상 허용합니다. 현재 총 가격 : " + resultPrice));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "basic/editForm";
        }

        itemRepository.update(itemId, item);
        Item updateItem = itemRepository.findById(itemId);
        model.addAttribute("item", updateItem);

        return "redirect:/basic/items/{itemId}";
    }

//    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId,
                       @Validated(UpdateCheck.class) @ModelAttribute Item item,
                       BindingResult bindingResult,
                       Model model) {

        //복합 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합이 10,000원 이상 허용합니다. 현재 총 가격 : " + resultPrice));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "basic/editForm";
        }

        itemRepository.update(itemId, item);
        Item updateItem = itemRepository.findById(itemId);
        model.addAttribute("item", updateItem);

        return "redirect:/basic/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String editV3(@PathVariable Long itemId,
                       @Validated @ModelAttribute("item") UpdateItemDto updateItemDto,
                       BindingResult bindingResult,
                       Model model) {

        //복합 검증
        if (updateItemDto.getPrice() != null && updateItemDto.getQuantity() != null) {
            int resultPrice = updateItemDto.getPrice() * updateItemDto.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합이 10,000원 이상 허용합니다. 현재 총 가격 : " + resultPrice));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "basic/editForm";
        }

        Item item = new Item().builder()
                .itemName(updateItemDto.getItemName())
                .price(updateItemDto.getPrice())
                .quantity(updateItemDto.getQuantity())
                .build();

        itemRepository.update(itemId, item);
        Item updateItem = itemRepository.findById(itemId);
        model.addAttribute("item", updateItem);

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
