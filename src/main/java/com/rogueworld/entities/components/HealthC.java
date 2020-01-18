package com.rogueworld.entities.components;

public class HealthC extends Component{
	
	private float maxHP = 20;
	private float curHP = 20;
	private float HPreg = 0.1f;
	
	public HealthC() {
		isShared = false;
	}
	
	public void regenerate() {
		changeCurHP(HPreg);
	}
	
	public float getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(float maxHP) {
		this.maxHP = maxHP;
	}

	public void setCurHP(float curHP) {
		this.curHP = curHP;
	}
	
	public float getCurHP() {
		return curHP;
	}

	public void changeCurHP(float quantity) {
		curHP += quantity;
		if(curHP > maxHP)
			curHP = maxHP;
	}

	public float getHPreg() {
		return HPreg;
	}

	public void setHPreg(float hPreg) {
		HPreg = hPreg;
	}

	@Override
	public HealthC clone() {
		HealthC comp = new HealthC();
		comp.maxHP = maxHP;
		comp.curHP = curHP;
		comp.HPreg = HPreg;
		return comp;
	}
	
	@Override
	public void serialize(StringBuilder sb) {
		sb.append("HEA:" + curHP);
	}
	
	@Override
	public void deserialize(String info) {
		curHP = Float.parseFloat(info);
	}

	@Override
	public boolean equals(Component comp) {
		HealthC c = (HealthC) comp;
		return maxHP == c.maxHP && curHP == c.curHP && HPreg == c.HPreg;
	}

}
