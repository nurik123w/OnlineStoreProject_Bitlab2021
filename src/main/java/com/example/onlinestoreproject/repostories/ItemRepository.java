package com.example.onlinestoreproject.repostories;
import com.example.onlinestoreproject.entity.Item;
import com.example.onlinestoreproject.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Item,Long> {

    List<Item> findAllByTypeEqualsAndCategories_NameAndActualTrue(Type type,String name);

    List<Item> findAllByTypeEqualsAndCategories_NameAndActualTrueOrderByPriceAsc(Type type, String c_name);

    List<Item> findAllByTypeEqualsAndCategories_NameAndPriceBetweenAndActualTrue(Type type, String c_name,double f , double s);

    List<Item> findAllByTypeEqualsAndCategories_NameAndActualTrueOrderByPriceDesc(Type type, String c_name);
    
    List<Item> findAllByTypeEqualsAndCategories_NameAndBrand_IdAndActualTrue(Type type, String c_name, Long id);

    List<Item> findAllByTypeEqualsAndCategories_NameAndBrand_IdAndPriceBetweenAndActualTrueOrderByPriceAsc(Type type,String c_name,Long b_id,double f, double t );

    List<Item> findAllByTypeEqualsAndCategories_NameAndBrand_IdAndPriceBetweenAndActualTrueOrderByPriceDesc(Type type,String c_name,Long b_id,double f, double t);

    List<Item> findAllByTypeEqualsAndCategories_NameAndPriceBetweenAndActualTrueOrderByPriceAsc(Type type,String c_name,double f, double t);

    List<Item> findAllByTypeEqualsAndCategories_NameAndPriceBetweenAndActualTrueOrderByPriceDesc(Type type,String c_name,double f, double t);
}
