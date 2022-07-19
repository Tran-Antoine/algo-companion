import dc_base
import dc_combine
import dc_divide

def dac (n, i, j)
{
    s = base_case(n, i, j) 
    if(s != None)
      return s
    else 
      m = divide(n, i, j)
      b = dac(n, i, m)
      c = dac(n, m, j)
      d = combine(b, c)
   return(d)
}