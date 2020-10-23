import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private final List<Account> accountRegistry = new ArrayList<>();
    private final List<Listing> listingRegistry = new ArrayList<>();
    private final List<Profile> profileRegistry = new ArrayList<>();

    private final File accountFile = new File("src/main/resources/userRegistry.csv");
    private final File listingFile = new File("src/main/resources/listingRegistry.csv");
    private final File profileFile = new File("src/main/resources/profileRegistry.csv");

    public Library(){
        fillLibrary();
    }

    public boolean checkAccountRegistry(String email, String password){
        return accountRegistry.contains(new Account(email,password));
    }

    public Profile getProfile(Account a){
        Profile profile = new Profile();
        double rating = 0.00;
        Image image = null;
        List<Listing> listingList = null;

        for(Profile b : profileRegistry){
            if(b.getAcc().getEmail().equals(a.getEmail())){
                rating = b.getRating();
                image = b.getProfilePic();
                listingList = b.getListingList();
            }
        }

        profile.setProfilePic(image);
        profile.setListingList(listingList);
        profile.setRating(rating);
        profile.setAcc(a);
        return profile;
    }

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

    public void addProfile(Account a){
        try {
            FileWriter fr = new FileWriter(profileFile,true);
            fr.append(a.getEmail());
            fr.append(",");
            fr.append(a.getPassword());
            fr.append(",");
            fr.append("0.00");
            fr.append(",");
            fr.append("null");
            fr.append(",");
            fr.append("null");
            fr.append("\n");

            fr.flush();
            fr.close();

            profileRegistry.add(new Profile(a, 0.00, null, null));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateProfile(Profile p) throws IOException {
        int count = 0;
        PrintWriter writer = new PrintWriter(profileFile);
        writer.print("");
        writer.close();

        for(Profile b : profileRegistry){
            if(b.getAcc().getEmail().equals(p.getAcc().getEmail())){
                profileRegistry.set(count, p);
            }
            FileWriter fr = new FileWriter(profileFile,true);
            fr.append(b.getAcc().getEmail());
            fr.append(",");
            fr.append(b.getAcc().getPassword());
            fr.append(",");
            String temp = String.valueOf(b.getRating());
            fr.append(temp);
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
            count++;
        }
        count = 0;
        writer = new PrintWriter(accountFile);
        writer.print("");
        writer.close();
        for(Account a : accountRegistry){
            if(a.getEmail().equals(p.getAcc().getEmail())){
                accountRegistry.set(count, p.getAcc());
            }
            FileWriter fr = new FileWriter(accountFile,true);
            fr.append(a.getEmail());
            fr.append(",");
            fr.append(a.getPassword());
            fr.append("\n");

            fr.flush();
            fr.close();
            count++;
        }
    }

    public void addListing(String author, String title, Integer isbn, Double price, String seller){
        try {
            FileWriter fr = new FileWriter(listingFile,true);
            fr.append(author);
            fr.append(",");
            fr.append(title);
            fr.append(",");
            fr.append(isbn.toString());
            fr.append(",");
            fr.append(price.toString());
            fr.append(",");
            fr.append(seller);
            fr.append("\n");

            fr.flush();
            fr.close();

            listingRegistry.add(new Listing(author,title,isbn,price,seller));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                Listing listing = new Listing(data[0],data[1],Integer.parseInt(data[2]),Double.parseDouble(data[3]),data[4]);

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
                File f = new File(data[2]);
                Image i = new Image(f);

                Profile profile = new Profile(a, Double.parseDouble(data[2]), i, null);

                profileRegistry.add(profile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
