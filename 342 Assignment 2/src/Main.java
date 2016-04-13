/*
 * Shawn Massoud 
 * November 2, 2015
 * Assignment 2 -Evolve 
 */


public class Main 
{
	static Population pop = new Population(100, 0.05);
	public static void main(String[] args) 
		{
		int countGen = 0;
		final long startTime = System.currentTimeMillis();
		while(Population.mostFit.fitness() > 0){
			pop.day();
			System.out.println(Population.mostFit.toString());
			countGen++;
		}
		final long endTime = System.currentTimeMillis();
		System.out.println("GENERATIONS:" + countGen);
		System.out.println("Running Time:" + (endTime - startTime));
		System.out.println();
		System.out.println();	
		testGenome();
		System.out.println();
		System.out.println();
		testPopulation();
	}
	public static void testGenome()
	{
		Genome gen = new Genome(0.9);
		Genome other = new Genome(0.9);
		System.out.println("This method test the Genome class");
		System.out.println("The genome before any method:" +gen);
		gen.addCharacter();
		System.out.println("The Genome after add character:" + gen);
		gen.deleteCharacter();
		System.out.println("The Genome after delete characte:" + gen);
		gen.changeCharacter();
		System.out.println("The Genome after change character:" + gen);
		gen.crossover(other);
		System.out.println("The Genome after crossOver:" + gen);
		System.out.println("To test the fitness method: " + gen.fitness());

	}
	public static void testPopulation()
	{
		System.out.println("This method test the Population class");
		pop.day();
		System.out.println(Population.mostFit.toString());
	}
	
}
