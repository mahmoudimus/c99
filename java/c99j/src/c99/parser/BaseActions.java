package c99.parser;

import c99.*;
import c99.Types.*;

public class BaseActions
{
protected CompEnv m_compEnv;
protected SymTable m_symTab;

protected CompilerOptions m_opts;
protected IErrorReporter m_reporter;

private Types.SimpleSpec m_specs[];
private Qual m_stdQuals[];

protected Spec stdSpec ( TypeSpec ts )
{
  return m_specs[ts.ordinal() - TypeSpec.VOID.ordinal()];
}
protected Qual stdQual ( TypeSpec ts )
{
  return m_stdQuals[ts.ordinal() - TypeSpec.VOID.ordinal()];
}

private static final SimpleSpec s_errorSpec = new SimpleSpec( TypeSpec.ERROR, -1, 0 );
protected static final Qual s_errorQual = new Qual(s_errorSpec);

protected void init ( CompEnv compEnv, SymTable symTab )
{
  m_compEnv = compEnv;
  m_symTab = symTab;

  m_opts = compEnv.opts;
  m_reporter = compEnv.reporter;

  // Initialize the basic type specs
  m_specs = new SimpleSpec[TypeSpec.LDOUBLE.ordinal() - TypeSpec.VOID.ordinal() + 1];
  m_stdQuals = new Qual[m_specs.length];
  for ( int i = TypeSpec.VOID.ordinal(); i <= TypeSpec.LDOUBLE.ordinal(); ++i )
  {
    final TypeSpec type = TypeSpec.values()[i];
    int size = -1, align = 0;
    if (type.sizeOf > 0)
    {
      size = type.sizeOf;
      align = Platform.alignment( m_opts, size );
    }
    int ind = i - TypeSpec.VOID.ordinal();
    m_specs[ind] = new SimpleSpec( type, size, align );
    m_stdQuals[ind] = new Qual( m_specs[ind] );
  }
}

public final void error ( CParser.Location loc, String msg, Object... args )
{
  m_reporter.error( BisonLexer.fromLocation( loc ), msg, args );
}

public final void error ( ISourceRange rng, String msg, Object... args )
{
  m_reporter.error( rng, msg, args );
}

public final void warning ( CParser.Location loc, String msg, Object... args )
{
  m_reporter.warning( BisonLexer.fromLocation( loc ), msg, args );
}

public final void warning ( ISourceRange rng, String msg, Object... args )
{
  m_reporter.warning( rng, msg, args );
}

public final void pedWarning ( CParser.Location loc, String msg, Object... args )
{
  warning( loc, msg, args );
}

public final void extWarning ( CParser.Location loc, String msg, Object... args )
{
  warning( loc, msg, args );
}

protected final PointerSpec newPointerSpec ( Qual to )
{
  int size = Platform.pointerSize( to );
  int align = Platform.alignment( m_opts, size );
  return new PointerSpec( to, size, align );
}

protected final ArraySpec newArraySpec ( ISourceRange loc, Qual to, int nelem )
{
  assert to.spec.isComplete() && to.spec.sizeOf() >= 0;

  ArraySpec s = new ArraySpec( to );
  s.nelem = nelem;
  if (s.nelem >= 0)
  {
    int size = (s.nelem * to.spec.sizeOf()) & Integer.MAX_VALUE; // note: convert to unsigned
    // Check for int32 overflow
    if (size < s.nelem || size < to.spec.sizeOf())
    {
      error( loc, "Array size overflow" );
      return null;
    }

    s.setSizeAlign( size, to.spec.alignOf() );
  }
  return s;
}
} // class

