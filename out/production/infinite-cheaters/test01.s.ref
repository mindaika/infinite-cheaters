	.text
    # _main ()
# Allocation map
	.p2align 4,0x90
	.globl _main
_main:
	subq $8,%rsp
    # 0.  Begin:
F0_Begin:
    # 1.   call _printStr("123")
	leaq _S0(%rip),%rdi
	call _printStr
    # 2.   call _printInt(123)
	movq $123,%rdi
	call _printInt
    # 3.   call _print()
	call _print
    # 4.   call _printBool(true)
	movq $1,%rdi
	call _printBool
    # 5.   return 
	addq $8,%rsp
	ret
    # 6.  End:
F0_End:
_S0:
	.asciz "123"
