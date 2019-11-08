import java.lang.Runnable;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

static private final JFrame S_FRAME = new JFrame();
static private final JButton S_BUTTON_AD = new JButton("Add");
static private final JButton S_BUTTON_RM = new JButton("Remove");

static private final ActionListener SWING_LISTENER = new ActionListener(){
  @Override public void actionPerformed(ActionEvent ae){
    String command = ae.getActionCommand();
    switch(command){
      case "Add":invokeLater(ADDING_TIT);break;
      case "Remove":invokeLater(REMOVING_TIT);break;
      default:break;
    }
  }
};

static private final Runnable SWING_SETUP = new Runnable(){
  @Override public void run(){
    
    S_BUTTON_AD.addActionListener(SWING_LISTENER);
    S_BUTTON_RM.addActionListener(SWING_LISTENER);
    
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setPreferredSize(new Dimension(320,240));
    contentPanel.add(S_BUTTON_AD,BorderLayout.LINE_START);
    contentPanel.add(S_BUTTON_RM,BorderLayout.CENTER);
    
    S_FRAME.setTitle("SwingExample");
    S_FRAME.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    S_FRAME.getContentPane().add(contentPanel);
    S_FRAME.setResizable(false);
    S_FRAME.pack();
    S_FRAME.setLocation(100,100);
    S_FRAME.setVisible(true);
    
    String thread = Thread.currentThread().toString();
    System.out.println("::swing_setup_from:"+thread);
  }
};