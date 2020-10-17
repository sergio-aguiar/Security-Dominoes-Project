package DominoesClient;

import java.util.Scanner;

public class DCMain
{
    private static final Scanner sc = new Scanner(System.in);
    private static final String serverHostName = "localhost";
    private static final int serverPort = 4000;

    private static String pseudonym;

    public static void main(String[] args)
    {
        DCStub dcStub = new DCStub(serverHostName, serverPort);

        pseudonymQuery();

        DCThread dcThread = new DCThread(pseudonym, dcStub);
        dcThread.start();
    }

    private static void pseudonymQuery()
    {
        do
        {
            System.out.print("\n[CLIENT] Insert Pseudonym: ");
            pseudonym = sc.next();
        }
        while (pseudonym.isEmpty());
    }
}
