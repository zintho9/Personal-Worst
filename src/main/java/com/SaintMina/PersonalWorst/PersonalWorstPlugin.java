package com.SaintMina.PersonalWorst;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
		name = "Personal Worsts",
		description = "Tracks your slowest completion times, shows them in the Collection Log, and triggers custom animations/sounds.",
		tags = {"pbt", "worst", "time", "boss", "pvm"}
)
public class PersonalWorstPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private PersonalWorstConfig config;

	@Inject
	private ConfigManager configManager;

	private final Random random = new Random();

	private static final Pattern TIME_PATTERN = Pattern.compile(
			"(?:(.+?)(?:\\s+duration:|\\s+Duration:))\\s*(?:(?:(\\d+):)?(\\d+):(\\d+)|(\\d+):(\\d+))",
			Pattern.CASE_INSENSITIVE
	);

	@Provides
	PersonalWorstConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PersonalWorstConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("personalworsts"))
		{
			return;
		}

		if (event.getKey().equals("purgePersonalWorsts"))
		{
			SwingUtilities.invokeLater(() -> {
				int confirm = JOptionPane.showConfirmDialog(
						null,
						"Are you sure you want to purge ALL saved Personal Worst times?\nThis action cannot be undone.",
						"Confirm Personal Worsts Purge",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE
				);

				if (confirm == JOptionPane.YES_OPTION)
				{
					purgeAllWorsts();
				}
			});
		}
	}

	private void purgeAllWorsts()
	{
		for (String key : configManager.getConfigurationKeys("personalworsts.worst_"))
		{
			String cleanKey = key.replace("personalworsts.", "");
			configManager.unsetConfiguration("personalworsts", cleanKey);
		}

		if (client != null)
		{
			client.addChatMessage(
					ChatMessageType.GAMEMESSAGE,
					"",
					"<col=ff0000>Personal Worsts data has been purged successfully.</col>",
					null
			);
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE && chatMessage.getType() != ChatMessageType.SPAM)
		{
			return;
		}

		String message = Text.removeTags(chatMessage.getMessage());
		Matcher matcher = TIME_PATTERN.matcher(message);

		if (matcher.find())
		{
			String activityName = matcher.group(1).trim();
			activityName = activityName.replaceAll("(?i)^Challenge complete:\\s*", "");

			int timeInSeconds = parseDurationToSeconds(message);

			if (!activityName.isEmpty() && timeInSeconds > 0)
			{
				checkAndUpdatePersonalWorst(activityName, timeInSeconds);
			}
		}
	}

	private void checkAndUpdatePersonalWorst(String activity, int seconds)
	{
		String configKey = "worst_" + activity.toLowerCase().replaceAll("[^a-z0-9]", "_");
		Integer currentWorst = configManager.getConfiguration("personalworsts", configKey, int.class);

		if (currentWorst == null || seconds > currentWorst)
		{
			configManager.setConfiguration("personalworsts", configKey, seconds);

			if (config.enableChatNotifications())
			{
				String formattedTime = formatTime(seconds);
				client.addChatMessage(
						ChatMessageType.GAMEMESSAGE,
						"",
						"<col=ff0000>New Personal Worst!</col> " + activity + " time: <col=ff0000>" + formattedTime + "</col>",
						null
				);
			}

			triggerEffects();
		}
	}

	private void triggerEffects()
	{
		if (client.getLocalPlayer() != null)
		{
			// Level-up fireworks
			if (config.enableFireworks())
			{
				client.getLocalPlayer().setGraphic(1389);
			}

			// Perform player emote animation
			WorstEmote emote = config.selectedEmote();
			if (emote == WorstEmote.RANDOM)
			{
				WorstEmote[] validEmotes = new WorstEmote[]{
						WorstEmote.CRY, WorstEmote.DANCE, WorstEmote.GOBLIN_SALUTE,
						WorstEmote.ZOMBIE_DANCE, WorstEmote.HEADBANG, WorstEmote.JUMP_FOR_JOY,
						WorstEmote.WAVE, WorstEmote.SHIVER
				};
				emote = validEmotes[random.nextInt(validEmotes.length)];
			}

			if (emote != WorstEmote.NONE && emote.getAnimationId() > 0)
			{
				client.getLocalPlayer().setAnimation(emote.getAnimationId());
			}
		}

		if (config.soundEffectId() > 0)
		{
			client.playSoundEffect(config.soundEffectId());
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (!config.enableCollectionLogDisplay())
		{
			return;
		}

		injectWorstIntoCollectionLog();
	}

	private void injectWorstIntoCollectionLog()
	{
		Widget titleWidget = client.getWidget(InterfaceID.COLLECTION_LOG, 2);
		if (titleWidget == null || titleWidget.isHidden())
		{
			return;
		}

		Widget[] children = titleWidget.getChildren();
		if (children == null)
		{
			return;
		}

		for (Widget child : children)
		{
			if (child != null && child.getText() != null && child.getText().contains("Personal Best:"))
			{
				String originalText = child.getText();
				if (!originalText.contains("Personal Worst:"))
				{
					String activityName = extractActiveBossName();
					if (activityName != null)
					{
						String configKey = "worst_" + activityName.toLowerCase().replaceAll("[^a-z0-9]", "_");
						Integer worstTime = configManager.getConfiguration("personalworsts", configKey, int.class);
						String worstStr = worstTime != null ? formatTime(worstTime) : "None";

						child.setText(originalText + "<br>Personal Worst: <col=ff0000>" + worstStr + "</col>");
					}
				}
			}
		}
	}

	String extractActiveBossName()
	{
		Widget header = client.getWidget(InterfaceID.COLLECTION_LOG, 1);
		if (header != null && header.getChild(1) != null)
		{
			return header.getChild(1).getText();
		}
		return null;
	}

	private int parseDurationToSeconds(String message)
	{
		Pattern p = Pattern.compile("(\\d+:)?(\\d+):(\\d+)");
		Matcher m = p.matcher(message);
		if (m.find())
		{
			int hours = m.group(1) != null ? Integer.parseInt(m.group(1).replace(":", "")) : 0;
			int minutes = Integer.parseInt(m.group(2));
			int seconds = Integer.parseInt(m.group(3));
			return (hours * 3600) + (minutes * 60) + seconds;
		}
		return 0;
	}

	private String formatTime(int totalSeconds)
	{
		int hours = totalSeconds / 3600;
		int minutes = (totalSeconds % 3600) / 60;
		int seconds = totalSeconds % 60;

		if (hours > 0)
		{
			return String.format("%d:%02d:%02d", hours, minutes, seconds);
		}
		return String.format("%d:%02d", minutes, seconds);
	}
}