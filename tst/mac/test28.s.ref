	.text
	.globl __class_A
__class_A:
	.quad __A_foo
    # _main ()
    # (a)
# Allocation map
# t1	%rbx
# t2	%rax
# t3	%rax
# t4	%rax
# t5	%rdi
# a	%rbx
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
    # 2.   [t1]:P = _class_A
	leaq __class_A(%rip),%r10
	movq %r10,(%rbx)
    # 3.   call _init0_A(t1)
	movq %rbx,%rdi
	call __init0_A
    # 4.   a = t1
    # 5.   t2 = [a]:P
	movq (%rbx),%rax
    # 6.   t3 = [t2]:P
	movq (%rax),%rax
    # 7.   t4 = call * t3(a, 1)
	movq %rbx,%rdi
	movq $1,%rsi
	call * %rax
    # 8.   8[a]:I = t4
	movl %eax,8(%rbx)
    # 9.   t5 = 8[a]:I
	movslq 8(%rbx),%rdi
    # 10.  call _printInt(t5)
	call __printInt
    # 11.  return 
	popq %rbx
	ret
    # 12. End:
F0_End:
    # _init0_A (obj)
# Allocation map
# obj	%rdi
	.p2align 4,0x90
	.globl __init0_A
__init0_A:
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
    # _init_A (obj, foo)
# Allocation map
# foo	%rsi
# obj	%rdi
	.p2align 4,0x90
	.globl __init_A
__init_A:
	subq $8,%rsp
    # 0.  Begin:
F2_Begin:
    # 1.   8[obj]:I = foo
	movl %esi,8(%rdi)
    # 2.   return 
	addq $8,%rsp
	ret
    # 3.  End:
F2_End:
    # _A_foo (obj, i)
# Allocation map
# i	%rax
	.p2align 4,0x90
	.globl __A_foo
__A_foo:
	subq $8,%rsp
	movq %rsi,%rax
    # 0.  Begin:
F3_Begin:
    # 1.   return i
	addq $8,%rsp
	ret
    # 2.  End:
F3_End:
