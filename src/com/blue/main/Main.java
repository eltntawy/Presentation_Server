package com.blue.main;

import com.blue.server.BluetoothServer;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Handler;

public class Main extends JFrame {
    private JPanel contentPane;
    private JButton buttonStart;
    private JButton buttonExit;
    private JTextArea textArea1;
    private JButton buttonStop;
    private JButton buttonClear;
    private BluetoothServer bluetoothServer;

    public Main() {

        init();
        dynInit();

        new Thread() {
            @Override
            public void run() {

                while(true) {
                    textArea1.setText(bluetoothServer.logString.toString());
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

    }

    private void onStart() {

            new Thread() {
                @Override
                public void run() {
                    try {
                        if(bluetoothServer != null)
                            bluetoothServer.startServer();

                        textArea1.setText(bluetoothServer.logString.toString());

                    } catch (IOException e) {
                        bluetoothServer.logString.append(e.getMessage() + "\n");
                        e.printStackTrace();
                        textArea1.setText(bluetoothServer.logString.toString());
                    }
                }
            }.start();
    }

    private void onExit() {
// add your code here if necessary
        try {

            if(bluetoothServer != null)
                bluetoothServer.stopService();

        } catch (IOException e) {
            bluetoothServer.logString.append(e.getMessage() + "\n");
            e.printStackTrace();
            textArea1.setText(bluetoothServer.logString.toString());
        }
        dispose();
    }

    public void init () {


        try {

            if(bluetoothServer == null)
                bluetoothServer = new BluetoothServer();


            textArea1.setText(bluetoothServer.logString.toString());

        } catch (IOException e) {
            bluetoothServer.logString.append(e.getMessage() + "\n");
            e.printStackTrace();
            textArea1.setText(bluetoothServer.logString.toString());

        }

    }

    public void dynInit () {
        setSize(500,1000);
        setLocation(100,50);
        setContentPane(contentPane);

        getRootPane().setDefaultButton(buttonStart);

        buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onStart();
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        });

        buttonClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bluetoothServer.logString = new StringBuffer();
                textArea1.setText("");
            }
        });

        buttonStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(bluetoothServer != null)
                        bluetoothServer.stopService();

                } catch (IOException e1) {
                    bluetoothServer.logString.append(e1.getMessage()+"\n");
                    e1.printStackTrace();
                    textArea1.setText(bluetoothServer.logString.toString());
                }
            }
        });
    }




    public static void main(String[] args) {
        Main dialog = new Main();

        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }
}
