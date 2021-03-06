#define LOG( sev, ... )  \
  printf( #sev ": " ); \
  printf( __VA_ARGS__ )


LOG( warn, "Error" );
LOG( err, "errno %d", errno );
LOG( info, "value %d=%d", valIndex-1, value+1 );
LOG( err );

#define LOG2( sev, fmt, ... )  \
  printf( #sev ": " fmt "\n", ## __VA_ARGS__ )

LOG2( err, "errno %d", errno );
LOG2( err, "badbad", );
LOG2( err, "error" );

#define LOG3( sev, fmt, args... )  \
  printf( #sev ": " fmt "\n", ## args )

LOG3( err3, "errno %d", errno );
LOG3( err3, "badbad", );
LOG3( err3, "error" );
