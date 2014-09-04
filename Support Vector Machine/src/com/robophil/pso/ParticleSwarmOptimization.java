package com.robophil.pso;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import libsvm.LibSVM;
import libsvm.svm_parameter;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.InstanceTools;
import net.sf.javaml.tools.data.FileHandler;

import com.robophil.particle.SwarmParticle;

public class ParticleSwarmOptimization { 
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> initialize the constant values :weight(W), _P_ and _G_ >>>>>>>>>>>>>>>>>
	private final double W = 0.729;
	
	private final double _P_ = 1.49445;
	
	private final double _G_ = 1.49445;
	
	private final double maxV = Math.pow(2, 0.25);
	
	private final double minV = Math.pow(2, -0.25);
	
	private final double maxX = -1.0381852225093116E9+2e-2;
	
	private final double minX = -1.0381852225093116E9+2e-2;
	
	private final int dim = 2;
	
	//Create an object to hold the most optimized solution of the entire swarm
	private SwarmParticle g_Best;
	
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> create a swarm with desired population >>>>>>>>>>>>>>>>>>>>>
	private SwarmParticle[] swarm;
	
	
	//>>>>>>>>>>>>>>>>>> Create Data items to be used to train the SVM and checking fitness <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	private final Dataset data;
	
	private final svm_parameter param;
	
	private final LibSVM trainer;
	
	private CrossValidation crossvalidation;
	

	/**
	 * performs the PSO algorithm
	 * @param size
	 * @param no_of_iteration
	 */
	public ParticleSwarmOptimization(int size, int no_of_iteration) {
		
		swarm = new SwarmParticle[size];		//set swarm size
		
//		data = new DefaultDataset();			//create a default_data_set instance of DATASET
		
		param = new svm_parameter();			//used for setting the parameters to be used in training the data
		
		trainer = new LibSVM();					//an instance of LibSvm 
		
//		generate_Random_Data();					//generate random data for training SVM
		
		data = loadData();						//load data from a file
			
		Initialise_Swarm();						//initialize members of swarm with values
		
		
		Start_Iteration(no_of_iteration);		//start the iterations for the specified no of times
	}

	
	/**
	 * Starts up the PSO algorithm
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ParticleSwarmOptimization(30, 200);
	}
	
	/**
	 * initialize all particles found in the swarm
	 */
	private void Initialise_Swarm(){
		
		//////////					loop through all the particles and initialize their position and velocity	/////////////
		for (int i = 0; i < swarm.length; i++) {
			
			double[] X_postion = new double[dim];		//for holding their positions 
			
			double[] V_velocity = new double[dim];		//for holding their velocity
			
			//generate random position X to initialize particle
			for (int j = 0; j < dim; j++) {
				
				X_postion[j] = minX + (Math.random() * (maxX - minX));	//get a random position to start with
				
			}//end loop
			
			
			//generate random velocity V to initialize particle
			for (int j = 0; j < dim; j++) {
				
				V_velocity[j] = minV + (Math.random() * (maxV - minV));	//get a random velocity to start with
				
			}//end loop
			
			
			//initialize particle's velocity and position
			swarm[i] = new SwarmParticle(V_velocity, X_postion);
			
			//evaluate  fitness
			set_and_evaluate_fitness(swarm[i]);
			
			//initialize best fitness with current fitness value
			swarm[i].setBestfitness(swarm[i].getFitness());
			
			//initialize g-best to the first particle, update when a better one is met
			if(i == 0){
				g_Best = new SwarmParticle(swarm[0].getVelocity(), swarm[0].getPosition());
				
				g_Best.setFitness(swarm[0].getFitness());
				g_Best.setBestfitness(g_Best.getFitness());
				
			}else {
				//if a better position is found, update the value of g_best
				if(swarm[i].getPercentage_bestfitness() > g_Best.getPercentage_bestfitness()){
					g_Best = new SwarmParticle(swarm[i].getVelocity(), swarm[i].getPosition());
					
					g_Best.setFitness(swarm[i].getFitness());
					g_Best.setBestfitness(g_Best.getFitness());
				}
				
			}
			
		}
		System.out.println("\n>>>> All particles initialized <<<<<<\nG-best ===> "+g_Best.getPercentage_bestfitness()+"% "
				+ "{"+g_Best.getPosition()[0]+" , "+g_Best.getPosition()[1]+"}\n");
		
		System.out.println("\n\nStart PSO Algorithm..................................\n");
		
	}
	
	/**
	 * evaluate the fitness value of the current parameters you have
	 */
	private void set_and_evaluate_fitness(SwarmParticle particle){
		
		double c = particle.getPosition()[0];
		
		double gamma = particle.getPosition()[1];
		
		//train SVM with 'C' and 'GAMMA' values
		double[] fitness = train_SVM(c, gamma);
		
		//set its fitness
		particle.setFitness(fitness);
		
	}
	
