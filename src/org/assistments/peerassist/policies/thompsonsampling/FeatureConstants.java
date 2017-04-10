package org.assistments.peerassist.policies.thompsonsampling;

import java.util.ArrayList;
import java.util.List;

import org.assistments.peerassist.miscellaneous.Pair;

public class FeatureConstants
{
	private static FeatureConstants featureConstants;
	
	private List<String> gradeList;
	private List<String> genderList;
	private List<Pair<Double, Double>> correctPercentBinList;
	private List<Pair<Double, Double>> completionPercentBinList;
	
	private FeatureConstants()
	{
		gradeList = new ArrayList<String>();
		gradeList.add("k");
		gradeList.add("1");
		gradeList.add("2");
		gradeList.add("3");
		gradeList.add("4");
		gradeList.add("5");
		gradeList.add("6");
		gradeList.add("7");
		gradeList.add("8");
		gradeList.add("9");
		gradeList.add("10");
		gradeList.add("11");
		gradeList.add("12");
		gradeList.add("Freshmen");
		gradeList.add("Sophomore");
		gradeList.add("Junior");
		gradeList.add("Senior");
		gradeList.add("Masters");
		gradeList.add("PhD");
		
		genderList = new ArrayList<String>();
		genderList.add("Male");
		genderList.add("Female");
		genderList.add("Unknown");
		
		correctPercentBinList = new ArrayList<Pair<Double, Double>>();
		correctPercentBinList.add(new Pair<Double, Double>(0.0, 0.105651106));
		correctPercentBinList.add(new Pair<Double, Double>(0.105651106, 0.289099526));
		correctPercentBinList.add(new Pair<Double, Double>(0.289099526, 0.439490446));
		correctPercentBinList.add(new Pair<Double, Double>(0.439490446, 0.581218274));
		correctPercentBinList.add(new Pair<Double, Double>(0.581218274, 0.723880597));
		correctPercentBinList.add(new Pair<Double, Double>(0.723880597, 0.847161572));
		correctPercentBinList.add(new Pair<Double, Double>(0.847161572, 0.940397351));
		correctPercentBinList.add(new Pair<Double, Double>(0.940397351, 1.0));

		
		completionPercentBinList = new ArrayList<Pair<Double, Double>>();
		completionPercentBinList.add(new Pair<Double, Double>(0.0, 0.133333333));
		completionPercentBinList.add(new Pair<Double, Double>(0.133333333, 0.178571429));
		completionPercentBinList.add(new Pair<Double, Double>(0.178571429, 0.181818182));
		completionPercentBinList.add(new Pair<Double, Double>(0.181818182, 0.387755102));
		completionPercentBinList.add(new Pair<Double, Double>(0.387755102, 0.581081081));
		completionPercentBinList.add(new Pair<Double, Double>(0.581081081, 0.709459459));
		completionPercentBinList.add(new Pair<Double, Double>(0.709459459, 0.808333333));
		completionPercentBinList.add(new Pair<Double, Double>(0.808333333, 0.870056497));
		completionPercentBinList.add(new Pair<Double, Double>(0.870056497, 1.0));
		
	}
	
	public static FeatureConstants getFeatureConstants()
	{
		if(featureConstants == null)
		{
			featureConstants = new FeatureConstants();
		}
		
		return featureConstants;
	}
	
	public List<String> getGradeList()
	{
		return gradeList;
	}
	
	public List<String> getGenderList()
	{
		return genderList;
	}
	
	public List<Pair<Double, Double>> getCorrectPercentBinList()
	{
		return correctPercentBinList;
	}
	
	public List<Pair<Double, Double>> getCompletionPercentBinList()
	{
		return completionPercentBinList;
	}

}
