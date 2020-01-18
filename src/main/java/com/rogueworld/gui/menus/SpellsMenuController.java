package com.rogueworld.gui.menus;

import java.util.EnumMap;

import com.rogueworld.actions.actions.Cast;
import com.rogueworld.actions.spells.Spell;
import com.rogueworld.gui.gamescreen.InputConfig;
import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.AbilitiesC;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.utils.text.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SpellsMenuController {
	
	@FXML public TreeView<Text> spellsList;
	@FXML public TextFlow spellDesc;
	@FXML public TextField searchField;
	
	private EnumMap<Skill, TreeItem<Text>> categories;
	
	@FXML
	public void initialize() {
		categories = createCategories();
		fillList();
		spellsList.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<TreeItem<Text>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<Text>> observable, TreeItem<Text> oldValue, TreeItem<Text> newValue) {
				refreshSpellDesc();
			}
		});
	}
	
	@FXML
	public void handlePressedKeyInSpellsList(KeyEvent event) {
		switch(event.getCode()) {
		case F:
			searchField.setVisible(true);
			searchField.requestFocus();
			break;
		case C:
			searchField.clear();
			fillList();
			break;
		case DOWN:
		case NUMPAD2:
			spellsList.getSelectionModel().selectNext();
			break;
		case UP:
		case NUMPAD8:
			spellsList.getSelectionModel().selectPrevious();
			break;
		case ESCAPE:
			RenderSystem.getInstance().closeSecondaryStage();
			break;
		case ENTER:
			Spell selectedSpell = getSelectedSpell();
			if(selectedSpell.canBeCasted(Main.player)) {
				Cast.castedSpell = selectedSpell;
				RenderSystem.getInstance().closeSecondaryStage();
				InputConfig.setCastInput();
			}
			break;
		default:
			break;
		}
		event.consume();
	}
	
	@FXML
	public void handlePressedKeyInSearchField(KeyEvent event) {
		switch(event.getCode()) {
		case ESCAPE:
			searchField.clear();
			spellsList.requestFocus();
			break;
		case ENTER:
			spellsList.requestFocus();
			break;
		default:
			break;
		}
	}
	
	@FXML
	public void fillList() {
		TreeItem<Text> root = new TreeItem<Text>();
		spellsList.setRoot(root);
		
		categories.values().forEach(branch -> branch.getChildren().clear());
		
		for(Spell spell : Main.player.get(AbilitiesC.class).getSpells().values()) {
			TreeItem<Text> categoryBranch = categories.get(spell.getUsedSkill());
			Text text = new Text(spell.getName());
			if(spell.canBeCasted(Main.player)) {
				text.setFill(Color.WHITE);
			}
			else {
				text.setFill(Color.GREY);
			}
			categoryBranch.getChildren().add(new TreeItem<Text>(text));
		}
		
		categories.values().forEach(branch -> {
			if(!branch.getChildren().isEmpty()) {
				root.getChildren().add(branch);
			}
		});
	}
	
	private EnumMap<Skill, TreeItem<Text>> createCategories() {
		EnumMap<Skill, TreeItem<Text>> map = new EnumMap<>(Skill.class);
		
		Text al = new Text("Aeromancy");
		al.setFill(Color.CRIMSON);
		map.put(Skill.AEROMANCY, new TreeItem<>(al));
		
		Text cl = new Text("Conjuration");
		cl.setFill(Color.CORNFLOWERBLUE);
		map.put(Skill.CONJURATION, new TreeItem<>(cl));
		
		Text crl = new Text("Cryomancy");
		crl.setFill(Color.HOTPINK);
		map.put(Skill.CRYOMANCY, new TreeItem<>(crl));
		
		Text gl = new Text("Geomancy");
		gl.setFill(Color.PERU);
		map.put(Skill.GEOMANCY, new TreeItem<>(gl));
		
		Text hl = new Text("Hemomancy");
		hl.setFill(Color.ROSYBROWN);
		map.put(Skill.HEMOMANCY, new TreeItem<>(hl));
		
		Text hyl = new Text("Hydromancy");
		hyl.setFill(Color.CORAL);
		map.put(Skill.HYDROMANCY, new TreeItem<>(hyl));
		
		Text il = new Text("Illusion");
		il.setFill(Color.YELLOW);
		map.put(Skill.ILLUSION, new TreeItem<>(il));
		
		Text nl = new Text("Necromancy");
		map.put(Skill.NECROMANCY, new TreeItem<>(nl));
		
		Text pl = new Text("Pyromancy");
		map.put(Skill.PYROMANCY, new TreeItem<>(pl));
		
		Text sl = new Text("Summoning");
		sl.setFill(Color.DARKOLIVEGREEN);
		map.put(Skill.SUMMONING, new TreeItem<>(sl));
		
		Text tl = new Text("Transfiguration");
		map.put(Skill.TRANSFIGURAION, new TreeItem<>(tl));
		
		map.values().forEach(ti -> ti.expandedProperty().set(true));
		
		return map;
	}
	
	private Spell getSelectedSpell() {
		return Main.player.get(AbilitiesC.class).getSpell(spellsList.getSelectionModel().getSelectedItem().getValue().getText());
	}
	
	private void refreshSpellDesc() {
		spellDesc.getChildren().clear();
		
		Spell selectedSpell = getSelectedSpell();
		
		if(selectedSpell != null) {
			Text name = new Text(StringUtils.toTitle(selectedSpell.getName()) + "\n\n");
			name.setFill(Color.WHITE);
			name.setFont(Font.font("courier new", FontWeight.BLACK, 20));
			name.setUnderline(true);
			
			Text desc = new Text(selectedSpell.getDescription() + "\n\n");
			desc.setFont(Font.font("courier new", FontWeight.BLACK, 16));
			desc.setFill(Color.WHITE);
			
			spellDesc.getChildren().addAll(name, desc);
		}
	}

}
