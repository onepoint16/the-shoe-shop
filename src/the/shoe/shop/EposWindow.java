/* _____                    ______      ____            __             
  / ___/____ _____ ___     / ____/___ _/ / /___ _____ _/ /_  ___  _____
  \__ \/ __ `/ __ `__ \   / / __/ __ `/ / / __ `/ __ `/ __ \/ _ \/ ___/
 ___/ / /_/ / / / / / /  / /_/ / /_/ / / / /_/ / /_/ / / / /  __/ /    
/____/\__,_/_/ /_/ /_/   \____/\__,_/_/_/\__,_/\__, /_/ /_/\___/_/     
                                              /____/                   
*/
package the.shoe.shop;

import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 * @author Sam Gallagher 30263952
 *
 */
public class EposWindow extends javax.swing.JFrame {

    Agents activeUser = new Agents();
    private String username;
    Stock shoppingCart = new Stock();
    Sales userSalesInfo = new Sales();
    private Boolean permission = false;
    DefaultListModel shoppingList = new DefaultListModel();
    DecimalFormat DF = new DecimalFormat("#.00");
    
    /** Creates new form EposWindow 
    * 
    */
    
    public EposWindow() {
        
        initComponents();
        
        hidePassword();
        
        lstShoppingCart.setModel(shoppingList);
    }

    /**
    * The hidePassword method is called to turn entered data into the PasswordField txtPassword into characters and then to change the character for the symbol *.
    * This is put in place for added security when logging in 
    */
    
    private void hidePassword(){
        
        txtPassword.setEchoChar('*');
    }  
        
    /**
    * Setter used to transfer username from login window to EPOS shopping window
    * The method createLoggedInUser is then called to create the current User as an Agents object
    * @param username 
    */
    
    public void setUsername(String username) {
        try{
            
            this.username = username;
        }
        
        catch(NullPointerException e){
            
            System.out.println("Error in setUsername " + e);
            
            this.username = "Guest";
        }
        
        onEposStart();
    }
    
    /**
    * This method is called on start up following the username being set by the setUsername method.
    * it will call the following methods
    * creatLoggedInUser is called to create the Agent Object
    * adminAuthenticication is called to validate the user status
    * managerView sets the visibility of certain function based on role
    * displayUserDetails is called to display the logged in user details to the user visually
    * displayUserSalesInfo is called to display the logged in users highest, lowest total number of and average sales
    */
    
    public void onEposStart() {
        
        createLoggedInUser(); 
        
        adminAuthenticication();
        
        managerView();  
        
        displayUserDetails(); 
        
        displayUserSalesInfo();
    }
    
    /**
    * This method will set the activeUser Agents object's username, password, ID and AgentType 
    */
    
    public void createLoggedInUser(){
        
        try {
            activeUser.setUsername(this.username);

            activeUser.setPassword(Database.passwordSearch(this.username));

            activeUser.setAgentType(Database.accountTypeSearch(this.username));

            activeUser.setUserID(Database.userIDSearch(this.username));
        }
        
        catch(Exception e){
            
            System.out.println("Error in createLoggedInUser " + e);
            
            activeUser.setUsername("Guest");

            activeUser.setPassword(Database.passwordSearch("Guest"));

            activeUser.setAgentType(Database.accountTypeSearch("Guest"));

            activeUser.setUserID(Database.userIDSearch("Guest"));            
        }
    }
    
    /**
    * This method will validate the logged in user's status. If the user is set as a Manager on the database the permission boolean will be set to true.
    * For Sales agent the value will remain false.
    */
    
    public void adminAuthenticication(){
        
        try{
            
            if ("Manager".equals(activeUser.getAgentType())){

                this.permission = true;
            }
            else {

                this.permission = false;
            }
        }
        
        catch (Exception e){
            
            System.out.println("Error in adminAuthenticication " + e);
            
            this.permission = false;
        }
    }
    
