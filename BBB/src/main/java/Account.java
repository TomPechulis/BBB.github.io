/**
 * The Account class stores the username and password of a specific user.
 *
 * @author  Chris Torres
 * @version 1.2
 * @since   2020-10-10
 */
public class Account {
    private String password;
    private String email;

    /**
     * Gets the current password of the Account
     * @return String This returns the password of the Account
     */
    public String getPassword() {
        return password;
    }
    /**
     * Sets the password of the Account
     * @param password The new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the current email of the Account
     * @return String This returns the email of the Account
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets the email of the Account
     * @param email The new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Class constructor
     * @param email The email of the user
     * @param password  The password of the user's account
     */
    public Account(String email, String password){
        this.password = password;
        this.email = email;
    }

    /**
     * Check equivalence of two Account Objects
     * @param o The Account being checked for equivalence
     * @return boolean This returns whether or not the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return getEmail() != null ? getEmail().equals(account.getEmail()) : account.getEmail() == null;
    }

    /**
     * Calculates hash code of an Account object
     * @return int This returns the generated hash code
     */
    @Override
    public int hashCode() {
        return getEmail() != null ? getEmail().hashCode() : 0;
    }
}
