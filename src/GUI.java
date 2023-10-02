import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GUI extends JFrame {
Box boxH1,boxH2,boxH3,boxH4,boxH5;
Box boxV;
JTextField inputKey,inputMing,inputMi,inputMingAsc,inputMiAsc;
JButton buttonDecrypt,buttonEncrypt,buttonEncryptAsc,buttonDecryptAsc;
JLabel L1,L2,L3,L4,L5,L6,L7,L8,L9;
EncryptListener listener1;
DecryptListener listener2;
EncryptAscListener listener3;
DecryptAscListener listener4;

public GUI(){
setLayout(new FlowLayout());
init();
setTitle("S-DES");
setVisible(true);
setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
}
void init(){
Font font=new Font("宋体",Font.PLAIN,30);
inputKey=new JTextField(10);
inputMing=new JTextField(10);
inputMi=new JTextField(10);
inputMingAsc=new JTextField(10);
inputMiAsc=new JTextField(10);
buttonEncrypt=new JButton("加密");
buttonDecrypt=new JButton("解密");
buttonEncryptAsc=new JButton("加密");
buttonDecryptAsc=new JButton("解密");
inputKey.setFont(font);
inputMing.setFont(font);
inputMi.setFont(font);
inputMingAsc.setFont(font);inputMiAsc.setFont(font);
buttonEncrypt.setFont(font);
buttonDecrypt.setFont(font);
buttonEncryptAsc.setFont(font);buttonDecryptAsc.setFont(font);
boxH1=Box.createHorizontalBox();
boxH2=Box.createHorizontalBox();
boxH3=Box.createHorizontalBox();
boxH4=Box.createHorizontalBox(); boxH5=Box.createHorizontalBox();
boxV=Box.createVerticalBox();
L1=new JLabel("请输入十位的密钥:");
L2=new JLabel("请输入八位的明文:");
L3=new JLabel("请输入八位的密文:");
L4=new JLabel();L5=new JLabel();L6=new JLabel();L9=new JLabel();
L7=new JLabel("请输入明文字符串:");
L8=new JLabel("请输入密文字符串:");
L1.setFont(font);
L2.setFont(font);
L3.setFont(font);
L4.setFont(font);L5.setFont(font);
L6.setFont(font);L7.setFont(font);
L8.setFont(font);L9.setFont(font);

boxH1.add(L1);
boxH1.add(Box.createHorizontalStrut(60));
boxH1.add(inputKey);
boxH2.add(L2);
boxH2.add(Box.createHorizontalStrut(60));
boxH2.add(inputMing);
boxH2.add(Box.createHorizontalStrut(60));
boxH2.add(buttonEncrypt);
boxH3.add(L3);
boxH3.add(Box.createHorizontalStrut(60));
boxH3.add(inputMi);
boxH3.add(Box.createHorizontalStrut(60));
boxH3.add(buttonDecrypt);
boxH4.add(L7);
boxH4.add(Box.createHorizontalStrut(60));
boxH4.add(inputMingAsc);
boxH4.add(Box.createHorizontalStrut(60));
boxH4.add(buttonEncryptAsc);
boxH5.add(L8);
boxH5.add(Box.createHorizontalStrut(60));
boxH5.add(inputMiAsc);
boxH5.add(Box.createHorizontalStrut(60));
boxH5.add(buttonDecryptAsc);
boxV.add(boxH1);
boxV.add(Box.createVerticalStrut(20));
boxV.add(boxH2);
boxV.add(Box.createVerticalStrut(15));
boxV.add(L4);
boxV.add(Box.createVerticalStrut(20));
boxV.add(boxH3);
boxV.add(Box.createVerticalStrut(15));
boxV.add(L5);
boxV.add(Box.createVerticalStrut(20));
boxV.add(boxH4);
boxV.add(Box.createVerticalStrut(15));
boxV.add(L6);
boxV.add(Box.createVerticalStrut(20));
boxV.add(boxH5);
boxV.add(Box.createVerticalStrut(15));
boxV.add(L9);
add(boxV);

listener1=new EncryptListener(); listener2=new DecryptListener(); 
listener3=new EncryptAscListener(); listener4=new DecryptAscListener();
listener1.setView(this); listener2.setView(this); 
listener3.setView(this); listener4.setView(this);
buttonEncrypt.addActionListener(listener1);
buttonDecrypt.addActionListener(listener2);
buttonEncryptAsc.addActionListener(listener3);
buttonDecryptAsc.addActionListener(listener4);
inputKey.addActionListener(listener1);
inputKey.addActionListener(listener2);
inputKey.addActionListener(listener3);
inputKey.addActionListener(listener4);
inputMing.addActionListener(listener1);
inputMi.addActionListener(listener2);
inputMingAsc.addActionListener(listener3);
inputMiAsc.addActionListener(listener4);
}
}

class EncryptListener implements ActionListener{
    GUI gui;
    SDES sdes=new SDES();
    public void setView(GUI gui){
        this.gui=gui;
    }
    public void actionPerformed(ActionEvent e){
        String key=gui.inputKey.getText();
        String Ming=gui.inputMing.getText();
        if(!(key.length() == 10 && key.matches("[01]+"))){
            gui.L4.setText("密钥格式错误，请确保输入十位二进制数字");
        }
        else if(!(Ming.length() == 8 && key.matches("[01]+"))){
            gui.L4.setText("明文格式错误，请确保输入八位二进制数字");
        }
        else{
            String Mi=sdes.encrypt(Ming, key);
            gui.L4.setText("密文是："+Mi);
        }
    }
}
class DecryptListener implements ActionListener{
    GUI gui;
    SDES sdes=new SDES();
    public void setView(GUI gui){
        this.gui=gui;
    }
    public void actionPerformed(ActionEvent e){
        String key=gui.inputKey.getText();
        String Mi=gui.inputMi.getText();
        if(!(key.length() == 10 && key.matches("[01]+"))){
            gui.L5.setText("密钥格式错误，请确保输入十位二进制数字");
        }
        else if(!(Mi.length() == 8 && key.matches("[01]+"))){
            gui.L5.setText("密文格式错误，请确保输入八位二进制数字");
        }
        else{
            String Ming=sdes.decrypt(Mi, key);
            gui.L5.setText("明文是："+Ming);
        }
    }
}
class EncryptAscListener implements ActionListener{
    GUI gui;
    SDES sdes=new SDES();
    public void setView(GUI gui){
        this.gui=gui;
    }
    public void actionPerformed(ActionEvent e){
        String key=gui.inputKey.getText();
        String Ming=gui.inputMingAsc.getText();
        if(!(key.length() == 10 && key.matches("[01]+"))){
            gui.L6.setText("密钥格式错误，请确保输入十位二进制数字");
        }
        else{
            String Mi=sdes.encryptAsc(Ming, key);
            gui.L6.setText("密文是："+Mi);
        }
    }
}
class DecryptAscListener implements ActionListener{
    GUI gui;
    SDES sdes=new SDES();
    public void setView(GUI gui){
        this.gui=gui;
    }
    public void actionPerformed(ActionEvent e){
        String key=gui.inputKey.getText();
        String Mi=gui.inputMiAsc.getText();
        if(!(key.length() == 10 && key.matches("[01]+"))){
            gui.L9.setText("密钥格式错误，请确保输入十位二进制数字");
        }
        else{
            String Ming=sdes.decryptAsc(Mi, key);
            gui.L9.setText("明文是："+Ming);
        }
    }
}