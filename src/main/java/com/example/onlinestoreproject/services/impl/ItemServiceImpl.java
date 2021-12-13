package com.example.onlinestoreproject.services.impl;
import com.example.onlinestoreproject.entity.Brand;
import com.example.onlinestoreproject.entity.Category;
import com.example.onlinestoreproject.entity.Item;
import com.example.onlinestoreproject.model.ItemRecord;
import com.example.onlinestoreproject.model.Type;
import com.example.onlinestoreproject.repostories.BrandRepository;
import com.example.onlinestoreproject.repostories.CategoryRepository;
import com.example.onlinestoreproject.repostories.ItemRepository;
import com.example.onlinestoreproject.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;




    @Override
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> findByType(Type type,String name){
        return itemRepository.findAllByTypeEqualsAndCategories_NameAndActualTrue(type,name);
    }

    @Override
    public Item getItem(Long id) {
        return itemRepository.getById(id);
    }

    @Override
    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }

    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

//    @Override
//    public List<ItemRecord> sortBy(String val,Type type, String c_name,Long b_id,double ff , double t) {
//        List<Item> sortedList;
//        List<ItemRecord> sortedList1 = new ArrayList<>();
//        if(val.equals("1")){
//         sortedList  = itemRepository.findAllByTypeEqualsAndCategories_NameAndBrand_IdAndPriceBetweenAndActualTrueOrderByPriceAsc(type,c_name,b_id,ff,t);
//            sortedList.forEach(f -> {
//                sortedList1.add(new ItemRecord(f.id, f.isActual(), f.details, f.name, f.price, f.type, f.img));
//            });
//
//            return sortedList1;
//        }else if(val.equals("2")){
//            sortedList = itemRepository.findAllByTypeEqualsAndCategories_NameAndBrand_IdAndPriceBetweenAndActualTrueOrderByPriceDesc(type,c_name,b_id,ff,t);
//            sortedList.forEach(f -> {
//                sortedList1.add(new ItemRecord(f.id, f.isActual(), f.details, f.name, f.price, f.type, f.img));
//            });
//
//            return sortedList1;
//        }
//        return null;
//    }


    @Override
    public List<ItemRecord> sortBy(String val,Type type, String c_name,Long b_id,double ff , double t) {
        List<Item> sortedList;
        List<ItemRecord> sortedList1 = new ArrayList<>();

        if(val!=null) {
            if (val.equals("1")) {
                if(b_id==0){
                    sortedList = itemRepository.findAllByTypeEqualsAndCategories_NameAndPriceBetweenAndActualTrueOrderByPriceDesc(type, c_name, ff, t);
                }else {
                    sortedList = itemRepository.findAllByTypeEqualsAndCategories_NameAndBrand_IdAndPriceBetweenAndActualTrueOrderByPriceDesc(type, c_name, b_id, ff, t);
                }
                sortedList.forEach(f -> {
                    sortedList1.add(new ItemRecord(f.id, f.isActual(), f.details, f.name, f.price, f.type, f.img));
                });
                return sortedList1;

            } else if (val.equals("2")) {
                if(b_id==0){
                    sortedList = itemRepository.findAllByTypeEqualsAndCategories_NameAndPriceBetweenAndActualTrueOrderByPriceAsc(type, c_name, ff, t);
                }else {
                    sortedList = itemRepository.findAllByTypeEqualsAndCategories_NameAndBrand_IdAndPriceBetweenAndActualTrueOrderByPriceAsc(type, c_name, b_id, ff, t);
                }
                sortedList.forEach(f -> {
                    sortedList1.add(new ItemRecord(f.id, f.isActual(), f.details, f.name, f.price, f.type, f.img));
                });
                return sortedList1;
            }
        }
        return null;
    }



    @Override
    public List<ItemRecord> findByAmounts(Type type,String name,double first, double second) {
        List<Item> sortedList;
        List<ItemRecord> sortedList1 = new ArrayList<>();
        sortedList = itemRepository.findAllByTypeEqualsAndCategories_NameAndPriceBetweenAndActualTrue(type,name,first,second);
            sortedList.forEach( f -> {
                sortedList1.add(new ItemRecord(f.id,f.isActual(),f.details,f.name,f.price,f.type,f.img));
            });
            return sortedList1;
    }

    @Override
    public List<ItemRecord> findByBrand(Type type, String c_name, Long id) {
        List<Item> item;
        List<ItemRecord> records = new ArrayList<>();
        item =  itemRepository.findAllByTypeEqualsAndCategories_NameAndBrand_IdAndActualTrue(type,c_name,id);
        item.forEach(i ->{
            records.add(new ItemRecord(i.id,i.isActual(),i.details,i.name,i.price,i.type,i.img));
        });
        return records;
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand addBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand getBrand(Long id) {
        return brandRepository.getById(id);
    }

    @Override
    public void deleteBrand(Brand brand) {
       brandRepository.delete(brand);
    }

    @Override
    public Category addCategory(Category c) {
        return categoryRepository.save(c);
    }

    @Override
    public Category getCategory(Long id) {
        return  categoryRepository.getById(id);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public double totalSum(HttpServletRequest req) {
        Map<Long,Integer> itemAmount = (Map<Long, Integer>) req.getSession().getAttribute("basket_amount");
        List<Item> itemList = (List<Item>) req.getSession().getAttribute("items_basket");

        double sum = 0;

        if(itemList!=null){
            sum = itemList.stream().map(m -> m.getPrice()*itemAmount.get(m.getId())).mapToDouble(Double::doubleValue).sum();
        }

        return sum;
    }


}
