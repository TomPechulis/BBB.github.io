public class BBB {
    public static void main(String [] args){
        Library library = new Library();

        Login login = new Login();
        login.getLogin(library);

        if(login.checkLogin()){
            System.out.println("phat bet");
        }
    }
}
