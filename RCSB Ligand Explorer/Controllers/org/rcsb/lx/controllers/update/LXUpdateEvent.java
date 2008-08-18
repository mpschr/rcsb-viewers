package org.rcsb.lx.controllers.update;

import org.rcsb.mbt.controllers.update.IUpdateListener;
import org.rcsb.mbt.controllers.update.UpdateEvent;
import org.rcsb.mbt.model.Structure;


public class LXUpdateEvent extends UpdateEvent
{
	public boolean transitory = false;

	public LXUpdateEvent(Action action, IUpdateListener view)
	{
		super(action, view);
	}

	public LXUpdateEvent(Action action, Structure structure)
	{
		super(action, structure);
	}

	public LXUpdateEvent(Action action)
	{
		super(action);
	}
	
}