package com.yvette.santorini.backend.godpowers;

import com.yvette.santorini.backend.godpowers.strategy.*;
import com.yvette.santorini.backend.godpowers.strategy.impl.*;

public class GodFactory {
    public static God create(String name) {
        switch (name.toLowerCase()) {
            case "none":
                return new God(
                    new DefaultMoveStrategy(),
                    new DefaultBuildStrategy(),
                    new DefaultWinConditionStrategy()
                );
            case "demeter":
                return new God(
                    new DefaultMoveStrategy(),
                    new DemeterBuildStrategy(),
                    new DefaultWinConditionStrategy()
                );
            
            case "minotaur":
                return new God(
                    new MinotaurMoveStrategy(),
                    new DefaultBuildStrategy(),
                    new DefaultWinConditionStrategy()
                );
            
            case "hephaestus":
                return new God(
                    new DefaultMoveStrategy(),
                    new HephaestusBuildStrategy(),
                    new DefaultWinConditionStrategy()
                );
            
            case "pan":
                return new God(
                    new DefaultMoveStrategy(),
                    new DefaultBuildStrategy(),
                    new PanWinConditionStrategy()
                );
            

            default:
                return new God(
                    new DefaultMoveStrategy(),
                    new DefaultBuildStrategy(),
                    new DefaultWinConditionStrategy()
                );
        }
    }
}
