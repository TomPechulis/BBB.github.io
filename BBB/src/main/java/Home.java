import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.List;

public class Home {
    Library library;

    public Home(Library i){
        library = i;
    }

    public void getHome(Account a) throws IOException {

        final JFrame homeFrame = new JFrame("B.B.B - Main Menu");

        final JTabbedPane pane = new JTabbedPane();

        homeFrame.setSize(700, 300);

        JPanel jComp1 = getMyAccount(a);
        pane.addTab("My Account", jComp1);

        pane.addTab("Book Search", new BookTable());

        pane.addTab("Seller Search", new SellerTable());

        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.add(pane);
        homeFrame.setVisible(true);
    }

    public JPanel getMyAccount(Account a) throws IOException {

        JPanel frame = new JPanel();
        frame.setLayout(new BoxLayout(frame, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel("Account: " + a.getEmail());

        final Profile[] profile = {library.getProfile(a)};

        JLabel label2 = new JLabel("Rating: " + profile[0].getRating());
        JLabel label3;
        if(profile[0].getInit()){
            label3 = new JLabel((Icon) profile[0].getProfilePic());
        }
        else{
            label3 = new JLabel();
        }
        JButton newListingButton = new JButton("New Listing");

        newListingButton.addActionListener(e -> {
            profile[0] = library.getProfile(a);
            Listing l = profile[0].newListing();
            library.addListing(l);
        });

        JButton editProfileButton = new JButton("Edit Profile");

        editProfileButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e){
                profile[0] = library.getProfile(a);
                profile[0].editProfile();
            }
        });

        frame.add(label);
        frame.add(label2);
        frame.add(label3);
        frame.add(newListingButton);
        frame.add(editProfileButton);
        AccountListingTable alt = new AccountListingTable(library, profile[0]);
        frame.add(alt);

        return frame;
    }

}

class BookTable extends JPanel {

    private final JTextField filterSearchText;
    //private final JTextField filterTitleText;
    //private final JTextField filterISBNText;
    private final TableRowSorter<MyTableModel> sorter;

    public BookTable() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        MyTableModel model = new MyTableModel();
        sorter = new TableRowSorter<>(model);
        JTable table = new JTable(model);
        table.setRowSorter(sorter);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

        //Whenever filterText changes, invoke newFilter.
        filterSearchText.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        if(toggle.isSelected()) {
                            newAuthorFilter();
                        }else if(toggle2.isSelected()){
                            newTitleFilter();
                        }else if(toggle3.isSelected()){
                            newISBNFilter();
                        }
                    }
                    public void insertUpdate(DocumentEvent e) {
                        if(toggle.isSelected()) {
                            newAuthorFilter();
                        }else if(toggle2.isSelected()){
                            newTitleFilter();
                        }else if(toggle3.isSelected()){
                            newISBNFilter();
                        }
                    }
                    public void removeUpdate(DocumentEvent e) {
                        if(toggle.isSelected()) {
                            newAuthorFilter();
                        }else if(toggle2.isSelected()){
                            newTitleFilter();
                        }else if(toggle3.isSelected()){
                            newISBNFilter();
                        }
                    }
                });
        l1.setLabelFor(filterSearchText);
        form.add(filterSearchText);


        add(form);

        add(scrollPane);
    }

    /**
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void newAuthorFilter() {
        RowFilter<MyTableModel, Object> rf;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(filterSearchText.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void newTitleFilter() {
        RowFilter<MyTableModel, Object> rf;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(filterSearchText.getText(), 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void newISBNFilter() {
        RowFilter<MyTableModel, Object> rf;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(filterSearchText.getText(), 2);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }


    class MyTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Title",
                "Author",
                "ISBN",
                "edition",
                "Price",
                "Seller"};

        private final Object[][] data;

        MyTableModel() {
            data = new Object[100][6];

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(new File("src/main/resources/listingRegistry.csv")));
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            String line;
            try {
                int row = 0;
                while (true) {
                    assert reader != null;
                    if ((line = reader.readLine()) == null) break;
                    String[] split = line.split(",");
                    for (int col = 0; col < 6; col++) {
                        data[row][col] = split[col];
                    }
                    row++;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }


        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            return col >= 2;
        }

    }
}

class SellerTable extends JPanel {
    private final JTextField filterAnimalText;
    private final JTextField filterIdText;
    private final TableRowSorter<MyTableModel> sorter;

    public SellerTable() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        MyTableModel model = new MyTableModel();
        sorter = new TableRowSorter<MyTableModel>(model);
        JTable table = new JTable(model);
        table.setRowSorter(sorter);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel form = new JPanel();
        JLabel l1 = new JLabel("Seller:", SwingConstants.TRAILING);

        form.add(l1);
        filterAnimalText = new JTextField(10);

        //Whenever filterText changes, invoke newFilter.
        filterAnimalText.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newSellerFilter();
                    }

                    public void insertUpdate(DocumentEvent e) {
                        newSellerFilter();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        newSellerFilter();
                    }
                });
        l1.setLabelFor(filterAnimalText);
        form.add(filterAnimalText);
        JLabel l2 = new JLabel("Rating:", SwingConstants.TRAILING);
        form.add(l2);
        filterIdText = new JTextField(10);
        filterIdText.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        newRatingFilter();
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        // TODO Auto-generated method stub
                        newRatingFilter();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        // TODO Auto-generated method stub
                        newRatingFilter();
                    }
                }

        );
        form.add(filterIdText);
        add(form);

        add(scrollPane);
    }

    /**
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void newSellerFilter() {
        RowFilter<MyTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(filterAnimalText.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void newRatingFilter() {
        RowFilter<MyTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(filterIdText.getText(), 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    class MyTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Sellers",
                "Rating"};

        private final Object[][] data;

        MyTableModel() {
            data = new Object[100][2];

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(new File("src/main/resources/profileRegistry.csv")));
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            String line = null;
            try {
                int row = 0;
                while (true) {
                    assert reader != null;
                    if ((line = reader.readLine()) == null) break;
                    String[] split = line.split(",");
                    for (int col = 0; col < 2; col++) {
                        data[row][col] = split[col];
                    }
                    row++;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }


        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

    }
}

class AccountListingTable extends JPanel {
    private boolean DEBUG = false;
    Library library = null;
    Profile profile = null;

    public AccountListingTable(Library library, Profile profile) throws IOException {
        super();

        this.library = library;
        this.profile = profile;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String [] columns = {"Email", "Title", "Author", "ISBN", "Price", "Edition", "Condition", "Image"};
        DefaultTableModel tableModel = new DefaultTableModel(columns,0);

        parseTable(tableModel,library,profile);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        JTable table = new JTable(tableModel);
        table.setRowSorter(sorter);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);
    }

    public static void parseTable(DefaultTableModel table,Library library, Profile profile) throws IOException {
        List<Listing> listingList = library.getListings(profile);

        for(Listing l : listingList){
            String [] row = {l.getSeller(),l.getTitle(),l.getAuthor(), String.valueOf(l.getPrice()),l.getEdition(),l.getCondition(),""};
            table.addRow(row);
        }
    }
}