	/**
	 * train the SVM with the following parameters
	 * @param c
	 * @param gamma
	 * @return
	 */
	private double[] train_SVM(double c, double gamma){
		
		param.C = c;
		param.gamma = gamma;
		param.cache_size = 100;
		param.kernel_type=svm_parameter.RBF;
		
		trainer.setParameters(param);
		//trainer.buildClassifier(data);
		
		crossvalidation = new CrossValidation(trainer);
		Map<Object, PerformanceMeasure> info = crossvalidation.crossValidation(data, 3);
		
		double a = info.get("Iris-setosa").getAccuracy();
		double b = info.get("Iris-virginica").getAccuracy();
		
		double[] val=new double[2];
		val[0] = a;
		val[1] = b;
		//System.out.println(info);
		System.out.printf("C ==> %.5f  Gamma ==> %.5f \t|",c,gamma);
		
		return val;
	}
	
	/**
	 * generate data to be used to train SVM
	 */
	private void generate_Random_Data(){
		//create data and add to data_set
        for (int i = 0; i < 16; i++) {
            Instance tmpInstance = InstanceTools.randomInstance(4);
            if(i<5)
            	tmpInstance.setClassValue("type_1");
            else
            	tmpInstance.setClassValue("type_2");
            
            data.add(tmpInstance);
        }
	}

	/**
	 * load data from a file into a data-set
	 * @return
	 */
	private Dataset loadData(){
		Dataset data, dataset = null;
		try {
			 dataset =  FileHandler.loadDataset(new File("CSCtest.data"), 4, ",");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data = dataset.copy();
		return data;
	}
	
	
	/**
	 * start to move the particles around the sample space for the specified no of times
	 * @param no_of_iteration
	 */
	private void Start_Iteration(int no_of_iteration){
		
		int i = 0;
		
		while (i < no_of_iteration) {
			
			for (int j = 0; j < swarm.length; j++) {
				
				//pick random values for r_p and r_g
				double r_p = Math.random();
				double r_g = Math.random();
				double[] velocity = new double[2];
				double[] position = new double[2];
				
				//calculation for the velocity
				for (int k = 0; k < dim; k++) {
					
					double v = swarm[j].getVelocity()[k];
					double x = swarm[j].getPosition()[k];
					double bp = swarm[j].getBestPosition()[k];
					double gbp = g_Best.getBestPosition()[k];
					
					v = (W*v) + ((_P_*r_p)*(bp - x)) + ((_G_*r_g)*(gbp - x));
					velocity[k]  = Math.max(Math.min(v, maxV), minV);
					
				}
				
				//update velocity of this particle
				swarm[j].setVelocity(velocity);
				
				//calculation for the position
				for (int k = 0; k < dim; k++) {
					
					double v = swarm[j].getVelocity()[k];
					double x = swarm[j].getPosition()[k];
					
					x = x + v;
					position[k] = Math.max(Math.min(x, maxX), minX);
				}
				
				//update position of this particle
				swarm[j].setPosition(position);
				
				//set and evaluate fitness
				set_and_evaluate_fitness(swarm[j]);
				
				double xxx = swarm[j].getPercentage_fitness();
				double bpp = swarm[j].getPercentage_bestfitness();
				double gbx = g_Best.getPercentage_bestfitness();
				
				String a= " | Particle best updated",
						b=" | G-best updated",str="";
				if(xxx > bpp){
					swarm[j].setBestPosition(swarm[j].getPosition());
					swarm[j].setBestfitness(swarm[j].getFitness());
					bpp = swarm[j].getPercentage_bestfitness();
					str+=a;
					//System.out.print("Particle best updated\t |");
					
					if(bpp > gbx){
						g_Best = new SwarmParticle(swarm[j].getVelocity(), swarm[j].getPosition());
						
						g_Best.setFitness(swarm[j].getFitness());
						g_Best.setBestfitness(g_Best.getFitness());
						str+=b;
						//System.out.print("Gbest updated\t |");
					}
				}
				System.out.println("current fitness===> "
				+swarm[j].getPercentage_fitness()+"%"+" | Best =>"+swarm[j].getPercentage_bestfitness()+"%"+str);
			}
			
			System.out.println("G-best ===> "+g_Best.getPercentage_bestfitness()+"% "
					+ "{"+g_Best.getPosition()[0]+" , "+g_Best.getPosition()[1]+"}\n");
			
			i++;
			
		}
		
		System.out.println("OPTIMAL PERCENTAGE ===> "+g_Best.getPercentage_bestfitness()+"%,"
				+ " C="+g_Best.getPosition()[0]+" Gamma ==>"+g_Best.getPosition()[1]);
		
	}
	
}
