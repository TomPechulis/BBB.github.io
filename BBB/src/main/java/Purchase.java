import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Purchase {
    private String sellerEmail;
    private String buyerEmail;
    private Listing l;

    /**
     * Class constructor
     * @param sellerEmail The email of the Listing seller
     * @param buyerEmail The email of the Listing buyer (current user)
     * @param l The listing being purchased
     */
    Purchase(String sellerEmail, String buyerEmail, Listing l){
        this.sellerEmail = sellerEmail;
        this.buyerEmail = buyerEmail;
        this.l = l;
    }

    /**
     * Sends an email message to the seller with information on the buyer and listing
     */
    public void purchaseEmail(){
        String to = sellerEmail;
        String from = "baylorblackmarketbookstore@gmail.com";
        String host = "smtp.gmail.com";

        //Get the session object
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("baylorblackmarketbookstore@gmail.com", "BBB12345");

            }

        });

        //compose the message
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("Purchase Notification");
            message.setText("Hello, this is a notification from BBB letting you know that a user has " +
                            "indicated that they want to purchase one of your listings.\n\nListing information:\n" +
                            "Title: " + l.getTitle()+"\n"+"Author: " + l.getAuthor()+"\n"+"ISBN: " + l.getIsbn()+"\n"+
                            "Price: $" + l.getPrice()+"\n"+"Edition: " + l.getEdition()+"\n"+"Condition: " +
                            l.getCondition()+"\n\n"+"Here is the buyer's contact information to complete the transaction:\n"+
                            buyerEmail);

            // Send message
            Transport.send(message);

        }catch (MessagingException mex) {mex.printStackTrace();}
    }
}
