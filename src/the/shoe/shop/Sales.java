/* _____                    ______      ____            __             
  / ___/____ _____ ___     / ____/___ _/ / /___ _____ _/ /_  ___  _____
  \__ \/ __ `/ __ `__ \   / / __/ __ `/ / / __ `/ __ `/ __ \/ _ \/ ___/
 ___/ / /_/ / / / / / /  / /_/ / /_/ / / / /_/ / /_/ / / / /  __/ /    
/____/\__,_/_/ /_/ /_/   \____/\__,_/_/_/\__,_/\__, /_/ /_/\___/_/     
                                              /____/                   
*/
package the.shoe.shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *@author Sam Gallagher 30263952
 * This class page it to perform sales function in relation to the signed in user.
 * There will be static methods called to perform calculations to display highest sale, lowest sale, average sale and total number of sales.
 * This will 
 */
public class Sales {

    /**
     * Fields
     * Integer salesID will be used to access information in the Sales table within the SamEposDb file.
     * Integer userID will be used to access information in the Sales table within the SamEposDb file.
     * Double salesTotal will be used to add information in the Sales table within the SamEposDb file.
     * ArrayList userTotalSales will be used to add sales from the Sales table for a specific userId this will then be used for calculations to display information to the user.
     * Integer totalSales will display the total number of sales for a user stored in the userTotalSales list.
     * Double highestSale will calculate the highest sale within the userTotalSales list.
     * Double lowestSale will calculate the lowest sale within the userTotalSales list.
     * Double averageSale will calculate the average sale from the userTotalSales list.
     */
    private int salesID;
    private int userID;
    private double salesTotal;
    private int totalSales;
    private double highestSale;
    private double lowestSale;
    private double averageSale;
    ArrayList<Double> userTotalSales = new ArrayList<>();

    /**
     * Constructors
     * Used to create Sales objects.
     * First is an empty constructor.
     * Second is an overloaded constructor and requires 3 arguments.
     * Third only require 1 argument
     */
    public Sales(){}
    
    public Sales(int salesID, int userID, double salesTotal){
        
        this.salesID = salesID;
        this.userID = userID;
        this.salesTotal = salesTotal;
        
    }

    public Sales(int userID) {
        this.userID = userID;
    }
    
     /**
     * Getters
     * 4 getters used to retrieve information regarding a Sales object
     * From call they will return:
     * @return salesID, userID, salesTotal and the contents of the ArrayList 
     */

    public int getSalesID() {
        return salesID;
    }

    public int getUserID() {
        return userID;
    }

    public double getSalesTotal() {
        return salesTotal;
    }

    public ArrayList<Double> userTotalSales() {
        return userTotalSales;
    }
    
    /**
     * Setters
     * 4 setters used to set the field for:
     * @param salesID, userID, salesTotal and to add items to the ArrayList
     */

    public void setSalesID(int salesID) {
        this.salesID = salesID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setSalesTotal(double salesTotal) {
        this.salesTotal = salesTotal;
    } 
    
    //Methods
    /**
     * This method is called to add sales related to a specific userID to the userTotalSales list.
     * 1 argument is passed through in the form of userID to the Database static method addSalesToList
     * This then returns the result set which is added to the Array List in the form of a for loop.
     */
    public void setuserTotalSales() {
        
        int rows = 0;
        
        try {
        
            ResultSet rs = Database.addSalesToList(this.userID);

            while(rs.next()){
                this.userTotalSales.add(rs.getDouble("salesTotal"));
                rows++;
            }
        }
        
        catch (SQLException e){
            System.out.println(e);
        }

    }    
    /**
     * This method is called to count the entries in the userTotalSales list.
     * The total number of sales is returned to be displayed
     * @return Integer totalSales
     */
    public int totalSales(){
        this.totalSales = this.userTotalSales.size();
        return this.totalSales;
    }
    /**
     * This method is called to calculate the highest sale within the userTotalSales list.
     * A for loop is ran with each sale being compared to the previous till the highest is calculated.
     * @return Double highestSale
     */
    public double highestSale(){
        double highest = 0;
        int countDown = this.userTotalSales.size();  
        
        for(int i = 0; i < countDown; i++){
            
            if (userTotalSales.get(i) > highest)
            {   
                highest = userTotalSales.get(i);
                this.highestSale = highest;
            }
        }
        
        return this.highestSale;
    }
    /**
     * This method is called to calculate the lowest sale within the userTotalSales list.
     * A for loop is ran with each sale being compared to the previous till the lowest is calculated.
     * @return Double Lowest
     */
    public double lowestSale(){
        double lowest = userTotalSales.get(0);
        int countDown = userTotalSales.size();
        
        for(int i = 0; i < countDown; i++){
            
            if (userTotalSales.get(i) < lowest)
            {
                lowest = userTotalSales.get(i);
                this.lowestSale = lowest;
            }
        }
        
        return this.lowestSale;
    }
    /**
     * This method is called to calculate the average sale within the userTotalSales list.
     * A for loop is ran to add all the sales to create a total which is then divided by the total number of sales within the list..
     * @return Double avergaeSale
     */
    public double averageSale(){
        double total = 0;
        int countDown = userTotalSales.size();
        
        for(int i = 0; i < countDown; i++){
            total = total + userTotalSales.get(i);
            this.averageSale = total / this.totalSales;
        }
        return this.averageSale;
    }
}
