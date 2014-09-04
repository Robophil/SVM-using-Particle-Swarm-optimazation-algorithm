package com.robophil.particle;

public class SwarmParticle {
	
	/**
	 * holds the velocities of this particle (2 Dimension) i.e GAMMA(0) and NU(1)
	 */
	private double[] velocity;
	/**
	 * holds the current positions of this particle (2 Dimension) i.e GAMMA(0) and NU(1)
	 */
	private double[] position;
	/**
	 * holds the best position of this particle (2 Dimension) i.e GAMMA(0) and NU(1)
	 */
	private double[] bestPosition;
	/**
	 * holds the current fitness of this particle
	 */
	private double[] fitness;
	/**
	 * holds the best fitness of this particle
	 */
	private double[] bestfitness;
	/**
	 * holds the current fitness of this particle
	 */
	private double Percentage_fitness;
	/**
	 * holds the best fitness of this particle
	 */
	private double Percentage_bestfitness;
	
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> CONSTRUCTOR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	/**
	 * this is a 'particle' object with member properties
	 * initial parameters 'velocity and position'
	 */
	public SwarmParticle(double[] velocity, double[] position) {
		
		setVelocity(velocity);			//initialize velocity
		setPosition(position);			//initialize position
		setBestPosition(position);		//initialize the best_position to the current position
		
	}
	
	
	
	
	//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Getters and Setters of this particle >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	/**
	 * get the current velocity of this particle (2 Dimension)
	 * @return
	 */
	public double[] getVelocity() {
		return velocity;
	}

	/**
	 * set the current velocity of this particle (2 Dimension)
	 * @param velocity
	 */
	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}

	/**
	 * get the current position of this particle (2 Dimension)
	 * @return
	 */
	public double[] getPosition() {
		return position;
	}

	/**
	 * set the current position of this particle (2 Dimension)
	 * @param position
	 */
	public void setPosition(double[] position) {
		this.position = position;
	}

	/**
	 * get the best position found for this particle (2 Dimension)
	 * @param position
	 */
	public double[] getBestPosition() {
		return bestPosition;
	}

	/**
	 * set the best position found for this particle (2 Dimension)
	 * @param position
	 */
	public void setBestPosition(double[] bestPosition) {
		this.bestPosition = bestPosition;
	}


	/**
	 * get the current fitness of this particle (2 Dimension)
	 * @param position
	 */
	public double[] getFitness() {
		return fitness;
	}

	
	/**
	 * set the current fitness of this particle (2 Dimension)
	 * and its fitness percentage
	 * @param position
	 */
	public void setFitness(double[] fitness) {
		this.fitness = fitness;
		setfitness_Percentage(getPercentage(fitness));
	}

	
	/**
	 * get the best fitness found for this particle (2 Dimension)
	 * @param position
	 */
	public double[] getBestfitness() {
		return bestfitness;
	}


	/**
	 * set the best fitness found for this particle (2 Dimension)
	 * and the best fitness in percentage
	 * @param position
	 */
	public void setBestfitness(double[] bestfitness) {
		this.bestfitness = bestfitness;
		setbestfitness_Percentage(getPercentage(bestfitness));
	}
	
	
	/**
	 * return as a percentage, the current fitness value
	 * @return
	 */
	public double getPercentage_fitness() {
		return Percentage_fitness;
	}


	/**
	 * set the current percentage fitness value
	 * @param percentage_fitness
	 */
	private void setfitness_Percentage(double percentage_fitness) {
		Percentage_fitness = percentage_fitness;
	}


	/**
	 * get the percentage best fitness value found so far
	 * @return
	 */
	public double getPercentage_bestfitness() {
		return Percentage_bestfitness;
	}


	/**
	 * set the best fitness value as a percentage
	 * @param percentage_bestfitness
	 */
	private void setbestfitness_Percentage(double percentage_bestfitness) {
		Percentage_bestfitness = percentage_bestfitness;
	}
	
	
	/**
	 * returns the fitness value for both in the form of percentage
	 * @param fitness
	 * @return
	 */
	private double getPercentage(double[] fitness){
		
		double a = fitness[0];
		double b = fitness[1];
		
		double percentage = ((a + b)/2) * 100;
		
		return percentage;
	}
}
