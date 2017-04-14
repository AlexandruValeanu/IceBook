import java.util.ArrayList;
import java.util.List;

public class MatchingEngine {
    private Book book = new SimpleBook(); // order book
    private List<Trade> trades = new ArrayList<>(); // list with all trades

    /**
     * Adds a new order to the book after the order was matched against the order book (if possible)
     *
     * @param order order to be inserted
     */
    public void addOrder(Order order){
        if (order == null)
            throw new IllegalArgumentException(); // runtime error (should not happen)

        tryMatching(order);
    }

    /**
     * Adds a new trade to the list of trades or increases the already existing trade's volume by quantity
     *
     * @param buyID    buy order id matched
     * @param sellID   sell order id matched
     * @param price    price in pence
     * @param quantity quantity of the trade
     */
    private void addTrade(int buyID, int sellID, short price, int quantity){
        Trade newTrade = new Trade(buyID, sellID, price, quantity);

        for (Trade trade: trades){
            if (trade.equals(newTrade)){ // we already have (buyId, sellId) in the list of trades
                trade.updateQuantity(quantity); // we just increase the trade's volume
                return;
            }
        }

        trades.add(newTrade); // insert the newly created trade (which did't exist before this operation)
    }

    /**
     *
     * @param ID      ID of order that has to be removed from the collection of trades
     * @param isBuyID true if ID corresponds to a by trade or false otherwise
     */
    private void printAllTradesWithID(int ID, boolean isBuyID){
        for (Trade trade: trades){
            if (isBuyID){
                if (trade.getBuyOrderId() == ID)
                    System.out.println(trade);
            }
            else{
                if (trade.getSellOrderId() == ID)
                    System.out.println(trade);
            }
        }

        // filter out all trades that have just been printed to stout
        trades.removeIf(trade -> {
            if (isBuyID){
                return trade.getBuyOrderId() == ID;
            }
            else{
                return trade.getSellOrderId() == ID;
            }
        });
    }

    /**
     * Remove all the satisfied (quantity == 0) orders from the book and print associated trades
     */
    private void clearTerminatedOrders(){
        while (true){
            Order order = book.getTopBuy();

            if (order == null || order.getQuantity() > 0) // no more orders or current one is not yet satisfied
                break;

            book.removeTopBuy();
            // print trades associated with order.ID
            printAllTradesWithID(order.getOrderIdentifier(), true);
        }

        while (true){
            Order order = book.getTopSell();

            if (order == null || order.getQuantity() > 0) // no more orders or current one is not yet satisfied
                break;

            book.removeTopSell();
            // print trades associated with order.ID
            printAllTradesWithID(order.getOrderIdentifier(), false);
        }
    }

    /**
     * Try to match the order against the order book and update orders, book and list of trades accordingly
     *
     * @param order order to be matched againts the order book
     */
    private void tryMatching(Order order){
        boolean iterate = true;

        while (iterate && order.getQuantity() > 0) {
            iterate = false;

            Order buy, sell;

            // populate buy and sell accordingly
            if (order.getType() == 'B'){ // current order is a buy
                buy = order;
                sell = book.getTopSell(); // we need the top sell from the book
            }
            else{ // current order is a sell
                buy = book.getTopBuy(); // we need the top buy from the book
                sell = order;
            }

            if (buy != null && sell != null) { // both are valid
                if (buy.getPrice() >= sell.getPrice()){ // can the be matched?
                    short tradePrice = sell.getPrice();
                    int tradeQuantity = Math.min(buy.getQuantity(), sell.getQuantity());
                    assert tradeQuantity > 0;

                    // add current trade to the trade list (or update some existing trade)
                    addTrade(buy.getOrderIdentifier(), sell.getOrderIdentifier(), tradePrice, tradeQuantity);

                    if (buy == order) // buy is not in the book
                        buy.decreaseOutsideBook(tradeQuantity);
                    else { // buy is in the book
                        buy.decreaseInsideBook(tradeQuantity);
                        book.reinsertTopBuy(); // we need to reinsert it (maybe the timestamp has changed)
                    }

                    if (sell == order) // sell is not in the book
                        sell.decreaseOutsideBook(tradeQuantity);
                    else {
                        sell.decreaseInsideBook(tradeQuantity);
                        book.reinsertTopSell(); // we need to reinsert it (maybe the timestamp has changed)
                    }

                    clearTerminatedOrders(); // clear all finished orders from the book
                    iterate = true; // try to match again
                }
            }
        }

        book.addOrder(order); // add order to the book
        clearTerminatedOrders(); // clear all finished orders from the book (the previous line may have inserted an
                                 // already satisfied order to the book
    }

    @Override
    public String toString(){
        return book.toString(); // actually print the order book
    }
}
