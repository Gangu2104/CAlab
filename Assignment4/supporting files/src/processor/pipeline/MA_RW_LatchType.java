package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction instruction;
	int alu_result;
	int load_result;
	boolean shouldDoNothing;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
		shouldDoNothing = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}
	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction inst) {
		instruction = inst;
	}

	public void setLoad_result(int result) {
		load_result = result;
	}

	public int getLoad_result() {
		return load_result;
	}

	public int getALU_result() {
		return alu_result;
	}

	public void setALU_result(int result) {
		alu_result = result;
	}

	public boolean getNothing() {
		return shouldDoNothing;
	}

	public void setNothing(boolean x) {
		shouldDoNothing = x;
	}

}
