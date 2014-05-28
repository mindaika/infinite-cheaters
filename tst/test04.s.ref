	.text
	.globl __class_Body
__class_Body:
    # _main ()
    # (b, i)
# Allocation map
# t1	%rbx
# t2	%rcx
# t3	%rdi
# b	%rcx
# i	%rax
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
	movq %rbx,%rcx
    # 5.   i = 2
	movq $2,%rax
    # 6.   8[b]:I = 3
	movl $3,8(%rcx)
    # 7.   t2 = 8[b]:I
	movslq 8(%rcx),%rcx
    # 8.   t3 = i + t2
	movq %rax,%rdi
	addq %rcx,%rdi
    # 9.   call _printInt(t3)
	call __printInt
    # 10.  return 
	popq %rbx
	ret
    # 11. End:
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
