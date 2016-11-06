package com.banktech.javachallenge.service.world;

import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.service.domain.submarine.*;
import com.banktech.javachallenge.service.domain.Island;
import com.banktech.javachallenge.service.domain.Torpedo;

import java.io.IOException;


public interface World {

    /**
     * Gives back the Cell on the given {@link Position}.
     *
     * @param position {@link Position}
     * @return {@link OwnSubmarine} or {@link Island} or {@link Torpedo} or Null if nothing is there.
     */
    Object cellAt(final Position position);

    void replaceCell(final Position position, Object object);


    /**
     * Moves selected {@link OwnSubmarine} on the Map.
     *
     * @param submarine   {@link OwnSubmarine}
     * @param moveRequest {@link MoveRequest}
     */
    SimpleResponse move(final OwnSubmarine submarine, final MoveRequest moveRequest) throws IOException;

    /**
     * Shoot with selected {@link OwnSubmarine}.
     *
     * @param submarine    {@link OwnSubmarine}
     * @param shootRequest {@link ShootRequest}
     */
    SimpleResponse shoot(final OwnSubmarine submarine, final ShootRequest shootRequest) throws IOException;

    /**
     * Gives back the width and height of the world.
     *
     * @return {@link Position} containing the width and height of the World
     */
    Position size();

    /**
     * Sonar.
     *
     * @param selectedSubmarine {@link OwnSubmarine}
     * @return {@link SonarResponse}
     * @throws IOException
     */
    SonarResponse sonar(OwnSubmarine selectedSubmarine) throws IOException;

    /**
     * Extended sonar.
     *
     * @param selectedSubmarine {@link Entity}
     * @return {@link SonarResponse}
     * @throws IOException
     */
    SonarResponse extendedSonar(OwnSubmarine selectedSubmarine) throws IOException;

}
