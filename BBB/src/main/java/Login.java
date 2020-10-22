import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Login {
    private boolean checkLogin = false;

    public void getLogin(final Library library){
        final Object lock = new Object();

        final JFrame loginFrame = new JFrame("B.B.B - Login");
        final JPanel loginPanel = new JPanel(new GridLayout(3,1));

        loginFrame.setSize(400,150);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
                JPanel createAccountPanel = new JPanel(new GridLayout(2,1));

                createAccountFrame.setSize(200,150);
                createAccountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JLabel emailLabel = new JLabel("Email:");
                final JTextField emailText = new JTextField();

                createAccountPanel.add(emailLabel);
                createAccountPanel.add(emailText);

                JLabel passwordLabel = new JLabel("Password:");
                final JTextField password = new JTextField();

                createAccountPanel.add(passwordLabel);
                createAccountPanel.add(password);

                JButton createAccountButton = new JButton("Make Account");
                createAccountButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(password.getText().equals("") || emailText.getText().equals("")){
                            JOptionPane.showMessageDialog(null,"Error: None of the fields" +
                                    " may be blank.", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                        }
                        else if(library.checkAccountRegistry(loginEmail.getText(),password.getText())){
                            JOptionPane.showMessageDialog(null,"Error: A User with that" +
                                    " Username already exists.", "B.B.B - Login", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            library.addAccount(emailText.getText(),password.getText());

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

    public boolean checkLogin(){
        return checkLogin;
    }
}
