import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import java.awt.Image;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The Home class contains the components that make up the main window the user sees after successfully logging in.
 *
 * @author  Tom Pechulis
 * @version 1.3
 * @since   2020-11-4
 */
public class Home {
    Library library;
    static Account currentUser;

    /**
     * Class constructor
     * @param i The current instance of Library being used
     */
    public Home(Library i){
        library = i;
    }

    /**
     * This method returns the currently logged in user
     * @return Account Gets the user that is currently logged in
     */
    public static Account currentUser() {
        return currentUser;
    }

    /**
     * The main window for the user, includes 3 tabs the user can interact with
     * @param currentAccount The Account of the currently logged in user
     * @throws IOException Thrown if library files cannot be updated
     */
    public void getHome(Account currentAccount) throws IOException {
        currentUser = currentAccount;

        final JFrame frame = new JFrame("B.B.B - Main Menu");

        final JTabbedPane pane = new JTabbedPane();

        frame.setSize(1750, 600);
        frame.setLocationRelativeTo(null);

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.PAGE_AXIS));
        final Profile[] profile = {library.getProfile(currentAccount)};

        if(profile[0].getProfilePic() != null){
            ImageIcon imageIcon = new ImageIcon(profile[0].getProfilePic().getPath().getAbsolutePath());

            Image image = imageIcon.getImage(); // transform it
            Image newimg = image.getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            imageIcon = new ImageIcon(newimg);  // transform it back

            userPanel.add(new JLabel(imageIcon));
        }

        JLabel accountName = new JLabel("Account: " + currentAccount.getEmail());

        JLabel rank = new JLabel("Rank: " + profile[0].getRating() / profile[0].getRaters());
        //JLabel picture;
        /*
        if(profile[0].getInit()){
            picture = new JLabel((Icon) profile[0].getProfilePic());
        }
        else{
            picture = new JLabel();
        }
*/

        JButton newListingButton = new JButton("New Listing");
        AccountListingTable alt = new AccountListingTable(library, profile[0],true);

        newListingButton.addActionListener(e -> {
            profile[0] = library.getProfile(currentAccount);
            JDialog dialog = new JDialog(frame,true);
            dialog.setTitle("B.B.B - New Listing");
            Listing l = profile[0].newListing(dialog);

            if(l != null && profile[0].getListingList() != null){
                profile[0].getListingList().add(l);
                library.addListing(l);
                alt.addRow(l);
            }
            else if(l != null && profile[0].getListingList() == null){
                List<Listing> temp = new ArrayList<>();
                temp.add(l);
                profile[0].setListingList(temp);
                library.addListing(l);
                alt.addRow(l);
            }
        });

        JButton editProfileButton = new JButton("Edit Profile");


        editProfileButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                profile[0] = library.getProfile(currentAccount);
                JDialog dialog = new JDialog(frame,true);
                dialog.setTitle("B.B.B - Edit Profile");
                profile[0].editProfile(dialog);

                try{
                    library.updateLibrary();

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                ImageIcon imageIcon = new ImageIcon(profile[0].getProfilePic().getPath().getAbsolutePath());
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg);  // transform it back

                userPanel.remove(userPanel.getComponent(0));
                userPanel.remove(accountName);
                userPanel.remove(rank);
                userPanel.remove(newListingButton);
                userPanel.remove(editProfileButton);
                userPanel.remove(alt);
                userPanel.add(new JLabel(imageIcon));
                userPanel.add(accountName);
                userPanel.add(rank);
                userPanel.add(newListingButton);
                userPanel.add(editProfileButton);
                userPanel.add(alt);
                frame.revalidate();
                frame.repaint();

            }
        });


        userPanel.add(accountName);
        userPanel.add(rank);
        //userPanel.add(picture);
        userPanel.add(newListingButton);
        userPanel.add(editProfileButton);
        userPanel.add(alt);

        pane.addTab("My Account", userPanel);

        pane.addTab("Book Search", new BookTable(library, profile[0]));

        pane.addTab("Seller Search", new SellerTable(library));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pane);
        frame.setVisible(true);
    }

}

/**
 * The BookTable class extends JPanel to create the tab showing all current listings, and allowing for searching and
 * filtering of results.
 *
 * @author  Tom Pechulis
 * @version 1.1
 * @since   2020-11-4
 */
class BookTable extends JPanel {

    private final JTextField filterSearchText;
    private final TableRowSorter<TableModel> sorter;

    /**
     * This method initializes the BookTable and populates it with information
     * @param library The Library instance containing the data needed to initialize
     * @param prof The Profile of the current Account
     */
    public BookTable(Library library, Profile prof) throws IOException {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        String [] columns = {"Email", "Title", "Author", "Price", "ISBN", "Edition", "Condition", "Picture", "Purchase"};
        DefaultTableModel tableModel = new DefaultTableModel(columns,0);

        parseTable(tableModel,library);

        JTable table = new JTable(tableModel)   {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };

        Action purchase = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.valueOf(e.getActionCommand());
                JFrame frame = new JFrame("Confirm");

                JPanel panel = new JPanel();
                LayoutManager layout = new FlowLayout();
                panel.setLayout(layout);
                int result = JOptionPane.showConfirmDialog(frame,"Are you sure?", "Confirm",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.YES_OPTION) {
                    ImageCustom i = new ImageCustom(new File(tableModel.getValueAt(modelRow,7).toString()));

                    Listing temp = new Listing(tableModel.getValueAt(modelRow,0).toString(),tableModel.getValueAt(modelRow,1).toString(),
                            tableModel.getValueAt(modelRow,2).toString(),tableModel.getValueAt(modelRow,4).toString(),
                            Double.parseDouble(tableModel.getValueAt(modelRow,3).toString()),tableModel.getValueAt(modelRow,5).toString(),
                            tableModel.getValueAt(modelRow,6).toString(),i);

                    Purchase p = new Purchase(tableModel.getValueAt(modelRow,0).toString(), prof.getAccount().getEmail(), temp);
                    p.purchaseEmail();
                }
                else if(result == JOptionPane.NO_OPTION){
                    panel.setVisible(false);
                }

                frame.getContentPane().add(panel, BorderLayout.CENTER);
                frame.setSize(560, 200);
                frame.setLocationRelativeTo(null);
                frame.setVisible(false);
            }
        };
        ButtonColumn purchaseButton = new ButtonColumn(table,purchase,8);

        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);

        ButtonGroup g = new ButtonGroup();
        JRadioButton toggle = new JRadioButton("Title");
        JRadioButton toggle2 = new JRadioButton("Author");
        JRadioButton toggle3 = new JRadioButton("ISBN");
        g.add(toggle);
        g.add(toggle2);
        g.add(toggle3);

        JPanel form = new JPanel();

        form.add(toggle);
        form.add(toggle2);
        form.add(toggle3);

        JLabel l1 = new JLabel("SEARCH:", SwingConstants.TRAILING);

        form.add(l1);
        filterSearchText = new JTextField(10);

        filterSearchText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(filterSearchText.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                search(filterSearchText.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                search(filterSearchText.getText());
            }
            public void search(String str) {
                if(toggle.isSelected()) {
                    if (str.length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)"+str,1));
                    }
                }else if(toggle2.isSelected()){
                    if (str.length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)"+str,2));
                    }
                }else if(toggle3.isSelected()){
                    if (str.length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)"+str,4));
                    }
                }
            }
        });
        l1.setLabelFor(filterSearchText);
        form.add(filterSearchText);
        table.setRowHeight(table.getRowHeight() + 50);
        add(form);

        add(scrollPane);
    }

    /**
     * This method is used to fill the BookTable with information and doesn't show the current user their own listings
     * @param library The Library instance containing the data needed to initialize
     * @param table The current state of the BookTable
     */
    public static void parseTable(DefaultTableModel table,Library library){
        List<Listing> listingList = library.getListingRegistry();
        Account currentUser = Home.currentUser();

        for(Listing l : listingList){
            if(l.getSeller().equals(currentUser.getEmail())){
                continue;
            }
            ImageIcon ic = new ImageIcon(l.getImage().getPath().getAbsolutePath());
            Image img = ic.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            ic = new ImageIcon(img);
            Object [] row = {l.getSeller(),l.getTitle(),l.getAuthor(), String.valueOf(l.getPrice()), l.getIsbn(),l.getEdition(),l.getCondition(), ic , "Purchase"};
            table.addRow(row);
        }
    }
}

