import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Pacman2 extends JApplet implements KeyListener
{
   private DrawingArea canvas;
   private ButtonPanel buttonPanel;
   private int xpos, ypos, cheeseEaten;
   private int[] cheeseX;
   private int[] cheeseY;
   private int[] mX;
   private int[] mY;//Arrays of positions
   private boolean dead;
   private int pacmanDirection;
   private boolean mouth;
   private int offset;
   private int RANDOM = 0;
   private int STILL = 11;
   private int UP = 4;
   private int DOWN = 10;
   private int LEFT = 7;
   private int RIGHT = 1;
   private int CHEESE_NUM = 10;
   private int MONSTERS_NUM = 6;
   private int FADE_SPEED = 10;
   private boolean god = false;
   private boolean godd = false;
   private Timer t;
   private ActionListener timeListener;
   //setting values here is easier

   public Pacman2()
   {
       xpos = cheeseEaten = ypos = 0;
       dead = false;
       mX = new int [MONSTERS_NUM];
       mY = new int [MONSTERS_NUM];
       cheeseX = new int [CHEESE_NUM];
       cheeseY = new int [CHEESE_NUM];
       setValues();
       t = new Timer(1000, timerListener);
       t.start();
   }
   public void init()
   {
       canvas = new DrawingArea();
       getContentPane().add (canvas, BorderLayout.CENTER);
       buttonPanel = new ButtonPanel();
       getContentPane().add (buttonPanel, BorderLayout.SOUTH);
       canvas.setBackground(Color.black);
       canvas.addKeyListener(this);
       canvas.requestFocus();
   }

    ActionListener timerListener = new ActionListener() 
    {
        public void actionPerformed(ActionEvent evt) 
        {
          for(int i = 0;i < MONSTERS_NUM; i++)
          {
              int x, y;
              do 
              {
                  x = mX[i];
                  y = mY[i];
                  x += random();
                  y += random();
              } 
              while(check(x, y) || x > 10 || x < 0 || y > 10 || y < 0);
          mX[i] = x;
          mY[i] = y;
          }

        canvas.repaint();
        }
    };

   private void setValues()
   {
      for(int i = 0;i < CHEESE_NUM; i++)
      {
          cheeseX[i]  = (int)(Math.random()*10 + 1);
          cheeseY[i]  = (int)(Math.random()*10 + 1);
      }

      do {
          xpos = (int)(Math.random()*10 + 1);
          ypos = (int)(Math.random()*10 + 1);
      }  while(check(xpos, ypos));

      for(int i = 0;i < MONSTERS_NUM; i++)
      {
          do {
              mX[i] = (int)(Math.random()*10 + 1);
              mY[i] = (int)(Math.random()*10 + 1);
          } while(check(mX[i], mY[i]) || (xpos == mX[i] && ypos== mY[i]));
      }
      cheeseEaten = 0;
      dead = false;
      pacmanDirection = RIGHT;
      mouth = true;
      offset = 0;
   }

   private void moveMonsters()
   {
     for(int i = 0;i < MONSTERS_NUM; i++)
     {
        int x, y;
       do 
       {
           x = mX[i];
           y = mY[i];
           x += random();
           y += random();
       } 
       while(check(x, y) || x > 10 || x < 0 || y > 10 || y < 0);
       mX[i] = x;
           mY[i] = y;
      }
   }
   private int random()
   {
      int move = (int)(Math.random() * 3);
      if(move == 2)
      {
          move = 1;
      }
      else if(move == 1)
      {
          move = -1;
      }
      return move;
   }
   private void action()
   {
      checkDead();
      if(dead) return;
      checkEaten();
      if(cheeseEaten == CHEESE_NUM) return;
      checkDead();
   }

   private boolean check(int x, int y)
   {
      for(int i = 0;i < CHEESE_NUM; i++)
      {
          if(x == cheeseX[i] && y == cheeseY[i])
          {
              return true;
          }
      }
      return false;
   }

   private void checkEaten()
   {
      for(int i = 0; i< CHEESE_NUM; i++)
      {
          if(xpos == cheeseX[i] && ypos == cheeseY[i])
          {
              cheeseX[i] = -1;
              cheeseY[i] = -1;
              cheeseEaten++;
          }
      }
   }

   private void checkDead()
   {
      for(int i = 0; i < MONSTERS_NUM; i++)
      {
          if(xpos == mX[i] && ypos == mY[i]&&god==false)
          {
              dead = true;
          }
      }
   }

   class DrawingArea extends JPanel
   {
      public void paintComponent(Graphics g)
      {
          super.paintComponent(g);
          drawGrid(g);
          g.setColor(Color.black);
          this.requestFocus();
      }

      public void drawGrid(Graphics g)
      {

          g.setColor(Color.black);
          g.setColor(Color.white);

          for(int row = 0; row < 11; row++)
          {
              for(int col = 0; col < 11; col++)
              {
                  g.fillRect(35 + col * 60, 35 + row * 60, 55, 55);
              }
          }
          g.setColor(Color.yellow);

          for(int i = 0; i < CHEESE_NUM; i++)
          {
              if(cheeseX[i] != -1)
              {
                  g.fillRect(40 + cheeseX[i] * 60, 40 + cheeseY[i] * 60, 45, 45);
              }
          }
          paintPacman(g);
          for(int i = 0; i < MONSTERS_NUM; i++)
          {
              g.setColor(Color.red);
              g.fillOval(35 + mX[i] * 60, 35 + mY[i] * 60, 54, 54);
              g.setColor(Color.black);
              // eyes
              g.fillRect(50 + mX[i] * 60, 50 + mY[i] * 60, 7, 5);
              g.fillRect(70 + mX[i] * 60, 50 + mY[i] * 60, 7, 5);
              // mouth
              g.fillRect(50 + mX[i] * 60, 70 + mY[i] * 60, 27, 3);

          }

          g.setColor(Color.yellow);

          Font myFont = new Font ("Comic Sans MS", Font.BOLD,45);
          g.setFont(myFont);

          if(cheeseEaten == CHEESE_NUM)
          {
              g.fill3DRect(40, 325, 640, 100, true);
              g.setColor(Color.black);
              g.drawString("           YOU WIN!", 45, 400);
          }
          else if(dead)
          {
              g.fill3DRect(40, 325, 640, 100, true);
              g.setColor(Color.black);
              g.drawString("   YOU LOST! Try Again!", 45, 400);
          }
       }
   }

   private void paintPacman(Graphics g)
   {
      g.setColor(Color.blue);
      g.fillArc(35 + xpos * 60, 35 + ypos * 60, 54, 54, pacmanDirection* 30 - offset, 300 + 2 * offset);
      if (offset == 25) mouth = false;
      if (offset == 0) mouth = true;
      if(mouth == false)
      {
          offset -= 5;
      }
      else
      {
          offset += 5;
      }
   }

   public void keyTyped(KeyEvent e) {}

   public void keyPressed(KeyEvent e)
   {
      int value = e.getKeyCode();
      if(value == KeyEvent.VK_DOWN)
      {
          movePacman(DOWN);
      }
      else if(value == KeyEvent.VK_UP)
      {
          movePacman(UP);
      }
      else if(value == KeyEvent.VK_RIGHT)
      {
          movePacman(RIGHT);
      }
      else if(value == KeyEvent.VK_LEFT)
      {
          movePacman(LEFT);
      }
      else if(value == KeyEvent.VK_SPACE)
              movePacman(RANDOM);
   }

  private void movePacman(int direction)
  {
      if(direction == RANDOM)
      {
          setValues();
          canvas.repaint();
          return;
      }

      if(dead == false && cheeseEaten < CHEESE_NUM)
      {
           if(direction == DOWN)
           {
                ypos++;
           }
           else if(direction == UP)
           {
                ypos--;
           }
           else if(direction == RIGHT)
           {
                xpos++;
           }
           else if(direction == LEFT)
           {
                xpos--;
           }

           pacmanDirection = direction;

           if(xpos == -1) xpos = 10;
           if(ypos == -1) ypos = 10;
           if(xpos == 11) xpos = 0;
           if(ypos == 11) ypos = 0;

           action();
           canvas.repaint();

       }
  }


   public void keyReleased(KeyEvent e) {}

  class ButtonPanel extends JPanel implements ActionListener
  {
      private JButton sud1, sud2, sud3, sud4, sud5, sud6, sud7;

      public ButtonPanel()
      {
            setBackground (Color.BLACK);
            sud1 = new JButton ("Left");
            sud1.addActionListener(this);
            this.add(sud1);
            sud2 = new JButton ("Right");
            sud2.addActionListener(this);
            this.add(sud2);
            sud3 = new JButton ("Up");
            sud3.addActionListener(this);
            this.add(sud3);
            sud4 = new JButton ("Down");
            sud4.addActionListener(this);
            this.add(sud4);
            sud5 = new JButton ("Reset");
            sud5.addActionListener(this);
            this.add(sud5);
            sud6 = new JButton ("God-Mode");
            sud6.addActionListener(this);
            this.add(sud6);
            sud7 = new JButton ("Freeze");
            sud7.addActionListener(this);
            this.add(sud7);

      }

      public void actionPerformed (ActionEvent evt)
      {
          String command = evt.getActionCommand();
          if (command.equals ("Left"))
          {
              movePacman(LEFT);
          }
          else if (command.equals ("Right"))
          {
              movePacman(RIGHT);
          }
          else if (command.equals ("Up"))
          {
              movePacman(UP);
          }
          else if (command.equals ("Down"))
          {
              movePacman(DOWN);
          }
          else if (command.equals ("Reset"))
          {
              movePacman(RANDOM);
          }
          else if (command.equals ("Freeze"))
          {
               movePacman(STILL);
          }
          else if (command.equals ("God-Mode"))
          {
               godd = false;
       if(god == true)
               god = false;
       else
               god = true;
      }

  }
}
}
