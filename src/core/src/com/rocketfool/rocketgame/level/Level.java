package com.rocketfool.rocketgame.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.rocketfool.rocketgame.Drawable;
import com.rocketfool.rocketgame.Updatable;
import com.rocketfool.rocketgame.entity.game.EntityManager;
import com.rocketfool.rocketgame.entity.game.Player;
import com.rocketfool.rocketgame.level.trigger.Trigger;

/**
 * Created by pythech on 02/04/16.
 */
public abstract class Level implements Updatable, Drawable {
    protected EntityManager entityManager;
    protected Player player;
    protected World world;
    protected Array<Trigger> triggers;

    public Level() {
        this.world = new World(new Vector2(0, 0), true);
        this.triggers = new Array<Trigger>();
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void update(float deltaTime) {
        for (Trigger trigger : triggers) {
            if (trigger.isTriggered()) {
                trigger.triggerPerformed();
            }
        }

        entityManager.update(deltaTime);

        world.step(1 / 60f, 6, 2);
    }

    @Override
    public void draw(SpriteBatch batch) {
        entityManager.draw(batch);
    }
}
