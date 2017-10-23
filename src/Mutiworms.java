import java.awt.*;

import acm.graphics.*;
import acm.program.GraphicsProgram;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.MouseListener; 
import java.awt.event.MouseMotionListener; 
import java.awt.event.MouseEvent;

public class Mutiworms extends GraphicsProgram {
    private static boolean redTurn;
    private GLabel redTeamName;
    private GLabel blueTeamName;
    private GLabel redHealthBar;
    private GLabel blueHealthBar;
    private double groundLevel;
    private double playerHeight;
    private double rightBound;
    private double ogHeight;
    private double ogWidth;
    private double yVelocityFactor = 1;
    private double xVelocityFactor = 1;
    private Person blueHuman;
    private Person redHuman;
    private GRect ground;
    private Treasure powerup;

    public void refresh() {

        Timer resizeTimer = new Timer(1, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                double scaleFactor;

                Mutiworms.this.resize(Mutiworms.this.getWidth(), Mutiworms.this.getHeight());
                redTeamName.setLocation(Mutiworms.this.getWidth()/2 - redTeamName.getWidth()/2, 50);
                blueTeamName.setLocation(Mutiworms.this.getWidth()/2 - blueTeamName.getWidth()/2, 50);
                redHealthBar.setLocation((Mutiworms.this.getWidth()/4) - (redHealthBar.getWidth()/2), 150);
                blueHealthBar.setLocation((Mutiworms.this.getWidth()/4) + (Mutiworms.this.getWidth()/2) - (blueHealthBar.getWidth()/2), 150);
                groundLevel = Mutiworms.this.getHeight() - (Mutiworms.this.getHeight()*.1);
                scaleFactor = ground.getWidth();
                remove(ground);
                ground = new GRect(0, groundLevel, Mutiworms.this.getWidth(), Mutiworms.this.getHeight() - groundLevel);
                groundLevel = groundLevel - playerHeight;
                ground.setColor(Color.black);
                ground.setFilled(true);
                scaleFactor = ground.getWidth()/scaleFactor;
                if (scaleFactor != 1) {
                    redHuman.setLocation(redHuman.getX() * scaleFactor, groundLevel);
                    blueHuman.setLocation(blueHuman.getX() * scaleFactor, groundLevel);
                    if (powerup != null) {
                        powerup.setLocation(powerup.getX() * scaleFactor, groundLevel);
                    }
                    playerHeight = playerHeight*scaleFactor;
                    redHuman.setSize(playerHeight, playerHeight);
                    blueHuman.setSize(playerHeight, playerHeight);
                    if (powerup != null) {
                        powerup.setSize(playerHeight, playerHeight);
                    }
                    if ((redHuman.getX() + playerHeight) > Mutiworms.this.getWidth()) {
                        redHuman.setLocation((Mutiworms.this.getWidth()-playerHeight), groundLevel);
                    } else if ((blueHuman.getX() + playerHeight) > Mutiworms.this.getWidth()) {
                        blueHuman.setLocation((Mutiworms.this.getWidth()-playerHeight), groundLevel);
                    } else if (powerup != null) {
                        if ((powerup.getX() + playerHeight) > Mutiworms.this.getWidth()) {
                            powerup.setLocation((Mutiworms.this.getWidth() - playerHeight), groundLevel);
                        }
                    }
                    //Scales the velocity multiplyer based on the size of the screen, since a larger screen has more pixels, meaning larger velocity
                    yVelocityFactor = Math.pow(ogHeight/Mutiworms.this.getHeight(), .44);
                    xVelocityFactor = Math.pow(ogWidth/Mutiworms.this.getWidth(), .44);
                }
                if (redHuman.getY() != groundLevel && blueHuman.getY() != groundLevel)
                {
                    redHuman.setLocation(redHuman.getX(), groundLevel);
                    blueHuman.setLocation(blueHuman.getX(), groundLevel);
                }
                if (powerup != null) {
                    if (powerup.getY() != groundLevel) {
                        powerup.setLocation(powerup.getX(), groundLevel);
                    }
                }
                add(ground);
                rightBound = Mutiworms.this.getWidth()-playerHeight;

            }

        });
        resizeTimer.start();
    }

    public void run()
    {
        //Some type of title screen will go here
        beginGame();
    }

    public void beginGame() {
        ogHeight = this.getHeight();
        ogWidth = this.getWidth();
        playerHeight = this.getHeight()*.04;
        redTeamName = new GLabel("Red Team's Turn");
        blueTeamName = new GLabel("Blue Team's Turn");
        redHealthBar = new GLabel("Health: " + 100);
        blueHealthBar = new GLabel("Health: " + 100);
        redHealthBar.setFont(new Font("Serif", Font.BOLD, 28));
        redHealthBar.setColor(Color.red);
        blueHealthBar.setFont(new Font("Serif", Font.BOLD, 28));
        blueHealthBar.setColor(Color.blue);
        redTeamName.setFont(new Font("Serif", Font.BOLD, 48));
        redTeamName.setColor(Color.red);
        blueTeamName.setFont(new Font("Serif", Font.BOLD, 48));
        blueTeamName.setColor(Color.blue);
        redTeamName.setLocation(this.getWidth()/2 - (redTeamName.getWidth()/2), 50);
        blueTeamName.setLocation(this.getWidth()/2 - (blueTeamName.getWidth()/2), 50);
        redHealthBar.setLocation((this.getWidth()/4) - (redHealthBar.getWidth()/2), 150);
        blueHealthBar.setLocation((this.getWidth()/4) + (this.getWidth()/2) - (blueHealthBar.getWidth()/2), 150);
        add(redHealthBar);
        add(blueHealthBar);
        groundLevel = this.getHeight() - (this.getHeight()*.1);
        ground = new GRect(0, groundLevel, this.getWidth(), this.getHeight() - groundLevel);
        groundLevel = groundLevel - playerHeight;
        ground.setColor(Color.black);
        ground.setFilled(true);
        add(ground);
        rightBound = this.getWidth()-playerHeight;
        refresh();


        createRed(playerHeight); createBlue(playerHeight);
       MouseListener testDrag = new DragListener();
       addMouseListeners(testDrag);
       if (Math.random() > .5) {
           redTurn = true;
           redHuman.sendToFront();
       } else {
           redTurn = false;
           blueHuman.sendToFront();
       }
       displayTeam();
   }

   private void createBlue(double playerHeight) {
       blueHuman = new Person(false, playerHeight);
       blueHuman.setColor(Color.blue);
       blueHuman.setFillColor(Color.blue);
       add(blueHuman);
   }

   private void createRed(double playerHeight) {
       redHuman = new Person(true, playerHeight);
       redHuman.setColor(Color.red);
       redHuman.setFillColor(Color.red);
       add(redHuman);
   }

   public void switchTeam() {
       redHealthBar.setLabel("Health: " + redHuman.health);
       blueHealthBar.setLabel("Health: " + blueHuman.health);
       if (redTurn) {
           redTurn = false;
           blueHuman.sendToFront();

       } else {
           redTurn = true;
           redHuman.sendToFront();
       }
       if (powerup == null) {
           powerup = new Treasure();
       }
       displayTeam();
   }

   public void displayTeam() {
       if (redTurn) {
           remove(blueTeamName);
           add(redTeamName);
       } else {
           remove(redTeamName);
           add(blueTeamName);
       }
   }
   class Treasure extends GRect
   {
      public Treasure()
      {
         super(playerHeight, playerHeight);
          this.setColor(Color.black);
          this.setFilled(true);
         double xpos = (Math.random() * (Mutiworms.this.getWidth()-50) + 1);
         move(xpos,groundLevel);
         add(this);
         sendToBack();
      }
      public void buff(Person buffed)
      {
         /*int buffDecide = (int)(Math.random()*3) + 1;
         if(buffDecide == 1)
         {
            buffed.changeHP(20);
         }
         if(buffDecide == 1)
         {
            buffed.changeHP(20);
         }
         if(buffDecide == 1)
         {
            buffed.changeHP(20);
         }*/
         buffed.changeHP(20);
         System.out.println("B");
         remove(this);
          powerup = null;
      }
      public String toString()
      {
         return "Treasure";
      }  
   }
   class Person extends GRect implements Shootable
   {
      private int health = 100;
      private boolean isRed; //either red or blue
      private ArrayList<Shootable> inventory;
      
      public Person(boolean redStatus, double playerHeight)
      {
         super(playerHeight,playerHeight);
         double xpos = (Math.random() * (Mutiworms.this.getWidth()-playerHeight) + 1);
         move(xpos, groundLevel);
         isRed = redStatus;
      }

      //Returns true is object belongs to red team, false if not
      public boolean getTeam() {
        return isRed;
      }
      
      public void changeHP(int deltaHP)
      {
         health = health + deltaHP;
      }
      public void shootThis(int xMag, int yMag) {

          Timer timer = new Timer(5, new ActionListener() {

              //Just enough to go across the map with the largest drag
              double yVel = yMag*yVelocityFactor*.24;
              double xVel = xMag*xVelocityFactor*.24;
              @Override
              public void actionPerformed(ActionEvent e) {
                  double xDist = xVel/5;
                  double yDist = yVel/-5;
                  yVel = yVel - (int)(yAccel/10);
                  move(xDist, yDist);
                  repaint();
                  if (powerup != null) {
                      if ((Person.this.getY() > powerup.getY() && Person.this.getY() < (powerup.getY() + playerHeight)) || (Person.this.getY() + playerHeight) > powerup.getY() && Person.this.getY() < (powerup.getY() + playerHeight)) {
                          if ((Person.this.getX() > powerup.getX() && Person.this.getX() < (powerup.getX() + playerHeight)) || (Person.this.getX() + playerHeight) > powerup.getX() && Person.this.getX() < (powerup.getX() + playerHeight))
                          powerup.buff(Person.this);
                      }
                  }

                  if (getX() <= 0 || getX() >= rightBound) {
                      if (getX() < 0) {
                          setLocation(0, getY());
                      } else if (getX() > rightBound) {
                          setLocation(rightBound, getY());
                      }
                      xVel = 0;
                  }
                  if (getY() > groundLevel) {
                      setLocation(getX(), groundLevel);
                      ((Timer)e.getSource()).stop();
                      (Person.this).setFilled(false);
                      switchTeam();
                  }
                  //if((getElementAt((double) getX(), (double) getY())).getClass() == Treasure.class)
                  //{
                  
              }
          });
          timer.start();

      }


   }

   class DragListener implements MouseListener, MouseMotionListener
   {
      private int finalX;
      private int finalY;
      private int initX;
      private int initY;
      private int xMag;
      private int yMag;
      private boolean tracking = false;
      private boolean exists = false;
      private GLine aimer;
      private Shootable selected;
      public void mousePressed(MouseEvent event) 
      {
          if(event.getButton() == 1)
         {
            tracking = true;
            initX = event.getX();
            initY = event.getY();
         }
      } //get selection if right click 
      public void mouseReleased(MouseEvent event) 
      {
         if(event.getButton() == 1)
         {
            tracking = false;
            exists = false;
            if ( aimer!= null)
            {
               remove(aimer);
            }
            finalX = event.getX();
            finalY = event.getY();
            //Java doesn't use traditional coordinate system, invert the y magnitude to get actual displacement
            //However, we want the opposite of the magnitudes for launch speeds (act like a slingshot, so init - final)
            xMag = initX - finalX;
            yMag = -1 * (initY - finalY);
            try {
                selected.shootThis(xMag, yMag);
                selected = null;
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null ,"No object was selected");
            }
         }

      } //gets final x and y pos of mouse and calls shoot method
      public void mouseClicked(MouseEvent event) {
          if (event.getButton() == 3) //r click
          {
              if (selected != null) {
                  ((GRect) (selected)).setFilled(false);
                  selected = null;
              }
              //Checks whether the object belongs to the team currently shooting/moving
              try {
                  if (((Shootable) (getElementAt((double) event.getX(), (double) event.getY()))).getTeam() == redTurn && ((Shootable) (getElementAt((double) event.getX(), (double) event.getY()))) == selected) {
                      System.out.println("s");   
                  }
                  else if (((Shootable) (getElementAt((double) event.getX(), (double) event.getY()))).getTeam() == redTurn) {
                      selected = (Shootable) (getElementAt((double) event.getX(), (double) event.getY()));
                      ((GRect) (selected)).setFilled(true);
                  } else {
                      JOptionPane.showMessageDialog(null, "This object does not belong to your team!");
                  }
              } catch (Exception e) {}
          }
      }
      public void mouseEntered(MouseEvent event) {}
      public void mouseExited(MouseEvent event) {}
      public void mouseDragged(MouseEvent event) 
      {
         if(tracking && !exists)
         {
            try{
            aimer = new GLine(((GObject)(selected)).getX(), ((GObject)(selected)).getY(), ((GObject)(selected)).getX() + (0.24)*(xVelocityFactor)*( initX -(event.getX())), ((GObject)(selected)).getY() + (0.24)*(yVelocityFactor)*( initY - (event.getY())));
            add(aimer);
            exists = true;
            }
            catch(Exception error){}
         }
         /*Timer timer = new Timer(100, new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
              try{
                 removed = !removed;
                 remove(aimer);
               }
               catch(Exception ex){}
               //aimer = null;
               ((Timer)e.getSource()).stop();
              }
          });
          timer.start();*/
          if(exists)
          {
            aimer.setEndPoint(((GObject)(selected)).getX() + (0.24)*(xVelocityFactor)*( initX -(event.getX())), ((GObject)(selected)).getY() + (0.24)*(yVelocityFactor)*( initY - (event.getY())));
          }
      }
      public void mouseMoved(MouseEvent event) {}
   }
   public static void main(String[] args)
   {
      new Mutiworms().start(args);
   }
}


/*   class Person extends GRect implements Shootable
   {
      private double friction = 10;
      private int health = 100;
      private boolean isRed; //either red or blue
      private ArrayList<Shootable> inventory;
      private boolean hasTurn;
      
      public Person(boolean redStatus)
      {
         super(100,100);
         double xpos = (Math.random() * 500 + 1);
         move(xpos,400);
         isRed = redStatus;
      }
      
      public void shootThis(int xMag, int yMag) 
      {
          while(getY() >= 400)
          {
            double xDist = xMag/100;
            double yDist = yMag/100;
            System.out.println("x: " + getX() + " y: " + getY());
            yMag = yMag - (int)(yAccel/100);
            move(xDist,yDist);
          }
      }
   }
*/

interface Shootable
{
   double yAccel = 100; //gravity
    boolean isRed = true;
   void shootThis(int xMag, int yMag);
   boolean getTeam();
}
/*class Bomb implements Shootable
{

}*/

