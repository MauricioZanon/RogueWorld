package com.rogueworld.entities.components;

public class RandomContentC extends Component{
	
	/** String con el nombre y la posibilidad de que aparezca cada item */
	public String randomContent = "";
	
	public RandomContentC(){
		isShared = true;
	}

	@Override
	public Component clone() {
		RandomContentC c = new RandomContentC();
		c.randomContent = randomContent;
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		return randomContent.equals(((RandomContentC) comp).randomContent);
	}

}
