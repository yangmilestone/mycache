package yangwb.client.jedis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by yangwb  on 2017/1/6.
 */
public class TestNIO
{
    public static void main(String[] args) throws IOException
    {
        File file = new File("C:\\Users\\yangwb\\Desktop\\hahaha.txt");
        File file1 = new File("C:\\Users\\yangwb\\Desktop\\hehe.txt");

        FileInputStream fin = new FileInputStream(file);
        FileOutputStream fout = new FileOutputStream(file1);

        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();

        ByteBuffer bf = ByteBuffer.allocate(2);
        bf.asReadOnlyBuffer();
        while (true)
        {
            bf.clear();
            int index =fcin.read(bf);
            System.out.println(fcin.position());
//            System.out.println(bf.position());
            System.out.println(bf.limit());
            if ( index == -1 )
            {
                break;
            }
            bf.flip();
            fcout.write(bf);
//            System.out.println(fcout.position());
        }
    }
}
