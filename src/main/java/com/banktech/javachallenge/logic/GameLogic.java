package com.banktech.javachallenge.logic;

import com.banktech.javachallenge.view.domain.ViewModel;

import java.io.IOException;


public interface GameLogic {
    
    ViewModel step(ViewModel currentViewModel) throws IOException;

}
