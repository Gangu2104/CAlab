package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
    Processor containingProcessor;
    MA_RW_LatchType MA_RW_Latch;
    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;

    public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType ifOfLatchType, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch) {
        this.containingProcessor = containingProcessor;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_EnableLatch = iF_EnableLatch;
        this.IF_OF_Latch = ifOfLatchType;
        this.OF_EX_Latch = oF_EX_Latch;
        this.EX_MA_Latch = eX_MA_Latch;
    }

    public void performRW() {
        //System.out.println("YessRW");
        //System.out.println(OF_EX_Latch.isEX_busy());
        if (MA_RW_Latch.isRW_enable()) {
            // MA_RW_Latch.RW_enable = false;
            if (!MA_RW_Latch.isNop) {
                Instruction cmd = MA_RW_Latch.getInstruction();

                int alu_output = MA_RW_Latch.getALU_Output();
                OperationType operationType = cmd.getOperationType();
                switch (operationType) {
                    case store:
                    case jmp:
                    case beq:
                    case bne:
                    case blt:
                    case bgt:
                    case nop:
                        break;
                    case load:
                        //System.out.println("RW "+cmd);
                        //int load_output = MA_RW_Latch.getLoad_Output();
                        int DOP = cmd.getDestinationOperand().getValue();
                        //System.out.println("ADDRESS OF LOAD STORING "+DOP);
                        //System.out.println("TO BE STORED "+alu_output);
                        containingProcessor.getRegisterFile().setValue(DOP, alu_output);
                        break;
                    case end:
                        //containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getRegisterFile().getFreezedprogramCounter());
                        Simulator.setSimulationComplete(true);
                        IF_EnableLatch.setIF_enable(false);
                        break;
                    default:
                        //System.out.println("RW "+cmd);
                        DOP = cmd.getDestinationOperand().getValue();
                        containingProcessor.getRegisterFile().setValue(DOP, alu_output);
                        break;
                }
                // Instruction instruction = MA_RW_Latch.getInstruction();
                // int aluResult = MA_RW_Latch.getALU_Output();
                // OperationType op_type = instruction.getOperationType();
                // int rd=0;	
                // switch(op_type.toString()){
                //     case "load":
                //         int loadResult = MA_RW_Latch.getLoad_Output();
                //         rd = instruction.getDestinationOperand().getValue();
                //         containingProcessor.getRegisterFile().setValue(rd, loadResult);
                //         break;
                //     case "end":
                //         Simulator.setSimulationComplete(true);
                //         IF_EnableLatch.setIF_enable(false);
                //         break;
                //     case "store":
                //     case "jmp":
                //     case "beq":
                //     case "bne":
                //     case "blt":
                //     case "bgt":
                //         break;
    
                //     default:
                //         rd = instruction.getDestinationOperand().getValue();
                //         containingProcessor.getRegisterFile().setValue(rd, aluResult);
                //         break;
                // }
                MA_RW_Latch.setRW_enable(false);
                // IF_EnableLatch.setIF_enable(true);
            }
        }
    }

}
