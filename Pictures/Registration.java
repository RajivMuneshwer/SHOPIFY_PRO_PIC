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
import java.time.LocalDate;
import java.nio.file.Files;


class MyFrame
        extends JDialog
        implements ActionListener {

    // Components of the Form
    private Container c;
    private JLabel title;
    private JTextField tname;
    private JTextArea tout;
    private JButton sub;
    private JButton reset;
    private JLabel picLabel;
    public String filePath;


    // constructor, to initialize the components
    // with default values.
    public MyFrame(String path, String currentName)
    {   filePath = path;
        setTitle("Barcode Editor");
        setBounds(500, 90, 700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        title = new JLabel("Enter Barcode");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(450, 30);
        c.add(title);

        tname = new JTextField();
        tname.setFont(new Font("Arial", Font.PLAIN, 15));
        tname.setSize(190, 20);
        tname.setLocation(450, 100);
        c.add(tname);
        String code = tname.getText();

        sub = new JButton("Submit");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(100, 20);
        sub.setLocation(425, 140);
        sub.addActionListener(this);
        c.add(sub);

        reset = new JButton("Reset");
        reset.setFont(new Font("Arial", Font.PLAIN, 15));
        reset.setSize(100, 20);
        reset.setLocation(550, 140);
        reset.addActionListener(this);
        c.add(reset);

        tout = new JTextArea();
        tout.setFont(new Font("Arial", Font.BOLD, 15));
        tout.setSize(175, 40);
        tout.setLocation(480, 200);
        tout.setLineWrap(true);
        tout.setEditable(false);
        tout.setText("Current Barcode:\n " + currentName);
        tout.setOpaque(false);
        c.add(tout);

        Image myPicture = null;
        BufferedImage myImage = null;
        try {
            int width = 400;
            int height = 400;
            File f = new File(filePath);
            myImage = ImageIO.read(f);
            myPicture = myImage.getScaledInstance(width,height, Image.SCALE_DEFAULT);
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
            String barcode = tname.getText();
            Path path = Paths.get(filePath);
            dispose();

            try {
                Files.move(path, path.resolveSibling(barcode + ".jpg"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        else if (e.getSource() == reset) {
            String def = "";
            tname.setText(def);
        }
    }

    public String getCode(){
        return tname.getText();
    }
}

public class Registration {
    public static void main(String[] args) throws IOException
    {   String[] pathnames = fileName();
        String src = path(date()).toString();

        for (String pathname : pathnames){
            src += "/" + pathname;
            MyFrame f = new MyFrame(src,pathname);
            src = path(date()).toString();
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
