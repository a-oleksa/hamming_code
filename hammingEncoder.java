import java.io.*;
import java.lang.Math;
public class hammingEncoder
{
	public static void main( String[] args ) throws IOException
	{
		// create new instance of buffered file IO for reading
		// and writing to files
		// todo: add ability to specify files through postional parameters.
		bufferedIO fs = new bufferedIO( "input.txt", "encoded.txt" );	
		// store contents of file in int array, also create array
		// for encoded data with size data*2 because of the parity
		// bits
		int[] data = fs.readBytes( );
		int[] edata = new int[data.length*2];
		// now loop through bytes to encoding them and save them
		// to the encoded array
		for( int x = 0, j = 0; x < data.length; x++, j++ )
		{
			// store the original byte first
			edata[j] = data[x];
			// now store parity byte next
			edata[++j] = genparity( data[x] );
		}		

		// and not write the encoded file to the disk
		fs.writeBytes( edata );
	}
	
	// method to generate parity of data
	// this method spilts the byte into
	// nibbles encodes the nibbles and 
	// appends the values together
	private static int genparity( int d )
	{
		// create integers to hold each parity nibble
		int part1, part2;

		// get the least signifcant nibble
		part1 = (byte)( d % 16 );
		// get most significant nibble by shifting 4 right
		part2 = (byte)( ( d>>>4) % 16 );

		// encode then append the nibbles by adding the 
		// lowest signicant nibble
		// to the most significant which is multiplied by 16
		return encoder( part1 ) + (encoder( part2 ) * 16);
	}	


	private static int encoder( int d  )
	{
		// create variables for holding bits inputed
		int b1, b2, b3, b4;
		// create array to hold 4 parity bits + the integer value 
		// for the bits
		int[] hcode = new int[5];	

		// get binary value from integer by shifting
		// right for most significant bit and ANDING the result
		b4 = (byte)(d & 1);
		b3 = (byte)((d>>>1) & 1 );
		b2 = (byte)((d>>>2) & 1 );
		b1 = (byte)((d>>>3) & 1 );
		

		//encode and calculate parity bits
		hcode[0] = (byte)(b2^b3^b4);
		hcode[1] = (byte)(b1^b3^b4);
		hcode[2] = (byte)(b1^b2^b4);
		hcode[3] = (byte)(b1^b2^b3^b4^hcode[0]^hcode[1]^hcode[2] );		
		// now convert bits to integer using
		// sigma ( bit * 2^postion )
		for( int x = 0, j=3; x < 4; x++,j-- )
			hcode[4] += ((int)hcode[x] * Math.pow(2,j ) );
	
		// now return the parity bit	
		return hcode[4]; 
	}

}
