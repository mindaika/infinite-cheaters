	.text
	.globl _class_Body
_class_Body:
	.quad _Body_go
    # _main ()
    # (b)
# Allocation map
# t1	%rbx
# t2	%rax
# t3	%rax
# b	%rdi
	.p2align 4,0x90
	.globl _main
_main:
	pushq %rbx
    # 0.  Begin:
F0_Begin:
    # 1.   t1 = call _malloc(8)
	movq $8,%rdi
	call _malloc
	movq %rax,%rbx
    # 2.   [t1]:P = _class_Body
	leaq _class_Body(%rip),%r10
	movq %r10,(%rbx)
    # 3.   call _init0_Body(t1)
	movq %rbx,%rdi
	call _init0_Body
    # 4.   b = t1
	movq %rbx,%rdi
    # 5.   t2 = [b]:P
	movq (%rdi),%rax
    # 6.   t3 = [t2]:P
	movq (%rax),%rax
    # 7.   call * t3(b)
	call * %rax
    # 8.   return 
	popq %rbx
	ret
    # 9.  End:
F0_End:
    # _init0_Body (obj)
# Allocation map
	.p2align 4,0x90
	.globl _init0_Body
_init0_Body:
	subq $8,%rsp
    # 0.  Begin:
F1_Begin:
    # 1.   return 
	addq $8,%rsp
	ret
    # 2.  End:
F1_End:
    # _init_Body (obj)
# Allocation map
	.p2align 4,0x90
	.globl _init_Body
_init_Body:
	subq $8,%rsp
    # 0.  Begin:
F2_Begin:
    # 1.   return 
	addq $8,%rsp
	ret
    # 2.  End:
F2_End:
    # _Body_go (obj)
# Allocation map
	.p2align 4,0x90
	.globl _Body_go
_Body_go:
	subq $8,%rsp
    # 0.  Begin:
F3_Begin:
    # 1.   call _printStr("Go!")
	leaq _S0(%rip),%rdi
	call _printStr
    # 2.   return 
	addq $8,%rsp
	ret
    # 3.  End:
F3_End:
_S0:
	.asciz "Go!"
