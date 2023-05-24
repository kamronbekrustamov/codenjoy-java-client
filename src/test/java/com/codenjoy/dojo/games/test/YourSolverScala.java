package com.codenjoy.dojo.games.test;

import com.codenjoy.dojo.client.runner.Language;
import com.codenjoy.dojo.services.Dice;

@Language("scala")
public class YourSolverScala extends YourSolver {
    public YourSolverScala(Dice dice) {
        super(dice);
    }
}
