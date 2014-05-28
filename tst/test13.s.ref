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
# Allocation map
# t1	%rax
	.p2align 4,0x90
	.globl __A_go
__A_go:
	subq $8,%rsp
    # 0.  Begin:
F3_Begin:
    # 1.   if 1 >= 2 goto L0
	movq $1,%r10
	cmpq $2,%r10
	jge F3_L0
    # 2.   call _printInt(1)
	movq $1,%rdi
	call __printInt
    # 3.   goto L1
	jmp F3_L1
    # 4.  L0:
F3_L0:
    # 5.   t1 = 3 * 4
	movq $3,%rax
	imulq $4,%rax
    # 6.   if t1 != 10 goto L2
	cmpq $10,%rax
	jne F3_L2
    # 7.   call _printInt(4)
	movq $4,%rdi
	call __printInt
    # 8.   goto L3
	jmp F3_L3
    # 9.  L2:
F3_L2:
    # 10.  call _printInt(5)
	movq $5,%rdi
	call __printInt
    # 11. L3:
F3_L3:
    # 12. L1:
F3_L1:
    # 13.  return 6
	movq $6,%rax
	addq $8,%rsp
	ret
    # 14. End:
F3_End:
