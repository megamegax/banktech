package com.banktech.javachallenge.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.domain.submarine.Submarine;

public class MapPanel extends JPanel {

    private static final double SCALE = 0.5;
    private static final double SPEED_SCALE = 2;
    private static final Dimension DIMENSION = new Dimension((int) (1700 * SCALE), (int) (800 * SCALE));

    private static final long serialVersionUID = 1L;

    private ViewModel viewModel;

    @Override
    public Dimension getPreferredSize() {
        return DIMENSION;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, DIMENSION.width, DIMENSION.height);
        g.setColor(Color.GRAY);
        if (viewModel != null && viewModel.getGame() != null) {
            List<Position> islands = viewModel.getGame().getMapConfiguration().getIslandPositions();
            for (Position island : islands) {
                drawCircle(g, island, viewModel.getGame().getMapConfiguration().getIslandSize());
            }
            g.setColor(Color.BLUE);
            List<OwnSubmarine> ownSubmarines = viewModel.getOwnSubmarines();
            for (OwnSubmarine submarine : ownSubmarines) {
                drawCircle(g, submarine.getPosition(), viewModel.getGame().getMapConfiguration().getSubmarineSize());
            }
            g.setColor(Color.BLACK);
            for (OwnSubmarine submarine : ownSubmarines) {
                drawSpeed(g, submarine.getPosition(), submarine.getAngle(), submarine.getVelocity());
            }
            g.setColor(Color.RED);
            List<Submarine> enemySubmarines = viewModel.getDetectedSubmarines();
            for (Submarine submarine : enemySubmarines) {
                drawCircle(g, submarine.getPosition(), viewModel.getGame().getMapConfiguration().getSubmarineSize());
            }
            g.setColor(Color.BLACK);
            for (Submarine submarine : enemySubmarines) {
                drawSpeed(g, submarine.getPosition(), submarine.getAngle(), submarine.getVelocity());
            }
        }
    }

    private void drawSpeed(Graphics g, Position position, double angle, double speed) {
        g.drawLine(scale(position.getX()), scale(position.getY()), 
                scale(position.getX() + SPEED_SCALE * Math.cos(angle / 180 * Math.PI) * speed), 
                scale(position.getY() + SPEED_SCALE * Math.sin(angle / 180 * Math.PI) * speed));
    }
    
    private void drawCircle(Graphics g, Position position, int radius) {
        g.fillOval(scale(position.getX() - radius), scale(position.getY() - radius), scale(radius * 2), scale(radius * 2));
    }

    private int scale(double number) {
        return (int) (SCALE * number);
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        repaint();
    }

}
