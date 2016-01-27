#Documenation
----
Created: 1/18/2016 2:57:32 PM

Modified: 1/18/2016 2:58:24 PM 




The classes for this program are listed below

##Helper Classes

AssemblyInstruction

* Represenets an assembly instruction.
* It is the parent class of RType, IType, and JType

RType

IType

JType

MemUnit

* Represents the memory where the instruction are stored

RegUnit

* Represents the registers
 
## Interpreter
Interpreter

* Contains the main logic for the interpreter
* Fetches next instruction
* Decodes instruction
* Executes instruction


## Exception Classes

**These classes are used for error handling code**
Thery're not really that important. They don't contain much code.

LoadException

MemoryUnitException

FetchException

DecodeException

ExecuteException


### Summary of Code

This program is a simulator for a basic assembly language inspired by the MIPS. 

The program loads an assembly program represented in hex from the command line. The user is required to enter each instruction of the assembly program one line at a time. The special -11111 is used to terminate loading. 

An important part of loading is that when reading in an instruction the interpreter always expects to read in 1 word, or 2 bytes of data. This is not a problem for valid instructions, but is for invalid instructions, or lines that are not instructions but data, such as 2, or 4. When the line is
suppose to be data, then the data must be padded on the left, with 0s. For example, 2 becomes 0002, 4 becomes 0004. If lines are not correctly padded, errors may occur in running.
 
Once the program is loaded, the code will be run by the interpreter by calling the method interpret(). This method
calls the individual methods that models the famous instruction fetch cycle, that are used by real cpus There methods are fetch(), decode(), and execute(). They will fetch, decode, and execute each instruction, respectively.

Sometimes errors may happen. If this is the case, each function will recognize the error and handle it correctly with an appropriate error message. The errors that it currently recognize are invalid ranges, invalid instructions, register overflows, register underflow, and divide by zeros. All errors will cause the program to abort immediately, after the error message has been outputted.

If no errors happen, then the program should run as expected, and a message saying the program was successful should be outputted. The program only runs one program for each time that it is executed. So to run a different assembly program the program will have to be run again, and the new assembly program loaded.



