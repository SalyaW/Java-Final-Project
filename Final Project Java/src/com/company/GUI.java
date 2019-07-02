package com.company;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class GUI extends JFrame {
    JFileChooser jfc =  new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    DefaultListModel model = new DefaultListModel();
    JList<String> filelist = new JList(model);
    JScrollPane sp = new JScrollPane(filelist);
    JPanel pnl = new JPanel();
    JButton zipfilesbutton = new JButton("Zip files");
    JButton unzipbutton = new JButton("Unzip selected");
    JButton refresh = new JButton("Refresh list");
    JLabel header = new JLabel("Zipped files:");

    public GUI() {
        this.setSize(new Dimension(500, 800));
        this.setVisible(true);
        this.setLayout(new BorderLayout());
//        pnl.setPreferredSize(new Dimension(250, 300));
//        pnl.setMinimumSize(new Dimension(250, 300));
//        filelist.setPreferredSize(new Dimension(240, 300));
        this.setResizable(false);
        this.setTitle("Zip");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);



        zipfilesbutton.addActionListener(new zipActionListener());
        unzipbutton.addActionListener(new unzipActionListener());
        refresh.addActionListener(e -> loadlist());

        pnl.add(zipfilesbutton);
        pnl.add(unzipbutton);
        pnl.add(refresh);
        this.add(header, BorderLayout.NORTH);
        this.add(sp, BorderLayout.CENTER);
        this.add(pnl, BorderLayout.SOUTH);
        loadlist();
        this.revalidate();
        this.repaint();
    }

    public void loadlist()
    {
        File folder = new File("zippedfiles");
        File[] listOfFiles = folder.listFiles();
        model.removeAllElements();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                model.add(i, listOfFiles[i].getName());
            }
        }
    }

    private void disableAllComponents()
    {
        zipfilesbutton.setEnabled(false);
        unzipbutton.setEnabled(false);
        filelist.setEnabled(false);
    }

    private void enableAllComponents()
    {
        zipfilesbutton.setEnabled(true);
        unzipbutton.setEnabled(true);
        filelist.setEnabled(true);
    }

    private class zipActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            jfc.setMultiSelectionEnabled(true);
            int returnValue = jfc.showOpenDialog(null);
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                disableAllComponents();
                zipfilesbutton.setText("Zipping...");
                File[] selectedFile = jfc.getSelectedFiles();
                String zippedName = JOptionPane.showInputDialog(null, "Enter zipped file name:", "Enter name", JOptionPane.PLAIN_MESSAGE);
                if (zippedName != null) {
                    String[] selectedFilePaths = new String[selectedFile.length];
                    for (int i = 0; i < selectedFile.length; i++) {
                        selectedFilePaths[i] = selectedFile[i].getAbsolutePath();
                    }
                    Zip z = new Zip(zippedName);
                    z.zipMultiFiles(Arrays.asList(selectedFilePaths));
                    loadlist();
                }
                JOptionPane.showMessageDialog(null, "File(s) zipped successfully.", "Success", JOptionPane.PLAIN_MESSAGE);
                zipfilesbutton.setText("Zip files");
                enableAllComponents();
            }
        }
    }

    private class unzipActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (filelist.getSelectedIndex() > -1) {
                jfc.setMultiSelectionEnabled(false);
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    Zip.UnzipFiles("zippedfiles/" + filelist.getSelectedValue(), jfc.getSelectedFile().getAbsolutePath());
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Please select file to unzip", "", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}