import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;


import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Bonheur on 05/01/2015.
 */
public class GuiForm extends JFrame {
    private JTextArea bienvenueDansLeClientTextArea;
    private JList list1;
    private JButton envoyerButton;
    private JButton annulerButton;
    private JButton ajoutButton;
    private JButton supprimerButton;
    private JPanel rootPanel;
    private JCheckBox testCheckBox1;
    private JCheckBox testCheckBox2;
    DefaultListModel dlm;


    public GuiForm() {
        super();
        File file = new File("");
        final Upload upl = new Upload();

        dlm = new DefaultListModel();

        setContentPane(rootPanel);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        testCheckBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (testCheckBox2.isSelected()) {
                    testCheckBox2.setSelected(false);
                    pack();
                }
            }
        });

        testCheckBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (testCheckBox1.isSelected()) {
                    testCheckBox1.setSelected(false);
                    pack();
                }
            }
        });

        //list1 = new JList(dlm);
        list1.setModel(dlm);

        ajoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser dialogue = new JFileChooser(new File("."));

                dialogue.setDialogTitle("Choisiez le fichier à envoyer sur CloudPix...");

                dialogue.setName("Choisiez le fichier à envoyer...");

                if (dialogue.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String extention = FilenameUtils.getExtension(dialogue.getSelectedFile().getName());
                    if (StringUtils.containsOnly(extention, "jpg") ||
                            StringUtils.containsOnly(extention, "jpeg") ||
                            StringUtils.containsOnly(extention, "png") ||
                            StringUtils.containsOnly(extention, "JPG") ||
                            StringUtils.containsOnly(extention, "PNG")) {
                        File fileList = dialogue.getSelectedFile();
                        dlm.addElement(fileList.getPath());
                        list1.setModel(dlm);
                        pack();

                    }
                }
            }
        });

        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        envoyerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "http://198.27.66.107/cloudpix/rest/upload/file/";
                if (testCheckBox1.isSelected()) {
                    url = "http://localhost:8080/rest/upload/file/";

                }
                if (testCheckBox2.isSelected()) {
                    url = "http://localhost:8081/rest/upload/file/";
                }


                ListModel model = list1.getModel();
                ListModel finalList = dlm;

                for (int i = 0; i < model.getSize(); i++) {
                    String o = (String) model.getElementAt(i);
                    File file = new File(o);
                    System.out.print("Debut du tranfert de :" + o);
                    String response = null;
                    try {
                        response = upl.upload(url, file);
                    } catch (IOException e1) {
                        System.out.print("Erreur du tranfert de :" + o + "avec pour erreur :" + e1.getMessage());
                        //e1.printStackTrace();
                    }
                    if (StringUtils.isBlank(response) && null != response) {
                        //finalList.removeListDataListener((ListDataListener) finalList.getElementAt(i));
                        dlm.remove(i);
                        list1.setModel(dlm);
                        pack();
                    }
                    System.out.print("/r Fin du tranfert de :" + o);
                }
            }
        });
        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedList = list1.getSelectedIndex();
                ListModel model = list1.getModel();
                if (selectedList >= 0 ){
                    dlm.remove(selectedList);
                    //model.removeListDataListener((ListDataListener) model.getElementAt(selectedList));
                    list1.setModel(dlm);
                    pack();
                }


            }
        });
        setVisible(true);
    }
}
