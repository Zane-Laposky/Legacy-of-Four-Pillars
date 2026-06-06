/*
 * MapPanel.java
 * Spring 2026
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Room;

/**
 * MapPanel renders a zoomable, scrollable minimap of the dungeon by
 * painting each visited room at its grid coordinate.
 * Unvisited adjacent rooms are shown as fog hints so the player can
 * see where doors lead without revealing room contents.
 *
 * @author Zane Laposky
 * @version 1.0
 */
public class MapPanel implements PropertyChangeListener {

    /**
     * Name of the property change event that supplies the current room.
     */
    private static final String ROOM_PROPERTY = "room";

    /**
     * Default size used when the canvas has not been laid out yet.
     */
    private static final int DEFAULT_CANVAS_SIZE = 300;

    /**
     * Initial zoom level for the map.
     */
    private static final double INITIAL_ZOOM = 2.0;

    /**
     * Minimum allowed zoom level.
     */
    private static final double ZOOM_MIN = 0.5;

    /**
     * Maximum allowed zoom level.
     */
    private static final double ZOOM_MAX = 6.0;

    /**
     * Amount of zoom change per mouse wheel tick.
     */
    private static final double ZOOM_STEP = 0.12;

    /**
     * Each room string is 3 characters wide and 3 rows tall.
     */
    private static final int ROOM_CHARACTER_COUNT = 3;

    /**
     * Approximate character width used for initial centering.
     */
    private static final int APPROXIMATE_CHARACTER_WIDTH = 8;

    /**
     * Background color for the map area.
     */
    private static final Color COL_BACKGROUND = new Color(18, 18, 24);

    /**
     * Color used for visited rooms.
     */
    private static final Color COL_VISITED = new Color(200, 215, 245);

    /**
     * Color used for the current room.
     */
    private static final Color COL_CURRENT = new Color(255, 220, 60);

    /**
     * Color used for fog-of-war room hints.
     */
    private static final Color COL_FOG = new Color(70, 80, 105);

    /**
     * Color used for the title label.
     */
    private static final Color COL_TITLE = new Color(160, 170, 200);

    /**
     * Color used for the hint label.
     */
    private static final Color COL_HINT = new Color(80, 90, 115);

    /**
     * Outer panel that holds the title, map canvas, and hint text.
     */
    private final JPanel myOuterPanel;

    /**
     * Canvas used to draw the map.
     */
    private final MapCanvas myCanvas;

    /**
     * Rooms the hero has visited, keyed by "x,y".
     */
    private final Map<String, Room> myVisitedRooms = new HashMap<>();

    /**
     * Adjacent unvisited room coordinates hinted by open doors.
     */
    private final Set<String> myHintedCoords = new HashSet<>();

    /**
     * Current room supplied by the controller.
     */
    private Room myCurrentRoom;

    /**
     * Current zoom level.
     */
    private double myZoom = INITIAL_ZOOM;

    /**
     * Horizontal drawing offset.
     */
    private double myOffsetX;

    /**
     * Vertical drawing offset.
     */
    private double myOffsetY;

    /**
     * Constructs the map panel and initializes the UI controls.
     */
    public MapPanel() {
        myCanvas = new MapCanvas();

        myOuterPanel = new JPanel(new BorderLayout());
        myOuterPanel.setBackground(COL_BACKGROUND);

        final JLabel title = new JLabel("Map");
        title.setForeground(COL_TITLE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 11f));
        title.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        myOuterPanel.add(title, BorderLayout.NORTH);

        myOuterPanel.add(myCanvas, BorderLayout.CENTER);

        final JLabel hint = new JLabel("Scroll = zoom  ·  drag = pan");
        hint.setForeground(COL_HINT);
        hint.setFont(hint.getFont().deriveFont(9f));
        hint.setBorder(BorderFactory.createEmptyBorder(2, 8, 4, 8));
        myOuterPanel.add(hint, BorderLayout.SOUTH);

