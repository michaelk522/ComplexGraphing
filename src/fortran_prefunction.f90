function arg(a, b) result(y)
    real(8), intent(in) :: a, b
    real(8) :: pi = 3.14159265
    real(8) :: y
    if (a >= 0 .and. b >= 0) then
        y = atan(b/a)
    else if (a < 0 ) then
        y = pi + atan(b/a)
    else
        y = 2*pi + atan(b/a)
    end if
end function arg

program array_maker
    implicit none
    real :: angle, mag, pi=3.141592653
    real(8) :: arg
    doublecomplex, dimension(-500:500,-500:500) :: domain, output_cartesian, output_polar
    doublecomplex :: thing = (0.0, 1.0), c, d
    integer :: a, b
    doublecomplex f

    print *, f(thing)

    do a=-500,500
        do b =-500,500
            domain(a, b) = complex(real(a)/100.0, real(b)/100.0)
        end do
    end do

    do a=-500,500
        do b =-500,500
            output_cartesian(a, b) = f(domain(a,b))
        end do
    end do

    do a=-500,500
        do b =-500,500
            mag = cdabs(output_cartesian(a,b))
            angle = arg(real(output_cartesian(a,b)), imag(output_cartesian(a,b)))
            output_polar(a, b) = complex(mag, angle)
        end do
    end do

    do a=-10,10
        do b =-10,10
            print *, domain(a,b), output_cartesian(a, b), output_polar(a,b)
        end do
    end do

    open(1, file = 'C:\Users\Michael\IdeaProjects\ComplexGraphingColors\src\data.txt', status='old')
    do a=-500,500
        do b =-500,500
            write(1,*) real(output_polar(a, b)), imag(output_polar(a, b))
        end do
    end do
    close(1)


end program array_maker

function f(z) result(y)
    doublecomplex, intent(in) :: z
    doublecomplex :: y