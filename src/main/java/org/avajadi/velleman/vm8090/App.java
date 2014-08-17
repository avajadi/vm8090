package org.avajadi.velleman.vm8090;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	Packet testPacket = new Packet(Cmd.ON);
    	testPacket.addRelay(0);
    	final FileOutputStream fos = new FileOutputStream( "/tmp/tempout.txt" );
        // allocate a channel to write that file
        FileChannel fc = fos.getChannel();
        // allocate a buffer, as big a chunk as we are willing to handle at a pop.
        //  Unlike the buffer on a stream, item is up to you not to overflow the buffer.
        ByteBuffer buffer = ByteBuffer.allocate( 7 );
        buffer.clear();
        buffer.put( testPacket.toBytes() );
        buffer.flip();
        fc.write( buffer );
        fc.close();
        fos.close();
        
    }
}
