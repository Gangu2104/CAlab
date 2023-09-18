package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		if(EX_MA_Latch.isMA_enable()){
			Instruction instruction = EX_MA_Latch.getInstruction();
			int result_alu = EX_MA_Latch.getALU_result();
			MA_RW_Latch.setALU_result(result_alu);
			OperationType op_type = instruction.getOperationType();
			switch(op_type.toString()){
				case "load":
					int result_load = containingProcessor.getMainMemory().getWord(result_alu);
					MA_RW_Latch.setLoad_result(result_load);
					break;

				case "store":
					int inst_line;
					inst_line=instruction.getSourceOperand1().getValue();
					int val_store = containingProcessor.getRegisterFile().getValue(inst_line);
					containingProcessor.getMainMemory().setWord(result_alu, val_store);
					break;
					
				default:
					break;
			}
			MA_RW_Latch.setInstruction(instruction);
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_enable(false);
		}
	}

}
