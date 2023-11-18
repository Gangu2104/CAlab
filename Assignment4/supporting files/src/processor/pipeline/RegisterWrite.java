package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Simulator;
import processor.Processor;

import generic.Statistics;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{

		if(MA_RW_Latch.isRW_enable() && !MA_RW_Latch.getNothing()){

			Statistics.setNumberOfRegisterWriteInstructions(Statistics.getNumberOfRegisterWriteInstructions() + 1);

			Instruction instruction = MA_RW_Latch.getInstruction();
			int aluResult = MA_RW_Latch.getALU_result();
			OperationType op_type = instruction.getOperationType();
				
			switch(op_type.toString()){
				case "load":
					int loadResult = MA_RW_Latch.getLoad_result();
					int rd = instruction.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(rd, loadResult);
					break;
				case "end":
					Simulator.setSimulationComplete(true);
					break;
				case "store":
				case "jmp":
				case "beq":
				case "bne":
				case "blt":
				case "bgt":
					break;

				default:
					rd = instruction.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(rd, aluResult);
					break;
			}		
			if(op_type.ordinal()!= 29){
				// MA_RW_Latch.setRW_enable(false);
				IF_EnableLatch.setIF_enable(true);
			}

		}
		else if(MA_RW_Latch.getNothing()){
			MA_RW_Latch.setNothing(false);
		} 

	}

}

