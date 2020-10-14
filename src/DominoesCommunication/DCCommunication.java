package DominoesCommunication;

import java.io.*;
import java.net.*;

public class DCCommunication
{
    private Socket commSocket;
    private final String serverHostName;
    private final int serverHostPort;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public DCCommunication(String hostName, int hostPort)
    {
        this.serverHostName = hostName;
        this.serverHostPort = hostPort;
    }

    public boolean open()
    {
        boolean success = true;
        SocketAddress serverAddress = new InetSocketAddress(this.serverHostName, this.serverHostPort);

        try
        {
            this.commSocket = new Socket();
            this.commSocket.connect(serverAddress);
        }
        catch (UnknownHostException e)
        {
            System.out.println(Thread.currentThread().getName() + " - the target server is an unknown host: "
                    + this.serverHostName + "!");

            e.printStackTrace();
            System.exit(801);
        }
        catch (NoRouteToHostException e)
        {
            System.out.println(Thread.currentThread().getName() + " - the target server is unreachable: "
                    + this.serverHostName + "!");

            e.printStackTrace();
            System.exit(802);
        }
        catch (ConnectException e)
        {
            System.out.println(Thread.currentThread().getName() + " - the server did not respond at: "
                    + this.serverHostName + "." + this.serverHostPort + "!");

            if (e.getMessage().equals("Connection refused")) success = false;
            else
            {
                System.out.println(e.getMessage() + "!");

                e.printStackTrace();
                System.exit(803);
            }
        }
        catch (SocketTimeoutException e)
        {
            System.out.println(Thread.currentThread().getName() + " - connection timed out while attempting to reach: "
                    + this.serverHostName + "." + this.serverHostPort + "!");

            success = false;
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - unidentified error while connecting to: "
                    + this.serverHostName + "." + this.serverHostPort + "!");

            e.printStackTrace();
            System.exit(804);
        }

        if (!success) return false;

        try
        {
            this.outputStream = new ObjectOutputStream(this.commSocket.getOutputStream());
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not open the socket's output stream!");

            e.printStackTrace();
            System.exit(805);
        }

        try
        {
            this.inputStream = new ObjectInputStream(this.commSocket.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread ().getName () + " - could not open the socket's input stream!");

            e.printStackTrace();
            System.exit(806);
        }

        return true;
    }

    public void close()
    {
        try
        {
            this.inputStream.close();
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not close the socket's input stream!");

            e.printStackTrace();
            System.exit(807);
        }

        try
        {
            this.outputStream.close();
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not close the socket's output stream!");

            e.printStackTrace();
            System.exit(808);
        }

        try
        {
            this.commSocket.close();
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not close the communication socket!");

            e.printStackTrace();
            System.exit(809);
        }
    }

    public Object readObject()
    {
        Object fromServer = null;

        try
        {
            fromServer = this.inputStream.readObject();
        }
        catch (InvalidClassException e)
        {
            System.out.println(Thread.currentThread().getName() + " - could not deserialize the read object!");

            e.printStackTrace();
            System.exit(810);
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - error reading an object from the socket's input "
                    + "stream!");

            e.printStackTrace();
            System.exit(811);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(Thread.currentThread().getName() + " - the read object's data type is unknown!");

            e.printStackTrace();
            System.exit(812);
        }

        return fromServer;
    }

    public void writeObject(Object toServer)
    {
        try
        {
            this.outputStream.writeObject(toServer);
        }
        catch (InvalidClassException e)
        {
            System.out.println(Thread.currentThread().getName() + " - the object being written could not be " +
                    "serialized!");

            e.printStackTrace();
            System.exit(813);
        }
        catch (NotSerializableException e)
        {
            System.out.println(Thread.currentThread().getName() + " - the object being written belongs to a " +
                    "non-serializable data type!");

            e.printStackTrace();
            System.exit(814);
        }
        catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName() + " - error writing an object into the socket's output "
                    + "stream!");

            e.printStackTrace();
            System.exit(815);
        }
    }
}
