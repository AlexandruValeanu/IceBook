/**
 * Interface that describes what methods a concrete implementation of an order book must have
 */
public interface Book {
    public void addOrder(Order order);
    public Order getTopBuy();
    public Order getTopSell();
    public Order removeTopBuy();
    public Order removeTopSell();
    public void reinsertTopBuy();
    public void reinsertTopSell();
}
