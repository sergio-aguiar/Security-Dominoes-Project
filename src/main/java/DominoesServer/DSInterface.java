package DominoesServer;

import DominoesCommunication.DMessage;
import DominoesCommunication.DMessageException;
import DominoesMisc.*;

import java.util.Stack;

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

        if (inMessage.getMessageType() >= 1 && (inMessage.getMessageType() <= 38 || inMessage.getMessageType() >= 43))
            if (inMessage.noCipheredSessionID())
                throw new DMessageException("Argument \"cipheredSessionID\" was not given", inMessage);

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
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
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
            case 24:
            case 26:
            case 27:
            case 28:
            case 31:
            case 32:
            case 33:
            case 35:
            case 36:
            case 37:
            case 38:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
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
                    throw new DMessageException("Argument \"pieceDif\" was not given", inMessage);
                break;
            case 17:
            case 29:
            case 30:
                if (inMessage.noFirstArgument())
                    throw new DMessageException("Argument \"tableID\" was not given", inMessage);
                if ((int) inMessage.getFirstArgument() < 0)
                    throw new DMessageException("Argument \"tableID\" was given an incorrect value", inMessage);
                if (inMessage.noSecondArgument())
                    throw new DMessageException("Argument \"commitData\" was not given", inMessage);
                break;
            case 20:
                if (inMessage.noFirstArgument())
                    throw new DMessageException("Argument \"tableID\" was not given", inMessage);
                if ((int) inMessage.getFirstArgument() < 0)
                    throw new DMessageException("Argument \"tableID\" was given an incorrect value", inMessage);
                if (inMessage.noSecondArgument())
                    throw new DMessageException("Argument \"piece\" was not given", inMessage);
                String[] splitArg1 = ((String) inMessage.getSecondArgument()).split("\\|");
                if (!inMessage.getSecondArgument().equals("None")
                        && (splitArg1.length != 2 || splitArg1[0].equals("") || splitArg1[1].equals("")))
                    throw new DMessageException("Argument \"piece\" was given an incorrect value", inMessage);
                break;
            case 25:
                if (inMessage.noFirstArgument())
                    throw new DMessageException("Argument \"tableID\" was not given", inMessage);
                if ((int) inMessage.getFirstArgument() < 0)
                    throw new DMessageException("Argument \"tableID\" was given an incorrect value", inMessage);
                if (inMessage.noSecondArgument())
                    throw new DMessageException("Argument \"targetEndPoint\" was not given", inMessage);
                if (!inMessage.getSecondArgument().equals("0")
                        && !inMessage.getSecondArgument().equals("1")
                        && !inMessage.getSecondArgument().equals("2")
                        && !inMessage.getSecondArgument().equals("3")
                        && !inMessage.getSecondArgument().equals("4")
                        && !inMessage.getSecondArgument().equals("5")
                        && !inMessage.getSecondArgument().equals("6")
                        && !inMessage.getSecondArgument().equals("First"))
                    throw new DMessageException("Argument \"targetEndPoint\" was given an incorrect value", inMessage);
                if (inMessage.noThirdArgument())
                    throw new DMessageException("Argument \"piece\" was not given", inMessage);
                String[] splitArg2 = ((String) inMessage.getThirdArgument()).split("\\|");
                if (!inMessage.getThirdArgument().equals("None")
                        && (splitArg2.length != 2 || splitArg2[0].equals("") || splitArg2[1].equals("")))
                    throw new DMessageException("Argument \"piece\" was given an incorrect value", inMessage);
                if (inMessage.noFourthArgument())
                    throw new DMessageException("Argument \"pieceEndPoint\" was not given", inMessage);
                if (!inMessage.getFourthArgument().equals("0")
                        && !inMessage.getFourthArgument().equals("1")
                        && !inMessage.getFourthArgument().equals("2")
                        && !inMessage.getFourthArgument().equals("3")
                        && !inMessage.getFourthArgument().equals("4")
                        && !inMessage.getFourthArgument().equals("5")
                        && !inMessage.getFourthArgument().equals("6"))
                    throw new DMessageException("Argument \"pieceEndPoint\" was given an incorrect value", inMessage);
                break;
            case 34:
                if (inMessage.noFirstArgument())
                    throw new DMessageException("Argument \"tableID\" was not given", inMessage);
                if ((int) inMessage.getFirstArgument() < 0)
                    throw new DMessageException("Argument \"tableID\" was given an incorrect value", inMessage);
                if (inMessage.noSecondArgument())
                    throw new DMessageException("Argument \"decision\" was not given", inMessage);
                break;
            default:
                throw new DMessageException("Invalid message type: " + inMessage.getMessageType());
        }

        switch(inMessage.getMessageType())
        {
            case 1:
                int return1 = this.dsImplementation.createTable(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.CREATE_TABLE_REQUEST.getMessageCode(), return1);
                break;
            case 2:
                DominoesTableInfo[] return2 = this.dsImplementation.listAvailableTables(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID());
                outMessage = new DMessage(DMessage.MessageType.LIST_TABLES_REQUEST.getMessageCode(), return2);
                break;
            case 3:
                boolean return3 = this.dsImplementation.joinTable(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.JOIN_TABLE_REQUEST.getMessageCode(), return3);
                break;
            case 4:
                int return4 = this.dsImplementation.joinRandomTable(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID());
                outMessage = new DMessage(DMessage.MessageType.JOIN_RANDOM_TABLE_REQUEST.getMessageCode(), return4);
                break;
            case 5:
                DominoesTableInfo return5 = this.dsImplementation.listTableInfo(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.LIST_TABLE_INFO_REQUEST.getMessageCode(), return5);
                break;
            case 6:
                this.dsImplementation.disbandTable(inMessage.getPseudonym(), inMessage.getCipheredSessionID(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.DISBAND_TABLE_REQUEST.getMessageCode(), (Object) null);
                break;
            case 7:
                this.dsImplementation.leaveTable(inMessage.getPseudonym(), inMessage.getCipheredSessionID(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.LEAVE_TABLE_REQUEST.getMessageCode(), (Object) null);
                break;
            case 8:
                boolean return8 = this.dsImplementation.startGame(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.START_GAME_REQUEST.getMessageCode(), return8);
                break;
            case 9:
                boolean return9 = this.dsImplementation.markAsReady(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.MARK_AS_READY_REQUEST.getMessageCode(), return9);
                break;
            case 10:
                boolean return10 = this.dsImplementation.isPlayerTurn(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.TURN_CHECK_REQUEST.getMessageCode(), return10);
                break;
            case 11:
                boolean return11 = this.dsImplementation.hasGameEnded(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.GAME_STATE_REQUEST.getMessageCode(), return11);
                break;
            case 12:
                boolean return12 = this.dsImplementation.isDeckSorting(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.DISTRIBUTION_STATE_REQUEST.getMessageCode(), return12);
                break;
            case 13:
                boolean return13 = this.dsImplementation.canDraw(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.CAN_DRAW_REQUEST.getMessageCode(), return13);
                break;
            case 14:
                DominoesDeck return14 = this.dsImplementation.getDeck(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.DECK_FETCH_REQUEST.getMessageCode(), return14);
                break;
            case 15:
                boolean return15 = this.dsImplementation.returnDeck(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument(),
                        (DominoesDeck) inMessage.getSecondArgument(), (byte[]) inMessage.getThirdArgument());
                outMessage = new DMessage(DMessage.MessageType.DECK_RETURN_REQUEST.getMessageCode(), return15);
                break;
            case 16:
                this.dsImplementation.skipTurn(inMessage.getPseudonym(), inMessage.getCipheredSessionID(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.SKIP_TURN_REQUEST.getMessageCode(), (Object) null);
                break;
            case 17:
                boolean return17 = this.dsImplementation.commitHand(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument(),
                        (DominoesCommitData) inMessage.getSecondArgument());
                outMessage = new DMessage(DMessage.MessageType.COMMIT_HAND_REQUEST.getMessageCode(), return17);
                break;
            case 18:
                boolean return18 = this.dsImplementation.hasPlayerCommitted(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.COMMIT_STATE_REQUEST.getMessageCode(), return18);
                break;
            case 19:
                boolean return19 = this.dsImplementation.isHandlingStart(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.START_HANDLING_STATE_REQUEST.getMessageCode(), return19);
                break;
            case 20:
                this.dsImplementation.stateHighestDouble(inMessage.getPseudonym(), inMessage.getCipheredSessionID(),
                        (int) inMessage.getFirstArgument(), (String) inMessage.getSecondArgument());
                outMessage = new DMessage(DMessage.MessageType.DOUBLE_STATING_REQUEST.getMessageCode(), (Object) null);
                break;
            case 21:
                boolean return21 = this.dsImplementation.hasDoubleCheckingEnded(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.DOUBLE_CHECKING_STATE_REQUEST.getMessageCode(),
                        return21);
                break;
            case 22:
                boolean return22 = this.dsImplementation.isRedistributionNeeded(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.REDISTRIBUTION_NEEDED_REQUEST.getMessageCode(),
                        return22);
                break;
            case 23:
                DominoesGameState return23 = this.dsImplementation.getGameState(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.GAME_STATE_FETCH_REQUEST.getMessageCode(),
                        return23);
                break;
            case 24:
                boolean return24 = this.dsImplementation.isResetNeeded(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.RESET_NEEDED_REQUEST.getMessageCode(), return24);
                break;
            case 25:
                boolean return25 = this.dsImplementation.playPiece(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument(),
                        (String) inMessage.getSecondArgument(), (String) inMessage.getThirdArgument(),
                        (String) inMessage.getFourthArgument());
                outMessage = new DMessage(DMessage.MessageType.PLAY_PIECE_REQUEST.getMessageCode(), return25);
                break;
            case 26:
                String return26 = this.dsImplementation.drawPiece(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.DRAW_PIECE_REQUEST.getMessageCode(), (Object) return26);
                break;
            case 27:
                this.dsImplementation.denounceCheating(inMessage.getPseudonym(), inMessage.getCipheredSessionID(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.DENOUNCE_CHEATING_REQUEST.getMessageCode(),
                        (Object) null);
                break;
            case 28:
                boolean return28 = this.dsImplementation.isHandlingCheating(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.CHEAT_HANDLING_STATE_REQUEST.getMessageCode(), return28);
                break;
            case 29:
                boolean return29 = this.dsImplementation.updateCommitment(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument(),
                        (DominoesCommitData) inMessage.getSecondArgument());
                outMessage = new DMessage(DMessage.MessageType.COMMIT_UPDATE_REQUEST.getMessageCode(), return29);
                break;
            case 30:
                boolean return30 = this.dsImplementation.sendCommitData(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument(),
                        (DominoesCommitData) inMessage.getSecondArgument());
                outMessage = new DMessage(DMessage.MessageType.SEND_COMMIT_DATA_REQUEST.getMessageCode(), return30);
                break;
            case 31:
                boolean return31 = this.dsImplementation.hasSentCommitData(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.HAS_SENT_COMMIT_REQUEST.getMessageCode(), return31);
                break;
            case 32:
                boolean return32 = this.dsImplementation.isHandlingAccounting(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.IS_HANDLING_ACCOUNT_REQUEST.getMessageCode(), return32);
                break;
            case 33:
                DominoesAccountingInfo return33 = this.dsImplementation.getAccountingInfo(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.ACCOUNTING_INFO_REQUEST.getMessageCode(), return33);
                break;
            case 34:
                boolean return34 = this.dsImplementation.sendAccountingDecision(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument(),
                        (boolean) inMessage.getSecondArgument());
                outMessage = new DMessage(DMessage.MessageType.ACCOUNTING_DECISION_REQUEST.getMessageCode(), return34);
                break;
            case 35:
                boolean return35 = this.dsImplementation.allSentDecision(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.ALL_DECISIONS_REQUEST.getMessageCode(), return35);
                break;
            case 36:
                boolean return36 = this.dsImplementation.allAgreedToAccounting(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.ALL_AGREED_REQUEST.getMessageCode(), return36);
                break;
            case 37:
                this.dsImplementation.passedProtestMenu(inMessage.getPseudonym(), inMessage.getCipheredSessionID(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.PROTEST_PASSED_REQUEST.getMessageCode(), (Object) null);
                break;
            case 38:
                boolean return38 = this.dsImplementation.allPassedProtestMenu(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.PROTEST_CHECK_REQUEST.getMessageCode(), return38);
                break;
            case 39:
                boolean return39 = this.dsImplementation.isUserRegistered(inMessage.getPseudonym());
                outMessage = new DMessage(DMessage.MessageType.CHECK_USER_REGISTER_REQUEST.getMessageCode(), return39);
                break;
            case 40:
                boolean return40 = this.dsImplementation.registerUser(inMessage.getPseudonym());
                outMessage = new DMessage(DMessage.MessageType.REGISTER_USER_REQUEST.getMessageCode(), return40);
                break;
            case 41:
                byte[] return41 = this.dsImplementation.greetServer(inMessage.getPseudonym(),
                        (byte[]) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.GREET_SERVER_REQUEST.getMessageCode(), return41);
                break;
            case 42:
                byte[] return42 = this.dsImplementation.getServerPublicKey();
                outMessage = new DMessage(DMessage.MessageType.SERVER_KEY_REQUEST.getMessageCode(), return42);
                break;
            case 43:
                byte[] return43 = this.dsImplementation.sendSessionID(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID());
                outMessage = new DMessage(DMessage.MessageType.SESSION_ID_SEND_REQUEST.getMessageCode(), return43);
                break;
            case 44:
                boolean return44 = this.dsImplementation.hasKeySortingStarted(inMessage.getPseudonym(),
                    inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.HAS_KEY_SORTING_STARTED_REQUEST.getMessageCode(),
                        return44);
                break;
            case 45:
                this.dsImplementation.startKeySorting(inMessage.getPseudonym(), inMessage.getCipheredSessionID(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.START_KEY_SORTING_REQUEST.getMessageCode(),
                        (Object) null);
                break;
            case 46:
                boolean return46 = this.dsImplementation.hasKeySortingEnded(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.HAS_KEY_SORTING_ENDED_REQUEST.getMessageCode(),
                        return46);
                break;
            case 47:
                byte[][] return47 = this.dsImplementation.getPlayerPublicKeys(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.PLAYER_PUBLIC_KEYS_REQUEST.getMessageCode(), return47);
                break;
            case 48:
                DominoesSymKeyMatrix return48 = this.dsImplementation.getSymKeyDistributionMatrix(
                        inMessage.getPseudonym(), inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.KEY_DISTRIBUTION_MATRIX_REQUEST.getMessageCode(),
                        return48);
                break;
            case 49:
                boolean return49 = this.dsImplementation.returnSymKeyDistributionMatrix(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument(),
                        (DominoesSymKeyMatrix) inMessage.getSecondArgument());
                outMessage = new DMessage(DMessage.MessageType.RETURN_KEY_DISTRIBUTION_MATRIX_REQUEST.getMessageCode(),
                        return49);
                break;
            case 50:
                byte[][] return50 = this.dsImplementation.getSessionSymKeys(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.SESSION_SYM_KEYS_REQUEST.getMessageCode(), return50);
                break;
            case 51:
                boolean return51 = this.dsImplementation.hasDeckBeenProtected(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.HAS_DECK_BEEN_PROTECTED_REQUEST.getMessageCode(),
                        return51);
                break;
            case 52:
                boolean return52 = this.dsImplementation.sendDeckProtectionPrivateKey(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument(),
                        (byte[]) inMessage.getSecondArgument());
                outMessage = new DMessage(DMessage.MessageType.SEND_DECK_PROTECTION_REQUEST.getMessageCode(), return52);
                break;
            case 53:
                this.dsImplementation.notifyDeckProtected(inMessage.getPseudonym(), inMessage.getCipheredSessionID(),
                        (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.NOTIFY_DECK_PROTECTED_REQUEST.getMessageCode(),
                        (Object) null);
                break;
            case 54:
                boolean return54 = this.dsImplementation.haveAllSentDeckProtectionPrivateKeys(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.HAVE_ALL_SENT_DECK_PROTECTION_REQUEST.getMessageCode(),
                        return54);
                break;
            case 55:
                boolean return55 = this.dsImplementation.hasSentDeckProtectionPrivateKey(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.HAS_SENT_DECK_PROTECTION_REQUEST.getMessageCode(),
                        return55);
                break;
            case 56:
                Stack<byte[]> return56 = this.dsImplementation.getDeckProtectionKeyStack(inMessage.getPseudonym(),
                        inMessage.getCipheredSessionID(), (int) inMessage.getFirstArgument());
                outMessage = new DMessage(DMessage.MessageType.DECK_PROTECTION_KEY_STACK_REQUEST.getMessageCode(),
                        return56);
                break;
        }
        return outMessage;
    }
}
