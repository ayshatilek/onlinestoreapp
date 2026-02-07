import java.util.*;

public class OnlineStoreApp {
    public static void main(String[] args) {

        Order order = new Order();

        order.addItem(new OrderItem("Laptop", 1, 1200));
        order.addItem(new OrderItem("Mouse", 2, 50));

        IPayment payment = new CreditCardPayment();
        IDelivery delivery = new CourierDelivery();

        INotification notification = new EmailNotification();

        DiscountCalculator discountCalculator = new DiscountCalculator();
        discountCalculator.addRule(new PercentageDiscountRule(10));
        discountCalculator.addRule(new FixedDiscountRule(50));

        double totalPrice = order.calculateTotalPrice(discountCalculator);

        payment.processPayment(totalPrice);
        delivery.deliverOrder(order);
        notification.sendNotification("Order successfully processed. Total: $" + totalPrice);
    }
}

class Order {
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double calculateTotalPrice(DiscountCalculator calculator) {
        double sum = 0;
        for (OrderItem item : items) {
            sum += item.getTotalPrice();
        }
        return calculator.applyDiscount(sum);
    }
}

class OrderItem {
    private String name;
    private int quantity;
    private double price;

    public OrderItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public double getTotalPrice() {
        return quantity * price;
    }
}

interface IPayment {
    void processPayment(double amount);
}

class CreditCardPayment implements IPayment {
    public void processPayment(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card");
    }
}

class PayPalPayment implements IPayment {
    public void processPayment(double amount) {
        System.out.println("Paid $" + amount + " using PayPal");
    }
}

class BankTransferPayment implements IPayment {
    public void processPayment(double amount) {
        System.out.println("Paid $" + amount + " using Bank Transfer");
    }
}

interface IDelivery {
    void deliverOrder(Order order);
}

class CourierDelivery implements IDelivery {
    public void deliverOrder(Order order) {
        System.out.println("Order delivered by courier");
    }
}

class PostDelivery implements IDelivery {
    public void deliverOrder(Order order) {
        System.out.println("Order delivered by post service");
    }
}

class PickUpPointDelivery implements IDelivery {
    public void deliverOrder(Order order) {
        System.out.println("Order delivered to pickup point");
    }
}

interface INotification {
    void sendNotification(String message);
}

class EmailNotification implements INotification {
    public void sendNotification(String message) {
        System.out.println("Email notification: " + message);
    }
}

class SmsNotification implements INotification {
    public void sendNotification(String message) {
        System.out.println("SMS notification: " + message);
    }
}

interface DiscountRule {
    double apply(double price);
}

class PercentageDiscountRule implements DiscountRule {
    private double percent;

    public PercentageDiscountRule(double percent) {
        this.percent = percent;
    }

    public double apply(double price) {
        return price - (price * percent / 100);
    }
}

class FixedDiscountRule implements DiscountRule {
    private double discount;

    public FixedDiscountRule(double discount) {
        this.discount = discount;
    }

    public double apply(double price) {
        return price - discount;
    }
}

class DiscountCalculator {
    private List<DiscountRule> rules = new ArrayList<>();

    public void addRule(DiscountRule rule) {
        rules.add(rule);
    }

    public double applyDiscount(double price) {
        double result = price;
        for (DiscountRule rule : rules) {
            result = rule.apply(result);
        }
        return Math.max(result, 0);
    }
}
