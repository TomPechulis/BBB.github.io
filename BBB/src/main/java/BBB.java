import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The BBB class initializes the application
 *
 * @author  Chris Torres
 * @version 1.1
 * @since   2020-10-10
 */
public class BBB {
    /**
     * This is the main method that creates an instance of the library, and allows the user to login. If the login
     * is successful, the Home screen is generated.
     * @param args Nothing.
     */
    public static void main(String [] args) throws IOException, URISyntaxException {

        Library library = new Library();

        Login login = new Login();
        login.getLogin(library);

        if (login.checkLogin()) {
            Account a = login.getAcc();
            Home home = new Home(library);
            home.getHome(a);
        }
    }
}
