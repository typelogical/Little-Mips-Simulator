import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
abstract class AbstractUnitTests {
	public abstract void runTests ();
}
class LoadException extends Exception {
	public LoadException (String msg) {
		super (msg);
	}
	public LoadException (String msg, Throwable e) {
		super (msg, e);
	}
}
class MemoryUnitException extends Exception {
	public MemoryUnitException (String msg) {
		super (msg);
	}
}

class ExecutionException extends Exception {
	public ExecutionException (String msg) {
		super (msg);
	}
}

class DecodeException extends Exception {
	public DecodeException (String msg) {
		super (msg);
	}
}
class AssemblyInstruction {
	private String opCode;
	public AssemblyInstruction (String opCode) {
		this.opCode = opCode;
	}
	public String getOpCode () {
		return opCode;
	}
}
class IType extends AssemblyInstruction {
	private int r1;
	private String addr; 
	
	public IType (String opCode, int r1, String addr) {
		super (opCode);
		this.r1 = r1;
		this.addr = addr;
	}

	public int getR1 () {
		return r1;
	}

	public String getAddr () {
		return addr;
	}
}
class RType extends AssemblyInstruction {
	private int r1;
	private int r2;
	private int r3;
	public RType (String opCode, int r1, int r2, int r3) {
		super (opCode);
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
	}
	public int getR1 () {
		return r1;
	}
	public int getR2 () {
		return r2;
	}
	public int getR3 () {
		return r3;
	}
}
class JType extends AssemblyInstruction {
	private String addr;	// in hex
	public JType (String opCode, String addr) {
		super (opCode);
		this.addr = addr;
	}
	public String getAddr () {
		return addr;
	}
}
class Interpreter {
	class MemUnit {
		private ArrayList<Integer> mem;
		int memSize;
		public MemUnit (int memSize) {
			mem = new ArrayList(memSize);
			this.memSize = memSize;
			for (int i = 0; i < memSize; ++i) {
				mem.add (0);
			}
		}
		public int getSize () {
			return memSize;
		}
		void dump (int size) {
			for (int i = 0; i < 10; ++i) {
				System.out.format ("%02d    ", i);
			}

			for (int i = 0; i < mem.size (); ++i) {
				if (i % 10 == 0) {
					System.out.print ("\n" + i + "      ");
				}
				System.out.format ("%4h ", mem.get (i));
			}
			System.out.println ();
		}

		Integer read (String addr) throws MemoryUnitException {
			Integer iAddr = 0;
			Integer iVal = 0;
			try {
				iAddr = Integer.parseInt (addr, 16);
			} catch (NumberFormatException e) {
				e.printStackTrace ();
			}
			if (iAddr > 0 || iAddr < memSize) {
				iVal = mem.get (iAddr);
			} else {
				new MemoryUnitException ("Invalid read");
			}
			return iVal;
		}
		// Write valud at given addr
		// addr is a String
		// value is in hex format
		void write (String addr, String value) throws MemoryUnitException {
			Integer iAddr = 0;
			Integer iVal = 0;
			Integer lowerBound = Integer.parseInt ("-ffff", 16);
			Integer upperbound = Integer.parseInt ("ffff", 16);

			try {
				iAddr = Integer.parseInt (addr, 16);
				iVal = Integer.parseInt (value, 16);

				if (iVal < lowerBound || iVal > upperbound) {
					throw new MemoryUnitException ("Invalid instruction range");
				}

				if (iAddr > 0 && iAddr < memSize) {
					mem.set (iAddr, iVal);
				} else {
					throw new MemoryUnitException ("Writing to non-existent memory location");
				}
			} catch (NumberFormatException e) {
				throw new MemoryUnitException ("Invalid assembly instruction");
			}
		}
	}	// end Mem unit

	class RegUnit {

		// The reg values
		private ArrayList<Integer> reg;
		
		public RegUnit () {
			reg = new ArrayList();
			for (int i = 0; i < 20; ++i)
				reg.add (0);
		}

	
		public static final int R0 = 0;                                                                                                                      
		public static final int R1 = 1;                                                                                                                      
		public static final int R2 = 2;                                                                                                                      
		public static final int R3 = 3;                                                                                                                      
		public static final int R4 = 4;                                                                                                                      
		public static final int R5 = 5;                                                                                                                      
		public static final int R6 = 6;                                                                                                                      
		public static final int R7 = 7;                                                                                                                      
		public static final int R8 = 8;                                                                                                                     
		public static final int R9 = 9;                                                                                                                     
		public static final int R10 = 10;                                                                                                                    
		public static final int R11 = 11;                                                                                                                    
		public static final int R12 = 12;                                                                                                                    
		public static final int R13 = 13;                                                                                                                    
		public static final int R14 = 14;                                                                                                                    
		public static final int R15 = 15;
		public static final int PC = 16;
		public static final int IR = 17;
		// Outputs the contents of registers
		public void dump () {
			System.out.format ("PC: %2d%n", reg.get (PC));
			System.out.format ("IR: %4d%n", reg.get (IR));
			System.out.println ("GECNERAL REGISTERS:");
			System.out.format ("#0: %2h    ", reg.get (R0));
			System.out.format ("#4: %2h    ", reg.get (R4));
			System.out.format ("#8: %2h    ", reg.get (R8));
			System.out.format ("#12: %2h   %n", reg.get (R12));
			System.out.format ("#1: %2h    ", reg.get (R1));
			System.out.format ("#5: %2h    ", reg.get (R5));
			System.out.format ("#9: %2h    ", reg.get (R9));
			System.out.format ("#13: %2h   %n", reg.get (R13));
			System.out.format ("#2: %2h    ", reg.get (R2));
			System.out.format ("#6: %2h    ", reg.get (R6));
			System.out.format ("#10: %2h   ", reg.get (R10));
			System.out.format ("#14: %2h   %n", reg.get (R14));
			System.out.format ("#3: %2h    ", reg.get (R3));
			System.out.format ("#7: %2h    ", reg.get (R7));
			System.out.format ("#11: %2h   ", reg.get (R11));
			System.out.format ("#15: %2h   %n", reg.get (R15));
		}

		public Integer read (int regId) {
			Integer value = 0;
			try {
				value = reg.get (regId);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace ();
			}
			return value;
		}

		public void write (int regId, int value) {
			try {
				reg.set (regId, value);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace ();
			}
		}
	}	 // end Reg Unit

	private MemUnit memu;
	private RegUnit regu;
	private AssemblyInstruction currInstruction;
	private final int REGSIZE = 256;
	private final int WORDSIZE = Integer.parseInt ("ffff", 16);
	public Interpreter () {
		memu = new MemUnit (100);
		regu = new RegUnit ();
	}
	public void start () {

	}
	private void fetchHelper (MemUnit memu, RegUnit regu) throws MemoryUnitException {
		int pc = regu.read (RegUnit.PC);
		int instruction = memu.read (Integer.toHexString (pc));
		regu.write (RegUnit.IR, instruction);
		regu.write (RegUnit.PC, ++pc);
	}
	private AssemblyInstruction decodeHelper (String instruction) throws DecodeException {
		String opCode = instruction.substring (0,1);
		boolean error = false;
		// Itype
		switch (opCode) {
			case "0":
			case "1":
			case "2":
			case "3":
			case "4": {
				int r1 = Integer.parseInt (instruction.substring (1,2), 16);
				int r2 = Integer.parseInt (instruction.substring (2,3), 16);
				int r3 = Integer.parseInt (instruction.substring (3,4), 16);
				return new RType (opCode, r1, r2, r3);
			}
			case "5":
			case "6":
			case "9":
			case "12": {
				String addr = instruction.substring (1,4);
				return new JType (opCode, addr);
			}
			case "7":
			case "8":
			case "10":
			case "11": {
				int r1 = Integer.parseInt (instruction.substring (1,2), 16);
				String addr = instruction.substring (2,4);
				return new IType (opCode, r1, addr);
			}
			default: 
				error = true;
			
		}
		if (error) throw new DecodeException ("Invalid instruction"); else  return null;
	}