    /**
    * This method is called to set the visibility of certain functions based on the boolean permission.
    * If the logged in user is a manager permission is set to true and the ability to add and delete users as well as update delete and add products.
    * There is also a function to delete all user sales and delete all sales in the SamEposDb Sales table which is only avaliable to Managers.
    */
    
    public void managerView(){
        
        try{
            lblAccountTypeUserTab.setVisible(this.permission);

            comboAccountType.setVisible(this.permission);

            btnAddUser.setVisible(this.permission);

            btnDeleteUser.setVisible(this.permission);

            lblUserIdSearch.setVisible(this.permission);

            comboUserIdSearch.setVisible(this.permission);

            btnAddProduct.setVisible(this.permission);

            btnDeleteProduct.setVisible(this.permission);

            btnUpdateProduct.setVisible(this.permission);

            btnDeleteAllSales.setVisible(this.permission);
        }
        
        catch(Exception e){
            
            System.out.println("Error in managerview " + e);
            
            lblAccountTypeUserTab.setVisible(false);

            comboAccountType.setVisible(false);

            btnAddUser.setVisible(false);

            btnDeleteUser.setVisible(false);

            lblUserIdSearch.setVisible(false);

            comboUserIdSearch.setVisible(false);

            btnAddProduct.setVisible(false);

            btnDeleteProduct.setVisible(false);

            btnUpdateProduct.setVisible(false);

            btnDeleteAllSales.setVisible(false);
        }
    }
    
    /**
    * This method is called to display the logged in users details to the user.
    */
    
    private void displayUserDetails(){
        
        try {
            
            lblUsername.setText(this.username);

            lblUserId.setText(Integer.toString(activeUser.getUserID()));

            lblAgentType.setText(activeUser.getAgentType());

            txtUsername.setText(this.username);

            txtPassword.setText(activeUser.getPassword());

            comboAccountType.setSelectedItem(activeUser.getAgentType());

            lblUserId2.setText(Integer.toString(activeUser.getUserID()));
        }
        
        catch (Exception e){
            
            System.out.println("displayUserDetails " + e);
            
            lblUsername.setText("Guest");

            lblUserId.setText(null);

            lblAgentType.setText(null);

            txtUsername.setText(null);

            txtPassword.setText(null);

            comboAccountType.setSelectedItem(null);

            lblUserId2.setText(null);            
        }
    }
    
    /**
     * This method is called to display the logged in users highest, lowest total number of and average sales
     * it calls to the Sales class page to get the user specific sales information
     */

    public void displayUserSalesInfo(){
        
        try {
            
            userSalesInfo.setUserID(activeUser.getUserID());

            userSalesInfo.setuserTotalSales();

            lblTotalNumberOfSales.setText(Integer.toString(userSalesInfo.totalSales()));
            
            Double highestSale =  userSalesInfo.highestSale();

            lblHighestSale.setText("£" + DF.format(highestSale));
            
            Double lowestSale =  userSalesInfo.lowestSale();

            lblLowestSale.setText("£" + DF.format(lowestSale));
            
            Double averageSale = userSalesInfo.averageSale();

            lblAverageSale.setText("£" + DF.format(averageSale));
        }  
        
        catch (Exception e){
            
            System.out.println("Error in displayUserSalesInfo " + e);
            
            lblTotalNumberOfSales.setText(null);

            lblHighestSale.setText(null);

            lblLowestSale.setText(null);

            lblAverageSale.setText(null);            
        }
    }
    
    /**
     * This method is called within the product name combo. It will display the products avaliable in the Stock table of the SamEposDb Access file.
     */
    
        public void loadProductNameCombo(){
            
        comboProductName.removeAllItems();
        
        int rows = 0;
        
        ResultSet rs = Database.comboProductLoad();
        
        try {
            
            while(rs.next()){
                
                comboProductName.addItem(rs.getString("productName"));
                
                rows++;
            }
        }
        
        catch(SQLException e){
            
            System.out.println("Error in product name combo " + e);
        }
        
    }
        
