	.text
	.globl __class_A
__class_A:
	.quad __A_foo
	.quad __A_bar
    # _main ()
    # (a, i)
# Allocation map
# t1	%rbx
# t2	%rax
# t3	%rax
# t4	%rax
# a	%rdi
# i	%rdi
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
    # 7.   t4 = call * t3(a, 2)
	movq $2,%rsi
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
    # _A_foo (obj, i)
# Allocation map
# t1	%rax
# t2	%rax
# t3	%rax
# obj	%rdi
# i	%rsi
	.p2align 4,0x90
	.globl __A_foo
__A_foo:
	subq $8,%rsp
    # 0.  Begin:
F3_Begin:
    # 1.   if i <= 1 goto L0
	cmpq $1,%rsi
	jle F3_L0
    # 2.   t1 = [obj]:P
	movq (%rdi),%rax
    # 3.   t2 = 8[t1]:P
	movq 8(%rax),%rax
    # 4.   t3 = call * t2(obj)
	call * %rax
    # 5.   return t3
	addq $8,%rsp
	ret
    # 6.   goto L1
	jmp F3_L1
    # 7.  L0:
F3_L0:
    # 8.   return 3
	movq $3,%rax
	addq $8,%rsp
	ret
    # 9.  L1:
F3_L1:
    # 10. End:
F3_End:
    # _A_bar (obj)
# Allocation map
# t1	%rax
# t2	%rax
# t3	%rax
# obj	%rdi
	.p2align 4,0x90
	.globl __A_bar
__A_bar:
	subq $8,%rsp
    # 0.  Begin:
F4_Begin:
    # 1.   t1 = [obj]:P
	movq (%rdi),%rax
    # 2.   t2 = [t1]:P
	movq (%rax),%rax
    # 3.   t3 = call * t2(obj, 1)
	movq $1,%rsi
	call * %rax
    # 4.   return t3
	addq $8,%rsp
	ret
    # 5.  End:
F4_End:
