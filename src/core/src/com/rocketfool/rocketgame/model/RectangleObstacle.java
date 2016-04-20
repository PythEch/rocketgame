package com.rocketfool.rocketgame.model;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Class for all objects with physical properties and a rectangular shape. Eg. Unplayed satellites.
 * Yaman, 20.04.2016, 23.02
 */
public class RectangleObstacle extends SolidObject {

    private int type;
    public static final int SMALL = 0;
    public static final int MEDIUM = 1;
    public static final int LARGE = 2;

    private int numPoints;
    private float[] dists;

    public RectangleObstacle(float x, float y, int type) {

        this.x = x;
        this.y = y;
        this.type = type;

        if(type == SMALL) {
            numPoints = 8;
            width = height = 12;
            speed = MathUtils.random(70, 100);
        }
        else if(type == MEDIUM) {
            numPoints = 10;
            width = height = 20;
            speed = MathUtils.random(50, 60);
        }
        else if(type == LARGE) {
            numPoints = 12;
            width = height = 40;
            speed = MathUtils.random(20, 30);
        }

        rotationSpeed = MathUtils.random(-1, 1);
        int radius = width / 2;

        radians = MathUtils.random(-1, 1);
        dx = MathUtils.cos(radians) * speed;
        dy = MathUtils.sin(radians) * speed;

        shapex = new float[numPoints];
        shapey = new float[numPoints];
        dists = new float[numPoints];

        for(int i=0; i<numPoints; i++) {
            dists[i] = MathUtils.random(radius/2, radius);
        }


    }

    @Override
    public void update(float deltaTime) {

        x += dx * deltaTime;
        y += dy * deltaTime;

        radians += rotationSpeed * deltaTime;

        wrap();

    }

    public void draw(ShapeRenderer shapeR) {
        shapeR.setColor(1, 1, 1, 1);
        shapeR.begin(ShapeRenderer.ShapeType.Line);

        for(int i=0, j=shapex.length-1; i<shapex.length; j=i++) {
            shapeR.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        }
        shapeR.end();
    }
}
