package DominoesSecurity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * The Serializer system.
 * Use to serialize and desirialize objects 
 * @author Ricardo Rodrigues Azevedo
 * @version 1.0
 * @since 2020-12-18
 */
public class Serializer {
    

    /**
     * Serialize an object
     * @param obj to be serialized
     * @return Serialized object in bytes
     */
    public static byte[] Serialize(Object obj) 
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ObjectOutputStream out = null;

        byte[] msgArray = null;

        try 
        {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            msgArray = bos.toByteArray();
        } catch (IOException e) 
        {
            e.printStackTrace();
            System.err.println("ERROR: Serializing message");
        } finally 
        {
            try 
            {
                bos.close();

            } catch (IOException e) 
            {
                e.printStackTrace();
                System.err.println("ERROR: closing ArrayOutputStream");
            }
        }

        return msgArray;
    }

    /**
     * Deserialize an object
     * @param obj in bytes to be deserialized
     * @return Object deserialized
     */
    public static Object Deserialize(byte[] obj) 
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
