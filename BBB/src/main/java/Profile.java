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
