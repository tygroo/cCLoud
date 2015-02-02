
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
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
    private JTable table1;
    private JTextPane textPane1;
    DefaultListModel dlm;
    String[] columnNames;
    Object[][] data;
    DefaultTableModel model;

    public GuiForm() {
        super();
        File file = new File("");
        final Upload upl = new Upload();

        dlm = new DefaultListModel();
        textPane1= new JTextPane();
        setContentPane(rootPanel);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        testCheckBox1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (testCheckBox2.isSelected()) {
                    testCheckBox2.setSelected(false);
                    pack();
                }
            }
        });

        testCheckBox2.addActionListener(new ActionListener() {
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
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        envoyerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url = "http://198.27.66.107/cloudpix/rest/upload/file/";
                String urlDL = "http://198.27.66.107/cloudpix/rest/";
                if (testCheckBox1.isSelected()) {
                    url = "http://localhost:8080/rest/upload/file/";
                    urlDL = "http://localhost:8080/rest/";
                }
                if (testCheckBox2.isSelected()) {
                    url = "http://localhost:8081/rest/upload/file/";
                    urlDL = "http://localhost:8080/rest/";
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
                    if (StringUtils.isNotBlank(response) && null != response) {
                        JSONParser parser=new JSONParser();
                        Object obj= null;
                        try {
                            obj = parser.parse(response);
                            JSONObject array= (JSONObject) obj;

                            long idPist = (long)array.get("id");
                            String namePict = (String) array.get("name");
                            String namePictM = (String) array.get("nameMed");
                            String namePictL = (String) array.get("nameLow");

                            String shotNameHight = (String) array.get("shortNameHight");
                            String shotNameMedium = (String) array.get("shortNameMed");
                            String shotNameLow = (String) array.get("shortNameLow");

                            String content = namePict + "==> shotURL Normal :"+ urlDL+shotNameHight+ "|| shotURL Medium :"+ urlDL+shotNameMedium + "|| shotURL Low :"+ urlDL+shotNameLow;
                            textPane1.add(new JTextArea(content,6,10));
                            textPane1.setVisible(true);
                            pack();
                            setVisible(true);

//                            String nameHPict = (String) array.get(2);
//                            String nameMPict = (String) array.get(1);
//                            String nameLPict = (String) array.get(5);

                            //String creationDate = (String) array.get(6);
                            //String user = (String) array.get(7);

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

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
