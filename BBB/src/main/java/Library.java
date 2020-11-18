import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The Library class is an information expert for the application.
 *
 * @author  Chris Torres
 * @version 1.2
 * @since   2020-10-10
 */
public class Library {
    private final List<Account> accountRegistry = new ArrayList<>();
    private final List<Listing> listingRegistry = new ArrayList<>();
    private final List<Profile> profileRegistry = new ArrayList<>();

    private final File accountFile = new File("src/main/resources/accountRegistry.csv");
    private final File listingFile = new File("src/main/resources/listingRegistry.csv");
    private final File profileFile = new File("src/main/resources/profileRegistry.csv");

    /**
     * Class constructor
     */
    public Library(){
        fillLibrary();
    }

    /**
     * This method checks if an entered Account is registered in the system
     * @param email The email of the user
     * @param password  The password of the user's account
     * @return boolean Checks if Account exists in accountRegistry
     */
    public boolean checkAccountRegistry(String email, String password){
        for(Account a : accountRegistry){
            if(a.getEmail().equals(email) && a.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets account associated with passed email
     * @param email The email of the user
     * @return Account Gives Account associated with email
     */
    public Account findAccount(String email){
        for(Account a : accountRegistry){
            if(a.getEmail().equals(email)){
                return a;
            }
        }
        return null;
    }

    /**
     * Gets the Profile associated with an Account
     * @param a Account of specified user
     * @return Profile Gets Profile associated with an Account
     */
    public Profile getProfile(Account a){
        Profile profile = null;

        for(Profile b : profileRegistry){
            if(b.getAccount().getEmail().equals(a.getEmail())){
                profile = b;
            }
        }
        return profile;
    }

    /**
     * Gets List of Listings associated with passed Profile
     * @param p Profile of specified user
     * @return List<Listing></Listing> All Listings of passed Profile
     */
    public List<Listing> getListings(Profile p){
        List<Listing> listingList = new ArrayList<>();
        for(Listing l : listingRegistry){
            if(l.getSeller().equals(p.getAccount().getEmail())){
                listingList.add(l);
            }
        }
        return listingList;
    }

    /**
     * Gets the current listingRegistry
     * @return List<Listing></Listing> The current listingRegistry
     */
    public List<Listing> getListingRegistry() {
        return listingRegistry;
    }
    /**
     * Gets the current profileRegistry
     * @return List<Profile></Profile> The current profileRegistry
     */
    public List<Profile> getProfileRegistry() {
        return profileRegistry;
    }

    /**
     * Adds a new Account to accountRegistry
     * @param email The email of the new Account
     * @param password The password of the new Account
     */
    public void addAccount(String email, String password){
        try {
            FileWriter fr = new FileWriter(accountFile,true);
            fr.append(email);
            fr.append(",");
            fr.append(password);
            fr.append("\n");

            fr.flush();
            fr.close();

            accountRegistry.add(new Account(email,password));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new Profile to profileRegistry
     * @param a Newly created Account
     */
    public void addProfile(Account a){
        try {
            FileWriter fr = new FileWriter(profileFile,true);
            fr.append(a.getEmail());
            fr.append(",");
            fr.append(a.getPassword());
            fr.append(",");

            //Ranking starts at 5
            fr.append("5.00");
            fr.append(",");

            //Rankers start at 0
            fr.append("1");
            fr.append(",");

            //Image for profile
            fr.append("null");
            fr.append(",");

            //Listing List
            fr.append("null");
            fr.append("\n");

            fr.flush();
            fr.close();

            profileRegistry.add(new Profile(a, 5.00, 1,null, null));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates each registry file to the current state of the registry list
     * @throws IOException Thrown if files can't be accessed
     */
    public void updateLibrary() throws IOException {
        PrintWriter writer = new PrintWriter(profileFile);
        writer.print("");
        writer.close();

        for(Profile b : profileRegistry){
            FileWriter fr = new FileWriter(profileFile,true);
            fr.append(b.getAccount().getEmail());
            fr.append(",");
            fr.append(b.getAccount().getPassword());
            fr.append(",");
            fr.append(String.valueOf(b.getRating()));
            fr.append(",");
            fr.append(String.valueOf(b.getRaters()));
            fr.append(",");
            String str;
            if(b.getInit()){
                str = b.getProfilePic().getPath().getAbsolutePath();
            }else{
                str = "null";
            }

            fr.append(str);
            fr.append(",");
            fr.append("null");

            fr.append("\n");

            fr.flush();
            fr.close();
        }

        writer = new PrintWriter(accountFile);
        writer.print("");
        writer.close();
        for(Profile a : profileRegistry){
            FileWriter fr = new FileWriter(accountFile,true);
            fr.append(a.getAccount().getEmail());
            fr.append(",");
            fr.append(a.getAccount().getPassword());
            fr.append("\n");

            fr.flush();
            fr.close();
        }
    }

    /**
     * Adds a new Listing to listingRegistry
     * @param l Newly created Listing
     */
    public void addListing(Listing l){
        try {
            FileWriter fr = new FileWriter(listingFile,true);
            fr.append(l.getSeller());
            fr.append(",");
            fr.append(l.getTitle());
            fr.append(",");
            fr.append(l.getAuthor());
            fr.append(",");
            fr.append(l.getIsbn());
            fr.append(",");
            fr.append(l.getPrice().toString());
            fr.append(",");
            fr.append(l.getEdition());
            fr.append(",");
            fr.append(l.getCondition());
            fr.append(",");
            fr.append(l.getImage().getPath().getAbsolutePath());
            fr.append("\n");

            fr.flush();
            fr.close();

            listingRegistry.add(l);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fills registry lists with contents of registry files
     */
    public void fillLibrary(){
        try{
            BufferedReader accountReader = new BufferedReader(new FileReader(accountFile));
            String accountLine;

            while ((accountLine = accountReader.readLine()) != null) {
                String [] data = accountLine.split(",");
                Account account = new Account(data[0],data[1]);

                accountRegistry.add(account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            BufferedReader listingReader = new BufferedReader(new FileReader(listingFile));
            String listingLine;

            while ((listingLine = listingReader.readLine()) != null) {
                //Get Email and Password
                String [] data = listingLine.split(",");
                ImageCustom temp = new ImageCustom(new File(data[7]));
                Listing listing = new Listing(data[0],data[1],data[2],data[3],Double.parseDouble(data[4]),data[5],data[6],temp);

                listingRegistry.add(listing);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try{
            BufferedReader profileReader = new BufferedReader(new FileReader(profileFile));
            String profileLine;

            while ((profileLine = profileReader.readLine()) != null) {
                //Get Email and Password
                String [] data = profileLine.split(",");
                Account a = new Account(data[0], data[1]);
                File f = new File(data[4]);
                ImageCustom i = new ImageCustom(f);
                List<Listing> listingList = new LinkedList<>();

                for(Listing l : listingRegistry){
                    if(l.getSeller().equals(data[0])){
                        listingList.add(l);
                    }
                }

                Profile profile = new Profile(a, Double.parseDouble(data[2]),Integer.parseInt(data[3]), i, listingList);

                profileRegistry.add(profile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Updates listingRegistry file with contents of listingRegistry list
     * @throws IOException Thrown if files can't be accessed
     */
    public void exportListings() throws IOException {
        PrintWriter writer = new PrintWriter(listingFile);
        writer.print("");
        writer.close();
        for(Listing a : listingRegistry){
            FileWriter fr = new FileWriter(listingFile,true);
            fr.append(a.getSeller());
            fr.append(",");
            fr.append(a.getTitle());
            fr.append(",");
            fr.append(a.getAuthor());
            fr.append(",");
            fr.append(a.getIsbn());
            fr.append(",");
            fr.append(String.valueOf(a.getPrice()));
            fr.append(",");
            fr.append(a.getEdition());
            fr.append(",");
            fr.append(a.getCondition());
            fr.append(",");
            fr.append(a.getImage().getPath().getAbsolutePath());
            fr.append("\n");

            fr.flush();
            fr.close();
        }
    }
}
