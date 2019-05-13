/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package markovhigher;
import java.util.*;
import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Marta Albecka, Pawel Karpinski, Piotr Lecki
 */

public class Markov {
    
    private static final Random rand = new Random();
    private static Matrix matrix;

    /**
     * Predict new element in set based on >memory< number of previous elements.
     * Each element has a chance to be chosen proportional to the number of times that it occurred in the entry set
     * after elements in prev list in matrix, which means that only elements that occurred at least one time can be
     * chosen.
     */
    private static Note nextPrediction() {
        Note nextObj = null;
        int sum = 0;
        int nextElementRand = Math.abs(rand.nextInt()) % matrix.getCounterSum();
        int counterListSize = matrix.getElement().getCounterList().size();
        for (int i = 0; i < counterListSize; i++) {
            if (sum <= nextElementRand && matrix.getElement().getCounter(i) != 0)
                nextObj = matrix.getElement().get(i);
            sum += matrix.getElement().getCounter(i);
        }

        if(nextObj == null)
            throw new IllegalStateException("Couldn't find next value");

        return nextObj;
    }

    /**
     * Predicts new values based on MEMORY number of previous elements.
     * Starts prediction from the beginning - starting collection has only null elements.
     *
     * @param notes    entry set of elements
     * @param MEMORY    number of previous elements that need to be taken into account
     * @param PRED_SIZE number of elements to be predicted
     * @return collection of new values
     * @see Matrix#memory
     */
    public static Vector<Note> markov(NotesCollection notes, int MEMORY, int PRED_SIZE) {
        //creating matrix and setting up counters
        matrix = Matrix.create(MEMORY);
        for (Note value : notes) {
            matrix.add(value);
        }
        //prediction part
        Vector<Note> ret = new Vector<>(PRED_SIZE);

        matrix.clearPrev();

        for (int i = 0; i < PRED_SIZE; i++) {
            Note nextPrediction = nextPrediction();
            matrix.updatePrevWith(nextPrediction);
            ret.add(nextPrediction);
        }
        return ret;
    }
    /**
     * Calling a method to save new values to a midi file
     * @param markovNotes   predicted collection of elements
     * @param pattern        string with the name of a file with patterns to create ne file
     * @param out           string with a name of a file to save new values
     * @throws InvalidMidiDataException
     * @throws IOException 
     */
    public static void saveAsMidi(Vector<Note> markovNotes, String pattern, String out) throws InvalidMidiDataException, IOException {
        saveAsMidi(markovNotes, new File(pattern), new File(out));
    }
    
    /**
     * Saves predicted collection of new elements as midi File.
     * @param markovNotes    predicted collection of elements
     * @param pattern        midi file with patterns to create ne file
     * @param out           file to save new values
     * no return value
     */
    public static void saveAsMidi(Vector<Note> markovNotes, File pattern , File out) throws InvalidMidiDataException, IOException {
        Sequence patternSequence = MidiSystem.getSequence(pattern);
        Sequence in = new Sequence(patternSequence.getDivisionType(), patternSequence.getResolution(), 1);
        MidiEvent event;
        long tick = 0;
        for (Note note : markovNotes) {
            tick += note.getDistanceFromPrevious();
            event = new MidiEvent(note.getMessage(), tick);
            in.getTracks()[0].add(event);

            ShortMessage mess = note.getMessage();
            ShortMessage endingMessage = new ShortMessage(mess.getStatus(), mess.getData1(), 0);
            event = new MidiEvent(endingMessage, tick + note.getDuration());
            in.getTracks()[0].add(event);
        }
        MidiSystem.write(in, MidiSystem.getMidiFileTypes(in)[0], out);
    }
    
}