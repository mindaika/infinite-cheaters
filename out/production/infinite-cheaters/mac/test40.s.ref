	.text
	.globl __class_A
__class_A:
	.quad __A_setk
    # _main ()
    # (a)
# Allocation map
# t1	%rbx
# t2	%rax
# t3	%rax
# t4	%rdi
# t5	%rdi
# t6	%rdi
# a	%rbx
	.p2align 4,0x90
	.globl __main
__main:
	pushq %rbx
    # 0.  Begin:
F0_Begin:
    # 1.   t1 = call _malloc(24)
	movq $24,%rdi
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
    # 7.   call * t3(a, 5)
	movq %rbx,%rdi
	movq $5,%rsi
	call * %rax
    # 8.   t4 = 12[a]:I
	movslq 12(%rbx),%rdi
    # 9.   call _printInt(t4)
	call __printInt
    # 10.  t5 = 16[a]:I
	movslq 16(%rbx),%rdi
    # 11.  call _printInt(t5)
	call __printInt
    # 12.  t6 = 20[a]:I
	movslq 20(%rbx),%rdi
    # 13.  call _printInt(t6)
	call __printInt
    # 14.  return 
	popq %rbx
	ret
    # 15. End:
F0_End:
    # _init0_A (obj)
# Allocation map
# t7	%rax
# t8	%rax
# obj	%rdi
	.p2align 4,0x90
	.globl __init0_A
__init0_A:
	subq $8,%rsp
    # 0.  Begin:
F1_Begin:
    # 1.   8[obj]:I = 2
	movl $2,8(%rdi)
    # 2.   t7 = 8[obj]:I
	movslq 8(%rdi),%rax
    # 3.   12[obj]:I = t7
	movl %eax,12(%rdi)
    # 4.   16[obj]:I = 3
	movl $3,16(%rdi)
    # 5.   t8 = 16[obj]:I
	movslq 16(%rdi),%rax
    # 6.   20[obj]:I = t8
	movl %eax,20(%rdi)
    # 7.   return 
	addq $8,%rsp
	ret
    # 8.  End:
F1_End:
    # _init_A (obj, i, j, k, l)
# Allocation map
# l	%r8
# obj	%rdi
# j	%rdx
# k	%rcx
# i	%rsi
	.p2align 4,0x90
	.globl __init_A
__init_A:
	subq $8,%rsp
    # 0.  Begin:
F2_Begin:
    # 1.   8[obj]:I = i
	movl %esi,8(%rdi)
    # 2.   12[obj]:I = j
	movl %edx,12(%rdi)
    # 3.   16[obj]:I = k
	movl %ecx,16(%rdi)
    # 4.   20[obj]:I = l
	movl %r8d,20(%rdi)
    # 5.   return 
	addq $8,%rsp
	ret
    # 6.  End:
F2_End:
    # _A_setk (obj, i)
# Allocation map
# obj	%rdi
# i	%rsi
	.p2align 4,0x90
	.globl __A_setk
__A_setk:
	subq $8,%rsp
    # 0.  Begin:
F3_Begin:
    # 1.   16[obj]:I = i
	movl %esi,16(%rdi)
    # 2.   return 
	addq $8,%rsp
	ret
    # 3.  End:
F3_End:
