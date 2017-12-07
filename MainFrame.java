package criptor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainFrame extends JFrame {

    private static Dimension FS = new Dimension(600, 600);
    private static JPanel mainPanel;
    private static JPanel imagePanel;

    private static JTextField iterCountText;
    private static JLabel generationCountLabel;

    private static ImagePanel inputPicture;
    private static ImagePanel outputPicture;
    private static ImagePanel noisePicture;
    private static JPanel textPanel;

    private static JTextArea inputText;
    private static JTextArea outputText;
    private static JLabel inputTextLabel;
    private static JLabel outputTextLabel;
    private static JButton criptTextButton;

    private static JLabel titleInputFile;
    private static JLabel titleOutputFile;
    private static JLabel titleNoiseFile;
    private static File inputFile;
    private static File outputFile;

    private static JProgressBar progressBar;
    private static JButton criptButton;

    private static boolean isChange = false;

    //Инициализация. Просто добавляем компоненты на фрейм, а также слушателей к ним
    public void init() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - getFS().width) / 2, (screenSize.height - getFS().height) / 2, getFS().width, getFS().height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(getFS());
        setResizable(false);

        setMainPanel(new JPanel(new GridBagLayout()));
        getMainPanel().setSize(getFS());
        getMainPanel().setLocation(0, 0);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.insets = new Insets(5, 200, 5, 200);

        setGenerationCountLabel(new JLabel("IterCount"));
        getGenerationCountLabel().setHorizontalAlignment(0);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        c.gridwidth = 1;
        getMainPanel().add(getGenerationCountLabel(), c);

        setIterCountText(new JTextField("20"));
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 3;
        c.gridwidth = 1;
        getMainPanel().add(getIterCountText(), c);

        c.insets = new Insets(5, 5, 5, 5);

        setImagePanel(new JPanel(new GridBagLayout()));
        getImagePanel().setBorder(BorderFactory.createLineBorder(Color.gray, 1));

        setTitleInputFile(new JLabel("InputFile"));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.gridwidth = 1;
        getImagePanel().add(getTitleInputFile(), c);

        setTitleOutputFile(new JLabel("OutputFile"));
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.gridwidth = 1;
        getImagePanel().add(getTitleOutputFile(), c);

        setTitleNoiseFile(new JLabel("Noise"));
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.gridwidth = 1;
        getImagePanel().add(getTitleNoiseFile(), c);

        c.ipady = 150;

        setInputPicture(new ImagePanel());
        getInputPicture().setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        getInputPicture().setBackground(Color.white);
        getInputPicture().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Использовать стандартное расположение");
                switch (choice) {
                    case 0:
                        setInputFile(new File("file.png"));
                        break;
                    default:
                        JFileChooser fileopen = new JFileChooser();
                        int ret = fileopen.showDialog(null, "Открыть файл");
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            setInputFile(fileopen.getSelectedFile());
                        } else {
                            setInputFile(new File("file.png"));
                        }
                        break;
                }
                getProgressBar().setValue(0);
                try {
                    getInputPicture().setImage(ImageIO.read(getInputFile()));
                } catch (IOException ex) {
                }
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.gridwidth = 1;
        getImagePanel().add(getInputPicture(), c);

        setOutputPicture(new ImagePanel());
        getOutputPicture().setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        getOutputPicture().setBackground(Color.white);
        getOutputPicture().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Использовать стандартное расположение?");
                switch (choice) {
                    case 0:
                        setOutputFile(new File("file.png"));
                        break;
                    default:
                        JFileChooser fileopen = new JFileChooser();
                        int ret = fileopen.showDialog(null, "Открыть файл");
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            setOutputFile(fileopen.getSelectedFile());
                        } else {
                            setOutputFile(new File("file.png"));
                        }
                        break;
                }
                getProgressBar().setValue(0);
                try {
                    getOutputPicture().setImage(ImageIO.read(getOutputFile()));
                } catch (IOException ex) {
                }
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        c.gridwidth = 1;
        getImagePanel().add(getOutputPicture(), c);

        setNoisePicture(new ImagePanel());
        getNoisePicture().setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        getNoisePicture().setBackground(Color.white);
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 1;
        getImagePanel().add(getNoisePicture(), c);

        c.ipady = 20;

        setTextPanel(new JPanel(new GridBagLayout()));
        getTextPanel().setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 1;
        getImagePanel().add(getTextPanel(), c);

        c.ipady = 0;

        setInputTextLabel(new JLabel("InputText"));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.gridwidth = 1;
        getTextPanel().add(getInputTextLabel(), c);

        setInputText(new JTextArea());
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.gridwidth = 1;
        getTextPanel().add(getInputText(), c);

        setOutputTextLabel(new JLabel("OutputText"));
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.gridwidth = 1;
        getTextPanel().add(getOutputTextLabel(), c);

        setOutputText(new JTextArea());
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 1;
        getTextPanel().add(getOutputText(), c);

        c.fill = GridBagConstraints.NONE;

        setCriptTextButton(new JButton("Cript Text"));
        getCriptTextButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (Algorithm.getValuesForText()) {
                    Algorithm.criptText();
                }
                getProgressBar().setValue(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 1;
        getTextPanel().add(getCriptTextButton(), c);

        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.gridwidth = 2;
        getMainPanel().add(getImagePanel(), c);

        setProgressBar(new JProgressBar(0, 100));
        getProgressBar().setBackground(Color.white);
        getProgressBar().setForeground(Color.green);
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 1;
        getMainPanel().add(getProgressBar(), c);

        setCriptButton(new JButton("Cript Image"));
        getCriptButton().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (Algorithm.getValuesForImage()) {
                    Algorithm.cryptImage();
                }
                getProgressBar().setValue(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 1;
        getMainPanel().add(getCriptButton(), c);

        add(getMainPanel());
    }

    public static Dimension getFS() {
        return FS;
    }

    public static void setFS(Dimension aFS) {
        FS = aFS;
    }

    public static JPanel getMainPanel() {
        return mainPanel;
    }

    public static void setMainPanel(JPanel aMainPanel) {
        mainPanel = aMainPanel;
    }

    public static JPanel getImagePanel() {
        return imagePanel;
    }

    public static void setImagePanel(JPanel aImagePanel) {
        imagePanel = aImagePanel;
    }

    public static JTextField getIterCountText() {
        return iterCountText;
    }

    public static void setIterCountText(JTextField aIterCountText) {
        iterCountText = aIterCountText;
    }

    public static JLabel getGenerationCountLabel() {
        return generationCountLabel;
    }

    public static void setGenerationCountLabel(JLabel aGenerationCountLabel) {
        generationCountLabel = aGenerationCountLabel;
    }

    public static ImagePanel getInputPicture() {
        return inputPicture;
    }

    public static void setInputPicture(ImagePanel aInputPicture) {
        inputPicture = aInputPicture;
    }

    public static ImagePanel getOutputPicture() {
        return outputPicture;
    }

    public static void setOutputPicture(ImagePanel aOutputPicture) {
        outputPicture = aOutputPicture;
    }

    public static ImagePanel getNoisePicture() {
        return noisePicture;
    }

    public static void setNoisePicture(ImagePanel aNoisePicture) {
        noisePicture = aNoisePicture;
    }

    public static JPanel getTextPanel() {
        return textPanel;
    }

    public static void setTextPanel(JPanel aTextPanel) {
        textPanel = aTextPanel;
    }

    public static JTextArea getInputText() {
        return inputText;
    }

    public static void setInputText(JTextArea aInputText) {
        inputText = aInputText;
    }

    public static JTextArea getOutputText() {
        return outputText;
    }

    public static void setOutputText(JTextArea aOutputText) {
        outputText = aOutputText;
    }

    public static JLabel getInputTextLabel() {
        return inputTextLabel;
    }

    public static void setInputTextLabel(JLabel aInputTextLabel) {
        inputTextLabel = aInputTextLabel;
    }

    public static JLabel getOutputTextLabel() {
        return outputTextLabel;
    }

    public static void setOutputTextLabel(JLabel aOutputTextLabel) {
        outputTextLabel = aOutputTextLabel;
    }

    public static JButton getCriptTextButton() {
        return criptTextButton;
    }

    public static void setCriptTextButton(JButton aCriptTextButton) {
        criptTextButton = aCriptTextButton;
    }

    public static JLabel getTitleInputFile() {
        return titleInputFile;
    }

    public static void setTitleInputFile(JLabel aTitleInputFile) {
        titleInputFile = aTitleInputFile;
    }

    public static JLabel getTitleOutputFile() {
        return titleOutputFile;
    }

    public static void setTitleOutputFile(JLabel aTitleOutputFile) {
        titleOutputFile = aTitleOutputFile;
    }

    public static JLabel getTitleNoiseFile() {
        return titleNoiseFile;
    }

    public static void setTitleNoiseFile(JLabel aTitleNoiseFile) {
        titleNoiseFile = aTitleNoiseFile;
    }

    public static File getInputFile() {
        return inputFile;
    }

    public static void setInputFile(File aInputFile) {
        inputFile = aInputFile;
    }

    public static File getOutputFile() {
        return outputFile;
    }

    public static void setOutputFile(File aOutputFile) {
        outputFile = aOutputFile;
    }

    public static JProgressBar getProgressBar() {
        return progressBar;
    }

    public static void setProgressBar(JProgressBar aProgressBar) {
        progressBar = aProgressBar;
    }

    public static JButton getCriptButton() {
        return criptButton;
    }

    public static void setCriptButton(JButton aCriptButton) {
        criptButton = aCriptButton;
    }

    public static boolean isIsChange() {
        return isChange;
    }

    public static void setIsChange(boolean aIsChange) {
        isChange = aIsChange;
    }

}
