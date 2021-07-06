import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;


class MyFrame
        extends JDialog
        implements ActionListener {

    // Components of the Form

    private final Container c;
    private final JLabel title;
    private final JButton back;
    public JTextField tname;
    private final JLabel tags;
    private final JButton sub;
    private final JButton next;
    private final JButton fin;
    private JLabel picLabel;
    private final JComboBox mgroup;
    public String filePath;
    public String bCode;
    public String dicTion;
    public int  position;
    public int listLength;
    public volatile boolean cont;
    public volatile boolean nextGate;
    public volatile boolean backGate;
    public volatile boolean chngNme;

    private final String[] Party = {"Birthday", "Wedding", "Bridal", "Shower", "Baby", "Christmas", "Old Years", "Graduation"};

    private final String[] Enter_Product_Group = {};

    private final String[] majorGroupsNames = {"Enter Product Group", "Party"};

    private final String[][] majorGroups = {Enter_Product_Group,Party};

    private final JCheckBox[] options = {null, null, null, null, null, null, null, null, null};

    // constructor, to initialize the components
    // with default values.
    public MyFrame(String path, String currentName, int index, int length, String dictioNary)
    {   filePath = path;
        position = index;
        cont = false;
        chngNme = false;
        listLength = length -1;
        bCode = currentName;
        dicTion = dictioNary;
        int chkWidth = 30;
        int chkLength = 200;
        int chkYPos = 130;

        if (index < listLength) {
            nextGate = true;
        } else if (index == listLength){
            nextGate = false;
        }

        if (index == 0) {
            backGate = false;
        } else if (index > 0) {
            backGate = true;
        }

        setTitle(bCode);
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

        tags = new JLabel("Tags");
        tags.setFont(new Font("Arial", Font.PLAIN, 15));
        tags.setSize(50,20);
        tags.setLocation(408,115);
        tags.setVisible(false);
        c.add(tags);

        sub = new JButton("Submit");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(70, 20);
        sub.setLocation(407, 320);
        sub.addActionListener(this);
        c.add(sub);

        back = new JButton("Back");
        back.setFont(new Font("Arial", Font.PLAIN, 15));
        back.setSize(70, 20);
        back.setLocation(408, 350);
        back.addActionListener(this);
        if (!backGate){
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
        if (!nextGate){
            next.setEnabled(false);
        } else{
            next.setEnabled(true);
        }
        c.add(next);

        mgroup = new JComboBox(majorGroupsNames);
        mgroup.setFont(new Font("Arial", Font.PLAIN, 15));
        mgroup.setSize(270, 20);
        mgroup.setLocation(405, 86);

        for (int i =0 ;i < options.length; i++){
            options[i] = new JCheckBox();
            options[i].setFont(new Font("Arial", Font.PLAIN, 15));
            options[i].setSize(chkLength,chkWidth);
            options[i].setLocation(408, chkYPos + (20 * i));
            options[i].setEnabled(false);
            options[i].setVisible(false);
            c.add(options[i]);
        }

        mgroup.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getItem().equals(majorGroupsNames[0])) {
                    tags.setVisible(false);
                    for (JCheckBox option : options) {
                        option.setEnabled(false);
                        option.setVisible(false);
                        option.setText("");
                    }
                } else {
                    tags.setVisible(true);
                    for (int j = 1; j < majorGroups.length; j++) {
                        if (e.getItem().equals(majorGroupsNames[j])) {
                            for (int i = 0; i < majorGroups[j].length; i++) {
                                options[i].setEnabled(true);
                                options[i].setVisible(true);
                                options[i].setText(majorGroups[j][i]);
                            }

                        }

                    }
                }
            }
        });
        c.add(mgroup);

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
        } catch (IOException ignored) {
        }
        setVisible(true);
    }

    // method actionPerformed()
    // to get the action performed
    // by the user and act accordingly
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sub) {
            if (tname.getText().replaceAll("\\s","").equals("") || mgroup.getSelectedItem() == majorGroupsNames[0]) {
                String def = "";
                tname.setText(def);
            } else {
                bCode = tname.getText();
                Path p = Path.of(filePath);
                try {
                    Files.move(p, p.resolveSibling(bCode + ".jpg"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                cont = true;
                chngNme = true;
                dicTion += "'"+ bCode +"'"+ ":" + "'";

                for (int i =0; i < majorGroups.length; i++) {
                    if (mgroup.getSelectedItem() == majorGroupsNames[i]) {
                        dicTion += majorGroupsNames[i] + ",";
                        for (int j = 0; j < majorGroups[i].length; j++){
                            if (options[j].isSelected()) {
                                dicTion += majorGroups[i][j] + ",";
                            }
                        }
                        dicTion += "',";
                    }
                }
                dispose();
            }
        }

        else if (e.getSource() == next) {
            if (position < listLength) {
                position += 1;
                dicTion += " ";
                cont = true;
                dispose();
            }
        }

        else if (e.getSource() == back) {
            position -= 1;
            dispose();
            cont = true;
        }

        else if (e.getSource() == fin) {
            position = -1;
            cont = true;
            dispose();
        }
    }

    public int getPosition(){
        return position;}

    public String getBarcode(){
        return bCode;}

}

public class Registration {

    public static void main(String[] args) throws InterruptedException
    {   String[] pathnames = fileName();
        StringBuilder src = new StringBuilder(path(date()).toString());
        int index = 0;
        String dictioNary = "";
        int terminator = 0;


        while (index != -1){
            src.append("/").append(pathnames[index]);
            MyFrame f = new MyFrame(src.toString(), pathnames[index], index, pathnames.length, dictioNary);
            while(!f.cont){
                Thread.sleep(60);
                ++ terminator;
                if (terminator > 10000){
                    System.exit(0);
                }
            }
            terminator = 0;
            if (f.chngNme) {
                pathnames[index] = f.getBarcode() + ".jpg";
                dictioNary = f.dicTion + " ";
            }
            src = new StringBuilder(path(date()).toString());
            index = f.getPosition();
        }
        dictioNary = "{" + dictioNary + "}";
        write(dictioNary);
        System.exit(0);
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
    public static void write(String dictionary)
    {
        try {
            String filename = "../Product list/" + date() + ".txt";
            FileWriter fw = new FileWriter(filename, true); //the true will append the new data
            fw.write( dictionary + System.lineSeparator());//appends the string to the file
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
}
