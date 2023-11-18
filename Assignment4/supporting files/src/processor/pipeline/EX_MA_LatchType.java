package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int alu_result;
	Instruction instruction;
	boolean shouldDoNothing;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
		shouldDoNothing = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}
	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	public Instruction getInstruction() {
		return this.instruction;
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
