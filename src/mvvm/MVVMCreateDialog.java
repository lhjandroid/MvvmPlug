package mvvm;

import javax.swing.*;
import java.awt.event.*;

public class MVVMCreateDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JEditorPane mvvmPackageName;
    private JEditorPane mvvmModelName;

    // 点击回调
    private DialogCallBack mCallBack;

    public MVVMCreateDialog(DialogCallBack callBack) {
        mCallBack = callBack;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(300, 150);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        if (mCallBack != null) {
            mCallBack.ok("",mvvmPackageName.getText().trim(), mvvmModelName.getText().trim());
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
//    public static void main(String[] args) {
//        MVVMCreateDialog dialog = new MVVMCreateDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }


    public interface DialogCallBack{
        void ok(String author,String packageName, String moduleName);
    }

}