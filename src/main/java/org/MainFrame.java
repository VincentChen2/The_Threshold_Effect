package org;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JSlider nSlider;
    private JSpinner pSpinner;
    private JButton generateButton;
    private JLabel statusLabel;
    private JLabel verticesLabel;
    private JLabel edgesLabel;
    private JLabel nValueLabel;

    private Graph currentGraph;

    public MainFrame() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Graph Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = createInputPanel();
        JPanel infoPanel = createInfoPanel();
        JPanel statusPanel = createStatusPanel();

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
    }

    private JPanel createInputPanel() {
        //Input Panel Settings
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Graph Parameters"));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;

        //(n) Parameter Label
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        JLabel nLabel = new JLabel("Number of vertices (n):");
        panel.add(nLabel, gridBagConstraints);

        //(n) Slider
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        nSlider = new JSlider(0, 10000, 10);
        nSlider.setMajorTickSpacing(2000);
        nSlider.setMinorTickSpacing(1000);
        nSlider.setPaintTicks(true);
        nSlider.setPaintLabels(true);
        panel.add(nSlider, gridBagConstraints);

        //(n) Value Label
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        nValueLabel = new JLabel("Value: " + nSlider.getValue());
        nValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(nValueLabel, gridBagConstraints);

        //Slider Action Listener (Updates Value Label)
        nSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                nValueLabel.setText("Value: " + nSlider.getValue());
            }
        });

        //(n) Manual Input
        //(n) Manual Input Label
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        JLabel manualInputLabel = new JLabel("Manual n input:");
        panel.add(manualInputLabel, gridBagConstraints);

        //(n) Manual Input Spinner
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        JSpinner nManualSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 10000, 1));
        nManualSpinner.setPreferredSize(new Dimension(100, nManualSpinner.getPreferredSize().height));
        panel.add(nManualSpinner, gridBagConstraints);

        //(n) Manual Input Apply Label
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        JButton applyNButton = new JButton("Apply");

        //(n) Manual Input Apply Button Listener
        applyNButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newValue = (Integer) nManualSpinner.getValue();
                nSlider.setValue(newValue);
            }
        });
        panel.add(applyNButton, gridBagConstraints);

        //(p) Parameter Label
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        JLabel pLabel = new JLabel("Edge probability (p):");
        panel.add(pLabel, gridBagConstraints);

        //(p) Spinner
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        SpinnerModel pModel = new SpinnerNumberModel(0.5, 0.0, 1.0, 0.001);
        pSpinner = new JSpinner(pModel);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(pSpinner, "0.000");
        pSpinner.setEditor(editor);
        pSpinner.setPreferredSize(new Dimension(150, pSpinner.getPreferredSize().height));
        panel.add(pSpinner, gridBagConstraints);

        //Generate Button
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new Insets(15, 5, 5, 5); // Extra top margin
        generateButton = new JButton("Generate Graph");
        generateButton.setPreferredSize(new Dimension(200, 35)); // Larger button
        generateButton.addActionListener(new GenerateButtonListener());
        panel.add(generateButton, gridBagConstraints);

        return panel;
    }

    private JPanel createInfoPanel() {
        //Info Panel Settings
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Graph Information"));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        Font infoFont = new Font("SansSerif", Font.BOLD, 14);

        //Vertices Label (Title)
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        JLabel verticesTextLabel = new JLabel("Number of vertices:");
        verticesTextLabel.setFont(infoFont);
        panel.add(verticesTextLabel, gridBagConstraints);

        //Vertices Label (Number)
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        verticesLabel = new JLabel("-");
        verticesLabel.setFont(infoFont.deriveFont(Font.PLAIN, 16f));
        verticesLabel.setForeground(Color.BLUE);
        panel.add(verticesLabel, gridBagConstraints);

        //Edges Label (Title)
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        JLabel edgesTextLabel = new JLabel("Number of edges:");
        edgesTextLabel.setFont(infoFont);
        panel.add(edgesTextLabel, gridBagConstraints);

        //Edges Label (Number)
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        edgesLabel = new JLabel("-");
        edgesLabel.setFont(infoFont.deriveFont(Font.PLAIN, 16f));
        edgesLabel.setForeground(Color.BLUE);
        panel.add(edgesLabel, gridBagConstraints);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Status"));

        //Status Label
        statusLabel = new JLabel("Ready to generate graph");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(statusLabel, BorderLayout.CENTER);

        return panel;
    }

    private class GenerateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            generateButton.setEnabled(false);
            statusLabel.setText("Generating Graph... (0/0) vertices completed");

            //Run graph generation in a separate thread to keep UI responsive
            SwingWorker<Graph, String> worker = new SwingWorker<Graph, String>() {
                @Override
                protected Graph doInBackground() throws Exception {
                    int n = nSlider.getValue();
                    double p = ((Number) pSpinner.getValue()).doubleValue();

                    return new Graph(n, p, this::publish);
                }

                @Override
                protected void process(java.util.List<String> chunks) {
                    //Update status with the latest progress
                    if (!chunks.isEmpty()) {
                        statusLabel.setText(chunks.getLast());
                    }
                }

                @Override
                protected void done() {
                    try {
                        currentGraph = get();
                        statusLabel.setText("Displaying Information");

                        // Update graph information
                        verticesLabel.setText(String.valueOf(currentGraph.getVertexCount()));
                        edgesLabel.setText(String.valueOf(currentGraph.getEdgeCount()));

                    } catch (Exception e) {
                        statusLabel.setText("Error generating graph: " + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        generateButton.setEnabled(true);
                    }
                }
            };

            worker.execute();
        }
    }
}