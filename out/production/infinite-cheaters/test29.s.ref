	.text
	.globl _class_A
_class_A:
	.quad _A_A
    # _main ()
    # (A, a)
# Allocation map
# t1	%rbx
# t2	%rax
# t3	%rax
# t4	%rax
# A	%rdi
# t5	%rax
# a	%rbx
	.p2align 4,0x90
	.globl _main
_main:
	pushq %rbx
    # 0.  Begin:
F0_Begin:
    # 1.   t1 = call _malloc(12)
	movq $12,%rdi
	call _malloc
	movq %rax,%rbx
    # 2.   [t1]:P = _class_A
	leaq _class_A(%rip),%r10
	movq %r10,(%rbx)
    # 3.   call _init0_A(t1)
	movq %rbx,%rdi
	call _init0_A
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
	movslq 8(%rbx),%rax
    # 10.  A = t5
	movq %rax,%rdi
    # 11.  call _printInt(A)
	call _printInt
    # 12.  return 
	popq %rbx
	ret
    # 13. End:
F0_End:
    # _init0_A (obj)
# Allocation map
# obj	%rdi
	.p2align 4,0x90
	.globl _init0_A
_init0_A:
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
    # _init_A (obj, A)
# Allocation map
# A	%rsi
# obj	%rdi
	.p2align 4,0x90
	.globl _init_A
_init_A:
	subq $8,%rsp
    # 0.  Begin:
F2_Begin:
    # 1.   8[obj]:I = A
	movl %esi,8(%rdi)
    # 2.   return 
	addq $8,%rsp
	ret
    # 3.  End:
F2_End:
    # _A_A (obj, i)
# Allocation map
# i	%rax
	.p2align 4,0x90
	.globl _A_A
_A_A:
	subq $8,%rsp
	movq %rsi,%rax
    # 0.  Begin:
F3_Begin:
    # 1.   return i
	addq $8,%rsp
	ret
    # 2.  End:
F3_End:
