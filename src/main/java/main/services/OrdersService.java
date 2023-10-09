package main.services;

import main.entities.Good;
import main.entities.Order;
import main.entities.OrderItem;
import main.repositories.GoodsRepository;
import main.repositories.OrdersItemsRepository;
import main.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersService
{
    private final GoodsRepository goodsRepository;
    private final OrdersRepository ordersRepository;
    private final OrdersItemsRepository ordersItemsRepository;
    private String notIncludedGoodsInformation;
    private boolean isOrderCreated;
    private int createdOrderId;

    @Autowired
    public OrdersService(GoodsRepository goodsRepository, OrdersRepository ordersRepository,
                         OrdersItemsRepository ordersItemsRepository)
    {
        this.goodsRepository = goodsRepository;
        this.ordersRepository = ordersRepository;
        this.ordersItemsRepository = ordersItemsRepository;
        deleteUnpaidOrders();
    }

    public void createOrder(OrderItem[] orderItems)
    {
        deleteUnpaidOrders();

        List<Good> goodsList = goodsRepository.findAll();
        List<OrderItem> includedOrderItems = new ArrayList<>();
        Order order;

        isOrderCreated = false;
        boolean nameEquality = false;
        boolean brandEquality = false;
        boolean quantityEnoughness = false;

        boolean goodsPresence;

        String orderItemName;
        String orderItemBrand;
        int orderItemQuantity;

        String goodItemName;
        String goodItemBrand;
        int goodItemQuantity;

        String orderInformation = "";
        notIncludedGoodsInformation = "";

        if(orderItems.length!=0)
        {
            for(OrderItem orderItem: orderItems)
            {
                orderItemName = orderItem.getName();
                orderItemBrand = orderItem.getBrand();
                orderItemQuantity = orderItem.getQuantity();

                String itemInformation = "";
                goodsPresence = false;

                for(Good good: goodsList)
                {
                    goodItemName = good.getName();
                    goodItemBrand = good.getBrand();
                    goodItemQuantity = good.getQuantity();

                    nameEquality = (orderItemName).equalsIgnoreCase(goodItemName);
                    brandEquality = (orderItemBrand).equalsIgnoreCase(goodItemBrand);
                    quantityEnoughness = orderItemQuantity <= goodItemQuantity;

                    goodsPresence = nameEquality && brandEquality && quantityEnoughness;

                    if(goodsPresence)
                    {
                        int id = good.getId();

                        orderItem.setGoodId(id);
                        includedOrderItems.add(orderItem);

                        Good requiredGood = goodsRepository.findById(id).get();
                        goodItemQuantity -= orderItemQuantity;
                        requiredGood.setQuantity(goodItemQuantity);
                        goodsRepository.save(requiredGood);

                        itemInformation += (orderItemName + " - " + orderItemBrand + " - " + orderItemQuantity);
                        orderInformation += itemInformation + "; ";

                        break;
                    }
                }

                if(!goodsPresence)
                {
                    if(!nameEquality)
                    {
                        notIncludedGoodsInformation += " Good "+orderItemName.toLowerCase()+" is absent in the good's database. ";
                    }
                    else if(!brandEquality)
                    {
                        notIncludedGoodsInformation += " Good " + orderItemName.toLowerCase() + " of brand " + orderItemBrand +
                                " is absent in the good's database. ";
                    }
                    else if(!quantityEnoughness)
                    {
                        notIncludedGoodsInformation += " Quantity of good " + orderItemName.toLowerCase() + " of brand "
                                + orderItemBrand + " in the good's database is less than quantity in the order. ";
                    }
                }
            }
            orderInformation = orderInformation.trim();
            int length = orderInformation.length();

            if(Character.toString(orderInformation.charAt(length-1)).equals(";"))
            {
                orderInformation = orderInformation.substring(0,length-1);
            }

            if(includedOrderItems.size()!=0)
            {
                order = new Order();
                order.setItems(orderInformation);

                LocalDateTime time = LocalDateTime.now();
                order.setCreationTime(time);
                order.setPaymentStatus("unpaid");

                ordersRepository.save(order);
                isOrderCreated = true;
                List<Order> ordersList = ordersRepository.findAll();
                int orderId = 0;

                for(Order order1: ordersList)
                {
                    if(order.equals(order1))
                    {
                        orderId = order1.getId();
                        break;
                    }
                }

                for(OrderItem orderItem: includedOrderItems)
                {
                    orderItem.setOrderId(orderId);
                    ordersItemsRepository.save(orderItem);
                }

                createdOrderId = orderId;
            }
            else
            {
                notIncludedGoodsInformation = "Unfortunately, Your order was not created, because no one items of goods " +
                        "in Your order is present in the database of goods.";
            }
        }
    }

    public String payOrder(int id)
    {
        String messageAboutPayment;
        try
        {
            Order order = ordersRepository.getReferenceById(id);

            if (order.getId() == id) {
                if (!order.getPaymentStatus().equals("paid")) {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime ordersCreationTime = order.getCreationTime();

                    if (now.isBefore(ordersCreationTime.plusMinutes(10))) {
                        order.setPaymentStatus("paid");
                        ordersRepository.save(order);
                        messageAboutPayment = "Order with id " + id + " was paid successfully.";
                    } else {
                        deleteUnpaidOrder(order, id);
                        messageAboutPayment = "Unfortunately, time for payment of order with id " + id + " was up" +
                                " (10 minutes after it's creation already passed)";
                    }
                } else {
                    messageAboutPayment = "Order with id " + id + " is already passed.";
                }
            } else {
                messageAboutPayment = "There is not order with id " + id + " in the database.";
            }
        }
        catch (EntityNotFoundException entityNotFoundException)
        {
            messageAboutPayment = "There is not order with id " + id + " in the database.";
        }
        catch (Exception exception)
        {
            messageAboutPayment = "Something went wrong with paying order with id " + id + ".";
        }
        return messageAboutPayment;
    }

    public String getNotIncludedGoodsInformation()
    {
        return this.notIncludedGoodsInformation;
    }

    public boolean ifOrderCreated()
    {
        return isOrderCreated;
    }

    public int getCreatedOrderId()
    {
        return createdOrderId;
    }

    private void deleteUnpaidOrders()
    {
        List<Order> orders = ordersRepository.findAll();
        List<Order> unpaidOrders = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for(Order order: orders)
        {
            LocalDateTime date = order.getCreationTime();
            if(!order.getPaymentStatus().equals("paid") && (date.plusMinutes(10)).isBefore(now))
            {
                unpaidOrders.add(order);
            }
        }

        for(Order order: unpaidOrders)
        {
            deleteUnpaidOrder(order, order.getId());
        }
    }

    private void deleteUnpaidOrder(Order order, int orderId)
    {
        List<OrderItem> ordersItems = ordersItemsRepository.findAll();
        List<OrderItem> requiredOrderItems = new ArrayList<>();
        for(OrderItem orderItem: ordersItems)
        {
            if(orderItem.getOrderId() == orderId)
            {
                requiredOrderItems.add(orderItem);
            }
        }
        List<Good> goods = goodsRepository.findAll();

        for(OrderItem required: requiredOrderItems)
        {
            int goodId = required.getGoodId();
            for(Good good: goods)
            {
                if(good.getId()==goodId)
                {
                    int goodsQuantity = good.getQuantity();
                    good.setQuantity(goodsQuantity + required.getQuantity());
                    goodsRepository.save(good);
                }
            }
            ordersItemsRepository.delete(required);
        }
        ordersRepository.delete(order);
    }
}
