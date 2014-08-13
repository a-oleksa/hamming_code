import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class bufferedIO
{
	/*public static void main( String[] args ) throws IOException
	{
		int[] data = readBytes( "test.txt" );
		int[] data2 = new int[data.length*2];
	
		System.out.println( data2.length + " " + data.length );	
		/*while( x < data.length )
		{
			data2[j] = data[x];
			System.out.println( "j " + j );
			data2[j++] = modify( data[x] );
			System.out.println( "j2" + j );
			x++;
		}
		//System.out.println( 
		for( int x = 0, j=0; x < data.length; x++, j++)
		{
			data2[j] = data[x];
			data2[++j] = modify( data2[x] );
		}
	
		writeBytes( "out.txt", data2 );
	}*/

	private BufferedInputStream fIn; 
	private BufferedOutputStream fOut; 
	public bufferedIO( String srcFile, String dstFile ) throws IOException
	{
		fIn = new BufferedInputStream(
			new FileInputStream( srcFile ));

		fOut = new BufferedOutputStream(
			new FileOutputStream(  dstFile ));

	}


	public int[] readBytes( ) throws IOException 
	{

		int size = fIn.available();
		int bytes[] = new int[size-1];
		for( int x = 0; x < bytes.length ; x++ )
			bytes[x] = fIn.read();
		fIn.close();
		return bytes;
	}

	public void writeBytes( int[] data ) throws IOException 
	{

		for( int x = 0; x < data.length; x++ )
			fOut.write( data[x] );
		
		// write a CR to replace the one not read in	
		fOut.write(10);	

		fOut.close();
		return;	
	}	
}
