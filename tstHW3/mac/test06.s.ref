	.text
	.globl __class_Body
__class_Body:
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
    # 1.   t1 = call _malloc(12)
	movq $12,%rdi
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
    # 6.   t3 = [t2]:P
	movq (%rax),%rax
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
# obj	%rdi
	.p2align 4,0x90
	.globl __init0_Body
__init0_Body:
	subq $8,%rsp
    # 0.  Begin:
F1_Begin:
    # 1.   8[obj]:I = 0
	movl $0,8(%rdi)
    # 2.   return 
	addq $8,%rsp
	ret
    # 3.  End:
F1_End:
    # _init_Body (obj, i)
# Allocation map
# obj	%rdi
# i	%rsi
	.p2align 4,0x90
	.globl __init_Body
__init_Body:
	subq $8,%rsp
    # 0.  Begin:
F2_Begin:
    # 1.   8[obj]:I = i
	movl %esi,8(%rdi)
    # 2.   return 
	addq $8,%rsp
	ret
    # 3.  End:
F2_End:
    # _Body_go (obj)
    # (j)
# Allocation map
# t1	%rax
# t2	%rax
# obj	%rdi
# j	%rax
	.p2align 4,0x90
	.globl __Body_go
__Body_go:
	subq $8,%rsp
    # 0.  Begin:
F3_Begin:
    # 1.   8[obj]:I = 4
	movl $4,8(%rdi)
    # 2.   t1 = 8[obj]:I
	movslq 8(%rdi),%rax
    # 3.   t2 = t1 + 2
	addq $2,%rax
    # 4.   j = t2
    # 5.   return j
	addq $8,%rsp
	ret
    # 6.  End:
F3_End:
