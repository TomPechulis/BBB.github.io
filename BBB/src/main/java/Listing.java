import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Objects;

public class Listing {
    private String author;
    private String title;
    private String seller;
    private String isbn;

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

    @Override
    public int hashCode() {
        return Objects.hash(author, title, seller, isbn, price, edition, condition, imageCustom);
    }

    private Double price;
    private String edition;

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public ImageCustom getImage() {
        return imageCustom;
    }

    public void setImage(ImageCustom imageCustom) {
        this.imageCustom = imageCustom;
    }

    private String condition;
    private ImageCustom imageCustom;

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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
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

    public Listing(){
    }
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

    public void editListing(){
        final Object lock = new Object();
        final JFrame listingFrame = new JFrame("B.B.B - Edit Listing");
        final JPanel listingPanel = new JPanel(new GridLayout(8,1));

        listingFrame.setSize(600,400);
        listingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final JLabel titleLabel = new JLabel("Title:");
        final JTextField title = new JTextField(getTitle());
        final JLabel authorLabel = new JLabel("Author:");
        final JTextField author = new JTextField(getAuthor());
        final JLabel isbnLabel = new JLabel("ISBN:");
        final JTextField isbn = new JTextField(getIsbn());
        final JLabel priceLabel = new JLabel("Price:");
        final JTextField price = new JTextField(getPrice().toString());
        final JLabel editionLabel = new JLabel("Edition:");
        final JTextField edition = new JTextField(getEdition());
        final JLabel conditionLabel = new JLabel("Condition:");
        final JTextField condition = new JTextField(getCondition());

        final JLabel imageLabel = new JLabel("Upload Image:");
        final JButton upload = new JButton("Choose File (PNG or JPG only, < 50 mb)");
        final ImageCustom[] i = {null};
        final boolean[] clickImage = {false};
        upload.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                File selectedFile = null;
                int returnValue = jfc.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = jfc.getSelectedFile();

                }
                assert selectedFile != null;
                i[0] = new ImageCustom(selectedFile);

                clickImage[0] = true;
            }
        });

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(clickImage[0]){
                    boolean check = i[0].verifyImage();
                    if (check) {
                        setImage(i[0]);
                    }
                    else{
                        clickImage[0] = false;
                        JOptionPane.showMessageDialog(null,"Error: Image Invalid",
                                "B.B.B - Listing", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if(!title.getText().isEmpty()){
                    setTitle(title.getText());
                }
                if(!author.getText().isEmpty()){
                    setAuthor(author.getText());
                }
                if(!isbn.getText().isEmpty()){
                    setIsbn(isbn.getText());
                }
                if(!price.getText().isEmpty()){
                    setPrice(Double.parseDouble(price.getText()));
                }
                if(!edition.getText().isEmpty()){
                    setEdition(edition.getText());
                }
                if(!condition.getText().isEmpty()) {
                    setCondition(condition.getText());
                }
                listingFrame.setVisible(false);
                listingFrame.dispose();
                synchronized(lock) {
                    lock.notify();
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createWindow(listingFrame);
                synchronized(lock) {
                    lock.notify();
                }
            }
        });

        listingPanel.add(titleLabel);
        listingPanel.add(title);
        listingPanel.add(authorLabel);
        listingPanel.add(author);
        listingPanel.add(isbnLabel);
        listingPanel.add(isbn);
        listingPanel.add(priceLabel);
        listingPanel.add(price);
        listingPanel.add(editionLabel);
        listingPanel.add(edition);
        listingPanel.add(conditionLabel);
        listingPanel.add(condition);
        listingPanel.add(imageLabel);
        listingPanel.add(upload);

        listingPanel.add(saveButton);
        listingPanel.add(cancelButton);

        listingFrame.add(listingPanel, BorderLayout.CENTER);
        listingFrame.setVisible(true);

        synchronized(lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void createWindow(JFrame log) {
        JFrame frame = new JFrame("Confirm");

        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);
        int result = JOptionPane.showConfirmDialog(frame,"Are you sure?", "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(result == JOptionPane.YES_OPTION) {
            panel.setVisible(false);
            log.setVisible(false);
            log.dispose();
        }
        else if(result == JOptionPane.NO_OPTION){
            panel.setVisible(false);
        }

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setSize(560, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(false);
    }
}
