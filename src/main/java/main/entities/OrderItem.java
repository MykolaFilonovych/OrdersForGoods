package main.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ordersitems")
public class OrderItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="brand")
    private String brand;
    @Column(name="quantity")
    private int quantity;
    @Column(name="goodid")
    private int goodId;
    @Column(name="orderid")
    private int orderId;

    public OrderItem()
    {

    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity && goodId == orderItem.goodId && name.equals(orderItem.name) &&
                brand.equals(orderItem.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, brand, quantity, goodId);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", quantity=" + quantity +
                ", goodId=" + goodId +
                ", orderId=" + orderId +
                '}';
    }
}
