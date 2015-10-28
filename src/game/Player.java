/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author 147003300
 */
public class Player {
    private int numTurns;
    private int maxTurns = 16;
    
    Player()
    {
    }
    public int getNumTurns()
    {
        return numTurns;
    }
    public void setNumTurns(int numTurns)
    {
        this.numTurns = numTurns;
    }
}
