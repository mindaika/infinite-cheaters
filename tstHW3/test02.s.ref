	.text
    # _main ()
    # (b, i, j)
# Allocation map
# t1	%rax
# t2	%rax
# b	%rdi
# j	%rbp
# i	%rbx
	.p2align 4,0x90
	.globl _main
_main:
	pushq %rbx
	pushq %rbp
	subq $8,%rsp
    # 0.  Begin:
F0_Begin:
    # 1.   b = true
	movq $1,%rdi
    # 2.   t1 = 1 + 1
	movq $1,%rax
	addq $1,%rax
    # 3.   i = t1
	movq %rax,%rbx
    # 4.   t2 = 3 * i
	movq $3,%rax
	imulq %rbx,%rax
    # 5.   j = t2
	movq %rax,%rbp
    # 6.   call _printBool(b)
	call _printBool
    # 7.   call _printInt(i)
	movq %rbx,%rdi
	call _printInt
    # 8.   call _printInt(j)
	movq %rbp,%rdi
	call _printInt
    # 9.   return 
	addq $8,%rsp
	popq %rbp
	popq %rbx
	ret
    # 10. End:
F0_End:
