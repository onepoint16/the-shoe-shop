/* _____                    ______      ____            __             
  / ___/____ _____ ___     / ____/___ _/ / /___ _____ _/ /_  ___  _____
  \__ \/ __ `/ __ `__ \   / / __/ __ `/ / / __ `/ __ `/ __ \/ _ \/ ___/
 ___/ / /_/ / / / / / /  / /_/ / /_/ / / / /_/ / /_/ / / / /  __/ /    
/____/\__,_/_/ /_/ /_/   \____/\__,_/_/_/\__,_/\__, /_/ /_/\___/_/     
                                              /____/                   
*/
package the.shoe.shop;

/**
 * @author Sam Gallagher 30263952
 * This is the Agents class page. Agent objects will be created by taking user inputted data and matching that to data in the SamEposDb Access file.
 * Information taken from the login table integer he Access database file will be displayed to the user and used to show specific figures to the user.
 * Manager status will give admin rights and the ability to create and delete users.
 */
public class Agents {

    /**
     * Fields
     * Integer userID will identify an entry within the SamEposDb Access file.
     * String username will be used to display information to the user and to match an entry within the SamEposDb file.
     * String password will be retrieved from the SamEposDb file and will be used to verify the user
     * String accountType will be retrieved from the SamEposDb file and will be used to permit specific functions based on role within the company (salesAgen or Manager)
     */
    private int userID;
    private String username;
    private String password;
    private String accountType;

    /**
     * Constructors
     * Used to create Agent objects
     * First constructor is an empty constructor
     * Second is an overloaded constructor requiring 4 arguments being required
     * Third is an overloaded constructor requiring 3 arguments being required
     */

    public Agents(){}
    
    public Agents(int userID, String username, String password, String accountType) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    public Agents(String username, String password, String accountType) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }
    
    /**
     * Getters
     * 4 getters used to retrieve information regarding a Agents object
     * From call they will return:
     * @return userID, username, password, accountType
     */
    
    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAgentType() {
        return accountType;
    }
    
    /**
     * Setters
     * 4 setters used to set information regarding an Agents object
     * From call they will set:
     * @param userID, username, password, accountType.
     */

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAgentType(String accountType) {
        this.accountType = accountType;
    }    
}