        initMouseListeners();
    }

    /**
     * Returns the outer panel used by the view layout.
     *
     * @return the map panel
     */
    public JPanel getPanel() {
        return myOuterPanel;
    }

    /**
     * Responds to room updates from the controller.
     *
     * @param theEvent the property change event
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (ROOM_PROPERTY.equals(theEvent.getPropertyName())) {
            final Room room = (Room) theEvent.getNewValue();

            if (room == null) {
                clearMap();
            } else {
                final String roomKey = key(room.getX(), room.getY());
                myCurrentRoom = room;

                if (!myVisitedRooms.containsKey(roomKey)) {
                    myVisitedRooms.put(roomKey, room);
                    centreOn(room);
                }

                myHintedCoords.remove(roomKey);
                updateHintedCoords(room);
            }

            myCanvas.repaint();
        }
        if (theEvent.getPropertyName().equals("resetMap")) {
            clearMap();
        }
    }

    /**
     * Clears the current map state.
     */
    /**
     * Clears all map data and restores the map to its
     * initial state for a new game.
     */
    private void clearMap() {

        myVisitedRooms.clear();
        myHintedCoords.clear();
        myCurrentRoom = null;
        myZoom = INITIAL_ZOOM;
        myOffsetX = 0;
        myOffsetY = 0;
        myCanvas.revalidate();
        myCanvas.repaint();
    }

    /**
     * Updates fog hints for the supplied room's neighbors.
     *
     * @param theRoom the room whose neighbors should be hinted
     */
    private void updateHintedCoords(final Room theRoom) {
        final Room northRoom = theRoom.getNorthRoom();
        final Room southRoom = theRoom.getSouthRoom();
        final Room eastRoom = theRoom.getEastRoom();
        final Room westRoom = theRoom.getWestRoom();

        if (northRoom != null) {
            final String northKey = key(northRoom.getX(), northRoom.getY());
            if (!myVisitedRooms.containsKey(northKey)) {
                myHintedCoords.add(northKey);
            }
        }

        if (southRoom != null) {
            final String southKey = key(southRoom.getX(), southRoom.getY());
            if (!myVisitedRooms.containsKey(southKey)) {
                myHintedCoords.add(southKey);
            }
        }

        if (eastRoom != null) {
            final String eastKey = key(eastRoom.getX(), eastRoom.getY());
            if (!myVisitedRooms.containsKey(eastKey)) {
                myHintedCoords.add(eastKey);
            }
        }

        if (westRoom != null) {
            final String westKey = key(westRoom.getX(), westRoom.getY());
            if (!myVisitedRooms.containsKey(westKey)) {
                myHintedCoords.add(westKey);
            }
        }
    }

    /**
     * Builds the key used to store grid coordinates in maps and sets.
     *
     * @param theX the x-coordinate
     * @param theY the y-coordinate
     * @return the coordinate key
     */
    private static String key(final int theX, final int theY) {
        return theX + "," + theY;
    }

    /**
     * Centers the map view on the supplied room.
     *
     * @param theRoom the room to center on
     */
    private void centreOn(final Room theRoom) {
        final int canvasWidth = myCanvas.getWidth() > 0
                ? myCanvas.getWidth() : DEFAULT_CANVAS_SIZE;
        final int canvasHeight = myCanvas.getHeight() > 0
                ? myCanvas.getHeight() : DEFAULT_CANVAS_SIZE;

        final double roomPixelSize = ROOM_CHARACTER_COUNT
                * charWidth() * myZoom;

        myOffsetX = canvasWidth / 2.0 - theRoom.getX() * roomPixelSize;
        myOffsetY = canvasHeight / 2.0 + theRoom.getY() * roomPixelSize;
    }

    /**
     * Approximate character width used only for initial centering.
     *
     * @return the approximate character width
     */
    private static int charWidth() {
        return APPROXIMATE_CHARACTER_WIDTH;
    }

    /**
     * Installs the mouse wheel and drag listeners used for zooming and panning.
     */
    private void initMouseListeners() {
        myCanvas.addMouseWheelListener((MouseWheelEvent theEvent) -> {
            final double previousZoom = myZoom;
            myZoom -= theEvent.getPreciseWheelRotation() * ZOOM_STEP;
            myZoom = Math.max(ZOOM_MIN, Math.min(ZOOM_MAX, myZoom));

            final double zoomFactor = myZoom / previousZoom;
            final double mouseX = theEvent.getX();
            final double mouseY = theEvent.getY();

            myOffsetX = mouseX - (mouseX - myOffsetX) * zoomFactor;
            myOffsetY = mouseY - (mouseY - myOffsetY) * zoomFactor;
            myCanvas.repaint();
        });

        final int[] dragPoint = new int[2];

        myCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent theEvent) {
                dragPoint[0] = theEvent.getX();
                dragPoint[1] = theEvent.getY();
                myCanvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseReleased(final MouseEvent theEvent) {
                myCanvas.setCursor(Cursor.getDefaultCursor());
            }
        });

        myCanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(final MouseEvent theEvent) {
                myOffsetX += theEvent.getX() - dragPoint[0];
                myOffsetY += theEvent.getY() - dragPoint[1];
                dragPoint[0] = theEvent.getX();
                dragPoint[1] = theEvent.getY();
                myCanvas.repaint();
            }
        });
    }

    /**
     * Inner canvas responsible for drawing the map contents.
     */
    private final class MapCanvas extends JPanel {

        /**
         * Constructs the drawing canvas.
         */
        MapCanvas() {
            setBackground(COL_BACKGROUND);
            setOpaque(true);
        }

        /**
         * Paints the map, hints, and placeholder text.
         *
         * @param theGraphics the graphics context
         */
        @Override
        protected void paintComponent(final Graphics theGraphics) {
            super.paintComponent(theGraphics);

            final Graphics2D graphics2D = (Graphics2D) theGraphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (myCurrentRoom == null) {
                drawPlaceholder(graphics2D);
            } else {
                final int fontSize = Math.max(6, (int) (8 * myZoom));
                final Font monoFont = new Font(Font.MONOSPACED, Font.PLAIN, fontSize);
                graphics2D.setFont(monoFont);

                final FontMetrics fontMetrics = graphics2D.getFontMetrics();
                final int charW = fontMetrics.charWidth('M');
                final int charH = fontMetrics.getHeight();
                final int roomW = charW * ROOM_CHARACTER_COUNT;
                final int roomH = charH * ROOM_CHARACTER_COUNT;

                graphics2D.setColor(COL_FOG);
                for (final String hintKey : myHintedCoords) {
                    final String[] parts = hintKey.split(",");
                    final int gridX = Integer.parseInt(parts[0]);
                    final int gridY = Integer.parseInt(parts[1]);
                    final int pixelX = roomPixelX(gridX, roomW);
                    final int pixelY = roomPixelY(gridY, roomH);
                    drawFogRoom(graphics2D, fontMetrics, pixelX, pixelY, charW, charH);
                }

                for (final Map.Entry<String, Room> entry : myVisitedRooms.entrySet()) {
                    final Room room = entry.getValue();
                    final boolean isCurrentRoom = room == myCurrentRoom;
                    final int pixelX = roomPixelX(room.getX(), roomW);
                    final int pixelY = roomPixelY(room.getY(), roomH);

                    graphics2D.setColor(isCurrentRoom ? COL_CURRENT : COL_VISITED);
                    drawRoomString(graphics2D, fontMetrics, room.toString(),
                            pixelX, pixelY, charW, charH);
                }
            }
        }

        /**
         * Draws the 3x3 character grid produced by Room.toString().
         *
         * @param theGraphics the graphics context
         * @param theFontMetrics the current font metrics
         * @param theRoomString the room string to draw
         * @param thePixelX the x-coordinate of the room's top-left corner
         * @param thePixelY the y-coordinate of the room's top-left corner
         * @param theCharW the width of one character cell
         * @param theCharH the height of one character row
         */
        private void drawRoomString(final Graphics2D theGraphics,
                                    final FontMetrics theFontMetrics,
                                    final String theRoomString,
                                    final int thePixelX,
                                    final int thePixelY,
                                    final int theCharW,
                                    final int theCharH) {
            final String[] rows = theRoomString.split("%%");
            if (rows.length != ROOM_CHARACTER_COUNT) {
                return;
            }

            final int baseline = thePixelY + theFontMetrics.getAscent();

            for (int row = 0; row < ROOM_CHARACTER_COUNT; row++) {
                final String rowString = rows[row];
                for (int col = 0; col < rowString.length()
                        && col < ROOM_CHARACTER_COUNT; col++) {
                    final String character = String.valueOf(rowString.charAt(col));
                    theGraphics.drawString(character,
                            thePixelX + col * theCharW,
                            baseline + row * theCharH);
                }
            }
        }

        /**
         * Draws a fogged placeholder room for an adjacent unvisited location.
         *
         * @param theGraphics the graphics context
         * @param theFontMetrics the current font metrics
         * @param thePixelX the x-coordinate of the placeholder's top-left corner
         * @param thePixelY the y-coordinate of the placeholder's top-left corner
         * @param theCharW the width of one character cell
         * @param theCharH the height of one character row
         */
        private void drawFogRoom(final Graphics2D theGraphics,
                                 final FontMetrics theFontMetrics,
                                 final int thePixelX,
                                 final int thePixelY,
                                 final int theCharW,
                                 final int theCharH) {
            final int baseline = thePixelY + theFontMetrics.getAscent();
            final String[] placeholder = {"╔═╗", "║?║", "╚═╝"};

            for (int row = 0; row < ROOM_CHARACTER_COUNT; row++) {
                final String rowString = placeholder[row];
                for (int col = 0; col < ROOM_CHARACTER_COUNT; col++) {
                    final String character = String.valueOf(rowString.charAt(col));
                    theGraphics.drawString(character,
                            thePixelX + col * theCharW,
                            baseline + row * theCharH);
                }
            }
        }

        /**
         * Draws the placeholder shown before the map has any room data.
         *
         * @param theGraphics the graphics context
         */
        private void drawPlaceholder(final Graphics2D theGraphics) {
            theGraphics.setColor(COL_HINT);
            theGraphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

            final String message = "Start a game to see the map";
            final FontMetrics fontMetrics = theGraphics.getFontMetrics();

            theGraphics.drawString(message,
                    (getWidth() - fontMetrics.stringWidth(message)) / 2,
                    (getHeight() + fontMetrics.getAscent()) / 2);
        }

        /**
         * Converts a grid x-coordinate to a pixel x-coordinate.
         *
         * @param theGridX the grid x-coordinate
         * @param theRoomW the room width in pixels
         * @return the pixel x-coordinate
         */
        private int roomPixelX(final int theGridX, final int theRoomW) {
            return (int) (myOffsetX + theGridX * theRoomW);
        }

        /**
         * Converts a grid y-coordinate to a pixel y-coordinate.
         *
         * Grid y increases upward while screen y increases downward.
         *
         * @param theGridY the grid y-coordinate
         * @param theRoomH the room height in pixels
         * @return the pixel y-coordinate
         */
        private int roomPixelY(final int theGridY, final int theRoomH) {
            return (int) (myOffsetY - theGridY * theRoomH - theRoomH);
        }
    }
}