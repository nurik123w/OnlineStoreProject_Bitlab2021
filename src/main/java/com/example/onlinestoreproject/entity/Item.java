package com.example.onlinestoreproject.entity;
import com.example.onlinestoreproject.model.Type;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;


@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Transactional
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;
    @Column(name = "name")
    public String name;
    @Column(name = "details")
    public String details;
    @Column(name = "price")
    public double price;

    @Column(name = "type")
    public Type type;

    @Column(name = "img")
    public String img;
    @Column(name = "actual")
    boolean actual = Boolean.TRUE;

    @ManyToOne( )
    @JoinColumn(name = "brand_id",referencedColumnName = "id")
    private Brand brand;

    @ManyToOne( )
    @JoinColumn(name = "categories_id",referencedColumnName = "id")
    private Category categories;

}
