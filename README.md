# Personal Worsts - RuneLite Plugin

A custom RuneLite plugin designed to track and celebrate your slowest, most disastrous completion times across Old School RuneScape bosses, raids, and minigames. Because achieving perfection is overrated—sometimes you just need to embrace the struggle!

---

## Features

- **Personal Worst Tracking:** Automatically parses completion time chat messages (including boss durations and activity logs like Tombs of Amascut rooms) to detect and save your slowest completion times.
- **Collection Log Integration:** Injects your **Personal Worst** record directly into the Collection Log interface, right below the official Personal Best line.
- **Visual Celebration (Fireworks):** Triggers a level-up style fireworks graphic on your player character whenever a new Personal Worst is achieved.
- **Player Emotes:** Choose an animation (such as *Cry*, *Dance*, *Zombie Dance*, *Headbang*, or *Random*) for your character to perform upon setting a new record.
- **Custom Sound Effects:** Plays a configurable sound effect on a new record (defaults to audio ID `382` for the infamous *swamp_fart_2* sound).
- **Data Management:** Includes a dedicated configuration panel with an in-game confirmation dialog to safely purge saved personal worsts when desired.

---

## Configuration Options

| Setting | Description | Default |
| :--- | :--- | :--- |
| **Chat Notifications** | Displays a red local chat alert when a new Personal Worst is set. | `Enabled` |
| **Show in Collection Log** | Displays the Personal Worst time in-line with Personal Best in the Collection Log interface. | `Enabled` |
| **Level-up Fireworks** | Plays the level-up graphics animation on your player upon setting a new record. | `Enabled` |
| **Player Emote** | Selects an emote for your character to perform (`None`, `Random`, `Cry`, `Dance`, `Goblin Salute`, etc.). | `Cry` |
| **Sound Effect ID** | Set a custom audio ID to play (Set to `0` to disable). | `382` (*swamp_fart_2*) |
| **Purge All Personal Worsts** | A reset button with a confirmation popup to clear all saved local records. | *Action Button* |

---

## Installation & Local Testing

### Prerequisites
- **JDK 11** installed and configured.
- **IntelliJ IDEA** (or equivalent Java IDE).
- **Gradle**.

### Running locally in Developer Mode
1. Clone the repository:
   ```bash
   git clone [https://github.com/zintho9/Personal-Worst.git](https://github.com/zintho9/Personal-Worst.git)