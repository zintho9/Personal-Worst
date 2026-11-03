package com.SaintMina.PersonalWorst;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PersonalWorstTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PersonalWorstPlugin.class);
		RuneLite.main(args);
	}
}