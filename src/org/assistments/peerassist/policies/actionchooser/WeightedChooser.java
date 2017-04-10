package org.assistments.peerassist.policies.actionchooser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assistments.peerassist.miscellaneous.Pair;
import org.assistments.peerassist.policies.Action;
import org.assistments.peerassist.policies.RewardFunction;

public class WeightedChooser
{
	//Action list can contain a special case where an action has no history
	//UCB Weighted-AIR only passes in used actions
	//Thompson sampling can pass in used and unused actions
	public static List<Pair<Action, Double>> calculateWeightedRewardValues(List<Action> actionList, Map<RewardFunction, List<Pair<Action, Double>>> actionHistoryMap, Map<RewardFunction, Map<Action, Double>> ucbValuesMap, Map<RewardFunction, Double> weightMap)
	{
		//adjusted weight = normalized weight * base weight | for a given action
		//normalized adjusted weight |  for a given action
		//adjusted reward = normalized adjusted weight * policy return value | for a given action
		//final reward = sum of all adjusted rewards
		
		Map<Action, Map<RewardFunction, Integer>> countMap = getCountMapActions(actionList, actionHistoryMap);
		Map<Action, Map<RewardFunction, Double>> normalizedCountMap = getNormalizedCountMap(countMap);
		Map<Action, Map<RewardFunction, Double>> adjustedWeightMap = getAdjustedWeightMap(normalizedCountMap, weightMap);
		Map<Action, Map<RewardFunction, Double>> normalizedAdjustedWeightMap = getNormalizedAdjustedWeightMap(adjustedWeightMap);
		Map<Action, Map<RewardFunction, Double>> adjustedRewardMap = getAdjustedRewardMap(normalizedAdjustedWeightMap, ucbValuesMap);
		
		return getWeightedRewardValues(adjustedRewardMap);

	}
	
	
	private static Map<Action, Map<RewardFunction, Integer>> getCountMapActions(List<Action> actionList, Map<RewardFunction, List<Pair<Action, Double>>> actionHistoryMap)
	{
		Map<Action, Map<RewardFunction, Integer>> countMap = new HashMap<Action, Map<RewardFunction, Integer>>();
		
		Set<RewardFunction> rewardFunctionKeySet = actionHistoryMap.keySet();
		
		//initialize all counts to zero
		for(Action action : actionList)
		{
			Map<RewardFunction, Integer> rewardCountMap = new HashMap<RewardFunction, Integer>();
			
			for(RewardFunction rewardFunction : rewardFunctionKeySet)
			{
				rewardCountMap.put(rewardFunction, 0);
			}
			
			countMap.put(action, rewardCountMap);
		}
		
		//every action will have counts for all reward functions
		
		for(RewardFunction rewardFunction : rewardFunctionKeySet)
		{
			List<Pair<Action, Double>> actionHistory = actionHistoryMap.get(rewardFunction);
			
			for(Pair<Action, Double> actionReward : actionHistory)
			{
				Action currentAction = actionReward.getValue1();
				
				if(actionReward.getValue2() >= 0)
				{
					int currentCount = countMap.get(currentAction).get(rewardFunction);
					countMap.get(currentAction).put(rewardFunction, ++currentCount);
				}
			}
		}
			
		return countMap;
	}

	private static Map<Action, Map<RewardFunction, Double>> getNormalizedCountMap(Map<Action, Map<RewardFunction, Integer>> countMap)
	{
		Map<Action, Map<RewardFunction, Double>> normalizedCountMap = new HashMap<Action, Map<RewardFunction, Double>>();
		
		Set<Action> actionKeySet = countMap.keySet();
		
		for(Action action : actionKeySet)
		{
			Map<RewardFunction, Double> normalizedRewardCountMap = new HashMap<RewardFunction, Double>();
			Map<RewardFunction, Integer> rewardCountMap = countMap.get(action);
			Set<RewardFunction> rewardFunctionKeySet = rewardCountMap.keySet();
			
			double total = 0.0;
			
			for(RewardFunction rewardFunction : rewardFunctionKeySet)
			{
				total = total + rewardCountMap.get(rewardFunction);
			}
			
			//If there is no action history, all rewards have equal actual weight
			if(total == 0)
			{
				double numberOfRewardFunctions = rewardFunctionKeySet.size();
				
				for(RewardFunction rewardFunction : rewardFunctionKeySet)
				{
					double normalizedReward = 1.0 / numberOfRewardFunctions;
					normalizedRewardCountMap.put(rewardFunction, normalizedReward);
				}
			}
			else
			{
				for(RewardFunction rewardFunction : rewardFunctionKeySet)
				{
					double normalizedReward = rewardCountMap.get(rewardFunction) / total;
					normalizedRewardCountMap.put(rewardFunction, normalizedReward);
				}
			}
			
			normalizedCountMap.put(action, normalizedRewardCountMap);
		}
		
		return normalizedCountMap;
	}
	
