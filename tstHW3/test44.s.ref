	.text
	.globl _class_Body
_class_Body:
    # _main ()
    # (b)
# Allocation map
# t1	%rbx
# t2	%rcx
# t3	%rax
# t4	%rdi
# b	%rax
	.p2align 4,0x90
	.globl _main
_main:
	pushq %rbx
    # 0.  Begin:
F0_Begin:
    # 1.   t1 = call _malloc(16)
	movq $16,%rdi
	call _malloc
	movq %rax,%rbx
    # 2.   [t1]:P = _class_Body
	leaq _class_Body(%rip),%r10
	movq %r10,(%rbx)
    # 3.   call _init0_Body(t1)
	movq %rbx,%rdi
	call _init0_Body
    # 4.   b = t1
	movq %rbx,%rax
    # 5.   t2 = 8[b]:I
	movslq 8(%rax),%rcx
    # 6.   t3 = 12[b]:I
	movslq 12(%rax),%rax
    # 7.   t4 = t2 + t3
	movq %rcx,%rdi
	addq %rax,%rdi
    # 8.   call _printInt(t4)
	call _printInt
    # 9.   return 
	popq %rbx
	ret
    # 10. End:
F0_End:
    # _init0_Body (obj)
# Allocation map
# t5	%rax
# t6	%rax
# obj	%rdi
	.p2align 4,0x90
	.globl _init0_Body
_init0_Body:
	subq $8,%rsp
    # 0.  Begin:
F1_Begin:
    # 1.   8[obj]:I = 5
	movl $5,8(%rdi)
    # 2.   t5 = 8[obj]:I
	movslq 8(%rdi),%rax
    # 3.   t6 = t5 + 2
	addq $2,%rax
    # 4.   12[obj]:I = t6
	movl %eax,12(%rdi)
    # 5.   return 
	addq $8,%rsp
	ret
    # 6.  End:
F1_End:
    # _init_Body (obj, i, j)
# Allocation map
# obj	%rdi
# j	%rdx
# i	%rsi
	.p2align 4,0x90
	.globl _init_Body
_init_Body:
	subq $8,%rsp
    # 0.  Begin:
F2_Begin:
    # 1.   8[obj]:I = i
	movl %esi,8(%rdi)
    # 2.   12[obj]:I = j
	movl %edx,12(%rdi)
    # 3.   return 
	addq $8,%rsp
	ret
    # 4.  End:
F2_End:
