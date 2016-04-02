package com.rocketfool.rocketgame.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.rocketfool.rocketgame.entity.game.EntityManager;
import com.rocketfool.rocketgame.entity.game.Map;
import com.rocketfool.rocketgame.entity.game.Planet;
import com.rocketfool.rocketgame.entity.game.Player;
import com.rocketfool.rocketgame.level.trigger.PositionTrigger;

/**
 * Created by pythech on 02/04/16.
 */
public class Level1 extends Level {
    public Level1() {
        super();

        int width = Gdx.graphics.getWidth() * 100;
        int height = Gdx.graphics.getHeight() * 100;

        this.player = new Player(0, 0, world);
        this.entityManager = new EntityManager(player, new Map(width, height));

        addTriggers();
        addPlanets();
    }

    private void addPlanets() {
        entityManager.addPlanet(new Planet(75, 75, 1e4f, 50, world));
    }

    private void addTriggers() {
        triggers.add(new PositionTrigger(10, 10, 10, player) {
            @Override
            public void triggerPerformed() {
                System.out.println("omg");
            }
        });
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
