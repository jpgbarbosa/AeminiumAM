package dinningPhilosophers;

import actor.Actor;
import annotations.writable;

class TableA extends Actor{
	@writable
	static Fork [] forks;
	@writable
	static PhilosopherA [] phils;
	
	TableA(PhilosopherA [] phils,Fork [] forks){
		super();
		this.phils = phils;
		this.forks = forks;
	}
	
	@Override
	protected void react(Object obj) {
		
		try{
			MessageActionA msgA = (MessageActionA)obj;
			if(msgA.msg.equals("take")){
				for(int i=1; i< phils.length+1; i++){
					if(msgA.owner==phils[i%forks.length]){
						if(forks[i%forks.length].checkAvailability() && forks[(i-1)].checkAvailability()){
							forks[i%forks.length].setAvailability(false);
							forks[(i-1)%forks.length].setAvailability(false);
							msgA.owner.sendMessage(new Reply(true));
						} else {
							msgA.owner.sendMessage(new Reply(false));
						}
						break;
					}
				}
			} else if(msgA.msg.equals("finished")){
				for(int i=1; i< phils.length+1; i++){
					if(msgA.owner==phils[i%forks.length]){
						forks[i%forks.length].setAvailability(true);
						forks[(i-1)].setAvailability(true);
					}
				}
			}
		}catch (Exception e){
			System.out.println(e);
		}
	}
}