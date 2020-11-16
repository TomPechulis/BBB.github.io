import java.io.File;

public class ImageCustom {
    private String type = "zzz";
    private double size;
    private File path;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

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

    public boolean verifyImage(){
        return((type.equals("PNG") || type.equals("png") || type.equals("JPG") || type.equals("jpg")) && size <= 50000000.0);
    }
}
