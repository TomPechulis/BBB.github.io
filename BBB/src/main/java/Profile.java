import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class Profile {
    private double rating;
    private int raters;
    private ImageCustom profilePic;
    private List<Listing> listingList;
    private Account account;
    private boolean init = false;

    /**
     * Class constructor
     * @param account The Account of the associated Profile
     * @param rating The 5.0 scale rating of the user
     * @param raters Number of people who have rated the user
     * @param p The profile picture of the user
     * @param l The list of Listings associated with the user
     */
    Profile(Account account, double rating, int raters, ImageCustom p, List<Listing> l){
        this.account = account;
        this.rating = rating;
        this.raters = raters;
        this.profilePic = p;
        this.listingList = l;
    }

    /**
     * Gets the current rating of the Profile
     * @return String This returns the rating of the Profile
     */
    public double getRating() {
        return rating;
    }
    /**
     * Sets the rating of the Profile
     * @param rating The new rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Allows user to rate another Profile
     * @param rating Added to rating total
     */
    public void rankUser(double rating) {
        this.rating += rating;
        raters++;
    }

    /**
     * Gets the current raters of the Profile
     * @return String This returns the raters of the Profile
     */
    public int getRaters() {
        return raters;
    }

    /**
     * Gets the current profilePic of the Profile
     * @return String This returns the profilePic of the Profile
     */
    public ImageCustom getProfilePic() {
        return profilePic;
    }
    /**
     * Sets the profilePic of the Profile
     * @param i The new profilePic
     */
    public void setProfilePic(ImageCustom i){
        profilePic = i;
    }

    /**
     * Gets the current status of the profile picture initialization of the Profile
     * @return String This returns the init of the Profile
     */
    public boolean getInit(){
        return init;
    }
    /**
     * Sets the init of the Profile
     * @param b The new init
     */
    public void setInit(boolean b){
        this.init = b;
    }

    /**
     * Gets the current listingList of the Profile
     * @return String This returns the listingList of the Profile
     */
    public List<Listing> getListingList() {
        return listingList;
    }

    /**
     * Sets the account of the Profile
     * @param b The new account
     */
    public void setAccount(Account b){
        this.account = b;
    }
    /**
     * Gets the current account of the Profile
     * @return String This returns the account of the Profile
     */
    public Account getAccount(){
        return this.account;
    }

    /**
     * Allows user to edit their own Profile
     * @param dialog Current JDialog window
     */
    public void editProfile(JDialog dialog){
        final JPanel loginPanel = new JPanel(new GridLayout(4,2));

        dialog.setSize(600,150);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final JLabel passwordLabel = new JLabel("New Password:");
        final JTextField password = new JTextField();
        final JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        final JTextField confirmPassword = new JTextField();

        loginPanel.add(passwordLabel);
        loginPanel.add(password);
        loginPanel.add(confirmPasswordLabel);
        loginPanel.add(confirmPassword);

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

        loginPanel.add(imageLabel);
        loginPanel.add(upload);


        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(clickImage[0]) {
                    boolean check = i[0].verifyImage();
                    if (check) {
                        setProfilePic(i[0]);
                        setInit(true);
                    }
                    else{
                        clickImage[0] = false;
                        JOptionPane.showMessageDialog(null,"Error: Image Invalid",
                                "B.B.B - Listing", JOptionPane.ERROR_MESSAGE);
                    }
                }
                Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");

                if(password.getText().equals("") || confirmPassword.getText().equals("")){

                }
                else if(password.getText().length() < 8){
                    JOptionPane.showMessageDialog(null,"Error: The password is less" +
                            " than 8 characters", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                }
                else if(!password.getText().equals(confirmPassword.getText())){
                    JOptionPane.showMessageDialog(null,"Error: The two passwords" +
                            " do not match", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                }
                else if(!textPattern.matcher(password.getText()).matches()){
                    JOptionPane.showMessageDialog(null,"Error: The password must have" +
                            " an upper and lower case character" +
                            " and one digit", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                }else{
                    Account a = getAccount();
                    a.setPassword(password.getText());
                    setAccount(a);
                    JOptionPane.showMessageDialog(null,"Notice: Profile" +
                            " has been updated", "", JOptionPane.PLAIN_MESSAGE);
                }
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createWindow(dialog);
            }
        });

        loginPanel.add(saveButton);
        loginPanel.add(cancelButton);

        dialog.add(loginPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    /**
     * Allows user to create a new Listing
     * @param dialog Current JDialog window
     */
    public Listing newListing(JDialog dialog){
        final JPanel listingPanel = new JPanel(new GridLayout(8,1));

        dialog.setSize(600,400);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        dialog.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        final JLabel titleLabel = new JLabel("Title:");
        final JTextField title = new JTextField();
        final JLabel authorLabel = new JLabel("Author:");
        final JTextField author = new JTextField();
        final JLabel isbnLabel = new JLabel("ISBN:");
        final JTextField isbn = new JTextField();
        final JLabel priceLabel = new JLabel("Price:");
        final JTextField price = new JTextField();
        final JLabel editionLabel = new JLabel("Edition:");
        final JTextField edition = new JTextField();
        final JLabel conditionLabel = new JLabel("Condition:");
        final JTextField condition = new JTextField();

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
        final Listing[] l1 = {null};

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                l1[0] = new Listing();
                if(clickImage[0] && !title.getText().isEmpty() && !author.getText().isEmpty() && !isbn.getText().isEmpty()
                   && !price.getText().isEmpty() && !edition.getText().isEmpty() && !condition.getText().isEmpty()) {

                    boolean check = i[0].verifyImage();
                    if (check) {
                        l1[0].setImage(i[0]);
                    }
                    else{
                        clickImage[0] = false;
                        JOptionPane.showMessageDialog(null,"Error: Image Invalid",
                                "B.B.B - Listing", JOptionPane.ERROR_MESSAGE);
                    }

                    l1[0].setTitle(title.getText());
                    l1[0].setAuthor(author.getText());
                    l1[0].setCondition(condition.getText());
                    l1[0].setEdition(edition.getText());
                    l1[0].setIsbn(isbn.getText());
                    l1[0].setPrice(Double.parseDouble(price.getText()));

                    l1[0].setSeller(getAccount().getEmail());

                    dialog.setVisible(false);
                    dialog.dispose();
                }
                else{
                    JOptionPane.showMessageDialog(null,"Error: One or more empty fields"
                            , "B.B.B - Listing", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createWindow(dialog);
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

        dialog.add(listingPanel, BorderLayout.CENTER);
        dialog.setVisible(true);

        return l1[0];
    }

    /**
     * Allows user to edit created listing
     * @param dialog The current JDialog window
     * @param l The Listing being edited
     */
    public Listing editListing(JDialog dialog,Listing l){
        final JPanel listingPanel = new JPanel(new GridLayout(8,1));

        dialog.setSize(600,400);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        dialog.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        final JLabel titleLabel = new JLabel("Title:");
        final JTextField title = new JTextField(l.getTitle());
        final JLabel authorLabel = new JLabel("Author:");
        final JTextField author = new JTextField(l.getAuthor());
        final JLabel isbnLabel = new JLabel("ISBN:");
        final JTextField isbn = new JTextField(l.getIsbn());
        final JLabel priceLabel = new JLabel("Price:");
        final JTextField price = new JTextField(String.valueOf(l.getPrice()));
        final JLabel editionLabel = new JLabel("Edition:");
        final JTextField edition = new JTextField(l.getEdition());
        final JLabel conditionLabel = new JLabel("Condition:");
        final JTextField condition = new JTextField(l.getCondition());

        final JLabel imageLabel = new JLabel("Upload Image:");
        final JButton upload = new JButton("Choose File (PNG or JPG only, < 50 mb)");
        final ImageCustom[] i = {l.getImage()};
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
        final Listing[] l1 = {null};

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                l1[0] = new Listing();
                if(!title.getText().isEmpty() && !author.getText().isEmpty() && !isbn.getText().isEmpty()
                        && !price.getText().isEmpty() && !edition.getText().isEmpty() && !condition.getText().isEmpty()) {

                    boolean check = i[0].verifyImage();
                    if (check) {
                        l1[0].setImage(i[0]);
                    }
                    else{
                        clickImage[0] = false;
                        JOptionPane.showMessageDialog(null,"Error: Image Invalid",
                                "B.B.B - Listing", JOptionPane.ERROR_MESSAGE);
                    }

                    l1[0].setTitle(title.getText());
                    l1[0].setAuthor(author.getText());
                    l1[0].setCondition(condition.getText());
                    l1[0].setEdition(edition.getText());
                    l1[0].setIsbn(isbn.getText());
                    l1[0].setPrice(Double.parseDouble(price.getText()));

                    l1[0].setSeller(getAccount().getEmail());

                    dialog.setVisible(false);
                    dialog.dispose();
                }
                else{
                    JOptionPane.showMessageDialog(null,"Error: One or more empty fields"
                            , "B.B.B - Listing", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createWindow(dialog);
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

        dialog.add(listingPanel, BorderLayout.CENTER);
        dialog.setVisible(true);

        return l1[0];
    }

    /**
     * Confirmation dialog
     * @param log Current JDialog window
     */
    public void createWindow(JDialog log) {
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
