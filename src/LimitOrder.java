public class LimitOrder extends Order {
    public LimitOrder(char type, int orderIdentifier, short price, int quantity) {
        this.type = type;
        this.orderIdentifier = orderIdentifier;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = System.currentTimeMillis(); // assign a timestamp when the order is created
    }

    /* We have to provide implementations for both abstract functions declared in Order
     *  Note: LimitOrder performs the same in both cases (both in the book and outside of it)
     */

    @Override
    public void decreaseOutsideBook(int amount) {
        assert quantity >= amount;
        quantity -= amount;
    }

    @Override
    public void decreaseInsideBook(int amount) {
        decreaseOutsideBook(amount);
    }
}
