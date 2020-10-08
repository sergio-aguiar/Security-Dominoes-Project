package DominoesClient;

import DominoesMisc.DominoesMenus;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DCThread extends Thread
{
    public enum DCStates
    {
        AT_MAIN_MENU("AMM"),
        AT_TABLE_LEADER_MENU("ATLM"),
        AT_TABLE_GUEST_MENU("ATGM"),
        AWAITING_GAME_START("AGS"),
        AT_PIECE_DISTRIBUTION("APD");

        private final String description;

        private DCStates(String description)
        {
            this.description = description;
        }

        @Override
        public String toString() {
            return this.description;
        }
    }

    private static final Scanner sc = new Scanner(System.in);

    private String serverHost;
    private int serverPort;

    // private final DCInterface dcInterface;

    /*public DCThread(DCInterface dcInterface)
    {
        this.serverHost = "";
        this.serverPort = -1;
        this.dcInterface = dcInterface;
    }*/

    public DCThread()
    {
        this.serverHost = "";
        this.serverPort = -1;
    }

    @Override
    public void run()
    {
        System.out.println("[CLIENT] Dominoes Client starting...");
        clientInitMenu();

    }

    private boolean isServerAddressValid()
    {
        return !this.serverHost.equals("") && this.serverPort > 1023 && this.serverPort < 65536;
    }

    private void clientInitMenu()
    {
        int option;
        while (true)
        {
            DominoesMenus.clientInitMenu();

            try
            {
                option = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                sc.next();
                option = -1;
            }

            switch (option)
            {
                case 1:
                    setServerAddress();
                    break;
                case 2:
                    if (!isServerAddressValid())
                    {
                        System.out.println("\n[CLIENT] Server Address has not been set yet.");
                    }
                    else
                    {
                        System.out.println("\n[CLIENT] Attempting to connect to server...");
                        return;
                    }
                    break;
                case 3:
                    System.out.println("\n[CLIENT] Shutting down...");
                    System.exit(701);
                default:
                    System.out.println("\n[CLIENT] Invalid option.\n[CLIENT] Must be a number within range [1-3].");
            }
        }
    }

    private void setServerAddress()
    {
        while (this.serverHost.equals("") || this.serverPort < 1024 || this.serverPort > 65535)
        {
            System.out.print("\n[CLIENT] Server Address: ");
            String[] option = sc.next().split(":");

            if (option.length != 2)
            {
                System.out.println("\n[CLIENT] Incorrect format for server address.\n[CLIENT] Must be HOST:PORT.");
                continue;
            }

            String[] splitHost = option[0].split("\\.");
            if (splitHost.length != 4)
            {
                System.out.println("[CLIENT] Incorrect format for server host.\n[CLIENT] Must be IPv4.");
                continue;
            }

            boolean failed = false;
            for (int i = 0; i < splitHost.length; i++)
            {
                try
                {
                    int parse = Integer.parseInt(splitHost[i]);

                    if (i == 3)
                    {
                        if (parse < 1 || parse > 254) failed = true;
                    }
                    else
                    {
                        if (parse < 0 || parse > 255) failed = true;
                    }
                }
                catch (NumberFormatException e)
                {
                    failed = true;
                    break;
                }
            }

            if (failed)
            {
                System.out.println("[CLIENT] Incorrect format for server host.\n[CLIENT] Must be IPv4.");
                continue;
            }

            this.serverHost = option[0];

            try
            {
                this.serverPort = Integer.parseInt(option[1]);
            }
            catch (NumberFormatException e)
            {
                System.out.println("[CLIENT] Incorrect server port.\n[CLIENT] Must be a number.");
                continue;
            }

            if (this.serverPort < 1024 || this.serverPort > 65535)
            {
                System.out.println("[CLIENT] Incorrect server port.\n[CLIENT] Must be within the [1024-65535] range.");
            }

            System.out.println("[CLIENT] Successful address!");
        }
    }
}
