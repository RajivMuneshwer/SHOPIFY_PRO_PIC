import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.stream.Stream;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JMenu;
import javax.swing.JMenuBar;



public class Registration {

    public static void main(String[] args) throws InterruptedException, IOException {
        int index = 0;
        int terminator = 0;
        boolean change = false;
        boolean noFile= false;
        File theDir = new File(date().toString());
        if (!theDir.exists()) {
            theDir.mkdirs();
            File empty = new File("no-image-icon.jpg");
            Files.copy(empty.toPath(), Path.of(theDir + "/" + empty.getName()), StandardCopyOption.REPLACE_EXISTING);
            noFile = true;
        }
        String[] pathnames = fileName();
        int[] majorList = new int[pathnames.length];
        boolean[][] grpList = new boolean[pathnames.length][MyFrame.options.length];
        StringBuilder src = new StringBuilder(path(date()).toString());
        int length = pathnames.length;
        if (length == 0){
            File empty = new File("no-image-icon.jpg");
            Files.copy(empty.toPath(), Path.of(theDir + "/" + empty.getName()), StandardCopyOption.REPLACE_EXISTING);
            noFile = true;
            pathnames = fileName();
            majorList = new int[pathnames.length];
            grpList = new boolean[pathnames.length][MyFrame.options.length];
            ++length;
        }
        if (pathnames[0].equals("no-image-icon.jpg")){
            noFile = true;
        }

        while (index != -1) {
            try {
                src.append("/").append(pathnames[index]);
            } catch (ArrayIndexOutOfBoundsException e){
                if (pathnames.length == 0){
                    File empty = new File("no-image-icon.jpg");
                    Files.copy(empty.toPath(), Path.of(theDir + "/" + empty.getName()), StandardCopyOption.REPLACE_EXISTING);
                    noFile = true;
                    pathnames = fileName();
                    majorList = new int[pathnames.length];
                    grpList = new boolean[pathnames.length][MyFrame.options.length];
                    ++length;
                }
                index = 0;
                src.append("/").append(pathnames[index]);

            }
            System.out.println(pathnames[index]);
            System.out.println(src);
            MyFrame f = new MyFrame(src.toString(), pathnames[index], index, length,
                    change, majorList[index], grpList[index], noFile);
            while (!f.cont) {
                Thread.sleep(60);
                ++terminator;
                if (terminator > 10000) {
                    System.exit(0);
                }
            }
            terminator = 0;
            change = f.chngNme;
            if (change) {
                pathnames[index] = f.getBarcode() + ".jpg";
                majorList[index] = f.tckMajorGroups;
                grpList[index] = f.tckGroups;
            }

            src = new StringBuilder(path(date()).toString());
            index = f.getPosition();
            noFile = f.nofile;

            if (f.iM){
                System.out.println("chanfed");
                pathnames = fileName();
                length= pathnames.length;
                majorList = new int[pathnames.length];
                grpList = new boolean[pathnames.length][MyFrame.options.length];
                index = f.getPosition();
            }


        }

    }


    public static String[] fileName() {
        Path p1 = path(date());
        String pathname = p1.toString();
        File f = new File(pathname);
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File f, String name) {
                return name.endsWith("jpg");
            }
        };
        String[] fileName = f.list(filter); //makes a list of the files that end with "jpg" to exclude .DS_Store
        fileName = Stream.of(fileName).sorted().toArray(String[]::new);
        return fileName;
    }

    public static Path path(LocalDate date) {
        String date_str = date.toString();
        return Paths.get(date_str);
    }

    public static LocalDate date() {
        return java.time.LocalDate.now();
    }

}




