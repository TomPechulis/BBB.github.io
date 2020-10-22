public class Listing {
    private String author;
    private String title;
    private String seller;
    private Integer isbn;
    private Double price;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Listing(String author, String title, Integer isbn, Double price, String seller){
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.seller = seller;
    }
}
