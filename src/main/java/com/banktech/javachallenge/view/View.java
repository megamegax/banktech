package com.banktech.javachallenge.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class View implements GUIListener {

    private JFrame frame;
    private MapPanel panel = new MapPanel();

    public View() {
        frame = new JFrame("Torpedo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(createTopPanel(), BorderLayout.NORTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.add(new JButton("<"));
        panel.add(new JButton(">"));
        panel.add(new JButton("Last"));
        return panel;
    }

    @Override
    public void refresh(List<ViewModel> turns) {
        panel.setViewModel(turns.get(turns.size() - 1));
    }

}
