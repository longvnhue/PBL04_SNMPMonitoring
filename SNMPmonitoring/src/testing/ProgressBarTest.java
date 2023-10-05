package testing;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

public class ProgressBarTest extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JProgressBar progressBar;
    private JButton btnNewButton;
    private Timer timer;
    private int progressValue = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ProgressBarTest frame = new ProgressBarTest();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ProgressBarTest() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        btnNewButton = new JButton("GO");
        btnNewButton.setBounds(159, 165, 89, 23);
        contentPane.add(btnNewButton);

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 176, 176));
        progressBar.setBounds(82, 217, 267, 33);
        contentPane.add(progressBar);

        textField = new JTextField();
        textField.setBounds(132, 103, 148, 33);
        contentPane.add(textField);
        textField.setColumns(10);

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startProgressBar();
            }
        });
    }

    private void startProgressBar() {
        // Disable the button while the progress bar is running
        btnNewButton.setEnabled(false);

        // Create a Timer to increment the progress bar
        timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (progressValue < Integer.parseInt(textField.getText())) {
                    progressValue += 1; // Increase the progress by 5% each time
                    progressBar.setValue(progressValue);
                } else {
                    // When progress reaches 100%, stop the timer and enable the button
                    timer.stop();
                    btnNewButton.setEnabled(true);
                }
            }
        });

        // Start the timer
        timer.start();
    }
}
