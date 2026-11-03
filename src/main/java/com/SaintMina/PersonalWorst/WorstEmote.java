package com.SaintMina.PersonalWorst;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorstEmote
{
    NONE("None", -1),
    RANDOM("Random", -2),
    CRY("Cry", 860),
    DANCE("Dance", 866),
    GOBLIN_SALUTE("Goblin Salute", 2110),
    ZOMBIE_DANCE("Zombie Dance", 3543),
    HEADBANG("Headbang", 2109),
    JUMP_FOR_JOY("Jump for Joy", 2107),
    WAVE("Wave", 863),
    SHIVER("Shiver", 2108);

    private final String name;
    private final int animationId;

    @Override
    public String toString()
    {
        return name;
    }
}