package main.services;

import main.entities.Good;
import main.repositories.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class GoodsService
{
    private final GoodsRepository goodsRepository;

    @Autowired
    public GoodsService(GoodsRepository goodsRepository)
    {
        this.goodsRepository = goodsRepository;
    }

    public List<Good> getAvailableGoods()
    {
        Comparator<Good> comparator = Comparator.comparingInt(Good::getId);
        List<Good> availableGoods = goodsRepository.findAll();
        availableGoods.sort(comparator);
        return availableGoods;
    }

    public void addGood(Good good)
    {
        List<Good> availableGoods = getAvailableGoods();
        boolean containsGood = false;
        int id = 0;

        for(Good item: availableGoods)
        {
            if(item.getName().equals(good.getName()) && item.getBrand().equals(good.getBrand()))
            {
                id = good.getId();
                containsGood = !containsGood;
                break;
            }
        }

        if(!containsGood)
        {
            goodsRepository.save(good);
        }
        else
            {

                Good good1 = goodsRepository.findById(id).get();

                if(good1.getPrice() != good.getPrice())
                {
                    good1.setPrice(good.getPrice());
                }

                int quantity = good1.getQuantity();
                quantity += good.getQuantity();
                good1.setQuantity(quantity);

                goodsRepository.save(good1);
            }
    }
}
