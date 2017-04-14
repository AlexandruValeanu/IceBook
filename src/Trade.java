public class Trade {
    private int buyOrderId;
    private int sellOrderId;
    private short price;
    private int quantity;

    public Trade(int buyOrderId, int sellOrderId, short price, int quantity) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Increase the total quantity of the current trade
     *
     * @param amount volume to be added to current trade
     */
    public void updateQuantity(int amount){
        this.quantity += amount;
    }

    // getters
    public int getBuyOrderId() {
        return buyOrderId;
    }
    public int getSellOrderId() {
        return sellOrderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trade trade = (Trade) o;

        if (buyOrderId != trade.buyOrderId) return false;
        return sellOrderId == trade.sellOrderId;
    }

    @Override
    public int hashCode() {
        int result = buyOrderId;
        result = 31 * result + sellOrderId;
        return result;
    }

    @Override
    public String toString() {
        return buyOrderId + "," + sellOrderId + "," + price + "," + quantity;
    }
}
