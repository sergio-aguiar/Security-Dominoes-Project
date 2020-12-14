package DominoesServer;

import DominoesCommunication.DMessage;
import DominoesCommunication.DMessageException;
import DominoesMisc.DominoesDeck;
import DominoesMisc.DominoesGameState;
import DominoesMisc.DominoesTable;

public class DSInterface
{
    private final DSImplementation dsImplementation;

    public DSInterface(DSImplementation dsImplementation)
    {
        this.dsImplementation = dsImplementation;
    }

    public DMessage processAndReply(DMessage inMessage) throws DMessageException
    {
        DMessage outMessage = null;

        switch(inMessage.getMessageType())
        {
            case 1:
                if (inMessage.noFirstArgument())
                    throw new DMessageException("Argument \"playerCap\" was not given", inMessage);
                if ((int) inMessage.getFirstArgument() < 2 || (int) inMessage.getFirstArgument() > 4)
                    throw new DMessageException("Argument \"playerCap\" was given an incorrect value", inMessage);
                break;
            case 2:
            case 4:
            case 5:
                break;
            case 3:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 16:
            case 18:
            case 19:
            case 21:
            case 22:
            case 23:
                if (inMessage.noFirstArgument())
                    throw new DMessageException("Argument \"tableID\" was not given", inMessage);
                if ((int) inMessage.getFirstArgument() < 0)
                    throw new DMessageException("Argument \"tableID\" was given an incorrect value", inMessage);
                break;
            case 15:
                if (inMessage.noFirstArgument())
                    throw new DMessageException("Argument \"tableID\" was not given", inMessage);
                if ((int) inMessage.getFirstArgument() < 0)
                    throw new DMessageException("Argument \"tableID\" was given an incorrect value", inMessage);
                if (inMessage.noSecondArgument())
                    throw new DMessageException("Argument \"deck\" was not given", inMessage);
                if (inMessage.noThirdArgument())
                    throw new DMessageException("Argument \"cardDif\" was not given", inMessage);
                if ((int) inMessage.getThirdArgument() < -1 || (int) inMessage.getThirdArgument() > 1)
                    throw new DMessageException("Argument \"cardDif\" was given an incorrect value", inMessage);
                break;
            case 17:
                if (inMessage.noFirstArgument())
                    throw new DMessageException("Argument \"tableID\" was not given", inMessage);
                if ((int) inMessage.getFirstArgument() < 0)
                    throw new DMessageException("Argument \"tableID\" was given an incorrect value", inMessage);
                if (inMessage.noSecondArgument())
                    throw new DMessageException("Argument \"bitCommitment\" was not given", inMessage);
                if (inMessage.getSecondArgument().equals(""))
                    throw new DMessageException("Argument \"bitCommitment\" was given an incorrect value", inMessage);
                break;
            case 20:
                if (inMessage.noFirstArgument())
                    throw new DMessageException("Argument \"tableID\" was not given", inMessage);
                if ((int) inMessage.getFirstArgument() < 0)
                    throw new DMessageException("Argument \"tableID\" was given an incorrect value", inMessage);
                if (inMessage.noSecondArgument())
                    throw new DMessageException("Argument \"piece\" was not given", inMessage);
                String[] splitArg = ((String) inMessage.getSecondArgument()).split("\\|");
                if (!inMessage.getSecondArgument().equals("None")
                        && (splitArg.length != 2 || splitArg[0].equals("") || splitArg[1].equals("")))
                    throw new DMessageException("Argument \"piece\" was given an incorrect value", inMessage);
                break;
            default:
                throw new DMessageException("Invalid message type: " + inMessage.getMessageType());
        }

        switch(inMessage.getMessageType())
        {
            case 1:
                int return1 = this.dsImplementation.createTable(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.CREATE_TABLE_REQUEST.getMessageCode(), return1);
                break;
            case 2:
                DominoesTable[] return2 = this.dsImplementation.listAvailableTables();
                outMessage = new DMessage(DMessage.MessageType.LIST_TABLES_REQUEST.getMessageCode(), return2);
                break;
            case 3:
                boolean return3 = this.dsImplementation.joinTable(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.JOIN_TABLE_REQUEST.getMessageCode(), return3);
                break;
            case 4:
                int return4 = this.dsImplementation.joinRandomTable(inMessage.getPseudonym());
                outMessage = new DMessage(DMessage.MessageType.JOIN_RANDOM_TABLE_REQUEST.getMessageCode(), return4);
                break;
            case 5:
                DominoesTable return5 = this.dsImplementation.listTableInfo(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.LIST_TABLE_INFO_REQUEST.getMessageCode(), return5);
                break;
            case 6:
                this.dsImplementation.disbandTable(inMessage.getPseudonym(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.DISBAND_TABLE_REQUEST.getMessageCode(), (Object) null);
                break;
            case 7:
                this.dsImplementation.leaveTable(inMessage.getPseudonym(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.LEAVE_TABLE_REQUEST.getMessageCode(), (Object) null);
                break;
            case 8:
                boolean return8 = this.dsImplementation.startGame(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.START_GAME_REQUEST.getMessageCode(), return8);
                break;
            case 9:
                boolean return9 = this.dsImplementation.markAsReady(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.MARK_AS_READY_REQUEST.getMessageCode(), return9);
                break;
            case 10:
                boolean return10 = this.dsImplementation.isPlayerTurn(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.TURN_CHECK_REQUEST.getMessageCode(), return10);
                break;
            case 11:
                boolean return11 = this.dsImplementation.hasGameEnded(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.GAME_STATE_REQUEST.getMessageCode(), return11);
                break;
            case 12:
                boolean return12 = this.dsImplementation.isDeckSorting(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.DISTRIBUTION_STATE_REQUEST.getMessageCode(), return12);
                break;
            case 13:
                boolean return13 = this.dsImplementation.canDraw(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.CAN_DRAW_REQUEST.getMessageCode(), return13);
                break;
            case 14:
                DominoesDeck return14 = this.dsImplementation.getDeck(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.DECK_FETCH_REQUEST.getMessageCode(), return14);
                break;
            case 15:
                boolean return15 = this.dsImplementation.returnDeck(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument(), (DominoesDeck) inMessage.getSecondArgument(),
                        (int) inMessage.getThirdArgument());
                outMessage = new DMessage(DMessage.MessageType.DECK_RETURN_REQUEST.getMessageCode(), return15);
                break;
            case 16:
                this.dsImplementation.skipTurn(inMessage.getPseudonym(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.SKIP_TURN_REQUEST.getMessageCode(), (Object) null);
                break;
            case 17:
                boolean return17 = this.dsImplementation.commitHand(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument(), (String) inMessage.getSecondArgument());
                outMessage = new DMessage(DMessage.MessageType.COMMIT_HAND_REQUEST.getMessageCode(), return17);
                break;
            case 18:
                boolean return18 = this.dsImplementation.hasPlayerCommitted(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.COMMIT_STATE_REQUEST.getMessageCode(), return18);
                break;
            case 19:
                boolean return19 = this.dsImplementation.isHandlingStart(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.START_HANDLING_STATE_REQUEST.getMessageCode(), return19);
                break;
            case 20:
                this.dsImplementation.stateHighestDouble(inMessage.getPseudonym(), (int) inMessage.getFirstArgument(),
                        (String) inMessage.getSecondArgument());
                outMessage = new DMessage(DMessage.MessageType.DOUBLE_STATING_REQUEST.getMessageCode(), (Object) null);
                break;
            case 21:
                boolean return21 = this.dsImplementation.hasDoubleCheckingEnded(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.DOUBLE_CHECKING_STATE_REQUEST.getMessageCode(),
                        return21);
                break;
            case 22:
                boolean return22 = this.dsImplementation.isRedistributionNeeded(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.REDISTRIBUTION_NEEDED_REQUEST.getMessageCode(),
                        return22);
                break;
            case 23:
                DominoesGameState return23 = this.dsImplementation.getGameState(inMessage.getPseudonym(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.GAME_STATE_FETCH_REQUEST.getMessageCode(),
                        return23);
                break;
        }
        return outMessage;
    }
}
