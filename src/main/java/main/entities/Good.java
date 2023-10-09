package main.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="goods")
public class Good
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="brand")
    private String brand;
    @Column(name="price")
    private int price;
    @Column(name="quantity")
    private int quantity;

    public Good()
    {

    }

    public int getId(){return id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Good good = (Good) o;
        return price == good.price && quantity == good.quantity && name.equals(good.name) && brand.equals(good.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, brand, price, quantity);
    }

    @Override
    public String toString() {
        return "Good{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
