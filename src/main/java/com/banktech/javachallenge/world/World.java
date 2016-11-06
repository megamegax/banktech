package com.banktech.javachallenge.world;

import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.ShootRequest;
import com.banktech.javachallenge.service.domain.submarine.SonarResponse;
import com.banktech.javachallenge.service.domain.submarine.Submarine;
import com.banktech.javachallenge.world.domain.Island;
import com.banktech.javachallenge.world.domain.Torpedo;

import java.io.IOException;


public interface World {

    /**
     * Gives back the Cell on the given {@link Position}.
     *
     * @param position {@link Position}
     * @return {@link Submarine} or {@link Island} or {@link Torpedo} or Null if nothing is there.
     */
    Object cellAt(final Position position);

    void replaceCell(final Position position, Object object);


    /**
     * Moves selected {@link Submarine} on the Map.
     *
     * @param submarine   {@link Submarine}
     * @param moveRequest {@link MoveRequest}
     */
    SimpleResponse move(final Submarine submarine, final MoveRequest moveRequest) throws IOException;

    /**
     * Shoot with selected {@link Submarine}.
     *
     * @param submarine    {@link Submarine}
     * @param shootRequest {@link ShootRequest}
     */
    SimpleResponse shoot(final Submarine submarine, final ShootRequest shootRequest) throws IOException;

    /**
     * Gives back the width and height of the world.
     *
     * @return {@link Position} containing the width and height of the World
     */
    Position size();

    /**
     * Sonar.
     *
     * @param selectedSubmarine {@link Submarine}
     * @return {@link SonarResponse}
     * @throws IOException
     */
    SonarResponse sonar(Submarine selectedSubmarine) throws IOException;
}
