package org.assistments.peerassist.policies.actionchooser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assistments.peerassist.miscellaneous.Pair;
import org.assistments.peerassist.policies.Action;
import org.assistments.peerassist.policies.RewardFunction;
import org.assistments.peerassist.policies.thompsonsampling.ThompsonSampling;

import cern.jet.random.Beta;
import cern.jet.random.engine.RandomEngine;

public class ThompsonSamplingActionChooser<P extends ThompsonSampling> implements ActionChooser
{
	private P parentPolicy;
	
	public ThompsonSamplingActionChooser(P parentPolicy)
	{
		this.parentPolicy = parentPolicy;
	}
	
	public P getParentPolicy()
	{
		return parentPolicy;
	}

	public void setParentPolicy(P parentPolicy)
	{
		this.parentPolicy = parentPolicy;
	}

	
	@Override
	public Action chooseAction()
	{		
		Action finalAction = null;
	
		List<Action> usedActionList = parentPolicy.getUsedActionList();
		List<Action> unusedActionList = parentPolicy.getUnusedActionList();
		Map<RewardFunction, List<Pair<Action, Double>>> actionHistoryMap = parentPolicy.getActionHistoryMap();
		Map<RewardFunction, Double> weightMap = parentPolicy.getWeightMap();
		Map<RewardFunction, Double> missingValueMap = parentPolicy.getMissingValueMap();
		
		Map<RewardFunction, Map<Action, Double>> thompsonSamplingValuesMap = new HashMap<RewardFunction, Map<Action, Double>>();
		//Map<RewardFunction, Boolean> newActionMap = new HashMap<RewardFunction, Boolean>();
		
		//calculate thomspon sampling values for each reward function
		RandomEngine randomEngine = parentPolicy.getRandomEngine();
//		RandomEngine randomEngine = new MersenneTwister(new Date());
		
		double tuningConstant = parentPolicy.getTuningConstant();

		Set<RewardFunction> rewardFunctionKeySet = actionHistoryMap.keySet();
		List<Action> actionList = new ArrayList<>();
		actionList.addAll(unusedActionList);
		actionList.addAll(usedActionList);
		// set prior for each action
		for(RewardFunction rewardFunction : rewardFunctionKeySet)
		{
			List<Pair<Action, Double>> actionHistory = actionHistoryMap.get(rewardFunction);

			//replace missing rewards with averages of default values
			replaceMissingRewards(actionHistory, missingValueMap.get(rewardFunction));
			
			
			Map<Action, Double> thomsponSamplingValues = new HashMap<Action, Double>();
			for(Action action: unusedActionList) {
				Beta beta = new Beta(1, 1, randomEngine);
				Double sampleValue = beta.nextDouble();
				thomsponSamplingValues.put(action, sampleValue);
			}
			
			Map<Action, Double> totalReward = new HashMap<>();
			Map<Action, Double> actionCount = new HashMap<>();
			for(Pair<Action, Double> actionReward : actionHistory) {
				Action actionKey = actionReward.getValue1();
				Double rewardValue = actionReward.getValue2();
				if(totalReward.containsKey(actionKey)) {
					totalReward.put(actionKey, totalReward.get(actionKey)+rewardValue);
					actionCount.put(actionKey, actionCount.get(actionKey)+1);
				} else {
					totalReward.put(actionKey, rewardValue);
					actionCount.put(actionKey, 1.0);
				}
			}
			for(Action action: usedActionList) {
				Double reward = totalReward.get(action);
				Double count = actionCount.get(action);
				Beta beta = new Beta(tuningConstant*reward+1, 1+tuningConstant*(count-reward), randomEngine);
				//Beta beta = new Beta(10*reward+1, 1, randomEngine);
				Double sampleValue = beta.nextDouble();
				thomsponSamplingValues.put(action, sampleValue);
			}
			thompsonSamplingValuesMap.put(rewardFunction, thomsponSamplingValues);
		}
		
		//normalize all rewards here
		
		double maxThompsonSamplingValue = 0.0;
		
		for(RewardFunction rewardFunction : thompsonSamplingValuesMap.keySet())
		{
			Map<Action, Double> thompsonSamplingValues = thompsonSamplingValuesMap.get(rewardFunction);

			for(Action action : thompsonSamplingValues.keySet())
			{
				double thomsponsSamplingValue = thompsonSamplingValues.get(action);
				
				if(thomsponsSamplingValue > maxThompsonSamplingValue)
				{
					maxThompsonSamplingValue = thomsponsSamplingValue;
				}
			}
		}
		
		if(maxThompsonSamplingValue > 0.0)
		{
			for(RewardFunction rewardFunction : thompsonSamplingValuesMap.keySet())
			{
				Map<Action, Double> thompsonSamplingValues = thompsonSamplingValuesMap.get(rewardFunction);
	
				for(Action action : thompsonSamplingValues.keySet())
				{
					double thompsonSamplingValueValue = thompsonSamplingValues.get(action);
					double normalizedValue = thompsonSamplingValueValue / maxThompsonSamplingValue;
					
					thompsonSamplingValues.replace(action, normalizedValue);
				}
			}
		}
		
		// random choose a action
//		if(!unusedActionList.isEmpty()) {
//			Map<Action, Double> finalreward = new HashMap<>();
//			for(RewardFunction rewardFunc: thompsonSamplingValuesMap.keySet()) {
//				Map<Action, Double> tmp = thompsonSamplingValuesMap.get(rewardFunc);
//				for(Action act: tmp.keySet()) {
//					if(finalreward.containsKey(act)) {
//						finalreward.put(act, finalreward.get(act)+tmp.get(act));
//					} else {
//						finalreward.put(act, tmp.get(act));
//					}
//				}
//			}
//			double maxValue = 0.0;
//			for(Action act: finalreward.keySet()) {
//				if(maxValue < finalreward.get(act)) {
//					finalAction = act;
//					maxValue = finalreward.get(act);
//				}
//			}
//		} else {
			//for each action calculate weighted reward
			List<Pair<Action, Double>> rewardList = WeightedChooser.calculateWeightedRewardValues(actionList, actionHistoryMap, thompsonSamplingValuesMap, weightMap);
			List<Pair<Action, Double>> list = rewardList;
//			List<Pair<Action, Double>> list = new ArrayList<>();
//			for(Pair<Action, Double> pair: rewardList) {
//				if (pair.getValue2() >= 0) {
//					list.add(pair);
//				}
//			}
			//if there are any actions
			if(list.size() > 0)
			{
				//sort the list by value of pair
				Collections.sort(list, Comparator.comparing(Pair::getValue2));
					
				int actionIndex = list.size()-1;
				finalAction = list.get(actionIndex).getValue1();
			}			
//		}
		return finalAction;
	}


	private void replaceMissingRewards(List<Pair<Action, Double>> rewardPairingList, double defaultReward)
	{
		double replaceValue = defaultReward;
		double average = calculateAverageReward(rewardPairingList);
		
		if(average >= 0)
		{
			replaceValue = average;
		}
		
		 for(int i=0; i<rewardPairingList.size(); i++)
		 {
			 Double reward = rewardPairingList.get(i).getValue2();
			 
			 if(reward < 0)
			 {
				 rewardPairingList.get(i).setValue2(replaceValue);
			 }
		 }
	}	
	
	private double calculateAverageReward(List<Pair<Action, Double>> rewardPairingList)
	{
		double average = -1.0;
		
		if(rewardPairingList.size() > 0)
		{
			double totalReward = 0.0;
			int rewardCount = 0;
			
			 for(Pair<Action, Double> actionRewardPair : rewardPairingList)
			 {
				 double reward = actionRewardPair.getValue2();
				 
				 if(reward >= 0)
				 {
					 totalReward = totalReward + reward;
					 rewardCount++;
				 }
			 }
			 
			 average = totalReward / rewardCount;
		}
		return average;
	}
}
