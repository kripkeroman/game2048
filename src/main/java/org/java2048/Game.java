package org.java2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Game extends JPanel implements KeyListener, Runnable
{
    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 500;
    public static final int HEIGHT = 600;
    public static final Font main = new Font("Babes Neue Regular", Font.PLAIN, 28);
    private Thread game;
    private boolean running;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private GameBoard board;

    private long startTime;
    private long elapsed;
    private boolean set;

    public Game()
    {
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);

        board = new GameBoard(WIDTH / 2 - GameBoard.BOARD_WIDTH / 2, HEIGHT - GameBoard.BOARD_HEIGHT - 10);
    }

    private void update()
    {
        board.update();
        Keyboard.update();
    }

    private void render()
    {
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0, WIDTH, HEIGHT);
        board.render(g);
        g.dispose();

        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run()
    {
        int fps = 0, updates = 0;
        long fpsTime = System.currentTimeMillis();
        double nsPerUpdate = 1000000000.0 / 60;
        double then = System.nanoTime();
        double unprocessed = 0;

        while (running)
        {
            boolean shouldRender = false;
            double now = System.nanoTime();
            unprocessed += (now - then) / nsPerUpdate;
            then = now;

            //update queue
            while (unprocessed >= 1) {
                updates++;
                update();
                unprocessed--;
                shouldRender = true;
            }

            //render
            if (shouldRender) {
                fps++;
                render();
                shouldRender = false;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception ex) {

                }
            }
        }

        //FPS timer
        if(System.currentTimeMillis() - fpsTime > 1000)
        {
            System.out.printf("%d fps %d", fps, updates);
            System.out.println();
            fps = 0;
            updates = 0;
            fpsTime += 1000;
        }
    }

    public synchronized void start()
    {
        if(running) return;
        running = true;
        game = new Thread(this, "game");
        game.start();
    }

    public synchronized void stop()
    {
        if(!running) return;
        running = false;
        System.exit(0);
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
        Keyboard.keyPressed(e);
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
        Keyboard.keyReleased(e);
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e)
    {

    }
}
