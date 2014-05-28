	.text
    # _main ()
# Allocation map
	.p2align 4,0x90
	.globl __main
__main:
	subq $8,%rsp
    # 0.  Begin:
F0_Begin:
    # 1.   call _printStr("123")
	leaq __S0(%rip),%rdi
	call __printStr
    # 2.   call _printInt(123)
	movq $123,%rdi
	call __printInt
    # 3.   call _print()
	call __print
    # 4.   call _printBool(true)
	movq $1,%rdi
	call __printBool
    # 5.   return 
	addq $8,%rsp
	ret
    # 6.  End:
F0_End:
__S0:
	.asciz "123"
