	.text
    # _main ()
    # (a)
# Allocation map
# t1	%rax
# t2	%rax
# t3	%rax
# t4	%rax
# t5	%rax
# t6	%rax
# t7	%rax
# a	%rbx
# t8	%rdi
# t9	%rax
# t10	%rax
# t11	%rdi
	.p2align 4,0x90
	.globl _main
_main:
	pushq %rbx
    # 0.  Begin:
F0_Begin:
    # 1.   t1 = call _malloc(8)
	movq $8,%rdi
	call _malloc
    # 2.   a = t1
	movq %rax,%rbx
    # 3.   t2 = 0 * 4
	movq $0,%rax
	imulq $4,%rax
    # 4.   t3 = a + t2
	movq %rax,%r10
	movq %rbx,%rax
	addq %r10,%rax
    # 5.   [t3]:I = 1
	movl $1,(%rax)
    # 6.   t4 = 1 * 4
	movq $1,%rax
	imulq $4,%rax
    # 7.   t5 = a + t4
	movq %rax,%r10
	movq %rbx,%rax
	addq %r10,%rax
    # 8.   [t5]:I = 2
	movl $2,(%rax)
    # 9.   t6 = 0 * 4
	movq $0,%rax
	imulq $4,%rax
    # 10.  t7 = a + t6
	movq %rax,%r10
	movq %rbx,%rax
	addq %r10,%rax
    # 11.  t8 = [t7]:I
	movslq (%rax),%rdi
    # 12.  call _printInt(t8)
	call _printInt
    # 13.  t9 = 1 * 4
	movq $1,%rax
	imulq $4,%rax
    # 14.  t10 = a + t9
	movq %rax,%r10
	movq %rbx,%rax
	addq %r10,%rax
    # 15.  t11 = [t10]:I
	movslq (%rax),%rdi
    # 16.  call _printInt(t11)
	call _printInt
    # 17.  return 
	popq %rbx
	ret
    # 18. End:
F0_End:
