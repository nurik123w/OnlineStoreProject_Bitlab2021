package com.example.onlinestoreproject.controllers;


import com.example.onlinestoreproject.entity.Brand;
import com.example.onlinestoreproject.entity.Category;
import com.example.onlinestoreproject.entity.Item;
import com.example.onlinestoreproject.entity.Users;
import com.example.onlinestoreproject.model.Type;
import com.example.onlinestoreproject.services.ItemService;
import com.example.onlinestoreproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ItemService itemService;

    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String admin(Model model){
        List<Brand> brands = itemService.getAllBrands();
        List<Category> categories = itemService.getAllCategory();
        List<Item> items = itemService.getAllItems();
        List<Users> users = userService.getAllUsers(getUserData().getId());

        model.addAttribute("userList",users);
        model.addAttribute("categoryList",categories);
        model.addAttribute("brandList",brands);
        model.addAttribute("itemList",items);
        return "admin";
    }

    @GetMapping("/addItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String addItem(Model model){
        List<Brand> brand =  itemService.getAllBrands();
        List<Category> category =  itemService.getAllCategory();

        model.addAttribute("brandList",brand);
        model.addAttribute("categoryList",category);

        return "addProduct";
    }

    @PostMapping("/addItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String add(@RequestParam(value = "name",defaultValue = "-")String name,
                      @RequestParam(value = "details", defaultValue = "-") String details,
                      @RequestParam(value = "price",defaultValue = "0") int price,
                      @RequestParam(value = "type",defaultValue = "1") Type type,
                      @RequestParam(value = "brand_id",defaultValue = "1")Long brand_id,
                      @RequestParam(value = "category_id",defaultValue = "1") Long category_id,
                      @RequestParam(value = "img",defaultValue = "https://www.pngall.com/wp-content/uploads/5/Cardboard-Box-Transparent.png")  String img){

        Brand brand = itemService.getBrand(brand_id);
        Category category = itemService.getCategory(category_id);

        if(brand!=null && category!=null){
            Item item = new Item(
                    null,name,details,price,type,img,true,brand,category
            );
            itemService.addItem(item);
        }
        return "redirect:/admin";
    }

    @GetMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String update(@PathVariable("id")Long id,Model model){
        Item item = itemService.getItem(id);
        List<Category> categories = itemService.getAllCategory();
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brandList",brands);
        model.addAttribute("categoryList",categories);
        model.addAttribute("item",item);
        System.out.println(item.getType());
        return "updatePage";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String postUpdate(@PathVariable("id") Long id,
                             @RequestParam("name")String name,
                             @RequestParam("details") String details,
                             @RequestParam("price") double price,
                             @RequestParam("type") Type type,
                             @RequestParam("brand_id")Long brand_id,
                             @RequestParam("category_id") Long category_id,
                             @RequestParam(value = "img",defaultValue = "https://www.pngall.com/wp-content/uploads/5/Cardboard-Box-Transparent.png")  String img){


        Brand brand = itemService.getBrand(brand_id);
        Category category = itemService.getCategory(category_id);
        if(brand!=null && category!=null){
            Item item = itemService.getItem(id);
            item.setName(name);
            item.setDetails(details);
            item.setCategories(category);
            item.setBrand(brand);
            item.setType(type);
            item.setPrice(price);
            item.setImg(img);
            itemService.saveItem(item);
        }

        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String delete(@PathVariable("id")Long id){
        Item item = itemService.getItem(id);
        if(item!=null){
            if(item.isActual()==true){
                item.setActual(false);
            }else {
                item.setActual(true);
            }
            itemService.saveItem(item);
        }
        return "redirect:/admin";
    }

    @GetMapping("/addCategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String Ctg(){
        return "addCategory";
    }

    @PostMapping("/addCategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String addCtg(@RequestParam(value = "name",defaultValue = "-")String name){
        if(!name.equals("-")){
            List<Item> items = itemService.getAllItems();
            itemService.addCategory(new Category(null,name,items));
        }
        return "redirect:/admin";
    }

    @GetMapping("/addBrand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String brand(){
        return "addBrand";
    }

    @PostMapping("/addBrand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String addBrand(@RequestParam(value = "name")String name,
                           @RequestParam("c_name")String c_name){
        if(name!=null&&c_name!=null){
            List<Item> items = itemService.getAllItems();
            itemService.addBrand(new Brand(null,name,c_name,true,items));
        }
        return "redirect:/admin";
    }



    public Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
//            User secUser = (User) authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(authentication.getName());
            return myUser;
        }
        return null;
    }

}
