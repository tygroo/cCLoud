import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Bonheur on 05/01/2015.
 */
public class GuiForm extends JFrame{
    private JTextArea bienvenueDansLeClientTextArea;
    private JList list1;
    private JButton envoyerButton;
    private JButton annulerButton;
    private JButton ajoutButton;
    private JButton supprimerButton;
    private JPanel rootPanel;
    private JCheckBox testCheckBox;
    DefaultListModel dlm;



    public GuiForm(){
        super();
        File file = new File("") ;
        final Upload upl = new Upload();

        dlm = new DefaultListModel();


        setContentPane(rootPanel);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
                            StringUtils.containsOnly(extention, "png")||
                            StringUtils.containsOnly(extention, "JPG")) {
                        File fileList = dialogue.getSelectedFile();
                        dlm.addElement(fileList.getPath());
                        list1.setModel(dlm);

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
                String url = "http://localhost:8081/rest/upload/file/";
                if (testCheckBox.isSelected()){
                    url = "http://198.27.66.107:8082/rest/upload/file/";
                }
                ListModel model = list1.getModel();

                for(int i=0; i < model.getSize(); i++){
                    String o =  (String)model.getElementAt(i);
                    File file = new File(o);
                    System.out.print("Debut du tranfert de :"+o);
                    try {
                        upl.upload(url,file);
                    } catch (IOException e1) {
                        System.out.print("Erreur du tranfert de :"+o+ "avec pour erreur :"+ e1.getMessage());
                        //e1.printStackTrace();
                    }
                    System.out.print("Fin du tranfert de :"+o);
                }
            }
        });
        setVisible(true);
    }
}
