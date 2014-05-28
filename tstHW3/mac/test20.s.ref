	.text
	.globl __class_A
__class_A:
	.quad __A_go
	.quad __A_back
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
    # 7.   t4 = call * t3(a, 5)
	movq $5,%rsi
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
    # _A_go (obj, n)
    # (i)
# Allocation map
# t1	%rsi
# t2	%rax
# t3	%rax
# t4	%rax
# n	%rbp
# obj	%rbx
# i	%rax
	.p2align 4,0x90
	.globl __A_go
__A_go:
	pushq %rbx
	pushq %rbp
	subq $8,%rsp
	movq %rdi,%rbx
	movq %rsi,%rbp
    # 0.  Begin:
F3_Begin:
    # 1.   i = 0
	movq $0,%rax
    # 2.   if n <= 0 goto L0
	cmpq $0,%rbp
	jle F3_L0
    # 3.   call _printInt(n)
	movq %rbp,%rdi
	call __printInt
    # 4.   t1 = n - 1
	movq %rbp,%rsi
	subq $1,%rsi
    # 5.   t2 = [obj]:P
	movq (%rbx),%rax
    # 6.   t3 = 8[t2]:P
	movq 8(%rax),%rax
    # 7.   t4 = call * t3(obj, t1)
	movq %rbx,%rdi
	call * %rax
    # 8.   i = t4
    # 9.  L0:
F3_L0:
    # 10.  return i
	addq $8,%rsp
	popq %rbp
	popq %rbx
	ret
    # 11. End:
F3_End:
    # _A_back (obj, n)
    # (i)
# Allocation map
# t1	%rax
# t2	%rax
# t3	%rax
# n	%rsi
# obj	%rdi
	.p2align 4,0x90
	.globl __A_back
__A_back:
	subq $8,%rsp
    # 0.  Begin:
F4_Begin:
    # 1.   t1 = [obj]:P
	movq (%rdi),%rax
    # 2.   t2 = [t1]:P
	movq (%rax),%rax
    # 3.   t3 = call * t2(obj, n)
	call * %rax
    # 4.   i = t3
    # 5.   return 0
	movq $0,%rax
	addq $8,%rsp
	ret
    # 6.  End:
F4_End:
