package actor;

import aeminium.runtime.implementations.Factory;
import aeminium.runtime.Runtime;

public class AeminiumRuntime {
	static Runtime rt;
	
	public AeminiumRuntime() {
		rt = Factory.getRuntime();
		rt.init();
	}
	
	public void endAeminiumRuntime(){
		rt.shutdown();
	}
}
