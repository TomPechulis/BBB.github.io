import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;

/**
 * The Login class creates the login window for the user
 *
 * @author  Chris Torres
 * @version 1.0
 * @since   2020-10-5
 */
public class Login {
    private boolean checkLogin = false;
    private Account curr = null;

    /**
     * This method gets the currently signed in Account
     * @return Account The currently signed in Account
     */
    public Account getAcc(){
        return curr;
    }

    /**
     * Creates login window
     * @param library The current instance of Library
     */
    public void getLogin(final Library library){
        final Object lock = new Object();

        final JFrame loginFrame = new JFrame("B.B.B - Login");
        final JPanel loginPanel = new JPanel(new GridLayout(3,1));

        loginFrame.setSize(400,150);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        final JLabel loginEmailLabel = new JLabel("Email:");
        final JTextField loginEmail = new JTextField();

        loginPanel.add(loginEmailLabel);
        loginPanel.add(loginEmail);

        final JLabel loginPasswordLabel = new JLabel("Password:");
        final JPasswordField loginPassword = new JPasswordField();

        loginPanel.add(loginPasswordLabel);
        loginPanel.add(loginPassword);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                if(loginEmail.getText().equals("") || loginPassword.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Error: None of the fields" +
                            " may be blank.", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                }

                else if(library.checkAccountRegistry(loginEmail.getText(),loginPassword.getText())){
                    JOptionPane.showMessageDialog(null,"Notice: You have been " +
                            "signed in.", "B.B.B - Login", JOptionPane.PLAIN_MESSAGE);

                    checkLogin = true;
                    curr = new Account(loginEmail.getText(), loginPassword.getText());
                    loginFrame.dispose();
                    synchronized(lock) {
                        lock.notify();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"Error: Wrong Username" +
                            " or Password.", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        final JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final JFrame createAccountFrame = new JFrame("B.B.B - Create Account");
                JPanel createAccountPanel = new JPanel(new GridLayout(3,1));

                createAccountFrame.setSize(300,150);
                createAccountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                createAccountFrame.setLocationRelativeTo(null);

                JLabel emailLabel = new JLabel("Email:");
                final JTextField emailText = new JTextField();

                createAccountPanel.add(emailLabel);
                createAccountPanel.add(emailText);

                JLabel passwordLabel = new JLabel("Password:");
                final JTextField password = new JTextField();

                createAccountPanel.add(passwordLabel);
                createAccountPanel.add(password);

                JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
                final JTextField confirmPassword = new JTextField();

                createAccountPanel.add(confirmPasswordLabel);
                createAccountPanel.add(confirmPassword);

                JButton createAccountButton = new JButton("Make Account");
                createAccountButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");

                        if(password.getText().equals("") || emailText.getText().equals("") || confirmPassword.getText().equals("")){
                            JOptionPane.showMessageDialog(null,"Error: None of the fields" +
                                    " may be blank.", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
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
                        }
                        else if(!emailText.getText().contains("@baylor.edu")){
                            JOptionPane.showMessageDialog(null,"Error: The email does not" +
                                    " belong to Baylor University", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                        }
                        else if(library.checkAccountRegistry(loginEmail.getText(),password.getText())){
                            JOptionPane.showMessageDialog(null,"Error: A User with that" +
                                    " email already exists.", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            library.addAccount(emailText.getText(),password.getText());
                            Account a = new Account(emailText.getText(), password.getText());
                            library.addProfile(a);

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

        loginPanel.add(loginButton);
        loginPanel.add(createAccountButton);

        loginFrame.add(loginPanel, BorderLayout.CENTER);
        loginFrame.setVisible(true);

        //Force Wait
        synchronized(lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if login successful or not
     * @return boolean Indicates status of login
     */
    public boolean checkLogin(){
        return checkLogin;
    }
}
