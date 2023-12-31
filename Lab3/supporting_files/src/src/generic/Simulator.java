package generic;
import processor.Processor;
import processor.pipeline.RegisterFile;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import generic.Statistics;
// import java.io.DataInputStream;
// import java.io.FileInputStream;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.io.InputStream;



import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p) 
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
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
		
		// InputStream is = null;
		// 	try{
		// 		is = new FileInputStream(assemblyProgramFile);
		// 	}
		// 	catch (FileNotFoundException e){
		// 		e.printStackTrace();
		// 	}
			
		// 	DataInputStream d_is = new DataInputStream(is);

		// 	int address = -1;
			
		// 	while(d_is.available() > 0){
		// 		int next = d_is.readInt();
		// 		if(address == -1){
		// 			processor.getRegisterFile().setProgramCounter(next);
		// 		}
		// 		else{
		// 			processor.getMainMemory().setWord(address, next);
		// 		}
		// 		address += 1;
		// 	}
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
	    // processor.getRegisterFile().setValue(0, 0);
        // processor.getRegisterFile().setValue(1, 65535);
        // processor.getRegisterFile().setValue(2,65535);
}
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getRWUnit().performRW();
			Clock.incrementClock();
			// Statistics statistics = new Statistics();
			// statistics.setNumberOfInstructions(statistics.getNumberOfInstructions() + 1);
			// statistics.setNumberOfCycles(statistics.getNumberOfCycles() + 1);
			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);
		}
		
		// TODO
		// set statistics
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
