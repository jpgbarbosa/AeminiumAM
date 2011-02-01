package unused.examples.gameOfLife_v2.messages;

import unused.examples.gameOfLife_v2.Quadrant;

public class AskBoundary {
	public Quadrant actor;
	public int borderIndex;
	public int round;
	
	public AskBoundary(Quadrant quadrant, int i, int round) {
		 actor = quadrant;
		 borderIndex = i;
		 this.round = round;
	}

}
