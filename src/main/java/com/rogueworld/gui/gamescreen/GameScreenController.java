package com.rogueworld.gui.gamescreen;

import com.rogueworld.ai.eventsystem.EventSystem;
import com.rogueworld.actions.actions.ActionType;
import com.rogueworld.actions.actions.Build;
import com.rogueworld.actions.actions.Bump;
import com.rogueworld.actions.actions.Cast;
import com.rogueworld.actions.actions.EndTurn;
import com.rogueworld.actions.actions.Examine;
import com.rogueworld.actions.actions.FollowPath;
import com.rogueworld.actions.actions.Jump;
import com.rogueworld.actions.actions.Kick;
import com.rogueworld.actions.actions.PickUp;
import com.rogueworld.actions.actions.Shoot;
import com.rogueworld.actions.actions.Throw;
import com.rogueworld.actions.actions.Turn;
import com.rogueworld.actions.actions.Use;
import com.rogueworld.actions.effects.Effects;
import com.rogueworld.gui.itemmenus.MenuFactory;
import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Type;
import com.rogueworld.entities.player.HungerLevel;
import com.rogueworld.entities.player.PlayerInfo;
import com.rogueworld.entities.player.ThirstLevel;
import com.rogueworld.utils.persistency.StateSaver;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;
import com.rogueworld.world.time.Clock;
import com.rogueworld.world.world.Direction;

import animatefx.animation.FadeIn;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class GameScreenController {

	@FXML
	public Canvas entitiesLayer;
	@FXML
	public Canvas darknessLayer;
	@FXML
	public Canvas selectionLayer;

	@FXML
	public VBox sideBar;
	@FXML
	public VBox resourceBars;
	@FXML
	public ScrollPane textContainer;
	@FXML
	public TextFlow console;
	@FXML
	public Label clockLabel;
	@FXML
	public Label viewedEntityName;

	public void initialize() {

		// LAYERS

		double sceneHeight = RenderSystem.getInstance().getSceneHeight();
		DrawUtils.tileSize = (int) (sceneHeight / DrawUtils.tileQuantity);
		entitiesLayer.setHeight(sceneHeight);
		entitiesLayer.setWidth(sceneHeight);

		entitiesLayer.focusedProperty().addListener((observedValue, oldValue, newValue) -> {
			if (!oldValue && newValue) {
				InputConfig.setGoToInput();
			}
		});

		EventSystem.getIsPlayersTurnProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue && !oldValue) {
				SelectionDrawer.reset();
				EntityDrawer.draw();
			}
		});

		EntityDrawer.initialize(entitiesLayer.getGraphicsContext2D());

		darknessLayer.setHeight(sceneHeight);
		darknessLayer.setWidth(sceneHeight);
		LightningDrawer.initialize(darknessLayer.getGraphicsContext2D());

		selectionLayer.setHeight(sceneHeight);
		selectionLayer.setWidth(sceneHeight);
		selectionLayer.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!oldValue && newValue) {
				DrawUtils.selectedTile = Main.player.get(PositionC.class).getTile();
				SelectionDrawer.draw();
			}
		});
		SelectionDrawer.initialize(selectionLayer.getGraphicsContext2D());

		// SIDEBAR

		sideBar.setPrefWidth(RenderSystem.getInstance().getSceneWidth() - RenderSystem.getInstance().getSceneHeight());

		ResourceBar hpBar = new ResourceBar("Health", Color.FIREBRICK, PlayerInfo.CUR_HP);
		resourceBars.getChildren().add(hpBar);

		console.setPrefWidth(RenderSystem.getInstance().getSceneWidth() - RenderSystem.getInstance().getSceneHeight());
		if (Console.messages != null) {
			console.getChildren().addAll(Console.messages);
		}
		Console.messages = console.getChildren();
		console.getChildren()
				.addListener((ListChangeListener<Node>) ((change) -> textContainer.setVvalue(Double.MAX_VALUE)));

		clockLabel.setText(Clock.getHour());
		clockLabel.textProperty().bind(Clock.hourProperty);

		Text hungerText = new Text();
		hungerText.setFont(Font.font("Courier New", FontWeight.BLACK, 15));
		PlayerInfo.HUNGER.addListener((observedValue, oldValue, newValue) -> {
			HungerLevel level = HungerLevel.getLevel(newValue.floatValue());
			String text = level.toString().replace("_", " ");
			if (!text.equals(hungerText.getText())) {
				new FadeIn(hungerText).play();
				hungerText.setText(text);
				hungerText.setFill(level.color);
			}
		});
		sideBar.getChildren().add(hungerText);

		Text thirstText = new Text();
		thirstText.setFont(Font.font("Courier New", FontWeight.BLACK, 15));
		PlayerInfo.THIRST.addListener((observedValue, oldValue, newValue) -> {
			ThirstLevel level = ThirstLevel.getLevel(newValue.floatValue());
			String text = level.toString().replace("_", " ");
			if (!text.equals(thirstText.getText())) {
				new FadeIn(thirstText).play();
				thirstText.setText(text);
				thirstText.setFill(level.color);
			}
		});
		sideBar.getChildren().add(thirstText);

		InputConfig.setChangeListener((observedValue, oldValue, newValue) -> {
			if (!oldValue.equals(newValue)) {
				if (!newValue.equals(MouseAction.GO_TO)) {
					selectionLayer.requestFocus();
				} else {
					entitiesLayer.requestFocus();
				}
			}
		});

		EntityDrawer.draw();
	}

	@FXML
	public void handleMouseMoved(MouseEvent e) {
		if (EventSystem.isPlayersTurn()) {
			DrawUtils.selectedTile = getTileUnderTheMouse(e.getX(), e.getY());
			SelectionDrawer.draw();
		}
	}

	@FXML
	public void handleMouseClicked(MouseEvent e) {
		Tile selectedTile = DrawUtils.selectedTile;
		if (EventSystem.isPlayersTurn()) {
			SelectionDrawer.reset();
			switch (e.getButton()) {
			case MIDDLE:
				System.out.println(Direction.get(Main.player.get(PositionC.class).getTile(), selectedTile));
				break;
			case PRIMARY:
				EntityDrawer.draw();
				executeAction();
				break;
			case SECONDARY:
				System.out.println(Map.getChunk(selectedTile).getCoord()[0]);
				System.out.println(Map.getChunk(selectedTile).getCoord()[1]);
				System.out.println();
				break;
			default:
				break;
			}
		}
		e.consume();
	}

	@FXML
	public void handlePressedKeyOnEntityLayer(KeyEvent key) {
		if (!EventSystem.isPlayersTurn())
			return;

		PositionC playerPos = Main.player.get(PositionC.class).clone();

		switch (key.getCode()) {
		case A:
			// activate
			break;
		case B:
			if (key.isShiftDown()) {
				RenderSystem.getInstance().changeScene("BuildMenu.fxml", true);
			}
			break;
		case C:
			if (key.isShiftDown()) {
				RenderSystem.getInstance().changeScene("CraftMenu.fxml", true);
			}
			break;
		case D:
			MenuFactory.openDropMenu();
			break;
		case E:
			if (key.isShiftDown()) {
				MenuFactory.openEatMenu();
			} else {
				InputConfig.setExamineInput();
			}
			break;
		case F:
			InputConfig.setShootInput();
			break;
		case I:
			MenuFactory.openInventory();
			break;
		case J:
			InputConfig.setJumpInput();
			break;
		case K:
			InputConfig.setKickInput();
			break;
		case M:
			// Mapa
			break;
		case Q:
			if (key.isShiftDown()) {
				StateSaver.getInstance().saveGameState();
			} else {
				MenuFactory.openQuaffMenu();
			}
			break;
		case S:
			if (key.isShiftDown()) {
				RenderSystem.getInstance().changeScene("SlaughterMenu.fxml", true);
			} else {
				RenderSystem.getInstance().changeScene("SpellsMenu.fxml", true);
			}
			break;
		case T:
			MenuFactory.openThrowMenu();
			break;
		case W:
			if (key.isShiftDown()) {
				MenuFactory.openWearMenu();
			} else {
				MenuFactory.openWieldMenu();
			}
			break;
		case PAGE_UP:
			// subir consola
			break;
		case PAGE_DOWN:
			// bajar consola
			break;
		case SPACE:
			InputConfig.setQuickUseInput();
			break;
		case COMMA:
			PickUp.execute(Main.player);
			break;
		case LESS:
			// TODO: crear una accion para esto
			if (key.isShiftDown()) {
				playerPos.coord[2]++;
			} else {
				playerPos.coord[2]--;
			}
			Effects.move(Main.player, playerPos.getTile());
			EndTurn.execute(Main.player, ActionType.WALK);
			break;
		case NUMPAD1:
			if (key.isControlDown())
				Turn.execute(Main.player, Direction.SW);
			else
				Bump.execute(playerPos, Direction.SW, !key.isAltDown());
			break;
		case NUMPAD2:
			if (key.isControlDown())
				Turn.execute(Main.player, Direction.S);
			else
				Bump.execute(playerPos, Direction.S, !key.isAltDown());
			break;
		case NUMPAD3:
			if (key.isControlDown())
				Turn.execute(Main.player, Direction.SE);
			else
				Bump.execute(playerPos, Direction.SE, !key.isAltDown());
			break;
		case NUMPAD4:
			if (key.isControlDown())
				Turn.execute(Main.player, Direction.W);
			else
				Bump.execute(playerPos, Direction.W, !key.isAltDown());
			break;
		case NUMPAD5:
			EndTurn.execute(Main.player, ActionType.WAIT);
			break;
		case NUMPAD6:
			if (key.isControlDown())
				Turn.execute(Main.player, Direction.E);
			else
				Bump.execute(playerPos, Direction.E, !key.isAltDown());
			break;
		case NUMPAD7:
			if (key.isControlDown())
				Turn.execute(Main.player, Direction.NW);
			else
				Bump.execute(playerPos, Direction.NW, !key.isAltDown());
			break;
		case NUMPAD8:
			if (key.isControlDown())
				Turn.execute(Main.player, Direction.N);
			else
				Bump.execute(playerPos, Direction.N, !key.isAltDown());
			break;
		case NUMPAD9:
			if (key.isControlDown())
				Turn.execute(Main.player, Direction.NE);
			else
				Bump.execute(playerPos, Direction.NE, !key.isAltDown());
			break;
		case ESCAPE:
			System.exit(0);
			break;
		default:
			break;
		}
		key.consume();
	}

	@FXML
	public void handlePressedKeyOnSelectionLayer(KeyEvent key) {
		PositionC playerPos = Main.player.get(PositionC.class);
		Tile selectedTile = DrawUtils.selectedTile;
		Tile newSelectedTile = null;
		int maxDistance = InputConfig.getMaxDistance();

		KeyCode code = key.getCode();
		switch (key.getCode()) {
		case NUMPAD1:
			newSelectedTile = Map.getTile(maxDistance == 0 ? playerPos.getTile() : selectedTile, Direction.SW);
			break;
		case NUMPAD2:
			newSelectedTile = Map.getTile(maxDistance == 0 ? playerPos.getTile() : selectedTile, Direction.S);
			break;
		case NUMPAD3:
			newSelectedTile = Map.getTile(maxDistance == 0 ? playerPos.getTile() : selectedTile, Direction.SE);
			break;
		case NUMPAD4:
			newSelectedTile = Map.getTile(maxDistance == 0 ? playerPos.getTile() : selectedTile, Direction.W);
			break;
		case NUMPAD6:
			newSelectedTile = Map.getTile(maxDistance == 0 ? playerPos.getTile() : selectedTile, Direction.E);
			break;
		case NUMPAD7:
			newSelectedTile = Map.getTile(maxDistance == 0 ? playerPos.getTile() : selectedTile, Direction.NW);
			break;
		case NUMPAD8:
			newSelectedTile = Map.getTile(maxDistance == 0 ? playerPos.getTile() : selectedTile, Direction.N);
			break;
		case NUMPAD9:
			newSelectedTile = Map.getTile(maxDistance == 0 ? playerPos.getTile() : selectedTile, Direction.NE);
			break;
		case ENTER:
			SelectionDrawer.reset();
			executeAction();
			key.consume();
			return;
		default:
			SelectionDrawer.reset();
			entitiesLayer.requestFocus();
			key.consume();
			return;
		}

		if (code != KeyCode.ESCAPE && code != KeyCode.ENTER && maxDistance == 0) {
			DrawUtils.selectedTile = newSelectedTile;
			executeAction();
			key.consume();
			return;
		}

		if (Map.getDistance(playerPos, newSelectedTile.pos) <= maxDistance) {
			DrawUtils.selectedTile = newSelectedTile;
			SelectionDrawer.draw();
		}

		key.consume();
	}

	private void executeAction() {
		int maxDistance = InputConfig.getMaxDistance();
		if ((maxDistance == 0 && Map.isAdjacent(DrawUtils.selectedTile, t -> t.has(Type.PLAYER))
				|| Map.getDistance(Main.player.get(PositionC.class), DrawUtils.selectedTile.pos) <= maxDistance)) {
			switch (InputConfig.getMouseAction()) {
			case BUILD:
				Build.execute(DrawUtils.selectedTile);
				break;
			case CAST:
				Cast.execute(Main.player, DrawUtils.selectedTile);
				break;
			case EXAMINE:
				Examine.execute(DrawUtils.selectedTile);
				break;
			case GO_TO:
				FollowPath.execute(Main.player, DrawUtils.selectedTile);
				break;
			case JUMP:
				Jump.execute(Main.player, DrawUtils.selectedTile);
				break;
			case KICK:
				Kick.execute(Main.player, DrawUtils.selectedTile);
				break;
			case QUICK_USE:
				Use.execute(DrawUtils.selectedTile);
				break;
			case SHOOT:
				Shoot.execute(Main.player, DrawUtils.selectedTile);
				break;
			case THROW:
				Throw.execute(Main.player, DrawUtils.selectedTile);
				break;
			}

			entitiesLayer.requestFocus();
		}
	}

	private Tile getTileUnderTheMouse(double x, double y) {
		PositionC pos00 = DrawUtils.getPos00();
		pos00.coord[0] += (int) (x / DrawUtils.tileSize);
		pos00.coord[1] += (int) (y / DrawUtils.tileSize);

		return pos00.getTile();
	}

}
