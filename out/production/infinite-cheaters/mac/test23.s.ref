	.text
	.globl __class_Body
__class_Body:
	.quad __Body_foo
    # _main ()
    # (b, i)
# Allocation map
# t1	%rbx
# t2	%rax
# t3	%rax
# t4	%rax
# b	%rdi
# i	%rdi
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
    # 7.   t4 = call * t3(b, 1)
	movq $1,%rsi
	call * %rax
    # 8.   i = t4
	movq %rax,%rdi
    # 9.   call _printInt(i)
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
    # _Body_foo (obj, i)
    # (y)
# Allocation map
# i	%rax
	.p2align 4,0x90
	.globl __Body_foo
__Body_foo:
	subq $8,%rsp
	movq %rsi,%rax
    # 0.  Begin:
F3_Begin:
    # 1.   return i
	addq $8,%rsp
	ret
    # 2.  End:
F3_End:
