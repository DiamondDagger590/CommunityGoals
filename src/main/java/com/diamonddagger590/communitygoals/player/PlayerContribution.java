package com.diamonddagger590.communitygoals.player;

import java.util.UUID;

public record PlayerContribution(int goalId, UUID contributor, int contributionAmount) {

}
