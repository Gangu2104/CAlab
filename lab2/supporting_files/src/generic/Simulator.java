package generic;

import java.io.FileInputStream;
import generic.Operand.OperandType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.ByteBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;

import java.util.Properties;
import java.util.HashMap;


public class Simulator {
		
	static FileInputStream inputcodeStream = null;


	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();

	}
	


	public static String getopcode(OperationType opName)
	{
		if(opName == OperationType.load){
			return "10110";
		}
		else if(opName == OperationType.add){
			return "00000";
		}
		else if(opName == OperationType.addi){
			return "00001";
		}
		else if(opName == OperationType.sub){
			return "00010";
		}
		else if(opName == OperationType.subi){
			return "00011";
		}
		else if(opName == OperationType.mul){
			return "00100";
		}
		else if(opName == OperationType.muli){
			return "00101";
		}
		else if(opName == OperationType.div){
			return "00110";
		}
		else if(opName == OperationType.divi){
			return "00111";
		}
		else if(opName == OperationType.and){
			return "01000";
		}
		else if(opName == OperationType.andi){
			return "01001";
		}
		else if(opName == OperationType.or){
			return "01010";
		}
		else if(opName == OperationType.ori){
			return "01011";
		}
		else if(opName == OperationType.xor){
			return "01100";
		}
		else if(opName == OperationType.xori){
			return "01101";
		}
		else if(opName == OperationType.slt){
			return "01110";
		}
		else if(opName == OperationType.slti){
			return "01111";
		}
		else if(opName == OperationType.sll){
			return "10000";
		}
		else if(opName == OperationType.slli){
			return "10001";
		}
		else if(opName == OperationType.srl){
			return "10010";
		}
		else if(opName == OperationType.srli){
			return "10011";
		}
		else if(opName == OperationType.sra){
			return "10100";
		}
		else if(opName == OperationType.srai){
			return "10101";
		}
		else if(opName == OperationType.store){
			return "10111";
		}
		else if(opName == OperationType.jmp){
			return "11000";
		}
		else if(opName == OperationType.beq){
			return "11001";
		}
		else if(opName == OperationType.bne){
			return "11010";
		}
		else if(opName == OperationType.blt){
			return "11011";
		}
		else if(opName == OperationType.bgt){
			return "11100";
		}
		else{
			return "11101";
		}
	}
	


	// public static int binaryStringToSignedint(String binaryString) {

    //     boolean isNegative = binaryString.charAt(0) == '1';

    //     // Parse the binary string to an integer, excluding the sign bit
    //     int binaryValue = Integer.parseInt(binaryString.substring(1), 2);

    //     // Adjust the result based on the sign bit
    //     if (isNegative) {
    //         binaryValue = -binaryValue;
    //     }

	// 	return binaryValue;
    // }
 


	public static int binaryToDecimal(String binary) {

        // Check if the input string is empty or not 32 characters long
        if (binary == null || binary.length() != 32) {
            throw new IllegalArgumentException("Input must be a 32-bit binary string");
        }

        // Check if the MSB is 1, indicating a negative number
        boolean isNegative = binary.charAt(0) == '1';

        // Initialize the decimal value to 0
        int decimal = 0;

        // If it's a negative number, convert it to its 2's complement form
        if (isNegative) {
            // Invert all bits
            StringBuilder inverted = new StringBuilder();
            for (int i = 0; i < 32; i++) {
                inverted.append(binary.charAt(i) == '0' ? '1' : '0');
            }

            // Add 1 to the inverted binary representation
            StringBuilder incremented = new StringBuilder(inverted);
            boolean carry = true;
            for (int i = 31; i >= 0; i--) {
                if (carry) {
                    char bit = inverted.charAt(i);
                    if (bit == '0') {
                        incremented.setCharAt(i, '1');
                        carry = false;
                    } else {
                        incremented.setCharAt(i, '0');
                    }
                }
            }

            // Convert the 2's complement form to decimal
            for (int i = 31; i >= 0; i--) {
                char bit = incremented.charAt(i);
                if (bit == '1') {
                    // If the current bit is 1, add the corresponding power of 2 to the decimal value
                    decimal += Math.pow(2, 31 - i);
                }
            }

            // Make the result negative
            decimal = -decimal;
        } else {
            // If it's a positive number, convert it to decimal directly
            for (int i = 31; i >= 0; i--) {
                char bit = binary.charAt(i);
                if (bit == '1') {
                    // If the current bit is 1, add the corresponding power of 2 to the decimal value
                    decimal += Math.pow(2, 31 - i);
                }
            }
        }

        return decimal;
    }




	public static String getbinarystring(int number, int last_how_many_chars)
	{
		String binaryString = Integer.toBinaryString(number);
		System.out.println("binary_without_0s : "+ binaryString);

		while (binaryString.length() < 32) {
			binaryString = "0" + binaryString;
		}

		System.out.println("binary_with_added_leading_0s : "+ binaryString);
		String binaryString1 = binaryString.substring( 32- last_how_many_chars );
		System.out.println("last_many_chars:" + binaryString1);
		return binaryString1;
	}

	


	public static void assemble(String objectProgramFile)
	{
		//TODO your assembler code
		//1. open the objectProgramFile in binary mode
		//2. write the firstCodeAddress to the file
		//3. write the data to the file
		//4. assemble one instruction at a time, and write to the file
		//5. close the file

		// String inputFile = args;
		// String outputFile = "/home/vishal/CA_Lab/Lab_A2/supporting_files/ex2.out";
        
		try {
		    // Create a FileOutputStream for writing to the file in binary mode
		    FileOutputStream bfile = new FileOutputStream(objectProgramFile);
		 
		    int firstCodeAddress = ParsedProgram.firstCodeAddress;
			bfile.write(firstCodeAddress);
		    // bfile.write(ByteBuffer.allocate(4).putInt(firstCodeAddress).array());


			ArrayList<Integer> dataArray = ParsedProgram.data;
			// byte[] a=ByteBuffer.allocate(4).putInt(10).array();
			// bfile.write(a);
			for (int i=0; i < firstCodeAddress; i++) {
				bfile.write(ByteBuffer.allocate(4).putInt(dataArray.get(i)).array());
			}   
			// ParsedProgram.parseCodeSection(inputFile, firstCodeAddress);
			// Instruction k=ParsedProgram.getInstructionAt(firstCodeAddress);
			// Instruction.OperationType str = OperationType Instruction.getOperationType();
			// System.out.println("first_code_address :"+firstCodeAddress);



			int programCounter = firstCodeAddress;
			System.out.println("checks : " + getbinarystring(4, 17));

			while(true){
				if( (programCounter-firstCodeAddress) > ParsedProgram.code.size() - 1 ){
					break;
				}

				Instruction instruction_line = ParsedProgram.getInstructionAt(programCounter);
				Instruction.OperationType opName = instruction_line.getOperationType();
				
				
				System.out.println("first_code_address :"+firstCodeAddress);
				// Check if the instruction is the "end" instruction
				if (instruction_line.getOperationType() == OperationType.end) {

					String end_opcode = getopcode(OperationType.end);
					String ends_zeroes = getbinarystring(0, 27);
					String output = end_opcode + ends_zeroes;
					// System.out.println( end_opcode + ends_zeroes );
					int finale = binaryToDecimal(output);
					bfile.write(ByteBuffer.allocate(4).putInt(finale).array());

					programCounter++;
					continue;
					// break; // Exit the loop when encountering the end instruction
				}

				Operand rs1 = instruction_line.getSourceOperand1();
				Operand rs2 = instruction_line.getSourceOperand2();
				Operand rsd = instruction_line.getDestinationOperand();


				String binary_rs1 = "";
				String binary_rs2 = "";
				String binary_rsd = "";

				if(rs1 != null){
					binary_rs1 = getbinarystring(rs1.value, 5);
				}
				if(rs2 != null){
					binary_rs2 = getbinarystring(rs2.value, 5);
				}
				if(rsd != null){
					binary_rsd = getbinarystring(rsd.value, 5);
				}
				

				OperationType opaName = instruction_line.getOperationType();
				switch( opaName.toString() ){

							//R2I type
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
							case "store" :{
									String opcode = getopcode(opaName);
									String rs2_immediate = getbinarystring(rs2.value, 17);
									String output = opcode + binary_rs1 + binary_rsd + rs2_immediate ;

									System.out.println(binaryToDecimal(output));
									int finale = binaryToDecimal(output);
									System.out.println(finale);

									bfile.write(ByteBuffer.allocate(4).putInt(finale).array());
									// System.out.println( opcode + binary_rs1 + binary_rsd + immediate );
									break;
								  }

							//R3I type
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
							case "sra" :{
									String opcode = getopcode(opaName);
									String unused_bits = getbinarystring(0, 12);
									String output = opcode + binary_rs1 + binary_rs2 + binary_rsd + unused_bits ;
									// System.out.println( opcode + binary_rs1 + binary_rs2 + binary_rsd + unused_bits );
									int finale = binaryToDecimal(output);
									bfile.write(ByteBuffer.allocate(4).putInt(finale).array());

									break;
								  }
							case "beq" : 
							case "bne ": 
							case "blt" : 
							case "bgt" : {
									String opcode = getopcode(opaName);
									int pc = instruction_line.getProgramCounter();
									// System.out.println("hi");

									// System.out.println(ParsedProgram.symtab.get(rsd.getLabelValue())-pc);
									String rsd_immediate = getbinarystring(ParsedProgram.symtab.get(rsd.getLabelValue())-pc, 17);
									// System.out.println( rsd.getLabelValue() );
									// System.out.println(immediate);
									// System.out.println( opcode + binary_rs1 + binary_rs2 + immediate );
									String output =  opcode + binary_rs1 + binary_rs2 + rsd_immediate  ;
									int finale = binaryToDecimal(output);
									bfile.write(ByteBuffer.allocate(4).putInt(finale).array());

									break;
								
							}
							case "jmp" :{
								    String opcode = getopcode(opaName);
									int pc = instruction_line.getProgramCounter();
									String immediate = getbinarystring(ParsedProgram.symtab.get(rsd.getLabelValue())-pc, 22);
									String output =  opcode + binary_rsd + immediate  ;
									int finale = binaryToDecimal(output);
									bfile.write(ByteBuffer.allocate(4).putInt(finale).array());
									// System.out.println( opcode + binary_rsd + immediate );
									break;
							}
							


					default: Misc.printErrorAndExit("unknown instruction!!");
				}




				// System.out.println(instruction_line.getProgramCounter());
				System.out.println(instruction_line.getProgramCounter());

				// System.out.println(binaryToDecimal("11101000000000000000000000000000"));
				// System.out.println(binaryToDecimal("00011000000000000000000000000000"));
				

				// System.out.println(instruction_line);
				// System.out.println(opName);
				// System.out.println(getopcode(opName));

				// System.out.println(rs1.getValue());
				// System.out.println(rs2.getValue());
				// System.out.println(rsd.getValue());

				// System.out.println(binary_rs1);
				// System.out.println(binary_rs2);
				// System.out.println(binary_rsd);

				// System.out.println(rs1.getLabelValue());
				// System.out.println(rs2.getLabelValue());
				// System.out.println(rsd.getLabelValue());

				// TODO: Implement code to assemble the instruction and write it to the file
				// You will need to convert the instruction into its binary representation
				// and write it as bytes to the file.

				programCounter++;				
			}
   
		    
		
		    
		    

		    bfile.close(); // Close the stream when done
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		
		
		
	}
	
}
