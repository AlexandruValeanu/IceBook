public class IcebergOrder extends Order {
    /** We need two new fields:
     *     peakSize: the maximum volume visible to the book
     *     initPeakSize: constant variable equal to the original peakSize
     */
    private int peakSize;
    private final int initPeakSize;

    public IcebergOrder(char type, int orderIdentifier, short price, int quantity, int peakSize) {
        this.type = type;
        this.orderIdentifier = orderIdentifier;
        this.price = price;
        this.quantity = quantity - peakSize; // realQuantity = quantity + peakSize
        this.peakSize = peakSize;
        this.initPeakSize = peakSize;
        this.timestamp = System.currentTimeMillis(); // assign a timestamp when the order is created
    }

    /**
     * We assign a new timestamp to the order
     * We need this function when an Inceberg Order is inserted into the book or in @decreaseInsideBook
     */
    public void setNewTimestamp(){
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public int getQuantity(){
        return peakSize; // do not expose the actual volume of the order (just the peakSize)
    }

    /**
     * Before the order enters the book we first try to decrease it's quantity (and peakSize if it was not
     * enough)
     */
    @Override
    public void decreaseOutsideBook(int amount) {
        assert quantity + peakSize >= amount;

        int maxPossible = Math.min(quantity, amount);
        quantity -= maxPossible;
        amount -= maxPossible;
        peakSize -= amount;
    }

    /**
     * After the order entered the book we first try to decrease it's peakSize (and quantity if it was
     * not enough and we need to recreate the peakSize)
     * If we recreate the peakSize we must assign a new timestamp using @setNewTimestamp
     */
    @Override
    public void decreaseInsideBook(int amount) {
        assert peakSize >= amount;
        peakSize -= amount;

        if (peakSize == 0){
            peakSize = Math.min(initPeakSize, quantity);
            quantity -= peakSize;
            setNewTimestamp();
        }
    }
}
