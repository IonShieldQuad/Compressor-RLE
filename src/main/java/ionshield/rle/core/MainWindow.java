package ionshield.rle.core;


import ionshield.rle.graph.RasterDisplay;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MainWindow {
    private JPanel rootPanel;
    private JTextArea log;
    
    private RasterDisplay graph;
    private JButton compressButton;
    private JButton decompressButton;
    private JButton saveButton;
    private JTextField fileNameField;
    private JButton loadButton;
    private JTextArea log2;
    
    private static String TITLE = "Compressor-RLE";
    
    private int precision = 3;
    
    private List<String> lines;
    private BufferedImage image;
    
    private MainWindow() {
        initComponents();
    }
    
    private void initComponents() {
        
        loadButton.addActionListener(e -> {
            loadFile();
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line).append(System.lineSeparator());
            }
            log.setText(sb.toString());
            log2.append("File size: " + Utils.getSize(lines) + System.lineSeparator());
            updateGraph();
        });
        
        compressButton.addActionListener(e -> {
            try {
                int prevSize = Utils.getSize(lines);
    
                lines = Utils.compress(lines);
                
                StringBuilder sb = new StringBuilder();
                for (String line : lines) {
                    sb.append(line).append(System.lineSeparator());
                }
                log.setText(sb.toString());
                log2.append("Compressed" + System.lineSeparator());
                log2.append("File size: " + Utils.getSize(lines) + System.lineSeparator());
                double factor = prevSize / (double)Utils.getSize(lines);
                log2.append("Compression factor: " + BigDecimal.valueOf(factor).setScale(precision, RoundingMode.HALF_UP).doubleValue() + System.lineSeparator());
                updateGraph();
            }
            catch (IllegalArgumentException ex) {
                log2.append(ex.getMessage() + System.lineSeparator());
            }
        });
    
        decompressButton.addActionListener(e -> {
            try {
                int prevSize = Utils.getSize(lines);
            
                lines = Utils.decompress(lines);
            
                StringBuilder sb = new StringBuilder();
                for (String line : lines) {
                    sb.append(line).append(System.lineSeparator());
                }
                log.setText(sb.toString());
                log2.append("Decompressed" + System.lineSeparator());
                log2.append("File size: " + Utils.getSize(lines) + System.lineSeparator());
                double factor = (double)Utils.getSize(lines) / prevSize;
                log2.append("Expansion factor: " + BigDecimal.valueOf(factor).setScale(precision, RoundingMode.HALF_UP).doubleValue() + System.lineSeparator());
                updateGraph();
            }
            catch (IllegalArgumentException ex) {
                log2.append(ex.getMessage() + System.lineSeparator());
            }
        });
        
        saveButton.addActionListener(e -> saveFile());
    }
    
    private void loadFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "IMG & TXT", "img", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                List<String> l = new ArrayList<>();
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    l.add(line);
                }
                lines = l;
                fileNameField.setText(file.getName());
                reader.close();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void saveFile() {
        if (lines == null || lines.isEmpty()) return;
        JFileChooser chooser = new JFileChooser();
        /*FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "IMG & TXT", "img", "txt");
        chooser.setFileFilter(filter);*/
        int returnVal = chooser.showSaveDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    
                for (int i = 0; i < lines.size(); i++) {
                    writer.write(lines.get(i));
                    if (i < lines.size() - 1) {
                        writer.newLine();
                    }
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    private void updateGraph() {
    
        try {
            image = Utils.readImage(lines);
            graph.setImage(image);
        } catch (IllegalArgumentException e) {
            log2.append(e.getMessage() + System.lineSeparator());
        }
        
        graph.repaint();
    }
    
    
    public static void main(String[] args) {
        JFrame frame = new JFrame(TITLE);
        MainWindow gui = new MainWindow();
        frame.setContentPane(gui.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
