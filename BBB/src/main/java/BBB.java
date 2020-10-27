import java.io.IOException;
import java.util.List;

public class BBB {
    public static void main(String [] args) throws IOException {

        Library library = new Library();

        Login login = new Login();
        login.getLogin(library);

        if (login.checkLogin()) {
            Account a = login.getAcc();
            Home home = new Home();
            home.getHome(library, a);

           /* tests for editing profile and updating in library
            p.editProfile();
            library.updateProfile(p);

*/


            /* tests for adding listing, editing listing, and updating in library
            Listing l = p.newListing();
            library.addListing(l);

            l.editListing();
            library.updateListing(l);

            List<Listing> listingList = library.getListings(p);
            for(Listing temp : listingList){
                System.out.println(temp.getTitle());
            }
            */

        }
    }
}
