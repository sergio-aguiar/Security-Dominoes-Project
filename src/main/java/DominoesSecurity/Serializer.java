package DominoesSecurity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer
{
    public static byte[] serialize(Object obj)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        byte[] msgArray = null;

        try 
        {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            msgArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            System.out.println("Serializer: serialize: " + e.toString());
        }
        finally
        {
            try 
            {
                bos.close();

            }
            catch (IOException e)
            {
                System.out.println("Serializer: serialize: " + e.toString());
            }
        }

        return msgArray;
    }

    public static Object deserialize(byte[] obj)
    {
        ByteArrayInputStream in = new ByteArrayInputStream(obj);

        ObjectInputStream oInputStream = null;

        try 
        {
            oInputStream = new ObjectInputStream(in);
        } catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("ERROR: Initializing Input Stream");
        }

        Object result = null;

        try 
        {
            result = oInputStream.readObject();

        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.err.println("ERROR: Object doesn't have a valid Class");
        } catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("ERROR: Serializing message");
        }
        finally
        {
            try 
            {
                in.close();

            } catch (IOException e)
            {
                e.printStackTrace();
                System.err.println("ERROR: closing ArrayOutputStream");
            }
        }

       return result;        
    }

}
