/**
 * Abtract class Order that describes what fields and methods an order must have
 */
public abstract class Order{
    protected char type;
    protected int orderIdentifier;
    protected short price;
    protected int quantity;
    protected long timestamp;

    // getters for all fields

    public char getType() {
        return type;
    }
    public int getOrderIdentifier() {
        return orderIdentifier;
    }
    public short getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public long getTimestamp() { return timestamp; }

    /* two abstract methods that describe what should happen when the quantity of an
     *  order is decreased; both when the order is actually in the book and when it isn't
     */
    public abstract void decreaseOutsideBook(int amount);
    public abstract void decreaseInsideBook(int amount);
}
