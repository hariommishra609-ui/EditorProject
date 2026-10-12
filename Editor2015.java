import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import java.awt.font.*;
import javax.swing.event.*;

public class Editor2015 extends JFrame implements ActionListener, KeyListener
{
    JFrame jf;
    JMenuBar mb;
    JMenu mFile, mEdit, mFormat, mRun, mHelp;

    JMenuItem mFileMenuItem[] = new JMenuItem[6];
    String strFileMenuItem[] = {"New", "Open", "Save", "Save As...", "Print...", "Exit"};

    JMenuItem mEditMenuItem[] = new JMenuItem[6];
    String strEditMenuItem[] = {"Cut", "Copy", "Paste", "Delete", "Select All", "Time & Date"};

    JMenuItem mFormatMenuItem[] = new JMenuItem[1];
    String strFormatMenuItem[] = {"Color"};

    JMenuItem mRunMenuItem[] = new JMenuItem[2];
    String strRunMenuItem[] = {"Compile", "Run.."};

    JTextArea ta, ta1;
    JScrollPane sp, sp1;

    int x;
    FileDialog fd;
    FileInputStream fis;
    FileOutputStream fos;
    byte b[];
    Runtime r;

    String filename = "";
    String fname = "";
    String result = "";
    boolean textchange;
    JColorChooser cc;

