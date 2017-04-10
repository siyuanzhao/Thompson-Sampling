package org.assistments.peerassist.policies.thompsonsampling;

import java.util.Random;

//import org.assistments.peerassist.domain.SchoolFeatures;
//import org.assistments.peerassist.domain.StudentFeatures;
import org.assistments.peerassist.policies.Action;
import org.assistments.peerassist.policies.actionchooser.ThompsonSamplingActionChooser;
import org.assistments.peerassist.policies.defaultpolicy.DefaultPolicy;

import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;

public class ThompsonSampling extends DefaultPolicy
{
	private ThompsonSamplingActionChooser<ThompsonSampling> thompsonSamplingActionChooser;	
	private RandomEngine randomEngine;
	private static final int DEFAULT_RANDOM_SEED = 21;
	
	//tuning constant [0, inifinity)
	//less = explore, more = exploit
	private double tuningConstant;
	
	//for all students who created explanations in the list and the current user
	//may be missing entries due to no data
	//private List<StudentFeatures> studentFeaturesList;
	//private List<SchoolFeatures> schoolFeaturesList;
	//private ProblemFeatures problemFeatures;
	//private ExplanationFeatures explanationFeatures;
	
	//contexts for all students who created explanations that are possible choices
	//private List<Integer[]> binaryStudentFeaturesList;
	//private List<Integer[]> binarySchoolFeaturesList;
	
	//context for current student (1 row)
	//private Integer[] binaryStudentFeatures;
	//private Integer[] binarySchoolFeatures;
	public ThompsonSampling()
	{
		super();
		randomEngine = new MersenneTwister(DEFAULT_RANDOM_SEED);
		thompsonSamplingActionChooser = new ThompsonSamplingActionChooser<ThompsonSampling>(this);
		tuningConstant = 1.0;
	}
	
	public ThompsonSampling(Random random)
	{
		super(random);
		randomEngine = new MersenneTwister(DEFAULT_RANDOM_SEED);
		thompsonSamplingActionChooser = new ThompsonSamplingActionChooser<ThompsonSampling>(this);
		tuningConstant = 1.0;
	}
	

	//
	
	public RandomEngine getRandomEngine()
	{
		return randomEngine;
	}
	
	public double getTuningConstant()
	{
		return tuningConstant;
	}
	
	//should really only set once
	public void setTuningConstant(double tuningConstant)
	{
		this.tuningConstant = tuningConstant;
	}

	/*
	public void setRandomEngine(RandomEngine randomEngine)
	{
		this.randomEngine = randomEngine;
	}
	*/
	

	public ThompsonSamplingActionChooser<ThompsonSampling> getThompsonSamplingActionChooser()
	{
		return thompsonSamplingActionChooser;
	}
	

	//
	

	//
	

	/*
	public List<StudentFeatures> getStudentFeaturesList()
	{
		return studentFeaturesList;
	}

	public void setStudentFeaturesList(List<StudentFeatures> studentFeaturesList)
	{
		this.studentFeaturesList = studentFeaturesList;
	}

	public List<SchoolFeatures> getSchoolFeaturesList()
	{
		return schoolFeaturesList;
	}

	public void setSchoolFeaturesList(List<SchoolFeatures> schoolFeaturesList)
	{
		this.schoolFeaturesList = schoolFeaturesList;
	}
	*/

	/*
	public ProblemFeatures getProblemFeatures()
	{
		return problemFeatures;
	}

	public void setProblemFeatures(ProblemFeatures problemFeatures)
	{
		this.problemFeatures = problemFeatures;
	}

	public ExplanationFeatures getExplanationFeatures()
	{
		return explanationFeatures;
	}

	public void setExplanationFeatures(ExplanationFeatures explanationFeatures)
	{
		this.explanationFeatures = explanationFeatures;
	}
	*/
	
	//
	
	/*
	public List<Integer[]> getBinaryStudentFeaturesList()
	{
		return binaryStudentFeaturesList;
	}

	public void setBinaryStudentFeaturesList(List<Integer[]> binaryStudentFeaturesList)
	{
		this.binaryStudentFeaturesList = binaryStudentFeaturesList;
	}
	
	public List<Integer[]> getBinarySchoolFeaturesList()
	{
		return binarySchoolFeaturesList;
	}

	public void setBinarySchoolFeaturesList(List<Integer[]> binarySchoolFeaturesList)
	{
		this.binarySchoolFeaturesList = binarySchoolFeaturesList;
	}
	
	
	//
	
	
	public void setBinaryStudentFeatures(Integer[] binaryStudentFeatures)
	{
		this.binaryStudentFeatures = binaryStudentFeatures;
	}

	public Integer[] getBinaryStudentFeatures()
	{
		return binaryStudentFeatures;
	}
	
	public Integer[] getBinarySchoolFeatures()
	{
		return binarySchoolFeatures;
	}

	public void setBinarySchoolFeatures(Integer[] binarySchoolFeatures)
	{
		this.binarySchoolFeatures = binarySchoolFeatures;
	}
	*/
	
	//
	@Override
	public void retrieveData()
	{
		
	}

	
	@Override
	public Action chooseAction()
	{
		return thompsonSamplingActionChooser.chooseAction();
	}

	@Override
	public void preprocess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logData() {
		// TODO Auto-generated method stub
		
	}
	
}
