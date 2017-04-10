package org.assistments.peerassist.policies.defaultpolicy;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.assistments.peerassist.miscellaneous.Pair;
import org.assistments.peerassist.policies.Action;
import org.assistments.peerassist.policies.Policy;
import org.assistments.peerassist.policies.RewardFunction;

//Must be abstract as to not have default variables that should not be inherited
public abstract class DefaultPolicy extends Policy
{
	/**
	 * usedActionList list of all possible actions that have been taken already
	 * unusedActionList list of all actions that have not been taken yet
	 * actionHistoryMap list of all action-reward pairs for all reward functions
	 * weightMap desired weighting for each reward function
	 * missingValueMap desired values to use when the reward is missing
	 */	
	private List<Action> usedActionList;
	private List<Action> unusedActionList;
	private Map<RewardFunction, List<Pair<Action, Double>>> actionHistoryMap;
	private Map<RewardFunction, Double> weightMap;
	private Map<RewardFunction, Double> missingValueMap;
	
	
	public DefaultPolicy()
	{
	
	}
	
	public DefaultPolicy(Random random)
	{
		super(random);
	}
	
	

	public List<Action> getUsedActionList()
	{
		return usedActionList;
	}

	public void setUsedActionList(List<Action> usedActionList)
	{
		this.usedActionList = usedActionList;
	}

	public List<Action> getUnusedActionList()
	{
		return unusedActionList;
	}

	public void setUnusedActionList(List<Action> unusedActionList)
	{
		this.unusedActionList = unusedActionList;
	}

	public Map<RewardFunction, List<Pair<Action, Double>>> getActionHistoryMap()
	{
		return actionHistoryMap;
	}

	public void setActionHistoryMap(Map<RewardFunction, List<Pair<Action, Double>>> actionHistoryMap)
	{
		this.actionHistoryMap = actionHistoryMap;
	}

	public Map<RewardFunction, Double> getWeightMap()
	{
		return weightMap;
	}

	public void setWeightMap(Map<RewardFunction, Double> weightMap)
	{
		this.weightMap = weightMap;
	}

	public Map<RewardFunction, Double> getMissingValueMap()
	{
		return missingValueMap;
	}

	public void setMissingValueMap(Map<RewardFunction, Double> missingValueMap)
	{
		this.missingValueMap = missingValueMap;
	}
}
