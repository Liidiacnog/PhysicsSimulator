package simulator.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;

import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.SimulatorObserver;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Viewer extends JComponent implements SimulatorObserver {

    private int _centerX;
    private int _centerY;
    private double _scale;
    private List<Body> _bodies;
    private boolean _showHelp;
    private boolean _showVectors;
    private final String _helpMsg = "h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit" + '\n';

    Viewer(Controller ctrl) {
        initGUI();
        ctrl.addObserver(this);
    }

    private void initGUI() {
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 2), 
            "Viewer", 
            TitledBorder.LEFT,
            TitledBorder.RIGHT
            )
        );

        _bodies = new ArrayList<>();
        _scale = 1.0;
        _showHelp = true;
        _showVectors = true;

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                case '-':
                    _scale = _scale * 1.1;
                    repaint();
                    break;
                case '+':
                    _scale = Math.max(1000.0, _scale / 1.1);
                    repaint();
                    break;
                case '=':
                    autoScale();
                    repaint();
                    break;
                case 'h':
                    _showHelp = !_showHelp;
                    repaint();
                    break;
                case 'v':
                    _showVectors = !_showVectors;
                    repaint();
                    break;
                default:
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });

        addMouseListener(new MouseListener() {
            // ...
            @Override
            public void mouseEntered(MouseEvent e) { // Which to import
                requestFocus();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // use ’gr’ to draw not ’g’ --- it gives nicer results
        Graphics2D gr = (Graphics2D) g;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // calculate the center
        _centerX = getWidth() / 2;
        _centerY = getHeight() / 2;

        gr.drawString("+", _centerX, _centerY);
        int radius = 4;
        for (Body b : _bodies) {
            int x = _centerX + (int) (b.getPosition().getX()/_scale);
            int y = _centerY + (int) (b.getPosition().getY()/_scale);
            gr.setColor(Color.BLUE);
            gr.fillOval(x, y, 2*radius, 2*radius);
            if (_showVectors) {
                x += radius;
                y += radius;
                int x1 = (int) b.getVelocity().direction().scale(15).getX() + x;
                int y1 = (int) b.getVelocity().direction().scale(15).getY() + y;
                drawLineWithArrow(gr, x, y, x1, y1, 2, 2, Color.GREEN, Color.GREEN);
                x1 = (int) b.getForce().direction().scale(15).getX() + x;
                y1 = (int) b.getForce().direction().scale(15).getY() + y;
                drawLineWithArrow(gr, x, y, x1, y1, 2, 3, Color.RED, Color.RED);
            }
            if (_showHelp) {
                String help = "Scaling ratio: " + _scale;
                gr.setColor(Color.RED);
                gr.drawString(_helpMsg, 8, 32);
                gr.drawString(help, 8, 47);
            }
            
        }
    }

    private void autoScale() {
        double max = 1.0;
        for (Body b : _bodies) {
            Vector2D p = b.getPosition();
            max = Math.max(max, Math.abs(p.getX()));
            max = Math.max(max, Math.abs(p.getY()));
        }
        double size = Math.max(1.0, Math.min(getWidth(), getHeight()));
        _scale = max > size ? 4.0 * max / size : 1.0;
    }

    // This method draws a line from (x1,y1) to (x2,y2) with an arrow.
    // The arrow is of height h and width w.
    // The last two arguments are the colors of the arrow and the line
    private void drawLineWithArrow(//
            Graphics g, //
            int x1, int y1, //
            int x2, int y2, //
            int w, int h, //
            Color lineColor, Color arrowColor) {

        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - w, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;
        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;
        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;
        int[] xpoints = { x2, (int) xm, (int) xn };
        int[] ypoints = { y2, (int) ym, (int) yn };
        g.setColor(lineColor);
        g.drawLine(x1, y1, x2, y2);
        g.setColor(arrowColor);
        g.fillPolygon(xpoints, ypoints, 3);
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _bodies = new ArrayList<>(bodies);
        autoScale();
        repaint();

    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _bodies = new ArrayList<>(bodies);
        autoScale();
        repaint();

    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        _bodies = new ArrayList<>(bodies);
        autoScale();
        repaint();

    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        _bodies = new ArrayList<>(bodies);
        repaint();
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {
        // TODO Auto-generated method stub

    }

}
