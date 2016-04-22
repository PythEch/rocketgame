package com.rocketfool.rocketgame.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;

import java.io.FileNotFoundException;

public class CutsceneScreen extends ApplicationAdapter {
    public OrthographicCamera cam;
    public CameraInputController inputController;
    public ModelInstance instance;
    public Environment environment;

    public VideoPlayer videoPlayer;
    public Mesh mesh;

    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 target = new Vector3();

    @Override
    public void create() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        /*cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.setToOrtho(false);
        cam.lookAt(0, 0, 0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();*/


        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        videoPlayer.resize(1280, 720);

      //  videoPlayer = VideoPlayerCreator.createVideoPlayer(new FitViewport(1280, 720, cam));
        try {
            videoPlayer.play(Gdx.files.internal("test.webm"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        inputController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(new InputMultiplexer());
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_BACK);
    }

    @Override
    public void render() {
        inputController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (!videoPlayer.render()) { // As soon as the video is finished, we start the file again using the same player.
            try {
                videoPlayer.play(Gdx.files.internal("test.webm"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dispose() {
    }

    public boolean needsGL20() {
        return true;
    }

    public void resume() {
    }

    public void resize(int width, int height) {
    }

    public void pause() {
    }
}