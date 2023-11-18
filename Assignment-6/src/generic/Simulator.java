package generic;

import processor.Clock;
import processor.Processor;
import processor.memorysystem.MainMemory;
import processor.pipeline.RegisterFile;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Simulator {

    static Processor processor;
    static boolean simulationComplete;
    static EventQueue eventQueue;
    public static int ins_count;
    public static long storeresp;

    public static void setupSimulation(String assemblyProgramFile, Processor p) {
        Simulator.processor = p;
        loadProgram(assemblyProgramFile);
        simulationComplete = false;
        eventQueue = new EventQueue();
        ins_count=0;
        storeresp=0;
    }

    static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		

		try (InputStream input = new FileInputStream(assemblyProgramFile)){
			DataInputStream data = new DataInputStream(input);
			int address = -1;
		while(data.available() > 0){
			int next = data.readInt();
			if(address == -1){
				processor.getRegisterFile().setProgramCounter(next);
			}
			else{
				processor.getMainMemory().setWord(address, next);
			}
			address += 1;
		}
			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1, 65535);
			processor.getRegisterFile().setValue(2, 65535);
			// System.out.println(processor.getRegisterFile().getProgramCounter());
			// System.out.println(processor.getMainMemory().getContentsAsString(0, 10));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

    }

    public static void simulate() {
        int cycles = 0;
        while (!Simulator.simulationComplete) {
            processor.getRWUnit().performRW();
            processor.getMAUnit().performMA();
            processor.getEXUnit().performEX();
            eventQueue.processEvents();
            processor.getOFUnit().performOF();
            processor.getIFUnit().performIF();
            Clock.incrementClock();
            cycles++;
            //Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
            //Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);
        }
        //System.out.println("Cycles Taken:         " + Clock.getCurrentTime());
        //System.out.println("Instruction Executed: " + (ins_count));
        // TODO
        // set statistics
        //Statistics.setNumberOfCycles((int) Clock.getCurrentTime());
        Statistics stat = new Statistics();
        stat.setNumberOfCycles(cycles);
        stat.setNumberOfInstructions(ins_count);
        stat.setCPI();
        stat.setIPC();
    }

    public static EventQueue getEventQueue() {
        return eventQueue;
    }

    public static void setSimulationComplete(boolean value) {
        simulationComplete = value;
    }
}
