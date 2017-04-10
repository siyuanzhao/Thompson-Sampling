package org.assistments.peerassist.miscellaneous;

import java.util.Random;

public class ConstantRNG
{
	private static final Random random = new Random(0);

	private ConstantRNG()
	{
		//
	}

	public static Random getRNG()
	{
		return random;
	}

	public static double getNextNumberRawU()
	{
		return random.nextDouble();
	}

	public static double getNextNumberRawG()
	{
		return random.nextGaussian();
	}

	public static int getNextInt()
	{
		return random.nextInt();
	}
	
	public static int getNextInt(int upperBound)
	{
		return random.nextInt(upperBound);
	}
	
	public static boolean getNextBoolean()
	{
		return random.nextBoolean();
	}

	public static int getNextInt(int lowerBound, int upperBound)
	{
		int adjustedUpperBound = (upperBound+1) - lowerBound;
		int randomNumber = random.nextInt(adjustedUpperBound);
		int adjustedNumber = randomNumber + lowerBound;

		return adjustedNumber;
	}


	/**
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	public static double getNextNumberUniform(double lowerBound, double upperBound)
	{
		double randomNumber = getNextNumberRawU();

		randomNumber = (lowerBound + randomNumber * (upperBound - lowerBound));

		// round to 2 decimal places
		randomNumber = (Math.floor(randomNumber * 100)) / 100;

		return randomNumber;
	}

	/**
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	/*
	public static double getNextNumberGuassian(double lowerBound, double upperBound)
	{
		double randomNumber = getNextNumberRawG();
		randomNumber = (lowerBound + randomNumber * (upperBound - lowerBound));
		randomNumber = (Math.floor(randomNumber * 100)) / 100;

		return randomNumber;
	}
	*/
}