	private void executeHelper (AssemblyInstruction asm, MemUnit memu, RegUnit regu) 
		throws ExecutionException {
		if (asm instanceof RType) {
			RType rType = (RType) asm;
			int r1Id = rType.getR1 ();
			int r2 = regu.read (rType.getR2 ());
			int r3 = regu.read (rType.getR3 ());
			int result = 0;
			switch (rType.getOpCode ()) {
				case "1":	//add
					result = r2 + r3;
					break;
				case "2":	//sub
					result = r2 - r3;
					break;
				case "3":	//mult
					result = r2 * r3;
					break;
				case "4":	//div
				try {
					result = r2 / r3;
				} catch (ArithmeticException e) {
					throw new ExecutionException ("Divide by zero");
				}
					break;
			}
			if (result < -REGSIZE) {
				throw new ExecutionException ("Register underflow");	
			}
			if (result > REGSIZE) {
				throw new ExecutionException ("Register overflow");
			}

			regu.write (r1Id, result);
		} else if (asm instanceof JType) {
			JType jType = (JType) asm;
			Scanner in = new Scanner (System.in);
			String addr = jType.getAddr ();
			int value;
			switch (jType.getOpCode ()) {
				case "5": // rw
					System.out.print ("Read word: ");
					value = in.nextInt ();

					if (value < -WORDSIZE || value > WORDSIZE) {
						throw new ExecutionException ("Invalid input expected size greater than 2 bytes.");
					}
					break;	
				case "6":	// write
					try {
						value = memu.read (addr);
					} catch (MemoryUnitException e) {
						throw new ExecutionException ("Could not read memory");
					}
					System.out.println(value);
					break;
				case "9":  // jmp
					int iAddr = Integer.parseInt (addr, 16);

					if (iAddr < 0 || iAddr > memu.getSize ()) {
						throw new ExecutionException ("Memory addr accessing invalid memmory");
					}
					
					regu.write (RegUnit.PC, iAddr - 1);
				case "c":
					// do nothing
			}

		} else if (asm instanceof IType) {
				IType iType = (IType) asm;
				int r1Id = iType.getR1 ();
				String addr = iType.getAddr ();
				switch (iType.getOpCode ()) {
					case "7":	// lw
						int value;
						try {
							value = memu.read(addr);
						} catch (MemoryUnitException e) {
							throw new ExecutionException ("Invalid memory access");
						}
						regu.write (r1Id, value);
						break;
					case "8":	{// sw
						String r1 = Integer.toHexString (regu.read (r1Id));
						try {
							memu.write (addr, r1);
						} catch (MemoryUnitException e) {

						}
						break;
					}
					case "a":	{// bzr
						int r1 = regu.read (r1Id);
						int iAddr = Integer.parseInt (addr, 16);
						if (r1 == 0) {
							if (iAddr < 0 || iAddr > memu.getSize ()) {
								throw new ExecutionException ("Memory addr accessing invalid memmory");
							}
							regu.write (RegUnit.PC, iAddr);
						}
						break;
					}
					case "b": // bng
						int r1 = regu.read (r1Id);
						int iAddr = Integer.parseInt (addr, 16);
						if (r1 < 0) {
							if (iAddr < 0 || iAddr > memu.getSize ()) {
								throw new ExecutionException ("Memory addr accessing invalid memmory");
							}
							regu.write (RegUnit.PC, iAddr);
						}
			}
		}
	}

    public void fetch () throws LoadException, MemoryUnitException {
    	// Update IC with new instruction
    	fetchHelper (memu, regu);
    }   
    // parse instruction
	// Take instruction in hex and decode it for execution
    public AssemblyInstruction  decode () {
    	// parse instruction
    	//return decodeHelper ();
    	return null;
	}

	public void execute ()  {
		// Get asm instruction info and execute instruction
		// switch ()

	}

	// Have user enter instruction in hex format one line at a time
	public void load () throws LoadException {
		Scanner in = null;
		String halt = "-11111";
		int startAddr = 0;
		String instruction = "";

		System.out.println ("Welcome to te loading screen. ");
		System.out.println ("Enter assembly instructions, one pure line.");
		System.out.println ("To finish loading, enter -11111.");
		
		in = new Scanner (System.in);
		instruction = in.next ();

		for (startAddr = 0; !instruction.equals (halt); ++startAddr) {
			try {
			memu.write (Integer.toHexString (startAddr), instruction);
			
			} catch (MemoryUnitException e) {
				throw new LoadException ("Could not write to memory",e);
			} 
			instruction = in.next ();
			
		}
		System.out.println ("The following program has been loaded.");
		System.out.println ("Addr    Instruction");
		for (int i = 0; i < startAddr; ++i) {
			String addr = Integer.toHexString (i);
			try {
				System.out.println (addr + "    " + Integer.toHexString (memu.read (addr)));
			} catch (MemoryUnitException e) {
				throw new LoadException ("Could not read from memory", e);
			}
		}
	}

	/*********************************************************************/
	// Tests
	class UnitTests extends AbstractUnitTests {
		public UnitTests () {

		}

		public void testMem () {
			int memSize = 100;
			MemUnit memu = new MemUnit (memSize);
			System.out.println ("Newly created mem unit: ");
			memu.dump (memSize);
			System.out.println ("Wrting data: ");
			try {
				memu.write ("00", "5008");
				memu.write ("01", "5009");
				memu.write ("02", "7008");
				memu.write ("03", "7109");
				memu.write ("04", "1201");
				memu.write ("05", "820a");
				memu.write ("06", "600a");
				memu.write ("07", "c000");
				memu.write ("08", "0000");
				memu.write ("09", "0000");
				memu.write ("0a", "0000");
			} catch (MemoryUnitException e) {

			}
			System.out.println ("After writing data");
			memu.dump (memSize);
		}
		public void testReg () {
			RegUnit regu = new RegUnit ();
			System.out.println ("Newly created reg unit:");
			regu.dump ();
			System.out.println ("Writing data");
			regu.write (RegUnit.R1, 3);
			regu.write (RegUnit.R2, 6);
			System.out.println ("Reading data");
			System.out.println (regu.read (RegUnit.R1));
			System.out.println (regu.read (RegUnit.R2));
		}
		public void testLoad () {
			try {
				load ();
			} catch (LoadException e) {
				System.out.println (e.getMessage ());
				e.printStackTrace ();
			}
		}
		public void testFetch () {
			MemUnit memu = new MemUnit (100);
			RegUnit regUnit = new RegUnit ();
			try {
			for (int i = 0, j = 2323; i < 10; ++i) {
				String k = Integer.toHexString (j + i * i);
				memu.write (Integer.toHexString (i), k);
				System.out.println ("Wrote " + k + " to memory");
			}

			for (int i = 0; i < 10; ++i) {
				fetchHelper (memu, regu);
				int pc = regu.read (RegUnit.PC);
				int ic = regu.read (RegUnit.IR);
				System.out.println ("IR: " + Integer.toHexString (ic) + " PC: " + pc);
			}	
			}catch (Exception e) {

			}	
		}
		public void testDecode () {
			AssemblyInstruction asm;
			String iType = "7208";
			String rType = "1015";
			String jType = "5008";
			try {
				System.out.println ("Decoding...");
				IType asm1 = (IType) decodeHelper (iType);
				if (asm1 != null) {
					System.out.println ("I Type: " + iType);
					System.out.println (asm1.getOpCode ());
					System.out.println (asm1.getR1 ());
					System.out.println (asm1.getAddr ());
				} else { System.out.println ("Null pointer");}
				RType asm2 = (RType) decodeHelper (rType);
				System.out.println ("R Type: " + rType);
				System.out.println (asm2.getOpCode ());
				System.out.println (asm2.getR1 ());
				System.out.println (asm2.getR2 ());
				System.out.println (asm2.getR3 ());

				JType asm3 = (JType) decodeHelper (jType);
				System.out.println ("J TYpe: " + jType);
				System.out.println (asm3.getOpCode ());
				System.out.println (asm3.getAddr ());
			} catch (DecodeException e) {

			}
		}

		public void printR (String msg, RegUnit regu, int id) {
			System.out.println (msg + Integer.toHexString (regu.read (id)));
		}
		public void printM (String msg, MemUnit regu, String addr) {
			try {
			System.out.println (msg + Integer.toHexString (memu.read (addr)));
			} catch (MemoryUnitException e) {
				e.getMessage ();
			}
		}
		public void catched (Exception e) {
			System.out.println ("Exception caught: " + e.getMessage ());
		}
		

