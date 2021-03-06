Load:

To begin, I read the instructions under PartII in the HW3 assignment. After reading them roughly 10 times, I sought assistance from the message board and the class instructors, as I had no idea whatsoever what I was supposed to be doing, despite having really no problems with PartI.

Once I got enough guidance to begin, I started by conceptually locating series of IR Instructions that led to an array dereference. The outcome of this is evidenced by the series of If statements followed by a long string of checks. While this works at least somewhat, I'm not 100% convinced this would work in all programs. If the IR Instructions leading to an array dereference weren't co-located, they would fail the check. Worst case though, the outcome of that would just mean that array dereference wasn't optimized. The solution to this, assuming it is in fact a possibility, would be to check all other IR code for the corresponding fragments. While this would increase optimization, it would reduce compiler performance. 

After flailing around the code for a couple days (weeks), I moved my code to the end of Func.gen() so that I could use the pointer into the codelist, rather than the list itself. This movement also allowed me to use the now generated X86 registers to compact the call. 

The actual improvement of the Load is relatively simple, once I figured it out: Create a new Mem call with the base (A in the example), the index (j) and the offset (4 * j) and call the emit2 function with the new memCall and the Load destination register, then increment the pointer to skip the unused instructions. 

For Store, I essentially recycled the code from IR.Store and the rewritten code from above. We apparently also need to check the register size before moving. 