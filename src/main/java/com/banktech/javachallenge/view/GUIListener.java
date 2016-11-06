package com.banktech.javachallenge.view;

import com.banktech.javachallenge.view.domain.ViewModel;

import java.util.List;

/**
 * Created by Mihaly on 2016. 11. 05..
 */
public interface GUIListener {
    void refresh(List<ViewModel> turns);
}
