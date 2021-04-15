
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ADMIN
 */
public class SnakeGame extends JFrame implements ActionListener, KeyListener{

    boolean loss=false;
    int BOM,dem=0;
    int maxXY=100;
    int m=20,n=35;
    int start=0;
    //Thiết lập màu nền
    Color backGround_cl[]={Color.gray,Color.LIGHT_GRAY,Color.darkGray,Color.green};
    //Tạo thao tác chuyển động cho rắ, theo chục tọa độ OXY, vd rắn đi qua phải thì tọa độ sẽ là {x,y+1}
    int convertX[]={-1,0,1,0};
    int convertY[]={0,1,0,-1};
    //Thiết lập tốc độ cho từng mức độ chơi
    int speed[]={700,550,400,250,100,60,40,20,10,5};
    //Tạo các thành phần của giao diện GUI
    private JButton bt[][] = new JButton[maxXY][maxXY];
    private JComboBox lv= new JComboBox();
    //mảng phân biệt thức ăn, khoảng trống, đầu rắn, thân rắn
    private int a[][]= new int[maxXY][maxXY];
    //Chiều dài của con rắn
    private int xSnake[]=new int[maxXY*maxXY];
    private int ySnake[]=new int[maxXY*maxXY];
    private int xFood,yFood;
    private int sizeSnake=0;
    private int direction=2;
    private JButton btnewGame,btScore;
    private JPanel pn,pn2;
    Container cn;
    Timer timer;

    public SnakeGame(String s,int k) {
        super(s);
        cn=init(k);
        timer= new Timer(speed[k],new ActionListener(){
            public void actionPerformed(ActionEvent e){
                runSnake(direction);
            }
        });
    }


    
    public Container init(int k){
        Container cn= this.getContentPane();
        pn=new JPanel();
        pn.setLayout(new GridLayout(m,n));
        for(int i=0;i<m;i++)
            for(int j=0;j<n;j++){
                bt[i][j] = new JButton();
                pn.add(bt[i][j]);
                bt[i][j].setActionCommand(i+" "+j);
                bt[i][j].addActionListener(this);
                bt[i][j].addKeyListener(this);
                bt[i][j].setBorder(null);
                a[i][j]=0;
            }
        
        pn2=new JPanel();
        pn2.setLayout(new FlowLayout());
        
        btnewGame=new JButton("New Game");
        btnewGame.addActionListener(this);
        btnewGame.addKeyListener(this);
        btnewGame.setFont(new Font("UTM MIcra",1,15));
        btnewGame.setBackground(Color.white);
        
        btScore=new JButton("3");
        btScore.addActionListener(this);
        btScore.addKeyListener(this);
        btScore.setFont(new Font("UTM Micra",1,15));
        btScore.setBackground(Color.white);
        
        for (int i = 1; i < speed.length; i++) {
            lv.addItem("Mức độ " + i);
        }
        lv.setSelectedIndex(k);
        lv.addKeyListener(this);
        lv.setFont(new Font("UTM Micra",1,15));
        lv.setBackground(Color.white);
        
        pn2.add(btnewGame);
        pn2.add(lv);
        pn2.add(btScore);
        
        a[m/2][n/2-1]=1;
        a[m/2][n/2]=1;
        a[m/2][n/2+1]=2;
        xSnake[0]=m/2;
        ySnake[0]=n/2-1;
        xSnake[1]=m/2;
        ySnake[1]=n/2;
        xSnake[2]=m/2;
        ySnake[2]=n/2+1;
        sizeSnake=3;
        
        creatFood();
        updateColor();
        cn.add(pn);
        cn.add(pn2,"South");
        this.setVisible(true);
        this.setSize(n*30,m*30);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        return cn;
    }
    
    public void updateColor(){
        for(int i=0;i<m;i++)
            for(int j=0;j<n;j++)
                bt[i][j].setBackground(backGround_cl[a[i][j]]);
    }
    
    public void runSnake(int k){
        //phần đầu rắn
        a[xSnake[sizeSnake-1]][ySnake[sizeSnake-1]]=1;
        xSnake[sizeSnake]=xSnake[sizeSnake-1]+convertX[k-1];
        ySnake[sizeSnake]=ySnake[sizeSnake-1]+convertY[k-1];
        
        //Đi xuyên tường
        if(xSnake[sizeSnake]<0)
            xSnake[sizeSnake]=m-1;
        if(xSnake[sizeSnake]==m)
            xSnake[sizeSnake]=0;
        if(ySnake[sizeSnake]<0)
            ySnake[sizeSnake]=n-1;
        if(ySnake[sizeSnake]==n)
            ySnake[sizeSnake]=0;
        //Cắn đuôi, kết thúc game
        if(a[xSnake[sizeSnake]][ySnake[sizeSnake]]==1){
            timer.stop();
            JOptionPane.showMessageDialog(null," Ngu ");
            loss=true;
            return;
        }
        a[xSnake[start]][ySnake[start]]=0;
        if(xFood==xSnake[sizeSnake]&&yFood==ySnake[sizeSnake]){
            a[xSnake[start]][ySnake[start]]=1;
            start--;
            creatFood();
            btScore.setText(String.valueOf(Integer.parseInt(btScore.getText())+1));
        }
        a[xSnake[sizeSnake]][ySnake[sizeSnake]]=2;
        start++;
        sizeSnake++;
        updateColor();
        for (int i = start; i < sizeSnake; i++) {
           xSnake[i-start]=xSnake[i];
           ySnake[i-start]=ySnake[i];
           
        }
        sizeSnake-=start;
        start=0;
    }
    
    public void creatFood(){
        int k=0;
        for(int i=0;i<m;i++)
            for(int j=0;j<n;j++)
                if(a[i][j]==0)
                    k++;
        int h=(int)((k-1)*Math.random()+1);
        k=0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if(a[i][j]==0){
                    k++;
                    if(k==h){
                        xFood=i;
                        yFood=j;
                        a[i][j]=3;
                        return;
                       }
                }
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
       if(!loss){
           if(e.getKeyCode()==e.VK_UP&&direction!=3){
               direction=1;
               timer.start();
           }
           if(e.getKeyCode()==e.VK_RIGHT&&direction!=4){
               direction=2;
               timer.start();
           }
           if(e.getKeyCode()==e.VK_LEFT&&direction!=2){
               direction=4;
               timer.start();
           }
           if(e.getKeyCode()==e.VK_DOWN&&direction!=1){
               direction=3;
               timer.start();
           }
       }
    }   

    @Override
    public void keyReleased(KeyEvent e) {
       
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()==btnewGame.getText()){
            new SnakeGame("Game Ran San Moi",lv.getSelectedIndex());
            this.dispose();
        }
    }
    
    public static void main(String[] args) {
        new SnakeGame("Game Ran San Moi",4);
    }
    
}
