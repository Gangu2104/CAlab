package processor.pipeline;

import java.nio.ByteBuffer;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;
import processor.Processor;
import generic.Statistics;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	IF_EnableLatchType IF_EnableLatch;
	MA_RW_LatchType MA_RW_Latch;


	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, IF_EnableLatchType iF_EnableLatch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	private static int twoscompliment(String s) {
		char[] neg_num = s.toCharArray();
		for (int i = 0; i < neg_num.length; i++) {
			if (neg_num[i] == '0') {
				neg_num[i] = '1';
			} 
			else {
				neg_num[i] = '0';
			}
		}
		String s1 = new String(neg_num);
		int ans = Integer.parseInt(s1, 2);
		ans++;
		return ans;
	}

	// public static boolean check(Instruction i,int rs1,int rs2){

	// 	if (i!=null ){
	// 		OperationType k = i.getOperationType();
	// 		int type_operation = k.ordinal();

	// 			if (type_operation<24){
	// 				int dest = i.getDestinationOperand().getValue();
	// 				if (dest==rs1 || dest==rs2){
	// 					return true;
	// 				}
	// 			}
	// 	}

	// 	return false;


	// }
	private static boolean check(Instruction inst, int register1, int register2) 
	{
		if (inst != null && inst.getOperationType() != null)
		{
			int opIndex = inst.getOperationType().ordinal();

			if (opIndex <= 23 ) {
				int dest_reg = inst != null ? inst.getDestinationOperand().getValue() : -1;
				if (register2 == dest_reg || register1 == dest_reg)
				{
					return true;
				}
			} 
			else if ((opIndex == 6 || opIndex == 7) && (register1 == 31 || register2 == 31)) {
				return true;
			}
		}
		return false;
	}


	private void isdatahazard(){
		
			System.out.println("data hazard found");
			IF_EnableLatch.setIF_enable(false);
			OF_EX_Latch.setNothing(true);

		
	}





	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);

			OperationType[] operationtype = OperationType.values();
			String instruction = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			while(instruction.length()!=32){
				instruction = "0" + instruction;
			}
			String opcode = instruction.substring(0, 5);
			int type_operation = Integer.parseInt(opcode, 2);
			OperationType operation = operationtype[type_operation];


			if (type_operation == 24 || type_operation == 25 || type_operation == 26 || type_operation == 27 || type_operation == 28 ) {
				IF_EnableLatch.setIF_enable(false);
			}

			boolean problem=false;
			Instruction ex_stage_inst = OF_EX_Latch.getInstruction();
			Instruction ma_stage_inst = EX_MA_Latch.getInstruction();
			Instruction rw_stage_inst = MA_RW_Latch.getInstruction();


			Instruction inst = new Instruction();
			Operand rs1 = new Operand();
			Operand rs2 = new Operand();
			Operand rd = new Operand();
			int registerNo,registerNo1;
			switch(operation.toString()){
				case "add" : 
				case "sub" : 
				case "mul" : 
				case "div" : 
				case "and" : 
				case "or" : 
				case "xor" : 
				case "slt" : 
				case "sll" : 
				case "srl" : 
				case "sra" :
					{
					rs1.setOperandType(OperandType.Register);
					registerNo1 = Integer.parseInt(instruction.substring(5, 10), 2);
					rs1.setValue(registerNo1);

					
					rs2.setOperandType(OperandType.Register);
					int registerNo2 = Integer.parseInt(instruction.substring(10, 15), 2);
					rs2.setValue(registerNo2);


					if (check(ex_stage_inst,registerNo1,registerNo2) || check(ma_stage_inst,registerNo1,registerNo2) || check(rw_stage_inst,registerNo1,registerNo2)){
						problem=true;
					}
	
					if (problem){
						this.isdatahazard();
						break;
					}




					rd.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(15, 20), 2);
					rd.setValue(registerNo);

					inst.setOperationType(operationtype[type_operation]);
					inst.setSourceOperand1(rs1);
					inst.setSourceOperand2(rs2);
					inst.setDestinationOperand(rd);
					break;}
				case "addi" :
				case "subi" :
				case "muli" :
				case "divi" : 
				case "andi" : 
				case "ori" : 
				case "xori" : 
				case "slti" : 
				case "slli" : 
				case "srli" : 
				case "srai" :
				case "load" :
				case "store":{

					rs1.setOperandType(OperandType.Register);
					registerNo1 = Integer.parseInt(instruction.substring(5, 10), 2);
					rs1.setValue(registerNo1);
					System.out.println("hi "+registerNo1);

					
					rs2.setOperandType(OperandType.Immediate);
					int registerNo2 = Integer.parseInt(instruction.substring(15, 32), 2);
					String imm=instruction.substring(15, 32);
					// imm_val = Integer.parseInt(imm, 2);
					// int imm1;
					if (imm.charAt(0) == '1'){
						registerNo2 = twoscompliment(imm);
						registerNo2 =  registerNo2 * -1;
					}
					rs2.setValue(registerNo2);

					if (check(ex_stage_inst,registerNo1,registerNo2) || check(ma_stage_inst,registerNo1,registerNo2) || check(rw_stage_inst,registerNo1,registerNo2)){
						problem=true;
					}	

					if (problem){
						this.isdatahazard();
						break;
					}


					
					rd.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
					rd.setValue(registerNo);

					inst.setOperationType(operationtype[type_operation]);
					inst.setSourceOperand1(rs1);
					inst.setSourceOperand2(rs2);
					inst.setDestinationOperand(rd);		
					break;		}
				case "beq" : 
				case "bne ": 
				case "blt" : 
				case "bgt" : {
					rs1.setOperandType(OperandType.Register);
					registerNo1 = Integer.parseInt(instruction.substring(5, 10), 2);
					rs1.setValue(registerNo1);

					
					rs2.setOperandType(OperandType.Register);
					int registerNo2 = Integer.parseInt(instruction.substring(10, 15), 2);
					rs2.setValue(registerNo2);

					if (check(ex_stage_inst,registerNo1,registerNo2) || check(ma_stage_inst,registerNo1,registerNo2) || check(rw_stage_inst,registerNo1,registerNo2)){
						problem=true;
					}

					if (problem){
						this.isdatahazard();
						break;
					}
					
					rd.setOperandType(OperandType.Immediate);
					registerNo = Integer.parseInt(instruction.substring(15, 32), 2);
					String imm=instruction.substring(15, 32);
					// imm_val = Integer.parseInt(imm, 2);
					// int imm1;
					if (imm.charAt(0) == '1'){
						registerNo = twoscompliment(imm);
						registerNo =  registerNo* -1;
					}
					// rd.setValue(imm_val);
					rd.setValue(registerNo);

					inst.setOperationType(operationtype[type_operation]);
					inst.setSourceOperand1(rs1);
					inst.setSourceOperand2(rs2);
					inst.setDestinationOperand(rd);	
					break;

				}

				case "jmp" :{
					rs1.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
					rs1.setValue(registerNo);	
					
					
					rd.setOperandType(OperandType.Immediate);
					registerNo = Integer.parseInt(instruction.substring(10, 32), 2);
					String imm=instruction.substring(10, 32);
					// imm_val = Integer.parseInt(imm, 2);
					// int imm1;
					if (imm.charAt(0) == '1'){
						registerNo = twoscompliment(imm);
						registerNo =  registerNo* -1;
					}
					rd.setValue(registerNo);
					inst.setOperationType(operationtype[type_operation]);
					inst.setSourceOperand1(rs1);
					// inst.setSourceOperand2(rs2);
					inst.setDestinationOperand(rd);		
					break;		
	
				}
				default:{
					inst.setOperationType(operationtype[type_operation]);
					IF_EnableLatch.setIF_enable(false);
					break;
				}

			

			}
			

			OF_EX_Latch.setInstruction(inst);
			// IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}