    /**
    * This method is called to display the ongoing subtotal, VAT and total of the current shopping order.
    */
        
    public void displaySaleTotal (double sT, double VAT, double t){
        
        try{
            
            lblSubTotal.setText("£" + DF.format(sT));
            
            lblVAT.setText("£" + DF.format(VAT));
            
            lblTotal.setText("£" + DF.format(t));
        }
        
        catch (Exception e){
            
            System.out.println("Error in displaySaleTotal " + e);
            
            lblSubTotal.setText("£" + null);
            
            lblVAT.setText("£" + null);
            
            lblTotal.setText("£" + null);            
        }
    }
    
    /**
     * This method is called from within the userID search combo and will load all user Id's so a search can be conducted
     */
    
    public void loadUserIDCombo(){
        
        comboUserIdSearch.removeAllItems();
        
        try {
            
            ResultSet rs = Database.comboUserIDLoad();
            
            while(rs.next()){
                
                comboUserIdSearch.addItem(rs.getString("userID"));
            }
        }
        
        catch(SQLException e){
            
            System.out.println("Error in userId search " + e);
        }
    }
    
    /**
     * This method is called from within the productIdSearch combo and will load all the stock Id's
     */

    public void loadstockIDCombo(){
        
        comboProductIdSearch.removeAllItems();
        
        int rows = 0;
        
        ResultSet rs = Database.comboProductIDLoad();
        
        try {
            
            while(rs.next()){
                
                comboProductIdSearch.addItem(rs.getString("stockID"));
                
                rows++;
            }
        }
        
        catch(SQLException e){
            
            System.out.println("Error in combo product search id " + e);
        }
    } 
    
    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupPane1 = new javax.swing.JTabbedPane();
        paneShop = new javax.swing.JPanel();
        comboProductName = new javax.swing.JComboBox<>();
        spinnerQuantity = new javax.swing.JSpinner();
        btnAddToOrder = new javax.swing.JButton();
        btnCanelLastItem = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        lblHighestSale = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblLowestSale = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblTotalNumberOfSales = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblAverageSale = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        paneAdmin = new javax.swing.JTabbedPane();
        paneUsers = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        lblAccountTypeUserTab = new javax.swing.JLabel();
        comboAccountType = new javax.swing.JComboBox<>();
        btnAddUser = new javax.swing.JButton();
        btnUpdateUser = new javax.swing.JButton();
        btnDeleteUser = new javax.swing.JButton();
        lblUserIdSearch = new javax.swing.JLabel();
        comboUserIdSearch = new javax.swing.JComboBox<>();
        txtPassword = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        lblUserId2 = new javax.swing.JLabel();
        paneProducts = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        comboProductIdSearch = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtProductPrice = new javax.swing.JTextField();
        btnAddProduct = new javax.swing.JButton();
        btnUpdateProduct = new javax.swing.JButton();
        btnDeleteProduct = new javax.swing.JButton();
        paneSales = new javax.swing.JPanel();
        btnDeleteAllSales = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstShoppingCart = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        lblSubTotal = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblVAT = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblUserId = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblAgentType = new javax.swing.JLabel();
        btnCompleteOrder = new javax.swing.JButton();
        btnCancelOrder = new javax.swing.JButton();
        btnSignOut = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("The Shoe Shop");
        setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        setPreferredSize(new java.awt.Dimension(700, 400));