/**
 * The SellerTable class extends JPanel to create the tab showing all sellers and allowing to visit their page.
 *
 * @author  Tom Pechulis
 * @version 1.1
 * @since   2020-11-4
 */
class SellerTable extends JPanel {
    private final JTextField seller;
    private final TableRowSorter<TableModel> sorter;

    /**
     * This method is used to initialize the SellerTable with the information from the Library
     * @param library The Library instance containing the data needed to initialize
     * @throws IOException Thrown when information cannot be obtained from visited profile
     */
    public SellerTable(Library library) throws IOException {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String [] columns = {"Seller","Rank", "Visit"};
        DefaultTableModel tableModel = new DefaultTableModel(columns,0);
        JTable table = new JTable(tableModel);

        parseTable(tableModel,library);

        Action visit = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = (String) tableModel.getValueAt(Integer.valueOf(e.getActionCommand()),0);
                Account visiting = library.findAccount(email);

                final JFrame frame = new JFrame("B.B.B - " + visiting.getEmail());
                frame.setSize(1000, 300);
                frame.setLocationRelativeTo(null);

                final JTabbedPane pane = new JTabbedPane();
                AccountListingTable alt = new AccountListingTable(library,library.getProfile(visiting),false);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

                JLabel accountName = new JLabel("Account: " + visiting.getEmail());

                final Profile[] profile = {library.getProfile(visiting)};

                DecimalFormat numberFormat = new DecimalFormat("#.00");
                JLabel rating = new JLabel("Rank: " + numberFormat.format(profile[0].getRating() / profile[0].getRaters()));

                if(profile[0].getProfilePic() != null){
                    ImageIcon imageIcon = new ImageIcon(profile[0].getProfilePic().getPath().getAbsolutePath());

                    Image image = imageIcon.getImage(); // transform it
                    Image newimg = image.getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                    imageIcon = new ImageIcon(newimg);  // transform it back

                    panel.add(new JLabel(imageIcon));
                }

                JButton rateSeller = new JButton("Rate Seller");

                rateSeller.addActionListener(x -> {
                    JDialog dialog = new JDialog(frame,true);
                    dialog.setTitle("B.B.B - Rate Seller");

                    final JPanel rankPanel = new JPanel(new GridLayout(2,2));

                    dialog.setSize(300,100);
                    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    dialog.setLocationRelativeTo(null);

                    dialog.addWindowListener(new WindowAdapter()
                    {
                        public void windowClosing(WindowEvent e)
                        {
                            dialog.setVisible(false);
                            dialog.dispose();
                        }
                    });

                    final JLabel rankLabel = new JLabel("Rank (0 - 5):");
                    final JTextField rank = new JTextField();

                    rankPanel.add(rankLabel);
                    rankPanel.add(rank);

                    final JButton submit = new JButton("Rank Seller");
                    submit.addActionListener(new AbstractAction() {
                        public void actionPerformed(ActionEvent e) {
                            if(Double.parseDouble(rank.getText()) < 0 || Double.parseDouble(rank.getText()) > 5){
                                JOptionPane.showMessageDialog(null,"Error: None of the fields" +
                                        " may be be above or below the bounds.", "B.B.B - Rank", JOptionPane.ERROR_MESSAGE);
                            }
                            else if(rank.getText().equals("")){
                                JOptionPane.showMessageDialog(null,"Error: None of the fields" +
                                        " may be blank.", "B.B.B - Rank", JOptionPane.ERROR_MESSAGE);
                            }
                            else{
                                library.getProfile(visiting).rankUser(Double.parseDouble(rank.getText()));

                                try {
                                    library.updateLibrary();
                                    //parseTable(tableModel,library);
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                DecimalFormat numberFormat = new DecimalFormat("#.00");
                                rating.setText("Rank: " + numberFormat.format(profile[0].getRating() / profile[0].getRaters()));
                                table.setValueAt((numberFormat.format(profile[0].getRating() / profile[0].getRaters())),table.getSelectedRow(),1);
                                tableModel.fireTableDataChanged();
                                dialog.setVisible(false);
                                dialog.dispose();
                            }
                        }
                    });
                    rankPanel.add(submit);

                    final JButton cancel = new JButton("Cancel");
                    cancel.addActionListener(new AbstractAction() {
                        public void actionPerformed(ActionEvent e) {
                            int result = JOptionPane.showConfirmDialog(frame,"Sure? You want to exit?"
                                    , "BBB - Rank Seller",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                            if(result == JOptionPane.YES_OPTION){
                                dialog.setVisible(false);
                                dialog.dispose();
                            }
                        }
                    });
                    rankPanel.add(cancel);

                    dialog.add(rankPanel, BorderLayout.CENTER);
                    dialog.setVisible(true);
                });

                panel.add(accountName);
                panel.add(rating);
                panel.add(rateSeller);
                panel.add(alt);

                pane.addTab("Seller Account", panel);

                frame.add(pane);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        };
        ButtonColumn visitButton = new ButtonColumn(table,visit,2);

        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel searchBar = new JPanel();
        JLabel searchLabel = new JLabel("Seller:", SwingConstants.TRAILING);

        searchBar.add(searchLabel);
        seller = new JTextField(10);

        //Whenever filterText changes, invoke newFilter.
        seller.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(seller.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                search(seller.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                search(seller.getText());
            }
            public void search(String str) {
                if (str.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)"+str,0));
                }
            }
        });
        table.setRowHeight(table.getRowHeight() + 50);

