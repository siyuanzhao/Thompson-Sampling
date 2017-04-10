package org.assistments.peerassist.policies;

import java.util.Random;

import org.assistments.peerassist.policies.actionchooser.ActionChooser;

public abstract class Policy implements ActionChooser
{
	private Random random;
	
	//not sure if this belongs here
	//however it is needed for the design to work for logging this id
	private int peerExplanationId;

	public Policy()
	{
		random = new Random(7);
	}
	
	public Policy(Random random)
	{
		this.random = random;
	}
	
	public Random getRandom()
	{
		return random;
	}

	public void setRandom(Random random)
	{
		this.random = random;
	}
	

	public int getPeerExplanationId()
	{
		return peerExplanationId;
	}

	public void setPeerExplanationId(int peerExplanationId)
	{
		this.peerExplanationId = peerExplanationId;
	}

	//expected to be called in the following order
	public abstract void retrieveData();
	public abstract void preprocess();
	public abstract Action chooseAction();
	public abstract void logData();
	
}
