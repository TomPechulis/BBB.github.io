import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
    void getProfile() {
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
}