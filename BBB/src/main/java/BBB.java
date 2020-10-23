import java.io.IOException;

public class BBB {
    public static void main(String [] args) throws IOException {
        Library library = new Library();

        Login login = new Login();
        login.getLogin(library);

        if(login.checkLogin()){
            Account a = login.getAcc();
            Profile p = library.getProfile(a);

            p.editProfile();
            library.updateProfile(p);
            
        }
    }
}
