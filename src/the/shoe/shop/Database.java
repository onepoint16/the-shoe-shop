/**
 * _____                    ______      ____            __             
  / ___/____ _____ ___     / ____/___ _/ / /___ _____ _/ /_  ___  _____
  \__ \/ __ `/ __ `__ \   / / __/ __ `/ / / __ `/ __ `/ __ \/ _ \/ ___/
 ___/ / /_/ / / / / / /  / /_/ / /_/ / / / /_/ / /_/ / / / /  __/ /    
/____/\__,_/_/ /_/ /_/   \____/\__,_/_/_/\__,_/\__, /_/ /_/\___/_/     
                                              /____/                   
 */
package the.shoe.shop;

/**
* @author Sam Gallagher 30263952
* This is the Database connections page and will perform all the SQL commands
* This will be used to create, read, update and delete entries from the Access File SamEposDb on the tables Stock, Agents and Sales.
* Below I have imported various SQL packages to process the commands for the Access table
**/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class Database {

    /**
     * String home and dbpath are used in every SQL command to access the correct folder containing the database
    */
    
    private static String home = System.getProperty("user.dir");
    private static String dbpath = "jdbc:ucanaccess:////"+ home + "/database/SamEposDb.accdb";    

    //Stock Access Tables methods
    /**
     * This function is called to search for a products price using the product name.
     * A SQL command is called and the product Cost is returned
     * @param productName
     * @return 
     */
    public static double productPriceSearch (String productName) {
        
        double productCost = 0.00;
        
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet productPriceRS = stmt.executeQuery ("SELECT productCost FROM Stock WHERE productName='" + productName + "'");
            String price = "productCost";
            while (productPriceRS.next()){   
            productCost = Double.parseDouble(productPriceRS.getString(price));
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return productCost;
    }
    /**
     * This function is called to search for a products ID using the product name.
     * A SQL command is called and the product ID is returned
     * @param productName
     * @return 
     */
    public static int stockIDSearch (String productName) {
        int stockID = 0;
        
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet stockIDRS = stmt.executeQuery ("SELECT stockID FROM Stock WHERE productName='" + productName + "'");
            String ID = "stockID";
            while (stockIDRS.next()){
            stockID = Integer.parseInt(stockIDRS.getString(ID));
            }            
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        
        return stockID;
    }
    /**
     * This function is called to search for a products name using the product ID.
     * A SQL command is called and the product name is returned
     * @param stockID
     * @return 
     */
    public static String productNameSearch (int stockID) {
        
        String productName = null;
        
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet productNameRS = stmt.executeQuery ("SELECT productName FROM Stock WHERE stockID='" + stockID + "'");
            String itemName = "productName";
            while (productNameRS.next()){
            productName = productNameRS.getString(itemName);
            }            
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        
        return productName;        
    }
    /**
     * This method is called to update a product entry in the stock table
     * product name, cost and ID is taken and the SQL command updates the entry
     * @param productName
     * @param productCost
     * @param stockID 
     */
    public static void updateProduct(String productName, double productCost, int stockID){
      
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            String productUpdateRS = "UPDATE Stock SET productName='" + productName + "', productCost='" + productCost +"' WHERE stockID='" + stockID + "'";
            stmt.executeUpdate(productUpdateRS);
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }
    /**
     * This method is called to create a new product entry in the stock table
     * product name and cost is taken and the SQL command creates the entry
     * @param productName
     * @param productCost 
     */
    public static void addProduct(String productName, double productCost) {
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement(); 
            String productAddRS = "INSERT INTO Stock(productName, productCost) VALUES ('" + productName + "','" + productCost + "')";
            stmt.executeUpdate(productAddRS);
        }
        catch (SQLException e) {
            System.out.println(e);
        }        
    }
    /**
     * This method is called to delete a product entry in the stock table
     * product name and cost is taken and the SQL command delete the entry
     * @param productName
     * @param productCost 
     */
    public static void deleteProduct(String productName, double productCost) {
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement(); 
            String productDeleteRS = "DELETE FROM Stock WHERE (productName='" + productName + "' AND productCost='"+ productCost +"')";
            stmt.executeUpdate(productDeleteRS);
        }
        catch (SQLException e) {
            System.out.println(e);
        }        
    }
    /**
     * This method is called to receive all product names from the stock table
     * the result set is returned
     * @return 
     */
    public static ResultSet comboProductLoad(){
        try
        {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery ("SELECT productName FROM Stock");

            return rs; 
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return null;
    }
    /**
     * This method is called to receive all product ID's from the stock table
     * the result set is returned
     * @return 
     */
    public static ResultSet comboProductIDLoad(){
        try
        {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery ("SELECT stockID FROM Stock");
            return rs; 
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return null;
    }   
    // Agents Access Table methods
    /**
     * This function is called to search for the username using a user selected integer userID.
     * A SQL command is called and the username is returned.
     * @param userID
     * @return String username
     */    
    public static String usermameSearch (int userID) {
        
        String username = null;
        
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet usernameSearchRS = stmt.executeQuery ("SELECT username FROM Agents WHERE userID='" + userID + "'");
            String usernameRS = "username";
            while (usernameSearchRS.next()){
            username = usernameSearchRS.getString(usernameRS);
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return username;
    }
    /**
     * This function is called to search for the userID using a user inputted String username.
     * A SQL command is called and the userID is returned.
     * @param username
     * @return Integer userID
     */       
    public static int userIDSearch (String username) {
        
        int userID = 0;
        
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet userIDRS = stmt.executeQuery ("SELECT userID FROM Agents WHERE username='" + username + "'");
            String ID = "userID";
            while (userIDRS.next()){
            userID = Integer.parseInt(userIDRS.getString(ID));
            }            
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        
        return userID;
    }
    /**
     * This function is called to search for the password using a user inputted String username.
     * A SQL command is called and the password is returned.
     * @param username
     * @return String password
     */       
    public static String passwordSearch (String username) {
        
        String password = null;
        
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet passwordRS = stmt.executeQuery ("SELECT password FROM Agents WHERE username='" + username + "'");
            String pass = "password";
            while (passwordRS.next()){
            password = passwordRS.getString(pass);
            }            
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        
        return password;        
    }
    /**
     * This function is called to search for the accountType using a user inputted String username.
     * A SQL command is called and the accountType is returned.
     * @param username
     * @return String accountType
     */      
    public static String accountTypeSearch(String username){
        
        String accountType = null;
        
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet accountTypeRS = stmt.executeQuery ("SELECT accountType FROM Agents WHERE username='" + username + "'");
            String ac = "accountType";
            while (accountTypeRS.next()){
            accountType = accountTypeRS.getString(ac);
            }            
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return accountType;
    }
    /**
     * This method is called to update an entry in the Agent table within sameposdb.
     * The method receives 4 user inputted or selected arguments and updates the matching entry with the selected userID.
     * @param username
     * @param password
     * @param accountType
     * @param userID 
     */    
    public static void updateAgent(String username, String password, String accountType, int userID){
      
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            String agentUpdateRS = "UPDATE Agents SET username='" + username 
                    + "', password='" + password + "', accountType='" + accountType +"' WHERE userID='" + userID + "'";
            stmt.executeUpdate(agentUpdateRS);

        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }
    /**
     * This method is called to create a new entry in the Agents table within the sameposdb file.
     * The method receives 3 user inputted arguments to create the entry
     * @param username
     * @param password
     * @param accountType 
     */    
    public static void addAgent(String username, String password, String accountType) {
        

        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement(); 
            String agentAddRS = 
                    "INSERT INTO Agents(username, password, accountType) "
                    + "VALUES ('" + username + "','" + password +  "','" + accountType + "')";
            stmt.executeUpdate(agentAddRS);
        }
        catch (SQLException e) {
            System.out.println(e);
        }        
    }
    /**
     * This method is called to delete an entry in the Agent table within the sameposdb file.
     * The method takes 2 user selected arguments and deletes the entry from the table.
     * In order for this method to work all user sales have to be deleted form the Sales table within the sameposdb file to work correctly.
     * @param username
     * @param userID 
     */    
    public static void deleteAgent(String username, int userID) {
        
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement(); 
            String agentDeleteRS = "DELETE FROM Agents WHERE (username='" + username + "' AND userID='"+ userID +"')";
            stmt.executeUpdate(agentDeleteRS);
        }
        catch (SQLException e) {
            System.out.println(e);
        }        
    }  
    /**
     * This method is called to load combos with the usernames being displayed
     * @return ResultSet
     */
    public static ResultSet comboUserLoad(){
        
        try
        {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery ("SELECT username FROM Agents");
            return rs; 
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return null;
    }
    /**
     * This method is called to display the userID in a ComboBox.
     * @return ResultSet
     */
    public static ResultSet comboUserIDLoad(){
        
        try
        {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery ("SELECT userID FROM Agents");
            return rs; 
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return null;
    }    
    /**
     * This method is called to count the number of entries in the Agents table within the sameposdb file.
     * @return Integer userCount.
     */    
    public static int countUsers(){
        
        int userCount = 0;
        
        try{
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery ("SELECT * from Agents");
            ResultSetMetaData rsmd = rs.getMetaData();
            userCount = rsmd.getColumnCount();
            return userCount;   
            
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return 0;
    }
    
        //Sales Access Tables methods
    /**
     * This method is called to get all sales from the Sales table in the sameposdb file.
     * The result set is returned and then added to a list. 1 argument is passed through in the form of an integer to specify userID
     * @param userID
     * @return Result set
     */
    public static ResultSet addSalesToList(int userID){
        
        try{
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet usersSalesRS = stmt.executeQuery ("SELECT salesTotal FROM Sales WHERE userID=" + userID );
            return usersSalesRS;
        }
        catch (SQLException e){
            return null;
        }
    }
    /**
     * This method si called to receive all sales related to a specific userID. These will then be used to display to the user.
     * 1 argument is passed in the form of an integer userID
     * @param userID
     * @return Double salesTotal
     */
    public static double salesTotalSearch (int userID) {
        
        double salesTotal = 0.00;
        
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet salesTotalRS = stmt.executeQuery ("SELECT salesTotal FROM Sales WHERE userID=" + userID );
            String salesT = "salesTotal";
            while (salesTotalRS.next()){
            salesTotal = Double.parseDouble(salesTotalRS.getString(salesT));
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return salesTotal;
    }
    /**
     * This method is called to identify the salesID;s for sales related to a specific user ID
     * 1 argument is passed in the form of an integer userID
     * @param userID
     * @return integer salesID
     */
    public static int salesIDSearch (int userID) {
        
        int salesID = 0;
        
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet salesIDRS = stmt.executeQuery ("SELECT salesID FROM Sales WHERE userID='" + userID + "'");
            String ID = "salesID";
            while (salesIDRS.next()){
            salesID = Integer.parseInt(salesIDRS.getString(ID));
            }            
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        
        return salesID;
    }
    /**
     * This method is called to identify the userID related to a specific salesID
     * 1 argument is passed in the form of an integer salesID
     * @param salesID
     * @return integer userID
     */
    public static int userIDSearch (int salesID) {
        
        int userID = 0;
        
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            ResultSet userIDRS = stmt.executeQuery ("SELECT userID FROM Sales WHERE salesID='" + salesID + "'");
            String ID = "userID";
            while (userIDRS.next()){
            userID = Integer.parseInt(userIDRS.getString(ID));
            }            
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        
        return userID;
    }
    /**
     * This method is called to update the details of a sale either to change amount or userID related.
     * 3 arguments are passed in:
     * @param salesTotal
     * @param userID
     * @param salesID 
     */
    public static void updateSale(double salesTotal, int userID, int salesID){
      
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement();
            String saleUpdateRS = "UPDATE Sales SET salesTotal='" + salesTotal + "', userID='" + userID +"' WHERE salesID='" + salesID + "'";
            stmt.executeUpdate(saleUpdateRS);        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }
    /**
     * This method is called to add a completed sale to the Sales table in the sameposdb file.
     * 2 arguments are passed in:
     * @param salesTotal
     * @param userID 
     */
    public static void addSale(double salesTotal, int userID) {
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement(); 
            String saleAddRS = "INSERT INTO Sales(userID, salesTotal) VALUES ('" + userID + "','" + salesTotal + "')";
            stmt.executeUpdate(saleAddRS);
        }
        catch (SQLException e) {
            System.out.println(e);
        }        
    }
    /**
     * This method is called to delete a specific sale related to salesID
     * 1 argument is passed in:
     * @param salesID 
     */
    public static void deleteSale(int salesID) {
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement(); 
            String saleDeleteRS = "DELETE FROM Sales WHERE (salesID='" + salesID +"')";
            stmt.executeUpdate(saleDeleteRS);        }
        catch (SQLException e) {
            System.out.println(e);
        }        
    }
    /**
     * This method is called to delete all sales for a specific userID
     * 1 argument id passed in:
     * @param userID 
     */
    public static void deleteAllUserSales(int userID){
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement(); 
            String saleDeleteRS = "DELETE FROM Sales WHERE (userID='" + userID +"')";
            stmt.executeUpdate(saleDeleteRS);        
        }
        catch (SQLException e) {
            System.out.println(e);
        }          
    }
    /**
     * This method is called to delete all sales. This will only be performed by managers.
     */
    public static void deleteAllSales(){
        try {
            Connection con = DriverManager.getConnection(dbpath);
            Statement stmt = con.createStatement(); 
            String saleDeleteRS = "DELETE FROM Sales";
            stmt.executeUpdate(saleDeleteRS);        
        }
        catch (SQLException e) {
            System.out.println(e);
        }          
    }
} 

