import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by eltntawy on 02/11/14.
 *
 *
 */
public class PerKeyListner implements ActionListener {


    JFrame mainFrame = new JFrame("Main Server Reciver");
    JPanel mainPanel = new JPanel();
    JButton btnSearch = new JButton("Search");


    public static void main(String args[]) {

    PerKeyListner obj = new PerKeyListner();

        obj.init();


    }

    public void init() {

        // setup layout

        mainFrame.setSize(300,600);
        mainFrame.add(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(btnSearch,BorderLayout.SOUTH);


        // dynamic init
        dynInit();

        // show frame
        mainFrame.setVisible(true);


    }

    public void dynInit() {

        btnSearch.addActionListener(this);

    }

    public void btnUpPress () {

        try {
            Robot r = new Robot();

            r.keyPress(KeyEvent.VK_UP);
            r.keyRelease(KeyEvent.VK_UP);

        } catch (AWTException ex) {
            System.err.println(ex.getMessage());
        }

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {


        if (btnSearch == actionEvent.getSource()) {
            btnUpPress();
            JOptionPane.showMessageDialog(mainFrame,"UpPress Action for this command","worring",JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainFrame,"No Action for this command","worring",JOptionPane.INFORMATION_MESSAGE);
        }

    }
}
