package com.rogueworld.application;

import com.almasb.fxgl.app.FXGLMenu;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuType;
import com.almasb.fxgl.app.SceneFactory;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MenuSampleApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setMenuEnabled(true);

        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new MyMenu(MenuType.GAME_MENU);
            }
        });
    }

    public class MyMenu extends FXGLMenu {
        public MyMenu(MenuType type) {
            super(type);
        }

        @Override
        protected Button createActionButton(String name, Runnable action) {
            return new Button(name);
        }

        @Override
        protected Button createActionButton(StringBinding name, Runnable action) {
            return new Button(name.get());
        }

        @Override
        protected Node createBackground(double width, double height) {
            return new Rectangle(width, height, Color.GRAY);
        }

        @Override
        protected Node createTitleView(String title) {
            return new Text(title);
        }

        @Override
        protected Node createVersionView(String version) {
            return new Text(version);
        }

        @Override
        protected Node createProfileView(String profileName) {
            return new Text(profileName);
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}