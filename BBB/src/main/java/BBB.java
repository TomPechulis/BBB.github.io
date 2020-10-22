import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BBB {

    //yo
    private static List<Account> registry = new ArrayList<>();
    private static File file = new File("src/main/resources/userRegistry.csv");

    public static void fillRegistry(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                //Get Email and Password
                String [] data = line.split(",");
                Account account = new Account(data[0],data[1]);

                registry.add(account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkRegistry(String email, String password){
        return registry.contains(new Account(email,password));
    }

    public static void addAccount(String email, String password){
        try {
            FileWriter fr = new FileWriter(file,true);
            fr.append(email);
            fr.append(",");
            fr.append(password);
            fr.append("\n");

            fr.flush();
            fr.close();

            registry.add(new Account(email,password));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        fillRegistry();

        final JFrame frame = new JFrame("B.B.B - Login");
        frame.setSize(400,150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel(new GridLayout(3,1));

        final JLabel eLabel = new JLabel("Email:");
        final JTextField email = new JTextField();
        panel.add(eLabel);
        panel.add(email);

        final JLabel pLabel = new JLabel("Password:");
        final JPasswordField password = new JPasswordField();
        panel.add(pLabel);
        panel.add(password);

        JButton login = new JButton("Login");
        login.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(email.getText().equals("") || password.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Error: None of the fields" +
                            " may be blank.", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                }
                else if(checkRegistry(email.getText(),password.getText())){
                    JOptionPane.showMessageDialog(null,"Notice: You have been " +
                            "signed in.", "B.B.B - Login", JOptionPane.PLAIN_MESSAGE);

                    frame.dispose();
                }
                else{
                    JOptionPane.showMessageDialog(null,"Error: Wrong Username" +
                            " or Password.", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        final JButton createAccount = new JButton("Create Account");
        createAccount.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFrame createAccountFrame = new JFrame("B.B.B - Create Account");
                createAccountFrame.setSize(200,150);
                createAccountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel createAccountPanel = new JPanel(new GridLayout(2,1));

                JLabel emailLabel = new JLabel("Email:");
                final JTextField emailText = new JTextField();
                createAccountPanel.add(emailLabel);
                createAccountPanel.add(emailText);

                JLabel pwLabel = new JLabel("Password:");
                final JTextField pword = new JTextField();
                createAccountPanel.add(pwLabel);
                createAccountPanel.add(pword);

                JButton createAccountButton = new JButton("Make Account");
                createAccountButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(pword.getText().equals("") || emailText.getText().equals("")){
                            JOptionPane.showMessageDialog(null,"Error: None of the fields" +
                                    " may be blank.", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                        }
                        else if(checkRegistry(email.getText(),pword.getText())){
                            JOptionPane.showMessageDialog(null,"Error: A User with that" +
                                    " Username already exists.", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            addAccount(email.getText(),pword.getText());

                            JOptionPane.showMessageDialog(null,"Notice: Your account" +
                                    " has been created", "B.B.B - Login", JOptionPane.PLAIN_MESSAGE);

                            createAccountFrame.dispose();
                        }
                    }
                });

                createAccountFrame.add(createAccountPanel,BorderLayout.CENTER);
                createAccountFrame.add(createAccountButton,BorderLayout.SOUTH);

                createAccountFrame.setVisible(true);
            }
        });

        panel.add(login);
        panel.add(createAccount);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
