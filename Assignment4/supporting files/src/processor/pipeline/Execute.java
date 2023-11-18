package processor.pipeline;

import java.util.Arrays;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;
import processor.Processor;
import generic.Statistics;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;

	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;

	}
	
	public void performEX()
	{

		if(OF_EX_Latch.isEX_enable() && !OF_EX_Latch.getNothing())
		{
			Instruction instruction = OF_EX_Latch.getInstruction();
			System.out.println(instruction);
			EX_MA_Latch.setInstruction(instruction);
			OperationType op_type = instruction.getOperationType();
			int opcode = Arrays.asList(OperationType.values()).indexOf(op_type);



			if (opcode<=29 && opcode>=24){

				Statistics.setNumberOfBranchTaken(Statistics.getNumberOfBranchTaken() + 2);

				IF_EnableLatch.setIF_enable(false);
				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(false);
			}




			int currentPC = containingProcessor.getRegisterFile().getProgramCounter() - 1;

			int aluResult = 0;

			if(opcode % 2 == 0 && opcode < 21){
				int rs1 = containingProcessor.getRegisterFile().getValue(
					instruction.getSourceOperand1().getValue());
				int rs2 = containingProcessor.getRegisterFile().getValue(
					instruction.getSourceOperand2().getValue());

				switch(op_type.toString()){
					case "add":
						aluResult = rs1 + rs2;
						break;
					case "sub":
						aluResult = rs1 - rs2;
						break;
					case "mul":
						aluResult = rs1 * rs2;
						break;
					case "div":
						aluResult = rs1 / rs2;
						int remainder = rs1 % rs2;
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case "and":
						aluResult = rs1 & rs2;
						break;
					case "or":
						aluResult = rs1 | rs2;
						break;
					case "xor":
						aluResult = rs1 ^ rs2;
						break;
					case "slt":
						if(rs1 < rs2)
							aluResult = 1;
						else
							aluResult = 0;
						break;
					case "sll":
						aluResult = rs1 << rs2;
						break;
					case "srl":
						aluResult = rs1 >>> rs2;
						break;
					case "sra":
						aluResult = rs1 >> rs2;
						break;
					default:
						break;
				}
			}
			else if( opcode < 23){
				int i = instruction.getSourceOperand1().getValue();
				int op1 = containingProcessor.getRegisterFile().getValue(i);
				int op2 = instruction.getSourceOperand2().getValue();

				switch(op_type.toString()){
					case "addi":
						aluResult = op1 + op2;
						break;
					case "subi":
						aluResult = op1 - op2;
						break;
					case "muli":
						aluResult = op1 * op2;
						break;
					case "divi":
						aluResult = op1 / op2;
						int remainder = op1 % op2;
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case "andi":
						aluResult = op1 & op2;
						break;
					case "ori":
						aluResult = op1 | op2;
						break;
					case "xori":
						aluResult = op1 ^ op2;
						break;
					case "slti":
						if(op1 < op2)
							aluResult = 1;
						else
							aluResult = 0;
						break;
					case "slli":
						aluResult = op1 << op2;
						break;
					case "srli":
						aluResult = op1 >>> op2;
						break;
					case "srai":
						aluResult = op1 >> op2;
						break;
					case "load":
						aluResult = op1 + op2;
						break;
					default:
						break;
				}
			}
			else if(opcode == 23){
				int op1 = containingProcessor.getRegisterFile().getValue(
					instruction.getDestinationOperand().getValue());
				int op2 = instruction.getSourceOperand2().getValue();
				aluResult = op1 + op2;
			}
			else if(opcode == 24){
				OperandType optype = instruction.getDestinationOperand().getOperandType();
				int imm = 0;
				if (optype == OperandType.Register){
					imm = containingProcessor.getRegisterFile().getValue(
						instruction.getDestinationOperand().getValue());
				}
				else{
					imm = instruction.getDestinationOperand().getValue();
				}
				aluResult = imm + currentPC;
				EX_IF_Latch.setIS_enable(true, aluResult);
			}
			else if(opcode < 29){
				int op1 = containingProcessor.getRegisterFile().getValue(
					instruction.getSourceOperand1().getValue());
				int op2 = containingProcessor.getRegisterFile().getValue(
					instruction.getSourceOperand2().getValue());
				int imm = instruction.getDestinationOperand().getValue();
				// System.out.println("bsbisbnvv");
				switch(op_type.toString()){
					case "beq":
						if(op1 == op2){
							aluResult = imm + currentPC;
							EX_IF_Latch.setIS_enable(true, aluResult);
						}
						break;
					case "bne":
						if(op1 != op2){
							aluResult = imm + currentPC;
							EX_IF_Latch.setIS_enable(true, aluResult);
						}

						break;
					case "blt":
						if(op1 < op2){
							aluResult = imm + currentPC;
							EX_IF_Latch.setIS_enable(true, aluResult);
							// System.out.println("bvauyb");
						}
						break;
					case "bgt":
						if(op1 > op2){
							aluResult = imm + currentPC;
							EX_IF_Latch.setIS_enable(true, aluResult);
						}
						break;
					default:
						break;
				}
			}
			System.out.println("aluResult" + Integer.toString(aluResult));
			EX_MA_Latch.setALU_result(aluResult);

			// OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);
			
		}
		else if(OF_EX_Latch.getNothing()){
			EX_MA_Latch.setNothing(true);
			OF_EX_Latch.setNothing(false);
			EX_MA_Latch.setInstruction(null);
		}

	}

}