    public Editor2015()
    {
        jf = new JFrame();
        jf.setLayout(null);

        mb = new JMenuBar();
        jf.setJMenuBar(mb);

        mFile = new JMenu("File");
        mFile.setMnemonic(KeyEvent.VK_F);
        mb.add(mFile);

        mEdit = new JMenu("Edit");
        mEdit.setMnemonic(KeyEvent.VK_E);
        mb.add(mEdit);

        mFormat = new JMenu("Format");
        mFormat.setMnemonic(KeyEvent.VK_O);
        mb.add(mFormat);

        mRun = new JMenu("Run");
        mRun.setMnemonic(KeyEvent.VK_R);
        mb.add(mRun);

        mHelp = new JMenu("Help");
        mHelp.setMnemonic(KeyEvent.VK_H);
        mb.add(mHelp);

        JMenuItem mHelpMenuItem = new JMenuItem("About Editor");
        mHelp.add(mHelpMenuItem);
        mHelpMenuItem.addActionListener(this);

        for (x = 0; x < 6; x++)
        {
            mFileMenuItem[x] = new JMenuItem(strFileMenuItem[x]);
            if (x == 4 || x == 5)
                mFile.addSeparator();
            mFile.add(mFileMenuItem[x]);
            mFileMenuItem[x].addActionListener(this);
        }

        for (x = 0; x < 6; x++)
        {
            mEditMenuItem[x] = new JMenuItem(strEditMenuItem[x]);
            if (x == 4)
                mEdit.addSeparator();
            mEdit.add(mEditMenuItem[x]);
            mEditMenuItem[x].addActionListener(this);
        }

        for (x = 0; x < 1; x++)
        {
            mFormatMenuItem[x] = new JMenuItem(strFormatMenuItem[x]);
            mFormat.add(mFormatMenuItem[x]);
            mFormatMenuItem[x].addActionListener(this);
        }

        for (x = 0; x < 2; x++)
        {
            mRunMenuItem[x] = new JMenuItem(strRunMenuItem[x]);
            mRun.add(mRunMenuItem[x]);
            mRunMenuItem[x].addActionListener(this);
        }

        ta = new JTextArea(50, 50);
        ta1 = new JTextArea(50, 50);

        ta.setFont(new Font("Courier New", Font.PLAIN, 20));
        ta1.setFont(new Font("Courier New", Font.PLAIN, 20));

        sp = new JScrollPane(ta);
        sp1 = new JScrollPane(ta1);

        sp.setBounds(0, 0, 1350, 520);
        sp1.setBounds(0, 530, 1350, 135);

        jf.add(sp);
        jf.add(sp1);

        ta.addKeyListener(this);
        r = Runtime.getRuntime();

        textchange = false;

        mFileMenuItem[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        mFileMenuItem[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        mFileMenuItem[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        mEditMenuItem[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        mEditMenuItem[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        mEditMenuItem[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        jf.setTitle("Editor2015 : Untitled");
        jf.setLocation(0, 0);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(1500, 740);
        jf.setVisible(true);
    }

    public void keyPressed(KeyEvent te)
    {
        if (te.getSource() == ta)
            textchange = true;
    }

    public void keyReleased(KeyEvent te) {}
    public void keyTyped(KeyEvent te) {}

    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();

        if (command.equals("New"))
        {
            int ask;

            if (textchange == true)
            {
                ask = JOptionPane.showConfirmDialog(
                        this,
                        "Text has been changed.\nDo you want to save it",
                        "Changes Save",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (ask == JOptionPane.YES_OPTION)
                {
                    SaveButtonCoding();
                    ta.setText(null);
                    NewButtonCoding();
                }
                else if (ask == JOptionPane.NO_OPTION)
                {
                    ta.setText(null);
                    NewButtonCoding();
                }
            }
            else
            {
                ta.setText(null);
                NewButtonCoding();
            }
        }
        else
        {
            try
            {
                if (command.equals("Open"))
                {
                    fd = new FileDialog(this, "Open a File", FileDialog.LOAD);
                    fd.setVisible(true);

                    String st3 = fd.getFile();
                    if (st3 == null)
                        return;

                    fis = new FileInputStream(fd.getDirectory() + fd.getFile());
                    b = new byte[fis.available()];
                    fis.read(b);
                    fis.close();

                    ta.setText(new String(b));
                    jf.setTitle("Editor2015 : " + fd.getFile());
                    filename = fd.getDirectory() + fd.getFile();
                    fname = fd.getFile();
                    textchange = false;
                }
                else if (command.equals("Save"))
                {
                    SaveButtonCoding();
                }
                else if (command.equals("Save As..."))
                {
                    int ask;
                    if (textchange == true)
                    {
                        ask = JOptionPane.showConfirmDialog(
                                this,
                                "Text has been changed.\nDo you want to save it",
                                "Changes Save",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE);

                        if (ask == JOptionPane.YES_OPTION)
                        {
                            SaveButtonCoding();
                            SaveAs();
                        }
                        else if (ask == JOptionPane.NO_OPTION)
                        {
                            SaveAs();
                        }
                    }
                    else
                    {
                        SaveAs();
                    }
                }
                else if (command.equals("Exit"))
                {
                    int ask;

                    if (textchange == true)
                    {
                        ask = JOptionPane.showConfirmDialog(
                                this,
                                "Text has been changed.\nDo you want to save it",
                                "Changes Save",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE);

                        if (ask == JOptionPane.YES_OPTION)
                        {
                            SaveButtonCoding();
                            System.exit(0);
                        }
                        else if (ask == JOptionPane.NO_OPTION)
                        {
                            System.exit(0);
                        }
                    }
                    else
                    {
                        System.exit(0);
                    }
                }
                else if (command.equals("Cut"))
                {
                    ta.cut();
                }
                else if (command.equals("Paste"))
                {
                    ta.paste();
                }
                else if (command.equals("Copy"))
                {
                    ta.copy();
                }
                else if (command.equals("Select All"))
                {
                    ta.selectAll();
                }
                else if (command.equals("Time & Date"))
                {
                    ta.insert("" + new Date(), ta.getSelectionStart());
                }
                else if (command.equals("Delete"))
                {
                    ta.replaceSelection("");
                }
                else if (command.equals("Color"))
                {
                    cc = new JColorChooser();
                    Color newColor = cc.showDialog(this, "Select a Color", Color.black);
                    if (newColor != null)
                    {
                        ta.setForeground(newColor);
                        ta.setSelectedTextColor(newColor);
                        ta1.setForeground(newColor);
                        ta1.setSelectedTextColor(newColor);
                    }
                }
                else if (command.equals("Font"))
                {
                    // Font chooser code in the original PDF is commented out.
                }
                else if (command.equals("Compile"))
                {
                    if (!filename.equals(""))
                    {
                        try
                        {
                            result = "";
                            Process error = r.exec("C:\\Program Files\\Java\\jdk1.8.0_131\\bin\\javac.exe " + filename);
                            BufferedReader err = new BufferedReader(new InputStreamReader(error.getErrorStream()));

                            while (true)
                            {
                                String temp = err.readLine();
                                if (temp != null)
                                {
                                    result += temp;
                                    result += "\n";
                                }
                                else
                                    break;
                            }

                            if (result.equals(""))
                                ta1.setText("Compilation Successful " + filename);
                            else
                                ta1.setText(result);

                            err.close();
                        }
                        catch (Exception e1)
                        {
                            ta1.setText("" + e1);
                        }
                    }
                    else
                    {
                        ta1.setText("Please provide java file name");
                    }
                }
                else if (command.equals("Run.."))
                {
                    int start = 0;

                    if (!fname.equals(""))
                    {
                        try
                        {
                            int num = fname.indexOf('.');
                            String fn = fname.substring(0, num);

                            Process p = r.exec(
                                    "C:\\Program Files\\Java\\jdk1.8.0_131\\bin\\java.exe -cp . " + fn,
                                    null,
                                    new File(new File(filename).getParent()));

                            BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
                            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                            result = "";

                            while (true)
                            {
                                String temp = output.readLine();
                                if (temp != null)
                                {
                                    result += temp;
                                    result += "\n";
                                }
                                else
                                    break;
                            }

                            if (result.equals(""))
                            {
                                while (true)
                                {
                                    String temp = error.readLine();
                                    if (temp != null)
                                    {
                                        result += temp;
                                        result += "\n";
                                    }
                                    else
                                        break;
                                }
                            }

                            output.close();
                            error.close();
                            ta1.setText(result);
                        }
                        catch (Exception e2)
                        {
                            System.out.println(e2);
                            ta1.setText("" + e2);
                        }
                    }
                }
                else if (command.equals("About Editor"))
                {
                    String hl = "This Editor has been created by Mr. Himanshu.\n"
                            + "Project was completed under the guidence of Prof. Manish Bhatia(HOD Java Deptt.).";
                    JOptionPane.showMessageDialog(this, hl);
                }
                else if (command.equals("Print..."))
                {
                    try
                    {
                        ta.print();
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(this, "" + e);
                    }
                }
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, "" + e);
            }
        }
    }

    public void SaveButtonCoding()
    {
        try
        {
            if (filename.equals(""))
            {
                fd = new FileDialog(this, "Save a File", FileDialog.SAVE);
                fd.setVisible(true);

                String st = fd.getFile();
                if (st == null)
                    return;

                filename = fd.getDirectory() + fd.getFile();
                fname = fd.getFile();
            }

            fos = new FileOutputStream(filename);
            b = ta.getText().getBytes();
            fos.write(b);
            fos.close();

            jf.setTitle("Editor2015 : " + fname);
            textchange = false;
            ta1.setText("File has been saved now.");
        }
        catch (Exception ee)
        {
            JOptionPane.showMessageDialog(this, "No file to save.");
        }
    }

    public void SaveAs()
    {
        try
        {
            fd = new FileDialog(this, "Save As a File", FileDialog.SAVE);
            fd.setVisible(true);

            String st1 = fd.getFile();
            if (st1 == null)
                return;

            fos = new FileOutputStream(fd.getDirectory() + fd.getFile());
            b = ta.getText().getBytes();
            fos.write(b);
            fos.close();

            jf.setTitle("Editor2015 : " + fd.getFile());
            filename = fd.getDirectory() + fd.getFile();
            fname = fd.getFile();
            textchange = false;
            ta1.setText("File has been Saved now.");
        }
        catch (Exception ee)
        {
            JOptionPane.showMessageDialog(this, "No file to save.");
        }
    }

    public void NewButtonCoding()
    {
        String str = JOptionPane.showInputDialog(
                null,
                "Please Enter the class name",
                "Provide a class name",
                JOptionPane.PLAIN_MESSAGE);

        if (str == null)
            return;

        ta.setText(
                "import java.lang.*;\n\n"
                + "public class " + str + "\n"
                + "{\n"
                + "\tpublic static void main(String [] s)\n"
                + "\t{\n"
                + "\t\t\n"
                + "\t}\n"
                + "}\n");

        filename = "";
        fname = "";
        textchange = false;
        jf.setTitle("Editor2015 : Untitled");
    }

    public static void main(String aq[])
    {
        Editor2015 a = new Editor2015();
    }
}
