package c99;

public class Tuple<A,B>
{
public final A a;
public final B b;

public Tuple ( A a, B b )
{
  this.a = a;
  this.b = b;
}

@Override public boolean equals ( Object o )
{
  if (this == o) return true;
  if (!(o instanceof Tuple)) return false;

  final Tuple<?, ?> tuple = (Tuple<?, ?>)o;

  return a != null ? a.equals(tuple.a) : tuple.a == null && (b != null ? b.equals(tuple.b) : tuple.b == null);

}

@Override public int hashCode ()
{
  int result = a != null ? a.hashCode() : 0;
  result = 31 * result + (b != null ? b.hashCode() : 0);
  return result;
}

@Override public String toString ()
{
  return "Tuple{" +
    "a=" + a +
    ", b=" + b +
    '}';
}
}