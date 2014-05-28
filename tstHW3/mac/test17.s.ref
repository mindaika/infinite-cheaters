	.text
	.globl __class_A
__class_A:
	.quad __A_go
    # _main ()
    # (a)
# Allocation map
# t1	%rbx
# t2	%rax
# t3	%rax
# t4	%rdi
# a	%rdi
	.p2align 4,0x90
	.globl __main
__main:
	pushq %rbx
    # 0.  Begin:
F0_Begin:
    # 1.   t1 = call _malloc(8)
	movq $8,%rdi
	call __malloc
	movq %rax,%rbx
    # 2.   [t1]:P = _class_A
	leaq __class_A(%rip),%r10
	movq %r10,(%rbx)
    # 3.   call _init0_A(t1)
	movq %rbx,%rdi
	call __init0_A
    # 4.   a = t1
	movq %rbx,%rdi
    # 5.   t2 = [a]:P
	movq (%rdi),%rax
    # 6.   t3 = [t2]:P
	movq (%rax),%rax
    # 7.   t4 = call * t3(a)
	call * %rax
	movq %rax,%rdi
    # 8.   call _printInt(t4)
	call __printInt
    # 9.   return 
	popq %rbx
	ret
    # 10. End:
F0_End:
    # _init0_A (obj)
# Allocation map
	.p2align 4,0x90
	.globl __init0_A
__init0_A:
	subq $8,%rsp
    # 0.  Begin:
F1_Begin:
    # 1.   return 
	addq $8,%rsp
	ret
    # 2.  End:
F1_End:
    # _init_A (obj)
# Allocation map
	.p2align 4,0x90
	.globl __init_A
__init_A:
	subq $8,%rsp
    # 0.  Begin:
F2_Begin:
    # 1.   return 
	addq $8,%rsp
	ret
    # 2.  End:
F2_End:
    # _A_go (obj)
    # (b)
# Allocation map
# t1	%rax
# t2	%rax
# t3	%rax
# t4	%rax
# b	%rbx
# t5	%rax
# t6	%rax
# t7	%rax
# t8	%rdi
# t9	%rax
# t10	%rax
# t11	%rax
	.p2align 4,0x90
	.globl __A_go
__A_go:
	pushq %rbx
    # 0.  Begin:
F3_Begin:
    # 1.   t1 = call _malloc(8)
	movq $8,%rdi
	call __malloc
    # 2.   b = t1
	movq %rax,%rbx
    # 3.   t2 = 0 * 4
	movq $0,%rax
	imulq $4,%rax
    # 4.   t3 = b + t2
	movq %rax,%r10
	movq %rbx,%rax
	addq %r10,%rax
    # 5.   [t3]:I = 3
	movl $3,(%rax)
    # 6.   t4 = 1 * 4
	movq $1,%rax
	imulq $4,%rax
    # 7.   t5 = b + t4
	movq %rax,%r10
	movq %rbx,%rax
	addq %r10,%rax
    # 8.   [t5]:I = 4
	movl $4,(%rax)
    # 9.   t6 = 1 * 4
	movq $1,%rax
	imulq $4,%rax
    # 10.  t7 = b + t6
	movq %rax,%r10
	movq %rbx,%rax
	addq %r10,%rax
    # 11.  t8 = [t7]:I
	movslq (%rax),%rdi
    # 12.  call _printInt(t8)
	call __printInt
    # 13.  t9 = 0 * 4
	movq $0,%rax
	imulq $4,%rax
    # 14.  t10 = b + t9
	movq %rax,%r10
	movq %rbx,%rax
	addq %r10,%rax
    # 15.  t11 = [t10]:I
	movslq (%rax),%rax
    # 16.  return t11
	popq %rbx
	ret
    # 17. End:
F3_End:
