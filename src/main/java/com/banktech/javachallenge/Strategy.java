package com.banktech.javachallenge;

import com.banktech.javachallenge.service.domain.game.GameInfoResponse;
import com.banktech.javachallenge.world.World;

public interface Strategy {

    void move(GameInfoResponse gameInfo, World world);

}
