	.text
    # _main ()
    # (i, j)
# Allocation map
# t1	%rax
# t2	%rcx
# t3	%rdi
# j	%rcx
# i	%rax
	.p2align 4,0x90
	.globl _main
_main:
	subq $8,%rsp
    # 0.  Begin:
F0_Begin:
    # 1.   t1 = 2 + 3
	movq $2,%rax
	addq $3,%rax
    # 2.   i = t1
    # 3.   t2 = 2 * 1
	movq $2,%rcx
	imulq $1,%rcx
    # 4.   j = t2
    # 5.   t3 = i + j
	movq %rax,%rdi
	addq %rcx,%rdi
    # 6.   call _printInt(t3)
	call _printInt
    # 7.   return 
	addq $8,%rsp
	ret
    # 8.  End:
F0_End:
