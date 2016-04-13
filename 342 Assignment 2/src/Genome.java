import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
 * Shawn Massoud 
 * November 2, 2015
 * Assignment 2 -Evolve 
 */

public class Genome 
{
	private double mutationRate;
	private Random rand = new Random();
	private List<Character> strg;
	//list of all characters working with
	private Character[]char1 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', '-', '\''};
	private List<Character> chars = Arrays.asList(char1);
	
	//constructor for Genome
	public Genome(double mutationRate) 
	{
		this.mutationRate = mutationRate;
		strg = new ArrayList<Character>();
		strg.add('A');
	}
	
	public Genome(Genome gene) 
	{
		this.mutationRate = gene.mutationRate;
		this.strg = gene.strg;
	}
	
	public void mutate()
	{
		addCharacter();
		deleteCharacter();
		changeCharacter();	
	}
	
	public void addCharacter()
	{
		if(rand.nextDouble()< this.mutationRate)
		{
			strg.add(rand.nextInt(strg.size()), chars.get(rand.nextInt(chars.size()-1)));
		}
	}
	
	public void deleteCharacter()
	{
		if(strg.size() >= 2 && rand.nextDouble()< this.mutationRate){
			strg.remove(rand.nextInt(strg.size()-1));
		}
	}
	public void changeCharacter()
	{
		for(int i = 0; i< strg.size(); i++){
			if(rand.nextDouble()< this.mutationRate)
			{
				strg.set(rand.nextInt(strg.size()), chars.get(rand.nextInt(chars.size())));
			}
		}	
	}
	
	public void crossover(Genome other) 
	{
		List<Character> newResult = new ArrayList<Character>();
		int newIndex = 0;
		List<Character> list1 = this.strg;
		List<Character> list2 = other.strg;

		while(newIndex < list1.size() || newIndex < list2.size()) 
		{
			if(rand.nextDouble()< 0.5)
			{
				if(newIndex < list1.size())
				{
					newResult.add(list1.get(newIndex));
					newIndex++;
				} 
				else 
				{
					break;
				}
			}
			else
			{
				if(newIndex < list2.size())
				{
					newResult.add(list2.get(newIndex));
					newIndex++;
				}
				else 
				{
					break;
				}
			}
		}
		this.strg = newResult;
	}
	
	public Integer fitness() 
	{
		List<Character> target =  new ArrayList<Character>();
		List<Character> list1 = this.strg;
		for(char ch: Population.target.toCharArray())
		{
			target.add(ch);
		}
		int lengthPenality = Math.abs(target.size() -  list1.size());
		int charReward = 0;
		int minIndex = Math.min(target.size(), list1.size());	
		for(int i = 0; i <  minIndex; i++){
			if(list1.get(i).equals(target.get(i)))
			{
				charReward++;
			}
		}
		int charPenality = target.size()- charReward;
		int fit = lengthPenality + charPenality;
		return fit;
	}
	
	public String toString() 
	{
		List<Character> list1 = this.strg;
		String returnString = "(\"";
		for(Character c: list1)
		{
			returnString += c;
		}
		returnString += "\", " + fitness()+ ")";
		return  returnString;
	}
}
