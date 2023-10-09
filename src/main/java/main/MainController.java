package main;

import main.businessLogic.OrdersInformationGenerator;
import main.entities.Good;
import main.entities.OrderItem;
import main.services.GoodsService;
import main.services.OrdersService;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class MainController
{
    private final OrdersInformationGenerator ordersInformationGenerator;
    private final GoodsService goodsService;
    private final OrdersService ordersService;

    @Autowired
    public MainController(GoodsService goodsService, OrdersService ordersService)
    {
        this.ordersInformationGenerator = new OrdersInformationGenerator();
        this.goodsService = goodsService;
        this.ordersService = ordersService;
    }

    @RequestMapping(path = "/addGood/{name}/{brand}/{price}/{quantity}")
    public String addGood(@PathVariable String name, @PathVariable String brand, @PathVariable int price, @PathVariable int quantity)
    {
        Good good = new Good();
        try {
            good.setName(name);
            good.setBrand(brand);
            good.setPrice(price);
            good.setQuantity(quantity);
            goodsService.addGood(good);
            return ("You have just added the following good: " + name + " of brand " + brand + ", its price is "
                    + price + ", and quantity is " + quantity + ".");
        }
        catch (TypeMismatchException typeMismatchException)
        {
            typeMismatchException.printStackTrace();
            return "Your good was not saved to the database, because types mismatch of parameters occurred.";
        }
        catch (NumberFormatException numberFormatException)
        {
            numberFormatException.printStackTrace();
            return "Your good was not saved to the database, because wrong format of numerical parameters occurred.";
        }
        catch (Exception exception)
        {
            System.out.println(exception.getClass());
            exception.printStackTrace();
            return "Your good was not saved to the database, because something went wrong...";
        }
    }

    @RequestMapping(path = "/availableGoods")
    public List<Good> availableGoods()
    {
        return goodsService.getAvailableGoods();
    }

    @RequestMapping(path = "/createOrder/{goodsInformation}")
    public String createOrderForGoods(@PathVariable String goodsInformation)
    {
        String informationAboutOrder = ordersInformationGenerator.getOrderForGoods(goodsInformation);
        if(!informationAboutOrder.equals("Blank order cannot be created!") &&
                !informationAboutOrder.contains("Order has invalid format"))
        {
            OrderItem[] orderItems = ordersInformationGenerator.getOrderItems();
            ordersService.createOrder(orderItems);
            String missingItemsInformation = ordersService.getNotIncludedGoodsInformation();

            if(ordersService.ifOrderCreated())
            {
                if(!missingItemsInformation.trim().equals(""))
                {
                    informationAboutOrder += " But there were such goods, which were not included to the order. ";
                    informationAboutOrder += missingItemsInformation;
                }
                if(orderItems.length!=0)
                {
                    informationAboutOrder += " Your order has id " + ordersService.getCreatedOrderId() +
                            ". You should pay for it during 10 minutes, because otherwise it will be deleted.";
                }
            }
            else
            {
                informationAboutOrder = missingItemsInformation;
            }
        }

        return informationAboutOrder;
    }

    @RequestMapping(path = "/payOrder/{orderId}")
    public String payOrder(@PathVariable int orderId)
    {
        return ordersService.payOrder(orderId);
    }
}
