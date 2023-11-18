package processor.pipeline;

import generic.*;
import generic.Instruction.OperationType;
import processor.Clock;
import processor.Processor;
import processor.memorysystem.Cache;
import generic.Event.EventType;

public class MemoryAccess implements Element{
    Processor containingProcessor;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;

    Cache cache;

    public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, Cache cache) {
        this.containingProcessor = containingProcessor;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
        this.IF_OF_Latch = iF_OF_Latch;
        this.OF_EX_Latch = oF_EX_Latch;
        this.cache = cache;
    }

    public void performMA() {
        //System.out.println("YessMA");
        if(EX_MA_Latch.isMA_enable() && !EX_MA_Latch.isMA_busy()) {
            if(EX_MA_Latch.isNop) {
                MA_RW_Latch.isNop = true;
            }
            else {
                MA_RW_Latch.isNop = false;
                MA_RW_Latch.insPC = EX_MA_Latch.insPC;
                Instruction instruction = EX_MA_Latch.getInstruction();
                int result_alu = EX_MA_Latch.getALU_result();
                MA_RW_Latch.setALU_Output(result_alu);

                MA_RW_Latch.setInstruction(instruction);
                OperationType op_type = instruction.getOperationType();
                if (instruction != null) {
			    switch(op_type.toString()){
				case "load":
                     EX_MA_Latch.setMA_busy(true);
                        Simulator.getEventQueue().addEvent(
                                new MemoryReadEvent(
                                        Clock.getCurrentTime(),
                                        this,
                                        this.cache, result_alu)
                        );
                        EX_MA_Latch.setMA_enable(false);
					break;

				case "store":
					int inst_line;
					inst_line=instruction.getSourceOperand1().getValue();
					int val_store = containingProcessor.getRegisterFile().getValue(inst_line);
                    EX_MA_Latch.setMA_busy(true);
                    Simulator.storeresp = Clock.getCurrentTime();
                    Simulator.getEventQueue().addEvent(
                            new MemoryWriteEvent(
                                    Clock.getCurrentTime() + this.cache.latency,
                                    this,
                                    this.cache,
                                    result_alu,
                                    val_store)
                    );
                    EX_MA_Latch.setMA_enable(false);
					// containingProcessor.getMainMemory().setWord(result_alu, val_store);
					break;
                default:
					break;
                }
              
            }
            EX_MA_Latch.setMA_enable(false);
            if(op_type.toString()=="end") {
                EX_MA_Latch.setMA_enable(false);
            }
            MA_RW_Latch.setRW_enable(true);
            }}
    }

    @Override
    public void handleEvent(Event e) {
        if(e.getEventType() == EventType.MemoryResponse) {
            MemoryResponseEvent event = (MemoryResponseEvent) e ;
            MA_RW_Latch.setALU_Output(event.getValue());
            MA_RW_Latch.insPC = EX_MA_Latch.insPC;
            MA_RW_Latch.setRW_enable(true);
            EX_MA_Latch.setMA_busy(false);
        }
        else {
            EX_MA_Latch.setMA_busy(false);
        }
    }
}
