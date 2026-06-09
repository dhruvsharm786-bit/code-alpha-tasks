import java.util.*;

public class StockTradingPlatform {

    // ---------------- STOCK CLASS ----------------

    static class Stock {

        private String symbol;
        private double price;

        public Stock(String symbol, double price) {
            this.symbol = symbol;
            this.price = price;
        }

        public String getSymbol() {
            return symbol;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    // ---------------- PORTFOLIO CLASS ----------------

    static class Portfolio {

        private HashMap<String, Integer> holdings =
                new HashMap<>();

        public void buy(String stock, int qty) {

            holdings.put(
                    stock,
                    holdings.getOrDefault(stock, 0) + qty
            );
        }

        public boolean sell(String stock, int qty) {

            if (!holdings.containsKey(stock))
                return false;

            int current = holdings.get(stock);

            if (current < qty)
                return false;

            holdings.put(stock, current - qty);

            if (holdings.get(stock) == 0)
                holdings.remove(stock);

            return true;
        }

        public HashMap<String, Integer> getHoldings() {
            return holdings;
        }

        public void displayPortfolio() {

            if (holdings.isEmpty()) {

                System.out.println(
                        "\nNo holdings available."
                );

                return;
            }

            System.out.println(
                    "\n----- PORTFOLIO -----"
            );

            for (String stock :
                    holdings.keySet()) {

                System.out.println(
                        stock
                                + " -> "
                                + holdings.get(stock)
                                + " shares"
                );
            }
        }
    }

    // ---------------- USER CLASS ----------------

    static class User {

        private String name;
        private double balance;

        private Portfolio portfolio;

        private ArrayList<String> history =
                new ArrayList<>();

        public User(
                String name,
                double balance
        ) {

            this.name = name;
            this.balance = balance;

            portfolio = new Portfolio();
        }

        public String getName() {
            return name;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(
                double balance
        ) {

            this.balance = balance;
        }

        public Portfolio getPortfolio() {
            return portfolio;
        }

        public void addTransaction(
                String transaction
        ) {

            history.add(transaction);
        }

        public void showHistory() {

            if (history.isEmpty()) {

                System.out.println(
                        "\nNo transactions yet."
                );

                return;
            }

            System.out.println(
                    "\n----- TRANSACTION HISTORY -----"
            );

            for (String t : history) {

                System.out.println(t);
            }
        }
    }

    // ---------------- MARKET CLASS ----------------

    static class Market {

        private HashMap<String, Stock> stocks =
                new HashMap<>();

        Random random =
                new Random();

        public Market() {

            stocks.put(
                    "AAPL",
                    new Stock("AAPL", 180)
            );

            stocks.put(
                    "TSLA",
                    new Stock("TSLA", 240)
            );

            stocks.put(
                    "GOOG",
                    new Stock("GOOG", 150)
            );

            stocks.put(
                    "MSFT",
                    new Stock("MSFT", 320)
            );
        }

        public void displayMarket() {

            System.out.println(
                    "\n----- MARKET DATA -----"
            );

            for (Stock stock :
                    stocks.values()) {

                System.out.println(
                        stock.getSymbol()
                                + " : ₹"
                                + String.format(
                                "%.2f",
                                stock.getPrice()
                        )
                );
            }
        }

        public Stock getStock(
                String symbol
        ) {

            return stocks.get(
                    symbol.toUpperCase()
            );
        }

        public void updatePrices() {

            for (Stock stock :
                    stocks.values()) {

                double change =
                        -5 + random.nextDouble() * 10;

                stock.setPrice(
                        Math.max(
                                10,
                                stock.getPrice()
                                        + change
                        )
                );
            }
        }

        public double calculatePortfolioValue(
                Portfolio portfolio
        ) {

            double total = 0;

            for (String symbol :
                    portfolio.getHoldings().keySet()) {

                Stock stock =
                        stocks.get(symbol);

                if (stock != null) {

                    total +=
                            stock.getPrice()
                                    * portfolio
                                    .getHoldings()
                                    .get(symbol);
                }
            }

            return total;
        }
    }

    // ---------------- TRADE ENGINE ----------------

    static class TradeEngine {

        public void buyStock(
                User user,
                Stock stock,
                int qty
        ) {

            if (qty <= 0) {

                System.out.println(
                        "Quantity must be greater than 0"
                );

                return;
            }

            double cost =
                    stock.getPrice() * qty;

            if (user.getBalance()
                    >= cost) {

                user.setBalance(
                        user.getBalance()
                                - cost
                );

                user.getPortfolio()
                        .buy(
                                stock.getSymbol(),
                                qty
                        );

                user.addTransaction(
                        "BOUGHT "
                                + qty
                                + " "
                                + stock.getSymbol()
                                + " @ ₹"
                                + String.format(
                                "%.2f",
                                stock.getPrice()
                        )
                );

                System.out.println(
                        "Purchase Successful"
                );

            } else {

                System.out.println(
                        "Insufficient Balance"
                );
            }
        }

        public void sellStock(
                User user,
                Stock stock,
                int qty
        ) {

            if (qty <= 0) {

                System.out.println(
                        "Quantity must be greater than 0"
                );

                return;
            }

            boolean sold =
                    user.getPortfolio()
                            .sell(
                                    stock.getSymbol(),
                                    qty
                            );

            if (sold) {

                double amount =
                        stock.getPrice() * qty;

                user.setBalance(
                        user.getBalance()
                                + amount
                );

                user.addTransaction(
                        "SOLD "
                                + qty
                                + " "
                                + stock.getSymbol()
                                + " @ ₹"
                                + String.format(
                                "%.2f",
                                stock.getPrice()
                        )
                );

                System.out.println(
                        "Sold Successfully"
                );

            } else {

                System.out.println(
                        "Not enough shares"
                );
            }
        }
    }

    // ---------------- MAIN METHOD ----------------

    public static void main(
            String[] args
    ) {

        try (
                Scanner sc =
                        new Scanner(System.in)
        ) {

            User user =
                    new User(
                            "Student",
                            100000
                    );

            Market market =
                    new Market();

            TradeEngine trade =
                    new TradeEngine();

            while (true) {

                market.updatePrices();

                System.out.println(
                        "\n=============================="
                );

                System.out.println(
                        " STOCK TRADING PLATFORM "
                );

                System.out.println(
                        "=============================="
                );

                System.out.println(
                        "Balance: ₹"
                                + String.format(
                                "%.2f",
                                user.getBalance()
                        )
                );

                System.out.println(
                        "\n1. Show Market"
                );

                System.out.println(
                        "2. Buy Stock"
                );

                System.out.println(
                        "3. Sell Stock"
                );

                System.out.println(
                        "4. View Portfolio"
                );

                System.out.println(
                        "5. Transaction History"
                );

                System.out.println(
                        "6. Net Worth"
                );

                System.out.println(
                        "7. Exit"
                );

                System.out.print(
                        "\nEnter Choice: "
                );

                int choice;

                try {

                    choice =
                            sc.nextInt();

                } catch (
                        InputMismatchException e
                ) {

                    System.out.println(
                            "Please enter a valid number."
                    );

                    sc.nextLine();

                    continue;
                }

                switch (choice) {

                    case 1:

                        market.displayMarket();

                        break;

                    case 2:

                        market.displayMarket();

                        System.out.print(
                                "Enter Symbol: "
                        );

                        String buySymbol =
                                sc.next();

                        System.out.print(
                                "Quantity: "
                        );

                        int buyQty =
                                sc.nextInt();

                        Stock buyStock =
                                market.getStock(
                                        buySymbol
                                );

                        if (buyStock != null)

                            trade.buyStock(
                                    user,
                                    buyStock,
                                    buyQty
                            );

                        else

                            System.out.println(
                                    "Stock Not Found"
                            );

                        break;

                    case 3:

                        market.displayMarket();

                        System.out.print(
                                "Enter Symbol: "
                        );

                        String sellSymbol =
                                sc.next();

                        System.out.print(
                                "Quantity: "
                        );

                        int sellQty =
                                sc.nextInt();

                        Stock sellStock =
                                market.getStock(
                                        sellSymbol
                                );

                        if (sellStock != null)

                            trade.sellStock(
                                    user,
                                    sellStock,
                                    sellQty
                            );

                        else

                            System.out.println(
                                    "Stock Not Found"
                            );

                        break;

                    case 4:

                        user.getPortfolio()
                                .displayPortfolio();

                        break;

                    case 5:

                        user.showHistory();

                        break;

                    case 6:

                        double portfolioValue =
                                market.calculatePortfolioValue(
                                        user.getPortfolio()
                                );

                        System.out.println(
                                "\nCash Balance: ₹"
                                        + String.format(
                                        "%.2f",
                                        user.getBalance()
                                )
                        );

                        System.out.println(
                                "Portfolio Value: ₹"
                                        + String.format(
                                        "%.2f",
                                        portfolioValue
                                )
                        );

                        System.out.println(
                                "Net Worth: ₹"
                                        + String.format(
                                        "%.2f",
                                        portfolioValue
                                                + user.getBalance()
                                )
                        );

                        break;

                    case 7:

                        System.out.println(
                                "Thank You For Using Stock Trading Platform!"
                        );

                        return;

                    default:

                        System.out.println(
                                "Invalid Choice"
                        );
                }
            }
        }
    }
}