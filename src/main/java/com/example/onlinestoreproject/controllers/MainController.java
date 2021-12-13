package com.example.onlinestoreproject.controllers;
import com.example.onlinestoreproject.entity.Brand;
import com.example.onlinestoreproject.entity.Item;
import com.example.onlinestoreproject.entity.Users;
import com.example.onlinestoreproject.model.ItemRecord;
import com.example.onlinestoreproject.model.Type;
import com.example.onlinestoreproject.services.ItemService;
import com.example.onlinestoreproject.services.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    private final UserService userService;


    @GetMapping()
    public String main(Model model){
        model.addAttribute("currentUser",getUserData());
        return "index";
    }

    @GetMapping("/goods/{type}/{category}")
    public String details(@PathVariable("type") String typeValue,
                          @PathVariable("category")String categoryName,
                          Model model){
        Type type = Type.valueOf(typeValue);
        List<Item> items  = itemService.findByType(type,categoryName);
        List<Brand> brands = itemService.getAllBrands();
            if(items!=null){
                model.addAttribute("itemList",items);
                model.addAttribute("brandList",brands);
                model.addAttribute("type", typeValue);
                model.addAttribute("category", categoryName);
                model.addAttribute("currentUser",getUserData());
            }

        return "goods";
    }

    @GetMapping("/details/{id}")
    public String detail(@PathVariable("id")Long id,Model model){
        Item item = itemService.getItem(id);
        model.addAttribute("currentUser",getUserData());
        if(item!=null){
                model.addAttribute("item",item);
        }

        return "details";
    }

    @GetMapping("/addToBasket/{id}")
    @ResponseBody
    public void addToBasket(@PathVariable("id")Long id,
                              HttpServletRequest request){
        Map<Long,Integer> items = (Map<Long, Integer>) request.getSession().getAttribute("basket_amount");
        ArrayList<Item> itemsList = (ArrayList<Item>) request.getSession().getAttribute("items_basket");
        if(items == null && itemsList == null ){
            items = new HashMap<>();
            itemsList = new ArrayList<>();
            request.getSession().setAttribute("basket_amount",items);
            request.getSession().setAttribute("items_basket",itemsList);
        }

        Integer amount = 0;
        Item item = itemService.getItem(id);
        if(items.containsKey(id)){
          amount = items.get(id);
          amount++;
        }else{
            amount = 1;
            itemsList.add(item);
            request.getSession().setAttribute("items_basket",itemsList);
        }

        items.put(id,amount);

        request.getSession().setAttribute("basket_amount",items);



        //        Map<Long, ArrayList<Item>> items = (Map<Long, ArrayList<Item>>) request.getSession().getAttribute("MY_BASKET");
//
//        if(items==null){
//            items = new HashMap<Long, ArrayList<Item>>();
//            request.getSession().setAttribute("MY_BASKET",items);
//        }
//
//        Item item = itemService.getItem(id);
//        if (items.containsKey(id)) {
//            ArrayList<Item> items1 = items.get(id);
////            Item item = itemService.getItem(id);
//            items1.add(item);
//            items.replace(id,items1);
//        }else {
//            ArrayList<Item> list = new ArrayList<>();
//            list.add(item);
//            items.put(item.getId(), list);
//        }

    }

    @GetMapping("/cart")
    public String cart(HttpServletRequest req,Model model){

        double sum =  itemService.totalSum(req);

        model.addAttribute("price",sum);
        model.addAttribute("currentUser",getUserData());

        return "cart";
    }

    @GetMapping("/amount")
    @ResponseBody
    public void saveCount(@RequestParam("id")Long id,
                          @RequestParam("amount")Integer amount,
                          HttpServletRequest req,HttpServletResponse resp) throws IOException {
        Map<Long,Integer> amountList = (Map<Long, Integer>) req.getSession().getAttribute("basket_amount");

        amountList.replace(id,amount);

        req.getSession().setAttribute("basket_amount",amountList);



        double sum  =   itemService.totalSum(req);


            PrintWriter printWriter = resp.getWriter();
            String parsed = "";


            Gson gson = new Gson();
            parsed = gson.toJson(sum);
            printWriter.println(parsed);
    }


    @PostMapping("/delete")
    public String delete(@RequestParam("id")Long id,Model model,HttpServletRequest req){


        Map<Long,Integer> amount = (Map<Long, Integer>) req.getSession().getAttribute("basket_amount");
        List<Item> items = (ArrayList<Item>) req.getSession().getAttribute("items_basket");
        items.remove(items.stream().filter(f->f.getId().equals(id)).findFirst().get());
        amount.remove(id);


        model.addAttribute("currentUser",getUserData());
        return "redirect:/cart";
    }

    @GetMapping("/sortOut")
    @ResponseBody
    public void sortOut (@RequestParam(value = "mfc_id",defaultValue = "0")Long id,
                         @RequestParam(value = "sort_type",defaultValue = "1")String elem,
                         @RequestParam(value = "category",defaultValue = "0")String category,
                         @RequestParam(value = "type",defaultValue = "0")String typeValue,
                         @RequestParam(value = "from",defaultValue = "0") double from,
                         @RequestParam(value = "till",defaultValue = "0") double till,
                         HttpServletResponse resp) throws IOException {

              if(till==0){
                  till= itemService.getAllItems().stream().max(Comparator.comparing(Item::getPrice)).get().getPrice();
              }

              Type type = Type.valueOf(typeValue);


              PrintWriter printWriter = resp.getWriter();
              List<ItemRecord> items = itemService.sortBy(elem,type,category,id,from,till);
              String parsed;

                if(!items.isEmpty()){
                    Gson gson = new Gson();
                    parsed = gson.toJson(items);
                    printWriter.println(parsed);
                }
    }


    @GetMapping(value = "/403")
    public String accessDenied(Model model){
        model.addAttribute("currentUser",getUserData());
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("currentUser",getUserData());
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        model.addAttribute("currentUser",getUserData());
        return "profile";
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public String profileSave(@RequestParam("user_full_name")String fullName,
                              @RequestParam("phone_num")String  phone,
                              @RequestParam("address_line")String address,
                              @RequestParam("postalCode")int postCode){

        Users user  = getUserData();
        if(user!=null){
            user.setFullName(fullName);
            user.setAddress(address);
            user.setPhoneNum(phone);
            user.setPostalCode(postCode);
            userService.save(user);
        }

        return "redirect:profile";
    }

    @PostMapping("/changePass")
    public String changePassword(
                                 @RequestParam(name = "user_password")String password,
                                 @RequestParam(name = "re_user_password")String rePassword){

        userService.changePassword(getUserData(),password,rePassword);

        return "redirect:profile";
    }


    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("currentUser",getUserData());
        return "register";
    }

    @PostMapping("/register")
    public String toRegister(@RequestParam(name = "user_email")String email,
                             @RequestParam(name = "user_password")String password,
                             @RequestParam(name = "re_user_password")String rePassword,
                             @RequestParam(name = "user_full_name")String fullName){


        if(password.equals(rePassword)){
            Users newUser = new Users();
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPassword(password);
            if(userService.createUser(newUser)!=null){
                 return "redirect:/register?success";
            }
        }

        return "redirect:/register?error";

    }


    public Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User) authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }



}
