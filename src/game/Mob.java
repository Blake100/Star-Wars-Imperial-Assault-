
package game;

import java.awt.Color;
import java.awt.Graphics2D;

public class Mob {
    private boolean Selected;
    private int health, dir;
    private int currRow,currColumn;
    private Color color;
    Mob(Color color){
        health = 2;
        Selected = false;
        dir=1;
      
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean Selected) {
        this.Selected = Selected;
    }

    public int getCurrRow() {
        return currRow;
    }

    public void setCurrRow(int currRow) {
        this.currRow = currRow;
    }

    public int getCurrColumn() {
        return currColumn;
    }

    public void setCurrColumn(int currColumn) {
        this.currColumn = currColumn;
    }
    public void setColor(Color color)
    {
        this.color = color;
    }
    public Color getColor()
    {
        return(color);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }
    public void draw(Graphics2D g,int xpos, int ypos,int width, int height)
    {
        g.fillOval(xpos,ypos,width,height);
    }
  
    
}
