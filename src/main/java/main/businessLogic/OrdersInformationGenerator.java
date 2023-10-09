package main.businessLogic;

import main.entities.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrdersInformationGenerator
{
    private OrderItem[] orderItems;

    public OrdersInformationGenerator()
    {

    }

    private boolean correctBracesOrdering(String ordersInformation)
    {
        int lengthOfString = ordersInformation.length();
        String bracesOrdering = "";
        for(int i = 0; i < lengthOfString; i++)
        {
            if(Character.toString(ordersInformation.charAt(i)).equals("{"))
            {
                bracesOrdering += "{";
            }
            else if(Character.toString(ordersInformation.charAt(i)).equals("}"))
            {
                bracesOrdering += "}";
            }
        }
        while(bracesOrdering.contains("{}"))
        {
            bracesOrdering = bracesOrdering.replace("{}", "");
        }
        return bracesOrdering.equals("");
    }

    private boolean orderIsNotEmpty(String ordersInformation)
    {
        ordersInformation = ordersInformation.trim();
        int lengthOfString = ordersInformation.length();
        String ordersContent = "";
        for(int i = 0; i < lengthOfString; i++)
        {
            String currentCharacter = Character.toString(ordersInformation.charAt(i));
            if(!currentCharacter.equals("{") && !currentCharacter.equals("}"))
            {
                ordersContent += currentCharacter;
            }
        }
        ordersContent = ordersContent.trim();
        return !(ordersContent.equals(""));
    }

    private String getOrderItemsAndUnitsQuantities(String ordersInformation)
    {
        int lengthOfInformation = ordersInformation.length();
        int correctnessOnBraces = 0;
        String orderItems = "";
        String unitsQuantities = "";
        int colonOccurrence = 0;

        for(int i = 0; i < lengthOfInformation; i++)
        {
            String occurrence = Character.toString(ordersInformation.charAt(i));
            if(occurrence.equals("{"))
            {
                correctnessOnBraces++;
            }
            if(occurrence.equals("}"))
            {
                correctnessOnBraces--;
                if(correctnessOnBraces==1)
                {
                    orderItems += ";";
                }
            }

            if((correctnessOnBraces==2) && !occurrence.equals("{"))
            {
                orderItems += occurrence;
            }

            if(colonOccurrence == 1 && !occurrence.equals(",") && correctnessOnBraces==1)
            {
                unitsQuantities += occurrence;
            }
            else if(colonOccurrence!=0)
            {
                unitsQuantities += ";";
                colonOccurrence--;
            }

            if((correctnessOnBraces==1) && occurrence.equals(":"))
            {
                colonOccurrence++;
            }
        }

        return orderItems+"&"+unitsQuantities;
    }

    private String buildOrderForGoods(String[] orderItem, String[] unitsQuantity)
    {
        String informationAboutOrder = "";
        orderItems = new OrderItem[orderItem.length];

        List<Integer> unitsOfGoods = new ArrayList<>();
        for (String s : unitsQuantity) {
            unitsOfGoods.add(Integer.parseInt(s));
        }

        for(int i = 0; i < orderItem.length; i++)
        {
            orderItems[i] = new OrderItem();

            String[] subitems = orderItem[i].split(",");

            int parameterSequence = 0;
            for(String subitem: subitems)
            {
                String parameter = subitem.split(":")[1];
                informationAboutOrder += (parameter + " ");
                if(parameterSequence == 0)
                {
                    orderItems[i].setName(parameter);
                }
                else
                    {
                        orderItems[i].setBrand(parameter);
                    }
                parameterSequence++;
            }
            int unitsOfGood = unitsOfGoods.get(i);
            orderItems[i].setQuantity(unitsOfGood);

            informationAboutOrder += unitsOfGood + ((unitsOfGood==1) ? " unit" : " units");

            if(i < orderItem.length-1)
            {
                if((orderItem.length >= 3) && (i < orderItem.length-2))
                {
                    informationAboutOrder += ", ";
                }
                else
                {
                    informationAboutOrder += ", and ";
                }
            }
            else
            {
                informationAboutOrder += ".";
            }
        }
        return informationAboutOrder;
    }

    public String getOrderForGoods(String ordersInformation)
    {
        String order = "Order includes the following good";

        try
        {
            boolean ordersIsNotBlank = orderIsNotEmpty(ordersInformation);
            if (correctBracesOrdering(ordersInformation) && ordersIsNotBlank)
            {
                String orderItemsAndUnitsQuantities = getOrderItemsAndUnitsQuantities(ordersInformation);
                if (!orderItemsAndUnitsQuantities.trim().equals("&")) {
                    String[] ordersParameters = orderItemsAndUnitsQuantities.split("&");
                    String orderItems = ordersParameters[0];
                    String unitsQuantities = ordersParameters[1];

                    if (Character.toString(orderItems.charAt(orderItems.length() - 1)).equals(";")) {
                        orderItems = orderItems.substring(0, orderItems.length() - 1);
                    }
                    if (Character.toString(unitsQuantities.charAt(unitsQuantities.length() - 1)).equals(";")) {
                        unitsQuantities = unitsQuantities.substring(0, unitsQuantities.length() - 1);
                    }

                    String[] orderItem = orderItems.split(";");
                    String[] unitsQuantity = unitsQuantities.split(";");

                    if (orderItem.length > 1) {
                        order += "s";
                    }
                    order += ": ";

                    order += buildOrderForGoods(orderItem, unitsQuantity);
                } else {
                    order = "Blank order cannot be created!";
                }
            }
            else if (!ordersIsNotBlank || (ordersInformation.trim()).equals("{}"))
            {
                order = "Blank order cannot be created!";
            }
            else
                {
                    order = "Order has invalid format, that's quantities of opening and closing braces are different.";
                }
        }
        catch (Exception exception)
        {
            order = "Order has invalid format.";
        }
        return order;
    }

    public OrderItem[] getOrderItems()
    {
        return this.orderItems;
    }
}
