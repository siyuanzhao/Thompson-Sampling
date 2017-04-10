package org.assistments.peerassist.policies.actionchooser;

import org.assistments.peerassist.policies.Action;

@FunctionalInterface
public interface ActionChooser
{
	public Action chooseAction();
}
