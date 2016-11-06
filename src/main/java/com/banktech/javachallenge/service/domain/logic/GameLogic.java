package com.banktech.javachallenge.service.domain.logic;

import java.io.IOException;

import com.banktech.javachallenge.view.ViewModel;

public interface GameLogic {
    
    void step(ViewModel currentViewModel) throws IOException;

}
