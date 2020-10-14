package DominoesCommunication;

import java.io.*;
import java.net.*;

public class DSCommunication
{
    private ServerSocket listeningSocket = null;
    private Socket commSocket = null;
    private final int serverPortNumb;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public DSCommunication(int portNumb)
    {
        this.serverPortNumb = portNumb;
    }

    public DSCommunication(int portNumb, ServerSocket lSocket)
    {
        this.serverPortNumb = portNumb;
        this.listeningSocket = lSocket;
    }

    public void start()
    {
        try
        {
            this.listeningSocket = new ServerSocket(this.serverPortNumb);
            this.setTimeout(10000);
        }
        catch (BindException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not associate the listening socket to " +
                    "port: " + this.serverPortNumb + "!");

            e.printStackTrace();
            System.exit(816);
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - unknown error when associating the listening " +
                    "socket to port: " + this.serverPortNumb + "!");

            e.printStackTrace();
            System.exit(817);
        }
    }

    public void end()
    {
        try
        {
            this.listeningSocket.close();
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not close the listening socket!");

            e.printStackTrace();
            System.exit(818);
        }
    }

    public DSCommunication accept() throws SocketTimeoutException
    {
        DSCommunication sCon = new DSCommunication(this.serverPortNumb, this.listeningSocket);

        try
        {
            sCon.commSocket = this.listeningSocket.accept();
        }
        catch (SocketTimeoutException e)
        {
            throw new SocketTimeoutException("Timeout!");
        }
        catch (SocketException e)
        {
            System.out.println(Thread.currentThread().getName() + " - the listening socket was closed during the " +
                    "listening process!");

            e.printStackTrace();
            System.exit(819);
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not open a communication channel due " +
                    "to a pending request!");

            e.printStackTrace();
            System.exit(820);
        }

        try
        {
            sCon.in = new ObjectInputStream(sCon.commSocket.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not open the socket's input stream!");

            e.printStackTrace();
            System.exit(821);
        }

        try
        {
            sCon.out = new ObjectOutputStream(sCon.commSocket.getOutputStream());
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not open the socket's output stream!");

            e.printStackTrace();
            System.exit(822);
        }

        return sCon;
    }

    public void close()
    {
        try
        {
            this.in.close();
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not close the socket's input stream!");

            e.printStackTrace();
            System.exit(823);
        }

        try
        {
            this.out.close();
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not close the socket's output stream!");

            e.printStackTrace();
            System.exit(824);
        }

        try
        {
            this.commSocket.close();
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not close the communication socket!");

            e.printStackTrace();
            System.exit(825);
        }
    }

    public void setTimeout(int time)
    {
        try
        {
            this.listeningSocket.setSoTimeout(time);
        }
        catch (SocketException e)
        {
            System.out.println(Thread.currentThread().getName() + " - error while setting the listening socket's " +
                    "timeout value!");

            e.printStackTrace();
            System.exit(826);
        }
    }

    public Object readObject()
    {
        Object fromClient = null;

        try
        {
            fromClient = this.in.readObject ();
        }
        catch (InvalidClassException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not deserialize the read object!");

            e.printStackTrace();
            System.exit(827);
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - error reading an object from the socket's " +
                    "input stream!");

            e.printStackTrace();
            System.exit(828);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(Thread.currentThread().getName() + " - the read object's data type is unknown!");

            e.printStackTrace();
            System.exit(829);
        }

        return fromClient;
    }

    public void writeObject(Object toClient)
    {
        try
        {
            this.out.writeObject(toClient);
        }
        catch (InvalidClassException e)
        {
            System.out.println(Thread.currentThread().getName() + " - the object being written could not be " +
                    "serialized!");

            e.printStackTrace();
            System.exit(830);
        }
        catch (NotSerializableException e)
        {
            System.out.println(Thread.currentThread().getName() + " - the object being written belongs to a " +
                    "non-serializable data type!");

            e.printStackTrace();
            System.exit(831);
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - error writing an object into the socket's " +
                    "output stream!");

            e.printStackTrace();
            System.exit(832);
        }
    }
}
