package main.entities;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="creationtime")
    private LocalDateTime creationTime;
    @Column(name="items")
    private String items;
    @Column(name="paymentstatus")
    private String paymentStatus;

    public Order()
    {

    }

    public int getId() {
        return id;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return creationTime.equals(order.creationTime) && items.equals(order.items) && paymentStatus.equals(order.paymentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creationTime, items, paymentStatus);
    }

    @Override
    public String toString() {
        return "Order{" +
                "creationTime=" + creationTime +
                ", items='" + items + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
