/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JFrame implements Runnable {
    static final int XBORDER = 20;
    static final int YBORDER = 20;
    static final int YTITLE = 30;
    static final int WINDOW_BORDER = 8;
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + (495 * 2);
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + (460 * 2);
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;
    
    final int numRows = 20;
    final int numColumns = 20;

    int board[][];
    
    int numMobs = 3;
    Mob[] mobs = new Mob[numMobs];
    Player playerOne,playerTwo;

    int currentRow;
    int currentColumn;

    int columnDir;
    int rowDir;
    
    boolean gameOver, playerOneTurn;
    
    int timeCount;
    int timeSpeedVal;
    
    static Game frame1;
    static Menu menuFrame;
    static SettingsMenu settings;
    public static void main(String[] args) {
        frame1 = new Game();
        frame1.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(false);
        
        menuFrame = new Menu();
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setVisible(true);
       
        settings = new SettingsMenu();
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settings.setVisible(false);
    }

    public Game() {

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button
                    int xpos = e.getX() - getX(0);
                    int ypos = e.getY() - getY(0);
                    if (xpos < 0 || ypos < 0 || xpos > getWidth2() || ypos > getHeight2())
                        return;
                    int ydelta = getHeight2()/numRows;
                    int xdelta = getWidth2()/numColumns;
                    
                    currentColumn = xpos/xdelta;
                    currentRow = ypos/ydelta;
                    
                    for(int i = 0; i < numMobs;i++)
                    {
                        mobs[i].setSelected(false);
                        if(mobs[i].getCurrColumn() == currentColumn && mobs[i].getCurrRow() == currentRow)
                        {
                            mobs[i].setSelected(true);
                        }
                    }
                }
                if (e.BUTTON3 == e.getButton()) {
                     //right button
                
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_RIGHT == e.getKeyCode())
                {
                    for(int i = 0; i < numMobs;i++)
                    {
                      if(mobs[i].isSelected())
                      {
                          if(playerOne.getNumTurns() > 0)
                          {
                            mobs[i].setCurrColumn(mobs[i].getCurrColumn()+1);
                            playerOne.setNumTurns(playerOne.getNumTurns()-1);
                          }
                      }
                    }
                }
                if (e.VK_LEFT == e.getKeyCode())
                {
                        for(int i = 0; i < numMobs;i++)
                        {
                          if(mobs[i].isSelected())
                          {
                              if(playerOne.getNumTurns() >0)
                                {
                                   mobs[i].setCurrColumn(mobs[i].getCurrColumn()-1);
                                   playerOne.setNumTurns(playerOne.getNumTurns()-1);
                                }
                          }
                        }
                }
                if (e.VK_UP == e.getKeyCode())
                {
                     for(int i = 0; i < numMobs;i++)
                        {
                          if(mobs[i].isSelected())
                          {
                              if(playerOne.getNumTurns() >0)
                                {
                                    mobs[i].setCurrRow(mobs[i].getCurrRow()-1);
                                    playerOne.setNumTurns(playerOne.getNumTurns()-1);
                                }
                          }
                        }
                }
                if (e.VK_DOWN == e.getKeyCode())
                {
                     for(int i = 0; i < numMobs;i++)
                        {
                          if(mobs[i].isSelected())
                          {
                              if(playerOne.getNumTurns() >0)
                                {
                                    mobs[i].setCurrRow(mobs[i].getCurrRow()+1);
                                    playerOne.setNumTurns(playerOne.getNumTurns()-1);
                                }
                          }
                        }
                }
                if (e.VK_ESCAPE == e.getKeyCode())
                {
                    frame1.setVisible(false);
                    menuFrame.setVisible(true);
                }

                repaint();
            }
        });
        init();
        start();
    }




    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

//fill background
        g.setColor(Color.cyan);

        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }

        g.setColor(Color.black);
//horizontal lines
        for (int zi=1;zi<numRows;zi++)
        {
            g.drawLine(getX(0) ,getY(0)+zi*getHeight2()/numRows ,
            getX(getWidth2()) ,getY(0)+zi*getHeight2()/numRows );
        }
//vertical lines
        for (int zi=1;zi<numColumns;zi++)
        {
            g.drawLine(getX(0)+zi*getWidth2()/numColumns ,getY(0) ,
            getX(0)+zi*getWidth2()/numColumns,getY(getHeight2())  );
        }

                for(int i = 0; i < numMobs;i++)
                {
                    mobs[i].draw(g,getX(0)+mobs[i].getCurrColumn()*getWidth2()/numColumns,
                    getY(0)+mobs[i].getCurrRow()*getHeight2()/numRows,
                    getWidth2()/numColumns,
                    getHeight2()/numRows);
                }
               
                 g.drawString("Player 1 Turns Left : " + playerOne.getNumTurns(), 32, 50);  
                 g.drawString("Player 2 Turns Left : " + playerTwo.getNumTurns(), 332, 50);  

 
      gOld.drawImage(image, 0, 0, null);
    }

////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = .05;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {
        playerOne = new Player();
        playerTwo = new Player();
        playerOneTurn = true;
        playerOne.setNumTurns( (int) (Math.random() * 18 +1));

        mobs[0] = new Mob(Color.black);
        mobs[1] = new Mob(Color.black);
        mobs[2] = new Mob(Color.black);
       
        mobs[0].setCurrColumn(12);
        mobs[0].setCurrRow(0);
        mobs[1].setCurrColumn(9);
        mobs[1].setCurrRow(0);
        mobs[2].setCurrColumn(6);
        mobs[2].setCurrRow(0);
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }
            reset();
        }
        
        if(playerOne.getNumTurns() <= 0 && playerOneTurn)
        {
            playerOneTurn = false;
            playerTwo.setNumTurns( (int) (Math.random() * 18 +1));
        }
        else if(playerTwo.getNumTurns() <= 0 &&  !playerOneTurn)
        {
            playerOneTurn = true;
            playerOne.setNumTurns( (int) (Math.random() * 18 +1));
        }
        
      timeCount++;  
    }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
      public int getX(int x) {
        return (x + XBORDER + WINDOW_BORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE );
    }

    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    
    public int getWidth2() {
        return (xsize - 2 * (XBORDER + WINDOW_BORDER));
    }

    public int getHeight2() {
        return (ysize - 2 * YBORDER - WINDOW_BORDER - YTITLE);
    }
    
}

