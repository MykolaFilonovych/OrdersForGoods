package TestOrderCreating;

import main.businessLogic.OrdersInformationGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TestOrderCreating
{
    private final OrdersInformationGenerator ordersInformationGenerator;

    public TestOrderCreating()
    {
        ordersInformationGenerator = new OrdersInformationGenerator();
    }

    @Test
    public void Test1()
    {
        String expectedOrder = "Order includes the following good: Mixer Zelmer 2 units.";
        String testedGoodsInformation = "{{name:Mixer, brand:Zelmer}:2}";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertEquals(actualOrder, expectedOrder);
    }

    @Test
    public void Test2()
    {
        String expectedOrder = "Order includes the following goods: Electrical kettle Vitek 3 units, and " +
                "Waffle maker Tefal 2 units.";
        String testedGoodsInformation = "{{name:Electrical kettle, brand:Vitek}:3,{name:Waffle maker, brand:Tefal}:2}";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertEquals(actualOrder, expectedOrder);
    }

    @Test
    public void Test3()
    {
        String expectedOrder = "Order includes the following goods: Electrical kettle Vitek 3 units, " +
                "Waffle maker Tefal 2 units, and Juicer Scarlett 4 units.";
        String testedGoodsInformation = "{{name:Electrical kettle, brand:Vitek}:3,{name:Waffle maker, brand:Tefal}:2," +
                "{name:Juicer, brand:Scarlett}:4}";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertEquals(actualOrder, expectedOrder);
    }

    @Test
    public void Test4()
    {
        String expectedOrder = "Order includes the following goods: Electrical kettle Vitek 3 units, " +
                "Waffle maker Tefal 2 units, Meat grinder Zelmer 1 unit, and Juicer Scarlett 4 units.";
        String testedGoodsInformation = "{{name:Electrical kettle, brand:Vitek}:3,{name:Waffle maker, brand:Tefal}:2," +
                "{name:Meat grinder, brand:Zelmer}:1,{name:Juicer, brand:Scarlett}:4}";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertEquals(actualOrder, expectedOrder);
    }

    @Test
    public void Test5()
    {
        String expectedOrder = "Order has invalid format, that's quantities of opening and closing braces are different.";
        String testedGoodsInformation = "{{name:Electrical kettle, brand:Vitek}:3,name:{Waffle maker, brand:Tefal}:2," +
                "{name:Meat grinder, brand:Zelmer}:1,{name:Juicer, brand:Scarlett}:4";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertEquals(actualOrder, expectedOrder);
    }

    @Test
    public void Test6()
    {
        String expectedOrder = "Blank order cannot be created!";
        String testedGoodsInformation = "{}";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertEquals(actualOrder, expectedOrder);
    }

    @Test
    public void Test7()
    {
        String expectedOrder = "Blank order cannot be created!";
        String testedGoodsInformation = "  {    }    ";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertEquals(actualOrder, expectedOrder);
    }

    @Test
    public void Test8()
    {
        String expectedOrder = "Order has invalid format";
        String testedGoodsInformation = "{{name:, brand:}:3}";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertTrue(actualOrder.contains(expectedOrder));
    }

    @Test
    public void Test9()
    {
        String expectedOrder = "Order has invalid format";
        String testedGoodsInformation = "{{Electrical kettle, :Vitek}:,{name:maker, brand:}:2\" +\n" +
                "                \"{name:Juicer, brand:Scarlett}:4}";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertTrue(actualOrder.contains(expectedOrder));
    }

    @Test
    public void Test10()
    {
        String expectedOrder = "Order has invalid format";
        String testedGoodsInformation =
                "{{name:Electrical kettle, brand:Vitek}:,{name:Waffle maker, brand:Tefal}:," +
                " {name:Juicer, brand:Scarlett}:}";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertTrue(actualOrder.contains(expectedOrder));
    }

    @Test
    public void Test11()
    {
        String expectedOrder = "Order has invalid format";
        String testedGoodsInformation =
                "{{name:Electrical kettle, },{name:Waffle maker}:," +
                        " {brand:Scarlett}:}";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertTrue(actualOrder.contains(expectedOrder));
    }

    @Test
    public void Test12()
    {
        String expectedOrder = "Order has invalid format";
        String testedGoodsInformation =
                "{{name:, brand:}:,{name:, brand:}:," +
                        " {name:, brand:}:}";
        String actualOrder = ordersInformationGenerator.getOrderForGoods(testedGoodsInformation);

        Assertions.assertTrue(actualOrder.contains(expectedOrder));
    }
}
