import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    Library library;
    final File accountFile = new File("src/main/resources/accountRegistry.csv");
    final File listingFile = new File("src/main/resources/listingRegistry.csv");
    final File profileFile = new File("src/main/resources/profileRegistry.csv");

    @BeforeEach
    void setUp() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(accountFile);
        writer.print("test@baylor.edu,TesterPass1\n");
        writer.close();

        writer = new PrintWriter(listingFile);
        writer.print("test@baylor.edu,Title,Author,ISBN,1.0,Edition,Condition,C:\\Users\\ctorr\\Documents\\kitten-report.jpg\n");
        writer.close();

        writer = new PrintWriter(profileFile);
        writer.print("test@baylor.edu,TesterPass1,5.00,1,null,null\n");
        writer.close();

        library = new Library();
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(accountFile);
        writer.print("");
        writer.close();

        writer = new PrintWriter(listingFile);
        writer.print("");
        writer.close();

        writer = new PrintWriter(profileFile);
        writer.print("");
        writer.close();
    }

    @Test
    void checkAccountRegistry() {
        assertTrue(library.checkAccountRegistry("test@baylor.edu","TesterPass1"));
    }

    @Test
    void checkEmailTaken() {
        assertTrue(library.checkEmailTaken("test@baylor.edu"));
    }

    @Test
    void findAccount() {
        Account account = new Account("test@baylor.edu","TesterPass1");

        assertEquals(account, library.findAccount("test@baylor.edu"));
    }

    @Test
    void findAccountFail(){
        Account account = new Account("test1@baylor.edu","TesterPass1");

        assertNotEquals(account, library.findAccount("test1@baylor.edu"));
    }

    @Test
    void getProfile(){
        Account account = new Account("test@baylor.edu","TesterPass1");
        assertNotNull(library.getProfile(account));
    }

    @Test
    void getProfileFail() {
        Account account = new Account("test1@baylor.edu","TesterPass1");

        assertNull(library.getProfile(account));
    }

    @Test
    void getListings() {
        Account account = new Account("test@baylor.edu","TesterPass1");
        Profile profile = new Profile(account,0,0,null,null);

        assertEquals(library.getListings(profile).size(), 1);
    }

    @Test
    void addAccount() {
        library.addAccount("test1@baylor.edu","TesterPass1");

        assertEquals(2,library.accountRegistry.size());
    }

    @Test
    void addProfile() {
        Account account = new Account("test@baylor.edu","TesterPass1");
        library.addProfile(account);

        assertEquals(2,library.profileRegistry.size());
    }

    @Test
    void updateLibraryProf() throws IOException {
        Account account = new Account("test1@baylor.edu","TesterPass1");
        Profile p = new Profile(account, 5.00, 1, null, null);
        library.profileRegistry.add(p);
        library.updateLibrary();
        assertEquals(89, library.profileFile.length());
    }

    @Test
    void updateLibraryAcc() throws IOException {
        Account account = new Account("test1@baylor.edu","TesterPass1");
        library.accountRegistry.add(account);
        library.updateLibrary();
        assertEquals(44, library.profileFile.length());
    }


    @Test
    void addListing(){
        Account account = new Account("test@baylor.edu","TesterPass1");
        library.addProfile(account);
        ImageCustom image = new ImageCustom(new File("null"));
        Listing l = new Listing("test@baylor.edu", "title", "author", "1234-5678", 10.99, "5th", "Used", image);
        library.addListing(l);

        assertEquals(2, library.listingRegistry.size());
    }


    @Test
    void fillLibraryAccountReg() throws FileNotFoundException {
        Account account = new Account("test1@baylor.edu","TesterPass1");
        PrintWriter writer = new PrintWriter(accountFile);
        writer.append("test1@baylor.edu,TesterPass1\n");
        writer.close();
        library.fillLibrary();
        assertTrue(library.accountRegistry.contains(account));
    }

    @Test
    void fillLibraryListingReg() throws FileNotFoundException {
        ImageCustom image = new ImageCustom(new File("C:\\Users\\ctorr\\Documents\\kitten-report1.jpg"));
        Listing l = new Listing("test1@baylor.edu","Title1","Author1","ISBN1",1.0,"Edition1","Condition1",image);
        PrintWriter writer= new PrintWriter(listingFile);
        writer.append("test1@baylor.edu,Title1,Author1,ISBN1,1.0,Edition1,Condition1,C:\\Users\\ctorr\\Documents\\kitten-report1.jpg\n");
        writer.close();
        library.fillLibrary();
        assertEquals(2,library.listingRegistry.size());
    }

    @Test
    void fillLibraryProfileReg() throws FileNotFoundException {
        Account account = new Account("test1@baylor.edu","TesterPass1");
        Profile p = new Profile(account, 5.00, 1, null, null);
        PrintWriter writer = new PrintWriter(profileFile);
        writer.append("test1@baylor.edu,TesterPass1,5.00,1,null,null\n");
        writer.close();
        library.fillLibrary();
        assertEquals(2, library.profileRegistry.size());
    }

    @Test
    void exportListings() throws IOException {
        ImageCustom image = new ImageCustom(new File("C:\\Users\\ctorr\\Documents\\kitten-report1.jpg"));
        Listing l = new Listing("test1@baylor.edu","Title1","Author1","ISBN1",1.0,"Edition1","Condition1",image);
        library.listingRegistry.add(l);
        library.exportListings();
        assertEquals(317, listingFile.length());
    }

}
