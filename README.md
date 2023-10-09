# OrdersForGoods
In this project developed REST API for creating orders for some goods.

Data for connecting to the database of application are located in the file application.properties in the folder "src/main/resources".

- For viewing available goods in the database, use the following command: localhost:8080/availableGoods.

- For adding new good to the database, use the following command: localhost:8080/addGood/{name}/{brand}/{price}/{quantity}
    (For example, localhost:8080/addGood/Blender/Zelmer/370/50. Firstly You should point name of good, then brand, then price(only in whole numbers, that's integer), and then quantity of good.
    Inputting non-numerical data, You don't need to put them in quotes(singular or double). If the good is already present in the database, it's quantity will be increased by quantity, pointed in the request, 
    but it's price will be changed, if by adding present good it's price in the request is different. If You put non-numerical data where numerical data must be (these are price and quantity), an error will occur.)

- For creating an order for goods, use the following command: localhost:8080/createOrder/{goodsInformation}
    ({goodsInformation} must have the following format:{{name:NameOfGood,brand:BrandOfGood}:number}, inputting non-numerical data, You don't need to put them in quotes(singular or double).
    You can also place multiple goods to the order, different goods must be divided by coma, for example:{{name:NameOfGood1,brand:BrandOfGood1}:number1,{name:NameOfGood2,brand:BrandOfGood2}:number2}
    Examples of requests for creating orders:
      localhost:8080/createOrder/{{name:Mixer,brand:Scarlett}:1}}
      localhost:8080/createOrder/{{name:Juicer,brand:Tefal}:2,{name:Waffle maker,brand:Zelmer}:2}
      localhost:8080/createOrder/{{name:Electrical kettle,brand:Vitek}:3,{name:Coffee machine,brand:Philips}:3,{name:Meat grinder,brand:Scarlett}:3}
    
    By choosing goods, You should those of them, which are available in the database, they can be viewed by mentioned command for viewing available goods, otherwise those goods, which are not present in the database,
    will not be included in the order.
    If after attempt to create an order You will see, that Your order is created, and has an id, You should memorise it's id and pay for it during 10 minutes after it's creating, using the command below for paying
    orders, otherwise it will be deleted. If You violate the format of goodsInformation, the message about it will occur.)

- For paying Your order, use the following command:
    localhost:8080/payOrder/{orderId}
    After successful creating an order, You should memorize it's id for paying for it. After paying Your order it will be marked as paid.
    Examples of requests for paying order:
      localhost:8080/payOrder/24
      localhost:8080/payOrder/25
    If Your order was paid successfully, You will see the message about it. In case of attempt to pay for paid order, the message will occur, that this order is already paid. If You attempt to pay for Your order
    after 10 minutes of it's creating, there will be the message, that time for order's paying is up. If You try to pay for not existing order, You will see the message, that there is no order with pointed id. 

Application is able to delete unpaid orders after 10 minutes of their creating. For this in class OrdersService.java created method deleteUnpaidOrders(), which is called at the time of initializing of 
OrdersService object and calling method createOrder(OrderItem[] orderItems) in OrdersService class. Unfortunately, I could not to do so that database could delete unpaid orders itself.



