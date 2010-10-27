package Actor;

public class Actor implements IActor {
	Runtime rt = null;

	public Actor(Runtime rt) {
		this.rt = rt;
	}

	@Override
	public void react() {
		// TODO Auto-generated method stub
	}

	@Override
	public void sendMessage() {
		
		Task t1 = rt.createNonBlockingTask(new Body(){

				@Override
				public void execute(rt,t1) throws Exception {
					react();
					
				}}, Runtime.NO_HINTS);
		
		rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
	}
}
