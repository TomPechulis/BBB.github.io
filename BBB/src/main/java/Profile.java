import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

public class Profile {
    private double rating;
    private Image profilePic;
    private List<Listing> listingList;
    private Account acc;
    private boolean init = false;

    Profile(){
    }

    Profile(Account a, double r, Image p, List<Listing> l){
        this.acc = a;
        this.rating = r;
        this.profilePic = p;
        this.listingList = l;
    }

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    public Image getProfilePic() {
        return profilePic;
    }
    public void setProfilePic(Image i){
        profilePic = i;
    }

    public boolean getInit(){
        return init;
    }
    public void setInit(boolean b){
        this.init = b;
    }

    public List<Listing> getListingList() {
        return listingList;
    }
    public void setListingList(List<Listing> listingList) {
        this.listingList = listingList;
    }

    public void setAcc(Account b){
        this.acc = b;
    }
    public Account getAcc(){
        return this.acc;
    }

    public void editProfile(){
        final Object lock = new Object();
        final JFrame loginFrame = new JFrame("B.B.B - Login");
        final JPanel loginPanel = new JPanel(new GridLayout(3,1));

        loginFrame.setSize(600,150);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final JLabel passwordLabel = new JLabel("New Password:");
        final JTextField password = new JTextField();

        loginPanel.add(passwordLabel);
        loginPanel.add(password);

        final JLabel imageLabel = new JLabel("Upload Image:");
        final JButton upload = new JButton("Choose File (PNG or JPG only, < 50 mb)");
        final Image[] i = {null};
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
                i[0] = new Image(selectedFile);

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
                if(!password.getText().isEmpty()){
                    Account a = getAcc();
                    a.setPassword(password.getText());
                    setAcc(a);
                }
                loginFrame.setVisible(false);
                loginFrame.dispose();
                synchronized(lock) {
                    lock.notify();
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                createWindow(loginFrame);
                synchronized(lock) {
                    lock.notify();
                }
            }
        });

        loginPanel.add(saveButton);
        loginPanel.add(cancelButton);

        loginFrame.add(loginPanel, BorderLayout.CENTER);
        loginFrame.setVisible(true);

        synchronized(lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public Listing newListing(){
        final Object lock = new Object();
        final JFrame listingFrame = new JFrame("B.B.B - New Listing");
        final JPanel listingPanel = new JPanel(new GridLayout(8,1));

        listingFrame.setSize(600,400);
        listingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
        final Image[] i = {null};
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
                i[0] = new Image(selectedFile);

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

                    l1[0].setSeller(getAcc().getEmail());

                    listingFrame.setVisible(false);
                    listingFrame.dispose();
                    synchronized(lock) {
                        lock.notify();
                    }
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
        return l1[0];
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
