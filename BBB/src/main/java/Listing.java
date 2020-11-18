import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Objects;

/**
 * The Listing class represents a book listing created by a user
 *
 * @author  Tyler Crumrine
 * @version 1.2
 * @since   2020-10-10
 */
public class Listing {
    private String author;
    private String title;
    private String seller;
    private String isbn;
    private String condition;
    private ImageCustom imageCustom;
    private Double price;
    private String edition;

    /**
     * Gets the current edition of the Listing
     * @return String This returns the edition of the Listing
     */
    public String getEdition() {
        return edition;
    }
    /**
     * Sets the edition of the Listing
     * @param edition The new edition
     */
    public void setEdition(String edition) {
        this.edition = edition;
    }

    /**
     * Gets the current condition of the Listing
     * @return String This returns the condition of the Listing
     */
    public String getCondition() {
        return condition;
    }
    /**
     * Sets the condition of the Listing
     * @param condition The new condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Gets the current imageCustom of the Listing
     * @return ImageCustom This returns the imageCustom of the Listing
     */
    public ImageCustom getImage() {
        return imageCustom;
    }
    /**
     * Sets the imageCustom of the Listing
     * @param imageCustom The new imageCustom
     */
    public void setImage(ImageCustom imageCustom) {
        this.imageCustom = imageCustom;
    }

    /**
     * Gets the current author of the Listing
     * @return String This returns the author of the Listing
     */
    public String getAuthor() {
        return author;
    }
    /**
     * Sets the author of the Listing
     * @param author The new author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the current title of the Listing
     * @return String This returns the title of the Listing
     */
    public String getTitle() {
        return title;
    }
    /**
     * Sets the title of the Listing
     * @param title The new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the current isbn of the Listing
     * @return String This returns the isbn of the Listing
     */
    public String getIsbn() {
        return isbn;
    }
    /**
     * Sets the isbn of the Listing
     * @param isbn The new isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the current price of the Listing
     * @return Double This returns the price of the Listing
     */
    public Double getPrice() {
        return price;
    }
    /**
     * Sets the price of the Listing
     * @param price The new price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Gets the current seller of the Listing
     * @return String This returns the seller of the Listing
     */
    public String getSeller() {
        return seller;
    }
    /**
     * Sets the seller of the Listing
     * @param seller The new seller
     */
    public void setSeller(String seller) {
        this.seller = seller;
    }

    /**
     * Check equivalence of two Listing Objects
     * @param o The Account being checked for equivalence
     * @return boolean This returns whether or not the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Listing listing = (Listing) o;
        return author.equals(listing.author) &&
                title.equals(listing.title) &&
                seller.equals(listing.seller) &&
                isbn.equals(listing.isbn) &&
                price.equals(listing.price) &&
                edition.equals(listing.edition) &&
                condition.equals(listing.condition) &&
                imageCustom.equals(listing.imageCustom);
    }

    /**
     * Calculates hash code of an Listing object
     * @return int This returns the generated hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(author, title, seller, isbn, price, edition, condition, imageCustom);
    }

    /**
     * Class constructor
     */
    public Listing(){
    }
    /**
     * Class constructor
     * @param seller The email of the seller of the listing
     * @param title The title of the listing
     * @param author The author of the listing
     * @param isbn The ISBN number of the listing
     * @param price The price of the listing in dollars
     * @param edition The edition of the listing
     * @param condition The condition of the listing
     * @param imageCustom A picture of the book
     */
    public Listing(String seller, String title, String author, String isbn, Double price, String edition, String condition,
                   ImageCustom imageCustom){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.edition = edition;
        this.condition = condition;
        this.imageCustom = imageCustom;
        this.seller = seller;
    }
}
