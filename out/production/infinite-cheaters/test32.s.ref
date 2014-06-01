	.text
    # _main ()
    # (i)
# Allocation map
# i	%rdi
	.p2align 4,0x90
	.globl _main
_main:
	subq $8,%rsp
    # 0.  Begin:
F0_Begin:
    # 1.   i = 0
	movq $0,%rdi
    # 2.   if 2 <= 1 goto L0
	movq $2,%r10
	cmpq $1,%r10
	jle F0_L0
    # 3.   if 1 <= 0 goto L1
	movq $1,%r10
	cmpq $0,%r10
	jle F0_L1
    # 4.   i = 1
	movq $1,%rdi
    # 5.   goto L2
	jmp F0_L2
    # 6.  L1:
F0_L1:
    # 7.   i = 2
	movq $2,%rdi
    # 8.  L2:
F0_L2:
    # 9.  L0:
F0_L0:
    # 10.  call _printInt(i)
	call _printInt
    # 11.  return 
	addq $8,%rsp
	ret
    # 12. End:
F0_End:
