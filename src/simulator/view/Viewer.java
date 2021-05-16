package simulator.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.SimulatorObserver;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.*;
import simulator.factories.Factory;

public class Viewer extends JComponent implements SimulatorObserver {

    private int _centerX, _centerY; // coordinates of its origin position (width and the height of the component by 2)
    private int _radius; // current list of bodies for drawing them, updated when the state changes.
    private double _scale; // to scale the universe so we can draw it within the area of the component
    private List<Body> _bodies;
    private SelectionDialog _bodySelDialog;
    private boolean _showHelp; /*
                                * true if the help text (in the left-top corner) should be shown, its value is
                                * changed when key ’h’ is pressed
                                */
    private boolean _showVectors; /*
                                   * true if the velocity/force vectors of each body are shown, its value is
                                   * changed when key ’v’ is pressed.
                                   */
    private Factory<Body> _fB;
    private Controller _ctrl;
    private Body _dgBody;
    private final String _helpMsg = "h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit" + '\n';
    private static String BodiesSelectionDialogTitle = "Addition of bodies"; 
    private static String BodiesSelectionDialogInstr = 
            "Select a body and provide values for the parameters in the 'Values' column"
		 		 +  "(default values are used for parameters with no user defined value)";

    Viewer(Controller ctrl, Factory<Body> fB) {
        _radius = 6;
        _fB = fB;
        _bodySelDialog = new SelectionDialog((Frame) SwingUtilities.getWindowAncestor(this), _fB,
                BodiesSelectionDialogTitle, BodiesSelectionDialogInstr);
        initGUI();
        ctrl.addObserver(this);
    }

    private void initGUI() {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2), "Viewer",
                TitledBorder.LEFT, TitledBorder.RIGHT));

        _bodies = new ArrayList<>();
        _scale = 1.0;
        _showHelp = true;
        _showVectors = true;


        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (_dgBody != null) {
                    _dgBody.setPosition(e.getX(), e.getY());
                    repaint();
                }
                
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });

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
            
            // ... TODO?

            @Override
            public void mouseEntered(MouseEvent e) {  
                requestFocus();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    if (_bodySelDialog.open() == 1) {
                        _ctrl.addBody(_bodySelDialog.getCBoxSelection()); //TODO parar la simulación
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                _dgBody = getSelectedBody(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                _dgBody = null;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }

    private Body getSelectedBody(int x, int y) {
        for (Body b: _bodies) {
            if (Math.sqrt(Math.pow(x - b.getPosition().getX(), 2) + Math.pow(y - b.getPosition().getY(), 2)) <= _radius) {
                return b;
            }
        }

        return null;
    }

    
    @Override
    protected void paintComponent(Graphics g) { //TODO poner instrucciones para añadir nuevo body
        super.paintComponent(g);

        // use ’gr’ to draw not ’g’ --- it gives nicer results
        Graphics2D gr = (Graphics2D) g;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // calculate the center
        _centerX = getWidth() / 2;
        _centerY = getHeight() / 2;
        
        //(1) draw a cross at the center;
        gr.drawString("+", _centerX, _centerY);
        for (Body b : _bodies) {
            int x = _centerX + (int) (b.getPosition().getX() / _scale);
            int y = _centerY + (int) (b.getPosition().getY() / _scale);
            gr.setColor(Color.BLUE);
            gr.fillOval(x, y, 2 * _radius, 2 * _radius);
            gr.drawString(b.getId(), x, y - y/20);

            if (_showVectors) { //(2) draw the help message 
                x += _radius;
                y += _radius;
                int x1 = (int) b.getVelocity().direction().scale(15).getX() + x;
                int y1 = (int) b.getVelocity().direction().scale(15).getY() + y;
                drawLineWithArrow(gr, x, y, x1, y1, 2, 2, Color.GREEN, Color.GREEN);
                x1 = (int) b.getForce().direction().scale(15).getX() + x;
                y1 = (int) b.getForce().direction().scale(15).getY() + y;
                drawLineWithArrow(gr, x, y, x1, y1, 2, 3, Color.RED, Color.RED);
            }
            
            //(3) draw the bodies, and if _showVectors is true, also the velocity and force vectors  (using 2 different colours)
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
    private void drawLineWithArrow(
            Graphics g, 
            int x1, int y1, 
            int x2, int y2, 
            int w, int h, 
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

    private void resetBodiesAndScale(List<Body> l){
        _bodies = new ArrayList<>(l);
        autoScale();
        repaint();
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
        resetBodiesAndScale(bodies);
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        resetBodiesAndScale(bodies);
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        resetBodiesAndScale(bodies);
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        _bodies = new ArrayList<>(bodies);
        repaint();
    }

}
