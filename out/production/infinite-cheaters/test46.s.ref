	.text
	.globl _class_A
_class_A:
	.quad _A_foo
	.quad _A_bar
	.globl _class_B
_class_B:
	.quad _A_foo
	.quad _A_bar
    # _main ()
    # (a, b)
# Allocation map
# t1	%rbx
# t2	%rbp
# t3	%rax
# t4	%rax
# b	%rbp
# t5	%rax
# t6	%rax
# a	%rbx
	.p2align 4,0x90
	.globl _main
_main:
	pushq %rbx
	pushq %rbp
	subq $8,%rsp
    # 0.  Begin:
F0_Begin:
    # 1.   t1 = call _malloc(16)
	movq $16,%rdi
	call _malloc
	movq %rax,%rbx
    # 2.   [t1]:P = _class_A
	leaq _class_A(%rip),%r10
	movq %r10,(%rbx)
    # 3.   call _init0_A(t1)
	movq %rbx,%rdi
	call _init0_A
    # 4.   a = t1
    # 5.   t2 = call _malloc(20)
	movq $20,%rdi
	call _malloc
	movq %rax,%rbp
    # 6.   [t2]:P = _class_B
	leaq _class_B(%rip),%r10
	movq %r10,(%rbp)
    # 7.   call _init0_B(t2)
	movq %rbp,%rdi
	call _init0_B
    # 8.   b = t2
    # 9.   t3 = [a]:P
	movq (%rbx),%rax
    # 10.  t4 = [t3]:P
	movq (%rax),%rax
    # 11.  call * t4(a)
	movq %rbx,%rdi
	call * %rax
    # 12.  8[b]:I = 10
	movl $10,8(%rbp)
    # 13.  12[b]:I = 11
	movl $11,12(%rbp)
    # 14.  t5 = [b]:P
	movq (%rbp),%rax
    # 15.  t6 = [t5]:P
	movq (%rax),%rax
    # 16.  call * t6(b)
	movq %rbp,%rdi
	call * %rax
    # 17.  return 
	addq $8,%rsp
	popq %rbp
	popq %rbx
	ret
    # 18. End:
F0_End:
    # _init0_A (obj)
# Allocation map
# t7	%rax
# t8	%rax
# obj	%rdi
	.p2align 4,0x90
	.globl _init0_A
_init0_A:
	subq $8,%rsp
    # 0.  Begin:
F1_Begin:
    # 1.   8[obj]:I = 1
	movl $1,8(%rdi)
    # 2.   t7 = 8[obj]:I
	movslq 8(%rdi),%rax
    # 3.   t8 = t7 + 1
	addq $1,%rax
    # 4.   12[obj]:I = t8
	movl %eax,12(%rdi)
    # 5.   return 
	addq $8,%rsp
	ret
    # 6.  End:
F1_End:
    # _init_A (obj, i, j)
# Allocation map
# obj	%rdi
# j	%rdx
# i	%rsi
	.p2align 4,0x90
	.globl _init_A
_init_A:
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
    # _A_foo (obj)
# Allocation map
# t1	%rax
# t2	%rax
# obj	%rdi
	.p2align 4,0x90
	.globl _A_foo
_A_foo:
	subq $8,%rsp
    # 0.  Begin:
F3_Begin:
    # 1.   t1 = [obj]:P
	movq (%rdi),%rax
    # 2.   t2 = 8[t1]:P
	movq 8(%rax),%rax
    # 3.   call * t2(obj)
	call * %rax
    # 4.   return 
	addq $8,%rsp
	ret
    # 5.  End:
F3_End:
    # _A_bar (obj)
# Allocation map
# t1	%rdi
# obj	%rdi
	.p2align 4,0x90
	.globl _A_bar
_A_bar:
	subq $8,%rsp
    # 0.  Begin:
F4_Begin:
    # 1.   t1 = 8[obj]:I
	movslq 8(%rdi),%rdi
    # 2.   call _printInt(t1)
	call _printInt
    # 3.   return 
	addq $8,%rsp
	ret
    # 4.  End:
F4_End:
    # _init0_B (obj)
# Allocation map
# t2	%rax
# t3	%rax
# obj	%rdi
	.p2align 4,0x90
	.globl _init0_B
_init0_B:
	subq $8,%rsp
    # 0.  Begin:
F5_Begin:
    # 1.   8[obj]:I = 1
	movl $1,8(%rdi)
    # 2.   t2 = 8[obj]:I
	movslq 8(%rdi),%rax
    # 3.   t3 = t2 + 1
	addq $1,%rax
    # 4.   12[obj]:I = t3
	movl %eax,12(%rdi)
    # 5.   16[obj]:I = 10
	movl $10,16(%rdi)
    # 6.   return 
	addq $8,%rsp
	ret
    # 7.  End:
F5_End:
    # _init_B (obj, i, j, k)
# Allocation map
# obj	%rdi
# j	%rdx
# k	%rcx
# i	%rsi
	.p2align 4,0x90
	.globl _init_B
_init_B:
	subq $8,%rsp
    # 0.  Begin:
F6_Begin:
    # 1.   8[obj]:I = i
	movl %esi,8(%rdi)
    # 2.   12[obj]:I = j
	movl %edx,12(%rdi)
    # 3.   16[obj]:I = k
	movl %ecx,16(%rdi)
    # 4.   return 
	addq $8,%rsp
	ret
    # 5.  End:
F6_End:
