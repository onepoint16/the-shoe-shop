/**_____                    ______      ____            __             
  / ___/____ _____ ___     / ____/___ _/ / /___ _____ _/ /_  ___  _____
  \__ \/ __ `/ __ `__ \   / / __/ __ `/ / / __ `/ __ `/ __ \/ _ \/ ___/
 ___/ / /_/ / / / / / /  / /_/ / /_/ / / / /_/ / /_/ / / / /  __/ /    
/____/\__,_/_/ /_/ /_/   \____/\__,_/_/_/\__,_/\__, /_/ /_/\___/_/     
                                              /____/                   
*/
package the.shoe.shop;

import java.util.ArrayList;

/**
 * @author Sam Gallagher 30263952
 * This is the Stock class page used to create stock objects to be added to a user generated shopping list.
 * The class page contains methods to calculate totals to display to the user as they shop.
 */
public class Stock {
    /**
     * Fields
     * Integer stockID will produce a number to identify the item in the Stock access database page.
     * String productName will give a name to match or create an entry in the Stock access database page.
     * Double productCost will give a decimal cost either read from or entered into the Stock access database page.
     * Integer quantity will be inputed by the user and used to perform total cost calculations
     * Double subTotal, VAT and total will be used to display the cost to the user as items are added.
     */
    private int stockID;
    private String productName;
    private double productCost;
    private double subTotal;
    private double VAT;
    private double total;
    ArrayList<Double> shoppingList = new ArrayList<>();
    
    /**
     * Constructors
     * Used to create Stock objects
     * First constructor is empty
     * Second constructor is an overloaded constructor with 3 arguments being required
     */
    
    public Stock(){}
    
    public Stock (int stockID, String productName, double productCost){
        
        this.stockID = stockID;
        this.productName = productName;
        this.productCost = productCost;
    }

    /**
     * Getters
     * 4 getters used to retrieve information regarding a Stock object
     * From call they will return:
     * @return stockID, productName, productCost and the contents of the ArrayList 
     */
    
    public int getStockID() {
        return stockID;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductCost() {
        return productCost;
    }

    public ArrayList<Double> getshoppingList() {
        return shoppingList;
    }

    /**
     * Setters
     * 4 setters used to set the field for:
     * @param stockID, productName, productCost and to add items to the ArrayList
     */
    
    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductCost(double productCost) {
        this.productCost = productCost;
    }

    public void setshoppingList(ArrayList<Double> shoppingList) {
        this.shoppingList = shoppingList;
    }
    
    /**
     * Methods
     * The method addToShoppingList is used to calculate the cost for the item times the quantity selected by the user
     * The method takes in the quantity and cost of the item and following the calculation the cost is added tot he list
     * This is then used to do final calculations in regards to the users orders
     * @param itemCost
     * @param itemQuantity 
     */
    public void addToShoppingList(Double itemCost, int itemQuantity){
        Double i = itemCost * itemQuantity;
        shoppingList.add(i);
    }
    /**
     * The method CalcSubTotal is used to calculate the subtotal of all the item costs in the shoppingList
     * As the user adds items tot he shoppingList the subtotal will calculate the number of entries and then using a for loop
     * the subTotal will be calculated by adding each entry to subTotal
     * @return subTotal is then returned to be displayed 
     */
    public double calcSubTotal(){
        
        
        int countDown = shoppingList.size();

        this.subTotal = 0.00;
       
        
        for (int i = 0; i < countDown; i++){
            double itemCost = shoppingList.get(i);

            this.subTotal = this.subTotal + itemCost;

        }
        return this.subTotal;
    }
    /**
     * The method calcVAT is used to calculate the ongoing VAT applied to the users sale
     * The method takes the subTotal calculated in the method above and times it by 0.2 which in turn will produce the 20% VAT applied
     * @return the VAT is returned to be displayed to the user
     */
    public double calcVAT(){
        VAT = this.subTotal * 0.2;
        return VAT;
    }
    /**
     * The method calcTotal is used to calculate the final total of the sale
     * The method takes the previously calculated subtotal and the VAT applied to it. The method then adds these together to create the final total
     * @return the total to be displayed to the user
     */
    public double calcTotal(){
        this.total = this.subTotal + this.VAT;
        return this.total;
    }
    /**
     * The method clearShoppingList is used to clear the shoppingList. This might occur mid use as the user no longer wishes to proceed with the sale
     * By using this method all entries in the list will be gone and the subtotal, VAT and total will be reset
     */
    public void clearShoppingList(){
        shoppingList.removeAll(shoppingList);
    }

    /**
     * 
     */
    public void removeLastFromShoppingList(){
        shoppingList.remove(shoppingList.size() - 1);
    }    
}
