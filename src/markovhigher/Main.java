package markovhigher;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.sound.midi.InvalidMidiDataException;

/**
 *
 * @author Marta Albecka, Pawel Karpinski, Piotr Lecki
 */
public class Main {
    private static final int PRED_SIZE = 200;
    private static final int MEMORY =200;

    public static void main(String[] args) throws InvalidMidiDataException, IOException {

        final NotesCollection notes = new NotesCollection("beatles-imagine.mid");
        NotesCollection subNotes = notes;
      subNotes.forEach(note ->
                System.out.println(note/*"key = " + note.getKey() + " velocity = " + note.getVelocity()*/));
        
        
        System.out.println("\n new  \n");
        
        Vector<Note> markovNotes = Markov.markov(subNotes, MEMORY, PRED_SIZE);

        Markov.saveAsMidi(markovNotes, "beatles-imagine.mid", "imagineall.mid");
         for(Note note : markovNotes) {
            System.out.println("key " + note.getKey() + " duration " + note.getDuration() + " " +
                    "distanceFromPrevious " + note.getDistanceFromPrevious());
        }
        System.out.print("Prediction finished");
    }
}