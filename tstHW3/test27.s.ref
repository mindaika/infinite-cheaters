	.text
	.globl _class_A
_class_A:
	.globl _class_B
_class_B:
    # _main ()
    # (x, y)
# Allocation map
# t1	%rbx
# t2	%rbp
# t3	%rdi
# t4	%rax
# t5	%rdi
# y	%rbp
# x	%rbx
	.p2align 4,0x90
	.globl _main
_main:
	pushq %rbx
	pushq %rbp
	subq $8,%rsp
    # 0.  Begin:
F0_Begin:
    # 1.   t1 = call _malloc(20)
	movq $20,%rdi
	call _malloc
	movq %rax,%rbx
    # 2.   [t1]:P = _class_A
	leaq _class_A(%rip),%r10
	movq %r10,(%rbx)
    # 3.   call _init0_A(t1)
	movq %rbx,%rdi
	call _init0_A
    # 4.   x = t1
    # 5.   t2 = call _malloc(16)
	movq $16,%rdi
	call _malloc
	movq %rax,%rbp
    # 6.   [t2]:P = _class_B
	leaq _class_B(%rip),%r10
	movq %r10,(%rbp)
    # 7.   call _init0_B(t2)
	movq %rbp,%rdi
	call _init0_B
    # 8.   y = t2
    # 9.   t3 = 8[x]:I
	movslq 8(%rbx),%rdi
    # 10.  call _printInt(t3)
	call _printInt
    # 11.  t4 = 8[y]:P
	movq 8(%rbp),%rax
    # 12.  t5 = 8[t4]:I
	movslq 8(%rax),%rdi
    # 13.  call _printInt(t5)
	call _printInt
    # 14.  return 
	addq $8,%rsp
	popq %rbp
	popq %rbx
	ret
    # 15. End:
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
    # 1.   8[obj]:I = 1
	movl $1,8(%rdi)
    # 2.   12[obj]:P = 0
	movq $0,12(%rdi)
    # 3.   return 
	addq $8,%rsp
	ret
    # 4.  End:
F1_End:
    # _init_A (obj, i, b)
# Allocation map
# b	%rdx
# obj	%rdi
# i	%rsi
	.p2align 4,0x90
	.globl _init_A
_init_A:
	subq $8,%rsp
    # 0.  Begin:
F2_Begin:
    # 1.   8[obj]:I = i
	movl %esi,8(%rdi)
    # 2.   12[obj]:P = b
	movq %rdx,12(%rdi)
    # 3.   return 
	addq $8,%rsp
	ret
    # 4.  End:
F2_End:
    # _init0_B (obj)
# Allocation map
# t6	%rbp
# obj	%rbx
	.p2align 4,0x90
	.globl _init0_B
_init0_B:
	pushq %rbx
	pushq %rbp
	subq $8,%rsp
	movq %rdi,%rbx
    # 0.  Begin:
F3_Begin:
    # 1.   t6 = call _malloc(20)
	movq $20,%rdi
	call _malloc
	movq %rax,%rbp
    # 2.   [t6]:P = _class_A
	leaq _class_A(%rip),%r10
	movq %r10,(%rbp)
    # 3.   call _init0_A(t6)
	movq %rbp,%rdi
	call _init0_A
    # 4.   8[obj]:P = t6
	movq %rbp,8(%rbx)
    # 5.   return 
	addq $8,%rsp
	popq %rbp
	popq %rbx
	ret
    # 6.  End:
F3_End:
    # _init_B (obj, a)
# Allocation map
# a	%rsi
# obj	%rdi
	.p2align 4,0x90
	.globl _init_B
_init_B:
	subq $8,%rsp
    # 0.  Begin:
F4_Begin:
    # 1.   8[obj]:P = a
	movq %rsi,8(%rdi)
    # 2.   return 
	addq $8,%rsp
	ret
    # 3.  End:
F4_End:
