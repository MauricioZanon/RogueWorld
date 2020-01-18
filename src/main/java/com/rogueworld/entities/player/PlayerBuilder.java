package com.rogueworld.entities.player;

import com.rogueworld.ai.states.PlayerState;
import com.rogueworld.ai.states.StateType;
import com.rogueworld.actions.spells.Dig;
import com.rogueworld.actions.spells.SummonLesserCreature;
import com.rogueworld.actions.spells.TeleportSelf;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.AbilitiesC;
import com.rogueworld.entities.components.BodyC;
import com.rogueworld.entities.components.BodyC.BodyPart;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.GraphicC;
import com.rogueworld.entities.components.HealthC;
import com.rogueworld.entities.components.LightSourceC;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.SkillsC;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.components.StatusEffectsC;
import com.rogueworld.entities.components.VisionC;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Att;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Flag;
import com.rogueworld.entities.main.Type;

import javafx.scene.paint.Color;

public abstract class PlayerBuilder {
	
	public static Entity createBasePlayer() { //TODO test
		if(Main.player != null) return null;
		
		Entity p = new Entity(Type.PLAYER, -1, "player");
		
		p.setAttribute(Att.STR, 10);
		p.setAttribute(Att.CON, 10);
		p.setAttribute(Att.DEX, 10);
		p.setAttribute(Att.CUN, 10);
		p.setAttribute(Att.INT, 10);
		p.setAttribute(Att.WIS, 10);
		p.setAttribute(Att.PER, 10);
		p.setAttribute(Att.DAMAGE, 10);
		p.setAttribute(Att.MOV_SPEED, 10);
		p.setAttribute(Att.ATTACK_SPEED, 10);
		p.setAttribute(Att.CAST_SPEED, 10);
		
		PositionC pos = new PositionC();
		pos.coord = new int[] {0,0,0};
		p.addComponent(pos);
		
		GraphicC gc = new GraphicC();
		gc.ASCII = "@";
		gc.color = Color.WHITE;
		p.addComponent(gc);
		
		HealthC hp = new HealthC();
		hp.setMaxHP(100);
		hp.setCurHP(100);
		hp.setHPreg(0.1f);
		p.addComponent(hp);
		PlayerInfo.CUR_HP.set(hp.getCurHP());
		PlayerInfo.MAX_HP.set(hp.getMaxHP());
		
		AIC AI = new AIC();
		AI.addState(StateType.IDLE, new PlayerState(p));
		AI.setState(StateType.IDLE);
		p.addComponent(AI);
		
		ContainerC inv = new ContainerC();
		inv.add(EntityFactory.createRandom(Type.POTION));
		inv.add(EntityFactory.createRandom(Type.POTION));
		inv.add(EntityFactory.createRandom(Type.POTION));
		inv.add(EntityFactory.createRandom(Type.POTION));
		inv.add(EntityFactory.createRandom(Type.ARMOR));
		inv.add(EntityFactory.createRandom(Type.ARMOR));
		inv.add(EntityFactory.createRandom(Type.ARMOR));
		inv.add(EntityFactory.createRandom(Type.WEAPON));
		
		for(int i = 0; i < 100; i++) {
			inv.add(EntityFactory.create("plank"));
			inv.add(EntityFactory.create("nail"));
			inv.add(EntityFactory.create("hammer"));
		}
		
		p.addComponent(inv);
		
		BodyC body = new BodyC();
		body.add(BodyPart.HEAD);
		body.add(BodyPart.TORSO);
		body.add(BodyPart.L_ARM);
		body.add(BodyPart.R_ARM);
		body.add(BodyPart.L_HAND);
		body.add(BodyPart.R_HAND);
		body.add(BodyPart.L_LEG);
		body.add(BodyPart.R_LEG);
		body.add(BodyPart.L_FOOT);
		body.add(BodyPart.R_FOOT);
		p.addComponent(body);
		
		AbilitiesC ac = new AbilitiesC();
		ac.addSpell(Dig.getInstance());
		ac.addSpell(TeleportSelf.getInstance());
		ac.addSpell(SummonLesserCreature.getInstance());
		p.addComponent(ac);
		
		p.addComponent(new MovementC());
		p.addComponent(new VisionC());
		p.addComponent(new StatusEffectsC());
		p.addComponent(new SkillsC());
		p.addComponent(new LightSourceC());
		
		p.get(SkillsC.class).set(Skill.CARPENTRY, 10);
		p.get(SkillsC.class).set(Skill.SURVIVAL, 10);
		p.get(SkillsC.class).set(Skill.FLETCHERY, 10);
		
		p.getFlags().add(Flag.LIGHT_SOURCE);
		p.setAttribute(Att.LIGHT_INTENSITY, 1.5f);
		
		return p;
	}

}
