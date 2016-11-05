package com.banktech.javachallenge.world;

import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.Submarine;


public interface World {
    Object cellAt(final Position position);

    void move(final Submarine submarine, final MoveRequest moveRequest);

    Position size();
}
