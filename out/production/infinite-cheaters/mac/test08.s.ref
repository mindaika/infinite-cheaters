	.text
	.globl __class_Body
__class_Body:
	.quad __Body_value
	.quad __Body_go
    # _main ()
    # (b)
# Allocation map
# t1	%rbx
# t2	%rax
# t3	%rax
# t4	%rdi
# b	%rdi
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
    # 2.   [t1]:P = _class_Body
	leaq __class_Body(%rip),%r10
	movq %r10,(%rbx)
    # 3.   call _init0_Body(t1)
	movq %rbx,%rdi
	call __init0_Body
    # 4.   b = t1
	movq %rbx,%rdi
    # 5.   t2 = [b]:P
	movq (%rdi),%rax
    # 6.   t3 = 8[t2]:P
	movq 8(%rax),%rax
    # 7.   t4 = call * t3(b)
	call * %rax
	movq %rax,%rdi
    # 8.   call _printInt(t4)
	call __printInt
    # 9.   return 
	popq %rbx
	ret
    # 10. End:
F0_End:
    # _init0_Body (obj)
# Allocation map
	.p2align 4,0x90
	.globl __init0_Body
__init0_Body:
	subq $8,%rsp
    # 0.  Begin:
F1_Begin:
    # 1.   return 
	addq $8,%rsp
	ret
    # 2.  End:
F1_End:
    # _init_Body (obj)
# Allocation map
	.p2align 4,0x90
	.globl __init_Body
__init_Body:
	subq $8,%rsp
    # 0.  Begin:
F2_Begin:
    # 1.   return 
	addq $8,%rsp
	ret
    # 2.  End:
F2_End:
    # _Body_value (obj, i, j, k)
# Allocation map
# t1	%rax
# t2	%rax
# j	%rdx
# k	%rcx
# i	%rsi
	.p2align 4,0x90
	.globl __Body_value
__Body_value:
	subq $8,%rsp
    # 0.  Begin:
F3_Begin:
    # 1.   t1 = i + j
	movq %rsi,%rax
	addq %rdx,%rax
    # 2.   t2 = t1 + k
	addq %rcx,%rax
    # 3.   return t2
	addq $8,%rsp
	ret
    # 4.  End:
F3_End:
    # _Body_go (obj)
# Allocation map
# t1	%rax
# t2	%rax
# t3	%rbp
# t4	%rax
# t5	%rax
# t6	%rax
# t7	%rax
# obj	%rbx
	.p2align 4,0x90
	.globl __Body_go
__Body_go:
	pushq %rbx
	pushq %rbp
	subq $8,%rsp
	movq %rdi,%rbx
    # 0.  Begin:
F4_Begin:
    # 1.   t1 = [obj]:P
	movq (%rbx),%rax
    # 2.   t2 = [t1]:P
	movq (%rax),%rax
    # 3.   t3 = call * t2(obj, 1, 1, 1)
	movq %rbx,%rdi
	movq $1,%rsi
	movq $1,%rdx
	movq $1,%rcx
	call * %rax
	movq %rax,%rbp
    # 4.   t4 = [obj]:P
	movq (%rbx),%rax
    # 5.   t5 = [t4]:P
	movq (%rax),%rax
    # 6.   t6 = call * t5(obj, 2, 2, 2)
	movq %rbx,%rdi
	movq $2,%rsi
	movq $2,%rdx
	movq $2,%rcx
	call * %rax
    # 7.   t7 = t3 + t6
	movq %rax,%r10
	movq %rbp,%rax
	addq %r10,%rax
    # 8.   return t7
	addq $8,%rsp
	popq %rbp
	popq %rbx
	ret
    # 9.  End:
F4_End:
