package processor.pipeline;

public class EX_IF_LatchType {
    // boolean isBranchTaken;
    // int offset;

    // public EX_IF_LatchType() {
    //     isBranchTaken = false;
    //     offset = 70000;
    // }
    boolean IS_enable;
	int PC;
	
	public EX_IF_LatchType(){
		IS_enable = false;
	}

	public boolean getIS_enable() {
		return IS_enable;
	}

	public void setIS_enable(boolean iS_enable, int newPC) {
		IS_enable = iS_enable;
		PC = newPC;
	}

	public void setIS_enable(boolean iS_enable) {
		IS_enable = iS_enable;
	}

	public int getPC() {
		return PC;
	}

}

