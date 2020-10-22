import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private final List<Account> accountRegistry = new ArrayList<>();
    private final List<Listing> listingRegistry = new ArrayList<>();

    private final File accountFile = new File("src/main/resources/userRegistry.csv");
    private final File listingFile = new File("src/main/resources/listingRegistry.csv");

    public Library(){
        fillLibrary();
    }

    public boolean checkAccountRegistry(String email, String password){
        return accountRegistry.contains(new Account(email,password));
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
    }
}