		public void testExecuteArithematic () {
			MemUnit memu;
			RegUnit regu;
			AssemblyInstruction asm;
			String asmStr;

			try {
				memu = new MemUnit (100);
				regu = new RegUnit ();

				// Test R0 = R0 + R1, without overflow or underflow 
				regu.write (RegUnit.R1, 2);
				regu.write (RegUnit.R2, 2);
				asmStr = "1012";
				asm = decodeHelper (asmStr);
				executeHelper (asm, memu, regu);
				printR ("Expected 4. R0 = ",regu, RegUnit.R0);

				try {
					// Test R0 = R0 + R1, with overflow
					regu.write (RegUnit.R1, 65536);
					regu.write (RegUnit.R2, 1);
					asmStr = "1012";
					asm = decodeHelper (asmStr);
					executeHelper (asm, memu, regu);
					printR ("Expected overflow. R0 = ",regu, RegUnit.R0);
				} catch (ExecutionException e) {
					catched (e);
				}
				try {
					// Test R0 = R0 + R1, with underflow
					regu.write (RegUnit.R1, -5536);
					regu.write (RegUnit.R2, -65536);
					asmStr = "1012";
					asm = decodeHelper (asmStr);
					executeHelper (asm, memu, regu);
					printR ("Expected underflow. R0 = ",regu, RegUnit.R0);
				} catch (ExecutionException e) {
					catched (e);
				}
				// Test R0 = R1 - R2, without overflow or underflow
				regu.write (RegUnit.R1, 4);
				regu.write (RegUnit.R2, 2);
				asmStr = "2012";
				asm = decodeHelper (asmStr);
				executeHelper (asm, memu, regu);
				printR ("Expected 2. R0 = ",regu, RegUnit.R0);
				try {
					// Test R0 = R1 - R2, with underflow
					regu.write (RegUnit.R1, -5536);
					regu.write (RegUnit.R2, -65536);
					asmStr = "2012";
					asm = decodeHelper (asmStr);
					executeHelper (asm, memu, regu);
					printR ("Expected underflow. R0 = ",regu, RegUnit.R0);
				} catch (ExecutionException e) {
					catched (e);
				}
				// Test R0 = R1 * R2, without overflow or underflow
				regu.write (RegUnit.R1, 4);
				regu.write (RegUnit.R2, 2);
				asmStr = "3012";
				asm = decodeHelper (asmStr);
				executeHelper (asm, memu, regu);
				printR ("Expected 8. R0 = ",regu, RegUnit.R0);
				try {
					// Test R0 = R1 * R2, with overflow
					regu.write (RegUnit.R1, 65500);
					regu.write (RegUnit.R2, 2);
					asmStr = "3012";
					asm = decodeHelper (asmStr);
					executeHelper (asm, memu, regu);
					printR ("Expected overflow. R0 = ", regu, RegUnit.R0);
				} catch (ExecutionException e) {
					catched (e);
				}

				try {
					// Test R0 = R1 * R2, with underflow
					regu.write (RegUnit.R1, -65500);
					regu.write (RegUnit.R2, 2);
					asmStr = "3012";
					asm = decodeHelper (asmStr);
					executeHelper (asm, memu, regu);
					printR ("Expected underflow. R0 = ", regu, RegUnit.R0);
				} catch (ExecutionException e) {
					catched (e);
				}

				// Test R0 = R1 / R2, without overflow or underflow
				regu.write (RegUnit.R1, 64);
				regu.write (RegUnit.R2, 2);
				asmStr = "4012";
				asm = decodeHelper (asmStr);
				executeHelper (asm, memu, regu);
				printR ("Expected 32. R0 = ",regu, RegUnit.R0);

				try {
					// Test R0 = R1 / R2, with / by 0
					regu.write (RegUnit.R1, 64);
					regu.write (RegUnit.R2, 0);
					asmStr = "4012";
					asm = decodeHelper (asmStr);
					executeHelper (asm, memu, regu);
					printR ("Expected / by 0. R0 = ",regu, RegUnit.R0);
				}catch (Exception e) {
					e.getMessage ();
				}
			}catch (Exception e) {
				e.getMessage ();
			}		
		}

		public void testExecuteIO () {
			MemUnit memu;
			RegUnit regu;
			AssemblyInstruction asm;
			String asmStr;

			memu = new MemUnit (100);
			regu = new RegUnit ();

			try {
				// Test read, with valid input
				asmStr = "5005";
				asm = decodeHelper (asmStr);
				executeHelper (asm, memu, regu);
				printM ("Read ", memu, "05");

				// Test read, with ivalid input
				asmStr = "5005";
				asm = decodeHelper (asmStr);
				executeHelper (asm, memu, regu);
				
			} catch (Exception e) {
				catched (e);
			}

			try {
				// Test write
				asmStr = "5005";
				memu.write ("05", "ff");
				asm = decodeHelper (asmStr);
				executeHelper (asm, memu, regu);
				printM ("Expected ff.", memu, "05");
			} catch (Exception e) {
				catched (e);
			}
		}

		public void testExecuteStoreAndLoad () {
			MemUnit memu;
			RegUnit regu;
			AssemblyInstruction asm;
			String asmStr;

			// try {
			// 	memu = new MemUnit (100);
			// 	regu = new RegUnit ();

		}

		public void testExecuteBranch () {

		}

		public void testExecuteHalt () {

		}

		public void testStart () {

		}
		@Override
		public void runTests () {
			testMem ();
			testReg ();
			testLoad ();
			testFetch ();
			testDecode ();
			testExecuteArithematic ();
		}
	}

	public AbstractUnitTests getTestInstance ()  {
		return new UnitTests ();
	}
	/*********************************************************************/
}

public class AsmInterpreter {
	public static void main (String[] args) {
		AbstractUnitTests tests = new Interpreter().getTestInstance ();
		tests.runTests ();
			// try {
			// 	//memu.write(startAddr.toHexString, instruction);
			// } catch (NumberFormatException e) {
			// 	System.out.println ("Invalid instruction. Load aborted.");
			// } catch (LoadException e) {

			// }
	}
	/*

	Errors to handle:
		Execution:
			Register overflow
			Invalid opcode
			invalid register


	*/
}