class MyFrame
        extends JDialog
        implements ActionListener {

    // Components of the Form

    private final Container c;
    private final JLabel title;
    private final JButton back;
    public JTextField tname;
    private final JLabel tags;
    private JLabel counter;
    private final JButton sub;
    private final JButton next;
    private final JButton fin;
    private final JButton copy;
    private JFrame pFrame;
    private JLabel picLabel;
    private final JComboBox mgroup;
    public String filePath;
    public String bCode;
    public String gName;
    public String SQLtags;
    public JMenuBar menuBar;
    public JMenu fiLe;
    public JMenuItem imPort;
    public JMenuItem oPen;
    public JMenuItem deLete;
    public boolean iM;
    public int position;
    public int listLength;
    public int tckMajorGroups;
    public boolean[] tckGroups;
    public volatile boolean cont;
    public volatile boolean nextGate;
    public volatile boolean backGate;
    public volatile boolean chngNme;
    public volatile boolean nofile;

    private final String[] Party = {"Birthday", "Wedding", "Bridal", "Shower", "Baby", "Christmas", "Old Years", "Graduation"};
    private final String[] Enter_Product_Group = {};
    private final String[] majorGroupsNames = {"Enter Product Group", "Party"};
    private final String[][] majorGroups = {Enter_Product_Group, Party};
    public static final JCheckBox[] options = {null, null, null, null, null, null, null, null, null};

    // constructor, to initialize the components
    // with default values.
    public MyFrame(String path, String currentName, int index, int length, boolean change,
                   int majorTicket, boolean[] grpTicket, boolean noFile) {
        cont = false;
        nofile = noFile;
        filePath = path;
        position = index;
        chngNme = change;
        bCode = currentName;
        tckGroups = grpTicket;
        listLength = length - 1;
        tckMajorGroups = majorTicket;
        iM = false;
        int chkWidth = 30;
        int chkYPos = 110;
        int chkLength = 200;

        if (index < listLength) {
            nextGate = true;
        } else if (index == listLength) {
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
        title.setLocation(410, 5);
        c.add(title);

        tname = new JTextField();
        tname.setFont(new Font("Arial", Font.PLAIN, 15));
        tname.setSize(250, 20);
        tname.setLocation(405, 36);
        if (chngNme || majorTicket != 0) {
            tname.setText(bCode.substring(0, bCode.length() - 4));
        }
        c.add(tname);

        tags = new JLabel("Tags");
        tags.setFont(new Font("Arial", Font.PLAIN, 15));
        tags.setSize(50, 20);
        tags.setLocation(408, 95);
        tags.setVisible(false);
        c.add(tags);

        sub = new JButton("Submit");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(70, 20);
        sub.setLocation(407, 290);
        sub.addActionListener(this);
        c.add(sub);

        back = new JButton("Back");
        back.setFont(new Font("Arial", Font.PLAIN, 15));
        back.setSize(70, 20);
        back.setLocation(408, 320);
        back.addActionListener(this);
        back.setEnabled(backGate);
        c.add(back);

        fin = new JButton("Finish");
        fin.setFont(new Font("Arial", Font.PLAIN, 15));
        fin.setSize(70, 20);
        fin.setLocation(625, 320);
        fin.addActionListener(this);
        c.add(fin);

        next = new JButton("Next");
        next.setFont(new Font("Arial", Font.PLAIN, 15));
        next.setSize(70, 20);
        next.setLocation(550, 320);
        next.addActionListener(this);
        if (!nextGate) {
            next.setEnabled(false);
        } else {
            next.setEnabled(true);
        }
        c.add(next);

        copy = new JButton("...");
        copy.setFont(new Font("Arial", Font.PLAIN, 15));
        copy.setSize(20, 20);
        copy.setLocation(656, 36);
        copy.addActionListener(this);
        c.add(copy);


        counter = new JLabel(String.valueOf(position + 1) + "/" + length);
        counter.setFont(new Font("Arial", Font.PLAIN,11));
        counter.setSize(15,15);
        counter.setLocation(10,336);
        c.add(counter);

        menuBar = new JMenuBar();
        fiLe = new JMenu("File");
        imPort = new JMenuItem("Import");
        oPen = new JMenuItem("Open");
        deLete = new JMenuItem("Delete");
        fiLe.add(deLete);
        fiLe.add(imPort);
        fiLe.add(oPen);
        imPort.addActionListener(this);
        oPen.addActionListener(this);
        deLete.addActionListener(this);
        menuBar.add(fiLe);
        setJMenuBar(menuBar);



        for (int i = 0; i < options.length; i++) {
            options[i] = new JCheckBox();
            options[i].setFont(new Font("Arial", Font.PLAIN, 15));
            options[i].setLocation(408, chkYPos + (20 * i));
            options[i].setSize(chkLength, chkWidth);
            options[i].setEnabled(false);
            options[i].setVisible(false);
            c.add(options[i]);
        }
        if (chngNme || majorTicket != 0) {
            for (int j = 0; j < majorGroups[tckMajorGroups].length; j++) {
                options[j].setEnabled(true);
                options[j].setVisible(true);
                options[j].setSelected(tckGroups[j]);
                options[j].setText(majorGroups[tckMajorGroups][j]);
            }
        }

        mgroup = new JComboBox(majorGroupsNames);
        mgroup.setFont(new Font("Arial", Font.PLAIN, 15));
        mgroup.setSize(270, 20);
        mgroup.setLocation(405, 66);
        if (chngNme || majorTicket != 0) {
            mgroup.setSelectedIndex(tckMajorGroups);
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

        Image myPicture;
        BufferedImage myImage;
        try {
            int width = 400;
            int height = 335;
            File f = new File(filePath);
            myImage = ImageIO.read(f);
            myPicture = myImage.getScaledInstance(width, height, Image.SCALE_FAST);
            picLabel = new JLabel(new ImageIcon(myPicture));
            picLabel.setSize(width, height);
            picLabel.setLocation(0, 0);
            c.add(picLabel);
        } catch (IOException ignored) {
        }

        if (nofile){
            title.setVisible(false);
            tname.setVisible(false);
            sub.setVisible(false);
            copy.setVisible(false);
            counter.setVisible(false);
            mgroup.setVisible(false);


        }

        setVisible(true);
    }




    // method actionPerformed()
    // to get the action performed
    // by the user and act accordingly
    public void actionPerformed(ActionEvent e) {
        JDBCSelectTest jdbc = new JDBCSelectTest();
        fileChooser fChoose = new fileChooser();

        if (e.getSource() == sub) {
            boolean there;
            try{
                there = jdbc.there(tname.getText());
            } catch (FileNotFoundException fileNotFoundException) {
                there = false;
                fileNotFoundException.printStackTrace();
            }
            boolean empty = tname.getText().replaceAll("\\s", "").equals("")
                    || mgroup.getSelectedItem() == majorGroupsNames[0];
            if (empty || !there){
                pFrame = new JFrame("INVALID ENTRY");
                if (empty) {
                    JOptionPane.showMessageDialog(pFrame,
                            "ENTER A Barcode and SELECT A Product Group",
                            "MISSING ENTRY",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(pFrame,
                            "Enter A Valid Barcode",
                            "INVALID ENTRY",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                bCode = tname.getText();
                Path p = Path.of(filePath);
                tckMajorGroups = mgroup.getSelectedIndex();
                tckGroups = new boolean[options.length];
                try {
                    Files.move(p, p.resolveSibling(bCode + ".jpg"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                cont = true;
                chngNme = true;
                SQLtags = "";


                for (int i = 0; i < majorGroups.length; i++) {
                    if (mgroup.getSelectedItem() == majorGroupsNames[i]) {
                        for (int j = 0; j < majorGroups[i].length; j++) {
                            if (options[j].isSelected()) {
                                SQLtags += majorGroups[i][j] + ",";
                                tckGroups[j] = true;
                            }
                        }
                    }
                }
                try {
                    jdbc.write(bCode,
                            String.valueOf(tckMajorGroups),
                            mgroup.getSelectedItem().toString(),
                            SQLtags,
                            LocalDate.now().toString()
                    );
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                dispose();
            }
        } else if (e.getSource() == next) {
            if (position < listLength) {
                cont = true;
                position += 1;
                SQLtags = "";
                chngNme = false;
                tckMajorGroups = 0;
                tckGroups = new boolean[options.length];
                dispose();

            }
        } else if (e.getSource() == back) {
            cont = true;
            position -= 1;
            chngNme = false;
            dispose();
        } else if (e.getSource() == fin) {
            System.exit(0);
        } else if (e.getSource() == copy) {
            tname.setText(bCode.substring(0, bCode.length() - 4));
        } else if (e.getSource() == imPort){
            try {
                fChoose.importer();
                position = 0;
                cont = true;
                iM = true;
                nofile = false;
                dispose();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else if (e.getSource() == oPen){
            try {
                position = fChoose.openPosition(position, fChoose.fileName());
                cont =true;
                iM= fChoose.usedImport;
                dispose();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else if (e.getSource() == deLete){
            fChoose.deleter();
            cont = true;
            iM = true;
            dispose();
        }
    }

    public int getPosition() {
        return position;
    }

    public String getBarcode() {
        return bCode;
    }

}


class JDBCSelectTest {

    public static boolean there(String barcode) throws FileNotFoundException {
        String url = "jdbc:mysql://localhost:3306/test_schema";
        String username = "root";
        String password = password();
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to Database");
            String sql = "SELECT count(*) FROM PLUS WHERE CodeText = " + barcode;
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            result.next();
            if (result.getInt(1) == 0) {
                result.close();
                connection.close();
                return false;
            } else {
                result.close();
                connection.close();
                return true;
            }
        } catch (SQLException throwables) {
            return false;
        }
    }


    public static void write(String barCode, String groupCode, String groupName, String tags,
                             String date

    )
            throws FileNotFoundException {
        String url = "jdbc:mysql://localhost:3306/test_schema";
        String username = "root";
        String password = password();
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to Database");
            String sql = "INSERT INTO Products (CodeNumber, GroupCode, GroupName, Tags, DateEdited)" +
                    " VALUES (" + barCode + "," + groupCode + ",\"" + groupName + "\",\"" + tags +
                    "\",\"" + date +
                    "\")" +
                    " ON DUPLICATE KEY UPDATE" +
                    " CodeNumber = " + barCode + "," +
                    " GroupCode = " + groupCode+ "," +
                    " GroupName = \"" + groupName + "\"," +
                    "Tags = \"" + tags + "\"," +
                    " DateEdited = \"" + date +"\""
                    ;
            System.out.println(sql);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    public static String password() throws FileNotFoundException {
        File config = new File("config.txt");
        Scanner con = new Scanner(config);
        return con.nextLine();
    }

}



class fileChooser {

    public static boolean usedImport = false;


    public static void importer() throws IOException {

        File[] files;
        int response;
        JFileChooser chooser = new JFileChooser(".");
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg"));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        response = chooser.showOpenDialog(null);

        if (response == JFileChooser.FILES_ONLY) {
            if (chooser.getSelectedFile().getName().equals("no-image-icon.jpg")){

            }else{
                Files.deleteIfExists(Paths.get(date() + "/" + "no-image-icon.jpg" ));

                usedImport = true;
                files = chooser.getSelectedFiles();
                File theDir = new File(String.valueOf(date()));
                for (File file : files) {
                    Files.copy(file.toPath(), Path.of(theDir + "/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    public static int openPosition(int index, String[] inDirectory) throws IOException {
        File file;
        int response;
        JFileChooser chooser = new JFileChooser(path(date()).toString());
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg"));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        response = chooser.showOpenDialog(null);


        if(response == JFileChooser.FILES_ONLY) {
            file = chooser.getSelectedFile();
            System.out.println("file name: "+ file.getName());
            if (file.getParentFile().toString().endsWith(path(date()).toString())) {
                System.out.println("In Directory");
                return position(file, inDirectory);
            } else{
                System.out.println("Out Directory");
                File theDir = new File(date().toString());
                Files.copy(file.toPath(), Path.of(theDir + "/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);
                return position(file, fileName());
            }
        }
        return index;
    }

    public static void deleter(){

        File[] files;
        int response;
        JFileChooser chooser = new JFileChooser(".");
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg"));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        response = chooser.showOpenDialog(null);

        if (response ==  JFileChooser.FILES_ONLY){
            int r = JOptionPane.showConfirmDialog(null, "Delete (" + chooser.getSelectedFiles().length+ ") Files");
            if (r == 0){
                files = chooser.getSelectedFiles();
                for (File f: files){
                    f.delete();
                }
            }

        }
    }


    public static int position(File file, String[] inDirectory){
        int index = -1;
        for (int i = 0; i < inDirectory.length; i++) {
            if (inDirectory[i].equals(file.getName())) {
                index =  i;
                System.out.println(file.getName());
            }
        }
        System.out.println("index = " + index);
        return index;
    }

    public static String[] fileName() {
        Registration r = new Registration();
        return r.fileName();
    }

    public static LocalDate date() {
        return java.time.LocalDate.now();
    }

    public static Path path(LocalDate date) {
        String date_str = date.toString();
        return Paths.get(date_str);
    }

}
