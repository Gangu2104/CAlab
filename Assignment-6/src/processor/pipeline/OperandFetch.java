package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;
import processor.Processor;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OperandFetch {
    Processor containingProcessor;
    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;


    public OperandFetch(Processor containingProcessor,
                        IF_OF_LatchType iF_OF_Latch,
                        OF_EX_LatchType oF_EX_Latch,
                        EX_MA_LatchType eX_MA_Latch,
                        MA_RW_LatchType mA_RW_Latch,
                        IF_EnableLatchType if_enableLatch) {
        this.containingProcessor = containingProcessor;
        this.IF_OF_Latch = iF_OF_Latch;
        this.OF_EX_Latch = oF_EX_Latch;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_EnableLatch = if_enableLatch;
    }
    public static int twoscompliment(String s) {
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

    public void performOF() {
        if(OF_EX_Latch.isEX_busy()) IF_OF_Latch.setOF_busy(true);
        else IF_OF_Latch.setOF_busy(false);
        // if(IF_OF_Latch.isOF_enable() == false) OF_EX_Latch.EX_enable = false;
        if(IF_OF_Latch.isOF_enable() && !OF_EX_Latch.isEX_busy())
        {
            OperationType[] operationtype = OperationType.values();
			String instruction = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			while(instruction.length()!=32){
				instruction = "0" + instruction;
			}
			String opcode = instruction.substring(0, 5);
			int type_operation = Integer.parseInt(opcode, 2);
			OperationType operation = operationtype[type_operation];

			Instruction inst = new Instruction();
			Operand rs1 = new Operand();
			Operand rs2 = new Operand();
			Operand rd = new Operand();
			int registerNo;
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
					registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
					rs1.setValue(registerNo);

					
					rs2.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
					rs2.setValue(registerNo);

					
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
					registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
					rs1.setValue(registerNo);

					
					rs2.setOperandType(OperandType.Immediate);
					registerNo = Integer.parseInt(instruction.substring(15, 32), 2);
					String imm=instruction.substring(15, 32);
					// imm_val = Integer.parseInt(imm, 2);
					// int imm1;
					if (imm.charAt(0) == '1'){
						registerNo = twoscompliment(imm);
						registerNo =  registerNo * -1;
					}
					rs2.setValue(registerNo);

					
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
					registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
					rs1.setValue(registerNo);

					
					rs2.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
					rs2.setValue(registerNo);

					
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
					break;
				}
			}
                OF_EX_Latch.isNop = false;
                OF_EX_Latch.setInstruction(inst);
                OF_EX_Latch.insPC = IF_OF_Latch.insPC;
                OF_EX_Latch.setEX_enable(true);
                IF_EnableLatch.setIF_enable(true);

            if(operation.toString()=="end") {
                IF_OF_Latch.setOF_enable(false);
                IF_EnableLatch.setIF_enable(false);
            }
            IF_OF_Latch.setOF_enable(false);
            OF_EX_Latch.setEX_enable(true);
        }
    }

}

