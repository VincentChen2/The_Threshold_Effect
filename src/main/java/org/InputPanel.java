package org;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPanel extends JPanel {
    private final GraphController controller;

    private JSlider nSlider;
    private JButton generateButton;
    private JLabel nValueLabel;
    private JSpinner pStartSpinner;
    private JSpinner pEndSpinner;
    private JSpinner pStepsSpinner;
    private JSpinner graphsPerPSpinner;

    public InputPanel(GraphController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Graph Parameters"));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;

        //(n) Parameter Label
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        JLabel nLabel = new JLabel("Number of vertices (n):");
        add(nLabel, gridBagConstraints);

        //(n) Slider
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        nSlider = new JSlider(0, 1000, 10);
        nSlider.setMajorTickSpacing(200);
        nSlider.setMinorTickSpacing(100);
        nSlider.setPaintTicks(true);
        nSlider.setPaintLabels(true);
        add(nSlider, gridBagConstraints);

        //(n) Value Label
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        nValueLabel = new JLabel("Value: " + nSlider.getValue());
        nValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(nValueLabel, gridBagConstraints);

        //Slider Action Listener (Updates Value Label)
        nSlider.addChangeListener(e -> nValueLabel.setText("Value: " + nSlider.getValue()));

        //(n) Manual Input
        //(n) Manual Input Label
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        JLabel manualInputLabel = new JLabel("Manual n input:");
        add(manualInputLabel, gridBagConstraints);

        //(n) Manual Input Spinner
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        JSpinner nManualSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 10000, 1));
        nManualSpinner.setPreferredSize(new Dimension(100, nManualSpinner.getPreferredSize().height));
        add(nManualSpinner, gridBagConstraints);

        //(n) Manual Input Apply Label
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        JButton applyNButton = new JButton("Apply");

        //(n) Manual Input Apply Button Listener
        applyNButton.addActionListener(e -> {
            int newValue = (Integer) nManualSpinner.getValue();
            nSlider.setValue(newValue);
        });
        add(applyNButton, gridBagConstraints);

        //(p) Range Panel
        JPanel pRangePanel = createPRangePanel();

        //Add Panel
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        add(pRangePanel, gridBagConstraints);

        //Generate Button
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new Insets(15, 5, 5, 5);
        generateButton = new JButton("Generate Graphs");
        generateButton.setPreferredSize(new Dimension(200, 35));
        generateButton.addActionListener(new GenerateButtonListener());
        assert generateButton != null;
        add(generateButton, gridBagConstraints);
    }

    private JPanel createPRangePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Probability Parameters"));

        //(p) Start Label
        JLabel pStartLabel = new JLabel("p start:");
        panel.add(pStartLabel);

        //(p) Start Spinner
        pStartSpinner = new JSpinner(new SpinnerNumberModel(0.1, 0.001, 1.0, 0.001));
        JSpinner.NumberEditor pStartEditor = new JSpinner.NumberEditor(pStartSpinner, "0.000");
        pStartSpinner.setEditor(pStartEditor);
        pStartSpinner.setPreferredSize(new Dimension(80, pStartSpinner.getPreferredSize().height));
        panel.add(pStartSpinner);

        //(p) End Label
        JLabel pEndLabel = new JLabel("p end:");
        panel.add(pEndLabel);

        //(p) End Spinner
        pEndSpinner = new JSpinner(new SpinnerNumberModel(0.9, 0.001, 1.0, 0.001));
        JSpinner.NumberEditor pEndEditor = new JSpinner.NumberEditor(pEndSpinner, "0.000");
        pEndSpinner.setEditor(pEndEditor);
        pEndSpinner.setPreferredSize(new Dimension(80, pEndSpinner.getPreferredSize().height));
        panel.add(pEndSpinner);

        //(p) Steps Label
        JLabel pStepsLabel = new JLabel("p steps:");
        panel.add(pStepsLabel);

        //(p) Steps Spinner
        pStepsSpinner = new JSpinner(new SpinnerNumberModel(5, 2, 100, 1));
        pStepsSpinner.setPreferredSize(new Dimension(60, pStepsSpinner.getPreferredSize().height));
        panel.add(pStepsSpinner);

        //Graphs per p Label
        JLabel graphsPerPLabel = new JLabel("Graphs per p:");
        panel.add(graphsPerPLabel);

        //Graphs per p Spinner
        graphsPerPSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        graphsPerPSpinner.setPreferredSize(new Dimension(65, graphsPerPSpinner.getPreferredSize().height));
        panel.add(graphsPerPSpinner);

        return panel;
    }

    public int getNValue() {
        return nSlider.getValue();
    }

    public double getPStart() {
        return ((Number) pStartSpinner.getValue()).doubleValue();
    }

    public double getPEnd() {
        return ((Number) pEndSpinner.getValue()).doubleValue();
    }

    public int getPSteps() {
        return (Integer) pStepsSpinner.getValue();
    }

    public int getGraphsPerP() {
        return (Integer) graphsPerPSpinner.getValue();
    }

    public void setGenerateButtonEnabled(boolean enabled) {
        generateButton.setEnabled(enabled);
    }

    public void generateGraphs() {
        controller.generateGraphs();
    }

    private class GenerateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            generateGraphs();
        }
    }
}