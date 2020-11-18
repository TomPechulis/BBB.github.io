import java.io.File;

/**
 * The ImageCustom stores the information about an image uploaded by a user.
 *
 * @author  Tyler Crumrine
 * @version 1.2
 * @since   2020-10-25
 */
public class ImageCustom {
    private String type = "zzz";
    private double size;
    private File path;

    /**
     * Gets the current path of the ImageCustom
     * @return String The current path of the ImageCustom
     */
    public File getPath() {
        return path;
    }
    /**
     * Sets the path of the Account
     * @param path The new File path
     */
    public void setPath(File path) {
        this.path = path;
    }

    /**
     * Class constructor
     * @param p The File passed by the user
     */
    ImageCustom(File p){
        this.path = p;

        String temp = p.getAbsolutePath();
        int count = 0;
        char[] ch = new char[3];
        for(int i = temp.length()-3; i < temp.length(); i++){
            ch[count] = temp.charAt(i);
            count++;
        }
        type = new String(ch);

        size = p.length();
    }

    /**
     * This method checks if the image is a JPG/PNG and less than 50 mb
     * @return boolean Checks if file meets type and size requirements
     */
    public boolean verifyImage(){
        return((type.equals("PNG") || type.equals("png") || type.equals("JPG") || type.equals("jpg")) && size <= 50000000.0);
    }
}
