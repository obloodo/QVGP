package plugins;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Core.*;
import Interfaces.*;

public class Gui extends JFrame  implements IGui, ActionListener{ 
  
  
	JButton bouton = new JButton("Fichier");
	JButton boutonGo = new JButton("Valider");
	private List<IQuestion> questions = new ArrayList<IQuestion>(); 
	JComboBox theme;
	int indiceQC;
	JPanel Menu = new JPanel();
	JPanel Points = new JPanel();
	JPanel Question = new JPanel();
	JPanel Reponses = new JPanel();
	JPanel BoutonGo = new JPanel();
	private JPanel container = new JPanel();
	JLabel points = new JLabel("Points : ");
	JLabel titre = new JLabel("Question : ");
	JPanel b6 = new JPanel();
	QuestionGUI qGui;
	Plateforme plateformeInstance;
	ICalculPoint calcPts;
	//producteur de question
 
  public Gui(String themeChoisi) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, IOException{    
    plateformeInstance = Plateforme.getInstance();
    IProducteurDeQuestion prod_question = (IProducteurDeQuestion) plateformeInstance.loadPluginDependencyFrom(this.getClass(), "IProducteurDeQuestion");
    
   questions=  prod_question.recupererQuestions(themeChoisi);
    calcPts= (ICalculPoint) plateformeInstance.loadPluginDependencyFrom(this.getClass(), "ICalculPoint");
   Object[] question = {questions.get(indiceQC),calcPts};
   qGui = (QuestionGUI) plateformeInstance.loadPluginDependencyWithParamsFrom(this.getClass(),"IGui",question,IQuestion.class,ICalculPoint.class); 
   indiceQC++;
    
    
    this.setTitle("Qui Veut Gagner Des Plugins");
    this.setSize(350, 260);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    //Ajout du bouton à notre content pane

    bouton.addActionListener(this);
    boutonGo.addActionListener(this);
  //  container.setBackground(Color.white);
   // container.add(pan, BorderLayout.CENTER);
    GridLayout experimentLayout = new GridLayout(0,2);
    
    

    //On définit le layout en lui indiquant qu'il travaillera en ligne
    Menu.setLayout(new BoxLayout(Menu, BoxLayout.LINE_AXIS));
    Menu.setAlignmentX( JPanel.LEFT_ALIGNMENT );
    Menu.setBorder(BorderFactory.createLineBorder(Color.black));
    Menu.add(bouton);
    

    Points.setLayout(new BoxLayout(Points, BoxLayout.PAGE_AXIS));
    Points.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    points.setText("Points : "+calcPts.getScore());
    Points.add(points);
    

    Question.setLayout(new BoxLayout(Question, BoxLayout.LINE_AXIS));
    Question.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    Question.add(qGui);
    

    Reponses.setLayout(experimentLayout);
    Reponses.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    Reponses.add(qGui.bouton1);
    Reponses.add(qGui.bouton2);
    Reponses.add(qGui.bouton3);
    Reponses.add(qGui.bouton4);
    
    
    

    BoutonGo.setLayout(new BoxLayout(BoutonGo, BoxLayout.LINE_AXIS));
    BoutonGo.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    BoutonGo.add(boutonGo);
    
    //On positionne maintenant ces trois lignes en colonne
    b6.setLayout(new BoxLayout(b6, BoxLayout.PAGE_AXIS));
   
    
    b6.add(Menu);
    b6.add(Points);
    b6.add(Question);
    b6.add(Reponses);
    b6.add(BoutonGo);

    points.setHorizontalAlignment(JLabel.CENTER);  
    this.getContentPane().add(b6);
    this.setVisible(true);
  
}
  
@Override
public void actionPerformed(ActionEvent arg0) {
  // TODO Auto-generated method stub
    if(arg0.getSource() == boutonGo){
    	qGui.verifierReponse();

    	try {
    		Thread.sleep(1000);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    	
    	if(indiceQC < questions.size()){
    		Object[] question = {questions.get(indiceQC),calcPts};
    		try {
    			qGui = (QuestionGUI) plateformeInstance.loadPluginDependencyWithParamsFrom(this.getClass(),"IGui",question,IQuestion.class,ICalculPoint.class);
    		} catch (ClassNotFoundException | InstantiationException
    				| IllegalAccessException | NoSuchMethodException
    				| SecurityException | IllegalArgumentException
    				| InvocationTargetException e) {
    					e.printStackTrace();
    		}
    		
    		updateQuestionUI();
			indiceQC++;
			
    	}else{
    		updateQuestionUI();
    		//Fin de partie
    	}
    }
}

public void updateQuestionUI(){
	b6.removeAll();
	Points.removeAll();
	points.setText("Points : "+calcPts.getScore());
	Points.add(points);
	Question.removeAll();
	Question.add(qGui);
	Reponses.removeAll();
	Reponses.add(qGui.bouton1);
	Reponses.add(qGui.bouton2);
	Reponses.add(qGui.bouton3);
	Reponses.add(qGui.bouton4);
	b6.add(Menu);
	b6.add(Points);
	b6.add(Question);
	b6.add(Reponses);
	b6.add(BoutonGo);
	b6.updateUI();
}

}       
