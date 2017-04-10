
package org.assistments.peerassist.miscellaneous;

public class Pair<A, B>
{
	private A value1;
	private B value2;
	
	public Pair(A v1, B v2)
	{
		value1 = v1;
		value2 = v2;
	}
	
	public void setValue1(A newValue1)
	{
		value1 = newValue1;
	}
	
	public A getValue1()
	{
		return value1;
	}
	
	public void setValue2(B newValue2)
	{
		value2 = newValue2;
	}
	
	public B getValue2()
	{
		return value2;
	}
		
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		sb.append(value1);
		sb.append(", ");
		sb.append(value2);
		sb.append("}");
		
		return sb.toString();
		
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value1 == null) ? 0 : value1.hashCode());
		result = prime * result + ((value2 == null) ? 0 : value2.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof Pair))
		{
			return false;
		}
		
		Pair other = (Pair) obj;
		
		if (value1 == null)
		{
			if (other.value1 != null)
			{
				return false;
			}
		}
		else if (!value1.equals(other.value1))
		{
			return false;
		}
		if (value2 == null)
		{
			if (other.value2 != null)
			{
				return false;
			}
		}
		else if (!value2.equals(other.value2))
		{
			return false;
		}
		
		return true;
	}

}
