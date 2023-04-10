/**
 * This class is to generate the failures based on Possion process (i.e., the failure interval follows exponential distribution with a failure rate as lambda)
 */

import java.util.Random;

public class ExponentialRandomNumberGenerator {
    
	Random random = null;
	public ExponentialRandomNumberGenerator(int seed)
	{
		random = new Random(seed);
	}
	
    public double generateRandomNumber(double lambda) {
        
        double u = random.nextDouble();
        return -Math.log(1 - u) / lambda;
    }
    
    public static void main(String[] args) {
    	ExponentialRandomNumberGenerator erg = new ExponentialRandomNumberGenerator(2);
        double lambda = 1.0/30; // set the lambda parameter
        for (int i = 0; i < 50; i++) {
            double randomNumber = erg.generateRandomNumber(lambda);
            System.out.println(randomNumber);
        }
    }
}