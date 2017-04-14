import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SimpleBook implements Book {
    // we use priority queues for maintaining buys and sells in the 'correct' order
    private PriorityQueue<Order> buys = new PriorityQueue<>(new ComparatorBuys());
    private PriorityQueue<Order> sells = new PriorityQueue<>(new ComparatorSells());

    /**
     * Add an orde to the book. If we have an Iceberg order we also assign a new
     * timestamp to it.
     *
     * @param order order to be added to current order book
     */
    @Override
    public void addOrder(Order order) {
        if (order instanceof IcebergOrder) {
            ((IcebergOrder) order).setNewTimestamp();
        }

        if (order.getType() == 'B')
            buys.add(order);
        else
            sells.add(order);
    }

    // getters for top buy/sell

    @Override
    public Order getTopBuy(){
        return buys.peek();
    }

    @Override
    public Order getTopSell(){
        return sells.peek();
    }

    @Override
    public Order removeTopBuy(){
        if (buys.isEmpty())
            return null;
        else
            return buys.remove();
    }

    @Override
    public Order removeTopSell(){
        if (sells.isEmpty())
            return null;
        else
            return sells.remove();
    }


    // we may need to reupdate the top buy/sell (for example, when its timestamp has changed)

    @Override
    public void reinsertTopBuy(){
        if (!buys.isEmpty()){
            buys.add(buys.remove());
        }
    }

    @Override
    public void reinsertTopSell(){
        if (!sells.isEmpty()){
            sells.add(sells.remove());
        }
    }

    // comparators for buys and sells

    private class ComparatorSells implements Comparator<Order> {

        @Override
        public int compare(Order o1, Order o2) {
            if (o1.getPrice() == o2.getPrice())
                return Long.compare(o1.getTimestamp(), o2.getTimestamp());
            else
                return Integer.compare(o1.getPrice(), o2.getPrice());
        }
    }

    private class ComparatorBuys implements Comparator<Order> {

        @Override
        public int compare(Order o1, Order o2) {
            if (o1.getPrice() == o2.getPrice())
                return Long.compare(o1.getTimestamp(), o2.getTimestamp());
            else
                return -Integer.compare(o1.getPrice(), o2.getPrice());
        }
    }

    /*
    Id columns width (excluding formatting marks) = 10
    Volume columns width (excluding formatting marks) = 13
    Price columns width (excluding formatting marks) = 7
    Total width including 7 formatting marks = 67
     */

    /**
     * Creates a string of a given length using a given character
     *
     * @param c character used to build the new string
     * @param l length of the new string
     * @return string of length l that contains only c's
     */
    private String repeatedCharacter(char c, int l){
        return String.join("", Collections.nCopies(l, String.valueOf(c)));
    }

    // helper methods used to print numbers with and without marks

    private String printWithoutFormMarks(int number, int width){
        String n = String.valueOf(number);
        return repeatedCharacter(' ', width - n.length()) + n;
    }

    private String printWithFormMarks(int number, int width, int nb){
        DecimalFormat formatter;

        if (nb == 5)
            formatter = new DecimalFormat("##,###");
        else // nb == 7
            formatter = new DecimalFormat("#,###,###,###");

        String n = formatter.format(number);
        return repeatedCharacter(' ', width - n.length()) + n;
    }

    /**
     *
     * @return string representation of the current book
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("+----------+-------------+-------+-------+-------------+----------+\n");
        sb.append("| BUY                            | SELL                           |\n");
        sb.append("| Id       | Volume      | Price | Price | Volume      | Id       |\n");
        sb.append("+----------+-------------+-------+-------+-------------+----------+\n");

        PriorityQueue<Order> tempBuys = new PriorityQueue<>(buys);
        PriorityQueue<Order> tempSells = new PriorityQueue<>(sells);


        while (!tempBuys.isEmpty() && !tempSells.isEmpty()){
            Order buy = tempBuys.remove();
            Order sell = tempSells.remove();

            sb.append("|");

            sb.append(printWithoutFormMarks(buy.getOrderIdentifier(), 10));
            sb.append("|");
            sb.append(printWithFormMarks(buy.getQuantity(), 13, 7));
            sb.append("|");
            sb.append(printWithFormMarks(buy.getPrice(), 7, 5));
            sb.append("|");

            sb.append(printWithFormMarks(sell.getPrice(), 7, 5));
            sb.append("|");
            sb.append(printWithFormMarks(sell.getQuantity(), 13, 7));
            sb.append("|");
            sb.append(printWithoutFormMarks(sell.getOrderIdentifier(), 10));

            sb.append("|\n");
        }

        while (!tempBuys.isEmpty()){
            Order buy = tempBuys.remove();

            sb.append("|");

            sb.append(printWithoutFormMarks(buy.getOrderIdentifier(), 10));
            sb.append("|");
            sb.append(printWithFormMarks(buy.getQuantity(), 13, 7));
            sb.append("|");
            sb.append(printWithFormMarks(buy.getPrice(), 7, 5));

            sb.append("|       |             |          |\n");
        }

        while (!tempSells.isEmpty()){
            Order sell = tempSells.remove();

            sb.append("|          |             |       |");

            sb.append(printWithFormMarks(sell.getPrice(), 7, 5));
            sb.append("|");
            sb.append(printWithFormMarks(sell.getQuantity(), 13, 7));
            sb.append("|");
            sb.append(printWithoutFormMarks(sell.getOrderIdentifier(), 10));

            sb.append("|\n");
        }

        sb.append("+----------+-------------+-------+-------+-------------+----------+\n");

        return sb.toString();
    }
}
