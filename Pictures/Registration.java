import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.nio.file.Files;


class MyFrame
        extends JDialog
        implements ActionListener {

    // Components of the Form
    private Container c;
    private JLabel title;
    public JTextField tname;
    private JTextArea tout;
    private JButton sub;
    private JButton next;
    private JButton fin;
    private JButton back;
    private JLabel picLabel;
    public String filePath;
    public String bcode;
    public int  position;
    public int list_length;
    public volatile boolean cont;
    public volatile boolean nextGate;
    public volatile boolean backGate;
    public volatile boolean chngNme;


    // constructor, to initialize the components
    // with default values.
    public MyFrame(String path, String currentName, int index, int length)
    {   filePath = path;
        position = index;
        cont = false;
        chngNme = false;
        list_length = length -1;
        bcode = currentName;


        if (index < list_length) {
            nextGate = true;
        }
        else if (index == list_length){
            nextGate = false;
        }

        if (index == 0) {
            backGate = false;
        }
        else if (index > 0){
            backGate = true;
        }

        setTitle(bcode);
        setBounds(500, 90, 700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        title = new JLabel("Edit Barcode");
        title.setFont(new Font("Arial", Font.PLAIN, 15));
        title.setSize(100, 30);
        title.setLocation(410, 25);
        c.add(title);

        tname = new JTextField();
        tname.setFont(new Font("Arial", Font.PLAIN, 15));
        tname.setSize(250, 20);
        tname.setLocation(405, 56);
        c.add(tname);
        String code = tname.getText();

        sub = new JButton("Submit");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(70, 20);
        sub.setLocation(407, 86);
        sub.addActionListener(this);
        c.add(sub);

        back = new JButton("Back");
        back.setFont(new Font("Arial", Font.PLAIN, 15));
        back.setSize(70, 20);
        back.setLocation(408, 350);
        back.addActionListener(this);
        if (backGate == false){
            back.setEnabled(false);
        } else{
            back.setEnabled(true);
        }
        c.add(back);

        fin = new JButton("Finish");
        fin.setFont(new Font("Arial", Font.PLAIN, 15));
        fin.setSize(70, 20);
        fin.setLocation(625, 350);
        fin.addActionListener(this);
        c.add(fin);

        next = new JButton("Next");
        next.setFont(new Font("Arial", Font.PLAIN, 15));
        next.setSize(70, 20);
        next.setLocation(550, 350);
        next.addActionListener(this);
        if (nextGate == false){
            next.setEnabled(false);
        } else{
            next.setEnabled(true);
        }

        c.add(next);

        Image myPicture = null;
        BufferedImage myImage = null;
        try {
            int width = 400;
            int height = 400;
            File f = new File(filePath);
            myImage = ImageIO.read(f);
            myPicture = myImage.getScaledInstance(width,height, Image.SCALE_FAST);
            picLabel = new JLabel(new ImageIcon(myPicture));
            picLabel.setSize(width,height);
            picLabel.setLocation(0,0);
            c.add(picLabel);
        } catch (IOException e) {
        }
        setVisible(true);
    }

    // method actionPerformed()
    // to get the action performed
    // by the user and act accordingly
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sub) {
            if (tname.getText().replaceAll("\\s","").equals("")) {
                String def = "";
                tname.setText(def);
            } else {
                bcode = tname.getText();
                Path path = Paths.get(filePath);
                Path p = Path.of(filePath);
                try {
                    Files.move(p, p.resolveSibling(bcode + ".jpg"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                cont = true;
                chngNme = true;
                dispose();

            }
        }

        else if (e.getSource() == next) {
            if (position < list_length) {
                position += 1;
                dispose();
                cont = true;
            }
        }

        else if (e.getSource() == back) {
            position -= 1;
            dispose();
            cont = true;
        }

        else if (e.getSource() == fin) {
            System.exit(0);
        }
    }

    public int getPosition(){
        return position;}

    public String getBarcode(){
        return bcode;}

}

public class Registration {
    public static void main(String[] args) throws IOException, InterruptedException
    {   String[] pathnames = fileName();
        String src = path(date()).toString();
        int index = 0;

        while (index < (pathnames.length -1)|| index > 0){
            src += "/" + pathnames[index];
            MyFrame f = new MyFrame(src, pathnames[index], index, pathnames.length);
            while(f.cont == false){
                Thread.sleep(50);
                continue;
            }
            if (f.chngNme == true) {
                pathnames[index] = f.getBarcode() + ".jpg";
            }
            src = path(date()).toString();
            index = f.getPosition();

        }
    }


    public static String[] fileName(){
        Path p1 = path(date());
        String pathname = p1.toString();
        File f = new File(pathname);
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File f, String name) {
                return name.endsWith("jpg");
            }
        };
        String[] fileName = f.list(filter);
        return fileName;
    }

    public static Path path(LocalDate date){
        String date_str = date.toString();
        return Paths.get(date_str);
    }
    public static LocalDate date(){
        return java.time.LocalDate.now();
    }
}
///time to wait((30*60000)) the System.exit(0)
/// instead just make it update the tout field so it confirms the update went through