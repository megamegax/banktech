package com.banktech.javachallenge.logic;

import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.view.domain.ViewModel;


public interface GameLogic {

    ViewModel sonar(ViewModel currentViewModel, Long submarineId);

    ViewModel step(ViewModel currentViewModel, Long submarineId,Position fallbackPosition,boolean avoidFriend);

}