	private static Map<Action, Map<RewardFunction, Double>> getAdjustedWeightMap(Map<Action, Map<RewardFunction, Double>> normalizedCountMap, Map<RewardFunction, Double> weightMap)
	{
		Map<Action, Map<RewardFunction, Double>> adjustedWeightMap = new HashMap<Action, Map<RewardFunction, Double>>();
		
		Set<Action> actionKeySet = normalizedCountMap.keySet();
		
		for(Action action : actionKeySet)
		{
			Map<RewardFunction, Double> adjustedRewardWeightMap = new HashMap<RewardFunction, Double>();
			Map<RewardFunction, Double> normalizedWeightMap = normalizedCountMap.get(action);
			Set<RewardFunction> rewardFunctionKeySet = normalizedWeightMap.keySet();
						
			for(RewardFunction rewardFunction : rewardFunctionKeySet)
			{
				double adjustedWeight = normalizedWeightMap.get(rewardFunction) * weightMap.get(rewardFunction);
				adjustedRewardWeightMap.put(rewardFunction, adjustedWeight);
			}
			
			adjustedWeightMap.put(action, adjustedRewardWeightMap);
		}
		
		return adjustedWeightMap;
	}
	
	private static Map<Action, Map<RewardFunction, Double>> getNormalizedAdjustedWeightMap(Map<Action, Map<RewardFunction, Double>> adjustedWeightMap)
	{
		Map<Action, Map<RewardFunction, Double>> normalizedAdjustedWeightMap = new HashMap<Action, Map<RewardFunction, Double>>();
		
		Set<Action> actionKeySet = adjustedWeightMap.keySet();
		
		for(Action action : actionKeySet)
		{
			Map<RewardFunction, Double> normalizedRewardAdjustedWeightMap = new HashMap<RewardFunction, Double>();
			Map<RewardFunction, Double> adjustedRewardWeightMap = adjustedWeightMap.get(action);
			Set<RewardFunction> rewardFunctionKeySet = adjustedRewardWeightMap.keySet();
			
			double total = 0.0;
			
			for(RewardFunction rewardFunction : rewardFunctionKeySet)
			{
				total = total + adjustedRewardWeightMap.get(rewardFunction);
			}
			
			for(RewardFunction rewardFunction : rewardFunctionKeySet)
			{
				double normalizedReward = adjustedRewardWeightMap.get(rewardFunction) / total;
				normalizedRewardAdjustedWeightMap.put(rewardFunction, normalizedReward);
			}
			
			normalizedAdjustedWeightMap.put(action, normalizedRewardAdjustedWeightMap);
		}
		
		return normalizedAdjustedWeightMap;
	}
	
	private static Map<Action, Map<RewardFunction, Double>> getAdjustedRewardMap(Map<Action, Map<RewardFunction, Double>> normalizedAdjustedWeightMap, Map<RewardFunction, Map<Action, Double>> ucbValuesMap)
	{
		Map<Action, Map<RewardFunction, Double>> adjustedRewardMap = new HashMap<Action, Map<RewardFunction, Double>>();
		
		Set<Action> actionKeySet = normalizedAdjustedWeightMap.keySet();
		
		for(Action action : actionKeySet)
		{
			Map<RewardFunction, Double> adjustedRewardWeightMap = new HashMap<RewardFunction, Double>();
			Map<RewardFunction, Double> normalizedAdjustedRewardWeightMap = normalizedAdjustedWeightMap.get(action);
			Set<RewardFunction> rewardFunctionKeySet = normalizedAdjustedRewardWeightMap.keySet();
						
			for(RewardFunction rewardFunction : rewardFunctionKeySet)
			{
				double adjustedReward = 0.0;
				
				if(ucbValuesMap.get(rewardFunction).get(action) != null)
				{
					adjustedReward = normalizedAdjustedRewardWeightMap.get(rewardFunction) * ucbValuesMap.get(rewardFunction).get(action);
				}
				
				adjustedRewardWeightMap.put(rewardFunction, adjustedReward);
			}
			
			adjustedRewardMap.put(action, adjustedRewardWeightMap);
		}
		
		return adjustedRewardMap;
	}
	
	private static List<Pair<Action, Double>> getWeightedRewardValues(Map<Action, Map<RewardFunction, Double>> adjustedRewardMap)
	{
		List<Pair<Action, Double>> weightedRewardValues = new ArrayList<Pair<Action, Double>>();
		
		Set<Action> actionKeySet = adjustedRewardMap.keySet();
		
		for(Action action : actionKeySet)
		{
			Set<RewardFunction> rewardFunctionKeySet = adjustedRewardMap.get(action).keySet();
			
			double totalReward = 0.0;
			
			for(RewardFunction rewardFunction : rewardFunctionKeySet)
			{
				totalReward = totalReward + adjustedRewardMap.get(action).get(rewardFunction);
			}
			
			Pair<Action, Double> actionReward = new Pair<Action, Double>(action, totalReward);
			weightedRewardValues.add(actionReward);
		}
		
		return weightedRewardValues;
	}
}