        searchBar.add(seller);
        add(searchBar);
        add(scrollPane);
    }

    /**
     * This method is used to fill the BookTable with information and doesn't show the current user their own listings
     * @param library The Library instance containing the data needed to initialize
     * @param table The current state of the BookTable
     */
    public static void parseTable(DefaultTableModel table,Library library){
        List<Profile> profileList = library.getProfileRegistry();
        Account currentUser = Home.currentUser();

        for(int i = 0; i < table.getRowCount(); i++){
            table.removeRow(i);
        }

        for(Profile p : profileList){
            if(p.getAccount().equals(currentUser)){
                continue;
            }
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            String temp = numberFormat.format(p.getRating()/p.getRaters());
            String [] row = {p.getAccount().getEmail(),temp,"Visit"};
            table.addRow(row);
        }
    }
}

/**
 * The AccountListingTable class extends JPanel to create the tab showing all the listings for the current user.
 *
 * @author  Tom Pechulis
 * @version 1.1
 * @since   2020-11-4
 */
class AccountListingTable extends JPanel {
    Library library;
    Profile profile;
    DefaultTableModel tableModel;

    /**
     * This method is used to add a new row to the AccountListingTable
     * @param l The listing being added to the AccountListingTable
     */
    public void addRow(Listing l){
        Object [] row = {l.getSeller(),l.getTitle(),l.getAuthor(), String.valueOf(l.getPrice()), l.getIsbn(),l.getEdition(),l.getCondition(), new ImageIcon(l.getImage().getPath().getAbsolutePath()), "Edit", "Delete"};
        tableModel.addRow(row);
    }

