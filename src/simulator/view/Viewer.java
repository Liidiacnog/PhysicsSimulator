package simulator.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.SimulatorObserver;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.border.TitledBorder;
import java.awt.*;
import simulator.factories.Factory;

public class Viewer extends JComponent implements SimulatorObserver {

    private int _centerX, _centerY; // coordinates of its origin position (width and the height of the component by
                                    // 2)
    private final int _radius = 6; // current list of bodies for drawing them, updated when the state changes.
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
    private Map<Body, Color> _bodiesColors; //maps a body to its colour, to be used when repainting it
    private Controller _ctrl;
    private Body _dgBody; //body being dragged by the user to perform some action on it
    private final String _helpMsg = "h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit" + '\n';
    private static String BodiesSelectionDialogTitle = "Addition of bodies";
    private static String BodiesSelectionDialogInstr = 
    "Select a body and provide values for the parameters in the 'Values' column";

    Viewer(Controller ctrl, Factory<Body> fB) {
        _fB = fB;
        _ctrl = ctrl;
        _bodySelDialog = new SelectionDialog((Frame) SwingUtilities.getWindowAncestor(this), _fB,
                BodiesSelectionDialogTitle, BodiesSelectionDialogInstr);
        initGUI();
        ctrl.addObserver(this);
    }

    private void initGUI() {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2), "Viewer",
                TitledBorder.LEFT, TitledBorder.RIGHT));

        _bodies = new ArrayList<>();
        _bodiesColors = new HashMap<>();
        _scale = 1.0;
        _showHelp = true;
        _showVectors = true;

        //to be able to drag bodies and change their position:
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO? _centerX + (int) (b.getPosition().getX() / _scale);
                if (_dgBody != null && ControlPanel.getStop()) {
                    double bx = (e.getX() - _centerX) * _scale;
                    double by = (e.getY() - _centerY) * _scale; // TODO -centerX, Y,  por qué?, y por qué no se hace igual que en getSelectedBody()
                    _dgBody.setPosition(bx, by);
                    repaint();
                }

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                //Do nothing
            }

        });
        
        //to be able to select bodies and change their colour
        addMouseListener(new MouseListener() {

            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocus();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1 && ControlPanel.getStop()) { //bodies are selected by double-clicking!
                    Body selBody = getSelectedBody(e.getX(), e.getY());
                    if (selBody != null) {
                        Color c = JColorChooser.showDialog(new JFrame(), "Change body color", Color.BLUE);
                        _bodiesColors.put(selBody, c);
                        repaint();
                    } else if (_bodySelDialog.open() == 1) {
                        try{
                            _ctrl.addBody(_bodySelDialog.getCBoxSelection());
                        }catch(IllegalArgumentException ex){
                            JOptionPane.showMessageDialog(new JFrame(),
                                                "The following error occurred: " + ex.getMessage(),
                                                "Error found while running: ",
                                                JOptionPane.ERROR_MESSAGE,
                                                null);
                        }
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
                //Do nothing
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
                //Do nothing
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //Do nothing
            }
        });


    }

    //returns the body whose oval occupies position x, y; or null if there is none
    private Body getSelectedBody(int x, int y) {
        for (Body b : _bodies) {
            int bx = _centerX + (int) (b.getPosition().getX() / _scale);
            int by = _centerY + (int) (b.getPosition().getY() / _scale);
            if (Math.sqrt(Math.pow(x - bx, 2) + Math.pow(y - by, 2)) <= _radius) {
                return b;
            }
        }

        return null;
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

        // (1) draw a cross at the center;
        gr.drawString("+", _centerX, _centerY + 4);
        for (Body b : _bodies) {
            int x = _centerX + (int) (b.getPosition().getX() / _scale) - _radius;
            int y = _centerY + (int) (b.getPosition().getY() / _scale) - _radius;
            if (_bodiesColors.containsKey(b))
                gr.setColor(_bodiesColors.get(b));
            else
                gr.setColor(Color.BLUE);
            gr.fillOval(x, y, 2 * _radius, 2 * _radius);
            gr.setColor(Color.BLACK);
            gr.setFont(new Font("Bold", Font.BOLD, 13));
            gr.drawString(b.getId(), x - x / 205, y - y / 20);

            if (_showVectors) { // (2) draw the help message
                int scaleFactor = 18;
                x += _radius;
                y += _radius;
                int x1 = (int) b.getVelocity().direction().scale(scaleFactor).getX() + x;
                int y1 = (int) b.getVelocity().direction().scale(scaleFactor).getY() + y;
                drawLineWithArrow(gr, x, y, x1, y1, 2, 2, Color.GREEN, Color.GREEN);
                x1 = (int) b.getForce().direction().scale(scaleFactor).getX() + x;
                y1 = (int) b.getForce().direction().scale(scaleFactor).getY() + y;
                drawLineWithArrow(gr, x, y, x1, y1, 2, 3, Color.RED, Color.RED);
            }

            // (3) draw the bodies, and if _showVectors is true, also the velocity and force
            // vectors (using 2 different colours)
            if (_showHelp) {
                String help = "Scaling ratio: " + _scale;
                gr.setColor(Color.RED);
                gr.setFont(new Font("Default", Font.PLAIN, 12));
                gr.drawString(_helpMsg, 8, 32);
                gr.drawString(help, 8, 47);
                // instructions on how to add a new body
                gr.setColor(Color.BLACK);
                gr.drawString("* NOTE: If the simulator is stopped: *", 8, 62);
                gr.drawString("* you can double-click anywhere to add a new body there *", 8, 77);
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
    private void drawLineWithArrow(Graphics g, int x1, int y1, int x2, int y2, int w, int h, Color lineColor,
            Color arrowColor) {

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

    //auxiliary method for observer methods
    private void resetBodiesAndScale(List<Body> l) {
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
