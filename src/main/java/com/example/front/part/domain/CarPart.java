package com.example.front.part.domain;

import lombok.Getter;

@Getter
public enum CarPart {
    FRONT_BUMPER("앞범퍼", 540000, false),
    REAR_BUMPER("뒷범퍼", 530000, false),
    TRUNK("트렁크", 770000, false),
    HEAD_LAMP("전조등", 210000, true),
    TAIL_LAMP("후램프", 150000, true),
    HOOD("본넷(후드)", 860000, false),
    FRONT_WINDSHIELD("앞유리", 290000, true),
    ROOF("루프", 2150000, false),
    REAR_WINDSHIELD("뒷유리", 210000, true),
    FRONT_FENDER("앞펜더", 710000, false),
    REAR_FENDER("뒷펜더", 2100000, false),
    SIDE_MIRROR("사이드미러", 160000, true),
    FRONT_DOOR("전도어", 800000, false),
    REAR_DOOR("후도어", 850000, false),
    SIDE_STEP("스텝", 140000, false),
    TIRE("타이어", 100000, true),
    WHEEL("휠", 170000, true);

    private final String displayName;
    private final int replacementCost;
    private final boolean isReplacementOnly;

    CarPart(String displayName, int replacementCost, boolean isReplacementOnly) {
        this.displayName = displayName;
        this.isReplacementOnly = isReplacementOnly;
        this.replacementCost = replacementCost;
    }
}
