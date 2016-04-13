import java.util.Random;

/*
 * Shawn Massoud 
 * November 2, 2015
 * Assignment 2 -Evolve 
 */


public class Population 
{
	static String target = "CHRISTOPHER PAUL MARRIOTT";
	Integer numGenomes;static Genome mostFit;
	private Genome[]myGenos;
	private Random randome1 = new Random();

	public Population(Integer numGenomes, double mutationRate) 
	{
		myGenos = new Genome[numGenomes];
		if(myGenos.length!= 0)
			for(int i = 0; i < myGenos.length; i++)
			{
				myGenos[i] = new Genome(mutationRate);
			}
		mostFit = myGenos[0];
	}

	public static Genome[] mergeSort(Genome[] inputList) 
	{
		if (inputList.length <= 1) 
		{
			return inputList;
		}

		// Here we are going to split the array in half 
		Genome[] first = new Genome[inputList.length / 2];
		Genome[] second = new Genome[inputList.length - first.length];
		System.arraycopy(inputList, 0, first, 0, first.length);
		System.arraycopy(inputList, first.length, second, 0, second.length);

		// going to sort the first half and the second half 
		mergeSort(first);
		mergeSort(second);

		// after sorting using mergeSort going to merge the two into one list called inputList
		merge(first, second, inputList);
		return inputList;
	}
	// Going to merge both halves into the result array	
	private static void merge(Genome[] first, Genome[] second, Genome [] result) 
	{
		// Element to consider in the first array
		int i = 0;
		
		// Next element to consider in the second array
		int k = 0;

		// Next open position in the result
		int j = 0;
		/* 
		 * As long as neither i/j nor k is past the end, move the
		 *  smaller element into the result.
		 */
		while (i < first.length && k < second.length) 
		{
			if (first[i].fitness() < second[k].fitness()) 
			{
				result[j] = first[i];
				i++;
			} 
			else 
			{
				result[j] = second[k];
				k++;
			}
			j++;
		}
		// make a copy of whatever is left 
		System.arraycopy(first, i, result, j, first.length - i);
		System.arraycopy(second, k, result, j, second.length - k);
	}

	//CODE ANALYSIS 
	public void day()
	{
		// sort the genomes based on fitness of genome which is in genomes
		mergeSort(myGenos);
		// update the mostFit of population
		mostFit = myGenos[0];
		
		if(randome1.nextDouble() < 0.05) 
		{
			for(int i = myGenos.length/2; i < myGenos.length; i++) 
			{
				int rnd = new Random().nextInt(myGenos.length/2);
				myGenos[i]= new Genome(myGenos[rnd]);

				myGenos[i].mutate();
			}

		}
		if(randome1.nextDouble()>= 0.05) 
		{
			for(int i = myGenos.length/2; i < myGenos.length; i++) 
			{
				int rnd = new Random().nextInt(myGenos.length/2);
				int rnd1 = new Random().nextInt(myGenos.length/2);
				myGenos[i] = new Genome(myGenos[rnd]);
				myGenos[i].crossover(myGenos[rnd1]);
				myGenos[i].mutate();
			}
		}
	}
}
