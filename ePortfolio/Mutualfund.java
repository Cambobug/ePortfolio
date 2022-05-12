package ePortfolio;

public class Mutualfund extends Investment{
    
    public Mutualfund()
    {

    }

    /**
     * Constructor used to create a mutualfund object, uses the superclasses investment constructor and methods for manipulation
     * @param symbol
     * @param name
     * @param quantity
     * @param price
     * @param bookValue
     */
    public Mutualfund(String symbol, String name, int quantity, double price, double bookValue) throws Exception
    {
        super(symbol, name, quantity, price, bookValue);
    }

    @Override
    public double calcBookValue(int addedQuantity, double newPrice) // used to calculate the book value of a mutulafund during purchase
    {
        return getInvestBookValue() + (newPrice * addedQuantity);
    }
    @Override
    public  double calcGain() // used to calculate gain of a specific investment
    {
        return (getInvestPrice() * getInvestQuantity()) - getInvestBookValue();
    }
    @Override
    public  double calcPayment(int soldQuantity) // used to calculate payment of a mutualfund back to the buyer during selling
    {
        return (getInvestPrice() * soldQuantity) - mutualSellCom;
    }
}
