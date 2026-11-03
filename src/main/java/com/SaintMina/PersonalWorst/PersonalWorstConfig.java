package com.SaintMina.PersonalWorst;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("personalworsts")
public interface PersonalWorstConfig extends Config
{
	@ConfigItem(
			keyName = "enableChatNotifications",
			name = "Chat Notifications",
			description = "Sends a local chat message whenever you set a new Personal Worst completion time."
	)
	default boolean enableChatNotifications()
	{
		return true;
	}

	@ConfigItem(
			keyName = "enableCollectionLogDisplay",
			name = "Show in Collection Log",
			description = "Displays your Personal Worst time in-line with the Personal Best field in the Collection Log."
	)
	default boolean enableCollectionLogDisplay()
	{
		return true;
	}

	@ConfigItem(
			keyName = "enableFireworks",
			name = "Level-up Fireworks",
			description = "Triggers level-up style fireworks graphic when a new Personal Worst is achieved."
	)
	default boolean enableFireworks()
	{
		return true;
	}

	@ConfigItem(
			keyName = "selectedEmote",
			name = "Player Emote",
			description = "Select an emote for your character to perform when a Personal Worst is set (or Random)."
	)
	default WorstEmote selectedEmote()
	{
		return WorstEmote.CRY;
	}

	@ConfigItem(
			keyName = "soundEffectId",
			name = "Sound Effect ID",
			description = "The RuneLite Sound Effect ID to play on a new Personal Worst (Default: 382 for swamp_fart_2, or 0 to disable)."
	)
	default int soundEffectId()
	{
		return 382;
	}

	@ConfigItem(
			keyName = "purgePersonalWorsts",
			name = "Purge All Personal Worsts",
			description = "Clear all saved Personal Worst completion times."
	)
	default Runnable purgePersonalWorsts()
	{
		return () -> {};
	}
}