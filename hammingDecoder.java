import java.io.*;
import java.lang.Math;
import java.util.Random;

public class hammingDecoder
{

	public static void main( String[] args ) throws IOException
	{
		// Create new bufferIO object to read and write to HDD
		// todo: add ability to specify files through postional parameters.
		bufferedIO fs = new bufferedIO( "encoded.txt", "decoded.txt" );
		
		// create to integer arrays, one holding encoded data
		// and the second which is half the size which stores 
		// decoded data
		int[] edata = fs.readBytes();
		int[] ddata = new int[edata.length/2];
	
		// loop through encoded bytes and store in new array
		for( int x = 0, j = 0; x < ddata.length; x++, j++ )
			ddata[x] = checkparity( edata[j], edata[++j] );

		
		fs.writeBytes( ddata );

	}

	// check parity method takes in two bytes parity and
	// data, splits each up and combines the data nibbles
	// with parity ones
	public static int checkparity( int d, int p )
	{
		// create four variables to hold the data and parity nibbles
		int data1, data2, parity1, parity2;
		// now create two more variables which hold the encoded
		// nibbles
		int dcode1, dcode2;

		// mask most significant bits for both data and parity bytes
		data1 = (byte)( d % 16 );
		parity1 = (byte)( p % 16 );

		// now shift right four and mask again to get most signifcant bits
		data2 = (byte)( ( d>>>4) % 16 );
		parity2 = (byte)( (p>>>4) % 16);

		// decode each nibble by combining the data and parity nibbles
		dcode1 = decode( data1, parity1 );
		dcode2 = decode( data2, parity2 );

		// now return the byte and multipying the most significant bits by sixteen
		return dcode1 + dcode2 * 16;
	}

	
	// method to take in an integer and corrupt
	// it randomly by chaning one bit
	public static int corruptdata( int d )
	{
		// create new instance of random
		Random rcorrupt = new Random();
	
		// generate a random float between zero to one	
		float ranNum = rcorrupt.nextFloat();
		// have a thirty percent chance of producing and error
		boolean nerrors = ranNum < 0.70 ? false : true;

		// if random number returns and error then change one byte
		if( nerrors )
		{
			// randomly select which bit postion will be changed
			int pos = rcorrupt.nextInt() % 4;
			
			d = (byte)( d | ( 1<<pos) );
		}
		return d;
	}
	
	// decode method returns the decoded
	// byte with two arguments one for
	// the data and other for parity byte
	public static int decode( int d, int p ) 
	{
		// int array for received hamming code, this will appened
		// the bits of the to data and parity
		int[] hcode = new int[8];
		// int to calculate data value
		int data = 0;

		// randomly generate an error for each nibble
		// which allows a maxium of two erros for the byte
		d = corruptdata( d );
		p = corruptdata( p );
		// get most signficant upper nibble from data
		for( int x = 0, j = 3; x < 4; x++, j-- )
			hcode[x] = ((d>>>j)&1);
		// now get least significant lower nibble from parity byte
		for( int x = 4, j = 3; x < 8; x++, j-- )
			hcode[x] = ((p>>>j)&1 );

	
		//syndrome bits
		byte s1, s2, s3, s4;
		s1 = (byte)( hcode[3] ^ hcode[4] ^ hcode[5] ^ hcode[6] );
		s2 = (byte)( hcode[1] ^ hcode[2] ^ hcode[5] ^ hcode[6] );
		s3 = (byte)( hcode[0] ^ hcode[2] ^ hcode[4] ^ hcode[6] );
		s4 = (byte)( hcode[7] ^ hcode[6] ^ hcode[5] ^ hcode[4] ^ hcode[3] ^ hcode[2] ^ hcode[1] ^ hcode[0] );
		//use syndrome bits to find and fix errors
		// fixing only needs done on first 4 data bits
		// because they are the only bits of interest
		// update: added && s4 for even parity check
		if( s1 == 1 && s2 == 0 && s3 == 0 && s4 == 1) //100 = 4, only data bit that uses most sig bit
			hcode[3] = (byte)( hcode[3] ^ 1 ); //swap bit with XOR
		else if( s1 == 0 && s2==1 && s3==1 && s4 == 1) //011 = 3, only data bit which s2 and s3 are true
			hcode[2] = (byte)( hcode[2] ^ 1 ); // swap
		else if( s1 == 0 && s2 == 1 && s3==0  && s4 == 1) //010 =2 because !s3 ( s2 & s3 checked above)
			hcode[1] = (byte)( hcode[1] ^ 1 ); //swap
		else if( s1==0  && s2 == 0 && s3 == 1 && s4 == 1 ) // 001 = 1, because !s2 and !s2&&s3 leaves only s3
			hcode[0] = (byte)( hcode[0] ^ 1 );

		if( s4 == 0 && ( s1 != 0 || s2 != 0 || s3 !=0 ))
		{
			System.out.println( "More than 2 errors data unrecoverable"  );
			return -1; 
		}
		// convert the int array  ( binary ) to decimal value	
		for( int x = 0, j=3; x < 4; x++,j-- )
			data += (int)( hcode[x] * Math.pow( 2, j ));	
		
		// and return the result
		return (int)data;	
	}	
}
