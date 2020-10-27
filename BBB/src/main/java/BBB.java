import java.io.IOException;
import java.util.List;

public class BBB {
    public static void main(String [] args) throws IOException {

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