    /**
     * This method is used to initialize the AccountListingTable with the information from the Library
     * @param library The Library instance containing the data needed to initialize
     */
    public AccountListingTable(Library library, Profile profile, boolean isOwner) {
        super();

        this.library = library;
        this.profile = profile;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        if(isOwner){
            String [] columns = {"Email", "Title", "Author", "Price", "ISBN", "Edition", "Condition", "Image","Edit", "Delete"};
            tableModel = new DefaultTableModel(columns,0);

            parseTable(tableModel,library,profile, true);

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            JTable table = new JTable(tableModel)      {
                //  Returning the Class of each column will allow different
                //  renderers to be used based on Class
                public Class getColumnClass(int column) {
                    return getValueAt(0, column).getClass();
                }
            };

            JFrame thisFrame = (JFrame) this.getParent();

            Action editListing = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog dialog = new JDialog(thisFrame,true);
                    dialog.setTitle("B.B.B - Edit Listing");
                    int modelRow = Integer.parseInt(e.getActionCommand());
                    Listing listing = profile.getListingList().get(modelRow);
                    Listing newListing = profile.editListing(dialog,listing);

                    if(newListing != null){
                        listing.setAuthor(newListing.getAuthor());
                        listing.setTitle(newListing.getTitle());
                        listing.setPrice(newListing.getPrice());
                        listing.setCondition(newListing.getCondition());
                        listing.setEdition(newListing.getEdition());
                        listing.setImage(newListing.getImage());
                        listing.setIsbn(newListing.getIsbn());

                        try {
                            library.exportListings();
                            parseTable(tableModel,library,profile, true);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            };
            ButtonColumn editListingButton = new ButtonColumn(table,editListing,8);

            Action deleteListing = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog dialog = new JDialog(thisFrame,true);
                    dialog.setTitle("B.B.B - Delete Listing");
                    int modelRow = Integer.parseInt(e.getActionCommand());

                    int result = JOptionPane.showConfirmDialog(dialog,"Are you sure?", "Confirm",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(modelRow);
                        Listing listing = profile.getListingList().get(modelRow);
                        library.getListingRegistry().remove(listing);

                        try {
                            library.exportListings();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            };
            ButtonColumn deleteListingButton = new ButtonColumn(table,deleteListing,9);

            table.setRowSorter(sorter);
            table.setPreferredScrollableViewportSize(new Dimension(500, 70));
            table.setFillsViewportHeight(true);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setRowHeight(table.getRowHeight() + 50);
            JScrollPane scrollPane = new JScrollPane(table);

            add(scrollPane);
        }
        else{
            String [] columns = {"Email", "Title", "Author", "Price", "ISBN", "Edition", "Condition", "Image"};
            tableModel = new DefaultTableModel(columns,0);

            parseTable(tableModel,library,profile,false);

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            JTable table = new JTable(tableModel)      {
                //  Returning the Class of each column will allow different
                //  renderers to be used based on Class
                public Class getColumnClass(int column) {
                    return getValueAt(0, column).getClass();
                }
            };
            table.setRowSorter(sorter);
            table.setPreferredScrollableViewportSize(new Dimension(500, 70));
            table.setFillsViewportHeight(true);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setRowHeight(table.getRowHeight() + 50);
            JScrollPane scrollPane = new JScrollPane(table);

            add(scrollPane);
        }
    }

    /**
     * This method is used to fill the AccountListingTable with information, also used when visiting a Profile
     * @param library The Library instance containing the data needed to initialize
     * @param table The current state of the BookTable
     * @param isOwner Checks if table is for the current suer
     */
    public static void parseTable(DefaultTableModel table,Library library, Profile profile, boolean isOwner){
        List<Listing> listingList = library.getListings(profile);

        for(int i = 0; i < table.getRowCount(); i++){
            table.removeRow(i);
        }

        for(Listing l : listingList){
            Object[] row;
            ImageIcon ic = new ImageIcon(l.getImage().getPath().getAbsolutePath());
            Image img = ic.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            ic = new ImageIcon(img);
            if(isOwner){

                row = new Object[]{l.getSeller(), l.getTitle(), l.getAuthor(), String.valueOf(l.getPrice()), l.getIsbn(), l.getEdition(), l.getCondition(), ic, "Edit", "Delete"};
            }
            else{
                row = new Object[]{l.getSeller(), l.getTitle(), l.getAuthor(), String.valueOf(l.getPrice()), l.getIsbn(), l.getEdition(), l.getCondition(),ic};
            }
            table.addRow(row);
        }
    }
}