        btnAddToOrder.setText("Add Item");
        btnAddToOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToOrderActionPerformed(evt);
            }
        });

        btnCanelLastItem.setText("Cancel Last Item");
        btnCanelLastItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanelLastItemActionPerformed(evt);
            }
        });

        jLabel13.setText("Highest Sale:");

        lblHighestSale.setText(".....");

        jLabel15.setText("Lowest Sale:");

        lblLowestSale.setText(".....");

        jLabel17.setText("Total Sales:");

        lblTotalNumberOfSales.setText(".....");

        jLabel19.setText("Average Sale:");

        lblAverageSale.setText(".....");

        loadProductNameCombo();

        javax.swing.GroupLayout paneShopLayout = new javax.swing.GroupLayout(paneShop);
        paneShop.setLayout(paneShopLayout);
        paneShopLayout.setHorizontalGroup(
            paneShopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneShopLayout.createSequentialGroup()
                .addGroup(paneShopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneShopLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(comboProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(spinnerQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paneShopLayout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addGroup(paneShopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneShopLayout.createSequentialGroup()
                                .addGroup(paneShopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(paneShopLayout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblTotalNumberOfSales))
                                    .addGroup(paneShopLayout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblHighestSale)))
                                .addGap(47, 47, 47)
                                .addGroup(paneShopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel19))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(paneShopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblLowestSale)
                                    .addComponent(lblAverageSale)))
                            .addGroup(paneShopLayout.createSequentialGroup()
                                .addComponent(btnAddToOrder)
                                .addGap(75, 75, 75)
                                .addComponent(btnCanelLastItem)))))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        paneShopLayout.setVerticalGroup(
            paneShopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneShopLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(paneShopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(paneShopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddToOrder)
                    .addComponent(btnCanelLastItem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(paneShopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(lblHighestSale)
                    .addComponent(jLabel15)
                    .addComponent(lblLowestSale))
                .addGap(18, 18, 18)
                .addGroup(paneShopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblTotalNumberOfSales)
                    .addComponent(jLabel19)
                    .addComponent(lblAverageSale))
                .addGap(53, 53, 53))
        );

        groupPane1.addTab("Shop", paneShop);

        jLabel21.setText("Username:");

        jLabel22.setText("Password:");

        lblAccountTypeUserTab.setText("AccountType:");

        comboAccountType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manager", "Sales Agent" }));

        btnAddUser.setText("Add");
        btnAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddUserActionPerformed(evt);
            }
        });

        btnUpdateUser.setText("Update");
        btnUpdateUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateUserActionPerformed(evt);
            }
        });

        btnDeleteUser.setText("Delete");
        btnDeleteUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteUserActionPerformed(evt);
            }
        });

        lblUserIdSearch.setText("User ID Search:");

        comboUserIdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboUserIdSearchActionPerformed(evt);
            }
        });

        jLabel2.setText("User ID:");

        lblUserId2.setText("....");

        loadUserIDCombo();

        javax.swing.GroupLayout paneUsersLayout = new javax.swing.GroupLayout(paneUsers);
        paneUsers.setLayout(paneUsersLayout);
        paneUsersLayout.setHorizontalGroup(
            paneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneUsersLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(paneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneUsersLayout.createSequentialGroup()
                        .addGroup(paneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneUsersLayout.createSequentialGroup()
                                .addGroup(paneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel21))
                                .addGap(18, 18, 18)
                                .addGroup(paneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(paneUsersLayout.createSequentialGroup()
                                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel22))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneUsersLayout.createSequentialGroup()
                                        .addComponent(lblUserId2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblAccountTypeUserTab)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(paneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(comboAccountType, 0, 1, Short.MAX_VALUE)
                                    .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)))
                            .addGroup(paneUsersLayout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(btnAddUser)
                                .addGap(46, 46, 46)
                                .addComponent(btnUpdateUser)
                                .addGap(38, 38, 38)
                                .addComponent(btnDeleteUser)))
                        .addGap(55, 55, Short.MAX_VALUE))
                    .addGroup(paneUsersLayout.createSequentialGroup()
                        .addGap(0, 100, Short.MAX_VALUE)
                        .addComponent(lblUserIdSearch)
                        .addGap(18, 18, 18)
                        .addComponent(comboUserIdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(169, 169, 169))))
        );
        paneUsersLayout.setVerticalGroup(
            paneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneUsersLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(paneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAccountTypeUserTab)
                    .addComponent(comboAccountType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(lblUserId2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddUser)
                    .addComponent(btnUpdateUser)
                    .addComponent(btnDeleteUser))
                .addGap(28, 28, 28)
                .addGroup(paneUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUserIdSearch)
                    .addComponent(comboUserIdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        paneAdmin.addTab("Users", paneUsers);

        jLabel25.setText("Stock ID:");

        comboProductIdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboProductIdSearchActionPerformed(evt);
            }
        });

        jLabel26.setText("Product Name:");

        jLabel27.setText("Product Price £: ");

        btnAddProduct.setText("Add");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });

        btnUpdateProduct.setText("Update");
        btnUpdateProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateProductActionPerformed(evt);
            }
        });

        btnDeleteProduct.setText("Delete");
        btnDeleteProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProductActionPerformed(evt);
            }
        });

        loadstockIDCombo();

        javax.swing.GroupLayout paneProductsLayout = new javax.swing.GroupLayout(paneProducts);
        paneProducts.setLayout(paneProductsLayout);
        paneProductsLayout.setHorizontalGroup(
            paneProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneProductsLayout.createSequentialGroup()
                .addGroup(paneProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneProductsLayout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addGroup(paneProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel26)
                            .addComponent(jLabel25))
                        .addGap(18, 18, 18)
                        .addGroup(paneProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtProductName)
                            .addComponent(comboProductIdSearch, 0, 120, Short.MAX_VALUE)
                            .addComponent(txtProductPrice)))
                    .addGroup(paneProductsLayout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(btnAddProduct)
                        .addGap(35, 35, 35)
                        .addComponent(btnUpdateProduct)
                        .addGap(30, 30, 30)
                        .addComponent(btnDeleteProduct)))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        paneProductsLayout.setVerticalGroup(
            paneProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneProductsLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(paneProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(comboProductIdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(paneProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(paneProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(paneProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddProduct)
                    .addComponent(btnUpdateProduct)
                    .addComponent(btnDeleteProduct))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        paneAdmin.addTab("Products", paneProducts);

        btnDeleteAllSales.setText("Delete All Sales!!!");
        btnDeleteAllSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAllSalesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneSalesLayout = new javax.swing.GroupLayout(paneSales);
        paneSales.setLayout(paneSalesLayout);
        paneSalesLayout.setHorizontalGroup(
            paneSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSalesLayout.createSequentialGroup()
                .addGap(183, 183, 183)
                .addComponent(btnDeleteAllSales, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(164, Short.MAX_VALUE))
        );
        paneSalesLayout.setVerticalGroup(
            paneSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneSalesLayout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(btnDeleteAllSales)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        paneAdmin.addTab("Sales", paneSales);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneAdmin)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneAdmin)
        );

        groupPane1.addTab("Admin", jPanel2);

        jScrollPane1.setViewportView(lstShoppingCart);

        jLabel1.setText("Sub Total:");

        lblSubTotal.setText(".....");

        jLabel3.setText("VAT:");

        lblVAT.setText(".....");

        jLabel5.setText("Total:");

        lblTotal.setText(".....");

        jLabel7.setText("User ID:");

        lblUserId.setText(".....");

        jLabel9.setText("Username:");

        lblUsername.setText(".....");

        jLabel11.setText("Agent Type:");

        lblAgentType.setText(".....");

        btnCompleteOrder.setText("Complete");
        btnCompleteOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompleteOrderActionPerformed(evt);
            }
        });

        btnCancelOrder.setText("Cancel Order");
        btnCancelOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelOrderActionPerformed(evt);
            }
        });

        btnSignOut.setText("Sign Out");
        btnSignOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(groupPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSignOut)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addGap(12, 12, 12)
                        .addComponent(lblUserId)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblUsername)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblAgentType)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSubTotal)
                            .addComponent(lblVAT)
                            .addComponent(lblTotal)))
                    .addComponent(btnCompleteOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(groupPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lblSubTotal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(lblVAT))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(lblTotal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCompleteOrder)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSignOut)
                    .addComponent(jLabel7)
                    .addComponent(lblUserId)
                    .addComponent(jLabel9)
                    .addComponent(lblUsername)
                    .addComponent(jLabel11)
                    .addComponent(lblAgentType)
                    .addComponent(btnCancelOrder))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        groupPane1.getAccessibleContext().setAccessibleName("PaneShop");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSignOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignOutActionPerformed
        // Sign out button used to exit out to login window
        
        try{
            LoginForm log = new LoginForm();

            log.setVisible(true);

            dispose();             
        }
        
        catch(Exception e){
            
            System.out.println("Error in Sign out button " + e);
        }
       
    }//GEN-LAST:event_btnSignOutActionPerformed

    private void btnAddToOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToOrderActionPerformed
        // Add button for adding items to shopping basket prior to completing sale. 
        
        try {
            
            String cartItem = String.valueOf(comboProductName.getSelectedItem());

            shoppingCart.setProductName(cartItem);

            shoppingCart.setProductCost(Database.productPriceSearch(cartItem));

            double itemCost = shoppingCart.getProductCost();

            int quantity = (Integer) spinnerQuantity.getValue();

            shoppingCart.addToShoppingList(itemCost, quantity);

            double sT = shoppingCart.calcSubTotal();

            double VAT = shoppingCart.calcVAT();

            double t = shoppingCart.calcTotal();

            String listDisplay = cartItem + " x " + Integer.toString(quantity);

            shoppingList.addElement(listDisplay);

            displaySaleTotal(sT, VAT, t);  
        }
        
        catch (Exception e) {
            
            System.out.println("Error in Add item to cart button " + e);
        }
    }//GEN-LAST:event_btnAddToOrderActionPerformed

    private void btnCompleteOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompleteOrderActionPerformed
        // This is the complete sale button this will add the sale to the users sales int he SamEposDb Access file.
        
        try {
            
            Database.addSale(shoppingCart.calcTotal(), activeUser.getUserID());
            
            displayUserSalesInfo();
            
            JOptionPane.showMessageDialog(null, "Completed sale your total is £" 
                    + DF.format(shoppingCart.calcTotal()), "Sale", JOptionPane.INFORMATION_MESSAGE);
            
            lstShoppingCart.clearSelection();
            
            shoppingList.removeAllElements();
            
            shoppingCart.clearShoppingList();
            
            lblSubTotal.setText(null);
            
            lblVAT.setText(null);
            
            lblTotal.setText(null);             
        }
        
        catch (HeadlessException e){
            
            System.out.println("Error in Complete Sale button " + e);
        }               
    }//GEN-LAST:event_btnCompleteOrderActionPerformed

    private void btnCancelOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelOrderActionPerformed
        // This is the cancel order button
        
        try {
       
            lstShoppingCart.clearSelection();
       
            shoppingList.removeAllElements();
       
            shoppingCart.clearShoppingList();
       
            lblSubTotal.setText(null);
       
            lblVAT.setText(null);
       
            lblTotal.setText(null);           
        }
        
        catch (Exception e) {
            
            System.out.println("Error in The cancel order button " + e);
        }
    }//GEN-LAST:event_btnCancelOrderActionPerformed

    private void btnCanelLastItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanelLastItemActionPerformed
        // This is the cancel last item button
        
        try {
            
            int lastShoppingList = shoppingList.size();
            
            shoppingList.removeElementAt(lastShoppingList - 1);
            
            shoppingCart.removeLastFromShoppingList();
            
            double sT = shoppingCart.calcSubTotal();

            double VAT = shoppingCart.calcVAT();

            double t = shoppingCart.calcTotal();
            
            displaySaleTotal(sT, VAT, t);             
        }
        
        catch (Exception e) {
            
            System.out.println("Error in the Cancel last item button " + e);
        }
    }//GEN-LAST:event_btnCanelLastItemActionPerformed

    private void comboUserIdSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboUserIdSearchActionPerformed
        // This is the userID search combo box
        
        String UserIdSearch;
        
        try{ 
            
            UserIdSearch = comboUserIdSearch.getSelectedItem().toString();
            
            int userID = Integer.parseInt(UserIdSearch);
            
            String usernameSearch = Database.usermameSearch(userID);
            
            String passwordSearch = Database.passwordSearch(usernameSearch);
            
            comboAccountType.setSelectedItem(Database.accountTypeSearch(usernameSearch));
            
            lblUserId2.setText(UserIdSearch);
            
            txtUsername.setText(usernameSearch);
            
            txtPassword.setText(passwordSearch);             
        }
        
        catch(Exception e)
        {
            System.out.println("Error in combo user Id search " + e);
            UserIdSearch = "1";
        }
               
    }//GEN-LAST:event_comboUserIdSearchActionPerformed

    private void comboProductIdSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboProductIdSearchActionPerformed
        // This is the product ID search combo
        
        try {

            String productIdSearch = String.valueOf(comboProductIdSearch.getSelectedItem());
            
            int productId = Integer.parseInt(productIdSearch);
            
            String productName = Database.productNameSearch(productId);
            
            Double productCost = Database.productPriceSearch(productName);
            
            txtProductName.setText(productName);
            
            txtProductPrice.setText(Double.toString(productCost));     
        }
        
        catch (Exception e) {
            
            System.out.println("Error in product ID search combo " + e);
        }
    }//GEN-LAST:event_comboProductIdSearchActionPerformed

    private void btnAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUserActionPerformed
        // This is the add user to database button
        
        try{
            
            String usernameAdd = txtUsername.getText();
            
            String passwordAdd =  new String (txtPassword.getPassword());
            
            String agentTypeAdd = (String) comboAccountType.getSelectedItem();
            
            Database.addAgent(usernameAdd, passwordAdd, agentTypeAdd);
            
            JOptionPane.showMessageDialog(null, "You have successfully added " 
                    + txtUsername.getText(), "Add", JOptionPane.INFORMATION_MESSAGE);

            loadUserIDCombo();              
        }
        
        catch (Exception e) {
            
            System.out.println("Error in add user button " + e);
        }
      
    }//GEN-LAST:event_btnAddUserActionPerformed

    private void btnUpdateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateUserActionPerformed
        // This is the update user databse entry button

        try{
            
            int userIdUpdate = Integer.parseInt(lblUserId2.getText());
            
            String usernameUpdate = txtUsername.getText();
            
            String passwordUpdate =  new String (txtPassword.getPassword());
            
            String agentTypeUpdate = (String) comboAccountType.getSelectedItem();
            
            Database.updateAgent(usernameUpdate, passwordUpdate, agentTypeUpdate, userIdUpdate);
            
            JOptionPane.showMessageDialog(null, "You have successfully updated " 
                    + txtUsername.getText(), "Update", JOptionPane.INFORMATION_MESSAGE);

            loadUserIDCombo();              
        }
        
        catch (Exception e) {
            
            System.out.println("Error in update user button " + e);
        }
                
    }//GEN-LAST:event_btnUpdateUserActionPerformed

    private void btnDeleteUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteUserActionPerformed
        // This is the delete user from database button
        
        try{
            
            int userIdDelete = Integer.parseInt(lblUserId2.getText());
            
            String usernameDelete = txtUsername.getText();
            
            Database.deleteAllUserSales(userIdDelete);
            
            Database.deleteAgent(usernameDelete, userIdDelete);
            
            JOptionPane.showMessageDialog(null, "You have successfully deleted " 
                    + txtUsername.getText(), "Deleted", JOptionPane.INFORMATION_MESSAGE);

            loadUserIDCombo();              
        }
        
        catch (Exception e) {
            
            System.out.println("Error in delete user button " + e);
        }       
    }//GEN-LAST:event_btnDeleteUserActionPerformed

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        // This is the add product to database button
        
        try {
            
            String productNameAdd = txtProductName.getText();
            
            Double productPriceAdd = Double.parseDouble(txtProductPrice.getText());
            
            Database.addProduct(productNameAdd, productPriceAdd);
            
            JOptionPane.showMessageDialog(null, "You have successfully added " 
                    + txtProductName.getText(), "Add", JOptionPane.INFORMATION_MESSAGE);            
            
        }
        
        catch (Exception e) {
            
            System.out.println("Error in add product button " + e);
        }
    }//GEN-LAST:event_btnAddProductActionPerformed

    private void btnUpdateProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateProductActionPerformed
        // This is the update product databse entry button
        
        try {
            
            int stockIdUpdate = (Integer) comboProductIdSearch.getSelectedItem();
           
            String productNameUpdate = txtProductName.getText();
            
            Double productPriceUpdate = Double.parseDouble(txtProductPrice.getText());

            Database.updateProduct(productNameUpdate, productPriceUpdate, stockIdUpdate);
            
            JOptionPane.showMessageDialog(null, "You have successfully updated " 
                    + txtProductName.getText(), "Update", JOptionPane.INFORMATION_MESSAGE);             
        }
        
        catch (Exception e) {
            
            System.out.println("Error in the update product button " + e);
        }
    }//GEN-LAST:event_btnUpdateProductActionPerformed

    private void btnDeleteProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProductActionPerformed
        // This is the delete product entry button
        
        try {
            
             String productNameDelete = txtProductName.getText();
            
            Double productPriceDelete = Double.parseDouble(txtProductPrice.getText());

            Database.deleteProduct(productNameDelete, productPriceDelete);
            
            JOptionPane.showMessageDialog(null, "You have successfully deleted " 
                    + txtProductName.getText(), "Delete", JOptionPane.INFORMATION_MESSAGE);             
        }
        
        catch (Exception e) {
            
            System.out.println("Error in the delete product button " + e);
        }        
    }//GEN-LAST:event_btnDeleteProductActionPerformed

    private void btnDeleteAllSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAllSalesActionPerformed
        // This is the delete all user sales button
        
        Database.deleteAllSales();
    }//GEN-LAST:event_btnDeleteAllSalesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EposWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EposWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EposWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EposWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EposWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnAddToOrder;
    private javax.swing.JButton btnAddUser;
    private javax.swing.JButton btnCancelOrder;
    private javax.swing.JButton btnCanelLastItem;
    private javax.swing.JButton btnCompleteOrder;
    private javax.swing.JButton btnDeleteAllSales;
    private javax.swing.JButton btnDeleteProduct;
    private javax.swing.JButton btnDeleteUser;
    private javax.swing.JButton btnSignOut;
    private javax.swing.JButton btnUpdateProduct;
    private javax.swing.JButton btnUpdateUser;
    private javax.swing.JComboBox<String> comboAccountType;
    private javax.swing.JComboBox<String> comboProductIdSearch;
    private javax.swing.JComboBox<String> comboProductName;
    private javax.swing.JComboBox<String> comboUserIdSearch;
    private javax.swing.JTabbedPane groupPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAccountTypeUserTab;
    private javax.swing.JLabel lblAgentType;
    private javax.swing.JLabel lblAverageSale;
    private javax.swing.JLabel lblHighestSale;
    private javax.swing.JLabel lblLowestSale;
    private javax.swing.JLabel lblSubTotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalNumberOfSales;
    private javax.swing.JLabel lblUserId;
    private javax.swing.JLabel lblUserId2;
    private javax.swing.JLabel lblUserIdSearch;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblVAT;
    private javax.swing.JList<String> lstShoppingCart;
    private javax.swing.JTabbedPane paneAdmin;
    private javax.swing.JPanel paneProducts;
    private javax.swing.JPanel paneSales;
    private javax.swing.JPanel paneShop;
    private javax.swing.JPanel paneUsers;
    private javax.swing.JSpinner spinnerQuantity;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtProductPrice;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

}
